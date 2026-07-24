# Deathmatch

Deathmatch is a gamemode where, after a configurable amount of time, all players are teleported into a arena and forced to fight to the death. Players who die in the deathmatch keep their inventory, and the last player standing earns a point. The point of this gamemode is that during the downtime between deatchmatches, players scramble to get the best gear that they can, in order to give them the biggest advantage in combat.

## Config
Accessed with `/mg ConfigGamemode Deathmatch [config key] [config value]`

| Config Key                       | Description                                                                                                                                          | Type    | Default / Recommended |
|----------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------|---------|-----------------------|
| `MinimumSecondsBeforeDeathMatch` | The minimum number of seconds before deathmatch begins. A random value between this and `MaximumSecondsBeforeDeathMatch` will be selected each game. | Integer | `300`                 |
| `MaximumSecondsBeforeDeathMatch` | The maximum number of seconds before deathmatch begins. Must be greater than or equal to `MinimumSecondsBeforeDeathMatch`.                           | Integer | `300`                 |
| `DeathmatchAreaRadiusBlocks`     | The radius (in blocks) of the deathmatch arena from its center point.                                                                                | Integer | `35`                  |