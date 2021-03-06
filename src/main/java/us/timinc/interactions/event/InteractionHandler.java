package us.timinc.interactions.event;

import java.util.ArrayList;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import us.timinc.interactions.recipe.InteractRecipe;
import us.timinc.interactions.recipe.InteractRecipes;
import us.timinc.interactions.util.IdUtil;
import us.timinc.interactions.util.MinecraftUtil;
import us.timinc.interactions.util.PointUtil;
import us.timinc.interactions.util.RandUtil;

/**
 * Implements the logic in recipes matching events.
 * 
 * @author Tim
 *
 */
public class InteractionHandler {
	/**
	 * The recipes this handler handles.
	 */
	public InteractRecipes interactRecipes;

	/**
	 * Creates a new interaction handler. Initializes recipes.
	 */
	public InteractionHandler() {
		interactRecipes = new InteractRecipes();
	}

	/**
	 * Reloads the recipes.
	 */
	public void reload() {
		interactRecipes = new InteractRecipes();
	}

	/**
	 * Subscribes to the player right clicking a block. Checks that it's a valid
	 * event, then processes it.
	 * 
	 * @param event
	 *            The interaction event.
	 */
	@SubscribeEvent
	public void onInteraction(PlayerInteractEvent.RightClickBlock event) {
		if (!event.getEntityPlayer().canPlayerEdit(event.getPos(), event.getFace(),
				event.getEntityPlayer().getHeldItem(event.getHand())))
			return;

		processMatchinInteractions(event);
	}

	/**
	 * The core of the event processing. We look for an interact recipe that
	 * matches the event, check to see what it does, roll for the modifications
	 * it makes, and execute the changes.
	 * 
	 * @param event
	 *            The interaction event.
	 */
	private void processMatchinInteractions(RightClickBlock event) {
		// Find any recipes that match the event and process them.
		ArrayList<InteractRecipe> matches = interactRecipes.findMatches(event);
		matches.forEach(r -> processInteraction(r, event));
	}

	/**
	 * Process an interaction recipe given an event context.
	 * 
	 * @param recipe
	 *            The interaction recipe to process.
	 * @param event
	 *            The event context.
	 */
	private void processInteraction(InteractRecipe recipe, RightClickBlock event) {
		if (event.getWorld().isRemote) {
			processInteractionClient(recipe, event);
		} else {
			processInteractionServer(recipe, event);
		}
	}

	/**
	 * Process an interaction recipe given an event context on the server.
	 * 
	 * @param recipe
	 *            The interaction recipe to process.
	 * @param event
	 *            The event context.
	 */
	private void processInteractionServer(InteractRecipe recipe, RightClickBlock event) {
		World world = event.getWorld();
		BlockPos targetPosition = event.getPos();
		EntityPlayer player = event.getEntityPlayer();
		ItemStack heldItem = player.getHeldItem(event.getHand());

		// Roll for success.
		boolean success = recipe.rollForChangeBlock();

		// If the recipe changes the target block, roll for it, and do it if
		// successful.
		if (recipe.changesTargetBlock() && success) {
			world.setBlockState(targetPosition, recipe.getChangeBlockState());
		}

		// If the recipe drops an item from the target block, roll for it,
		// and do it if successful.
		if (recipe.dropsItem(success) && recipe.rollForDropItem()) {
			EntityItem itemDropEntity = MinecraftUtil.createEntityItem(world, event.getHitVec(), recipe.createDrop());

			world.spawnEntity(itemDropEntity);
		}

		// If the recipe damages the held item, roll for it, and do it if
		// successful.
		if (recipe.damagesHeldItem(success) && recipe.rollForDamageItem()) {
			MinecraftUtil.damageItemStack(heldItem, recipe.getDamage(), player);
		}
	}

	/**
	 * Process an interaction recipe given an event context on the client.
	 * 
	 * @param recipe
	 *            The interaction recipe to process.
	 * @param event
	 *            The event context.
	 */
	private void processInteractionClient(InteractRecipe recipe, RightClickBlock event) {
		World world = event.getWorld();
		BlockPos targetPosition = event.getPos();

		// If the recipe spaws particles, spawn them.
		if (recipe.spawnsParticles()) {
			int count = recipe.rollForParticleCount();
			for (int i = 0; i <= count; i++) {
				double[] particlePosition = getParticlePosition(targetPosition, recipe.getParticleArea());
				world.spawnParticle(EnumParticleTypes.getByName(recipe.getParticleName()), particlePosition[0],
						particlePosition[1], particlePosition[2], (float) Math.random() * 0.02D,
						(float) Math.random() * 0.02D, (float) Math.random() * 0.02D,
						recipe.getParticleParam().isEmpty() ? 0
								: Item.getIdFromItem(
										IdUtil.createItemStackFrom(recipe.getParticleParam(), 1).getItem()));
			}
		}
	}

	private double[] getParticlePosition(BlockPos targetPosition, String string) {
		double[] retval = new double[3];
		switch (string) {
		case "in":
			retval[0] = (double) targetPosition.getX() + Math.random();
			retval[1] = (double) targetPosition.getY() + Math.random();
			retval[2] = (double) targetPosition.getZ() + Math.random();
			break;
		case "out":
			int side = RandUtil.roll(0, 2);
			double[] sideFinder = { -0.1, 1.1 };
			retval[0] = (double) targetPosition.getX() + (side == 0 ? sideFinder[RandUtil.roll(0, 1)] : Math.random());
			retval[1] = (double) targetPosition.getY() + (side == 1 ? sideFinder[RandUtil.roll(0, 1)] : Math.random());
			retval[2] = (double) targetPosition.getZ() + (side == 2 ? sideFinder[RandUtil.roll(0, 1)] : Math.random());
			break;
		}
		return retval;
	}
}
