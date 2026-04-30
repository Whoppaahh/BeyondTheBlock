package net.ryan.beyond_the_block.utils.battle;

import net.minecraft.server.MinecraftServer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class  BattleManager {
    private static final Map<UUID,  BattleSession> SESSIONS = new HashMap<>();

    public static void addSession( BattleSession session) {
        SESSIONS.put(session.getId(), session);
    }

    public static void removeSession(UUID id) {
        SESSIONS.remove(id);
    }

    public static Collection< BattleSession> getSessions() {
        return SESSIONS.values();
    }

    public static void tick(MinecraftServer server) {
        for ( BattleSession session : SESSIONS.values().toArray(new  BattleSession[0])) {
            session.tick(server);
        }
    }

    public static  BattleSession getSessionByFighter(UUID fighterUuid) {
        for ( BattleSession session : SESSIONS.values()) {
            if (session.containsFighter(fighterUuid)) {
                return session;
            }
        }

        return null;
    }

    public static  BattleSession getSessionByOwner(UUID ownerUuid) {
        for ( BattleSession session : SESSIONS.values()) {
            if (session.containsOwner(ownerUuid)) {
                return session;
            }
        }

        return null;
    }
}