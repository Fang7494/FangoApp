package net.fango.gui.theme;

import java.util.ArrayList;
import java.util.List;

public class ThemeManager {
    private final List<Theme> themes = new ArrayList<>();
    private int activeIndex = 0;

    public ThemeManager() {
        themes.add(new MeteorDarkTheme());
        themes.add(new ClassicDarkTheme());
    }

    public Theme  active()              { return themes.get(activeIndex); }
    public List<Theme> all()            { return themes; }
    public void   setActive(int i)      { activeIndex = Math.max(0, Math.min(i, themes.size() - 1)); }
    public void   setActive(String name){ for (int i = 0; i < themes.size(); i++) if (themes.get(i).name().equals(name)) { activeIndex = i; return; } }
    public void   cycle()               { activeIndex = (activeIndex + 1) % themes.size(); }
}
