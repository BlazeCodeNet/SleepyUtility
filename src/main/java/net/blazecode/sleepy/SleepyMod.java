package net.blazecode.sleepy;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;

@Environment( EnvType.SERVER )
public class SleepyMod implements DedicatedServerModInitializer
{

	public static final String MODID = "sleepy";

	@Override
	public void onInitializeServer( )
	{
		AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
		
		EntitySleepEvents.ALLOW_SETTING_SPAWN.register( (plr, pos) ->
		{
			ServerPlayerEntity srvPlr = (ServerPlayerEntity ) plr;
			boolean triggered = (srvPlr.isSneaking() && SleepyMod.getConfig().getSpawnpointSetStance() == 0);
			
			return triggered || !SleepyMod.getConfig( ).getEnabled( );
		});
	}
	
	public static ModConfig getConfig()
	{
		if (config == null)
		{
			config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
		}
		return config;
	}
	private static ModConfig config;
	
	@Config(name = MODID)
	public static class ModConfig implements ConfigData
	{
		@Comment("Toggles the entire mod on or off")
		boolean enabled = true;
		
		@Comment("When to set spawnpoint? 0 for crouch, 1 for standing.")
		int spawnpointSetStance=0;

		public boolean getEnabled()
		{
			return enabled;
		}
		public int getSpawnpointSetStance() { return spawnpointSetStance; }
	}
}
