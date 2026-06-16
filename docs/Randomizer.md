# Randomizer

The randomizer gamemode makes it so that when a block is mined, instead of dropping its normal drop, it will instead drop a completely random item out of all items in the game. Each type block is assigned a different drop, so mining the same type of block will always drop the same random item each game. No points are gained from this gamemode, so it is recommended that you pair it with other gamemodes for a better experience.

## Config
Accessed with `/mg config randomizer [config key] [config value]`

| Config Key                   | Description                                                                      | Type    | Default / Recommended |
|------------------------------|----------------------------------------------------------------------------------|---------|-----------------------|
| `RerandomizeAfterDeathMatch` | If true, all block drop mappings will be rerandomized after a deathmatch ends.   | Boolean | `false`               |
| `RandomlyEnchantGear`        | If true, gear obtained through randomized drops may receive random enchantments. | Boolean | `true`                |

