local splash = {}

function splash:onCreate(screen)
    print("splash::onCreate()")
    self.screen = screen
    self.netManager = screen:getNetManager()
    self.ui = screen:getUIManager()

    self.loadingLabel = self.ui:newLabel(R.skin.default.label)
    self.loadingLabel:setText(R.text.common.loading)

    self.ui:add(self.loadingLabel):expand()
end

function splash:onResume(params)
    print("splash::onResume()")
    if self.netManager:isLoggedIn() then
        self.screen:setMenuScreen(R.screen.mainmenu)
    else
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
