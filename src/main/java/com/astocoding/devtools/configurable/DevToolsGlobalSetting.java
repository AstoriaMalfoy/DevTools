package com.astocoding.devtools.configurable;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

@State(name = "devTools", storages = {@Storage("devTools.xml")})
public class DevToolsGlobalSetting implements PersistentStateComponent < DevToolsGlobalSetting > {

    public static DevToolsGlobalSetting getInstance(){
        return ApplicationManager.getApplication().getService( DevToolsGlobalSetting.class );
    }

    @Override
    public @Nullable DevToolsGlobalSetting getState() {
        return this;
    }

    @Override
    public void loadState( @NotNull DevToolsGlobalSetting devToolsGlobalSetting ) {
        this.autoFormatEnable = devToolsGlobalSetting.getAutoFormatEnable();
    }

    private Boolean autoFormatEnable;

    public void setAutoFormatEnable(Boolean autoFormatEnable) {
        this.autoFormatEnable = autoFormatEnable;
    }

    public Boolean getAutoFormatEnable() {
        return this.autoFormatEnable == null || autoFormatEnable;
    }
}




