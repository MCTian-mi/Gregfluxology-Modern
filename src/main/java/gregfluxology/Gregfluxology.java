package gregfluxology;

import com.gregtechceu.gtceu.api.capability.GTCapability;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.mojang.logging.LogUtils;
import gregfluxology.cap.FEEnergyWrapper;
import gregfluxology.util.GFyUtility;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.slf4j.Logger;

@Mod(Gregfluxology.MODID)
public class Gregfluxology {

    public static final String MODID = "gregfluxology";

    private static final Logger LOGGER = LogUtils.getLogger();

    public Gregfluxology(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.register(this);
        modContainer.registerConfig(ModConfig.Type.COMMON, GFyConfig.SPEC);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (Block block : BuiltInRegistries.BLOCK) {
            if (GFyUtility.isGTMachine(block)) {
                event.registerBlock(Capabilities.EnergyStorage.BLOCK,
                        (level, pos, state, blockEntity, side) -> {
                            IEnergyContainer energyContainer = level.getCapability(GTCapability.CAPABILITY_ENERGY_CONTAINER, pos,
                                    state, blockEntity, side);
                            if (energyContainer != null) {
                                return new FEEnergyWrapper(energyContainer, side);
                            }
                            return null;
                        }, block);
            }
        }
    }
}
