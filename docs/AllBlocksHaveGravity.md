# AllBlocksHaveGravity

AllBlocksHaveGravity is an effect that makes every block* in the game susceptible to gravity, within the defined vertical and horizontal radius. This means for example that caves will collapse and trees will fall when you're next to them

\*Blocks that are indestructible, like bedrock and end portal frame, will not fall.
## Performance
This effect is VERY demanding in terms of both client fps and server lag, depending on the configured radii. There have been major measures taken to mitigate this effect, described below.

Implemented optimizations:
1. Culling of falling blocks hidden behind other blocks:
- Essentially, if a large cuboid of blocks fall at the same time, only the blocks on the outside are rendered, as the inner blocks wouldn't be visible to anyone. The block's falling placement logic still occurs.
2. Culling of blocks outside of any player's line of sight:
- Similar to the optimization above, any falling block that couldn't possibly be viewed by a player is culled as well.
3. Only rendering a maximum of n total falling blocks at the same time:
- This optimization means that there's a limit to how many falling blocks can be rendered at once (default 5000). If this limit is ever reached, falling blocks are that are furthest away from players are culled to make room for new falling blocks. (Keeping the placement logic, of course)
4. Item entity culling:
- Over time, the world will fill up with thousand of item entities dropped by blocks that fall weirdly. The plugin attempts to prune these excessive items, keeping them to a configurable number (default 500).

If you are running into performance issues, here's what I'd recommend.

1. Make sure `EnableInvisibleBlockCulling` is `true`
2. Lower `VerticalEffectRadius` or `HorizontalEffectRadius`. This will lead to slightly less cave ins, in a smaller radius, but will massively improve client (and server) lag.
3. Lower the `MaxConcurrentFallingBlockEntities`. Especially during very large collapses, (like the end islands for example), your client will be forced to render massive amounts of falling block entities. Lowering this setting will reduce the load on your client by a large amount, at the potential cost of visually not seeing every individual block fall. Lowering this to absurdly low values will remove client lag entirely, at the cost visually missing many falling blocks.
## Config
Accessed with `/mg ConfigModifier AllBlocksHaveGravity [config key] [config value]`

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