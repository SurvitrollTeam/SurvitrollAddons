package me.monkeykiller.survitroll.classes.custom_entities;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class CustomEntity {
	protected String id;
	protected EntityType entityType;

	public CustomEntity(String id, EntityType entityType) {
		this.id = id;
		this.entityType = entityType;
	}

	public String getId() {
		return id;
	}

	public EntityType getEntityType() {
		return entityType;
	}

	public void spawn(Location location) {
	}
}
