package com.commanditems.main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.commanditem.save.DatabaseStorage;
import com.commanditems.command.CommandItemListCommand;
import com.commanditems.command.CreateCommand;
import com.commanditems.command.GiveItemCommand;
import com.commanditems.command.ImportPresetsCommand;
import com.commanditems.command.OpenItemListUICommand;
import com.commanditems.command.RemoveItemCommand;

import commanditems.easyitems.ItemHandler;
import commanditems.gui.easygui.InventoryManager;


public class CommandItems extends JavaPlugin{
	private DatabaseStorage storage;
	public static Plugin plugin;
	public static ConfigManager cm;
	
	@Override
	public void onEnable() {
		config();
		cm = new ConfigManager(this);
		cm.CreateFiles();
		createSQLiteDatabase();

		storage.loadItems();
		
		getCommand("createitem").setExecutor(new CreateCommand());
		getCommand("listcommanditems").setExecutor(new CommandItemListCommand());
		getCommand("givecommanditem").setExecutor(new GiveItemCommand());
		getCommand("removecommanditem").setExecutor(new RemoveItemCommand());
		getCommand("opencommanditemlist").setExecutor(new OpenItemListUICommand());
		getCommand("importpresets").setExecutor(new ImportPresetsCommand());

		getServer().getPluginManager().registerEvents(new ItemHandler(), this);
		getServer().getPluginManager().registerEvents(new InventoryManager(), this);
		plugin = this;
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public void createSQLiteDatabase() {
		String format = this.getConfig().getString("PluginSettings.Savefile");
		String name = this.getConfig().getString("PluginSettings.Username");
		String password = this.getConfig().getString("PluginSettings.Password");
		String path = this.getConfig().getString("PluginSettings.Path");
		
		if(format.equalsIgnoreCase("sqlite")) {
			File gatesqlitefile = new File(this.getDataFolder(), File.separator + "CommandItems.sqlite");
			String SQLiteurl = "jdbc:sqlite:" + this.getDataFolder() + File.separator + "CommandItems.sqlite";
			if(!this.getDataFolder().exists()) {
				try {
					Files.createDirectory(Paths.get(this.getDataFolder().getPath()));
				} catch (IOException e) {
					System.out.println("Could not create data folder!");
					disable();
					return;
				}
			}
			
			if (!gatesqlitefile.exists()) {
				try {
					gatesqlitefile.createNewFile();
				} catch (IOException e) {
					System.out.println("Could not create new save file!");
					disable();
					return;
				}
			}
			try {
				DriverManager.getConnection(SQLiteurl);
			} catch (SQLException e) {
				e.printStackTrace();
				disable();
				return;
			}
			this.storage = new DatabaseStorage(this);
			storage.createNewDatabase(DatabaseStorage.Format.SQlite);
		}else {
			if(name == null || password == null || path == null) {
				this.getLogger().log(Level.WARNING, "Database informations could not be read!");
				disable();
				return;
			}
			
			
			this.storage = new DatabaseStorage(this,name, password, path);
			storage.createNewDatabase(DatabaseStorage.Format.MySql);
		}
		
	}
	
	private void disable() {
		this.getServer().getPluginManager().disablePlugin(this);
	}
	
	public void config() {
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
	}
	
}
