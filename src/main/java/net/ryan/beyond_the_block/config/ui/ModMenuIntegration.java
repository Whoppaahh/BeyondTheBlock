package net.ryan.beyond_the_block.config.ui;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.ryan.beyond_the_block.config.schema.ConfigClient;
import net.ryan.beyond_the_block.config.schema.ConfigServer;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {

            ConfigClient config = AutoConfig.getConfigHolder(ConfigClient.class).getConfig();
            ConfigServer serverConfig = AutoConfig.getConfigHolder(ConfigServer.class).getConfig();


            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.literal("Beyond The Block Config"));

            ConfigEntryBuilder entry = builder.entryBuilder();

            ConfigCategory gameplay = builder.getOrCreateCategory(Text.literal("Gameplay (Server)"));

            if (MinecraftClient.getInstance().isIntegratedServerRunning()) {

                SubCategoryBuilder guards = entry.startSubCategory(Text.literal("Guards"))
                        .setExpanded(false);

                guards.add(entry.startBooleanToggle(
                                Text.literal("Enable Guards"),
                                serverConfig.features.guards.enabled)
                        .setSaveConsumer(v -> serverConfig.features.guards.enabled = v)
                        .build());

                gameplay.addEntry(guards.build());
            }

            /* ========================================================= */
            /* ======================= VISUALS ========================== */
            /* ========================================================= */

            ConfigCategory visuals = builder.getOrCreateCategory(Text.literal("Visuals"));

            /* ---------- Blood ---------- */

            SubCategoryBuilder blood = entry.startSubCategory(Text.literal("Blood Effects"))
                    .setExpanded(false);

            blood.add(entry.startBooleanToggle(Text.literal("Enable Blood"),
                            config.visuals.blood.enabled)
                    .setTooltip(Text.literal("Enable blood particle effects when entities are damaged"))
                    .setDefaultValue(true)
                    .setSaveConsumer(v -> config.visuals.blood.enabled = v)
                    .build());

            if (config.visuals.blood.enabled) {
                blood.add(entry.startFloatField(Text.literal("Health Threshold"),
                                config.visuals.blood.healthFraction)
                        .setTooltip(Text.literal("Only show blood when entity health is below this fraction"))
                        .setDefaultValue(0.25F)
                        .setSaveConsumer(v -> config.visuals.blood.healthFraction = v)
                        .build());
            }

            visuals.addEntry(blood.build());

            /* ---------- Guard Visuals ---------- */

            SubCategoryBuilder guardVisuals = entry.startSubCategory(Text.literal("Guard Visuals"))
                    .setExpanded(false);

            guardVisuals.add(entry.startBooleanToggle(Text.literal("Enable Variants"),
                            config.visuals.guards.variants)
                    .setTooltip(Text.literal("Enable different guard appearances"))
                    .setDefaultValue(false)
                    .setSaveConsumer(v -> config.visuals.guards.variants = v)
                    .build());

            guardVisuals.add(entry.startBooleanToggle(Text.literal("Show Berets"),
                            config.visuals.guards.berets)
                    .setTooltip(Text.literal("Allow guards to wear berets"))
                    .setDefaultValue(false)
                    .setSaveConsumer(v -> config.visuals.guards.berets = v)
                    .build());

            guardVisuals.add(entry.startBooleanToggle(Text.literal("Show Shoulder Pads"),
                            config.visuals.guards.shoulderPads)
                    .setTooltip(Text.literal("Render shoulder pads on guards"))
                    .setDefaultValue(false)
                    .setSaveConsumer(v -> config.visuals.guards.shoulderPads = v)
                    .build());

            visuals.addEntry(guardVisuals.build());

            /* ---------- Horse Camera ---------- */

            SubCategoryBuilder horseCam = entry.startSubCategory(Text.literal("Horse Camera"))
                    .setExpanded(false);

            horseCam.add(entry.startFloatField(Text.literal("Minimum Alpha"),
                            config.visuals.horses.minAlpha)
                    .setTooltip(Text.literal("Minimum transparency when camera clips into horse"))
                    .setDefaultValue(0.25F)
                    .setSaveConsumer(v -> config.visuals.horses.minAlpha = v)
                    .build());

            horseCam.add(entry.startFloatField(Text.literal("Fade Pitch"),
                            config.visuals.horses.fadePitch)
                    .setTooltip(Text.literal("Pitch angle at which fading begins"))
                    .setDefaultValue(30F)
                    .setSaveConsumer(v -> config.visuals.horses.fadePitch = v)
                    .build());

            horseCam.add(entry.startBooleanToggle(Text.literal("Enable Head Offset"),
                            config.visuals.horses.headPitchOffset)
                    .setTooltip(Text.literal("Adjust horse head rotation based on camera pitch"))
                    .setDefaultValue(true)
                    .setSaveConsumer(v -> config.visuals.horses.headPitchOffset = v)
                    .build());

            if (config.visuals.horses.headPitchOffset) {
                horseCam.add(entry.startFloatField(Text.literal("Head Offset Degrees"),
                                config.visuals.horses.headOffsetDegrees)
                        .setTooltip(Text.literal("Maximum head rotation offset"))
                        .setDefaultValue(30F)
                        .setSaveConsumer(v -> config.visuals.horses.headOffsetDegrees = v)
                        .build());
            }

            visuals.addEntry(horseCam.build());

            /* ---------- Names ---------- */

            SubCategoryBuilder names = entry.startSubCategory(Text.literal("Entity Names"))
                    .setExpanded(false);

            names.add(entry.startBooleanToggle(Text.literal("Enable Names"),
                            config.visuals.names.enabled)
                    .setTooltip(Text.literal("Enable custom generated names"))
                    .setDefaultValue(true)
                    .setSaveConsumer(v -> config.visuals.names.enabled = v)
                    .build());

            if (config.visuals.names.enabled) {

                names.add(entry.startBooleanToggle(Text.literal("Only When Employed"),
                                config.visuals.names.onlyWhenEmployed)
                        .setTooltip(Text.literal("Only name villagers with professions"))
                        .setDefaultValue(true)
                        .setSaveConsumer(v -> config.visuals.names.onlyWhenEmployed = v)
                        .build());

                names.add(entry.startBooleanToggle(Text.literal("Use Colours"),
                                config.visuals.names.colourise)
                        .setTooltip(Text.literal("Colour names based on profession"))
                        .setDefaultValue(true)
                        .setSaveConsumer(v -> config.visuals.names.colourise = v)
                        .build());

                names.add(entry.startDoubleField(Text.literal("Visibility Range"),
                                config.visuals.names.visibilityRange)
                        .setTooltip(Text.literal("Maximum distance names are visible"))
                        .setDefaultValue(8.0)
                        .setSaveConsumer(v -> config.visuals.names.visibilityRange = v)
                        .build());

                names.add(entry.startBooleanToggle(Text.literal("Alliteration"),
                                config.visuals.names.alliteration)
                        .setTooltip(Text.literal("Generate alliterative names"))
                        .setDefaultValue(true)
                        .setSaveConsumer(v -> config.visuals.names.alliteration = v)
                        .build());
            }

            visuals.addEntry(names.build());

            /* ---------- Title ---------- */

            visuals.addEntry(entry.startBooleanToggle(Text.literal("Custom Title Logo"),
                            config.visuals.title.customLogo)
                    .setTooltip(Text.literal("Replace the default Minecraft logo"))
                    .setDefaultValue(true)
                    .setSaveConsumer(v -> config.visuals.title.customLogo = v)
                    .build());

            /* ---------- Enchantments ---------- */

            visuals.addEntry(entry.startBooleanToggle(Text.literal("Show Enchantment Highlights"),
                            config.visuals.enchantments.showHighlights)
                    .setTooltip(Text.literal("Highlight affected blocks when using enchantments"))
                    .setDefaultValue(true)
                    .setSaveConsumer(v -> config.visuals.enchantments.showHighlights = v)
                    .build());

            /* ========================================================= */
            /* ========================= HUD ============================ */
            /* ========================================================= */

            ConfigCategory hud = builder.getOrCreateCategory(Text.literal("HUD"));

            /* ---------- Trajectory ---------- */

            SubCategoryBuilder trajectory = entry.startSubCategory(Text.literal("Trajectory"))
                    .setExpanded(false);

            trajectory.add(entry.startBooleanToggle(Text.literal("Enable Trajectory Preview"),
                            config.hud.trajectory.enabled)
                    .setTooltip(Text.literal("Show a projectile path preview"))
                    .setDefaultValue(true)
                    .setSaveConsumer(v -> config.hud.trajectory.enabled = v)
                    .build());

            if (config.hud.trajectory.enabled) {

                trajectory.add(entry.startBooleanToggle(Text.literal("Only While Aiming"),
                                config.hud.trajectory.onlyWhileAiming)
                        .setTooltip(Text.literal("Only show trajectory when using items"))
                        .setDefaultValue(true)
                        .setSaveConsumer(v -> config.hud.trajectory.onlyWhileAiming = v)
                        .build());

                trajectory.add(entry.startBooleanToggle(Text.literal("Require Sneak"),
                                config.hud.trajectory.requireSneak)
                        .setTooltip(Text.literal("Only show trajectory while sneaking"))
                        .setDefaultValue(false)
                        .setSaveConsumer(v -> config.hud.trajectory.requireSneak = v)
                        .build());

                trajectory.add(entry.startIntField(Text.literal("Max Steps"),
                                config.hud.trajectory.maxSteps)
                        .setTooltip(Text.literal("Maximum simulation steps"))
                        .setDefaultValue(64)
                        .setSaveConsumer(v -> config.hud.trajectory.maxSteps = v)
                        .build());
            }

            hud.addEntry(trajectory.build());

            /* ---------- Camera ---------- */

            SubCategoryBuilder camera = entry.startSubCategory(Text.literal("Auto Camera"))
                    .setExpanded(false);

            camera.add(entry.startBooleanToggle(Text.literal("Enable Auto Camera"),
                            config.hud.camera.enabled)
                    .setTooltip(Text.literal("Automatically adjust camera when mounting"))
                    .setDefaultValue(true)
                    .setSaveConsumer(v -> config.hud.camera.enabled = v)
                    .build());

            hud.addEntry(camera.build());

            /* ---------- Paths ---------- */

            SubCategoryBuilder paths = entry.startSubCategory(Text.literal("Path HUD"))
                    .setExpanded(false);

            paths.add(entry.startBooleanToggle(Text.literal("Preview Mode"),
                            config.hud.paths.previewMode)
                    .setTooltip(Text.literal("Preview paths before placing"))
                    .setDefaultValue(true)
                    .setSaveConsumer(v -> config.hud.paths.previewMode = v)
                    .build());

            paths.add(entry.startBooleanToggle(Text.literal("Show Width HUD"),
                            config.hud.paths.showWidthHud)
                    .setTooltip(Text.literal("Display current path width"))
                    .setDefaultValue(true)
                    .setSaveConsumer(v -> config.hud.paths.showWidthHud = v)
                    .build());

            hud.addEntry(paths.build());

            /* ========================================================= */

            builder.setSavingRunnable(() -> {
                AutoConfig.getConfigHolder(ConfigClient.class).save();
                if (MinecraftClient.getInstance().isIntegratedServerRunning()) {
                    AutoConfig.getConfigHolder(ConfigServer.class).save();
                }
            });

            return builder.build();
        };
    }
}