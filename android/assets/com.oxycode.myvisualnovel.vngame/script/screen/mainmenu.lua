local mainmenu = {}

function mainmenu:onCreate(screen)
    print("mainmenu::onCreate()")
    self.screen = screen
    screen:playMusic(R.music.bg1)
    screen:newButton("game", function()
        screen:pushScreen(R.screen.game, {
            lolis = "cute"
        })
    end)
end

function mainmenu:onResume(params)
    print("mainmenu::onResume()")
    if params ~= nil then
        print(params.oxygen)
    end
end

function mainmenu:onPause()
    print("mainmenu::onPause()")
end

function mainmenu:onDestroy()
    print("mainmenu::onDestroy()")
end

return mainmenu
