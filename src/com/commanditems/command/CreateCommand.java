package com.commanditems.command;

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
import com.commanditems.main.ItemManager;

import commanditems.easyitems.EasyItem;
import commanditems.easyitems.Globals;
import net.md_5.bungee.api.ChatColor;

public class CreateCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
		Player p = null;
		if(sender instanceof Player) {
			p = (Player)sender;
			if(!p.hasPermission(Globals.PERMISSIONADMINCREATEITEM)) {
				p.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
				return true;
			}
		}
		ArgsOutput out = CommandArgsReader.read(args);
		String name = out.get("name");
		String material = out.get("material", "PAPER");
		String command = out.get("command");
		String executer = out.get("executor", "console");
		String lifecount = out.get("lifecount", "-1");
		
		if(name.equals("null") || command.equals("null")) {
			if(p == null) {
				Bukkit.getLogger().log(Level.WARNING, "Name or Command can not be null!");
				Bukkit.getLogger().log(Level.WARNING, "Command usage: /createitem name='sample' command='/help'");
				Bukkit.getLogger().log(Level.WARNING, "arguments: name<-NEEDED, material, command<-NEEDED, executer[player, console], lifecount[0-999 or -1 for Infinite]");
			}else {
				p.sendMessage(ChatColor.RED + "Name or Command can not be null!");
				p.sendMessage(ChatColor.GOLD + "Command usage: /createitem name='sample' command='/help'");
				p.sendMessage(ChatColor.GOLD + "arguments: name<-NEEDED, material, command<-NEEDED, executer[player, console], lifecount[0-999 or -1 for Infinite]");
			}
			return false;
		}else if(ItemManager.getItemByName(name) != null) {
			if(p == null) {
				Bukkit.getLogger().log(Level.WARNING, "An item with the given name allready exists!");
				return true;
			}else {
				p.sendMessage(ChatColor.RED + "An item with the given name allready exists!");
				return true;
			}
		}

		String uuid = UUID.randomUUID().toString();
		List<String> lore = DescriptionReader.read(out.get("description"));
		EasyItem item = new EasyItem(name,lifecount, Material.valueOf(material.toUpperCase()), lore, x ->{
			CommandDispacher.dispach(x);
		}, uuid);
		item.setNbtTag("ItemUUID", uuid);
		
		ItemManager.addUUID(uuid, item);
		ItemManager.addCommandUUID(uuid, command);
		ItemManager.addExecuterUUID(uuid, executer);
		DatabaseStorage.saveItem(item);	
		Bukkit.getLogger().log(Level.INFO, "Item was created and saved!");
		if(p != null) p.sendMessage(ChatColor.GREEN + "Item was created and saved!");
		return true;
	}
	
}
