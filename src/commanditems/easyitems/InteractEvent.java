package commanditems.easyitems;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class InteractEvent {

	private Player player = null;
	private ItemStack item = null;
	private ItemStack mainHand = null;
	private ItemStack offHand = null;
	private EquipmentSlot hand = null;
	private Block clickedBlock = null;
	private Action action = null;
	
	
	public InteractEvent(Player player, ItemStack item, ItemStack mainHand, ItemStack offHand,
			EquipmentSlot hand, Block clickedBlock, Action action) {
		this.player = player;
		this.item = item;
		this.mainHand = mainHand;
		this.offHand = offHand;
		this.hand = hand;
		this.clickedBlock = clickedBlock;
		this.action = action;
	}

	public Player getPlayer() {
		return player;
	}

	public ItemStack getItem() {
		return item;
	}

	public ItemStack getMainHand() {
		return mainHand;
	}

	public ItemStack getOffHand() {
		return offHand;
	}

	public EquipmentSlot getHand() {
		return hand;
	}

	public Block getClickedBlock() {
		return clickedBlock;
	}

	public Action getAction() {
		return action;
	}
	
}
