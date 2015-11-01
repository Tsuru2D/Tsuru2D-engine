local settings = {}

function settings:onCreate(screen)
    print("mainmenu::onCreate()")
    self.screen = screen
    self.netManager = screen:getNetManager()
    self.ui = screen:getUIManager()
    self.settings = nil

    -- Status label
    self.statusLabel = self.ui:newLabel(R.skin.default.label)
    self.statusLabel:setText(R.text.common.reading_settings)
    self.ui:add(self.statusLabel):expandX():top():left()

    -- Save button
    self.saveButton = self.ui:newTextButton(R.skin.default.button)
    self.saveButton:setText(R.text.common.save)
    self.saveButton:setClickListener(function()
        self.netManager:writeGameSettings(self.settings, function(success, errorCode, data)
            self:onWriteSettingsResult(success, errorCode, data)
        end)
    end)
    self.ui:add(self.saveButton):expandX():top():right()
    self.ui:row()

    self.mainTable = self.ui:newTable()

    -- Slider
    self.slider = self.ui:newSlider(R.skin.default.slider)
    self.slider:setValueChangedListener(function(slider, value)
        print("Value: " .. value)
        self.settings.sliderValue = value
    end)
    self.mainTable:add(self.slider):width(200):fillX():spaceBottom(15)
    self.mainTable:row()

    -- CheckBox
    self.checkBox = self.ui:newCheckBox(R.skin.default.checkbox)
    self.checkBox:setText(R.text.common.check_me)
    self.checkBox:setCheckedChangedListener(function(checkbox, value)
        print("Checked: " .. (value and "true" or "false"))
        self.settings.checkBoxValue = value
    end)
    self.mainTable:add(self.checkBox):left():spaceBottom(15)
    self.mainTable:row()

    self.ui:add(self.mainTable):colspan(2):expand()

    self.netManager:readGameSettings(function(success, errorCode, data)
        self:onReadSettingsResult(success, errorCode, data)
    end)
end

function settings:onWriteSettingsResult(success, errorCode, data)
    if not success then
        self.statusLabel:setText(R.text.common.writing_settings_failed, errorCode)
        if errorCode then
            print("Write settings error: " .. errorCode)
        end
        return
    end
    self.screen:setMenuScreen(R.screen.mainmenu)
end

function settings:onReadSettingsResult(success, errorCode, data)
    if not success then
        self.statusLabel:setText(R.text.common.reading_settings_failed, errorCode)
        if errorCode then
            print("Read settings error: " .. errorCode)
        end
        return
    end
    self.settings = data
    self.slider:setValue(data.sliderValue or 0)
    self.checkBox:setChecked(data.checkBoxValue or false)
    self.statusLabel:setText(R.text.common.ready)
end

function settings:onResume(params)
    print("mainmenu::onResume()")
end

function settings:onPause()
    print("mainmenu::onPause()")
end

function settings:onDestroy()
    print("mainmenu::onDestroy()")
end

return settings
