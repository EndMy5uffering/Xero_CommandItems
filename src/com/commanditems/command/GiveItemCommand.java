package com.commanditems.command;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.commanditem.util.ArgsOutput;
import com.commanditem.util.CommandArgsReader;
import com.commanditems.main.ItemManager;

public class GiveItemCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
		
		if(sender instanceof Player) {
			return true;
		}
		if(args.length < 2) {
			return false;
		}

		ArgsOutput out = CommandArgsReader.read(args);
		String player = out.get("player");
		String item = out.get("item");
		String ammount = out.get("ammount", "1");
		
		if(player.equals("null") || item.equals("null")) {
			Bukkit.getLogger().log(Level.WARNING, "/givecommanditem <player> <item> [ammount]");
			return false;
		}
		int itemAmmount = Integer.valueOf(ammount);
		
		ItemStack itemforplayer = ItemManager.getItemByName(item).getItem();
		itemforplayer.setAmount(itemAmmount);
		Bukkit.getPlayer(player).getInventory().addItem();
		
		return true;
	}
	
}
