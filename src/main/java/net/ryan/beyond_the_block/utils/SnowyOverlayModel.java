package net.ryan.beyond_the_block.utils;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class SnowyOverlayModel implements FabricBakedModel, BakedModel {
    private final BakedModel baseModel;
    private final Sprite snowSprite;

    public SnowyOverlayModel(BakedModel baseModel, boolean isCrossOrLeaf) {
        this.baseModel = baseModel;
        this.snowSprite = MinecraftClient.getInstance()
                .getSpriteAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)
                .apply(new Identifier("minecraft", "block/snow"));
    }

    // FabricBakedModel entry point
    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(@Nullable BlockRenderView view, BlockState state, @Nullable BlockPos pos,
                               Supplier<Random> randomSupplier, RenderContext context) {

        // Render base model through fallback consumer
        context.fallbackConsumer().accept(baseModel);

        if (view == null || pos == null) return;

        float fade = getSnowFade(view, pos);
        if (fade <= 0.01f) return;

        // Build overlay mesh
        MeshBuilder builder = net.fabricmc.fabric.api.renderer.v1.RendererAccess.INSTANCE.getRenderer().meshBuilder();
        QuadEmitter emitter = builder.getEmitter();

        // Copy quads from base model to overlay
        for (Direction dir : Direction.values()) {
            List<BakedQuad> quads = baseModel.getQuads(state, dir, randomSupplier.get());
            for (BakedQuad quad : quads) {
                addOverlayQuad(emitter, quad, view, pos, fade);
            }
        }
        for (BakedQuad quad : baseModel.getQuads(state, null, randomSupplier.get())) {
            addOverlayQuad(emitter, quad, view, pos, fade);
        }

        Mesh mesh = builder.build();
        context.meshConsumer().accept(mesh);
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        // Just render the vanilla item model
        context.fallbackConsumer().accept(baseModel);
    }


    // ---------------- internal helpers ----------------

    private float getSnowFade(BlockRenderView view, BlockPos pos) {
        BlockState above = view.getBlockState(pos.up());
        if (above.isOf(Blocks.SNOW) || above.isOf(Blocks.SNOW_BLOCK)) return 1.0f;

        ClientWorld world = MinecraftClient.getInstance().world;
        if (world == null) return 0f;

        float temp = world.getBiome(pos).value().getTemperature();
        float fade = 1.0f - temp;
        return Math.max(0f, Math.min(fade, 1.0f));
    }

    private void addOverlayQuad(QuadEmitter emitter, BakedQuad original, BlockRenderView view, BlockPos pos, float fade) {
        int biomeColor = MinecraftClient.getInstance().world.getColor(pos, BiomeColors.GRASS_COLOR);
        int biomeR = (biomeColor >> 16) & 0xFF;
        int biomeG = (biomeColor >> 8) & 0xFF;
        int biomeB = biomeColor & 0xFF;

        int whiteR = 255, whiteG = 255, whiteB = 255;
        float tintStrength = 0.4f;
        float blend = (1.0f - fade) * tintStrength;

        int r = (int) (whiteR * (1 - blend) + biomeR * blend);
        int g = (int) (whiteG * (1 - blend) + biomeG * blend);
        int b = (int) (whiteB * (1 - blend) + biomeB * blend);
        int a = (int) (255 * fade);

        emitter.fromVanilla(original.getVertexData(), 0, false);
        emitter.spriteBake(0,this.snowSprite, 0);
        emitter.spriteColor(0, ((a & 0xFF) << 24) | (r << 16) | (g << 8) | b);
        emitter.emit();
    }

    // --------------- Standard BakedModel passthroughs ---------------
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
        return baseModel.getQuads(state, face, random);
    }

    @Override public boolean useAmbientOcclusion() { return baseModel.useAmbientOcclusion(); }
    @Override public boolean hasDepth() { return baseModel.hasDepth(); }
    @Override public boolean isSideLit() { return baseModel.isSideLit(); }
    @Override public boolean isBuiltin() { return baseModel.isBuiltin(); }
    @Override public Sprite getParticleSprite() { return baseModel.getParticleSprite(); }
    @Override public ModelTransformation getTransformation() { return baseModel.getTransformation(); }
    @Override public ModelOverrideList getOverrides() { return baseModel.getOverrides(); }
}

