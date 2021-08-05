package net.blazecode.sleepy.mixins.ents;

import com.mojang.authlib.GameProfile;
import net.blazecode.vanillify.api.VanillaUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntMixin extends PlayerEntity
{
    @Shadow public abstract void setSpawnPoint(RegistryKey<World> dimension, @Nullable BlockPos pos, float angle, boolean spawnPointSet, boolean sendMessage);

    @Redirect( method = "trySleep", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;setSpawnPoint(Lnet/minecraft/util/registry/RegistryKey;Lnet/minecraft/util/math/BlockPos;FZZ)V"))
    void setSpawnPointRedirect(ServerPlayerEntity serverPlayerEntity, RegistryKey<World> dimension, BlockPos pos, float angle, boolean spawnPointSet, boolean sendMessage)
    {
        if(serverPlayerEntity.isSneaking())
        {
            this.setSpawnPoint(dimension, pos, angle, spawnPointSet, sendMessage);
            serverPlayerEntity.sendMessage(VanillaUtils.getText("You've slept and set your spawn!", Formatting.ITALIC, Formatting.GREEN), false);
        }
    }

    public ServerPlayerEntMixin(World world, BlockPos pos, float yaw, GameProfile profile)
    {
        super(world, pos, yaw, profile);
    }
}
