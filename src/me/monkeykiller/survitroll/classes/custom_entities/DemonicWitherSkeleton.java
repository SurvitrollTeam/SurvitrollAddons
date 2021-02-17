package me.monkeykiller.survitroll.classes.custom_entities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import me.monkeykiller.survitroll.Main;

public class DemonicWitherSkeleton extends CustomEntity {
	private Main plugin;

	public DemonicWitherSkeleton(Main plugin) {
		super("survitroll:demonic_wither_skeleton", EntityType.WITHER_SKELETON);
		this.plugin = plugin;
	}

	@Override
	public void spawn(Location location) {
		WitherSkeleton entity = (WitherSkeleton) location.getWorld().spawnEntity(location, this.entityType);
		EntityEquipment equipment = entity.getEquipment();
		equipment.setChestplate(plugin.cimanager.getItemById("survitroll:infernal_netherite_chestplate").get());
		equipment.setChestplateDropChance(0.0f);
		equipment.setBoots(plugin.cimanager.getItemById("survitroll:infernal_netherite_boots").get());
		equipment.setBootsDropChance(0.0f);

		equipment.setItemInMainHand(plugin.cimanager.getItemById("survitroll:infernal_netherite_sword").get());
		equipment.setItemInMainHandDropChance(0.0f);
		equipment.setItemInOffHand(new ItemStack(Material.SHIELD));
		equipment.setItemInOffHandDropChance(0.0f);
		entity.setCustomName(plugin.methods.color("&6&lDemonic Wither Skeleton"));

		entity.setCanPickupItems(false);
		entity.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(30.0d);
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(50.0d);
		entity.setHealth(50.0d);
		entity.setLootTable(null);
	}

}
