local screen = {}

function screen:onCreate(screen)
    self.screen = screen
    self.table = screen:newTable()
    self.startGameButton = screen:newButton()
    self.startGameButton:setOnClick(function(button)
        self.screen:setScreen(R.screen.game)
    end)
    self.table:addChild(self.startGameButton)
end
