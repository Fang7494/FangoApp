package net.fango;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import net.fango.module.ModuleManager;
import net.fango.gui.ClickGUI;
import net.fango.gui.theme.ThemeManager;

public class FangoClient implements ClientModInitializer {

    public static final String NAME    = "Fango A++";
    public static final String VERSION = "1.0.0";

    public static ModuleManager modules;
    public static ThemeManager   themes;
    public static ClickGUI       gui;

    private static KeyBinding guiKey;

    @Override
    public void onInitializeClient() {
        modules = new ModuleManager();
        themes  = new ThemeManager();
        gui     = new ClickGUI();

        guiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.fango.gui",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "category.fango"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (guiKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(gui);
                }
            }
            // Tick all enabled modules
            if (client.player != null) {
                modules.getAll().stream()
                    .filter(m -> m.isEnabled())
                    .forEach(m -> m.onTick());
            }
        });
    }
}
