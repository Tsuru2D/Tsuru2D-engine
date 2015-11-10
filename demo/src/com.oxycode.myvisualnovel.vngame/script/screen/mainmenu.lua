local mainmenu = {}

function mainmenu:onCreate(screen)
    print("mainmenu::onCreate()")
    self.screen = screen
    self.netManager = screen:getNetManager()
    self.ui = screen:getUIManager()

    self.mainTable = self.ui:newTable()

    -- Start game button
    self.startGameButton = self.ui:newButton(R.skin.skin.start)
    self.startGameButton:setClickListener(function()
        screen:setGameScreenNew(R.scene.simplescene1, {})
    end)
    self.mainTable:add(self.startGameButton):fillX():width(200):height(100)
    self.mainTable:row()

    -- Settings button
    self.settingsButton = self.ui:newButton(R.skin.skin.setting)
    self.settingsButton:setClickListener(function()
        if not self.netManager:isLoggedIn() then
            self.loginStatusLabel:setText(R.text.common.not_logged_in)
        else
            screen:setMenuScreen(R.screen.settings)
        end
    end)
    self.mainTable:add(self.settingsButton):fillX():spaceBottom(15):width(200):height(100)
    self.mainTable:row()

    -- Logout button
    self.logoutButton = self.ui:newButton(R.skin.skin.logout)
    self.logoutButton:setClickListener(function()
        self.netManager:logout()
        self.screen:setMenuScreen(R.screen.login)
    end)
    self.mainTable:add(self.logoutButton):fillX():spaceBottom(15):width(200):height(100)
    self.mainTable:row()

    self.ui:add(self.mainTable):expand()

    -- screen:playMusic(R.music.bg1)
    screen:setBackground(R.image.background)
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
