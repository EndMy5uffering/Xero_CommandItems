package commanditems.easyitems;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.commanditems.main.CommandItems;

public class ItemHandler implements Listener {
	
	private static Set<EasyItem> easyItems = new HashSet<EasyItem>();
	private static Set<Player> clicked = new HashSet<>();
	private static Set<Player> droped = new HashSet<>();
	
	public static void add(EasyItem... items) {
		easyItems.addAll(Arrays.asList(items));
	}
	
	public static void removeItem(EasyItem item) {
		easyItems.remove(item);
	}
	
	public static EasyItem getEasyItemWithItem(ItemStack item) {
		for(EasyItem i : easyItems) {
			if(i.getEasyItemUUID().equals(EasyItem.getNbtTag(item, EasyItem.customTagUUID))) {
				return i;
			}
		}
		return null;
	}
	
	@EventHandler
	public void OnPlayerInteract(PlayerInteractEvent e) {
			EasyItem i = ItemHandler.getEasyItemWithItem(e.getItem());
			if(i == null) return;
			if(droped.contains(e.getPlayer())) {
				e.setCancelled(true);
				droped.remove(e.getPlayer());
				return;
			}
			if(e.getHand().equals(EquipmentSlot.OFF_HAND) || !EasyItem.isValidItem(e.getItem()) || !i.hasAcces(e.getPlayer())) return;
			e.setCancelled(true);
			if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) return;
			String lives = EasyItem.getNbtTag(e.getItem(), EasyItem.customTagLifeCount);
			boolean stacked = isStacked(e.getItem());
			if(stacked && !hasAvaliableSlot(e.getPlayer()) && !lives.equals("-1")) {
				e.getPlayer().sendMessage("Not enough inventory space available!");
				return;
			}
			if(i.getAction() != null) {
				InteractEvent event = new InteractEvent(e.getPlayer(), e.getItem(), e.getPlayer().getInventory().getItemInMainHand(), e.getPlayer().getInventory().getItemInOffHand(), e.getHand(), e.getClickedBlock(), e.getAction());
				i.execute(event);
			}
			if(lives == null || lives.equals("") || lives.equals("-1")) return;
			try {
				int count = Integer.valueOf(lives);
				if(!stacked) {
					if(count < 2) {
						e.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
					}else {
						count--;
						e.getPlayer().getInventory().setItemInMainHand(EasyItem.setNbtTag(e.getItem(), EasyItem.customTagLifeCount, count+""));
					}
				}else {
					if(count < 2) {
						ItemStack item = e.getItem();
						item.setAmount(item.getAmount()-1);
						e.getPlayer().getInventory().setItemInMainHand(item);
					}else {
						count--;
						ItemStack item = e.getItem();
						item.setAmount(item.getAmount()-1);
						ItemStack less = EasyItem.setNbtTag(e.getItem(), EasyItem.customTagLifeCount, count+"");
						less.setAmount(1);
						e.getPlayer().getInventory().setItemInMainHand(item);
						e.getPlayer().getInventory().addItem(less);
					}
				}
			}catch(Exception except) {
				except.printStackTrace();
			}
			return;
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		EasyItem i = ItemHandler.getEasyItemWithItem(e.getItemDrop().getItemStack());
		if(i == null) return;
		droped.add(e.getPlayer());
	}
	
	public boolean hasAvaliableSlot(Player player){
		return player.getInventory().firstEmpty() != -1;
	}
	
	public boolean isStacked(ItemStack i) {
		return i.getAmount() > 1;
	}

	public static Set<EasyItem> getEasyItems() {
		return easyItems;
	}
	
}
