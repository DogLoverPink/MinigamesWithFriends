## Configuration guide
Configuration is handled separately for each gamemode and modifier, and is accomplished with one of three commands, depending on what you're configuring.

### Usage
| Command                                                          | Configures                                                          |
|------------------------------------------------------------------|---------------------------------------------------------------------|
| `/mg ConfigMainGame [config key] [config value]`                 | Config command for the game as a whole                              |
| `/mg ConfigGamemode <gamemode name> [config key] [config value]` | Config command for a gamemodes (ex: BlockShuffle, DeathSwap)        |
| `/mg ConfigModifier <modifier name> [config key] [config value]` | Config command for modifiers (ex: Randomizer, AllBlocksHaveGravity) |


If the \[config key\] field is left blank, it will show all the available config options for that gamemode/modifier, and their current values.
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

## Main game configuration

### Main Game (applies to everything)
Accessed with `/mg ConfigMainGame [config key] [config value]`

| Config Key                             | Description                                                                   | Type    | Default/Recomended                                                                                                  |
|----------------------------------------|-------------------------------------------------------------------------------|---------|---------------------------------------------------------------------------------------------------------------------|
| PointsToWin                            | The number of points needed for a player to have won the game.                | Integer | Highly depends on game mode and player count, and how long you want the game to be. Start with between 3-5 perhaps. |
| PointsPerDeathmatchWin                 | The number of points that is awarded when a player wins a deathmatch.         | Integer | 1                                                                                                                   |
| KeepInventoryOnDeath                   | Set to true to if you want players to not lose their inventories on death.    | Boolean | false                                                                                                               |
| SetToDayOnGameStart                    | Set to true to set the time to day when the game starts.                      | Boolean | true                                                                                                                |
| ResetAdvancementsOnGameStart           | Set to true to reset every player's advancements when the starts.             | Boolean | true                                                                                                                |
| TeleportPlayersToWorldSpawnOnGameStart | Set to true to teleport every player to the world spawn when the game starts. | Boolean | true                                                                                                                |

## Gamemode specific configuration

### Block Shuffle
Accessed with `/mg ConfigGamemode BlockShuffle [config key] [config value]`

| Config Key                     | Description                                                                                                                                                            | Type    | Default/Recomended |
|--------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------|--------------------|
| `MinimumSecondsBeforeShuffle`  | The minimum number of seconds before the next block shuffle occurs. A random value between this and `MaximumSecondsBeforeShuffle` will be selected each round.         | Integer | `60`               |
| `MaximumSecondsBeforeShuffle`  | The maximum number of seconds before the next block shuffle occurs. Must be greater than or equal to `MinimumSecondsBeforeShuffle`.                                    | Integer | `180`              |
| `PointsPerSuccessfulBlockStep` | The number of points awarded when a player successfully steps on their assigned block.                                                                                 | Integer | `1`                |
| `ShuffleBlocksPerPlayer`       | If true, each player receives their own unique target block. If false, all players must find the same block.                                                           | Boolean | `false`            |
| `GivePointsAtEndOfRound`       | If true, points are awarded at the end of the round to players who managed to find their block. If false, points are awarded immediately when the block is stepped on. | Boolean | `false`            |
| `AllowNetherBlocks`            | If false, blocks exclusive to the nether will not appear as a target block.                                                                                            | Boolean | `false`            |

### DeathSwap
Accessed with `/mg ConfigGamemode DeathSwap [config key] [config value]`

| Config Key                        | Description                                                                                                                                               | Type    | Default / Recommended |
|-----------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------|---------|-----------------------|
| `MinimumSecondsBeforeSwap`        | The minimum number of seconds before the next player swap occurs. A random value between this and `MaximumSecondsBeforeSwap` will be selected each cycle. | Integer | `60`                  |
| `MaximumSecondsBeforeSwap`        | The maximum number of seconds before the next player swap occurs. Must be greater than or equal to `MinimumSecondsBeforeSwap`.                            | Integer | `180`                 |
| `PointsPerImpressiveKill`         | The number of points awarded for an "impressive" swap kill (ex: ).                                                                                        | Integer | `2`                   |
| `PointsPerLameKill`               | The number of points awarded for a "lame" kill caused by basic environmental damage such as lava, void, or fall damage immediately after a swap.          | Integer | `1`                   |
| `KeepInventoryOnSwapRelatedDeath` | If true, players will keep their inventory when they die as a direct result of a swap. If false, normal death item drops will occur.                      | Boolean | `true`                |

### Deathmatch
Accessed with `/mg ConfigGamemode Deathmatch [config key] [config value]`

| Config Key                       | Description                                                                                                                                          | Type    | Default / Recommended |
|----------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------|---------|-----------------------|
| `MinimumSecondsBeforeDeathMatch` | The minimum number of seconds before deathmatch begins. A random value between this and `MaximumSecondsBeforeDeathMatch` will be selected each game. | Integer | `300`                 |
| `MaximumSecondsBeforeDeathMatch` | The maximum number of seconds before deathmatch begins. Must be greater than or equal to `MinimumSecondsBeforeDeathMatch`.                           | Integer | `300`                 |
| `DeathmatchAreaRadiusBlocks`     | The radius (in blocks) of the deathmatch arena from its center point.                                                                                | Integer | `35`                  |

### DimensionSwap
Accessed with `/mg ConfigGamemode DimensionSwap [config key] [config value]`

| Config Key                            | Description                                                                                                                                           | Type    | Default / Recommended |
|---------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------|---------|-----------------------|
| `MinimumSecondsBeforeSwap`            | The minimum number of seconds before a dimension swap occurs. A random value between this and `MaximumSecondsBeforeSwap` will be selected each round. | Integer | `60`                  |
| `MaximumSecondsBeforeSwap`            | The maximum number of seconds before a dimension swap occurs. Must be greater than or equal to `MinimumSecondsBeforeSwap`.                            | Integer | `180`                 |
| `NumberOfSwaps`                       | The number of dimension swaps that occur before a DeathMatch round begins.                                                                            | Integer | `2`                   |
| `SendPlayersToMainWorldAfterGameEnds` | If true, players will be teleported back to the main world when the game ends.                                                                        | Boolean | `false`               |

### WouldYouRather
Accessed with `/mg ConfigGamemode WouldYouRather [config key] [config value]`

| Config Key                                 | Description                                                                                                                                                              | Type    | Default / Recommended |
|--------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------|-----------------------|
| `MinimumSecondsBeforeNewChoice`            | The minimum number of seconds before a new Would You Rather prompt appears. A random value between this and `MaximumSecondsBeforeNewChoice` will be selected each cycle. | Integer | `60`                  |
| `MaximumSecondsBeforeNewChoice`            | The maximum number of seconds before a new "Would You Rather" prompt appears. Must be greater than or equal to `MinimumSecondsBeforeNewChoice`.                          | Integer | `180`                 |
| `AllocatedSecondsForChoosingOption`        | The amount of time players have to select their choice.                                                                                                                  | Integer | `25`                  |
| `PreventMovingDuringChoiceSelection`       | If true, players will be unable to move while selecting their choice                                                                                                     | Boolean | `true`                |
| `ApplyDamageImmunityDuringChoiceSelection` | If true, players can't take damage while selecting their choice                                                                                                          | Boolean | `true`                |
| `StartGameWithAChoicePrompt`               | If true, players are given a Would You Rather prompt at the start of the game.                                                                                           | Boolean | `true`                |

## Modifier specific configuration

### Randomizer
Accessed with `/mg ConfigModifier Randomizer [config key] [config value]`

| Config Key                   | Description                                                                      | Type    | Default / Recommended |
|------------------------------|----------------------------------------------------------------------------------|---------|-----------------------|
| `RerandomizeAfterDeathMatch` | If true, all block drop mappings will be rerandomized after a deathmatch ends.   | Boolean | `false`               |
| `RandomlyEnchantGear`        | If true, gear obtained through randomized drops may receive random enchantments. | Boolean | `true`                |

### AllBlocksHaveGravity
Accessed with `/mg ConfigModifier AllBlocksHaveGravity [config key] [config value]`

Most of these options tune the performance/appearance trade-off. If this modifier is causing lag, the settings with the largest impact are `HorizontalEffectRadius`, `VerticalEffectRadius`, and `MaxConcurrentFallingBlockEntities`. See the [AllBlocksHaveGravity page](AllBlocksHaveGravity.md) for more detail.

| Config Key                           | Description                                                                                                                                              | Type    | Default / Recommended |
|--------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|---------|-----------------------|
| `HorizontalEffectRadius`             | How far out from each player, in blocks, the game looks for blocks that should fall.                                                                     | Integer | `10`                  |
| `VerticalEffectRadius`               | How far above and below each player, in blocks, the game looks for blocks that should fall.                                                              | Integer | `40`                  |
| `EnableInvisibleBlockCulling`        | If true, blocks that no player can see are not spawned as falling block entities. They still land exactly when and where they would have.                | Boolean | `true`                |
| `MaxBlockVisibilityDistance`         | Blocks further than this from every player are never drawn as falling entities. They still fall and land normally.                                       | Integer | `48`                  |
| `AlwaysVisibleRadiusBlocks`          | Block entities that spawn within this radius of any player will never be culled.                                                                         | Integer | `6`                   |
| `MaxConcurrentFallingBlockEntities`  | The most falling block entities allowed at once. When the cap is hit, the entities furthest from any player are retired first. Set to `-1` for no limit. | Integer | `5000`                |
| `MaxDroppedItemsPerWorld`            | The most dropped block items allowed on the ground per world. Oldest items are cleaned up first. Set to `-1` for no limit.                               | Integer | `500`                 |
| `ApplyPhysicsUpdatesToFallingBlocks` | If true, blocks that fall run phyiscs check on nearby blocks, meaning more vanilla behaviour is kept, at the cost of performance                         | Boolean | `false`               |
