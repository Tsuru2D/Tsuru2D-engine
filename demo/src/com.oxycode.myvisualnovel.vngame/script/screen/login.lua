local login={}
function login:onCreate(screen)
    print("login::onCreate()")
    self.screen = screen
    self.netManager = screen:getNetManager()
    self.ui = screen:getUIManager()

    -- Login status label
    self.loginStatusLabel = self.ui:newLabel(R.skin.default.label)
    if self.netManager:isLoggedIn() then
        self.loginStatusLabel:setText(R.text.common.logged_in)
    else
        self.loginStatusLabel:setText(R.text.common.logged_out)
    end
    self.ui:add(self.loginStatusLabel):top():left()
    self.ui:row()

    self.mainTable = self.ui:newTable()
    self.logTable= self.ui:newTable()


    -- Username textbox
    self.usernameTextField = self.ui:newTextField(R.skin.skin.text)
    self.usernameTextField:setHint(R.text.common.username)
    self.usernameTextField:setText("test@test.com")
    self.usernameTextField:setTextChangedListener(function(textbox, value)
        print("Username: " .. value)
    end)
    self.logTable:add(self.usernameTextField):fillX():height(60):padLeft(10):padRight(10)
    self.logTable:row()

    -- Password textbox
    self.passwordTextField = self.ui:newTextField(R.skin.skin.text)
    self.passwordTextField:setText("abc")
    self.passwordTextField:setHint(R.text.common.password)
    self.passwordTextField:setPasswordMode(true)
    self.passwordTextField:setTextChangedListener(function(textbox, value)
        print("Password: " .. value)
    end)
    self.logTable:add(self.passwordTextField):fillX():height(60):padLeft(10):padRight(10)
    self.logTable:row()

    -- Login button
    self.loginButton = self.ui:newToggleButton(R.skin.skin.login)
    if self.netManager:isLoggedIn() then
        self.loginButton:setChecked(true)
    else
        self.loginButton:setChecked(false)
    end
    self.loginButton:setClickListener(function()

            self.loginButton:setEnabled(false)
            self.loginButton:setChecked(false)

            self.netManager:login(
                self.usernameTextField:getText(),
                self.passwordTextField:getText(),
                function(success, errorCode, data)
                    self:onLoginResult(success, errorCode, data)
                end
            )
    end)

    self.mainTable:add(self.loginButton):fillX():width(200):height(100)
    self.mainTable:row()

    self.ui:add(self.logTable):width(150):bottom():left():padBottom(200):padRight(150):padLeft(175)
    self.ui:add(self.mainTable):expand():bottom():left():padBottom(200)


    screen:setBackground(R.image.background)
end


function login:onLoginResult(success, errorCode, data)
    self.loginButton:setEnabled(true)
    print ("enable")
    if not success then
        self.loginButton:setChecked(false)
        self.loginStatusLabel:setText(R.text.common.login_failed, errorCode)
        if errorCode then
            print("Login error: " .. errorCode)
        end
        return
    end
    self.loginStatusLabel:setText(R.text.common.logged_in)
    self.screen:setMenuScreen(R.screen.mainmenu)
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
