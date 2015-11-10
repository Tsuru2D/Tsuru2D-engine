return function(scene)
    scene:setup(function(frame, state)
        state.bob = frame:create(R.object.bob, {
            x = -400,
            y = 50,
            stance = R.image.character.bob.base.sleepy,
            clothes = R.image.character.bob.clothes.home,
            face = R.image.character.bob.face.happy,
        })
        frame:background(R.image.background.bg1)
    end)
    scene:frame("f1", function(frame, state)
        frame:music(R.music.bg1)
        frame:character(R.object.bob)
        frame:transform(state.bob, {
            scale = {value = 1.1, duration = 0.5, interpolation = "swing"},
            x = {value = 300, duration = 1, interpolation = "pow2"}
        })
        frame:text(R.text.simplescene1.greetings)
    end)
    scene:frame("f2", function(frame, state)
        frame:character(R.object.bob)
        frame:transform(state.bob, {
            rotation = {value = 15, duration = 0.5, interpolation = "swing"},
            alpha = {value = 0.5, duration = 0.3}
        })
        frame:text(R.text.simplescene1.rotate)
    end)
    scene:frame("f3", function(frame, state)
        frame:transform(state.bob, {
            rotation = {value = 0, duration = 0.5, interpolation = "swing"},
            alpha = {value = 1, duration = 0.3}
        })
        frame:character(R.object.bob)
        frame:text(R.text.simplescene1.and_fade)
    end)
    scene:frame("f4", function(frame, state)
        frame:transform(state.bob, {
            scale = {value = 1, duration = 0.5, interpolation = "swing"},
            x = {value = 1500, duration = 1, interpolation = "pow3"}
        })
        frame:character(R.object.bob)
        frame:text(R.text.simplescene1.swing_in)
    end)
    scene:frame("f5", function(frame, state)
        frame:character(R.object.bob)
        frame:text(R.text.simplescene1.go_to_next_scene)
    end)
    return R.scene.simplescene2
end
