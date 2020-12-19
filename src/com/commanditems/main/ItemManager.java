package com.commanditems.main;

import java.util.HashMap;
import java.util.Map;

import commanditems.easyitems.EasyItem;
import commanditems.easyitems.ItemHandler;

public class ItemManager {

	private static Map<String, EasyItem> uuidItemMap = new HashMap<>();
	private static Map<String, EasyItem> nameItemMap = new HashMap<>();
	private static Map<String, String> uuidCommand = new HashMap<>();
	private static Map<String, String> uuidExecuter = new HashMap<>();
	
	public static void addUUID(String UUID, EasyItem item) {
		if(uuidItemMap.get(UUID) != null) return;
		uuidItemMap.put(UUID, item);
		nameItemMap.put(item.getItem().getItemMeta().getDisplayName(), item);
		ItemHandler.add(item);
	}
	
	public static void addCommandUUID(String UUID, String cmd) {
		uuidCommand.put(UUID, cmd);
	}
	
	public static void addExecuterUUID(String UUID, String executer) {
		uuidExecuter.put(UUID, executer);
	}
	
	public static EasyItem getItemByUUID(String UUID) {
		return uuidItemMap.get(UUID);
	}
	
	public static EasyItem getItemByName(String UUID) {
		return nameItemMap.get(UUID);
	}
	
	public static String getCommandUUID(String UUID) {
		return uuidCommand.get(UUID);
	}
	
	public static String getExecuterUUID(String UUID) {
		return uuidExecuter.get(UUID);
	}
	
	public static void removeItemUUID(String UUID) {
		EasyItem item = uuidItemMap.get(UUID);
		nameItemMap.remove(item.getItem().getItemMeta().getDisplayName());
		ItemHandler.removeItem(item);
		uuidCommand.remove(UUID);
		uuidItemMap.remove(UUID);
	}

	public static Map<String, EasyItem> getUUIDItemMap() {
		return uuidItemMap;
	}

	public static Map<String, String> getUUIDCommand() {
		return uuidCommand;
	}
	
}
