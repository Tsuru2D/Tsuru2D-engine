local game = {}

function game:onCreate(screen)
    print("game::onCreate()")
    self.screen = screen
    screen:newButton("back to main menu", function()
        screen:popScreen({
            oxygen = 8
        })
    end)
    screen:newButton("click me #2", function()
        screen:popScreen({
            oxygen = 8
        })
    end)
end

function game:onResume(params)
    print("game::onResume()")
    print("lolis: " .. params.lolis)
end

function game:onPause()
    print("game::onPause()")
end

function game:onMusic()
    print("game::onMusic()")
end

function game:onDestroy()
    print("game::onDestroy()")
end

return game
