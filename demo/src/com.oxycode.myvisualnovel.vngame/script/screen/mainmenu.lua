local mainmenu = {}

function mainmenu:onCreate(screen)
    print("mainmenu::onCreate()")
    self.screen = screen
    -- screen:playMusic(R.music.bg1)
    local startGameButton = screen:newButton()
    -- startGameButton:setText(R.text.common.start_game)
    startGameButton:setStyle({
        up = R.image.button,
        down=R.image.button_2,
        hover=R.image.button_2
    })
    startGameButton:setOnClick(function()
        screen:setGameScreenNew(R.scene.simplescene1, {})
    end)
    screen:add(startGameButton):fillX()

    -- local b2 = screen:newButton()
    -- b2:setStyle({
    --     up = R.image.windows,
    --     down=R.image.windows2,
    --     hover=R.image.windows2,
    -- })
    -- b2:setOnClick(function()
    --     print("hi")
    -- end)
    -- screen:add(b2):fillX()
    
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
