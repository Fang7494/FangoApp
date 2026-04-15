package net.fango.gui.theme;

public class MeteorDarkTheme implements Theme {
    @Override public String name()         { return "Meteor Dark"; }
    @Override public int overlay()         { return 0xAA000000; }
    @Override public int panelBg()         { return 0xFF141418; }
    @Override public int headerBg()        { return 0xFF0D0D0F; }
    @Override public int rowBg()           { return 0xFF1A1A20; }
    @Override public int hover()           { return 0xFF222230; }
    @Override public int accent()          { return 0xFF7B68EE; }
    @Override public int accentDark()      { return 0xFF5A4ECC; }
    @Override public int badgeBg()         { return 0xFF2A2A35; }
    @Override public int inputBg()         { return 0xFF1E1E26; }
    @Override public int inputFocused()    { return 0xFF252535; }
    @Override public int toggleOff()       { return 0xFF3A3A4A; }
    @Override public int textBright()      { return 0xFFE0E0E0; }
    @Override public int textMuted()       { return 0xFF888899; }
    @Override public int muted()           { return 0xFF555566; }
}
