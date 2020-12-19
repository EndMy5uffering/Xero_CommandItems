package com.commanditems.main;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigManager {
	private Plugin pluign;
	public File presets;

	public ConfigManager(Plugin pluign) {
		this.pluign = pluign;
	}

	public void CreateFiles() {
		if (!this.pluign.getDataFolder().exists()) {
			this.pluign.getDataFolder().mkdir();
		}
		this.presets = new File(this.pluign.getDataFolder(), "presets.txt");
		
		if (!this.presets.exists()) {
			try {
				pluign.saveResource("presets.txt", false);
				this.presets = new File(this.pluign.getDataFolder(), "presets.txt");
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
}
