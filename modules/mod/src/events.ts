script.on_event(
    [defines.events.on_pre_build, defines.events.on_player_dropped_item],
    (e: OnPreBuildEvent | OnPlayerDroppedItemEvent) => {
      game.print("player " + e.player_index + " dropped item, tick:" + e.tick)
    }
);

script.on_event(
    defines.events.on_tick,
    (e: OnTickEvent) => {
      if (e.tick % 60 == 0) {
        game.print("test mod tick: " + e.tick)
      }
    }
);

script.on_load(() => {
  game.print("loaded Factorio Web Map!")
});
