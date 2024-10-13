package gregfluxology.cap;

import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.capability.compat.FeCompat;
import com.gregtechceu.gtceu.common.pipelike.cable.EnergyNetHandler;
import gregfluxology.GFyConfig;
import gregfluxology.util.GFyUtility;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class FEEnergyWrapper implements IEnergyStorage {

    private final IEnergyContainer energyContainer;
    private final Direction facing;

    public FEEnergyWrapper(IEnergyContainer energyContainer, Direction facing) {
        this.energyContainer = energyContainer;
        this.facing = facing;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) { // TODO: add a config here to remove voltage restrictions.

        if (!canReceive()) return 0;

        if (maxReceive == 1 && simulate) {
            // Damn you mekanism
            return energyContainer.getEnergyCanBeInserted() > 0L ? 1 : 0;
        }

        long maxIn = maxReceive / FeCompat.ratio(true);
        long missing = energyContainer.getEnergyCanBeInserted();
        long voltage = energyContainer.getInputVoltage();
        maxIn = Math.min(missing, maxIn);
        long maxAmp = Math.min(energyContainer.getInputAmperage(), maxIn / voltage);

        if (GFyConfig.ignoreCableCapacity && energyContainer instanceof EnergyNetHandler) { // TODO: add a config here to remove this check, thus protecting the cables from burning.
            maxIn = maxReceive / FeCompat.ratio(true);
            maxAmp = maxIn / voltage;
        }

        if (maxAmp < 1L) return 0;

        if (!simulate) {
            maxAmp = energyContainer.acceptEnergyFromNetwork(facing, voltage, maxAmp);
        }

        return GFyUtility.safeConvertEUToFE(maxAmp * voltage);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0; // this is done in base CEu
    }

    @Override
    public int getEnergyStored() {
        return GFyUtility.safeConvertEUToFE(energyContainer.getEnergyStored());
    }

    @Override
    public int getMaxEnergyStored() {
        return GFyUtility.safeConvertEUToFE(energyContainer.getEnergyCapacity());
    }

    @Override
    public boolean canExtract() {
        return false; // this is done in base CEu
    }

    @Override
    public boolean canReceive() {
        return energyContainer.inputsEnergy(this.facing);
    }
}
