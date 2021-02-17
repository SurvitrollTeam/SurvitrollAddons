package me.monkeykiller.survitroll;

import java.util.Random;
import java.util.regex.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import de.tr7zw.nbtapi.NBTItem;

public class Methods {

	public static int quantityDroppedWithBonus(int fortune, Random random) {
		if (fortune < 1)
			return 1;
		int i = random.nextInt(fortune + 2) - 1;
		if (i < 0)
			i = 0;
		return i + 1;
	}

	public boolean damage(int unbreakingLevel) {
		if (unbreakingLevel <= 0)
			return true;
		if (new Random().nextInt(unbreakingLevel + 1) <= 0)
			return true;
		return false;
	}

	public Location spread(Location entityLoc, double range) {
		Random random = new Random();
		double x = parseCoords(entityLoc.getX(), -30000000, 30000000);
		double z = parseCoords(entityLoc.getZ(), -30000000, 30000000);

		World world = entityLoc.getWorld();
		if (world == null)
			return null;
		double xRangeMin = x - range, zRangeMin = z - range, xRangeMax = x + range, zRangeMax = z + range;
		Location location = getSpreadLocation(world, random, xRangeMin, zRangeMin, xRangeMax, zRangeMax, entityLoc);
		return location;
	}

	private Location getSpreadLocation(World world, Random random, double xRangeMin, double zRangeMin, double xRangeMax,
			double zRangeMax, Location entityLoc) {
		double x = xRangeMin >= xRangeMax ? xRangeMin : random.nextDouble() * (xRangeMax - xRangeMin) + xRangeMin;
		double z = zRangeMin >= zRangeMax ? zRangeMin : random.nextDouble() * (zRangeMax - zRangeMin) + zRangeMin;
		Location location = new Location(world, x, entityLoc.getY(), z);
		Location output = new Location(world, Math.floor(location.getX()) + 0.5D,
				getBlockBelowLoc(location).getY() + 1.0D, Math.floor(location.getZ()) + 0.5D);
		return output;
	}

	public Location getBlockBelowLoc(Location loc) {
		Location locBelow = loc.subtract(0, 1, 0);
		if (locBelow.getBlock().getType() == Material.AIR)
			locBelow = getBlockBelowLoc(locBelow);
		return locBelow;
	}

	public double parseCoords(double input, double min, double max) {
		if (input < min)
			return min;
		if (input > max)
			return max;
		return input;

	}

	public static String color(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}
	

	public boolean validateIp(String ip) {
		Pattern pattern = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
				+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
		Matcher matcher = pattern.matcher(ip);
		return matcher.matches();
	}

	// REPEATING TASK METHODS

	public BukkitRunnable repeatingTask() {
		return new BukkitRunnable() {

			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					double maxHealth = 20.0d;
					EntityEquipment equipment = p.getEquipment();
					if (getCustomItemId(equipment.getHelmet()).equalsIgnoreCase("survitroll:infernal_netherite_helmet")
							&& (getCustomItemId(equipment.getChestplate())
									.equalsIgnoreCase("survitroll:infernal_netherite_chestplate")
									|| getCustomItemId(equipment.getChestplate())
											.equalsIgnoreCase("survitroll:infernal_netherite_elytra"))
							&& getCustomItemId(equipment.getLeggings())
									.equalsIgnoreCase("survitroll:infernal_netherite_leggings")
							&& getCustomItemId(equipment.getBoots())
									.equalsIgnoreCase("survitroll:infernal_netherite_boots"))
						maxHealth += 4.0d;

					p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
					if (maxHealth < p.getHealth())
						p.setHealth(maxHealth);

					ItemStack item = p.getInventory().getItemInMainHand();
					if (getCustomItemId(item).equalsIgnoreCase("survitroll:infernal_netherite_pickaxe")
							|| getCustomItemId(item).equalsIgnoreCase("survitroll:infernal_netherite_shovel")
							|| getCustomItemId(item).equalsIgnoreCase("survitroll:infernal_netherite_axe")
							|| getCustomItemId(item).equalsIgnoreCase("survitroll:infernal_netherite_hoe")) {

						if (p.hasPotionEffect(PotionEffectType.FAST_DIGGING)
								&& p.getPotionEffect(PotionEffectType.FAST_DIGGING).getAmplifier() >= 1)
							return;

						p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 10, 0, true, false, true));

					}

				}
			}

		};
	}

	private String getCustomItemId(ItemStack Item) {
		if (Item == null || Item.getType() == Material.AIR)
			return "survitroll:unknown";
		NBTItem ItemNBT = new NBTItem(Item);
		String ItemId = ItemNBT.getString("ItemId");
		if (ItemId == null)
			return "survitroll:unknown";
		return ItemId;
	}

}
