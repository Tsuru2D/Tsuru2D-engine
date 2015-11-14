local save={}

function save:onCreate(screen)
    self.screen = screen
    self.netManager = screen:getNetManager()
    self.ui = screen:getUIManager()

    self.controlTable = self.ui:newTable()

    --savebutton
    self.saveButton = self.ui:newTextButton(R.skin.default.button)
    self.saveButton:setText(R.text.common.save)
    self.saveButton:setClickListener(function()
        self.netManager:saveLocal(0)
    end)
    self.controlTable:add(self.saveButton)
    self.ui:add(self.controlTable):top():right()
    self.ui:row()

    --savebutton2
    self.saveButton2 = self.ui:newTextButton(R.skin.default.button)
    self.saveButton2:setText(R.text.common.save2)
    self.saveButton2:setClickListener(function()
        self.netManager:saveLocal(1);
    end)
    self.controlTable:add(self.saveButton2)
    self.ui:add(self.controlTable):top():right()
    self.ui:row()

    --loadButton
    self.loadButton = self.ui:newTextButton(R.skin.default.button)
    self.loadButton:setText(R.text.common.load)
    self.loadButton:setClickListener(function()
        self.netManager:loadSave(0, function(data)
            local saveData = data
            if not saveData then
                print("No save data")
                return
            end
            self.screen:setGameScreenResume(saveData, {})
        end)
    end)
    self.controlTable:add(self.loadButton)
    self.ui:add(self.controlTable):top():right()
    self.ui:row()

    --loadbutton2
    self.loadbutton2 = self.ui:newTextButton(R.skin.default.button)
    self.loadbutton2:setText(R.text.common.load2)
    self.loadbutton2:setClickListener(function()
        print("load2")
        self.netManager:loadSave(1, function(data)
            local saveData = data
            if not saveData then
                print("No save data")
                return
            end
            self.screen:setGameScreenResume(saveData, {})
        end)
    end)
    self.controlTable:add(self.loadbutton2)
    self.ui:add(self.controlTable):top():right()
    self.ui:row()

    --syncButton
    self.syncButton = self.ui:newTextButton(R.skin.default.button)
    self.syncButton:setText(R.text.common.syncsave)
    self.syncButton:setClickListener(function()
        self.netManager:writeSave(true, function() end)
    end)
    self.controlTable:add(self.syncButton)
    self.ui:add(self.controlTable):top():right()
    self.ui:row()

    --downloadSaveButton
    self.downloadSaveButton = self.ui:newTextButton(R.skin.default.button)
    self.downloadSaveButton:setText(R.text.common.downloadsave)
    self.downloadSaveButton:setClickListener(function()
        self.netManager:enumerateSaves(0, 100, function(success, errorCode, data)
            if not success then
                if errorCode then
                    print("Loading save error: " .. errorCode)
                end
                return
            end
        end)
    end)
    self.controlTable:add(self.downloadSaveButton)
    self.ui:add(self.controlTable):top():right()
    self.ui:row()



    self.ui:add(self.controlTable):top():right()

end


function save:onResume(params)
    print("save::onResume()")
end

function save:onPause()
    print("save::onPause()")
end

function save:onDestroy()
    print("save::onDestroy()")
end

return save
