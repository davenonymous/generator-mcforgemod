package <%= group %>.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketDoSomething {
    int slot;
    int range;

    public PacketDoSomething(int slot, int range) {
        this.slot = slot;
        this.range = range;
    }

    public PacketDoSomething(PacketBuffer buf) {
        this.slot = buf.readInt();
        this.range = buf.readInt();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeInt(slot);
        buf.writeInt(range);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity serverPlayer = ctx.get().getSender();
        });
        ctx.get().setPacketHandled(true);
    }
}
