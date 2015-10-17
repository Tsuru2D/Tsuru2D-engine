local mainmenu = {}

function mainmenu:onCreate(screen)
    print(screen)
    print(self)
    self.screen = screen
    screen:playMusic(R.music.bg1)
    --[[self.table = screen:newTable()
    self.startGameButton = screen:newButton()
    self.startGameButton:setOnClick(function(button)
        self.screen:setScreen(R.screen.game)
    end)
    self.table:addChild(self.startGameButton)--]]
end

return mainmenu
