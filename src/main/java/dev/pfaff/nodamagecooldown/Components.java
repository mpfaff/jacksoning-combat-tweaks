package dev.pfaff.nodamagecooldown;

import eu.pb4.polymer.core.api.other.PolymerComponent;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;

import static dev.pfaff.nodamagecooldown.NoDamageCooldown.MOD_ID;

public final class Components {
	public static final ComponentType<Unit> BYPASS_KNOCKBACK_COOLDOWN = Registry.register(
		Registries.DATA_COMPONENT_TYPE,
		Identifier.of(MOD_ID, "bypass_knockback_cooldown"),
		ComponentType.<Unit>builder().codec(Unit.CODEC).packetCodec(PacketCodec.unit(Unit.INSTANCE)).build()
	);

	static {
		PolymerComponent.registerDataComponent(BYPASS_KNOCKBACK_COOLDOWN);
	}

	public static void initialize() {
	}
}
