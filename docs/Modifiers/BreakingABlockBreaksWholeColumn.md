# BreakingABlockBreaksWholeColumn

> **BreakingABlockBreaksWholeColumn is a modifier, not a gamemode.** Enable it with `/mg EnableModifier BreakingABlockBreaksWholeColumn`.

Breaking any block also destroys every other block in the same vertical column (the blocks directly above and/or below it). Mine one block near the surface and you carve a shaft straight down to bedrock; the modifier turns ordinary digging into rapid, dramatic terrain destruction. No points are gained from this modifier, so it is designed to be layered on top of a gamemode.

Liquids caught in the column are simply drained rather than dropped as items.

## Config
Accessed with `/mg ConfigModifier BreakingABlockBreaksWholeColumn [config key] [config value]`

| Config Key       | Description                                                                                                | Type                                           | Default / Recommended |
|------------------|-----------------------------------------------------------------------------------------------------------|------------------------------------------------|-----------------------|
| `BreakDirection` | Which blocks in the column are destroyed. `Below` breaks only the blocks beneath the one you broke; `AboveAndBelow` breaks the entire column from the top of the world to the bottom. | Enum: `Below` / `AboveAndBelow`                | `AboveAndBelow`       |
| `AnimationMode`  | How the column's blocks are removed. See the table below.                                                  | Enum: `None` / `RapidBreaking` / `FallingBlocks` | `None`                |
| `DropItems`      | Whether the destroyed blocks drop their items. (Has no effect in `FallingBlocks` mode — see the note below.) | Boolean                                        | `true`                |

### Animation modes

| `AnimationMode`  | Effect                                                                                                                                                     |
|------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| `None`           | The whole column is destroyed instantly, with each block playing its normal break sound and particles.                                                    |
| `RapidBreaking`  | The column is destroyed one block at a time, top to bottom, one block per tick. This gives a visible cascade rather than everything vanishing at once.    |
| `FallingBlocks`  | Each block in the column turns into a falling block entity, so the column physically collapses downward. Be aware this is the most performance-heavy mode on tall columns, similar to [AllBlocksHaveGravity](AllBlocksHaveGravity.md). |

> **Note on `DropItems`:** in `FallingBlocks` mode the falling blocks always drop as items if they can't settle into place, so the `DropItems` setting only affects the `None` and `RapidBreaking` modes.
