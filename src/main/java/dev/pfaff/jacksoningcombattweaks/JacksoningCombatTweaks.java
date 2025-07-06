package dev.pfaff.jacksoningcombattweaks;

import net.fabricmc.api.ModInitializer;

public class JacksoningCombatTweaks implements ModInitializer {
	public static final String MOD_ID = "jacksoning_combat_tweaks";

	@Override
	public void onInitialize() {
		Components.initialize();
	}
}
