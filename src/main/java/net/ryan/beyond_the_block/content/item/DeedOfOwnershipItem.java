package net.ryan.beyond_the_block.content.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.ryan.beyond_the_block.feature.pets.PetCommandAccessor;
import net.ryan.beyond_the_block.feature.pets.PetCommandState;
import net.ryan.beyond_the_block.feature.pets.PetHomeAccessor;

import java.util.List;
import java.util.UUID;

public class DeedOfOwnershipItem extends Item {

    private static final String PET_UUID_KEY = "PetUuid";
    private static final String OLD_OWNER_KEY = "OldOwnerUuid";
    private static final String PET_NAME_KEY = "PetName";

    public DeedOfOwnershipItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        return nbt != null && nbt.contains(PET_UUID_KEY);
    }

    @Override
    public void appendTooltip(
            ItemStack stack,
            World world,
            List<Text> tooltip,
            TooltipContext context
    ) {
        NbtCompound nbt = stack.getNbt();

        if (nbt == null || !nbt.contains(PET_UUID_KEY)) {
            tooltip.add(Text.literal("Unsigned").formatted(Formatting.GRAY));
            tooltip.add(Text.literal("Right-click your pet to sign.").formatted(Formatting.DARK_GRAY));
            return;
        }

        String petName = nbt.getString(PET_NAME_KEY);

        tooltip.add(Text.literal("Pet: " + petName).formatted(Formatting.GOLD));

        tooltip.add(Text.literal("Bound Deed").formatted(Formatting.AQUA));

        tooltip.add(Text.literal("Use on the assigned pet to transfer ownership.")
                .formatted(Formatting.DARK_GRAY));
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand) {
        if (!(entity instanceof TameableEntity pet)) {
            return ActionResult.PASS;
        }

        if (!pet.isTamed()) {
            return ActionResult.FAIL;
        }

        if (player.getWorld().isClient) {
            return ActionResult.SUCCESS;
        }

        NbtCompound nbt = stack.getOrCreateNbt();

        if (!nbt.contains(PET_UUID_KEY)) {
            return bindDeed(stack, player, pet, nbt);
        }

        return transferOwnership(stack, player, pet, nbt);
    }

    private ActionResult bindDeed(ItemStack stack, PlayerEntity player, TameableEntity pet, NbtCompound nbt) {
        if (!pet.isOwner(player)) {
            player.sendMessage(Text.literal("Only the current owner can sign this deed."), true);
            return ActionResult.CONSUME;
        }

        UUID petUuid = pet.getUuid();
        UUID ownerUuid = player.getUuid();

        nbt.putUuid(PET_UUID_KEY, petUuid);
        nbt.putUuid(OLD_OWNER_KEY, ownerUuid);
        nbt.putString(PET_NAME_KEY, pet.getName().getString());

        stack.setCustomName(Text.literal("Deed: " + pet.getName().getString()));

        player.sendMessage(Text.literal("Deed signed for " + pet.getName().getString() + "."), true);
        return ActionResult.CONSUME;
    }

    private ActionResult transferOwnership(ItemStack stack, PlayerEntity player, TameableEntity pet, NbtCompound nbt) {
        UUID deedPetUuid = nbt.getUuid(PET_UUID_KEY);

        if (!pet.getUuid().equals(deedPetUuid)) {
            player.sendMessage(Text.literal("This deed belongs to another pet.").formatted(Formatting.RED), true);
            return ActionResult.CONSUME;
        }

        UUID oldOwnerUuid = nbt.getUuid(OLD_OWNER_KEY);

        if (pet.getOwnerUuid() == null || !pet.getOwnerUuid().equals(oldOwnerUuid)) {
            player.sendMessage(Text.literal("This deed is no longer valid."), true);
            return ActionResult.CONSUME;
        }

        pet.setOwnerUuid(player.getUuid());
        pet.setSitting(false);

        if (pet instanceof PetCommandAccessor commandAccessor) {
            commandAccessor.btb$setPetCommandState(PetCommandState.FOLLOW);
        }

        if (pet instanceof PetHomeAccessor homeAccessor) {
            homeAccessor.btb$clearPetHome();
        }

        if (!pet.getWorld().isClient) {
            player.sendMessage(Text.literal("You are now the owner of " + pet.getName().getString() + "."), false);
        }

        pet.getWorld().playSound(
                null,
                pet.getBlockPos(),
                SoundEvents.ENTITY_PLAYER_LEVELUP,
                SoundCategory.NEUTRAL,
                0.75F,
                1.4F
        );

        if (pet.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(
                    ParticleTypes.HEART,
                    pet.getX(),
                    pet.getY() + pet.getHeight() * 0.75D,
                    pet.getZ(),
                    8,
                    0.35D,
                    0.35D,
                    0.35D,
                    0.02D
            );
        }
        stack.decrement(1);
        return ActionResult.CONSUME;
    }
    private boolean isValidForPet(ItemStack stack, TameableEntity pet) {
        NbtCompound nbt = stack.getNbt();
        if (nbt == null || !nbt.contains(PET_UUID_KEY)) return false;

        return pet.getUuid().equals(nbt.getUuid(PET_UUID_KEY));
    }
}