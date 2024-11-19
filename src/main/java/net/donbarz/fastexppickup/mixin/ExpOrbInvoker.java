package net.donbarz.fastexppickup.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import net.minecraft.entity.ExperienceOrbEntity;

@Mixin(ExperienceOrbEntity.class)
public interface ExpOrbInvoker {
    @Invoker("repairPlayerGears")
    int invokeRepairPlayerGears(ServerPlayerEntity player, int amount);
}