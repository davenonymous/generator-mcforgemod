package <%= group %>.network;

import <%= group %>.<%= baseClass %>;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Networking {
    public static SimpleChannel INSTANCE;
    private static final String CHANNEL_NAME = "channel";
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerMessages() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(<%= baseClass %>.MODID, CHANNEL_NAME), () -> "1.0", s -> true, s -> true);

        INSTANCE.registerMessage(nextID(), PacketDoSomething.class, PacketDoSomething::toBytes, PacketDoSomething::new, PacketDoSomething::handle);
    }

    public static void sendDoSomethingMessageToServer(int slot, int range) {
        INSTANCE.sendToServer(new PacketDoSomething(slot, range));
    }

    public static void sendDoSomethingMessageToClient(int slot, int range) {
        INSTANCE.sendToServer(new PacketDoSomething(slot, range));
    }
}
