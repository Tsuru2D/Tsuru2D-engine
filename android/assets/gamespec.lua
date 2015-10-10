-----------------------------------------
-- Tsuru2D game scene script example
-----------------------------------------
-- TODO: How can we implement "go to frame after click" vs. "go to frame now"
-- TODO: What about asset pre-fetching? Is that even possible with this model?
--       The hacky way: have a "pre-fetch" mode where we virtualize everything
--       and run the scene through a asset cacher
-- TODO: What about scene transitions? Fade in/out, etc.
-- TODO: Camera effects (bounce, shake, etc)
scene("second_scene", function(scene)
    setup(function(state)
        create("alice", R.character.alice, {
            x = 1.5,
            y = 0.5,
            z = 1,
            stance = "sleepy",
            clothes = "home",
            face = "happy",
        })

        create("cg", R.image.cg.alice_happy, {
            bounds = {0,0,1,1},
            alpha = 0,
        })

        alpha(0)
        camera({bounds = {0.15,0.15,0.85,0.85}})
        background(R.image.bg.school)
    end)

    frame("f1", function()
        character(R.character.alice)
        music(R.music.bgm1)
        alpha(1).next(function()
            sound(R.sound.boom)
            camera("shake")
        end).next(function()
            transform("alice", {x=0.3})
            text(R.text.chapter1.scene1.what_was_that)
            voice(R.voice.chapter1.scene1.alice_what_was_that)
        end)

        character(R.character.alice)
        music(R.music.bgm1)
        alpha(1).parallel(sound(R.sound.boom), camera("shake"))
        transform(R.character.alice, {
            x = 0.3,
        }).transform(R.character.alice, {
            y = animate(0,3, 0.4, "bounce")
        })

        parallel {
            text(R.text.chapter1.scene1.what_was_that),
            voice(R.voice.chapter1.scene1.alice_what_was_that)
        }

        delay(1)
            .next(text(R.text.blah))
            .next(function()
                music(R.music.blah)
                camera{bounds={0,0,1,1}}
            end)
            .next(interactive("player_name", {type = "textbox"}))

        sound(R.sound.boom)
            .next()
        frame:delay(1, function()
            frame.camera:alpha(1).next(function()
                frame.state.alice:x(0.3)
                frame:camera({
                    bounds = {0.15,0.15,0.85,0.85}
                })
                frame:music(R.music.bgm1)
                camera:bounds({0,0,1,1}) -- todo: camera effects (bounce, shake, etc)
                frame:character(R.character.alice)
                frame:text(R.text.chapter1.scene2.hello_world)
            end)
        end)
    end)

    frame("f2", function()
        character(R.character.alice)
        text(R.text.chapter1.scene2.this_is_a_test)
    end)
    frame("f3", function(state)
        state.alice:face("happy")
        character(R.character.alice)
        text(R.text.chapter1.scene2.whats_your_name)
        interactive("player_name", {
            -- Everything in this table is directly passed
            -- to the UI script, so they can put anything
            -- they want in here
            type = "textbox",
        })
        if var("player_name"):match("%a") then -- all letters?
            goto("f4")
        else
            goto("f3.5")
        end
    end)
    frame("f3.5", function(state)
        state.alice:face("annoyed")
        character(R.character.alice)
        text(R.text.chapter1.scene2.come_on_be_serious)
        goto("f3")
    end)
    frame("f4", function()
        character(R.character.alice)
        text(R.text.chapter1.scene2.fmt1_hi_there, var("player_name")) -- string formatting
    end)
    frame("f5", function(state)
        state.cg:alpha(1).next(function()
            character(R.character.alice)
            text(R.text.chapter1.scene2.this_is_a_cg)
        end)
    end)
    frame("f6", function(state)
        state.cg:alpha(0)
        text(R.text.chapter1.scene2.narrator_blah)
    end)
    frame("f7", function()
        character(R.character.alice)
        text(R.text.chapter1.scene2.where_should_we_go)
        interactive("next_location1", {
            type = "multichoice",
            choices = {
                {
                    value = "school",
                    text = R.text.chapter1.school,
                },
                {
                    value = "home",
                    text = R.text.chapter1.home,
                }
            }
        })
    end)
    frame("f7", function(state)
        character(R.character.alice)
        text(R.text.chapter1.scene2.lets_go)
        delay(1, function()
            state.alice:x(1.5)
        end)
        if var("next_location1") == "school" then
            goto(R.scene.chapter1.school)
        else
            goto(R.scene.chapter1.home)
        end
    end)
end)

video(R.video.op)

splash(R.image.splash.generic)
