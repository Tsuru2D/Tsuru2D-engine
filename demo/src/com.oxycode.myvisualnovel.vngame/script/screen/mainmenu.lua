local mainmenu = {}

function mainmenu:onCreate(screen)
    print("mainmenu::onCreate()")
    self.screen = screen
    -- screen:playMusic(R.music.bg1)
    local startGameButton = screen:newTextButton()
    startGameButton:setText(R.text.common.start_game)
    startGameButton:setStyle({
        up = R.image.play1,
        down=R.image.play2,
        hover=R.image.play2
    })
    startGameButton:setOnClick(function()
        screen:setGameScreenNew(R.scene.simplescene1, {})
    end)
    screen:add(startGameButton):fillX()

    local b2 = screen:newButton()
    b2:setStyle({
        up = R.image.b1,
        down=R.image.b2,
        hover=R.image.b3
    })
    b2:setOnClick(function()
        print("ih")
    end)
    screen:add(b2):fillX()
    
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
