return function(scene)
    scene:setup(function(frame, state)
        state.bob = frame:create(R.object.bob, {
            x = 1.5,
            y = 0.5,
            z = 1,
        })
        frame:background(R.image.bg.school)
    end)
    scene:frame("f1", function(frame, state, globals)
        frame:character(R.object.bob)
        frame:transform(state.bob, {x = 0.7})
        frame:text(R.text.simplescene1.next_scene)
        print("This should be pomf: " .. globals.my_var)
    end)
    scene:frame("f2", function(frame, state)
        frame:character(R.object.bob)
        frame:text(R.text.simplescene1.isnt_that_cool)
        frame:voice(R.voice.chapter1.scene1.alice_what_was_that)
    end)
    return R.scene.simplescene3
end
