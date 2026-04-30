package net.ryan.beyond_the_block.utils.battle;

public class BattleRules {
    public boolean allowOwnerInterference = false;
    public boolean allowOutsideDamage = false;
    public boolean allowDeath = false;
    public boolean restoreHealthAfterBattle = true;
    public boolean restorePositionAfterBattle = true;

    public int countdownTicks = 100; // 5 seconds
    public int maxBattleTicks = 20 * 60 * 3; // 3 minutes
    public double arenaRadius = 12.0D;
}
