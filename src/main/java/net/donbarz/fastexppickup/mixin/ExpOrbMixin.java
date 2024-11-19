package net.donbarz.fastexppickup.mixin;

import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperienceOrbEntity.class)
public class ExpOrbMixin {

    @Inject(method = "onPlayerCollision", at = @At("HEAD"), cancellable = true)
    private void onPlayerCollision(PlayerEntity player, CallbackInfo info) {
        if (player instanceof ServerPlayerEntity serverPlayerEntity) {
            ExperienceOrbEntity orb = (ExperienceOrbEntity) (Object) this;
            ExperienceOrbAccessor accessor = (ExperienceOrbAccessor) orb;
            ExpOrbInvoker invoker = (ExpOrbInvoker) orb;

            // Ignore the pickup delay
            player.experiencePickUpDelay = 0;

            player.sendPickup(orb, 1);
            int i = invoker.invokeRepairPlayerGears(serverPlayerEntity, accessor.getAmount());
            if (i > 0) {
                player.addExperience(i);
            }

            accessor.setPickingCount(accessor.getPickingCount() - 1);
            if (accessor.getPickingCount() == 0) {
                orb.discard();
            }

            info.cancel();
        }
    }
}