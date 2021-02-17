package me.monkeykiller.suvitroll.infernal_armor;

import org.bukkit.Material;

import de.tr7zw.nbtapi.NBTContainer;
import me.monkeykiller.survitroll.classes.CustomItem;

public class InfernalLeggings extends CustomItem {
	public InfernalLeggings(String ItemId, int CustomModelData, Material ItemMaterial, String ItemName,
			String ItemNameColor) {
		super(ItemId, CustomModelData, ItemMaterial, ItemName, ItemNameColor);
		NBTContainer nbt = new NBTContainer("{display:{color:16711680},HideFlags:4,Unbreakable:1b}");
		nbt.mergeCompound(new NBTContainer(
				"{AttributeModifiers:[{AttributeName:\"generic.armor\",Name:\"generic.armor\",Amount:7,Operation:0,UUID:[I;-1587863142,-1238872436,-1591395824,620125164],Slot:\"legs\"},{AttributeName:\"generic.armor_toughness\",Name:\"generic.armor_toughness\",Amount:4,Operation:0,UUID:[I;1828556821,2004438898,-1124534508,-1780380397],Slot:\"legs\"},{AttributeName:\"generic.knockback_resistance\",Name:\"generic.knockback_resistance\",Amount:0.11,Operation:0,UUID:[I;2049964226,1829259467,-1364292716,-493331478],Slot:\"legs\"}]}"));
		this.setNBT(nbt);
	}
}
