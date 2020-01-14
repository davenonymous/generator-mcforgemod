package <%= group %>.setup;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.nio.file.Path;

@Mod.EventBusSubscriber
public class Config {
    public static final String CATEGORY_GENERAL = "general";

    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    static {
        COMMON_BUILDER.comment("General Settings").push(CATEGORY_GENERAL);
        setupGeneralConfig(COMMON_BUILDER);
        COMMON_BUILDER.pop();
        COMMON_CONFIG = COMMON_BUILDER.build();

        CLIENT_BUILDER.comment("General Settings").push(CATEGORY_GENERAL);
        setupClientConfig(CLIENT_BUILDER);
        CLIENT_BUILDER.pop();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    private static void setupGeneralConfig(ForgeConfigSpec.Builder b) {
    }

    private static void setupClientConfig(ForgeConfigSpec.Builder b) {
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        configData.load();
        spec.setConfig(configData);
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {
    }

    @SubscribeEvent
    public static void onReload(final ModConfig.ConfigReloading configEvent) {
    }
}
