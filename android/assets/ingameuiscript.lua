local screen = {}

function screen:onCreate()
    self.table = self:newTable()
    local startGameButton = screen:newButton()
    startGameButton:setOnClick(function(button)
        screen:setScreen(R.screen.game)
    end)
    buttonTable:addChild(startGameButton)
end

function screen:onEnterFrame(frame)

end

function screen:onText(text)
    self.textbox:setText(text)
end

function screen:onSound(sound)

    screen:playSound(sound)
end

function screen:onVoice(voice)
    screen:playVoice(voice)
end

function screen:onInteractive(info)
    if info.type == "textbox" then
        -- blah
    end
end

return screen
