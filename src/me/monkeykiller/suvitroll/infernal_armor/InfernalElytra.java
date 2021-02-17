package me.monkeykiller.suvitroll.infernal_armor;

import org.bukkit.Material;

import de.tr7zw.nbtapi.NBTContainer;
import me.monkeykiller.survitroll.classes.CustomItem;

public class InfernalElytra extends CustomItem {
	
	public InfernalElytra(String ItemId, int CustomModelData, Material ItemMaterial, String ItemName,
			String ItemNameColor) {
		super(ItemId, CustomModelData, ItemMaterial, ItemName, ItemNameColor);
		NBTContainer nbt = new NBTContainer("{display:{color:16711680},HideFlags:4}");
		nbt.mergeCompound(new NBTContainer(
				"{AttributeModifiers:[{AttributeName:\"generic.armor\",Name:\"generic.armor\",Amount:9,Operation:0,UUID:[I;-1867307333,-2021701174,-2059891976,570986966],Slot:\"chest\"},{AttributeName:\"generic.armor_toughness\",Name:\"generic.armor_toughness\",Amount:4,Operation:0,UUID:[I;-730730558,-667203234,-1773860127,1289824428],Slot:\"chest\"},{AttributeName:\"generic.knockback_resistance\",Name:\"generic.knockback_resistance\",Amount:0.11,Operation:0,UUID:[I;1123504586,-1397141610,-2064154495,60481462],Slot:\"chest\"}]}"));
		this.setNBT(nbt);
	}
	
}
