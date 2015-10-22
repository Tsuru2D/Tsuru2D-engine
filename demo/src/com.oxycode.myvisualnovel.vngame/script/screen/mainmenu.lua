local mainmenu = {}

function mainmenu:onCreate(screen)
    print("mainmenu::onCreate()")
    self.screen = screen
    -- screen:playMusic(R.music.bg1)
    local startGameButton = screen:newButton()
    startGameButton:setText(R.text.common.start_game)
    startGameButton:setOnClick(function()
        screen:setGameScreenNew(R.scene.simplescene1, {})
    end)
    screen:add(startGameButton):fillX()
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
