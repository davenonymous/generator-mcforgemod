package <%= group %>.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import <%= group %>.<%= baseClass %>;
import <%= group %>.util.Logz;

import java.io.File;

public class ConfigurationHandler {
    public static Configuration configuration;
    public static File baseDirectory;

    public static void init(File configFile) {
        if(configuration != null) {
            return;
        }

        baseDirectory = new File(configFile.getParentFile(), "<%= modid %>");
        if(!baseDirectory.exists()) {
            baseDirectory.mkdir();
        }

        configuration = new Configuration(new File(baseDirectory, "settings.cfg"), null);
        loadConfiguration();
    }

    private static void loadConfiguration() {
        Logz.info("Loading configuration");

        // TODO: Add config getters

        if(configuration.hasChanged()) {
            configuration.save();
        }
    }

    public static void saveConfiguration() {
        Logz.info("Saving configuration");
        configuration.save();
    }

    @SubscribeEvent
    public void onConfigurationChanged(ConfigChangedEvent event) {
        if(!event.getModID().equalsIgnoreCase(<%= baseClass %>.MODID)) {
            return;
        }

        loadConfiguration();
    }

    public static class Settings {
        // TODO: Add config options fields
    }
}
