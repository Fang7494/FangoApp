package net.fango.module;

import net.fango.module.combat.*;
import net.fango.module.player.*;
import net.fango.module.movement.*;
import net.fango.module.render.*;
import net.fango.module.world.*;
import net.fango.module.misc.*;
import java.util.*;
import java.util.stream.Collectors;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        // Combat
        register(new KillAura());
        register(new Criticals());
        register(new AutoArmor());
        register(new AntiKnockback());
        register(new Reach());
        register(new Velocity());
        register(new TriggerBot());
        register(new AimAssist());
        // Player
        register(new NoFall());
        register(new AutoEat());
        register(new Freecam());
        register(new FastPlace());
        register(new AutoTool());
        register(new Scaffold());
        register(new Blink());
        register(new AntiAFK());
        // Movement
        register(new Speed());
        register(new Flight());
        register(new Sprint());
        register(new NoSlow());
        register(new Step());
        register(new Jesus());
        register(new Parkour());
        register(new HighJump());
        // Render
        register(new ESP());
        register(new Tracers());
        register(new FullBright());
        register(new Xray());
        register(new Chams());
        register(new HUD());
        register(new Nametags());
        register(new StorageESP());
        // World
        register(new AutoMine());
        register(new Nuker());
        register(new AutoBuild());
        register(new ChestStealer());
        register(new InfinityMiner());
        register(new Timer());
        register(new AutoFarm());
        // Misc
        register(new ChatSuffix());
        register(new AntiBot());
        register(new AutoReconnect());
        register(new DiscordRPC());
        register(new Notifications());
    }

    private void register(Module m) { modules.add(m); }
    public List<Module> getAll()    { return Collections.unmodifiableList(modules); }
    public List<Module> getByCategory(Category cat) {
        return modules.stream().filter(m -> m.getCategory() == cat).collect(Collectors.toList());
    }
    public Optional<Module> getByName(String name) {
        return modules.stream().filter(m -> m.getName().equalsIgnoreCase(name)).findFirst();
    }
}
