local game = {}

function game:onCreate(screen)
    print("game::onCreate()")
    self.screen = screen
    local ui = screen:getUIManager()
    screen:setClickListener(function()
        screen:nextFrame()
    end)
    local label = ui:newLabel(R.skin.default.label)
    self.label = label
    ui:add(label):bottom():expand()

    local button = ui:newTextButton(R.skin.default.button)
    button:setText(R.text.common.back_to_menu)
    button:setClickListener(function()
        screen:setMenuScreen(R.screen.mainmenu)
    end)
    self.button = button
    ui:add(button):top():right()
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

function game:onTransform(actor, params)
    print("game::onTransform()")
    self.screen:transform(actor, params)
end

function game:onCharacter()
    print("game::onCharacter()")
end

function game:onCamera()
    print("game::onCamera()")
end

function game:onMusic(musicID)
    print("game::onMusic()")
    self.screen:playMusic(musicID)
end

function game:onBackground(imageID)
    print("game::onBackground()")
    self.screen:setBackground(imageID)
end

function game:onInteractive()
    print("game::onInteractive()")
end

function game:onLeaveFrame()
    print("game::onLeaveFrame()")
end

return game
