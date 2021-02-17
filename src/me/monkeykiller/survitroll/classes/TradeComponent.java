package me.monkeykiller.survitroll.classes;

import org.bukkit.inventory.ItemStack;

public class TradeComponent {
	private ItemStack sellItem;
	private ItemStack buyItem;

	public TradeComponent(ItemStack sellItem, ItemStack buyItem) {
		this.sellItem = sellItem;
		this.buyItem = buyItem;
	}

	public ItemStack getSellItem() {
		return sellItem;
	}

	public void setSellItem(ItemStack sellItem) {
		this.sellItem = sellItem;
	}

	public ItemStack getBuyItem() {
		return buyItem;
	}

	public void setBuyItem(ItemStack buyItem) {
		this.buyItem = buyItem;
	}

}
