package <%= group %>;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import <%= group %>.config.ConfigurationHandler;
import <%= group %>.proxy.CommonProxy;
import <%= group %>.util.Logz;

@Mod(modid = <%= baseClass %>.MODID, version = <%= baseClass %>.VERSION, name = "<%= modname %>", acceptedMinecraftVersions = "[1.12,1.13)")
public class <%= baseClass %> {
    public static final String MODID = "<%= modid %>";
    public static final String VERSION = "1.0.0";

    @Mod.Instance(<%= baseClass %>.MODID)
    public static <%= baseClass %> instance;

    @SidedProxy(clientSide = "<%= group %>.proxy.ClientProxy", serverSide = "<%= group %>.proxy.ServerProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Logz.logger = event.getModLog();

        ConfigurationHandler.init(event.getSuggestedConfigurationFile());
        MinecraftForge.EVENT_BUS.register(ConfigurationHandler.class);

        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
