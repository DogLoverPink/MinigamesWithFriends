# DimensionSwap

Dimension swap is a simple but unique gamemode that is yet to be seen in a minecraft plugin before. The premise is simple. Before the game starts, the server owner will gather an amount of minecraft worlds/maps, either downloaded from the internet, or hand built. Then throughout the game, every random interval of time each player will be sent to a random dimension/world, prioritizing the unexplored dimensions first. The goal of the game is explore each map and attempt to gather as many resources as possible. Every configured amount of swaps, players will instead have a deathmatch, with the winner of the deathmatch gaining points (your inventory will not be lost during this). This gamemode however does require additional one-time setup for the server owner, and a guide on what to do is below.

## Setup
Tip: try to get at minimum 2-3 maps per player for the best experience. (ex. if you are running a 5 player game, at minimum make sure to have 10-15 maps)

**This setup only needs to be done one time per server version\*! You do NOT have to redownload the maps or run the `PreLoadSavedDimensionSwapWorlds` command every time you want to play this game**
\*Everytime you update your server to a new major version of minecraft, it is recommeded to rerun the command

1. Download an assortment of maps from the internet. I highly recommend  [minecraftmaps.com](https://www.minecraftmaps.com/), but any other website such as [planetminecraft.com](https://www.planetminecraft.com/projects/) or [minecraft-schematics.com](https://www.minecraft-schematics.com/) will work as well. It doesn't really matter what kind of maps it is. Worlds from any minecraft version should work, and the worlds can be just builds, puzzle maps, or full-blown cities. Alternatively, if you have worlds that you've made yourself, you can use those as well.
2. Move all unzipped world folders into the directory `world/dimensions/minecraft/saveddimensionswapworlds`. All worlds in this folder must be unzipped (you must extract all .zip files), but having invalid or duplicate world names, or being improperly nested is fixed automatically by the plugin.
3. After, while the server is running, run the command `/mg dimensionswap PreLoadSavedDimensionSwapWorlds`. This command only needs to be run once, but will massively improve performance during the actual game. From a technical standpoint, this command loads and unloads every saved world, as updating worlds to higher versions is very slow and resource intensive, so doing it once beforehand instead of mid-game, every game, is far more efficient.
4. Restart your server.

### Recommended Gamemodes
DimensionSwap works great with basically any combination of gamemodes. I'd say the one that works the best is Blockshuffle, as going to new maps exposes you to a large variety of blocks. I would NOT recommend running this gamemode with the Deathmatch gamemode, as deathmatches are already built in.

## Config
Accessed with `/mg ConfigGamemode DimensionSwap [config key] [config value]`

| Config Key                            | Description                                                                                                                                           | Type    | Default / Recommended |
|---------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------|---------|-----------------------|
| `MinimumSecondsBeforeSwap`            | The minimum number of seconds before a dimension swap occurs. A random value between this and `MaximumSecondsBeforeSwap` will be selected each round. | Integer | `60`                  |
| `MaximumSecondsBeforeSwap`            | The maximum number of seconds before a dimension swap occurs. Must be greater than or equal to `MinimumSecondsBeforeSwap`.                            | Integer | `180`                 |
| `NumberOfSwaps`                       | The number of dimension swaps that occur before a DeathMatch round begins.                                                                            | Integer | `2`                   |
| `SendPlayersToMainWorldAfterGameEnds` | If true, players will be teleported back to the main world when the game ends.                                                                        | Boolean | `false`               |