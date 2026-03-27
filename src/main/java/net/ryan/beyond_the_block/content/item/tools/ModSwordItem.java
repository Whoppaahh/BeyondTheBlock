package net.ryan.beyond_the_block.content.item.tools;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.ryan.beyond_the_block.content.effect.ModEffects;
import net.ryan.beyond_the_block.content.item.ModItems;
import net.ryan.beyond_the_block.content.particle.ModParticles;

public class ModSwordItem extends SwordItem {
    public ModSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (stack.isOf(ModItems.RUBY_SWORD)) {

            // Chance for Bleed effect (uncomment if you want to make it random)
            // if (random.nextInt(10) < 3) { // 30% chance to apply the bleed effect
            // Apply Bleed status effect to the target
            target.addStatusEffect(new StatusEffectInstance(ModEffects.BLEED, 100, 0, false, false, true));

            // Optional: You can adjust the logic to handle Bleed effect duration, damage, etc.
        }
            // Handle gem-related particle spawning
            NbtCompound nbt = stack.getNbt();
            if (nbt != null && nbt.contains("GemList", NbtElement.LIST_TYPE)) {
                NbtList gemList = nbt.getList("GemList", NbtElement.STRING_TYPE);

                for (NbtElement gem : gemList) {
                    String gemName = gem.asString();
                    spawnGemParticles(gemName, target, attacker);
                }
            }

            // If you want to trigger additional behaviour, keep it here
            //

            return super.postHit(stack, target, attacker);
    }



    private void spawnGemParticles(String gemName, Entity target, Entity attacker) {
        if(!(target.getWorld() instanceof ServerWorld serverWorld)) return;
        if (!(attacker instanceof PlayerEntity player) || !(target instanceof LivingEntity livingTarget)) return;

        ParticleEffect particle = switch (gemName.toLowerCase()) {
            case "nocturnite" -> ModParticles.NOCTURNITE_PARTICLE;
            case "amberine" -> ModParticles.AMBERINE_PARTICLE;
            case "azuros" -> ModParticles.AZUROS_PARTICLE;
            case "indigra" -> ModParticles.INDIGRA_PARTICLE;
            case "miranite" -> ModParticles.MIRANITE_PARTICLE;
            case "chromite" -> ModParticles.CHROMITE_PARTICLE;
            case "rosette" -> ModParticles.ROSETTE_PARTICLE;
            default -> ParticleTypes.ENCHANT; // fallback
        };

        // Get the ItemStack used for the hit
        ItemStack itemStack = player.getMainHandStack();


        if (itemStack != null && itemStack.hasNbt()) {
            NbtCompound nbt = itemStack.getNbt();
            if (nbt != null && nbt.contains("GemList", NbtElement.LIST_TYPE)) {
                NbtList gemList = nbt.getList("GemList", NbtElement.STRING_TYPE);

                // Combo check for gem combinations
                boolean hasNocturniteAzuros = gemList.contains("Nocturnite") && gemList.contains("Azuros");
                boolean hasIndigraMiranite = gemList.contains("Indigra") && gemList.contains("Miranite");
                boolean hasChromiteRosette = gemList.contains("Chromite") && gemList.contains("Rosette");
                boolean hasAmberineMiranite = gemList.contains("Amberine") && gemList.contains("Miranite");
                boolean hasAzurosIndigra = gemList.contains("Azuros") && gemList.contains("Indigra");

                // If we detect a combo, change the particle
                if (hasNocturniteAzuros) {
                    particle = ParticleTypes.DRAGON_BREATH; // Combo effect for Nocturnite + Azuros
                    livingTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 60, 0));
                } else if (hasIndigraMiranite) {
                    particle = ParticleTypes.ENCHANTED_HIT; // Combo effect for Indigra + Miranite
                    livingTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 100, 0));
                }else if (hasChromiteRosette) {
                    particle = ParticleTypes.FLASH;
                    target.setVelocity(target.getVelocity().add(new Vec3d(0, 1, 0)));// knock up
                }else if (hasAmberineMiranite) {
                    particle = ParticleTypes.FLAME;
                    livingTarget.setOnFireFor(3);
                }else if (hasAzurosIndigra) {
                    particle = ParticleTypes.ELECTRIC_SPARK;
                    livingTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 80, 1));
                }
            }
        }

        double x = target.getX();
        double y = target.getBodyY(0.5); // a bit more centered than just target.getY()
        double z = target.getZ();

        serverWorld.spawnParticles(particle, x, y, z, 10, 0.3, 0.6, 0.3, 0.01);
    }
}
