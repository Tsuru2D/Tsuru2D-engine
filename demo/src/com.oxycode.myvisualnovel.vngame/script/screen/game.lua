local game = {}

function game:onCreate(screen)
    print("game::onCreate()")
    self.screen = screen
    self.ui = screen:getUIManager()
    self.net = screen:getNetManager()
    screen:setClickListener(function()
        screen:nextFrame()
    end)

    self.controlTable = self.ui:newTable()

    -- Back to menu button
    self.backButton = self.ui:newTextButton(R.skin.default.button)
    self.backButton:setText(R.text.common.back_to_menu)
    self.backButton:setClickListener(function()
        screen:setMenuScreen(R.screen.mainmenu)
    end)
    self.controlTable:add(self.backButton)

    -- Save button
    self.saveButton = self.ui:newTextButton(R.skin.default.button)
    self.saveButton:setText(R.text.common.save)
    self.saveButton:setClickListener(function()
        self.net:writeSave(0, true, function(success, errorCode)
            if not success then
                if errorCode then
                    print("Save error: " .. errorCode)
                end
            end
        end)
    end)
    self.controlTable:add(self.saveButton)
    self.ui:add(self.controlTable):top():right()
    self.ui:row()

    -- Text label
    self.textLabel = self.ui:newLabel(R.skin.default.label)
    self.ui:add(self.textLabel):bottom():expand()
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
