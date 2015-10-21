local game = {}

function game:onCreate(screen)
    print("game::onCreate()")
    self.screen = screen
    screen:setOnClick(function()
        screen:nextFrame()
    end)
    local label = screen:newLabel()
    -- label:setText(R.text.common.title)
    self.label = label
    screen:add(label):bottom():expand()
end

function game:onResume(params)
    print("game::onResume()")
end

function game:onPause()
    print("game::onPause()")
end

function game:onDestroy()
    print("game::onDestroy()")
end

--[[ Game functions ]]--

function game:onEnterFrame()
    print("game::onEnterFrame()")
end

function game:onText(textID)
    print("game::onText()")
    self.label:setText(textID)
end

function game:onSound()
    print("game::onSound()")
end

function game:onVoice()
    print("game::onVoice()")
end

function game:onTransform()
    print("game::onTransform()")
end

function game:onCharacter()
    print("game::onCharacter()")
end

function game:onCamera()
    print("game::onCamera()")
end

function game:onMusic()
    print("game::onMusic()")
end

function game:onBackground()
    print("game::onBackground()")
end

function game:onInteractive()
    print("game::onInteractive()")
end

function game:onLeaveFrame()
    print("game::onLeaveFrame()")
end

return game
