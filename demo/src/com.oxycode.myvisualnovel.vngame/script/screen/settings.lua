local settings = {}

function settings:onCreate(screen)
    print("settings::onCreate()")
    self.screen = screen
    self.netManager = screen:getNetManager()
    self.ui = screen:getUIManager()
    self.settings = {}
    self.refreshAction = nil

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

    self.tabContainer = self.ui:newTabContainer(R.skin.default.tabcontainer)
    self.tabContainer:addTab(R.text.common.settings1, function()
        local tabContent = self.ui:newTable()

        -- Slider
        local slider = self.ui:newSlider(R.skin.default.slider)
        slider:setValueChangedListener(function(slider, value)
            print("Slider value: " .. value)
            self.settings.sliderValue = value
        end)
        tabContent:add(slider):width(200):fillX():spaceBottom(15)
        tabContent:row()

        -- CheckBox
        local checkBox = self.ui:newCheckBox(R.skin.default.checkbox)
        checkBox:setText(R.text.common.check_me)
        checkBox:setCheckedChangedListener(function(checkbox, value)
            print("Checkbox value: " .. (value and "true" or "false"))
            self.settings.checkBoxValue = value
        end)
        tabContent:add(checkBox):left():spaceBottom(15)

        self.refreshAction = function(data)
            slider:setValue(data.sliderValue or 0)
            checkBox:setChecked(data.checkBoxValue or false)
        end
        self.refreshAction(self.settings)
        return tabContent
    end)
    self.tabContainer:addTab(R.text.common.settings2, function()
        local tabContent = self.ui:newTable()

        -- Dropdown
        local dropdown = self.ui:newDropDown(R.skin.default.dropdown)
        dropdown:setItems({
            {value = 1, text = R.text.common.dropdown_item1},
            {value = 2, text = R.text.common.dropdown_item2},
            {value = 3, text = R.text.common.dropdown_item3},
            {value = 4, text = R.text.common.dropdown_item4}
        })
        dropdown:setSelectedValue(1);
        dropdown:setValueChangedListener(function(dropdown, value)
            print("Dropdown value: " .. value)
            self.settings.dropdownValue = value
        end)
        tabContent:add(dropdown):width(200):fillX()

        self.refreshAction = function(data)
            dropdown:setSelectedValue(data.dropdownValue or 1)
        end
        self.refreshAction(self.settings)
        return tabContent
    end)
    self.ui:add(self.tabContainer):colspan(2):fill()

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
    if self.refreshAction then
        self.refreshAction(data)
    end
    self.statusLabel:setText(R.text.common.ready)
end

function settings:onResume(params)
    print("settings::onResume()")
end

function settings:onPause()
    print("settings::onPause()")
end

function settings:onDestroy()
    print("settings::onDestroy()")
end

return settings
