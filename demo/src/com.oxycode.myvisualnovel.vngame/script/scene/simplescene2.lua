return function(scene)
    scene:setup(function(frame, state)
        state.alice = frame:create(R.object.alice, {
            x = 1500,
            y = 50
        })
        state.bob = frame:create(R.object.bob, {
            x = -400,
            y = 50
        })

        -- state.linux=frame:create(R.object.linux, {x=-400, y = 50,alpha=0})

        frame:background(R.image.background.bg2)
    end)
    scene:frame("f1", function(frame, state)
        frame:character(R.object.bob)
        frame:transform(state.bob, {
            x = {value = 200, duration = 1, interpolation = "pow2"}
        })
        frame:transform(state.alice, {
            x = {value = 800, duration = 1, interpolation = "pow2"}
        })
        frame:text(R.text.simplescene1.next_scene)
    end)
    scene:frame("f2", function(frame, state)
        frame:character(R.object.bob)
        frame:transform(state.bob, {
            x = {value = 400, duration = 0.5, interpolation = "pow2"}
        })
        frame:text(R.text.simplescene1.greet_Alice)
    end)
    scene:frame("f3", function(frame, state)
        frame:character(R.object.alice)
        frame:transform(state.bob, {
            x = {value =150, duration = 0.5, interpolation = "pow3"}
        })
        frame:transform(state.alice, {
            x = {value =460, duration = 1, interpolation = "sineIn"}
        })
        frame:text(R.text.simplescene1.pretty)
    end)
    scene:frame("f4", function(frame, state)
        frame:character(R.object.alice)
        frame:transform(state.alice, {
            x = {value =430, duration = 0.5, interpolation = "sineIn"},
            scale= {value=1.1, duration=0.5,interpolation="pow4"}
        })
        frame:text(R.text.simplescene1.ugly)
    end)
    scene:frame("f4", function(frame, state)
        frame:character(R.object.alice)
        frame:transform(state.alice, {x =460, scale={value=1, duration=0.5,interpolation="pow4"}})

        frame:text(R.text.simplescene1.drag)
    end)
    scene:frame("f5", function(frame, state)
        frame:character(R.object.bob)
        frame:transform(state.bob, {
            x = {value =460, duration = 1.5, interpolation = "sineIn"}
        })
        frame:transform(state.alice, {
            x = {value =800, duration = 1, interpolation = "sineIn"}
        })
        frame:text(R.text.simplescene1.copyright)
    end)

    -- scene:frame("f6", function(frame, state)
    --     frame:character(R.object.bob)
    --     frame:transform(state.bob, {
    --         x = {value =-400, duration = 1.5, interpolation = "sineIn"}
    --     })
    --     frame:transform(state.alice, {
    --         x = {value =2000, duration = 1, interpolation = "sineIn"}
    --     })
    --     frame:transform(state.linux,{x = {value =400, duration = 1, interpolation = "bounceIn"},
    --                                 alpha={value=1, duration=1, interpolation="sineIn"}})
    --     frame:text(R.text.simplescene1.linux)
    -- end)

    return R.scene.simplescene1
end
