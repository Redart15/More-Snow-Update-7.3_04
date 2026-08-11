package net.helinos.moresnow.compat.aether;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import org.spongepowered.asm.mixin.Mixins;
import turniplabs.halplibe.helper.EnvironmentHelper;

public class MoreSnowAetherLoader implements PreLaunchEntrypoint {

	@Override
	public void onPreLaunch() {
		if (!EnvironmentHelper.isServerEnvironment()) {
			FabricLoader loader = FabricLoader.getInstance();
			if (loader.isModLoaded("aether")) {
				//TODO redo this once Aether has a 8.0 release
//				Mixins.addConfiguration("compat/aether/aether.mixins.json");
			}
		}
	}
}
