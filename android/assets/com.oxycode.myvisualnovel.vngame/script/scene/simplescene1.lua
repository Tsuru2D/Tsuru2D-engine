return function(scene)
    scene:setup(function(frame, state)
        state.bob = frame:create(R.object.bob, {
            x = 1.5,
            y = 0.5,
            z = 1,
            stance = R.image.character.bob.base.sleepy,
            clothes = R.image.character.bob.clothes.home,
            face = R.image.character.bob.face.happy,
        })
        frame:background(R.image.bg1)
    end)
    scene:frame("f1", function(frame, state, globals)
        frame:character(R.object.bob)
        frame:music(R.music.bgm1)
        frame:transform(state.bob, {x = 0.3})
        frame:text(R.text.simplescene1.what_was_that)
        frame:voice(R.voice.chapter1.scene1.alice_what_was_that)
        globals.my_var = "pomf"
    end)
    scene:frame("f2", function(frame, state)
        frame:character(R.object.bob)
        frame:text(R.text.simplescene1.lolis_are_cute)
        frame:voice(R.voice.chapter1.scene1.alice_what_was_that)
    end)
    scene:frame("f3", function(frame, state)
        frame:character(R.object.bob)
        frame:text(R.text.simplescene1.long_text)
        frame:voice(R.voice.chapter1.scene1.alice_what_was_that)
    end)
    scene:frame("f4", function(frame, state)
        frame:character(R.object.bob)
        frame:text(R.text.simplescene1.this_is_text)
        frame:voice(R.voice.chapter1.scene1.alice_what_was_that)
    end)
    return R.scene.simplescene2
end
