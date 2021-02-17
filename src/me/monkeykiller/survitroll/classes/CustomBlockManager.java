package me.monkeykiller.survitroll.classes;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTContainer;
import de.tr7zw.nbtapi.NBTItem;

public class CustomBlockManager {
	private Set<CustomBlock> cblocks = new HashSet<CustomBlock>();

	public void addCustomBlock(String id, int CustomModelData, ItemStack loot, ItemStack silkloot, boolean allowFortune,
			boolean allowSilkTouch, int xpLoot) {
		this.cblocks.add(new CustomBlock(id, CustomModelData, loot, silkloot, allowFortune, allowSilkTouch, xpLoot));
	}

	public void addCustomBlock(CustomBlock cblock) {
		this.cblocks.add(cblock);
	}

	public CustomBlock getBlockById(String id) {
		for (CustomBlock block : cblocks)
			if (block.getId().equalsIgnoreCase(id))
				return block;
		return null;
	}

	public Set<CustomBlock> getBlockList() {
		return this.cblocks;
	}

	public void createBlock(Material itemMaterial, String itemName, String itemColor, String id, int CustomModelData,
			boolean allowFortune, boolean allowSilkTouch) {
		NBTItem nbt = new NBTItem(new ItemStack(itemMaterial));
		nbt.mergeCompound(getBlockCompound(id, CustomModelData, itemName, itemColor));
		addCustomBlock(id, CustomModelData, nbt.getItem(), nbt.getItem(), allowFortune, allowSilkTouch, 0);
	}

	public NBTContainer getBlockCompound(String ItemId, int CustomModelData, String itemName, String ItemNameColor) {
		return (new NBTContainer("{ItemId: \"" + ItemId + "\", CustomModelData: " + CustomModelData
				+ ", display: {Name: '{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\""
				+ ItemNameColor + "\",\"text\":\"" + itemName + "\"}],\"text\":\"\"}'}}"));
	}

	public void setupBlocks() {
		// INFERNAL NETHERITE BLOCK
		this.createBlock(Material.BARRIER, "Infernal Netherite Block", "gold", "survitroll:infernal_netherite_block", 1,
				false, true);

		// COPPER BLOCK
		this.createBlock(Material.BARRIER, "Copper Block", "white", "survitroll:copper_block", 2, false, true);

		// COPPER ORE
		this.createBlock(Material.BARRIER, "Copper Ore", "white", "survitroll:copper_ore", 3, false, true);

		// RUBY ORE
		NBTItem ro_nbtmeta = new NBTItem(new ItemStack(Material.BARRIER));
		ro_nbtmeta.mergeCompound(this.getBlockCompound("survitroll:ruby_ore", 4, "Ruby Ore", "white"));
		NBTItem r_nbtmeta = new NBTItem(new ItemStack(Material.GUNPOWDER));
		r_nbtmeta.mergeCompound(this.getBlockCompound("survitroll:ruby", 1, "Ruby", "white"));

		this.addCustomBlock("survitroll:ruby_ore", 4, r_nbtmeta.getItem(), ro_nbtmeta.getItem(), true, true, 6);

		// RUBY BLOCK
		this.createBlock(Material.BARRIER, "Ruby Block", "white", "survitroll:ruby_block", 5, false, true);

		// this.addCustomBlock("survitroll:debug", 6, null, null, false, false, 0);
		NBTItem debug_nbtmeta = new NBTItem(new ItemStack(Material.BARRIER));
		debug_nbtmeta.mergeCompound(this.getBlockCompound("survitroll:debug", 6, "debug", "white"));
		this.addCustomBlock("survitroll:debug", 6, debug_nbtmeta.getItem(), debug_nbtmeta.getItem(), true, true, 0);
		// DEBUG 2

		NBTItem debug2_nbtmeta = new NBTItem(new ItemStack(Material.BARRIER));
		debug2_nbtmeta.mergeCompound(this.getBlockCompound("survitroll:debug_2", 7, "debug_2", "white"));
		this.addCustomBlock("survitroll:debug_2", 7, debug2_nbtmeta.getItem(), debug2_nbtmeta.getItem(), true, true, 0);

		NBTItem unknown = new NBTItem(new ItemStack(Material.BARRIER));
		unknown.mergeCompound(this.getBlockCompound("survitroll:unknown", 999999999, "unknown", "white"));
		this.addCustomBlock("survitroll:unknown", 999999999, unknown.getItem(), unknown.getItem(), true, true, 0);
		// this.createBlock(Material.BARRIER, "unknown", "white",
		// "survitroll:unknown", 999999999, false, true);

		// this.createBlock(Material.BARRIER, "unknown", "white",
		// "survitroll:unknown", 999999999, false, true);
	}

}
