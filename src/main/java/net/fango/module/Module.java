package net.fango.module;

import net.fango.module.setting.Setting;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Module {
    private final String   name;
    private final String   description;
    private final Category category;
    private       boolean  enabled = false;
    private       int      keybind = -1;

    protected final List<Setting<?>> settings = new ArrayList<>();

    protected Module(String name, String description, Category category) {
        this.name        = name;
        this.description = description;
        this.category    = category;
    }

    public final void toggle() {
        enabled = !enabled;
        if (enabled) onEnable(); else onDisable();
    }

    protected void onEnable()  {}
    protected void onDisable() {}
    public    void onTick()    {}

    public String           getName()        { return name; }
    public String           getDescription() { return description; }
    public Category         getCategory()    { return category; }
    public boolean          isEnabled()      { return enabled; }
    public int              getKeybind()     { return keybind; }
    public void             setKeybind(int k){ keybind = k; }
    public List<Setting<?>> getSettings()    { return Collections.unmodifiableList(settings); }

    protected <T> Setting<T> addSetting(Setting<T> s) {
        settings.add(s);
        return s;
    }
}
