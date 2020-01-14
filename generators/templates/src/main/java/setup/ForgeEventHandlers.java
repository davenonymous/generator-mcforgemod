package <%= group %>.setup;

import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import java.util.stream.Collectors;

public class ForgeEventHandlers {
    @SubscribeEvent
    public void serverLoad(FMLServerStartingEvent event) {
        // ModCommands.register(event.getCommandDispatcher());
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void startServer(FMLServerAboutToStartEvent event) {
        IReloadableResourceManager manager = event.getServer().getResourceManager();
        manager.addReloadListener((IResourceManagerReloadListener) resourceManager -> {
            // Recipes updated on the server
        });
    }

    @SubscribeEvent
    public void recipesUpdated(RecipesUpdatedEvent event) {
        // Recipes updated on the client
        if(ModList.get().isLoaded("jei")) {

        }
    }
}
