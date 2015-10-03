-----------------------------------------
-- Tsuru2D game scene script example
-----------------------------------------
-- TODO: How can we implement frame GOTO's?
--    Possible solution: maintain a list of past frames
--       On goto(), if frame is in past list, go to that frame
--       Otherwise, skip forward until the target frame is found
--    But then, how will object allocations work?
--    And by doing so, we would be bypassing the execution order
--    within the script
--       Importantly, code between frame() calls would not be executed
--       the second time around
--    Also, how will pre-fetching work, if everything is a function call?
--    Additionally, to implement this we have to create a "virtual execution environment",
--    Where we "run" the frame beforehand and convert it to a Java object at the
--    same time, so we can build a frame table, then finally pass everything
--    to the UI code, but this is incredibly complex to implement and would be unintuitive
--    to the game programmer.

--    Asset prefetching code can be done with a little help with function envs and
--    pushFrame() -> run function -> popFrame() and seeing which asset IDs were
--    created, then prefetching those
--
--    Alternative: We could make goto() only run outside frames, and use Lua's
--    "goto" functionality
-- TODO: How can we implement "go to frame after click" vs. "go to frame now"
scene("second_scene", function()
    -- TODO: fade in
    local alice = create(R.character.alice, {
        x = 1.5,
        y = 0.5,
        z = 1,
        stance = "sleepy",
        clothes = "home",
        face = "happy",
    })
    frame("f1", function()
        background(R.image.bg.school)
        delay(1, function()
            camera:alpha(1).next(function()
                alice:x(0.3)
                camera:bounds({0.15,0.15,0.85,0.85})
                music(R.music.bgm1)
                camera:bounds({0,0,1,1}) -- todo: camera effects (bounce, shake, etc)
                character(R.character.alice)
                text(R.text.chapter1.scene2.hello_world)
            end)
        end)
    end)
    frame("f2", function()
        character(R.character.alice)
        text(R.text.chapter1.scene2.this_is_a_test)
    end)
    frame("f3", function()
        alice:face("happy")
        character(R.character.alice)
        text(R.text.chapter1.scene2.whats_your_name)
        interactive("player_name", {
            -- Everything in this table is directly passed
            -- to the UI script, so they can put anything
            -- they want in here
            type = "textbox",
        })
        if var("player_name"):match("%a") then -- all letters?
            goto("f3")
        else
            goto("f3.5")
        end
    end)
    frame("f3.5", function()
        alice:face("annoyed")
        character(R.character.alice)
        text(R.text.chapter1.scene2.come_on_be_serious)
        goto("f3")
    end)
    frame("f4", function()
        character(R.character.alice)
        text(R.text.chapter1.scene2.fmt1_hi_there, var("player_name")) -- string formatting
    end)
    local cg = create(R.image.cg.alice_happy, {
        bounds = {0,0,1,1},
        alpha = 0,
    })
    frame("f5", function()
        cg:alpha(1).next(function()
            character(R.character.alice)
            text(R.text.chapter1.scene2.this_is_a_cg)
        end)
    end)
    frame("f6", function()
        cg:alpha(0)
        text(R.text.chapter1.scene2.narrator_blah)
    end)
    delete(cg)
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
    frame("f7", function()
        character(R.character.alice)
        text(R.text.chapter1.scene2.lets_go)
        delay(1, function()
            alice:x(1.5)
        end)
        if var("next_location1") == "school" then
            goto(R.scene.chapter1.school)
        else
            goto(R.scene.chapter1.home)
        end
    end)
    delete(alice)
    -- TODO: fade out
end)

video(R.video.op)

splash(R.image.splash.generic)
