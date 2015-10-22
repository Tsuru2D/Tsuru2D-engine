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
        frame:background(R.image.bg2)
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
        frame:text(R.text.simplescene1.isnt_that_cool)
    end)
    return R.scene.simplescene1
end
