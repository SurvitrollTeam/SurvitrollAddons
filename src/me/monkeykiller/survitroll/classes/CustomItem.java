package me.monkeykiller.survitroll.classes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTContainer;
import de.tr7zw.nbtapi.NBTItem;

public class CustomItem {
	String ItemId;
	int CustomModelData;
	String ItemName;
	String ItemNameColor;
	Material ItemMaterial;
	NBTContainer nbt = null;

	public CustomItem(String ItemId, int CustomModelData, Material ItemMaterial, String ItemName,
			String ItemNameColor) {
		this.ItemId = ItemId;
		this.CustomModelData = CustomModelData;
		this.ItemName = ItemName;
		this.ItemNameColor = ItemNameColor;
		this.ItemMaterial = ItemMaterial;
	}

	public CustomItem(String ItemId, int CustomModelData, Material ItemMaterial, String ItemName, String ItemNameColor,
			NBTContainer nbt) {
		this.ItemId = ItemId;
		this.CustomModelData = CustomModelData;
		this.ItemName = ItemName;
		this.ItemNameColor = ItemNameColor;
		this.ItemMaterial = ItemMaterial;
		this.nbt = nbt;
	}

	public String getItemId() {
		return ItemId;
	}

	public void setItemId(String ItemId) {
		this.ItemId = ItemId;
	}

	public int getCustomModelData() {
		return CustomModelData;
	}

	public void setCustomModelData(int CustomModelData) {
		this.CustomModelData = CustomModelData;
	}

	public NBTContainer getNBT() {
		return nbt;
	}

	public void setNBT(NBTContainer nbt) {
		this.nbt = nbt;
	}

	public ItemStack get() {
		NBTItem nbtItem = new NBTItem(new ItemStack(ItemMaterial));
		nbtItem.mergeCompound(getBlockCompound(ItemId, CustomModelData, ItemName, ItemNameColor));
		return nbtItem.getItem();
	}

	public NBTContainer getBlockCompound() {
		return getBlockCompound(ItemId, CustomModelData, ItemName, ItemNameColor);
	}

	public NBTContainer getBlockCompound(String ItemId, int CustomModelData, String ItemName, String ItemNameColor) {
		NBTContainer NBTc = new NBTContainer("{ItemId: \"" + ItemId + "\", CustomModelData: " + CustomModelData
				+ ", display: {Name: '{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\""
				+ ItemNameColor + "\",\"text\":\"" + ItemName + "\"}],\"text\":\"\"}'}}");
		if (nbt != null)
			NBTc.mergeCompound(nbt);
		return NBTc;
	}

}
