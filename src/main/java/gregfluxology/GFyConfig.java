package gregfluxology;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = Gregfluxology.MODID, bus = EventBusSubscriber.Bus.MOD)
public class GFyConfig {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    static final ModConfigSpec SPEC = BUILDER.build();
    private static final ModConfigSpec.BooleanValue IGNORE_CABLE_CAPACITY = BUILDER
            .comment("Ignoring the max input amperage of a CEu cable/wire, burning them during energy overflow. (like in base CEu)")
            .define("ignoreCableCapacity", false);
    public static boolean ignoreCableCapacity;

    @SubscribeEvent
    static void onReload(final ModConfigEvent.Reloading event) {
        ignoreCableCapacity = IGNORE_CABLE_CAPACITY.get();
    }
}
