package net.fango.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.fango.FangoClient;
import net.fango.gui.theme.Theme;
import net.fango.module.Category;
import net.fango.module.Module;
import java.util.List;

public class ClickGUI extends Screen {

    private static final int PANEL_W  = 180;
    private static final int CAT_H    = 28;
    private static final int MOD_H    = 24;
    private static final int TOGGLE_W = 28;
    private static final int TOGGLE_H = 14;
    private static final int SEARCH_H = 26;
    private static final int PADDING  = 10;
    private static final int RADIUS   = 4;

    private Category selectedCategory = Category.COMBAT;
    private String   searchQuery      = "";
    private boolean  searchFocused    = false;
    private Module   expandedModule   = null;
    private int      scrollOffset     = 0;

    public ClickGUI() {
        super(Text.literal(FangoClient.NAME));
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        Theme t = FangoClient.themes.active();

        ctx.fill(0, 0, width, height, t.overlay());

        int guiH   = height - 60;
        int guiY   = 30;
        int totalW = PANEL_W + 320;
        int guiX   = (width - totalW) / 2;

        // Left panel
        drawRoundRect(ctx, guiX, guiY, PANEL_W, guiH, RADIUS, t.panelBg());
        ctx.fill(guiX, guiY, guiX + PANEL_W, guiY + 36, t.headerBg());
        drawCenteredText(ctx, FangoClient.NAME,    guiX + PANEL_W / 2, guiY + 10, t.accent(),   true);
        drawCenteredText(ctx, "v" + FangoClient.VERSION, guiX + PANEL_W / 2, guiY + 24, t.muted(), false);

        int catY = guiY + 42;
        for (Category cat : Category.values()) {
            boolean active  = cat == selectedCategory;
            boolean hovered = isHov(mx, my, guiX + 4, catY, PANEL_W - 8, CAT_H);
            int bg = active ? t.accent() : (hovered ? t.hover() : 0);
            if (bg != 0) drawRoundRect(ctx, guiX + 4, catY, PANEL_W - 8, CAT_H, RADIUS, bg);
            ctx.drawText(textRenderer, cat.displayName(), guiX + 14, catY + 8,
                active ? t.textBright() : t.textMuted(), false);
            int count = FangoClient.modules.getByCategory(cat).size();
            String badge = String.valueOf(count);
            int bw = textRenderer.getWidth(badge) + 8;
            drawRoundRect(ctx, guiX + PANEL_W - bw - 6, catY + 7, bw, 13, 3,
                active ? t.accentDark() : t.badgeBg());
            ctx.drawText(textRenderer, badge, guiX + PANEL_W - bw - 2, catY + 9, t.textBright(), false);
            catY += CAT_H + 2;
        }

        // Center panel
        int cx = guiX + PANEL_W + 4;
        int cw = 316;
        drawRoundRect(ctx, cx, guiY, cw, guiH, RADIUS, t.panelBg());

        // Search bar
        drawRoundRect(ctx, cx + PADDING, guiY + PADDING, cw - PADDING * 2, SEARCH_H, RADIUS,
            searchFocused ? t.inputFocused() : t.inputBg());
        String ph = searchQuery.isEmpty() ? "Search modules..." : searchQuery;
        ctx.drawText(textRenderer, ph, cx + PADDING + 8, guiY + PADDING + 7,
            searchQuery.isEmpty() ? t.textMuted() : t.textBright(), false);

        // Module list
        List<Module> mods = FangoClient.modules.getByCategory(selectedCategory);
        if (!searchQuery.isEmpty()) {
            mods = mods.stream()
                .filter(m -> m.getName().toLowerCase().contains(searchQuery.toLowerCase()))
                .toList();
        }

        int modY = guiY + PADDING + SEARCH_H + 6 - scrollOffset;
        for (Module mod : mods) {
            if (modY + MOD_H < guiY || modY > guiY + guiH) { modY += MOD_H + 2; continue; }
            boolean hov = isHov(mx, my, cx + PADDING, modY, cw - PADDING * 2, MOD_H);
            drawRoundRect(ctx, cx + PADDING, modY, cw - PADDING * 2, MOD_H, RADIUS,
                hov ? t.hover() : t.rowBg());
            ctx.drawText(textRenderer, mod.getName(), cx + PADDING + 8, modY + 7,
                mod.isEnabled() ? t.textBright() : t.textMuted(), false);
            int tx = cx + cw - PADDING - TOGGLE_W - 4;
            int ty = modY + (MOD_H - TOGGLE_H) / 2;
            drawToggle(ctx, tx, ty, mod.isEnabled(), t);
            if (!mod.getSettings().isEmpty()) {
                ctx.drawText(textRenderer, mod == expandedModule ? "\u25b2" : "\u25bc",
                    cx + cw - PADDING - TOGGLE_W - 16, modY + 7, t.textMuted(), false);
            }
            modY += MOD_H + 2;
            if (mod == expandedModule) {
                modY = SettingsRenderer.render(ctx, textRenderer, mod,
                    cx + PADDING, modY, cw - PADDING * 2, t, mx, my);
                modY += 4;
            }
        }
        super.render(ctx, mx, my, delta);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int btn) {
        Theme t = FangoClient.themes.active();
        int guiH   = height - 60;
        int guiY   = 30;
        int totalW = PANEL_W + 320;
        int guiX   = (width - totalW) / 2;

        int catY = guiY + 42;
        for (Category cat : Category.values()) {
            if (isHov((int)mx, (int)my, guiX + 4, catY, PANEL_W - 8, CAT_H)) {
                selectedCategory = cat;
                expandedModule   = null;
                scrollOffset     = 0;
                return true;
            }
            catY += CAT_H + 2;
        }

        int cx = guiX + PANEL_W + 4;
        int cw = 316;
        List<Module> mods = FangoClient.modules.getByCategory(selectedCategory);
        if (!searchQuery.isEmpty()) {
            mods = mods.stream()
                .filter(m -> m.getName().toLowerCase().contains(searchQuery.toLowerCase()))
                .toList();
        }
        int modY = guiY + PADDING + SEARCH_H + 6 - scrollOffset;
        for (Module mod : mods) {
            if (isHov((int)mx, (int)my, cx + PADDING, modY, cw - PADDING * 2, MOD_H)) {
                int tx = cx + cw - PADDING - TOGGLE_W - 4;
                if (mx >= tx && mx <= tx + TOGGLE_W) {
                    mod.toggle();
                } else {
                    expandedModule = (expandedModule == mod) ? null : mod;
                }
                return true;
            }
            modY += MOD_H + 2;
            if (mod == expandedModule) {
                if (SettingsRenderer.handleClick(mod, (int)mx, (int)my,
                    cx + PADDING, modY, cw - PADDING * 2, t)) return true;
                modY += SettingsRenderer.getHeight(mod) + 4;
            }
        }

        if (isHov((int)mx, (int)my, cx + PADDING, guiY + PADDING, cw - PADDING * 2, SEARCH_H)) {
            searchFocused = true; return true;
        }
        searchFocused = false;
        return super.mouseClicked(mx, my, btn);
    }

    @Override
    public boolean keyPressed(int key, int scan, int mods) {
        if (key == org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE) { close(); return true; }
        if (searchFocused && key == org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE && !searchQuery.isEmpty()) {
            searchQuery = searchQuery.substring(0, searchQuery.length() - 1);
            return true;
        }
        return super.keyPressed(key, scan, mods);
    }

    @Override
    public boolean charTyped(char c, int mods) {
        if (searchFocused) { searchQuery += c; return true; }
        return super.charTyped(c, mods);
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double hAmt, double vAmt) {
        scrollOffset = Math.max(0, scrollOffset - (int)(vAmt * 10));
        return true;
    }

    @Override public boolean shouldPause() { return false; }

    private void drawRoundRect(DrawContext ctx, int x, int y, int w, int h, int r, int color) {
        ctx.fill(x + r, y,     x + w - r, y + h,     color);
        ctx.fill(x,     y + r, x + r,     y + h - r, color);
        ctx.fill(x + w - r, y + r, x + w, y + h - r, color);
        ctx.fill(x,         y,         x + r,     y + r,     color);
        ctx.fill(x + w - r, y,         x + w,     y + r,     color);
        ctx.fill(x,         y + h - r, x + r,     y + h,     color);
        ctx.fill(x + w - r, y + h - r, x + w,     y + h,     color);
    }

    private void drawToggle(DrawContext ctx, int x, int y, boolean on, Theme t) {
        int track = on ? t.accent() : t.toggleOff();
        drawRoundRect(ctx, x, y, TOGGLE_W, TOGGLE_H, TOGGLE_H / 2, track);
        int kx = on ? x + TOGGLE_W - TOGGLE_H + 2 : x + 2;
        ctx.fill(kx, y + 2, kx + TOGGLE_H - 4, y + TOGGLE_H - 2, t.textBright());
    }

    private void drawCenteredText(DrawContext ctx, String text, int cx, int y, int color, boolean shadow) {
        ctx.drawText(textRenderer, text, cx - textRenderer.getWidth(text) / 2, y, color, shadow);
    }

    private boolean isHov(int mx, int my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
}
