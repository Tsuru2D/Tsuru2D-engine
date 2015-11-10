local game = {}

function game:onCreate(screen)
    print("game::onCreate()")
    self.screen = screen
    self.ui = screen:getUIManager()
    self.net = screen:getNetManager()
    screen:setClickListener(function()
        screen:nextFrame()
    end)

    -- Text label
    self.textLabel = self.ui:newLabel(R.skin.default.label)
    self.ui:add(self.textLabel):bottom():expand()

    -- Back to menu button
    self.backButton = self.ui:newTextButton(R.skin.default.button)
    self.backButton:setText(R.text.common.back_to_menu)
    self.backButton:setClickListener(function()
        -- TODO: This next line is a workaround for the bug detailed in
        -- GameScreen. Once the bug has been fixed, this should be removed
        screen:setClickListener(nil)
        screen:setMenuScreen(R.screen.mainmenu)
    end)
    self.ui:add(self.backButton):top():right()

    -- Save button
    self.saveButton = self.ui:newTextButton(R.skin.default.button)
    self.saveButton:setText(R.text.common.save)
    self.saveButton:setClickListener(function()
        print("write save data")
        self.net:writeSave(0, true, function(success, errorCode)
            if not success then
                if errorCode then
                    print("Login error: " .. errorCode)
                end
            end
        end)
    end)
    self.ui:add(self.saveButton):top():right()

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

function game:onText(textID, ...)
    print("game::onText()")
    self.textLabel:setText(textID, ...)
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
