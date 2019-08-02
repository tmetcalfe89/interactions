# interactions
Add (right-click) interactions to Minecraft.

This is a simple mod to add interactions on right-click. It is intended for use by modpack developers and does nothing out of the box.

It adds a folder in the Minecraft directory called "interactions". Create a JSON file in there with any name you'd like and add entries matching the following:

```
[
  {
    "targetBlock": "<namespace>:<block>:<metadata>", // The block that is interacted with must match this to activate this block.
    "handItem": "<namespace>:<item>:<metadata>",     // The player must have this item in their hand.
    "changeBlock": "<namespace>:<block>:<metadata>", // If this interaction changes the target block, it changes into this. (optional)
    "changeChance": "<x>:<y>",                       // If this interaction changes the target block, it has x in y chances to do so. (optional)
    "dropItem": "<namespace>:<item>:<metadata>",     // If this interaction drops an item, it drops this. (optional)
    "dropChance": "<x>:<y>",                         // If this interaction drops an item, it has x in y chances to do so. (optional)
    "damage": "<x>",                                 // If this interaction damages the hand item, it does this much damage. (optional)
    "damageChance": "<x>:<y>"                        // If this interaction damages the hand item, it has x in y chances to do so. (optional)
  }
]

```

# Examples

Here's an example that turns dirt into coarse dirt when a player interacts with it with a stick in their hand.

```
[
  {
    "targetBlock": "minecraft:dirt:0",
    "handItem": "minecraft:stick:1",
    "changeBlock": "minecraft:dirt:1"
  }
]

```

Here's an example that drops a diamond from diamond ore and turns the ore into stone when a player interacts with it with a diamond pickaxe in their hand. The pickaxe takes 1 damage.

```
[
  {
    "targetBlock": "minecraft:diamond_ore:0",
    "handItem": "minecraft:diamond_pickaxe:0",
    "changeBlock": "minecraft:stone:0"
  }
]

```

# Pros

* Easy to use.
* Damage works with both damageable items (in which case it damages the item) and undamageable items (in which case it consumes items in the stack).
* Works with mod items and blocks.
* Works with damage/metadata.

# Cons

* Doesn't hook in with other APIs.
* Doesn't work with ore dictionaries.
* Doesn't work with tool levels. For example, you can't have say an iron pickaxe is required, and then have it automatically know diamond works as well.

# Notes

* Damage on handItem is ignored if the item is damageable when checking if it should trigger the interaction. You've still gotta put it in there.
* Cons are subject to improvement.