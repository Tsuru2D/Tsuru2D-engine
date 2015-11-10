local splash = {}
function splash:onCreate(screen)
    print("splash::onCreate()")
    self.screen = screen
    self.netManager = screen:getNetManager()
    self.ui = screen:getUIManager()

    screen:setBackground(R.image.background)

    self.mainTable = self.ui:newTable()
    self.loadingLable = self.ui:newLabel(R.skin.default.label)
    self.loadingLable:setText(R.text.common.loading)


    self.ui:add(self.loadingLable):top():left()
    self.ui:row()

    self.ui:add(self.mainTable):expand():bottom():left():padBottom(55)
end

function splash:onResume(params)
    print("splash::onResume()")

    if self.netManager:isLoggedIn() then
        print("to mainmenu")
        self.screen:setMenuScreen(R.screen.mainmenu)
    else
        print("to login")
        self.screen:setMenuScreen(R.screen.login)
    end
end

function splash:onPause()
    print("splash::onPause()")
end

function splash:onDestroy()
    print("splash::onDestroy()")
end

return splash
