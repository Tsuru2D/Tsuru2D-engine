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
    self.usernameTextField = self.ui:newTextField(R.skin.default.textbox)
    self.usernameTextField:setHint(R.text.common.username)
    self.usernameTextField:setText("test@test.com")
    self.usernameTextField:setTextChangedListener(function(textbox, value)
        print("Username: " .. value)
    end)
    self.mainTable:add(self.usernameTextField):fillX():height(55):padLeft(5):padRight(5):padBottom(5):colspan(2)
    self.mainTable:row()

    -- Password textbox
    self.passwordTextField = self.ui:newTextField(R.skin.default.textbox)
    self.passwordTextField:setText("abc")
    self.passwordTextField:setHint(R.text.common.password)
    self.passwordTextField:setPasswordMode(true)
    self.passwordTextField:setTextChangedListener(function(textbox, value)
        print("Password: " .. value)
    end)
    self.mainTable:add(self.passwordTextField):fillX():height(55):padLeft(5):padRight(5):padBottom(5):colspan(2)
    self.mainTable:row()

    -- Login button
    self.loginButton = self.ui:newButton(R.skin.imagebutton.login)
    self.loginButton:setClickListener(function()
        self.loginStatusLabel:setText(R.text.common.logging_in)
        self.loginButton:setEnabled(false)
        self.registerButton:setEnabled(false)
        local username = self.usernameTextField:getText()
        local password = self.passwordTextField:getText()
        self.netManager:login(username, password, function(success, errorCode, data)
            self.loginButton:setEnabled(true)
            self.registerButton:setEnabled(true)
            if not success then
                self.loginStatusLabel:setText(R.text.common.login_failed, errorCode)
                print("Login error: " .. errorCode)
            else
                self.screen:setMenuScreen(R.screen.mainmenu)
            end
        end)
    end)
    self.mainTable:add(self.loginButton):fillX():width(200):height(100)

    -- Register button
    self.registerButton = self.ui:newButton(R.skin.imagebutton.login)
    self.registerButton:setClickListener(function()
        self.loginStatusLabel:setText(R.text.common.registering)
        self.loginButton:setEnabled(false)
        self.registerButton:setEnabled(false)
        local username = self.usernameTextField:getText()
        local password = self.passwordTextField:getText()
        self.netManager:register(username, password, function(success, errorCode, data)
            self.loginButton:setEnabled(true)
            self.registerButton:setEnabled(true)
            if not success then
                self.loginStatusLabel:setText(R.text.common.registering_failed, errorCode)
                print("Registration error: " .. errorCode)
            else
                self.screen:setMenuScreen(R.screen.mainmenu)
            end
        end)
    end)
    self.mainTable:add(self.registerButton):fillX():width(200):height(100)

    self.ui:add(self.mainTable):expand()

    -- screen:setBackground(R.image.background.mainmenu_bg)
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
