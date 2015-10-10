function onCreate(screen)
    local buttonTable = screen:newTable()
    local startGameButton = screen:newButton()
    startGameButton:setOnClick(function(button)
        screen:setScreen(R.screen.game)
    end)
    buttonTable:addChild(startGameButton)
end
