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
        self.statusLabel:setText(R.text.common.writing_settings)
        self.netManager:writeGameSettings(self.settings, function(success, errorCode, data)
            self:onWriteSettingsResult(success, errorCode, data)
        end)
    end)
    self.ui:add(self.saveButton):expandX():top():right()
    self.ui:row()

    self.tabContainer = self.ui:newTabContainer(R.skin.default.tabcontainer)
    self.tabContainer:addTab(R.text.settingtext.tab1, function()
        local tabContent = self.ui:newTable()
        local slider = self.ui:newSlider(R.skin.default.slider)
        local label = self.ui:newLabel(R.skin.default.label)
        local checkBox = self.ui:newCheckBox(R.skin.default.checkbox)
        local localtable = self.ui:newTable()

        -- Sound CheckBox
        checkBox:setText(R.text.settingtext.checkboxtext)
        checkBox:setCheckedChangedListener(function(checkbox, value)
            print("Sound enabled: " .. (value and "true" or "false"))
            self.settings.soundEnabled = value
            slider:setEnabled(value)
        end)

        -- Volume Slider
        slider:setValueChangedListener(function(slider, value)
            print("Volume: " .. value)
            label:setText(R.text.settingtext.volumefmt, math.floor(value * 100))
            self.settings.volume = value
        end)

        localtable:add(checkBox):expandX():left():spaceBottom(30)
        localtable:row()
        localtable:add(label):width(200):fillX():spaceBottom(30)
        localtable:row()
        localtable:add(slider):width(200):fillX():spaceBottom(30)
        localtable:row()
        tabContent:add(localtable):expand()
        ------------------------------------------------------------------------------------------------
        self.refreshAction = function(data)
            slider:setValue(data.volume or 0)
            checkBox:setChecked(data.soundEnabled or false)
            label:setText(R.text.settingtext.volumefmt, math.floor((data.volume or 0) * 100))
            slider:setEnabled(checkBox:isChecked())
        end
        self.refreshAction(self.settings)
        return tabContent
    end)
    -----------------------------------------------------------------------------------
    self.tabContainer:addTab(R.text.settingtext.tab2, function()
        local tabContent = self.ui:newTable()
        local labeltable = self.ui:newTable()
        local label

        -- Dropdown
        local dropdown = self.ui:newDropDown(R.skin.default.dropdown)
        dropdown:setItems({
            {value = 1, text = R.text.settingtext.color1},
            {value = 2, text = R.text.settingtext.color2},
            {value = 3, text = R.text.settingtext.color3},
            {value = 4, text = R.text.settingtext.color4},
            {value = 5, text = R.text.settingtext.color5}
        })
        dropdown:setSelectedValue(1);
        tabContent:add(dropdown):expandX():width(200):spaceBottom(130)
        tabContent:row()
        tabContent:add(labeltable):expandX()
        self.changeLabelColor = function(value)
            if value == 1 then
                if label ~= nil then
                    labeltable:remove(label)
                end
                label = self.ui:newLabel(R.skin.colorfulLabel.labelblue)
                labeltable:add(label)
            end
            if value == 2 then
                if label ~= nil then
                    labeltable:remove(label)
                end
                label = self.ui:newLabel(R.skin.colorfulLabel.labelpink)
                labeltable:add(label)
            end
            if value == 3 then
                if label ~= nil then
                    labeltable:remove(label)
                end
                label = self.ui:newLabel(R.skin.colorfulLabel.labelbrown)
                labeltable:add(label)
            end
            if value == 4 then
                if label ~= nil then
                    labeltable:remove(label)
                end
                label = self.ui:newLabel(R.skin.colorfulLabel.labelorange)
                labeltable:add(label)
            end
            if value == 5 then
                if label ~= nil then
                    labeltable:remove(label)
                end
                label = self.ui:newLabel(R.skin.colorfulLabel.labelwhite)
                labeltable:add(label)
            end
            label:setText(R.text.settingtext.colorlabeltext)
        end
        dropdown:setValueChangedListener(function(dropdown, value)
            print("Dropdown value: " .. value)
            self.settings.dropdownValue = value
            self.changeLabelColor(dropdown:getSelectedValue())
        end)
        self.refreshAction = function(data)
            dropdown:setSelectedValue(data.dropdownValue or 1)
            self.changeLabelColor(dropdown:getSelectedValue())
        end
        self.refreshAction(self.settings)
        return tabContent
    end)

    self.ui:add(self.tabContainer):colspan(2):expandY():fill()

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
