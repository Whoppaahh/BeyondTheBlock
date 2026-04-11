package net.ryan.beyond_the_block.content.registry.family;

import net.minecraft.util.SignType;
import net.ryan.beyond_the_block.mixin.accessors.SignTypeAccessor;

public final class ModSignTypes {
    public static final SignType CHERRY = SignTypeAccessor.btb$register(SignTypeAccessor.btb$create("cherry"));
    public static final SignType PALE_OAK = SignTypeAccessor.btb$register(SignTypeAccessor.btb$create("pale_oak"));
    public static final SignType BAMBOO = SignTypeAccessor.btb$register(SignTypeAccessor.btb$create("bamboo"));

    public static void init() {
    }
}
