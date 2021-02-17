package me.monkeykiller.suvitroll.infernal_armor;

import org.bukkit.Material;

import de.tr7zw.nbtapi.NBTContainer;
import me.monkeykiller.survitroll.classes.CustomItem;

public class InfernalBoots extends CustomItem {
	public InfernalBoots(String ItemId, int CustomModelData, Material ItemMaterial, String ItemName,
			String ItemNameColor) {
		super(ItemId, CustomModelData, ItemMaterial, ItemName, ItemNameColor);
		NBTContainer nbt = new NBTContainer("{display:{color:16711680},HideFlags:4,Unbreakable:1b}");
		nbt.mergeCompound(new NBTContainer(
				"{AttributeModifiers:[{AttributeName:\"generic.armor\",Name:\"generic.armor\",Amount:4,Operation:0,UUID:[I;1943952614,-1488237289,-1911519543,874019916],Slot:\"feet\"},{AttributeName:\"generic.armor_toughness\",Name:\"generic.armor_toughness\",Amount:4,Operation:0,UUID:[I;-1851956083,1661226888,-1600690145,-1284276452],Slot:\"feet\"},{AttributeName:\"generic.knockback_resistance\",Name:\"generic.knockback_resistance\",Amount:0.11,Operation:0,UUID:[I;-2095964221,322325611,-1685387095,1435557408],Slot:\"feet\"}]}"));
		this.setNBT(nbt);
	}
}
