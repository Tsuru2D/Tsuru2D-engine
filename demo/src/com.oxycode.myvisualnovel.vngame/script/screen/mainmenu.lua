local mainmenu = {}

function mainmenu:onCreate(screen)
    print("mainmenu::onCreate()")
    self.screen = screen
    self.netManager = screen:getNetManager()
    self.ui = screen:getUIManager()

    -- Login status label
    self.loginStatusLabel = self.ui:newLabel(R.skin.default.label)
    if self.netManager:isLoggedIn() then
        self.loginStatusLabel:setText(R.text.common.logged_in)
    else
        self.loginStatusLabel:setText(R.text.common.please_log_in)
    end
    self.ui:add(self.loginStatusLabel):top():left():expandX()
    self.ui:row()

    self.mainTable = self.ui:newTable()

    -- Username textbox
    self.usernameTextField = self.ui:newTextField(R.skin.default.textbox)
    self.usernameTextField:setHint(R.text.common.username)
    self.usernameTextField:setText("test@test.com")
    self.usernameTextField:setTextChangedListener(function(textbox, value)
        print("Username: " .. value)
    end)
    self.mainTable:add(self.usernameTextField):fillX():height(30):padLeft(10):padRight(10)
    self.mainTable:row()

    -- Password textbox
    self.passwordTextField = self.ui:newTextField(R.skin.default.textbox)
    self.passwordTextField:setText("abc")
    self.passwordTextField:setHint(R.text.common.password)
    self.passwordTextField:setPasswordMode(true)
    self.passwordTextField:setTextChangedListener(function(textbox, value)
        print("Password: " .. value)
    end)
    self.mainTable:add(self.passwordTextField):fillX():height(30):padLeft(10):padRight(10):spaceBottom(15)
    self.mainTable:row()

    -- Login button
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
    self.mainTable:add(self.loginButton):fillX():spaceBottom(15)
    self.mainTable:row()

    -- Start game button
    self.startGameButton = self.ui:newTextButton(R.skin.default.button)
    self.startGameButton:setText(R.text.common.start_game)
    self.startGameButton:setClickListener(function()
        screen:setGameScreenNew(R.scene.simplescene1, {})
    end)
    self.mainTable:add(self.startGameButton):fillX():spaceBottom(15)
    self.mainTable:row()

    -- Settings button
    self.settingsButton = self.ui:newTextButton(R.skin.default.button)
    self.settingsButton:setText(R.text.common.settings)
    self.settingsButton:setClickListener(function()
        if not self.netManager:isLoggedIn() then
            print("Not logged in!")
        else
            screen:setMenuScreen(R.screen.settings)
        end
    end)
    self.mainTable:add(self.settingsButton):fillX():spaceBottom(15)
    self.mainTable:row()

    self.ui:add(self.mainTable):expand()

    -- screen:playMusic(R.music.bg1)
end

function mainmenu:onLoginResult(success, errorCode, data)
    if not success then
        self.loginStatusLabel:setText(R.text.common.login_failed, errorCode)
        if errorCode then
            print("Login error: " .. errorCode)
        end
        return
    end
    self.loginStatusLabel:setText(R.text.common.logged_in)
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
