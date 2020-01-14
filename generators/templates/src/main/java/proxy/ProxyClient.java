package <%= group %>.proxy;


import com.davenonymous.libnonymous.gui.config.WidgetGuiConfig;
import com.davenonymous.libnonymous.setup.IProxy;
import <%= group %>.<%= baseClass %>;
import <%= group %>.setup.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModList;

public class ProxyClient implements IProxy {
    @Override
    public void init() {
        ModList.get().getModContainerById(<%= baseClass %>.MODID).ifPresent(c -> c.registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (mc, parent) -> {
            return new WidgetGuiConfig(parent, Config.COMMON_CONFIG, Config.CLIENT_CONFIG);
        }));
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getInstance().world;
    }

    @Override
    public PlayerEntity getClientPlayer() {
        return Minecraft.getInstance().player;
    }
}
