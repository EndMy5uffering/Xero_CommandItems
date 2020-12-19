package com.commanditems.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.commanditem.guis.ItemListGUI;

import commanditems.easyitems.Globals;
import commanditems.gui.easygui.InventoryManager;
import net.md_5.bungee.api.ChatColor;

public class OpenItemListUICommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
	
		
		if(sender instanceof Player) {
			Player p = (Player)sender;
			
			if(p.hasPermission(Globals.PERMISSIONADMINOPENLISTGUI)) {
				ItemListGUI gui = new ItemListGUI(p);
				if(gui.OpenGUI()) InventoryManager.addGUI(gui);
			}else {
				p.sendMessage(ChatColor.RED + "You dont have permission to use this command!");
			}
		}
		
		return true;
	}
}
