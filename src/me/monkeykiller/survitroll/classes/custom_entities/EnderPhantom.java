package me.monkeykiller.survitroll.classes.custom_entities;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Phantom;

import me.monkeykiller.survitroll.Main;

public class EnderPhantom extends CustomEntity {
	private Main plugin;

	public EnderPhantom(Main plugin) {
		super("survitroll:ender_phantom", EntityType.PHANTOM);
		this.plugin = plugin;
	}

	@Override
	public void spawn(Location location) {
		Phantom entity = (Phantom) location.getWorld().spawnEntity(location, EntityType.PHANTOM);
		entity.setLootTable(null);
		entity.setSize(8);
		entity.setCustomName(plugin.methods.color("&6&lEnder Phantom"));
		entity.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(50.0d);
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(100.0d);
		entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(25.0d);
		entity.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK).setBaseValue(8.0d);
		entity.setHealth(100.0d);
	}
}
