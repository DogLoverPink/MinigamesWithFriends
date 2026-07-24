# Randomizer

> **Randomizer is a modifier, not a gamemode.** Enable it with `/mg EnableModifier Randomizer`.

The randomizer modifier makes it so that when a block is mined, instead of dropping its normal drop, it will instead drop a completely random item out of all items in the game. Each type block is assigned a different drop, so mining the same type of block will always drop the same random item each game. No points are gained from this modifier, so it is designed to be layered on top of a gamemode rather than played on its own.

## Config
Accessed with `/mg ConfigModifier Randomizer [config key] [config value]`

| Config Key                   | Description                                                                      | Type    | Default / Recommended |
|------------------------------|----------------------------------------------------------------------------------|---------|-----------------------|
| `RerandomizeAfterDeathMatch` | If true, all block drop mappings will be rerandomized after a deathmatch ends.   | Boolean | `false`               |
| `RandomlyEnchantGear`        | If true, gear obtained through randomized drops may receive random enchantments. | Boolean | `true`                |
