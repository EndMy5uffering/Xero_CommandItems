package commanditems.easyitems;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EasyItem {

	private ItemStack item;
	private final ItemAction action;
	private Set<String> Permissions = new HashSet<String>();
	
	protected final static String code = "31415926535";
	protected final static String customTagName = "EASYITEMCODE";
	protected final static String customTagUUID = "EASYITEMUUID";
	protected final static String customTagRandom = "EASYITEMRANDOM";
	protected final static String customTagLifeCount = "LIFECOUNT";
	protected final static String LifeCount = "-1";
	
	public EasyItem(String name, Material m, List<String> lore, ItemAction action) {
		item = getItem(name, lore, m);
		item = setNbtTag(item, customTagName, code);
		item = setNbtTag(item, customTagLifeCount, LifeCount);
		item = setNbtTag(item, customTagUUID, UUID.randomUUID().toString());
		this.action = action;
	}
	
	public EasyItem(String name, String lifeCount, Material m, List<String> lore, ItemAction action) {
		item = getItem(name, lore, m);
		item = setNbtTag(item, customTagName, code);
		item = setNbtTag(item, customTagLifeCount, lifeCount);
		item = setNbtTag(item, customTagUUID, UUID.randomUUID().toString());
		this.action = action;
	}
	
	public EasyItem(String name, Material m, List<String> lore, ItemAction action, String uuid) {
		item = getItem(name, lore, m);
		item = setNbtTag(item, customTagName, code);
		item = setNbtTag(item, customTagLifeCount, LifeCount);
		item = setNbtTag(item, customTagUUID, uuid);
		this.action = action;
	}
	
	public EasyItem(String name, String lifeCount, Material m, List<String> lore, ItemAction action, String uuid) {
		item = getItem(name, lore, m);
		item = setNbtTag(item, customTagName, code);
		item = setNbtTag(item, customTagLifeCount, lifeCount);
		item = setNbtTag(item, customTagUUID, uuid);
		this.action = action;
	}
	
	/**
	 * Creates an EasyItem and registers it in the handler.
	 * 
	 * @return EasyItem
	 * 
	 * */
	public static EasyItem createItem(String name, Material m, List<String> lore, ItemAction action) {
		EasyItem item = new EasyItem(name, m, lore, action);
		ItemHandler.add(item);
		return item;
	}
	
	public String getEasyItemUUID() {
		return getNbtTag(item, customTagUUID);
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ItemStack) && !(obj instanceof EasyItem)) return false;
		ItemStack other;

		if(obj instanceof ItemStack) {
			other = (ItemStack)obj;
		}else {
			other = ((EasyItem)obj).getItem(); 
		}

		if(!other.hasItemMeta()) return false;

		ItemMeta metaOther = other.getItemMeta();
		ItemMeta myMeta = item.getItemMeta();
		return (item.getType().equals(other.getType())
				&& myMeta.getDisplayName().equals(metaOther.getDisplayName()));
	}
	
	public static String getNbtTag(ItemStack item, String tag) {
		net.minecraft.server.v1_16_R3.ItemStack nbtItem = org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack.asNMSCopy(item);
		net.minecraft.server.v1_16_R3.NBTTagCompound nbtcomp = nbtItem.getTag();
		return nbtcomp == null ? "null" : nbtcomp.getString(tag);
	}
	
	public static ItemStack setNbtTag(ItemStack item, String tag, String value) {
		net.minecraft.server.v1_16_R3.ItemStack nbtItem = org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack.asNMSCopy(item);
		net.minecraft.server.v1_16_R3.NBTTagCompound nbtcomp = (nbtItem.hasTag()) ? nbtItem.getTag() : new net.minecraft.server.v1_16_R3.NBTTagCompound();
		nbtcomp.set(tag, net.minecraft.server.v1_16_R3.NBTTagString.a(value));
		nbtItem.setTag(nbtcomp);
		return org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack.asBukkitCopy(nbtItem);
	}
	
	public void setNbtTag(String tag, String value) {
		net.minecraft.server.v1_16_R3.ItemStack nbtItem = org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack.asNMSCopy(item);
		net.minecraft.server.v1_16_R3.NBTTagCompound nbtcomp = (nbtItem.hasTag()) ? nbtItem.getTag() : new net.minecraft.server.v1_16_R3.NBTTagCompound();
		nbtcomp.set(tag, net.minecraft.server.v1_16_R3.NBTTagString.a(value));
		nbtItem.setTag(nbtcomp);
		item = org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack.asBukkitCopy(nbtItem);
	}
	
	public static ItemStack getItem(String name, List<String> lore, Material m) {
		lore = lore == null ? new ArrayList<String>() : lore;
		ItemStack out = new ItemStack(m);
		ItemMeta meta = out.getItemMeta();
		meta.setLore(lore);
		meta.setDisplayName(name);
		out.setItemMeta(meta);
		return out;
	}
	
	public static boolean isValidItem(ItemStack item) {
		if(item.hasItemMeta() && getNbtTag(item, customTagName).equals(code)) {
			return true;
		}
		
		return false;
	}
	
	public void addPermissions(String... permissions) {
		for(String s : permissions) {
			this.Permissions.add(s);
		}
	}
	
	public boolean hasAcces(Player p) {
		if(Permissions.size() == 0) return true;
		for(String s : Permissions) {
			if(p.hasPermission(s)) return true;
		}
		return false;
	}
	
	public void execute(InteractEvent e) {
		if (e.getHand() == EquipmentSlot.HAND) {
			getAction().apply(e);
			return;
		}
		
	}
	
	public ItemStack getItem() {
		return item;
	}

	public ItemAction getAction() {
		return action;
	}

	public Set<String> getPermissions() {
		return Permissions;
	}

	public static String getCode() {
		return code;
	}

	public static String getCustomtagname() {
		return customTagName;
	}

	public static String getCustomtaglifecount() {
		return customTagLifeCount;
	}

	public static String getLifecount() {
		return LifeCount;
	}
	
	
	
}
