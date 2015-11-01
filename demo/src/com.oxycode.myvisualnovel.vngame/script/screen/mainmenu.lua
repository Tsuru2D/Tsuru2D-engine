local mainmenu = {}

function mainmenu:onCreate(screen)
    print("mainmenu::onCreate()")
    self.screen = screen
    self.netManager = screen:getNetManager()
    self.ui = screen:getUIManager()
    self.settings = nil

    self.mainTable = self.ui:newTable()

    self.usernameTextField = self.ui:newTextField(R.skin.default.textbox)
    self.mainTable:add(self.usernameTextField):fillX():height(30)
    self.mainTable:row()

    -- Login status label
    self.loginStatusLabel = self.ui:newLabel(R.skin.default.label)
    self.loginStatusLabel:setText(R.text.common.logging_in)
    self.mainTable:add(self.loginStatusLabel):fillX():padBottom(15)
    self.mainTable:row()

    self.theButton = self.ui:newTextButton(R.skin.default.button)
    self.theButton:setText(R.text.common.click_me)
    self.theButton:setOnClick(function()
        if self.settings then
            if not self.settings.lolis then
                self.settings.lolis = 0
            end
            print("Current lolis: " .. self.settings.lolis)
            self.settings.lolis = self.settings.lolis + 1
            self.netManager:writeGameSettings(self.settings, function(success, errorCode, data)
                self:onWriteSettingsResult(success, errorCode, data)
            end)
        end
    end)
    self.mainTable:add(self.theButton):fillX():padBottom(15)
    self.mainTable:row()

    -- Start game button
    self.startGameButton = self.ui:newTextButton(R.skin.default.button)
    self.startGameButton:setText(R.text.common.start_game)
    self.startGameButton:setOnClick(function()
        screen:setGameScreenNew(R.scene.simplescene1, {})
    end)
    self.mainTable:add(self.startGameButton):fillX()
    self.mainTable:row()

    self.ui:add(self.mainTable):expand()

    -- Login to server
    self.netManager:login("test@test.com", "abc", function(success, errorCode, data)
        self:onLoginResult(success, errorCode, data)
    end)

    -- screen:playMusic(R.music.bg1)
end

function mainmenu:onLoginResult(success, errorCode, data)
    if not success then
        self.loginStatusLabel:setText(R.text.common.login_failed)
        if errorCode then
            print("Login error: " .. errorCode)
        end
        return
    end

    self.loginStatusLabel:setText(R.text.common.reading_settings)
    self.netManager:readGameSettings(function(success, errorCode, data)
        self:onReadSettingsResult(success, errorCode, data)
    end)
end

function mainmenu:onWriteSettingsResult(success, errorCode, data)
    if not success then
        self.loginStatusLabel:setText(R.text.common.writing_settings_failed)
        if errorCode then
            print("Write settings error: " .. errorCode)
        end
        return
    end

    self.loginStatusLabel:setText(R.text.common.reading_settings)
    self.netManager:readGameSettings(function(success, errorCode, data)
        self:onReadSettingsResult(success, errorCode, data)
    end)
end

function mainmenu:onReadSettingsResult(success, errorCode, data)
    if not success then
        self.loginStatusLabel:setText(R.text.common.reading_settings_failed)
        if errorCode then
            print("Read settings error: " .. errorCode)
        end
        return
    end

    self.loginStatusLabel:setText(R.text.common.login_complete)
    self.settings = data
end

function mainmenu:onResume(params)
    print("mainmenu::onResume()")
end

function mainmenu:onPause()
    print("mainmenu::onPause()")
end

function mainmenu:onDestroy()
    print("mainmenu::onDestroy()")
end

return mainmenu
