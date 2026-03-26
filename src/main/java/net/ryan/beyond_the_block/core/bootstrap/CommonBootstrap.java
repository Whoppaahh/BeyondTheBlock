package net.ryan.beyond_the_block.core.bootstrap;

public class CommonBootstrap {
    public static void init(){
        ContentRegistrar.register();
        SystemRegistrar.register();
    }
}
