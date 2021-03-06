# interactions
Add (right-click) interactions to Minecraft.

This is a mod to add interactions on right-click. It is intended for use by modpack developers and does nothing out of the box.

It adds a folder in the Minecraft directory called "interactions". Create a JSON file in there with any name you'd like and add entries matching the following:

```
[
  {
    "targetBlock": "<namespace>:<block>:<metadata>",      // The block that is interacted with must match this to activate this block.
    "heldItem": "<namespace>:<item>:<metadata>",          // The player must have this item in their hand.
    "offhandItem": "<namespace>:<item>:<metadata>",       // The player must have this item in their offhand. (optional)
    "replacementBlock": "<namespace>:<block>:<metadata>", // If this interaction replaces the target block, it replaces with this. (optional)
    "replacementChance": "<x>:<y>",                       // If this interaction replaces the target block, it has x in y chances to do so. (optional)
    "dropItem": "<namespace>:<item>:<metadata>",          // If this interaction drops an item, it drops this. (optional)
    "dropOnlyOnSuccess": "<true/false>",                  // If this interaction drops an item, setting this to true only allows the drop if block replacement was successful. (optional)
    "dropChance": "<x>:<y>",                              // If this interaction drops an item, it has x in y chances to do so. (optional)
    "dropCount": "<x>:<y>",                               // If this interactions drops an item, it drops between x and y (y can be left out for an exact amount). (optional)
    "damage": "<x>",                                      // If this interaction damages the hand item, it does this much damage. (optional)
    "damageOffhand": "<x>",                               // If this interaction damages the offhand item, it does this much damage. (optional)
    "damageOnlyOnSuccess": "<true/false>",                // If this interaction damages the hand item, setting this to true only allows the damage if block replacement was successful. (optional)
    "damageChance": "<x>:<y>",                            // If this interaction damages the hand item, it has x in y chances to do so. (optional),
    "particleType": "<particletype>:<parameter>",         // If this interaction emits particles, it emits these particles. (both parts optional),
    "particleArea": "<in/out>",                           // If this interaction emits particles, it emits the particles here in reference to the target block. "in" emits the particles inside the block, "out" emits them just slightly outside. (optional, defaults to "in")
    "particleCount": "<x>:<y>"                            // If this interaction emits particles, it emits between x and y (y can be left out for an exact amount). (optional)
  }
]

```

# Examples

Here's an example that turns dirt into coarse dirt when a player interacts with it with a stick in their hand.

```
[
  {
    "targetBlock": "minecraft:dirt:0",
    "heldItem": "minecraft:stick",
    "replacementBlock": "minecraft:dirt:1"
  }
]
```

Here's an example that drops a diamond from diamond ore and turns the ore into stone when a player interacts with it with a pickaxe in their hand. The pickaxe takes 1 damage.

```
[
  {
    "targetBlock": "minecraft:diamond_ore",
    "dropItem": "minecraft:diamond",
    "heldItem": "minecraft:*_pickaxe",
    "replacementBlock": "minecraft:stone",
    "damage": "1"
  }
]
```

Here's the above example, but with (lots of) cracking diamond ore particles.
Note: For reference, applying bonemeal creates 15 happyVillager particles.
```
[
  {
    "targetBlock": "minecraft:diamond_ore",
    "dropItem": "minecraft:diamond",
    "heldItem": "minecraft:diamond_pickaxe",
    "replacementBlock": "minecraft:stone",
    "damage": "1",
    "particleType": "iconcrack",
    "particleArea": "out",
    "particleCount": "30:60"
  }
]
```

Here's a goofy example showing off ore dictionary compatibility. Right-click a stone block with anything registered to the gemDiamond group and it'll turn into a diamond ore, consuming the item.
```
[
  {
    "targetBlock": "minecraft:stone",
    "heldItem": "ore:gemDiamond",
    "replacementBlock": "minecraft:diamond_ore",
    "damage": "1"
  }
]
```

# Pros

* Easy to use.
* Damage works with both damageable items (in which case it damages the item) and undamageable items (in which case it consumes items in the stack).
* Works with mod items and blocks.
* Works with damage/metadata.
* Works with ore dictionaries.

# Cons

* Doesn't hook in with other APIs.

# Notes

* Damage on heldItem is ignored if the item is damageable when checking if it should trigger the interaction.
* Cons are subject to improvement.
