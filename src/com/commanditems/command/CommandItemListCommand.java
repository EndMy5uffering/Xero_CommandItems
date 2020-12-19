package com.commanditems.command;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.commanditems.main.ItemManager;

public class CommandItemListCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
	
		if(sender instanceof Player) {
			return true;
		}
		
		for(String s : ItemManager.getUUIDItemMap().keySet()) {
			Bukkit.getLogger().log(Level.INFO, "Item: " + s + " ItemName: " + ItemManager.getItemByUUID(s).getItem().getItemMeta().getDisplayName() + " command: " + ItemManager.getCommandUUID(s));
		}
		
		return true;
	}
	
}
