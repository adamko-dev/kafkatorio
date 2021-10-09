script.on_event(
    [defines.events.on_pre_build, defines.events.on_player_dropped_item],
    (e: OnPreBuildEvent | OnPlayerDroppedItemEvent) => {
      log("player " + e.player_index + " dropped item, tick:" + e.tick)
    }
)