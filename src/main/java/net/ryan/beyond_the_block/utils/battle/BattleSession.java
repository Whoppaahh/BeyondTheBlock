package net.ryan.beyond_the_block.utils.battle;

import net.minecraft.entity.LivingEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

import java.util.UUID;

public class  BattleSession {
    private final UUID id = UUID.randomUUID();

    private final ServerWorld world;
    private final BlockPos arenaCenter;

    private final  BattleTeam teamA;
    private final  BattleTeam teamB;
    private final  BattleRules rules;

    private  BattleState state =  BattleState.WAITING;

    private int countdownTicks;
    private int activeTicks;

    public  BattleSession(
            ServerWorld world,
            BlockPos arenaCenter,
            ServerPlayerEntity ownerA,
            LivingEntity fighterA,
            ServerPlayerEntity ownerB,
            LivingEntity fighterB,
             BattleRules rules
    ) {
        this.world = world;
        this.arenaCenter = arenaCenter;
        this.teamA = new  BattleTeam(ownerA, fighterA);
        this.teamB = new  BattleTeam(ownerB, fighterB);
        this.rules = rules;
        this.countdownTicks = rules.countdownTicks;
    }

    public UUID getId() {
        return id;
    }

    public  BattleState getState() {
        return state;
    }

    public boolean containsFighter(UUID entityUuid) {
        return teamA.fighterUuid.equals(entityUuid) || teamB.fighterUuid.equals(entityUuid);
    }

    public boolean containsOwner(UUID playerUuid) {
        return teamA.ownerUuid.equals(playerUuid) || teamB.ownerUuid.equals(playerUuid);
    }

    public void start(MinecraftServer server) {
        LivingEntity fighterA = getFighterA(server);
        LivingEntity fighterB = getFighterB(server);

        ServerPlayerEntity ownerA = server.getPlayerManager().getPlayer(teamA.ownerUuid);
        ServerPlayerEntity ownerB = server.getPlayerManager().getPlayer(teamB.ownerUuid);

        if (fighterA == null || fighterB == null || ownerA == null || ownerB == null) {
            cancel(server, "Battle failed to start.");
            return;
        }

        fighterA.teleport(world, arenaCenter.getX() - 3.0D, arenaCenter.getY(), arenaCenter.getZ(), fighterA.getYaw(), fighterA.getPitch());
        fighterB.teleport(world, arenaCenter.getX() + 3.0D, arenaCenter.getY(), arenaCenter.getZ(), fighterB.getYaw(), fighterB.getPitch());

        fighterA.setInvulnerable(true);
        fighterB.setInvulnerable(true);

        ownerA.changeGameMode(GameMode.SPECTATOR);
        ownerB.changeGameMode(GameMode.SPECTATOR);

        ownerA.setCameraEntity(fighterA);
        ownerB.setCameraEntity(fighterB);

        state =  BattleState.COUNTDOWN;

        ownerA.sendMessage(Text.literal("  battle starting..."), false);
        ownerB.sendMessage(Text.literal("  battle starting..."), false);
    }

    public void tick(MinecraftServer server) {
        if (state ==  BattleState.COUNTDOWN) {
            tickCountdown(server);
        } else if (state ==  BattleState.ACTIVE) {
            tickActive(server);
        }
    }

    private void tickCountdown(MinecraftServer server) {
        countdownTicks--;

        if (countdownTicks <= 0) {
            LivingEntity fighterA = getFighterA(server);
            LivingEntity fighterB = getFighterB(server);

            if (fighterA == null || fighterB == null) {
                cancel(server, "Battle cancelled.");
                return;
            }

            fighterA.setInvulnerable(false);
            fighterB.setInvulnerable(false);

            fighterA.setTarget(fighterB);
            fighterB.setTarget(fighterA);

            state =  BattleState.ACTIVE;
        }
    }

    private void tickActive(MinecraftServer server) {
        activeTicks++;

        LivingEntity fighterA = getFighterA(server);
        LivingEntity fighterB = getFighterB(server);

        if (fighterA == null || fighterB == null) {
            cancel(server, "A fighter disappeared.");
            return;
        }

        keepInsideArena(fighterA);
        keepInsideArena(fighterB);

        fighterA.setTarget(fighterB);
        fighterB.setTarget(fighterA);

        if (fighterA.getHealth() <= 1.0F && !rules.allowDeath) {
            finish(server, teamB);
            return;
        }

        if (fighterB.getHealth() <= 1.0F && !rules.allowDeath) {
            finish(server, teamA);
            return;
        }

        if (!fighterA.isAlive()) {
            finish(server, teamB);
            return;
        }

        if (!fighterB.isAlive()) {
            finish(server, teamA);
            return;
        }

        if (activeTicks >= rules.maxBattleTicks) {
            if (fighterA.getHealth() > fighterB.getHealth()) {
                finish(server, teamA);
            } else if (fighterB.getHealth() > fighterA.getHealth()) {
                finish(server, teamB);
            } else {
                cancel(server, "Battle ended in a draw.");
            }
        }
    }

    private void keepInsideArena(LivingEntity entity) {
        double maxDistanceSq = rules.arenaRadius * rules.arenaRadius;

        if (entity.squaredDistanceTo(
                arenaCenter.getX() + 0.5D,
                arenaCenter.getY(),
                arenaCenter.getZ() + 0.5D
        ) > maxDistanceSq) {
            entity.requestTeleport(
                    arenaCenter.getX() + 0.5D,
                    arenaCenter.getY(),
                    arenaCenter.getZ() + 0.5D
            );
        }
    }

    private void finish(MinecraftServer server,  BattleTeam winningTeam) {
        state =  BattleState.FINISHED;

        ServerPlayerEntity winner = server.getPlayerManager().getPlayer(winningTeam.ownerUuid);

        cleanup(server);

        if (winner != null) {
            winner.sendMessage(Text.literal("Your   won the battle!"), false);
        }

         BattleManager.removeSession(id);
    }

    public void cancel(MinecraftServer server, String reason) {
        state =  BattleState.CANCELLED;
        cleanup(server);

        ServerPlayerEntity ownerA = server.getPlayerManager().getPlayer(teamA.ownerUuid);
        ServerPlayerEntity ownerB = server.getPlayerManager().getPlayer(teamB.ownerUuid);

        if (ownerA != null) ownerA.sendMessage(Text.literal(reason), false);
        if (ownerB != null) ownerB.sendMessage(Text.literal(reason), false);

         BattleManager.removeSession(id);
    }

    private void cleanup(MinecraftServer server) {
        LivingEntity fighterA = getFighterA(server);
        LivingEntity fighterB = getFighterB(server);

        ServerPlayerEntity ownerA = server.getPlayerManager().getPlayer(teamA.ownerUuid);
        ServerPlayerEntity ownerB = server.getPlayerManager().getPlayer(teamB.ownerUuid);

        cleanupFighter(fighterA, teamA);
        cleanupFighter(fighterB, teamB);

        cleanupOwner(ownerA, teamA);
        cleanupOwner(ownerB, teamB);
    }

    private void cleanupFighter(LivingEntity fighter,  BattleTeam team) {
        if (fighter == null) return;

        fighter.setInvulnerable(false);
        fighter.setTarget(null);

        if (rules.restoreHealthAfterBattle) {
            fighter.setHealth(Math.max(1.0F, team.originalHealth));
        }

        if (rules.restorePositionAfterBattle && team.originalFighterPos != null) {
            fighter.requestTeleport(
                    team.originalFighterPos.x,
                    team.originalFighterPos.y,
                    team.originalFighterPos.z
            );
        }
    }

    private void cleanupOwner(ServerPlayerEntity owner,  BattleTeam team) {
        if (owner == null) return;

        owner.setCameraEntity(owner);
        owner.changeGameMode(GameMode.SURVIVAL);

        if (rules.restorePositionAfterBattle && team.originalOwnerPos != null) {
            owner.requestTeleport(
                    team.originalOwnerPos.x,
                    team.originalOwnerPos.y,
                    team.originalOwnerPos.z
            );
        }
    }

    private LivingEntity getFighterA(MinecraftServer server) {
        return  BattleUtil.getLivingEntity(server, teamA.fighterUuid);
    }

    private LivingEntity getFighterB(MinecraftServer server) {
        return  BattleUtil.getLivingEntity(server, teamB.fighterUuid);
    }
}