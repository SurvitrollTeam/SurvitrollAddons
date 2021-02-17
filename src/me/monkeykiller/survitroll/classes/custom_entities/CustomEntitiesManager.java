package me.monkeykiller.survitroll.classes.custom_entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CustomEntitiesManager {
	public static Set<CustomEntity> CustomEntities = new HashSet<CustomEntity>();

	public static void addCustomEntity(CustomEntity customEntity) {
		CustomEntities.add(customEntity);
	}

	public static ArrayList<String> getIdList() {
		ArrayList<String> output = new ArrayList<String>();
		for (CustomEntity CE : CustomEntities)
			output.add(CE.getId());
		return output;
	}

	public static CustomEntity getCustomEntitybyId(String id) {
		for (CustomEntity CE : CustomEntities)
			if (CE.getId().equalsIgnoreCase(id))
				return CE;
		return null;
	}
}
