package net.donbarz.fastexppickup.mixin;

import net.donbarz.fastexppickup.config.ModConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ExperienceOrbEntity.class)
public class ExpOrbMixin {

    @Inject(method = "onPlayerCollision", at = @At("HEAD"), cancellable = true)
    private void onPlayerCollision(PlayerEntity player, CallbackInfo info) {
        if (player instanceof ServerPlayerEntity serverPlayerEntity) {
            int maxOrbsPerTick = ModConfig.INSTANCE.orbsPerPickupTick;
            int orbsProcessed = 0;

            // Get all nearby experience orbs within the player's expanded bounding box
            var nearbyOrbs = player.getWorld().getEntitiesByClass(
                    ExperienceOrbEntity.class,
                    player.getBoundingBox().expand(1.0), // Adjust the range as needed
                    Entity::isAlive// Only pick up valid orbs
            );

            if(player.experiencePickUpDelay == 0){
                // Iterate through each orb and process them
                player.experiencePickUpDelay = ModConfig.INSTANCE.pickUpDelay;
                for (ExperienceOrbEntity orb : nearbyOrbs) {
                    if (orbsProcessed >= maxOrbsPerTick) {
                        break; // Stop if the max orbs per tick limit is reached
                    }

                    ExperienceOrbAccessor accessor = (ExperienceOrbAccessor) orb;
                    ExpOrbInvoker invoker = (ExpOrbInvoker) orb;

                    player.sendPickup(orb, 1);

                    // Add experience or repair player gear
                    int experienceGained = invoker.invokeRepairPlayerGears(serverPlayerEntity, accessor.getAmount());
                    if (experienceGained > 0) {
                        player.addExperience(experienceGained);
                    }

                    // Mark the orb for removal
                    accessor.setPickingCount(accessor.getPickingCount() - 1);
                    if (accessor.getPickingCount() <= 0) {
                        orb.discard();
                    }

                    orbsProcessed++;
                }
            }

            // Cancel the default behavior to prevent duplicate processing
            info.cancel();
        }
    }
}