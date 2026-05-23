package net.ryan.beyond_the_block.utils.battle;

import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import java.util.UUID;

public class BattleTeam {
    public final UUID ownerUuid;
    public final UUID fighterUuid;

    public Vec3d originalOwnerPos;
    public Vec3d originalFighterPos;

    public float originalHealth;
    public GameMode originalGameMode;

    public BattleTeam(ServerPlayerEntity owner, LivingEntity fighter) {
        this.ownerUuid = owner.getUuid();
        this.fighterUuid = fighter.getUuid();
        this.originalOwnerPos = owner.getPos();
        this.originalFighterPos = fighter.getPos();
        this.originalHealth = fighter.getHealth();
        this.originalGameMode = owner.interactionManager.getGameMode();
    }
}
