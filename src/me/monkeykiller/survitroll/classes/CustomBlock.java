package me.monkeykiller.survitroll.classes;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import me.monkeykiller.survitroll.Methods;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NBTTagList;
import net.minecraft.server.v1_16_R3.TileEntityMobSpawner;

public class CustomBlock {
	String id;
	int CustomModelData;
	ItemStack loot;
	ItemStack silkloot;

	boolean allowFortune = true;
	boolean allowSilkTouch = true;
	int xpLoot = 0;

	public CustomBlock(String id, int CustomModelData, ItemStack loot, ItemStack silkloot, boolean allowFortune,
			boolean allowSilkTouch, int xpLoot) {
		this.id = id;
		this.CustomModelData = CustomModelData;
		this.loot = loot;
		this.silkloot = silkloot;

		this.allowFortune = allowFortune;
		this.allowSilkTouch = allowSilkTouch;
		this.xpLoot = xpLoot;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ItemStack getLoot() {
		return loot;
	}

	public void setLoot(ItemStack loot) {
		this.loot = loot;
	}

	public ItemStack getSilkLoot() {
		return silkloot;
	}

	public void setSilkLoot(ItemStack silkloot) {
		this.silkloot = silkloot;
	}

	public void mine(BlockBreakEvent event, int fortuneLevel, boolean hasSilkTouch) {
		event.setExpToDrop(xpLoot);
		if (loot == null)
			return;
		Location blockLocation = event.getBlock().getLocation();
		if (fortuneLevel > 0 && allowFortune) {
			ItemStack fortuned = loot.clone();
			fortuned.setAmount(Methods.quantityDroppedWithBonus(fortuneLevel, new Random()));

			event.getBlock().getWorld().dropItemNaturally(blockLocation, fortuned);
		} else if (hasSilkTouch && allowSilkTouch) {
			if (silkloot != null)
				event.getBlock().getWorld().dropItemNaturally(blockLocation, silkloot);
		} else {
			event.getBlock().getWorld().dropItemNaturally(blockLocation, loot);
			// entity.setPickupDelay(8);
		}
	}

	public void place(Location loc) {
		Block block = loc.getBlock();
		block.setType(Material.SPAWNER);

		BlockPosition blockpos = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		TileEntityMobSpawner spawner = (TileEntityMobSpawner) ((CraftWorld) loc.getWorld()).getHandle()
				.getTileEntity(blockpos);

		NBTTagCompound tileData = spawner.b();
		NBTTagCompound spawnData = new NBTTagCompound();
		NBTTagList armor = new NBTTagList();
		NBTTagCompound head = new NBTTagCompound();
		NBTTagCompound tags = new NBTTagCompound();

		tags.setString("ItemId", id);
		tags.setInt("CustomModelData", CustomModelData);

		head.setString("id", "minecraft:barrier");
		head.setByte("Count", (byte) 1);
		head.set("tag", tags);

		armor.add(new NBTTagCompound());
		armor.add(new NBTTagCompound());
		armor.add(new NBTTagCompound());
		armor.add(head);

		spawnData.setString("id", "minecraft:armor_stand");
		spawnData.setByte("Marker", (byte) 1);
		spawnData.setByte("Invisible", (byte) 1);
		spawnData.set("ArmorItems", armor);

		tileData.setShort("SpawnCount", (short) 0);
		tileData.setShort("SpawnRange", (short) 0);
		tileData.setShort("MaxNearbyEntities", (short) 0);
		tileData.setShort("RequiredPlayerRange", (short) 0);
		tileData.set("SpawnData", spawnData);

		spawner.load(null, tileData);
	}

}
