package com.commanditems.main;

import org.bukkit.command.CommandSender;

import commanditems.easyitems.EasyItem;
import commanditems.easyitems.InteractEvent;

public class CommandDispacher {

	public static void dispach(InteractEvent e) {
		String command = ItemManager.getCommandUUID(EasyItem.getNbtTag(e.getItem(), "ItemUUID"));
		if(command == null) return;
		command = command.replace("{PLAYERNAME}", e.getPlayer().getName());
		CommandSender s = null;
		String executer = ItemManager.getExecuterUUID(EasyItem.getNbtTag(e.getItem(), "ItemUUID"));
		if(executer != null) {
			if(executer.equals("player")) {
				s = e.getPlayer();
			}else {
				s = CommandItems.plugin.getServer().getConsoleSender();
			}
		}
		if(s == null) return;
		CommandItems.plugin.getServer().dispatchCommand(s, command);
	}
	
}
