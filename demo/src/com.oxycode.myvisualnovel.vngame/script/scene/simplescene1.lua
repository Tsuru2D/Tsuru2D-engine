return function(scene)
    scene:setup(function(frame, state)
        state.bob = frame:create(R.object.bob, {
            x = -400,
            y = 80
        })
        frame:background(R.image.background.bg1)
    end)
    scene:frame("f1", function(frame, state)
        -- frame:music(R.music.bg1)
        frame:transform(state.bob, {
            x = {value = 300, duration = 1, interpolation = "pow2"}
        })
    end)
    scene:frame("f1.5", function(frame, state)
        frame:character(R.object.bob)
        frame:text(R.text.simplescene1.hello_world)
    end)
    scene:frame("f2", function(frame, state)
        frame:character(R.object.bob)
        frame:text(R.text.simplescene1.my_name_is)
    end)
    scene:frame("f3", function(frame, state)
        frame:character(R.object.bob)
        frame:text(R.text.simplescene1.my_talented_artist)
    end)
    scene:frame("f4", function(frame, state)
        frame:character(R.object.bob)
        frame:text(R.text.simplescene1.check_out_things)
    end)
    scene:frame("f5", function(frame, state)
        frame:character(R.object.bob)
        frame:text(R.text.simplescene1.rotation)
        frame:transform(state.bob, {
            rotation = {value = 15, duration = 0.5, interpolation = "swing"},
        })
    end)
    scene:frame("f5.5", function(frame, state)
        frame:character(R.object.bob)
        frame:text(R.text.simplescene1.rotation)
        frame:transform(state.bob, {
            rotation = {value = 0, duration = 0.5, interpolation = "swing"},
        })
    end)
    scene:frame("f6", function(frame, state)
        frame:character(R.object.bob)
        frame:text(R.text.simplescene1.turn_big)
        frame:transform(state.bob, {
            scale = {value = 1.1, duration = 0.5, interpolation = "pow2"},
        })
    end)
    scene:frame("f7", function(frame, state)
        frame:character(R.object.bob)
        frame:text(R.text.simplescene1.turn_small)
        frame:transform(state.bob, {
            scale = {value = 0.9, duration = 0.5, interpolation = "pow2"},
        })
    end)
    scene:frame("f7.5", function(frame, state)
        frame:character(R.object.bob)
        frame:text(R.text.simplescene1.turn_small)
        frame:transform(state.bob, {
            scale = {value = 1, duration = 0.5, interpolation = "pow2"},
        })
    end)
    scene:frame("f8", function(frame, state)
        frame:character(R.object.bob)
        frame:text(R.text.simplescene1.turn_invisible)
        frame:transform(state.bob, {
            alpha = {value = 0.5, duration = 0.3}
        })
    end)
    scene:frame("f9", function(frame, state)
        frame:character(R.object.bob)
        frame:text(R.text.simplescene1.isnt_that_awesome)
        frame:transform(state.bob, {
            alpha = {value = 1, duration = 0.3}
        })
    end)
    scene:frame("f10", function(frame, state)
        frame:character(R.object.bob)
        frame:text(R.text.simplescene1.lets_go_see_alice)
    end)
    scene:frame("f11", function(frame, state)
        frame:transform(state.bob, {
            x = {value = 1500, duration = 1, interpolation = "pow3"}
        })
    end)
    return R.scene.simplescene2
end
