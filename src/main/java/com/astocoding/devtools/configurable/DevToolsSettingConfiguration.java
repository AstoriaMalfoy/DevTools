package com.astocoding.devtools.configurable;

import com.astocoding.devtools.constant.Constant;
import com.astocoding.devtools.ui.DevToolsSettingUI;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class DevToolsSettingConfiguration implements Configurable {

    private DevToolsSettingUI devToolsSettingUI ;
    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return Constant.SETTING_PANEL_TITLE;
    }

    @Override
    public @Nullable JComponent createComponent() {
        DevToolsGlobalSetting settingInstance = DevToolsGlobalSetting.getInstance();
        DevToolsSettingUI devToolsSettingUI = new DevToolsSettingUI();
        this.devToolsSettingUI = devToolsSettingUI;
        devToolsSettingUI.getIsOpenAutoFormat().setSelected(settingInstance.getAutoFormatEnable());
        return devToolsSettingUI.getMainJPanel();
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {
        DevToolsGlobalSetting devToolsGlobalSetting = DevToolsGlobalSetting.getInstance();
        devToolsGlobalSetting.setAutoFormatEnable(
                devToolsSettingUI.getIsOpenAutoFormat().isSelected()
        );
    }
}
