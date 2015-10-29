local mainmenu = {}

function mainmenu:onCreate(screen)
    print("mainmenu::onCreate()")
    self.screen = screen
    local netManager = screen:getNetManager()
    local ui = screen:getUIManager()
    netManager:login("test@test.com", "abc", function(success, errorCode, data)
        print("register success:" .. (success and "true" or "false"))
        if errorCode then print("error: " .. errorCode) end
        netManager:writeGameSettings({lolis="cute"}, function(success, errorCode, data)
            print("write settings success:" .. (success and "true" or "false"))
            if errorCode then print("error: " .. errorCode) end
            netManager:readGameSettings(function(success, errorCode, data)
                print("read settings success:" .. (success and "true" or "false"))
                if errorCode then print("error: " .. errorCode) end
                print("lolis = " .. data.lolis)
            end)
        end)
    end)
    -- screen:playMusic(R.music.bg1)
    local startGameButton = ui:newButton()
    startGameButton:setStyle({
        up = R.image.button,
        down=R.image.button_2,
        hover=R.image.button_2
    })
    startGameButton:setOnClick(function()
        screen:setGameScreenNew(R.scene.simplescene1, {})
    end)
    ui:add(startGameButton):fillX()
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
