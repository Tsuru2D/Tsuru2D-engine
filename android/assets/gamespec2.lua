-- TODO:
-- How to handle scene transition (animation, inter-scene frames)
-- How to play video
-- How to do persistent animation (e.g. sleepy rotate back/forwards)

frame {
    -- The unique ID of the frame. Only required if you plan to jump
    -- to this frame from some other part of the game.
    id = "scene_start",

    -- Create/delete objects before the frame gets shown
    setup = {
        -- Create a new object with the specified initial state
        [R.character.bob] = {
            -- Transform values (built-in, required)
            x = 1.5,
            y = 0.5,
            z = 1,

            -- Transform values (built-in, optional, default values below)
            scale = {1,1}, -- {Xscale,Yscale}
            scale = 1, -- Shorthand form of the above (sets both x and y scales)
            rotation = 0, -- TODO: Radians or degrees?
            color = {1,1,1}, -- {R,G,B}
            alpha = 0,

            -- Game-defined state values (may or may not be required,
            -- it's defined by the game after all)
            stance = R.character.bob.stance.straight,
            clothes = R.character.bob.clothes.home,
            face = R.character.bob.face.happy,
        },

        -- Delete an object (make sure to do this to free memory)
        [R.character.rock] = "delete",
    },

    -- Things that change this frame, maps object ID to value dict
    -- Long syntax:
    --   <key> = {value = <value> [, interpolation = <interpolation>] [, time = <time>]}
    -- Short syntax (equivalent to long syntax with no optional parameters):
    --   <key> = <value>
    transform = {
        [R.character.alice] = {
            face = R.character.alice.face.angry, -- Short syntax
            x = {value = 0.2, interpolation = "elasticIn", time = 0.3}, -- Long syntax
            y = 0.2, -- Short syntax
        },
        [R.character.bob] = {x = 0.5, y = 0.5} -- Ultra-short syntax (most common use case)
    },

    -- The sound to play this frame
    sound = R.audio.sound.boom,

    -- Starts playing this BGM, replace the currently playing BGM if there is one
    music = R.audio.music.bgm1,

    -- Stops the currently playing BGM
    music = "none",

    -- The character who will be speaking this frame
    -- If not specified, it will be the narrator
    character = R.character.bob,

    -- The text that the character (or narrator) says
    text = R.text.chapter1.hello,

    -- The voice that is played this frame.
    -- Can be a resource ID, "auto", or "none".
    -- If it is a resource ID, that voice will be played.
    -- "auto" and "none" override the default voice mode
    -- defined in the character script.
    --
    -- "auto" generates the resource ID from the `text` property.
    -- The algorithm  is as follows:
    --   let relpath := `R.text` - `text`
    --   let voicepath := `R.voice` + relpath
    --
    -- "none" exists only to disable the voice if "auto" is the
    -- default voice mode in the character script.
    voice = "auto",

    -- Control the camera
    camera = {
        -- Zoom in to a section of the scene
        bounds = {0,0,1,1}, -- {top, left, right, bottom}
        -- TODO: implement persistent effects (blur, border, etc)
        -- TODO: implement one-frame effects (shake, ???)
    },

    -- Free-response choice prompt
    interactive = {
        -- The type of the interactive prompt
        type = "textbox",
        -- Some text displayed above the textbox to remind
        -- the user what they're inputting
        prompt = R.text.chapter1.whatsYourName,
        -- The key stored in the global game state dict.
        -- The value will be whatever the user types in the
        -- textbox.
        key = "player_name"
    },

    -- Multiple choice prompt
    interactive = {
        type = "multichoice",
        prompt = R.text.chapter1.whereToGoNext,
        -- This is the key stored in the global game state dict.
        -- If this is not specified, nothing will be written to the
        -- game state dict.
        key = "next_location1",
        choices = {
            -- This is an array instead of a dict, so that the entry order
            -- will be preserved at run-time.
            {
                -- The value that will be looked up with `key`.
                -- Only provide this if `key` is specified.
                value = "next_location1_school",
                -- The text displayed on the menu entry
                text = R.text.chapter1.gotoSchool,
                -- The ID of a frame to jump to after this entry
                -- is selected.
                frame = "scene_school"
            },
            {
                value = "next_location1_home",
                text = R.text.chapter1.gotoHome,
                frame = "scene_home"
            }
        }
    },
}
