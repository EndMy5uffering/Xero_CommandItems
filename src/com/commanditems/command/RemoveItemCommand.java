package com.commanditems.command;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.commanditem.save.DatabaseStorage;
import com.commanditem.util.ArgsOutput;
import com.commanditem.util.CommandArgsReader;
import com.commanditems.main.ItemManager;

import commanditems.easyitems.EasyItem;

public class RemoveItemCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
		
		if(sender instanceof Player) {
			return true;
		}
		if(args.length < 1) {
			return false;
		}

		ArgsOutput out = CommandArgsReader.read(args);
		
		EasyItem item = ItemManager.getItemByName(out.get("item"));
		
		if(item != null) {
			DatabaseStorage.removeItem(item);
			ItemManager.removeItemUUID(out.get("item"));
		}else {
			Bukkit.getLogger().log(Level.WARNING, "No item found with uuid: " + out.get("item"));
		}
		
		return true;
	}
}
