package net.helinos.moresnow;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.helinos.moresnow.block.init.MoreSnowBlocks;
import net.minecraft.client.gui.options.data.OptionsPage;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.Registry;
import net.minecraft.core.lang.I18n;
import turniplabs.halplibe.util.BlockInitEntrypoint;
import turniplabs.halplibe.util.ClientStartEntrypoint;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.toml.Toml;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoreSnow implements ClientStartEntrypoint, GameStartEntrypoint, ModInitializer, ClientModInitializer, BlockInitEntrypoint {
	public static final String MOD_ID = "moresnow";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final TomlConfigHandler CONFIG = new TomlConfigHandler(MOD_ID, new Toml("More Snow configuration file."), false);
    public static final Registry<Block<?>> LAYERS = new Registry<>();
    public ModSettings modSettings;
	public static I18n LANGUAGE = null;
	public static OptionsPage MOD_OPTIONS = null;

	static {
		File configFile = CONFIG.getConfigFile();
		if (configFile.exists()) {
			CONFIG.loadConfig();
            CONFIG.setDefaults(CONFIG.getRawParsed());
		} else {
			Toml defaultConfig = new Toml("More Snow configuration file.");
			defaultConfig.addCategory("BlockIDs");

			CONFIG.setDefaults(defaultConfig);

			try {
                configFile.getParentFile().mkdirs();
                configFile.createNewFile();
                CONFIG.writeConfig();
                CONFIG.loadConfig();
            } catch (IOException e) {
                throw new RuntimeException("Failed to generate configuration file!", e);
            }
		}
	}

	@Override
	public void onInitialize() {/* no need */}

	@Override
	public void onInitializeClient() {/* no need */}

	@Override
	public void afterGameStart(){
		LANGUAGE = I18n.getInstance();
	}

	@Override
	public void beforeGameStart() {
		MoreSnow.LAYERS.register("snow", Blocks.LAYER_SNOW);
		MoreSnow.LAYERS.register("leaves", Blocks.LAYER_LEAVES_OAK);
		MoreSnow.LAYERS.register("slate", Blocks.LAYER_SLATE);
	}

	@Override
	public void afterClientStart() {/* no need */}

	@Override
	public void beforeClientStart() {
	}

	@Override
	public void afterBlockInit() {
		MoreSnowBlocks.init();
	}
}
