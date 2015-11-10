local login = {}

function login:onCreate(screen)
    print("login::onCreate()")
    self.screen = screen
    self.netManager = screen:getNetManager()
    self.ui = screen:getUIManager()

    -- Login status label
    self.loginStatusLabel = self.ui:newLabel(R.skin.default.label)
    self.loginStatusLabel:setText(R.text.common.logged_out)
    self.ui:add(self.loginStatusLabel):top():left()
    self.ui:row()

    self.mainTable = self.ui:newTable()

    -- Username textbox
    self.usernameTextField = self.ui:newTextField(R.skin.skin.text)
    self.usernameTextField:setHint(R.text.common.username)
    self.usernameTextField:setText("test@test.com")
    self.usernameTextField:setTextChangedListener(function(textbox, value)
        print("Username: " .. value)
    end)
    self.mainTable:add(self.usernameTextField):fillX():height(40):padLeft(5):padRight(5)
    self.mainTable:row()

    -- Password textbox
    self.passwordTextField = self.ui:newTextField(R.skin.skin.text)
    self.passwordTextField:setText("abc")
    self.passwordTextField:setHint(R.text.common.password)
    self.passwordTextField:setPasswordMode(true)
    self.passwordTextField:setTextChangedListener(function(textbox, value)
        print("Password: " .. value)
    end)
    self.mainTable:add(self.passwordTextField):fillX():height(40):padLeft(5):padRight(5)
    self.mainTable:row()

    -- Login button
    self.loginButton = self.ui:newButton(R.skin.skin.login)
    self.loginButton:setClickListener(function()
        self.loginStatusLabel:setText(R.text.common.logging_in)
        self.loginButton:setEnabled(false)
        self.netManager:login(
            self.usernameTextField:getText(),
            self.passwordTextField:getText(),
            function(success, errorCode, data)
                self:onLoginResult(success, errorCode, data)
            end
        )
    end)

    self.mainTable:add(self.loginButton):fillX():width(200):height(100)
    self.ui:add(self.mainTable):expand()

    screen:setBackground(R.image.background)
end

function login:onLoginResult(success, errorCode, data)
    self.loginButton:setEnabled(true)
    if not success then
        self.loginStatusLabel:setText(R.text.common.login_failed, errorCode)
        print("Login error: " .. errorCode)
    else
        self.screen:setMenuScreen(R.screen.mainmenu)
    end
end

function login:onResume(params)
    print("login::onResume()")
end

function login:onPause()
    print("login::onPause()")
end

function login:onDestroy()
    print("login::onDestroy()")
end

return login
