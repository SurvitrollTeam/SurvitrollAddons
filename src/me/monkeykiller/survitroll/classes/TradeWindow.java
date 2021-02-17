package me.monkeykiller.survitroll.classes;

import java.util.Arrays;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.sun.istack.internal.NotNull;

import de.tr7zw.nbtapi.NBTItem;

public class TradeWindow {
	private String id;
	private TraderImage img;
	private TradeComponent trade1;
	private TradeComponent trade2;
	private TradeComponent trade3;
	@SuppressWarnings("unused")
	private TradeComponent trade4;
	@SuppressWarnings("unused")
	private TradeComponent trade5;

	private Map<String, String> pixelSpliters = Maps
			.newHashMap(ImmutableMap.of("-9", "", "-130", "", "-66", "", "+1", ""));

	private String menuBase = ChatColor.WHITE + pixelSpliters.get("-9") + "솋";

	public TradeWindow(String id, TraderImage img, @NotNull TradeComponent trade1) {
		craftTradeWindow(id, img, trade1, null, null, null, null);
	}

	public TradeWindow(String id, TraderImage img, @NotNull TradeComponent trade1, @NotNull TradeComponent trade2) {
		craftTradeWindow(id, img, trade1, trade2, null, null, null);
	}

	public TradeWindow(String id, TraderImage img, @NotNull TradeComponent trade1, @NotNull TradeComponent trade2,
			@NotNull TradeComponent trade3) {
		craftTradeWindow(id, img, trade1, trade2, trade3, null, null);
	}

	public TradeWindow(String id, TraderImage img, @NotNull TradeComponent trade1, @NotNull TradeComponent trade2,
			@NotNull TradeComponent trade3, @NotNull TradeComponent trade4) {
		craftTradeWindow(id, img, trade1, trade2, trade3, trade4, null);
	}

	public TradeWindow(String id, TraderImage img, @NotNull TradeComponent trade1, @NotNull TradeComponent trade2,
			@NotNull TradeComponent trade3, @NotNull TradeComponent trade4, @NotNull TradeComponent trade5) {
		craftTradeWindow(id, img, trade1, trade2, trade3, trade4, trade5);
	}

	private void craftTradeWindow(String id, TraderImage img, TradeComponent trade1, TradeComponent trade2,
			TradeComponent trade3, TradeComponent trade4, TradeComponent trade5) {
		this.id = id;
		this.img = img;
		this.trade1 = trade1;
		this.trade2 = trade2;
		this.trade3 = trade3;
		this.trade4 = trade4;
		this.trade5 = trade5;
	}

	public String getTradeId() {
		return id;
	}

	private String generateGUI(Player player) {
		/*
		 * /give @p
		 * chest{display:{Name:'{"color":"white","text":"솋솙속손"}'}}
		 * 솋솙속 솋솙속손 -9-18-9-5 씄
		 */
		String output = menuBase + "" + img.getChar();
		if (trade1 != null)
			output += "속"; // 솎

		if (trade2 != null)
			output += "손";
		if (trade3 != null)
			output += "손";

		return output;
	}

	private Inventory getWindow(Player player) {
		Inventory inv = Bukkit.getServer().createInventory(null, 54, generateGUI(player));
		if (trade1 != null) {
			inv.setItem(1, trade1.getSellItem());
			ItemStack invisItem = new ItemStack(Material.PAPER);
			ItemMeta meta = invisItem.getItemMeta();
			meta.setCustomModelData(8006);
			invisItem.setItemMeta(meta);
			inv.setItem(2, invisItem);
			inv.setItem(3, trade1.getBuyItem());
		}
		if (trade2 != null) {
			inv.setItem(10, trade2.getSellItem());
			ItemStack invisItem = new ItemStack(Material.PAPER);
			ItemMeta meta = invisItem.getItemMeta();
			meta.setCustomModelData(8006);
			invisItem.setItemMeta(meta);
			inv.setItem(11, invisItem);
			inv.setItem(12, trade2.getBuyItem());
		}

		NBTItem itemnbt = new NBTItem(new ItemStack(Material.PAPER));
		itemnbt.setString("TradeWindowId", id);
		//itemnbt.mergeCompound(new NBTContainer("{TradeWindowId:\"" + id + "\"}"));
		ItemStack item = itemnbt.getItem();
		ItemMeta meta = item.getItemMeta();
		meta.setCustomModelData(8006);
		meta.setDisplayName(
				ChatColor.WHITE + "Original from" + ChatColor.GOLD + "" + ChatColor.BOLD + " Origin Realms");
		meta.setLore(Arrays.asList(ChatColor.WHITE + "Originaly created by SystemZee"));
		item.setItemMeta(meta);
		inv.setItem(53, item);
		return inv;
	}

	public void openWindow(Player player) {
		player.openInventory(getWindow(player));
	}
	
	public void onClick(InventoryClickEvent event) {
		
	}
}
