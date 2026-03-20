package net.cozystudios.tokimisruinedmod.network;

import net.cozystudios.tokimisruinedmod.TokimisRuinedMod;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record PoopOverlayPayload() implements CustomPayload {
    public static final Id<PoopOverlayPayload> ID = new CustomPayload.Id<>(
            Identifier.of(TokimisRuinedMod.MOD_ID, "poop_overlay")
    );
    public static final PacketCodec<RegistryByteBuf, PoopOverlayPayload> CODEC =
            PacketCodec.unit(new PoopOverlayPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
