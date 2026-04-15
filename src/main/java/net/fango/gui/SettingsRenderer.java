package net.fango.gui;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.fango.gui.theme.Theme;
import net.fango.module.Module;
import net.fango.module.setting.Setting;
import java.util.List;

public final class SettingsRenderer {

    private static final int ROW_H   = 20;
    private static final int PADDING = 8;

    public static int render(DrawContext ctx, TextRenderer tr, Module mod,
                             int x, int y, int w, Theme t, int mx, int my) {
        List<Setting<?>> settings = mod.getSettings();
        int totalH = settings.size() * (ROW_H + 2) + PADDING * 2;
        ctx.fill(x, y, x + w, y + totalH, t.inputBg());

        int ry = y + PADDING;
        for (Setting<?> s : settings) {
            ctx.drawText(tr, s.getName(), x + PADDING, ry + 4, t.textMuted(), false);
            switch (s.getType()) {
                case BOOLEAN -> renderBool(ctx, (Setting<Boolean>) s, x + w - 36, ry, t);
                case INTEGER -> renderIntSlider(ctx, tr, (Setting<Integer>) s, x + w / 2, ry, w / 2 - PADDING, t);
                case DOUBLE  -> renderDblSlider(ctx, tr, (Setting<Double>)  s, x + w / 2, ry, w / 2 - PADDING, t);
                case ENUM    -> renderEnum(ctx, tr, s, x + w / 2, ry, w / 2 - PADDING, t);
                case STRING  -> renderString(ctx, tr, (Setting<String>) s, x + w / 2, ry, w / 2 - PADDING, t);
            }
            ry += ROW_H + 2;
        }
        return y + totalH;
    }

    public static int getHeight(Module mod) {
        return mod.getSettings().size() * (ROW_H + 2) + PADDING * 2;
    }

    public static boolean handleClick(Module mod, int mx, int my,
                                      int x, int y, int w, Theme t) {
        int ry = y + PADDING;
        for (Setting<?> s : mod.getSettings()) {
            if (s.getType() == Setting.Type.BOOLEAN) {
                if (mx >= x + w - 36 && mx <= x + w - 8 && my >= ry && my <= ry + ROW_H) {
                    Setting<Boolean> bs = (Setting<Boolean>) s;
                    bs.setValue(!bs.getValue());
                    return true;
                }
            } else if (s.getType() == Setting.Type.ENUM) {
                if (mx >= x + w / 2 && mx <= x + w - PADDING && my >= ry && my <= ry + ROW_H) {
                    cycleEnum(s);
                    return true;
                }
            }
            ry += ROW_H + 2;
        }
        return false;
    }

    private static void renderBool(DrawContext ctx, Setting<Boolean> s, int x, int y, Theme t) {
        boolean on = s.getValue();
        ctx.fill(x, y + 3, x + 28, y + 17, on ? t.accent() : t.toggleOff());
        int kx = on ? x + 16 : x + 2;
        ctx.fill(kx, y + 5, kx + 10, y + 15, t.textBright());
    }

    private static void renderIntSlider(DrawContext ctx, TextRenderer tr, Setting<Integer> s,
                                        int x, int y, int w, Theme t) {
        int min = s.getMin(), max = s.getMax(), val = s.getValue();
        int filled = (int)(((float)(val - min) / (max - min)) * (w - PADDING));
        ctx.fill(x, y + 7, x + w - PADDING, y + 13, t.toggleOff());
        ctx.fill(x, y + 7, x + filled, y + 13, t.accent());
        ctx.fill(x + filled - 3, y + 5, x + filled + 3, y + 15, t.textBright());
        String lbl = String.valueOf(val);
        ctx.drawText(tr, lbl, x + w - PADDING - tr.getWidth(lbl), y + 4, t.textBright(), false);
    }

    private static void renderDblSlider(DrawContext ctx, TextRenderer tr, Setting<Double> s,
                                        int x, int y, int w, Theme t) {
        double min = s.getMin(), max = s.getMax(), val = s.getValue();
        int filled = (int)(((val - min) / (max - min)) * (w - PADDING));
        ctx.fill(x, y + 7, x + w - PADDING, y + 13, t.toggleOff());
        ctx.fill(x, y + 7, x + filled, y + 13, t.accent());
        ctx.fill(x + filled - 3, y + 5, x + filled + 3, y + 15, t.textBright());
        String lbl = String.format("%.1f", val);
        ctx.drawText(tr, lbl, x + w - PADDING - tr.getWidth(lbl), y + 4, t.textBright(), false);
    }

    private static void renderEnum(DrawContext ctx, TextRenderer tr, Setting<?> s,
                                   int x, int y, int w, Theme t) {
        ctx.fill(x, y + 2, x + w - PADDING, y + ROW_H - 2, t.inputBg());
        ctx.drawText(tr, s.getValue().toString(), x + 4, y + 6, t.textBright(), false);
        ctx.drawText(tr, "\u25be", x + w - PADDING - 8, y + 6, t.textMuted(), false);
    }

    private static void renderString(DrawContext ctx, TextRenderer tr, Setting<String> s,
                                     int x, int y, int w, Theme t) {
        ctx.fill(x, y + 2, x + w - PADDING, y + ROW_H - 2, t.inputBg());
        ctx.drawText(tr, s.getValue(), x + 4, y + 6, t.textBright(), false);
    }

    @SuppressWarnings("unchecked")
    private static <T> void cycleEnum(Setting<T> s) {
        T[] vals = s.getValues();
        if (vals == null || vals.length == 0) return;
        for (int i = 0; i < vals.length; i++) {
            if (vals[i].equals(s.getValue())) {
                s.setValue(vals[(i + 1) % vals.length]);
                return;
            }
        }
    }
}
