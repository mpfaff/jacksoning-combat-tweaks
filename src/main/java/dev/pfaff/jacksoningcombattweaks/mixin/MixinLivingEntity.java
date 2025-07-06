package dev.pfaff.jacksoningcombattweaks.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.pfaff.jacksoningcombattweaks.Components;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
	public MixinLivingEntity(EntityType<?> type, World world) {
		super(type, world);
		throw new AssertionError();
	}

	// FIXME: this is an awful hack around mixins limitations
	@Unique
	private boolean isInCooldownTmp;

	@Inject(method = "damage", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/entity/LivingEntity;timeUntilRegen:I"), require = 1, allow = 1)
	private void damage$storeIsInCooldownTmp(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		this.isInCooldownTmp = this.timeUntilRegen > 10.0F && ((Object) this) instanceof PlayerEntity && (source.getWeaponStack() == null || !source.getWeaponStack().contains(Components.BYPASS_KNOCKBACK_COOLDOWN));
	}

	@ModifyExpressionValue(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageSource;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"), slice = @Slice(
		from = @At(value = "FIELD", opcode = Opcodes.GETSTATIC, target = "Lnet/minecraft/registry/tag/DamageTypeTags;NO_KNOCKBACK:Lnet/minecraft/registry/tag/TagKey;"),
		to = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileEntity;getKnockback(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/damage/DamageSource;)Lit/unimi/dsi/fastutil/doubles/DoubleDoubleImmutablePair;")
	), require = 1, allow = 1)
	private boolean damage$knockbackCooldown(boolean isIn) {
		return isIn || this.isInCooldownTmp;
	}

	@Inject(method = "getKnockbackAgainst", at = @At("HEAD"), cancellable = true, require = 1, allow = 1)
	private void getKnockbackAgainst$knockbackCooldown(Entity target, DamageSource damageSource, CallbackInfoReturnable<Float> cir) {
		if (target instanceof LivingEntity living) {
			if (((MixinLivingEntity) (Object) living).isInCooldownTmp) {
				cir.setReturnValue(0.0f);
			}
		}
	}
}
