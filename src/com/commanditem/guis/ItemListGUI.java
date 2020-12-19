package com.commanditem.guis;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.commanditems.main.ItemManager;

import commanditems.easyitems.EasyItem;
import commanditems.easyitems.Globals;
import commanditems.gui.easygui.GUIPages;

public class ItemListGUI extends GUIPages{

	public ItemListGUI(Player p) {
		super(p, 9*6, "CommandItems List", "ItemListGUI");
		
		this.addUIAccessPermission(Globals.PERMISSIONADMINOPENLISTGUI);
		setup();
	}
	
	public void setup() {
		ArrayList<ItemStack> items = new ArrayList<>();
		for(EasyItem i : ItemManager.getUUIDItemMap().values()) {
			items.add(i.getItem());
		}
		
		this.setSorceList(items);
		
		this.setGeneralFunction(x -> {
			if(x.event.getRawSlot() > -1 && x.event.getRawSlot() < 45 && !x.item.getType().equals(Material.BLACK_STAINED_GLASS_PANE)) {
				x.player.getInventory().addItem(x.item);
			}
		});
	}

}
