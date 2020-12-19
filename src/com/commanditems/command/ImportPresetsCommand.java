package com.commanditems.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.commanditem.save.DatabaseStorage;
import com.commanditem.util.ArgsOutput;
import com.commanditem.util.CommandArgsReader;
import com.commanditem.util.DescriptionReader;
import com.commanditems.main.CommandDispacher;
import com.commanditems.main.CommandItems;
import com.commanditems.main.ItemManager;

import commanditems.easyitems.EasyItem;

public class ImportPresetsCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
	
		if(sender instanceof Player) {
			return true;
		}
		
		File presets = CommandItems.cm.presets;
		
		try {
			FileReader reader = new FileReader(presets);
			BufferedReader breader = new BufferedReader(reader);
			
			breader.lines().filter(x -> {return x.charAt(0) != '#';})
			.forEach(x -> {
				ArgsOutput out = CommandArgsReader.read(x);
				String name = out.get("name");
				String material = out.get("material", "PAPER");
				String command = out.get("command");
				String executer = out.get("executer", "console");
				String lifecount = out.get("lifecount", "-1");
				
				if(name.equals("null") || command.equals("null")) {
					Bukkit.getLogger().log(Level.WARNING, "Name or Command can not be null!");
					return;
				}else if(ItemManager.getItemByName(name) != null) {
					Bukkit.getLogger().log(Level.WARNING, "An item with the given name allready exists!");
					return;
				}
				
				String uuid = UUID.randomUUID().toString();
				List<String> lore = DescriptionReader.read(out.get("description"));
				EasyItem item = new EasyItem(name,lifecount, Material.valueOf(material), lore, y ->{
					CommandDispacher.dispach(y);
				}, uuid);
				item.setNbtTag("ItemUUID", uuid);
				
				ItemManager.addUUID(uuid, item);
				ItemManager.addCommandUUID(uuid, command);
				ItemManager.addExecuterUUID(uuid, executer);
				DatabaseStorage.saveItem(item);
				
				return;
			});
			
			try {
				breader.close();
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return true;
	}
}
