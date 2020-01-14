package <%= group %>;

import <%= group %>.network.Networking;
import <%= group %>.proxy.ProxyClient;
import <%= group %>.proxy.ProxyServer;
import <%= group %>.setup.Config;
import <%= group %>.setup.ForgeEventHandlers;
import com.davenonymous.libnonymous.setup.IProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(<%= baseClass %>.MODID)
public class <%= baseClass %> {
    public static final String MODID = "<%= modid %>";

    public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ProxyClient(), () -> () -> new ProxyServer());

    public <%= baseClass %>() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);

        Config.loadConfig(Config.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MODID + "-client.toml"));
        Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MODID + "-common.toml"));
    }

    private void setup(final FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());
        Networking.registerMessages();
        proxy.init();
    }
}
