# DeathSwap

DeathSwap is a gamemode that involves players getting points by killing their opponents in creative ways. The overall premise is simple. Every x amount of time, each player is randomly assigned another player to swap with. Each player will then teleport to the position of their swapper, and their swapper will teleport to their own swapper as well. The point is that before someone is teleported to you, you put them a situation that will lead to them dying after the switch, such dying of fall damage, getting blown up by a creeper, or even drowning. Points are awarded for successful kills on the player that swaps to you, and the amount of points change slightly changes based on the method of death.

| Method of Killing | Points Earned |
|-------------------|---------------|
| Fall Damage       | 1             |
| Void Death        | 1             |
| Lava              | 1             |
| Anything Else     | 2             |
*Note that these are the default values specific in the starting config, but can be configured with the PointsPerImpressiveKill and PointsPerLameKill config option if desired.

Another thing to note is that by default, dying as a result of another player killing you during a swap will make you not lose your inventory, though this can be configured as well.

## Config
Accessed with `/mg ConfigGamemode DeathSwap [config key] [config value]`

| Config Key                        | Description                                                                                                                                               | Type    | Default / Recommended |
|-----------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------|---------|-----------------------|
| `MinimumSecondsBeforeSwap`        | The minimum number of seconds before the next player swap occurs. A random value between this and `MaximumSecondsBeforeSwap` will be selected each cycle. | Integer | `60`                  |
| `MaximumSecondsBeforeSwap`        | The maximum number of seconds before the next player swap occurs. Must be greater than or equal to `MinimumSecondsBeforeSwap`.                            | Integer | `180`                 |
| `PointsPerImpressiveKill`         | The number of points awarded for an "impressive" swap kill (ex: ).                                                                                        | Integer | `2`                   |
| `PointsPerLameKill`               | The number of points awarded for a "lame" kill caused by basic environmental damage such as lava, void, or fall damage immediately after a swap.          | Integer | `1`                   |
| `KeepInventoryOnSwapRelatedDeath` | If true, players will keep their inventory when they die as a direct result of a swap. If false, normal death item drops will occur.                      | Boolean | `true`                |