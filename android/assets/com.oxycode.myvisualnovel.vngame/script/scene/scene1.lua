-----------------------------------------
-- Tsuru2D game scene script example
-----------------------------------------
-- TODO: How can we implement "go to frame after click" vs. "go to frame now"
-- TODO: What about scene transitions? Fade in/out, etc.
-- TODO: Camera effects (bounce, shake, etc)
-- TODO: Video playback

---
-- Action documentation
--
-- There are two types of actions:
--     asynchronous (async) - don't wait for action to finish
--     synchronous - wait for action to finish
--
-- By default, actions are async, but can be made synchronous by
-- calling :wait() on the action.
--
-- Play two sounds at the same time:
--     sound(R.sound.boom1)
--     sound(R.sound.boom2)
--
-- Play two sounds, one after the other:
--     sound(R.sound.boom1):wait()
--     sound(R.sound.boom2)
--
-- To group multiple async actions and make their aggregate action
-- synchronous, you can use sync{}:wait():
--     sync {
--         sound(R.sound.boom1),
--         sound(R.sound.boom2),
--         sound(R.sound.boom3)
--     }:wait()
-- This is the same as:
--     local a = sound(R.sound.boom1)
--     local b = sound(R.sound.boom2)
--     local c = sound(R.sound.boom3)
--     a:wait()
--     b:wait()
--     c:wait()
--
-- Note that these two are equivalent:
--     delay(1, function() x():wait() end):wait()
--     delay(1):wait(); x():wait()
--
-- And so are these two (the former is probably a bug):
--     delay(1, function() x():wait() end)
--     delay(1, function() x() end)
--
-- Note that this call is useless:
--     delay(1)
-- The developer probably meant:
--     delay(1):wait()
--
-- Also note the difference between:
--     delay(1, function() x() end):wait()
--     delay(1, function() x():wait() end):wait()
-- The first will wait for one second, then continue.
-- The second will wait for one second, then wait for x() to finish, then continue.
--
-- Some actions only have asynchronous forms. For example:
--     music()
--     sound() (unfortunately, due to a limitation in libGDX)
--     character()
-- Calling :wait() on these actions will do nothing.
--
-- Finally, note that interactive() and getvar() are not
-- actions, and therefore, you do not call :wait() on them.
-- interactive() will always block until it is finished.
---

scene("scene1", function(scene)
    scene:setup(function(frame, state)
        state.alice = frame:create(R.character.alice, {
            x = 1.5,
            y = 0.5,
            z = 1,
            stance = R.image.character.alice.base.sleepy,
            clothes = R.image.character.alice.clothes.home,
            face = R.image.character.alice.face.happy,
        })
        state.cg = frame:create(R.image.cg.alice_angry, {
            bounds = {0,0,1,1},
            alpha = 0,
        })
        -- Note that all animations in the setup function
        -- will be suppressed
        frame:alpha(0)
        frame:camera({bounds = {0.15,0.15,0.85,0.85}})
        frame:background(R.image.bg.school)
    end)
    scene:frame("f1", function(frame, state)
        frame:alpha(1):wait()
        frame:character(R.character.alice)
        frame:music(R.music.bgm1)
        frame:delay(1):wait()
        local boom = frame:sound(R.sound.boom)
        frame:camera("shake"):wait()
        boom:wait()
        frame:delay(1, function()
            frame:sound(R.sound.boom):wait()
        end):wait()
        frame:camera("shake")
        frame:delay(0.5):wait()
        local trans = frame:transform(state.alice, {x = 0.3})
        frame:camera({bounds = {0,0,1,1}}):wait()
        trans:wait()
        frame:text(R.text.chapter1.scene1.what_was_that)
        frame:voice(R.voice.chapter1.scene1.alice_what_was_that)
    end)
    scene:frame("f2", function(frame)
        frame:character(R.character.alice)
        frame:text(R.text.chapter1.scene2.what_happened)
        frame:voice(R.voice.chapter1.scene2.alice_what_happened)
    end)
    scene:frame("f3", function(frame, state, globals)
        frame:character(R.character.alice)
        local trans = frame:transform(state.alice, {face = R.image.character.alice.face.suspicious})
        frame:text(R.text.chapter1.scene2.whats_your_name):wait()
        trans:wait()
        local name = frame:interactive {
            -- Everything in this table is directly passed to the UI script,
            -- so developers can put anything they want in here
            type = "textbox",
            prompt = R.text.whats_your_name
        }:wait()
        if name:match("%a") then
            globals.player_name = name
            frame:gotoframe("f4")
        else
            frame:gotoframe("f3.5")
        end
    end)
    scene:frame("f3.5", function(frame, state)
        frame:transform(state.alice, {face = R.image.character.alice.face.annoyed})
        frame:character(R.character.alice)
        frame:text(R.text.chapter1.scene2.come_on_be_serious)
        frame:voice(R.voice.chapter1.scene2.alice_come_on_be_serious)
        frame:gotoframe("f3")
    end)
    scene:frame("f4", function(frame, state, globals)
        frame:character(R.character.alice)
        frame:text(R.text.chapter1.scene2.fmt1_hi_there, globals.player_name)
        frame:voice(R.voice.chapter1.scene2.alice_hi_there)
    end)
    scene:frame("f5", function(frame, state)
        frame:transform(state.cg, {alpha = 1}):wait()
        frame:text(R.text.chapter1.scene2.narrator_this_is_a_cg)
    end)
    scene:frame("f6", function(frame, state)
        frame:text(R.text.chapter1.scene2.narrator_blah)
        frame:transform(state.cg, {alpha = 0})
    end)
    scene:frame("f7", function(frame, state, globals)
        frame:character(R.character.alice)
        frame:text(R.text.chapter1.scene2.where_should_we_go)
        globals.next_location1 = frame:interactive {
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
        }:wait()
    end)
    scene:frame("f8", function(frame, state, globals)
        frame:character(R.character.alice)
        frame:text(R.text.chapter1.scene2.lets_go)
        frame:voice(R.voice.chapter1.scene2.alice_lets_go)
        frame:delay(1, function()
            frame:transform(state.alice, {x = 0.3}):wait()
        end):wait()
        frame:delay(0.5):wait()
        if globals.next_location1 == "school" then
            frame:gotoframe(R.scene.chapter1.school)
        else
            frame:gotoframe(R.scene.chapter1.home)
        end
    end)
end)

splash(R.image.splash.generic)
