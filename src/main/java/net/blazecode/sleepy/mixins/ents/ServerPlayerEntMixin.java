package net.blazecode.sleepy.mixins.ents;

import com.mojang.authlib.GameProfile;
import net.blazecode.sleepy.SleepyMod;
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
    public ServerPlayerEntMixin( World world, BlockPos pos, float yaw, GameProfile profile )
    {
        super( world, pos, yaw, profile );
    }
    
    @Shadow public abstract void setSpawnPoint( RegistryKey<World> dimension, @Nullable BlockPos pos, float angle, boolean spawnPointSet, boolean sendMessage);

    @Redirect( method = "trySleep", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;setSpawnPoint(Lnet/minecraft/util/registry/RegistryKey;Lnet/minecraft/util/math/BlockPos;FZZ)V"))
    void setSpawnPointRedirect(ServerPlayerEntity serverPlayerEntity, RegistryKey<World> dimension, BlockPos pos, float angle, boolean spawnPointSet, boolean sendMessage)
    {
        boolean triggered = (serverPlayerEntity.isSneaking() && SleepyMod.getConfig().getSpawnpointSetStance() == 0);
        if(triggered || !SleepyMod.getConfig().getEnabled())
        {
            this.setSpawnPoint(dimension, pos, angle, spawnPointSet, false);
            serverPlayerEntity.sendMessage(VanillaUtils.getText("Spawn point has been set!", Formatting.ITALIC, Formatting.GREEN), false);
        }
        else
        {
            serverPlayerEntity.sendMessage(VanillaUtils.getText("Spawn point was skipped!", Formatting.ITALIC, Formatting.YELLOW), false);
        }
    }
}
