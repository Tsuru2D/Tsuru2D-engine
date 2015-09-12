{
    -- optional ID of the frame, you only have to define it if you plan 
    -- to "jump" to this frame from another frame (branching).
    id = "scene_start",

    -- this gets called at the beginning of the frame
    onEnter = function(state)
        -- create bob (he just entered the scene)
        local bob = state:createObject(R.character.bob, {
            -- x position of bob. notice how this is greater than 1, 
            -- which means he will spawn outside the scene (obviously, 
            -- the idea is that he will animate in). also, note that 
            -- positions are specified as a ratio (1.5 means 1.5*width). 
            -- this is so that games will run on a variety of screen sizes.
            x = 1.5,
            -- y position of bob.
            y = 0.5,
            -- the z-height of bob. objects with a higher 
            -- z-height get shown BEHIND objects with a lower 
            -- z-height
            z = 1,
            -- rotation of bob, the origin of rotation is the 
            -- center of the sprite. units are undecided as of right now. 
            -- degrees is more intuitive, but radians is used in 
            -- programming more often. hmm...
            rotation = 0,
            -- alpha (transparency) of bob, where 0 = completely 
            -- transparent (aka invisible) and 1 = completely 
            -- opaque (aka visible). as with the x position, 
            -- you should change this to 1 using :transform()
            alpha = 0
        })

        -- get alice (she was in the scene previously)
        local alice = state:getObject(R.character.alice)
        -- move alice's x position to 0.2 over 0.3 seconds
        alice:transform(R.transform.linear({x=0.2}, 0.3))
        -- move alice's y position to 0.2 over the default time interval
        -- this one uses the "quadOut" easing curve (see http://easings.net/)
        alice:transform(R.transform.quadOut{y=0.2})
        -- give alice some summer clothes
        alice:setState(R.character.alice.clothes.summer)
        -- make alice happy, yay!
        alice:setState(R.character.alice.face.happy)

        -- deallocate resources for objects no longer needed
        -- maybe we should rename character to something more generic, 
        -- like object/gameobject/actor, since this is used for 
        -- props too, like this rock for instance
        state:deleteObject(R.character.rock)
        -- play a boom sound effect (plays only once)
        state:playSound(R.audio.sound.boom)
        -- start playing some background music (loops)
        state:playMusic(R.audio.music.bgm1)
        -- set the character who will be speaking this frame
        state:setCharacter(R.character.bob)
        -- set the stuff that bob will say
        state:setText(R.text.chapter1.hello)
        -- let bob be heard! normally you would use a resource 
        -- id, like R.audio.voice.bob.hello, but we can save 
        -- some effort by using "auto". this will look up the 
        -- resource with the same name as the text resource, as 
        -- specified in :setText(). in this case, it will be 
        -- R.audio.voice.chapter1.hello. that is formed using 
        -- two components, {R.audio.voice} (the base directory 
        -- for voice files) and {chapter1.hello}, the relative 
        -- path of bob's text.
        state:playVoice("auto")
        -- do some stuff with the camera
        state:transformCamera({
            -- the viewport of the camera (so you can zoom in)
            -- the format is {left, top, right, bottom}, so this 
            -- line will reset the camera to the full view
            bounds = {0,0,1,1},
            -- perform some effects on the camera
            -- this one makes it shake for +-5% pixels for 0.5 seconds
            effect = R.script.camera.shake{amplitude=5, duration=0.5}
        })

    end,

    -- this gets called once all animations have completed
    onReady = function(state)
        -- displays a choice dialog, the first argument is the 
        -- ID of the choice which must be unique across the entire 
        -- game. it can be read using state:getChoice("my_second_choice")
        state:displayChoice("my_second_choice", {
            {
                -- the programmer-defined value that can be read later
                value = "opt1",
                -- the text that is displayed to the player
                text = R.text.common.firstChoiceOpt1,
                -- (optional) if this is set, the scene will jump 
                -- to this frame after the user selects an option
                destinationFrame = "scene_A"
            },
            {
                -- same as above, but notice that we don't specify a 
                -- destination frame, so the game will continue from the 
                -- next frame (unless overridden in onLeave, as we will see)
                value = "opt2",
                text = R.text.common.firstChoiceOpt2
            },
        })
    end,

    -- this gets called when leaving the frame
    onLeave = function(state)
        -- let's see what the user picked...
        local choice1 = state:getChoice("my_first_choice")
        local choice2 = state:getChoice("my_second_choice")

        -- we CAN handle stuff for case choice2 == "opt1", 
        -- but that would override the destinationFrame 
        -- option we specified in the choice.
        if choice1 == "opt1" and choice2 == "opt2" then
            -- should this be named "gotoFrame" or "goToFrame"?
            -- or maybe even "jumpToFrame"?
            state:goToFrame("scene_B")
        elseif choice1 == "opt2" and choice2 == "opt2" then
            state:goToFrame("scene_C")
        end
    end,
}