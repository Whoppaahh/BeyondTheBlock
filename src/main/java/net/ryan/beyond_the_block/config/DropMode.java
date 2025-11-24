package net.ryan.beyond_the_block.config;

public enum DropMode {
    NORMAL,  // Drop at each log
    MERGED,  // Drop all at origin
    DIRECT   // Attempt to add to player's inventory
}
