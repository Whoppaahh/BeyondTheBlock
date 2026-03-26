package net.ryan.beyond_the_block.config.schema;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "beyond_the_block-client")
public class ConfigClient implements ConfigData {

    public Visuals visuals = new Visuals();
    public HUD hud = new HUD();

    /* ================= VISUALS ================= */

    public static class Visuals {
        public Blood blood = new Blood();
        public Guards guards = new Guards();
        public Horses horses = new Horses();
        public Names names = new Names();
        public Title title = new Title();
        public Enchantments enchantments = new Enchantments();
    }

    public static class Enchantments {
        public boolean showHighlights = true;
    }

    public static class Blood {
        public boolean enabled = true;
        public float healthFraction = 0.25F;
    }

    public static class Title {
        public boolean customLogo = true;
    }

    public static class Guards {
        public boolean variants = false;
        public boolean berets = false;
        public boolean shoulderPads = false;
    }

    public static class Horses {
        public float minAlpha = 0.15F;
        public float fadePitch = 5.0F;
        public boolean headPitchOffset = true;
        public float headOffsetDegrees = 70.0F;
    }

    public static class Names {
        public boolean enabled = true;
        public boolean onlyWhenEmployed = true;
        public boolean colourise = true;
        public double visibilityRange = 8.0;
        public boolean alliteration = true;
        public boolean nameGolems = true;
        public boolean nameTamed = true;
        public GenderMode genderMode = GenderMode.BOTH;

        public enum GenderMode {
            MALE, FEMALE, BOTH
        }
    }

    /* ================= HUD ================= */

    public static class HUD {
        public Trajectory trajectory = new Trajectory();
        public AutoCamera camera = new AutoCamera();
        public Paths paths = new Paths();
    }

    public static class Trajectory {
        public boolean enabled = true;

        public boolean showBow = true;
        public boolean showCrossbow = true;
        public boolean showTrident = false;
        public boolean showThrowables = false;

        public boolean onlyWhileAiming = true;
        public boolean requireSneak = false;

        public int maxSteps = 64;

        public boolean gradient = true;
        public boolean thickLine = true;
        public int thicknessLines = 3;
        public float thicknessOffset = 0.03f;

        public boolean showImpactMarker = true;

        public int colorNone = 0x19FFE0;
        public int colorBlock = 0xFF5555;
        public int colorEntity = 0xFFF16A;
    }

    public static class AutoCamera {
        public boolean enabled = true;
        public Mode cameraModeOnMount = Mode.PREVIOUS;

        public enum Mode {
            PREVIOUS, ALWAYS_FIRST, ALWAYS_THIRD
        }
    }

    public static class Paths {
        public boolean previewMode = true;
        public boolean showWidthHud = true;
    }
}