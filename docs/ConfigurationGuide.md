## Configuration guide
Configuration for each gamemode is handled seperately, and can be accomplished with the `/mg config` commmand.

### Usage
`/mg config <gamemode name> [config key] [config value]`
<br>
If the \[config key\] field is left blank, it will show all the available config options for that specific gamemode, and their current values.
<br>
**Example:**
[image.jpg]
<br>
If the \[config value\] field is left blank, it will show the current value for that config key.
<br>
**Example:**
[image.jpg]
Config values can be updated on the fly during a game, and a changed value will be automatically updated without the need for stopping or restarting the game.

Also, you may notice that many gamemodes have config options for MinimumTime\[...\] and MaximumTime\[...\]. What this means is that a random value between the min and max will be chosen for the that time value. Ex: if MinTime is 60, and MaxTime is 90, that time period will be a random time between 1 minute and 1 minute & 30 seconds.

## Gamemode specific configuration

### Main Game (all gamemodes)
Accessed with `/mg config mainGame [config key] [config value]`

| Config Key                             | Description                                                                   | Type    | Default/Recomended                                                                                                  |
|----------------------------------------|-------------------------------------------------------------------------------|---------|---------------------------------------------------------------------------------------------------------------------|
| PointsToWin                            | The number of points needed for a player to have won the game.                | Integer | Highly depends on game mode and player count, and how long you want the game to be. Start with between 3-5 perhaps. |
| PointsPerDeathmatchWin                 | The number of points that is awarded when a player wins a deathmatch.         | Integer | 1                                                                                                                   |
| KeepInventoryOnDeath                   | Set to true to if you want players to not lose their inventories on death.    | Boolean | false                                                                                                               |
| SetToDayOnGameStart                    | Set to true to set the time to day when the game starts.                      | Boolean | true                                                                                                                |
| ResetAdvancementsOnGameStart           | Set to true to reset every player's advancements when the starts.             | Boolean | true                                                                                                                |
| TeleportPlayersToWorldSpawnOnGameStart | Set to true to teleport every player to the world spawn when the game starts. | Boolean | true                                                                                                                |

### Block Shuffle
Accessed with `/mg config blockShuffle [config key] [config value]`

| Config Key                     | Description                                                                                                                                                            | Type    | Default/Recomended |
|--------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------|--------------------|
| `MinimumSecondsBeforeShuffle`  | The minimum number of seconds before the next block shuffle occurs. A random value between this and `MaximumSecondsBeforeShuffle` will be selected each round.         | Integer | `60`               |
| `MaximumSecondsBeforeShuffle`  | The maximum number of seconds before the next block shuffle occurs. Must be greater than or equal to `MinimumSecondsBeforeShuffle`.                                    | Integer | `180`              |
| `PointsPerSuccessfulBlockStep` | The number of points awarded when a player successfully steps on their assigned block.                                                                                 | Integer | `1`                |
| `ShuffleBlocksPerPlayer`       | If true, each player receives their own unique target block. If false, all players must find the same block.                                                           | Boolean | `false`            |
| `GivePointsAtEndOfRound`       | If true, points are awarded at the end of the round to players who managed to find their block. If false, points are awarded immediately when the block is stepped on. | Boolean | `false`            |
| `AllowNetherBlocks`            | If false, blocks exclusive to the nether will not appear as a target block.                                                                                            | Boolean | `false`            |

### DeathSwap
Accessed with `/mg config deathswap [config key] [config value]`

| Config Key                        | Description                                                                                                                                               | Type    | Default / Recommended |
|-----------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------|---------|-----------------------|
| `MinimumSecondsBeforeSwap`        | The minimum number of seconds before the next player swap occurs. A random value between this and `MaximumSecondsBeforeSwap` will be selected each cycle. | Integer | `60`                  |
| `MaximumSecondsBeforeSwap`        | The maximum number of seconds before the next player swap occurs. Must be greater than or equal to `MinimumSecondsBeforeSwap`.                            | Integer | `180`                 |
| `PointsPerImpressiveKill`         | The number of points awarded for an "impressive" swap kill (ex: ).                                                                                        | Integer | `2`                   |
| `PointsPerLameKill`               | The number of points awarded for a "lame" kill caused by basic environmental damage such as lava, void, or fall damage immediately after a swap.          | Integer | `1`                   |
| `KeepInventoryOnSwapRelatedDeath` | If true, players will keep their inventory when they die as a direct result of a swap. If false, normal death item drops will occur.                      | Boolean | `true`                |

### Randomizer
Accessed with `/mg config randomizer [config key] [config value]`

| Config Key                   | Description                                                                      | Type    | Default / Recommended |
|------------------------------|----------------------------------------------------------------------------------|---------|-----------------------|
| `RerandomizeAfterDeathMatch` | If true, all block drop mappings will be rerandomized after a deathmatch ends.   | Boolean | `false`               |
| `RandomlyEnchantGear`        | If true, gear obtained through randomized drops may receive random enchantments. | Boolean | `true`                |

### Deathmatch
Accessed with `/mg config deathmatch [config key] [config value]`

| Config Key                       | Description                                                                                                                                          | Type    | Default / Recommended |
|----------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------|---------|-----------------------|
| `MinimumSecondsBeforeDeathMatch` | The minimum number of seconds before deathmatch begins. A random value between this and `MaximumSecondsBeforeDeathMatch` will be selected each game. | Integer | `300`                 |
| `MaximumSecondsBeforeDeathMatch` | The maximum number of seconds before deathmatch begins. Must be greater than or equal to `MinimumSecondsBeforeDeathMatch`.                           | Integer | `300`                 |
| `DeathmatchAreaRadiusBlocks`     | The radius (in blocks) of the deathmatch arena from its center point.                                                                                | Integer | `35`                  |

### DimensionSwap
Accessed with `/mg config dimensionswap [config key] [config value]`

| Config Key                            | Description                                                                                                                                           | Type    | Default / Recommended |
|---------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------|---------|-----------------------|
| `MinimumSecondsBeforeSwap`            | The minimum number of seconds before a dimension swap occurs. A random value between this and `MaximumSecondsBeforeSwap` will be selected each round. | Integer | `60`                  |
| `MaximumSecondsBeforeSwap`            | The maximum number of seconds before a dimension swap occurs. Must be greater than or equal to `MinimumSecondsBeforeSwap`.                            | Integer | `180`                 |
| `NumberOfSwaps`                       | The number of dimension swaps that occur before a DeathMatch round begins.                                                                            | Integer | `2`                   |
| `SendPlayersToMainWorldAfterGameEnds` | If true, players will be teleported back to the main world when the game ends.                                                                        | Boolean | `false`               |

### WouldYouRather
Accessed with `/mg config wouldyourather [config key] [config value]`

| Config Key                                 | Description                                                                                                                                                              | Type    | Default / Recommended |
|--------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------|-----------------------|
| `MinimumSecondsBeforeNewChoice`            | The minimum number of seconds before a new Would You Rather prompt appears. A random value between this and `MaximumSecondsBeforeNewChoice` will be selected each cycle. | Integer | `60`                  |
| `MaximumSecondsBeforeNewChoice`            | The maximum number of seconds before a new "Would You Rather" prompt appears. Must be greater than or equal to `MinimumSecondsBeforeNewChoice`.                          | Integer | `180`                 |
| `AllocatedSecondsForChoosingOption`        | The amount of time players have to select their choice.                                                                                                                  | Integer | `25`                  |
| `PreventMovingDuringChoiceSelection`       | If true, players will be unable to move while selecting their choice                                                                                                     | Boolean | `true`                |
| `ApplyDamageImmunityDuringChoiceSelection` | If true, players can't take damage while selecting their choice                                                                                                          | Boolean | `true`                |
| `StartGameWithAChoicePrompt`               | If true, players are given a Would You Rather prompt at the start of the game.                                                                                           | Boolean | `true`                |



