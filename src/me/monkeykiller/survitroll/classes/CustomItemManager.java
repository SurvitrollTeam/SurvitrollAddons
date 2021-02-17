package me.monkeykiller.survitroll.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;

import de.tr7zw.nbtapi.NBTContainer;

public class CustomItemManager {
	private Set<CustomItem> citems = new HashSet<CustomItem>();

	public void addCustomItem(String ItemId, int CustomModelData, Material ItemMaterial, String ItemName,
			String ItemNameColor) {
		this.citems.add(new CustomItem(ItemId, CustomModelData, ItemMaterial, ItemName, ItemNameColor));
	}

	public void addCustomItem(String ItemId, int CustomModelData, Material ItemMaterial, String ItemName,
			String ItemNameColor, NBTContainer nbt) {
		this.citems.add(new CustomItem(ItemId, CustomModelData, ItemMaterial, ItemName, ItemNameColor, nbt));
	}
	
	public void addCustomItem(CustomItem customItem) {
		this.citems.add(customItem);
	}

	public CustomItem getItemById(String ItemId) {
		for (CustomItem item : citems)
			if (item.getItemId().equalsIgnoreCase(ItemId))
				return item;
		return null;
	}

	public ArrayList<String> getIdList() {
		ArrayList<String> tmp = new ArrayList<>();
		for (CustomItem item : citems)
			tmp.add(item.getItemId());
		Collections.sort(tmp, String.CASE_INSENSITIVE_ORDER);
		return tmp;
	}
}
