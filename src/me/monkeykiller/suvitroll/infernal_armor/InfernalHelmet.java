package me.monkeykiller.suvitroll.infernal_armor;

import org.bukkit.Material;

import de.tr7zw.nbtapi.NBTContainer;
import me.monkeykiller.survitroll.classes.CustomItem;

public class InfernalHelmet extends CustomItem {

	public InfernalHelmet(String ItemId, int CustomModelData, Material ItemMaterial, String ItemName,
			String ItemNameColor) {
		super(ItemId, CustomModelData, ItemMaterial, ItemName, ItemNameColor);
		NBTContainer nbt = new NBTContainer("{display:{color:16711680},HideFlags:4,Unbreakable:1b}");
		nbt.mergeCompound(new NBTContainer(
				"{AttributeModifiers:[{Name: \"generic.armor\", Amount: 4, Operation: 0, UUID: [I; -517381906, -1743303050, -1600865225, -143096231], Slot: \"head\", AttributeName: \"generic.armor\"}, {Name: \"generic.armor_toughness\", Amount: 4, Operation: 0, UUID: [I; -950380671, -70432517, -1239281164, -321780697], Slot: \"head\", AttributeName: \"generic.armor_toughness\"}, {Name: \"generic.knockback_resistance\", Amount: 0.11d, Operation: 0, UUID: [I; 1093048049, -1246148996, -1987160870, 136511532], Slot: \"head\", AttributeName: \"generic.knockback_resistance\"}]}"));
		this.setNBT(nbt);
	}

}
