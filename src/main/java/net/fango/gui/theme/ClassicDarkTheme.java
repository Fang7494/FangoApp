package net.fango.gui.theme;

public class ClassicDarkTheme implements Theme {
    @Override public String name()         { return "Classic Dark"; }
    @Override public int overlay()         { return 0x99000000; }
    @Override public int panelBg()         { return 0xFF1C1C1C; }
    @Override public int headerBg()        { return 0xFF111111; }
    @Override public int rowBg()           { return 0xFF222222; }
    @Override public int hover()           { return 0xFF2D2D2D; }
    @Override public int accent()          { return 0xFF00AAFF; }
    @Override public int accentDark()      { return 0xFF0077CC; }
    @Override public int badgeBg()         { return 0xFF333333; }
    @Override public int inputBg()         { return 0xFF1A1A1A; }
    @Override public int inputFocused()    { return 0xFF252525; }
    @Override public int toggleOff()       { return 0xFF444444; }
    @Override public int textBright()      { return 0xFFFFFFFF; }
    @Override public int textMuted()       { return 0xFF999999; }
    @Override public int muted()           { return 0xFF555555; }
}
