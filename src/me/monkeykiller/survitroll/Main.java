package me.monkeykiller.survitroll;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import de.tr7zw.nbtapi.NBTContainer;
import me.monkeykiller.survitroll.classes.CustomBlockManager;
import me.monkeykiller.survitroll.classes.CustomItemManager;
import me.monkeykiller.survitroll.classes.ORCustomBlock;
import me.monkeykiller.survitroll.classes.ORCustomBlockData;
import me.monkeykiller.survitroll.classes.ORCustomBlockMultiFace;
import me.monkeykiller.survitroll.classes.TradeComponent;
import me.monkeykiller.survitroll.classes.TradeWindow;
import me.monkeykiller.survitroll.classes.TradeWindowManager;
import me.monkeykiller.survitroll.classes.TraderImage;
import me.monkeykiller.survitroll.classes.custom_entities.CustomEntitiesManager;
import me.monkeykiller.survitroll.classes.custom_entities.DemonicWitherSkeleton;
import me.monkeykiller.survitroll.classes.custom_entities.EnderPhantom;
import me.monkeykiller.suvitroll.infernal_armor.InfernalBoots;
import me.monkeykiller.suvitroll.infernal_armor.InfernalChestplate;
import me.monkeykiller.suvitroll.infernal_armor.InfernalElytra;
import me.monkeykiller.suvitroll.infernal_armor.InfernalHelmet;
import me.monkeykiller.suvitroll.infernal_armor.InfernalLeggings;

@SuppressWarnings("static-access")
public class Main extends JavaPlugin implements CommandExecutor {

	public PluginConfigFile messages = new PluginConfigFile(this, "messages.yml");
	public PluginConfigFile ipsecurity = new PluginConfigFile(this, "ipsecurity.yml");

	public Methods methods = new Methods();
	public CommandManager cmdManager = new CommandManager(this);
	public CustomBlockManager cbmanager = new CustomBlockManager();
	public CustomItemManager cimanager = new CustomItemManager();
	public TradeWindowManager twmanager = new TradeWindowManager();
	private BukkitRunnable repeatingTask = methods.repeatingTask();

	public void onEnable() {
		registerConfig();

		Bukkit.getPluginManager().registerEvents(new Events(this), this);
		cbmanager.setupBlocks();

		setupItems();

		setupEntities();
		setupTrades();
		getCommand("itemhax").setTabCompleter(cmdManager);
		getCommand("mobhax").setTabCompleter(cmdManager);
		getServer().getConsoleSender()
				.sendMessage(methods.color(messages.get().getString("prefix") + "Plugin Enabled!"));
		repeatingTask.runTaskTimer(this, 0L, 1L);
	}

	public void onDisable() {
		getServer().getConsoleSender()
				.sendMessage(methods.color(messages.get().getString("prefix") + "Plugin Disabled!"));
		repeatingTask.cancel();
	}

	public void setupTrades() {
		ItemStack sell = cimanager.getItemById("survitroll:ruby").get();
		sell.setAmount(5);
		ItemStack buy = cimanager.getItemById("survitroll:copper_ingot").get();
		sell.setAmount(5);
		twmanager.addTradeWindow(new TradeWindow("survitroll:test", TraderImage.FARMER, new TradeComponent(sell, buy),
				new TradeComponent(sell, buy), new TradeComponent(sell, buy)));
	}

	public void setupItems() {
		// BLOCKS BEGIN
		cimanager.addCustomItem("survitroll:infernal_netherite_block", 1, Material.BARRIER, "Infernal Netherite Block",
				"gold");
		cimanager.addCustomItem("survitroll:copper_block", 2, Material.BARRIER, "Copper Block", "white");
		cimanager.addCustomItem("survitroll:copper_ore", 3, Material.BARRIER, "Copper Ore", "white");
		cimanager.addCustomItem("survitroll:ruby_ore", 4, Material.BARRIER, "Ruby Ore", "white");
		cimanager.addCustomItem("survitroll:ruby_block", 5, Material.BARRIER, "Ruby Block", "white");
		// DEBUG
		cimanager.addCustomItem("survitroll:debug", 6, Material.BARRIER, "debug", "white");
		cimanager.addCustomItem("survitroll:debug_2", 7, Material.BARRIER, "debug_2", "white");
		cimanager.addCustomItem("survitroll:unknown", 999999999, Material.BARRIER, "unknown", "white");
		// BLOCKS END

		// ITEMS
		cimanager.addCustomItem("survitroll:ruby", 1, Material.GUNPOWDER, "Ruby", "white");
		cimanager.addCustomItem("survitroll:copper_ingot", 2, Material.GUNPOWDER, "Copper Ingot", "white");

		cimanager.addCustomItem("survitroll:chorus_pearl", 1, Material.SNOWBALL, "Chorus Pearl", "aqua");
		cimanager.addCustomItem("survitroll:switcher", 2, Material.SNOWBALL, "Switcher", "gold");

		cimanager.addCustomItem("survitroll:golden_chorus_fruit", 1, Material.CHORUS_FRUIT, "Golden Chorus Fruit",
				"gold");
		cimanager.addCustomItem("survitroll:magic_mirror", 1, Material.CARROT_ON_A_STICK, "Magic Mirror", "yellow");
		cimanager.addCustomItem("survitroll:copper_wrench", 2, Material.CARROT_ON_A_STICK, "Copper Wrench", "white");
		cimanager.addCustomItem("survitroll:christmas_hat", 2, Material.LEATHER_HELMET, "Christmas Hat", "yellow");
		cimanager.addCustomItem("survitroll:infernal_netherite_ingot", 3, Material.GUNPOWDER,
				"Infernal Netherite Ingot", "gold");

		cimanager.addCustomItem("survitroll:infernal_netherite_scrap", 4, Material.GUNPOWDER,
				"Infernal Netherite Scrap", "gold");
		cimanager.addCustomItem("survitroll:experience_book", 1, Material.IRON_HORSE_ARMOR, "Experience Book", "yellow",
				new NBTContainer(getXPBookMeta(0)));

		cimanager.addCustomItem("survitroll:infernal_netherite_sword", 1, Material.NETHERITE_SWORD,
				"Infernal Netherite Sword", "gold", setupInfernalWeapon("9", 8.0, "1.6"));
		cimanager.addCustomItem("survitroll:infernal_netherite_axe", 1, Material.NETHERITE_AXE,
				"Infernal Netherite Axe", "gold", setupInfernalWeapon("11", 10.0, "1"));
		cimanager.addCustomItem("survitroll:infernal_netherite_pickaxe", 1, Material.NETHERITE_PICKAXE,
				"Infernal Netherite Pickaxe", "gold", setupInfernalWeapon("7", 6.0, "1.2"));
		cimanager.addCustomItem("survitroll:infernal_netherite_shovel", 1, Material.NETHERITE_SHOVEL,
				"Infernal Netherite Shovel", "gold", setupInfernalWeapon("7.5", 6.5, "1"));
		cimanager.addCustomItem("survitroll:infernal_netherite_hoe", 1, Material.NETHERITE_HOE,
				"Infernal Netherite Hoe", "gold", setupInfernalWeapon("2", 1.0, "4"));
		cimanager.addCustomItem(new InfernalHelmet("survitroll:infernal_netherite_helmet", 1, Material.LEATHER_HELMET,
				"Infernal Netherite Helmet", "dark_purple"));
		cimanager.addCustomItem(new InfernalChestplate("survitroll:infernal_netherite_chestplate", 1,
				Material.LEATHER_CHESTPLATE, "Infernal Netherite Chestplate", "dark_purple"));
		cimanager.addCustomItem(new InfernalElytra("survitroll:infernal_netherite_elytra", 1, Material.ELYTRA,
				"Infernal Netherite Elytra", "dark_purple"));
		cimanager.addCustomItem(new InfernalLeggings("survitroll:infernal_netherite_leggings", 1,
				Material.LEATHER_LEGGINGS, "Infernal Netherite Leggings", "dark_purple"));
		cimanager.addCustomItem(new InfernalBoots("survitroll:infernal_netherite_boots", 1, Material.LEATHER_BOOTS,
				"Infernal Netherite Boots", "dark_purple"));

		setupORBlocks();
	}

	public void setupEntities() {
		CustomEntitiesManager.addCustomEntity(new DemonicWitherSkeleton(this));
		CustomEntitiesManager.addCustomEntity(new EnderPhantom(this));
	}

	public String getXPBookMeta(int level) {
		return String.format(
				"{Levels: %s, display:{Lore: ['{\"extra\":[{\"bold\":true,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"green\",\"text\":\"XP Saved: \"},{\"bold\":false,\"italic\":false,\"color\":\"gray\",\"text\":\"%s\"}],\"text\":\"\"}']}}",
				level, level);
	}

	public NBTContainer setupInfernalWeapon(String attackDamageLore, double attackDamage, String attackSpeedLore) {
		NBTContainer nbt = new NBTContainer("{HideFlags: 2, Damage: 0}");
		nbt.mergeCompound(new NBTContainer(String.format(
				"{AttributeModifiers: [{Name: \"generic.attack_damage\", Amount: %s, Operation: 0, UUID: [I; -707604922, 699220125, -1509012632, -2135593081], Slot: \"mainhand\", AttributeName: \"minecraft:generic.attack_damage\"}]}",
				attackDamage)));
		nbt.mergeCompound(getLoreWithAttributes(attackDamageLore, attackSpeedLore));
		return nbt;
	}

	public NBTContainer getLoreWithAttributes(String attackDamage, String attackSpeed) {
		return new NBTContainer(
				"{display: {Lore: ['{\"extra\":[{\"text\":\" \"}],\"text\":\"\"}', '{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"gray\",\"text\":\"When in Main Hand:\"}],\"text\":\"\"}', '{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"dark_green\",\"text\":\" "
						+ attackDamage
						+ " \"},{\"italic\":false,\"color\":\"dark_green\",\"text\":\"Attack Damage\"}],\"text\":\"\"}', '{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"dark_green\",\"text\":\" "
						+ attackSpeed
						+ " \"},{\"italic\":false,\"color\":\"dark_green\",\"text\":\"Attack Speed\"}],\"text\":\"\"}']}}");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return cmdManager.commandManager(sender, cmd, label, args);
	}

	public void registerConfig() {

		File configFile = new File(getDataFolder(), "config.yml");
		if (!configFile.exists()) {
			getConfig().options().copyDefaults(true);
			saveConfig();
		}

		messages.register();
		ipsecurity.register();
	}

	public void setupORBlocks() {

		newORB("originrealms:red_crystal_lantern", 1001, "Red Crystal Lantern", "white",
				new ORCustomBlockData(Instrument.GUITAR, new Note(4), false));

		newORB("originrealms:green_crystal_lantern", 1002, "Green Crystal Lantern", "white",
				new ORCustomBlockData(Instrument.FLUTE, new Note(19), false));

		newORB("originrealms:fungal_planks", 1003, "Fungal Planks", "white",
				new ORCustomBlockData(Instrument.BASS_DRUM, new Note(13), false));

		newORB("originrealms:fungal_planks_black", 1004, "Black Fungal Planks", "white",
				new ORCustomBlockData(Instrument.BASS_DRUM, new Note(14), false));
		newORB("originrealms:fungal_planks_blue", 1005, "Blue Fungal Planks", "white",
				new ORCustomBlockData(Instrument.BASS_DRUM, new Note(15), false));
		newORB("originrealms:fungal_planks_brown", 1006, "Brown Fungal Planks", "white",
				new ORCustomBlockData(Instrument.BASS_DRUM, new Note(16), false));
		newORB("originrealms:fungal_planks_cyan", 1007, "Cyan Fungal Planks", "white",
				new ORCustomBlockData(Instrument.BASS_DRUM, new Note(17), false));
		newORB("originrealms:fungal_planks_gray", 1008, "Gray Fungal Planks", "white",
				new ORCustomBlockData(Instrument.BASS_DRUM, new Note(18), false));
		newORB("originrealms:fungal_planks_green", 1009, "Green Fungal Planks", "white",
				new ORCustomBlockData(Instrument.BASS_DRUM, new Note(19), false));
		newORB("originrealms:fungal_planks_light_blue", 1010, "Light Blue Fungal Planks", "white",
				new ORCustomBlockData(Instrument.BASS_DRUM, new Note(20), false));
		newORB("originrealms:fungal_planks_light_gray", 1011, "Light Gray Fungal Planks", "white",
				new ORCustomBlockData(Instrument.BASS_DRUM, new Note(21), false));
		newORB("originrealms:fungal_planks_lime", 1012, "Lime Fungal Planks", "white",
				new ORCustomBlockData(Instrument.BASS_DRUM, new Note(22), false));
		newORB("originrealms:fungal_planks_magenta", 1013, "Magenta Fungal Planks", "white",
				new ORCustomBlockData(Instrument.BASS_DRUM, new Note(23), false));
		newORB("originrealms:fungal_planks_orange", 1014, "Orange Fungal Planks", "white",
				new ORCustomBlockData(Instrument.BASS_DRUM, new Note(24), false));
		newORB("originrealms:fungal_planks_pink", 1015, "Pink Fungal Planks", "white",
				new ORCustomBlockData(Instrument.BASS_GUITAR, new Note(1), false));
		newORB("originrealms:fungal_planks_purple", 1016, "Purple Fungal Planks", "white",
				new ORCustomBlockData(Instrument.BASS_GUITAR, new Note(2), false));
		newORB("originrealms:fungal_planks_red", 1017, "Red Fungal Planks", "white",
				new ORCustomBlockData(Instrument.BASS_GUITAR, new Note(3), false));
		newORB("originrealms:fungal_planks_white", 1018, "White Fungal Planks", "white",
				new ORCustomBlockData(Instrument.BASS_GUITAR, new Note(4), false));
		newORB("originrealms:fungal_planks_yellow", 1019, "Yellow Fungal Planks", "white",
				new ORCustomBlockData(Instrument.BASS_GUITAR, new Note(5), false));
		newORB("originrealms:fungal_planks_carved", 1020, "Carved Fungal Planks", "white",
				new ORCustomBlockData(Instrument.GUITAR, new Note(24), false));
		newORB("originrealms:vertical_fungal_planks", 1021, "Vertical Fungal Planks", "white",
				new ORCustomBlockData(Instrument.PIANO, new Note(1), false));

		newORB("originrealms:crate_1", 1022, "Crate 1", "white",
				new ORCustomBlockData(Instrument.BANJO, new Note(1), false));
		newORB("originrealms:crate_3", 1023, "Crate 2", "white",
				new ORCustomBlockData(Instrument.BANJO, new Note(2), false));

		newORB("originrealms:limestone", 1024, "Limestone", "white",
				new ORCustomBlockData(Instrument.BIT, new Note(21), false));

		newORB("originrealms:limestone_bricks", 1025, "Limestone Bricks", "white",
				new ORCustomBlockData(Instrument.BIT, new Note(19), false));

		newORB("originrealms:slate", 1026, "Slate", "white",
				new ORCustomBlockData(Instrument.BELL, new Note(11), false));
		newORB("originrealms:chiseled_slate", 1027, "Chiseled Slate", "white",
				new ORCustomBlockData(Instrument.BIT, new Note(17), false));

		newORB("originrealms:ruby_brick", 1028, "Ruby Brick", "white",
				new ORCustomBlockData(Instrument.COW_BELL, new Note(3), false));

		newORB("originrealms:vertical_oak_planks", 1029, "Vertical Oak Planks", "white",
				new ORCustomBlockData(Instrument.BANJO, new Note(19), false));
		newORBt("originrealms:test", 1030, "Vertical Oak Planks", "white",
				new ORCustomBlockData(Instrument.BANJO, new Note(19), false));

		newORB("originrealms:dark_oak_paper_lantern", 1031, "Dark Oak Paper Lantern", "white",
				new ORCustomBlockData(Instrument.CHIME, new Note(3), false));

		newORB("originrealms:chiseled_andesite", 1032, "Chiseled Andesite", "white",
				new ORCustomBlockData(Instrument.CHIME, new Note(8), false));
		newORB("originrealms:andesite_bricks", 1033, "Andesite Bricks", "white",
				new ORCustomBlockData(Instrument.CHIME, new Note(9), false));

		newORB("originrealms:mangrove_paper_lantern", 1034, "Mangrove Paper Lantern", "white",
				new ORCustomBlockData(Instrument.PIANO, new Note(11), false));

	}

	public void newORB(String id, int modelData, String name, String color, ORCustomBlockData data) {
		cimanager.addCustomItem(id, modelData, Material.BARRIER, name, color);
		ItemStack test = cimanager.getItemById(id).get();
		if (test == null)
			return;
		cbmanager.addCustomBlock(new ORCustomBlock(id, test, test, true, true, 0, data));
	}

	public void newORBt(String id, int modelData, String name, String color, ORCustomBlockData data) {
		cimanager.addCustomItem(id, modelData, Material.BARRIER, name, color);
		ItemStack test = cimanager.getItemById(id).get();
		if (test == null)
			return;
		cbmanager.addCustomBlock(new ORCustomBlockMultiFace(id, test, test, true, true, 0, data));
	}

}
