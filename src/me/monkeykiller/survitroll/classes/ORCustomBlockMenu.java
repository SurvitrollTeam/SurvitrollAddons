package me.monkeykiller.survitroll.classes;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.monkeykiller.survitroll.Main;
import net.md_5.bungee.api.ChatColor;

public class ORCustomBlockMenu {

	Main plugin;

	public ORCustomBlockMenu(Main plugin) {
		this.plugin = plugin;
	}

	public void open(Player player) {
		List<String> citems = plugin.cimanager.getIdList().stream().filter(id -> id.startsWith("originrealms:"))
				.collect(Collectors.toList());
		Inventory inv = Bukkit.getServer().createInventory(null, 54, ChatColor.of("#37bbf5") + "" + ChatColor.BOLD + "Origin Realms " + ChatColor.GOLD + "" + ChatColor.BOLD + "Blocks");
		for (int i = 0; i < citems.size(); i++)
			inv.setItem(i, plugin.cimanager.getItemById(citems.get(i)).get());
		player.openInventory(inv);
	}

}
