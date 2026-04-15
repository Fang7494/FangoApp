package net.fango.module;

public enum Category {
    COMBAT   ("Combat"),
    PLAYER   ("Player"),
    MOVEMENT ("Movement"),
    RENDER   ("Render"),
    WORLD    ("World"),
    MISC     ("Misc");

    private final String display;
    Category(String display) { this.display = display; }
    public String displayName() { return display; }
}
