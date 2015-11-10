local mainmenu = {}

function mainmenu:onCreate(screen)
    print("mainmenu::onCreate()")
    self.screen = screen
    self.netManager = screen:getNetManager()
    self.ui = screen:getUIManager()

    self.mainTable = self.ui:newTable()

    -- Start game button
    self.startGameButton = self.ui:newButton(R.skin.imagebutton.start)
    self.startGameButton:setClickListener(function()
        screen:setGameScreenNew(R.scene.simplescene1, {})
    end)
    self.mainTable:add(self.startGameButton):fillX():width(200):height(100)
    self.mainTable:row()

    -- Continue game button
    self.continueGameButton = self.ui:newButton(R.skin.imagebutton.continue)
    self.continueGameButton:setClickListener(function()
        self.netManager:enumerateSaves(0, 0, function(success, errorCode, data)
            if not success then
                if errorCode then
                    print("Loading save error: " .. errorCode)
                end
                return
            end
            local saveData = data[1]
            if not saveData then
                print("No save data")
                return
            end
            self.screen:setGameScreenResume(saveData, {})
        end)
    end)
    self.mainTable:add(self.continueGameButton):fillX():width(200):height(100)
    self.mainTable:row()

    -- Settings button
    self.settingsButton = self.ui:newButton(R.skin.imagebutton.settings)
    self.settingsButton:setClickListener(function()
        screen:setMenuScreen(R.screen.settings)
    end)
    self.mainTable:add(self.settingsButton):fillX():spaceBottom(15):width(200):height(100)
    self.mainTable:row()

    -- Logout button
    self.logoutButton = self.ui:newButton(R.skin.imagebutton.logout)
    self.logoutButton:setClickListener(function()
        self.netManager:logout()
        self.screen:setMenuScreen(R.screen.login)
    end)
    self.mainTable:add(self.logoutButton):fillX():spaceBottom(15):width(200):height(100)
    self.mainTable:row()

    self.ui:add(self.mainTable):expand()

    -- screen:playMusic(R.music.bg1)
    -- screen:setBackground(R.image.background)
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
