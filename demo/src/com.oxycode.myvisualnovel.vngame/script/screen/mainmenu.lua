local mainmenu = {}

function mainmenu:onCreate(screen)
    print("mainmenu::onCreate()")
    self.screen = screen
    self.netManager = screen:getNetManager()
    self.ui = screen:getUIManager()
    self.settings = nil

    self.mainTable = self.ui:newTable()

    self.usernameTextField = self.ui:newTextField(R.skin.default.textbox)
    self.usernameTextField:setHint(R.text.common.username)
    self.usernameTextField:setTextChangedListener(function(textbox, value)
        print("Username: " .. value)
    end)
    self.mainTable:add(self.usernameTextField):fillX():height(30):padLeft(10):padRight(10)
    self.mainTable:row()

    self.passwordTextField = self.ui:newTextField(R.skin.default.textbox)
    self.passwordTextField:setHint(R.text.common.password)
    self.passwordTextField:setPasswordMode(true)
    self.passwordTextField:setTextChangedListener(function(textbox, value)
        print("Password: " .. value)
    end)
    self.mainTable:add(self.passwordTextField):fillX():height(30):padLeft(10):padRight(10)
    self.mainTable:row()

    self.loginButton = self.ui:newTextButton(R.skin.default.button)
    self.loginButton:setText(R.text.common.log_in)
    self.loginButton:setClickListener(function()
        self.netManager:login(
            self.usernameTextField:getText(),
            self.passwordTextField:getText(),
            function(success, errorCode, data)
                self:onLoginResult(success, errorCode, data)
            end
        )
    end)

    self.mainTable:add(self.loginButton):fillX()
    self.mainTable:row()

    self.slider = self.ui:newSlider(R.skin.default.slider)
    self.slider:setValueChangedListener(function(slider, value)
        print("Value: " .. value)
    end)
    self.mainTable:add(self.slider):fillX()
    self.mainTable:row()

    self.checkbox = self.ui:newCheckBox(R.skin.default.checkbox)
    self.checkbox:setText(R.text.common.check_me)
    self.checkbox:setCheckedChangedListener(function(checkbox, value)
        print("Checked: " .. (value and "true" or "false"))
    end)
    self.mainTable:add(self.checkbox):fillX()
    self.mainTable:row()

    -- Login status label
    self.loginStatusLabel = self.ui:newLabel(R.skin.default.label)
    self.loginStatusLabel:setText(R.text.common.please_log_in, "FMT1", "FMT2")
    self.mainTable:add(self.loginStatusLabel):fillX():padBottom(15)
    self.mainTable:row()

    self.theButton = self.ui:newTextButton(R.skin.default.button)
    self.theButton:setText(R.text.common.click_me)
    self.theButton:setClickListener(function()
        if self.settings then
            if not self.settings.lolis then
                self.settings.lolis = 0
            end
            print("Current lolis: " .. self.settings.lolis)
            self.settings.lolis = self.settings.lolis + 1
            self.netManager:writeGameSettings(self.settings, function(success, errorCode, data)
                self:onWriteSettingsResult(success, errorCode, data)
            end)
        else
            print("Not logged in!")
        end
    end)
    self.mainTable:add(self.theButton):fillX():padBottom(15)
    self.mainTable:row()

    -- Start game button
    self.startGameButton = self.ui:newTextButton(R.skin.default.button)
    self.startGameButton:setText(R.text.common.start_game)
    self.startGameButton:setClickListener(function()
        screen:setGameScreenNew(R.scene.simplescene1, {})
    end)
    self.mainTable:add(self.startGameButton):fillX()
    self.mainTable:row()

    self.ui:add(self.mainTable):expand()

    -- Login to server
    --[[
    self.netManager:login("test@test.com", "abc", function(success, errorCode, data)
        self:onLoginResult(success, errorCode, data)
    end)]]

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
