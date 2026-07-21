# BlockShuffle

Blockshuffle is a gamemode where players are tasked with finding specific blocks around the world and stepping on them. Every x amount of time all players will be assigned a random block, from a list of all blocks that are obtainable in survival. Players who step on their assigned block are awarded a point. After all players find their block, or after time expires, a new block is chosen.

## Blockshuffle Command
Usage: `/mg blockshuffle (BanBlock/UnbanBlock/ListBannedBlocks/Skip) [Block]"`
<br>
>`/mg blockshuffle BanBlock BLOCK_NAME`
> <br>
> Bans a block from appearing as a option in blockshuffle. Use UPPER_SNAKE_CASE for the material name. (Ex. AMETHYST_BLOCK)


>`/mg blockshuffle UnbanBlock BLOCK_NAME`
> <br>
> Unbans an already banned block. Use UPPER_SNAKE_CASE for the material name. (Ex. AMETHYST_BLOCK)

> `/mg blockshuffle ListBannedBlocks`
> <br>
> Lists all of the blocks currently banned from blockshuffle.

> `/mg blockshuffle Skip`
> <br>
> Changes the current block that players are trying to find to another randomly generated one. Can only be used while in game.


## Config
Accessed with `/mg ConfigGamemode BlockShuffle [config key] [config value]`

| Config Key                     | Description                                                                                                                                                            | Type    | Default/Recomended |
|--------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------|--------------------|
| `MinimumSecondsBeforeShuffle`  | The minimum number of seconds before the next block shuffle occurs. A random value between this and `MaximumSecondsBeforeShuffle` will be selected each round.         | Integer | `60`               |
| `MaximumSecondsBeforeShuffle`  | The maximum number of seconds before the next block shuffle occurs. Must be greater than or equal to `MinimumSecondsBeforeShuffle`.                                    | Integer | `180`              |
| `PointsPerSuccessfulBlockStep` | The number of points awarded when a player successfully steps on their assigned block.                                                                                 | Integer | `1`                |
| `ShuffleBlocksPerPlayer`       | If true, each player receives their own unique target block. If false, all players must find the same block.                                                           | Boolean | `false`            |
| `GivePointsAtEndOfRound`       | If true, points are awarded at the end of the round to players who managed to find their block. If false, points are awarded immediately when the block is stepped on. | Boolean | `false`            |
| `AllowNetherBlocks`            | If false, blocks exclusive to the nether will not appear as a target block.                                                                                            | Boolean | `false`            |
