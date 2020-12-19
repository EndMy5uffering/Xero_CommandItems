package com.commanditem.save;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import com.commanditem.util.DescriptionReader;
import com.commanditems.main.CommandDispacher;
import com.commanditems.main.ItemManager;

import commanditems.easyitems.EasyItem;

public class DatabaseStorage {

	public enum Format{
		SQlite,
		MySql
	}
	
	String SQLiteurl = "";
	static DatabaseInfo dbinfo;
	public DatabaseStorage(Plugin plugin) {
		SQLiteurl = "jdbc:sqlite:" + plugin.getDataFolder() + File.separator + "CommandItems.sqlite";
		dbinfo = DatabaseAccess.getDatabaseInfo(SQLiteurl, null, null);
	}
	
	public DatabaseStorage(Plugin plugin, String name, String pass, String path) {
		String MySQLurl = "jdbc:mysql:" + path;
		dbinfo = DatabaseAccess.getDatabaseInfo(MySQLurl, name, pass);
	}
	
	public void createNewDatabase(Format f) {
		String idSQLite = "id INTEGER PRIMARY KEY AUTOINCREMENT";
		String idMySql = "id INT AUTO_INCREMENT PRIMARY KEY";
		String sql = "CREATE TABLE IF NOT EXISTS commanditems ("
				+ " {id},"
				+ " name VARCHAR(30),"
				+ " material VARCHAR(35),"
				+ " description VARCHAR(256),"
				+ " command VARCHAR(256),"
				+ " executor VARCHAR(7),"
				+ " lifecount VARCHAR(3),"
				+ " itemuuid VARCHAR(36)"
				+ " ); ";
		
		if(f.equals(DatabaseStorage.Format.SQlite)) sql = sql.replace("{id}", idSQLite);
		if(f.equals(DatabaseStorage.Format.MySql)) sql = sql.replace("{id}", idMySql);
		
		try {
			DatabaseAccess.executeQuerry(dbinfo, sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveItem(EasyItem item) {
		try {
			DatabaseAccess.executeQuerry(dbinfo, getInsertString(item));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static String getInsertString(EasyItem item) {
		String lore = "null";
		
		if(item.getItem().getItemMeta().getLore() != null && !item.getItem().getItemMeta().getLore().isEmpty()) lore = DescriptionReader.formatBack(item.getItem().getItemMeta().getLore());
		
		String querry = "INSERT INTO commanditems(name, material, description, command, executor, lifecount, itemuuid)"
				+ " VALUES('" + item.getItem().getItemMeta().getDisplayName() + "'" +
				",'" + item.getItem().getType() + "'" +
				",'" + lore + "'" +
				",'" + ItemManager.getCommandUUID(EasyItem.getNbtTag(item.getItem(), "ItemUUID")) + "'" +
				",'" + ItemManager.getExecuterUUID(EasyItem.getNbtTag(item.getItem(), "ItemUUID")) + "'" +
				",'" + EasyItem.getNbtTag(item.getItem(), EasyItem.getCustomtaglifecount()) + "'" +
				",'" + EasyItem.getNbtTag(item.getItem(), "ItemUUID") + "'" + ");";
	
		return querry;
	}
	
	public static void removeItem(EasyItem item) {
		try {
			DatabaseAccess.executeQuerry(dbinfo, "DELETE FROM commanditems WHERE name='" + item.getItem().getItemMeta().getDisplayName() + "';");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void loadItems() {
		try {
			ResultSet s = DatabaseAccess.getData(dbinfo, "SELECT * FROM commanditems;");
			
			while(s.next()) {
				String name = s.getString(2);
				String material = s.getString(3);
				String Command = s.getString(5);
				String executor = s.getString(6);
				String livecount = s.getString(7);
				String uuid = s.getString(8);
				List<String> lore = DescriptionReader.read(s.getString(4));
				EasyItem item = new EasyItem(name, livecount, Material.valueOf(material), lore, x ->{
					CommandDispacher.dispach(x);
				}, uuid);
				item.setNbtTag("ItemUUID", uuid);
				ItemManager.addUUID(uuid, item);
				ItemManager.addCommandUUID(uuid, Command);
				ItemManager.addExecuterUUID(uuid, executor);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
