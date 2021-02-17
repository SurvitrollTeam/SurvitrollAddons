package me.monkeykiller.survitroll;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class PluginConfigFile {
	private Main plugin;
	private String fileName;
	private FileConfiguration config = null;
	private File configFile;

	public PluginConfigFile(Main plugin, String fileName) {
		this.plugin = plugin;
		this.fileName = fileName;
	}

	public FileConfiguration get() {
		if (config == null) {
			configFile = new File(plugin.getDataFolder(), fileName);
			reload();
		}
		return config;
	}

	public void reload() {
		config = YamlConfiguration.loadConfiguration(configFile);
		Reader defConfigStream;
		try {
			defConfigStream = new InputStreamReader(plugin.getResource(fileName), "UTF8");
			if (defConfigStream != null)
				config.setDefaults(YamlConfiguration.loadConfiguration(defConfigStream));

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	public void save() {
		try {
			config.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void reinstall() {
		configFile.delete();
		register();
	}

	public void register() {
		configFile = new File(plugin.getDataFolder(), fileName);
		if (!configFile.exists()) {
			get().options().copyDefaults(true);
			save();
		}
	}
}
