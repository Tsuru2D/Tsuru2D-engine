local game = {}

function game:onCreate(screen)
    print("game::onCreate()")
    self.screen = screen
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

end

function game:onText()
    print("game::onText()")
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

function game:onLeaveFrame()

end

return game
