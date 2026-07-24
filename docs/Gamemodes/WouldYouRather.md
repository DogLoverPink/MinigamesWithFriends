# WouldYouRather

WouldYouRather is a gamemode where, every x amount of time, players are presented with a "Would you rather..." prompt containing two choices. Each choice pairs a beneficial effect with a detrimental effect. Players must pick one of the two options, applying both the good and bad effect attached to it. Over the course of the game, players accumulate an amount of buffs and debuffs that alter their gameplay. If a player fails to pick before time runs out, the first option is chosen automatically. No points are gained in this gamemode, and it works best when paired when the deathmatch gamemode. With that said, it is also very fun on its own if you are doing your own challenge, such as trying to beat the game with the gamemode active.

## Prompt Examples
<details>
  <summary>Example Prompt #1</summary>
  
  ![WYR Example Prompt #1](https://cdn.modrinth.com/data/cached_images/283af593f466316393f4b1a82490f6323693002c.png)
  </details>
  <details>
  <summary>Example Prompt  #2</summary>

  ![WYR Prompt Example #2](https://cdn.modrinth.com/data/cached_images/ca3976d7df64294e51485548d35c79bd0e9525b2.png)
  </details>
  <details>
  <summary>Example Prompt  #3</summary>

  ![WYR Example Prompt #3](https://cdn.modrinth.com/data/cached_images/3e04efa00f67611359ff82047ab3eefcfa069991.png)
  </details>

## WouldYouRather Command
Usage: `/mg wouldyourather (RemoveEffect/CheckActiveEffects/SendNewPrompt) [Player] [Effect]`
<br>
>`/mg wouldyourather RemoveEffect PLAYER EFFECT`
> <br>
> Removes an active Would You Rather effect from the specified player. The current effects that the player has will be shown in the command's tab completion.

>`/mg wouldyourather CheckActiveEffects [PLAYER]`
> <br>
> Lists all Would You Rather effects currently active on the specified player, or on yourself, if no player is provided.

>`/mg wouldyourather SendNewPrompt`
> <br>
> Immediately triggers a new "Would you rather" choice prompt for all players, regardless of the timer.


## Config
Accessed with `/mg ConfigGamemode WouldYouRather [config key] [config value]`

| Config Key                                 | Description                                                                                                                                                              | Type    | Default / Recommended |
|--------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------|-----------------------|
| `MinimumSecondsBeforeNewChoice`            | The minimum number of seconds before a new Would You Rather prompt appears. A random value between this and `MaximumSecondsBeforeNewChoice` will be selected each cycle. | Integer | `60`                  |
| `MaximumSecondsBeforeNewChoice`            | The maximum number of seconds before a new "Would You Rather" prompt appears. Must be greater than or equal to `MinimumSecondsBeforeNewChoice`.                          | Integer | `180`                 |
| `AllocatedSecondsForChoosingOption`        | The amount of time players have to select their choice.                                                                                                                  | Integer | `25`                  |
| `PreventMovingDuringChoiceSelection`       | If true, players will be unable to move while selecting their choice                                                                                                     | Boolean | `true`                |
| `ApplyDamageImmunityDuringChoiceSelection` | If true, players can't take damage while selecting their choice                                                                                                          | Boolean | `true`                |
| `StartGameWithAChoicePrompt`               | If true, players are given a Would You Rather prompt at the start of the game.                                                                                           | Boolean | `true`                |
