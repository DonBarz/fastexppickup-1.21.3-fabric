package net.donbarz.fastexppickup;

import net.donbarz.fastexppickup.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class FastExpPickup implements ModInitializer {
    @Override
    public void onInitialize() {
        ConfigManager.loadConfig();
        ConfigManager.saveConfig();
    }
}
