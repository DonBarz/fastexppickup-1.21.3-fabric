package net.donbarz.fastexppickup.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.entity.ExperienceOrbEntity;

@Mixin(ExperienceOrbEntity.class)
public interface ExperienceOrbAccessor {
    @Accessor("amount")
    int getAmount();

    @Accessor("pickingCount")
    int getPickingCount();

    @Accessor("pickingCount")
    void setPickingCount(int pickingCount);
}