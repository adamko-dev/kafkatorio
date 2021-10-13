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
        let msg = "test mod tick: " + e.tick + "\n"
        // game.print(msg)
        game.write_file("tick.log", msg, true)


        let groups = game.permissions.groups.map(val => {
          return val.name + "\n" + serpent.block(val)
        }).join("\n")

        game.write_file("permissions.json", groups, false)
        // game.write_file("permissions-groups.json", serpent.block(game.permissions.groups), false)
      }
    }
);


// script.on_load(() => {
//   game.print("loaded Factorio Web Map!")
// });
