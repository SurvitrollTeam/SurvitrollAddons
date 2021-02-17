package me.monkeykiller.survitroll;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;

import me.monkeykiller.survitroll.classes.CustomItem;
import me.monkeykiller.survitroll.classes.ORCustomBlockMenu;
import me.monkeykiller.survitroll.classes.custom_entities.CustomEntitiesManager;
import me.monkeykiller.survitroll.classes.custom_entities.CustomEntity;

public class CommandManager implements TabCompleter {
	private Main plugin;
	private Methods m;
	private FileConfiguration messages;

	public CommandManager(Main plugin) {
		this.plugin = plugin;
		this.m = plugin.methods;
		this.messages = plugin.messages.get();
	}

	@SuppressWarnings("static-access")
	public boolean commandManager(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("fly")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(m.color(messages.getString("error-prefix") + messages.getString("non-player")));
				return false;
			}
			Player player = (Player) sender;
			if (!checkPermission(player, "survitroll.commands.fly"))
				return false;
			if (args.length == 0) {
				player.setAllowFlight(!player.getAllowFlight());
				player.sendMessage(
						m.color(messages.getString("fly.prefix") + String.format(messages.getString("fly.fly-self"),
								player.getAllowFlight() ? messages.getString("fly.fly-enabled")
										: messages.getString("fly.fly-disabled"))));
				return true;
			} else if (args.length == 1) {
				Player target = Bukkit.getPlayer(args[0]);
				if (target == null) {
					player.sendMessage(m.color(messages.getString("error-prefix")
							+ String.format(messages.getString("user-not-found"), args[0])));
					return false;
				}
				target.setAllowFlight(!target.getAllowFlight());
				player.sendMessage(
						m.color(messages.getString("fly.prefix") + String.format(messages.getString("fly.fly-others"),
								target.getName(), target.getAllowFlight() ? messages.getString("fly.fly-enabled")
										: messages.getString("fly.fly-disabled"))));
				return true;
			}

		} else if (label.equalsIgnoreCase("survitroll")) {
			if (!checkPermission(sender, "survitroll.command"))
				return false;
			if (args[0].equalsIgnoreCase("reload")) {
				plugin.reloadConfig();
				plugin.messages.reload();
				plugin.ipsecurity.reload();
				messages = plugin.messages.get();
				sender.sendMessage(m.color(messages.getString("prefix") + messages.getString("reload-config")));
				return true;
			} else if (args[0].equalsIgnoreCase("del")) {
				plugin.messages.reinstall();
				plugin.ipsecurity.reinstall();
				new File(plugin.getDataFolder(), "config.yml").delete();
				plugin.registerConfig();

				sender.sendMessage(m.color("&aConfig reinstalled!"));
				return true;
			}
			return false;
		} else if (label.equalsIgnoreCase("itemhax")) {
			if (!checkPermission(sender, "survitroll.commands.give"))
				return false;
			if (args.length < 2) {
				// itemhax <player> survitroll:infernal_netherite_block 64
				sender.sendMessage(m.color("&cUsage: /itemhax <player> <item> [count]"));
				return false;
			}
			Player target = Bukkit.getPlayerExact(args[0]);
			if (target == null) {
				sender.sendMessage(m.color(String.format(messages.getString("user-not-found"), args[0])));
				return false;
			}

			CustomItem item = plugin.cimanager.getItemById(args[1]);
			if (item == null) {
				sender.sendMessage(m.color(messages.getString("error-prefix")
						+ String.format(messages.getString("give.unknown-item"), args[1])));
				return false;
			}

			int count = 1;
			if (args.length > 2) {
				try {
					count = Integer.parseInt(args[2]);
				} catch (NumberFormatException n) {
					sender.sendMessage(m.color(messages.getString("error-prefix")
							+ String.format(messages.getString("give.invalid-count"), "invalid_number")));
					return false;
				}
			}
			if (count < 1) {
				sender.sendMessage(m.color(messages.getString("error-prefix")
						+ String.format(messages.getString("give.invalid-count"), count)));
				return false;
			}
			ItemStack i = item.get();
			i.setAmount(count);
			target.getInventory().addItem(i);
			Team team = target.getScoreboard().getTeam(target.getName());
			sender.sendMessage(m.color(String.format(messages.getString("give.given-item"),
					team.getPrefix() + team.getColor() + target.getName() + team.getSuffix(), count,
					item.getItemId())));
			return true;

		} else if (label.equalsIgnoreCase("ip")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(m.color(messages.getString("error-prefix") + messages.getString("non-player")));
				return false;
			}
			if (!checkPermission(sender, "survitroll.commands.ip"))
				return false;
			if (args.length == 0) {
				String tmp = new String();
				for (String s : messages.getStringList("ipsecurity.help"))
					tmp += s + "\n";

				sender.sendMessage(m.color(tmp));
				return false;
			} else if (args.length > 0) {
				if (args[0].equalsIgnoreCase("add")) {
					if (args.length < 2) {
						String tmp = new String();
						for (String s : messages.getStringList("ipsecurity.help"))
							tmp += s + "\n";

						sender.sendMessage(m.color(tmp));
						return false;
					}

					String ip = args[1];
					String playerUUID = ((Player) sender).getUniqueId().toString();
					if (!m.validateIp(ip)) {
						sender.sendMessage(m.color(String.format(
								messages.getString("error-prefix") + messages.getString("ipsecurity.invalid-ip"), ip)));
						return false;
					}
					FileConfiguration ipsecurity = plugin.ipsecurity.get();
					List<String> ips = ipsecurity.getStringList("users." + playerUUID);
					if (ips.contains(ip)) {
						sender.sendMessage(m.color(
								messages.getString("error-prefix") + messages.getString("ipsecurity.existent-ip")));
						return false;
					}
					ips.add(ip);
					ipsecurity.set("users." + playerUUID, ips);
					plugin.ipsecurity.save();
					sender.sendMessage(m.color(messages.getString("ipsecurity.prefix")
							+ String.format(messages.getString("ipsecurity.new-ip"), ip)));
					return true;
				} else if (args[0].equalsIgnoreCase("info")) {
					FileConfiguration ipsecurity = plugin.ipsecurity.get();
					String tmp = new String();
					for (String ip : ipsecurity.getStringList("users." + ((Player) sender).getUniqueId()))
						tmp += " -" + ip + "\n";
					sender.sendMessage(m.color("&eIp List: \n" + tmp));
				}
			}
		} else if (label.equalsIgnoreCase("mobhax")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(m.color(messages.getString("error-prefix") + messages.getString("non-player")));
				return false;
			}
			if (!checkPermission(sender, "survitroll.commands.summon"))
				return false;
			if (args.length < 1) {
				sender.sendMessage(m.color("&cUsage: /mobhax <mob>"));
				return false;
			}
			CustomEntity CE = CustomEntitiesManager.getCustomEntitybyId(args[0]);
			if (CE == null) {
				sender.sendMessage("Mob " + args[0] + " does not exist!");
				return false;
			}

			CE.spawn(((Player) sender).getLocation());
			sender.sendMessage("Sucessfully Spawned!");
			return true;
		} else if (label.equalsIgnoreCase("cblocks")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(m.color(messages.getString("error-prefix") + messages.getString("non-player")));
				return false;
			}
			if (!checkPermission(sender, "survitroll.commands.cblocks"))
				return false;
			new ORCustomBlockMenu(plugin).open((Player) sender);
			return true;
		}
		return false;
	}

	@SuppressWarnings("static-access")
	private boolean checkPermission(CommandSender sender, String permission) {
		if (!(sender instanceof Player))
			return true;
		if (!sender.hasPermission(permission) && !((Player) sender).isOp()) {
			sender.sendMessage(m.color(messages.getString("error-prefix") + messages.getString("no-permission")));
			return false;
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("itemhax")) {
			if (args.length == 1)
				return null;

			else if (args.length == 2) {
				if (args[1].equals(""))
					return plugin.cimanager.getIdList();
				else
					return plugin.cimanager.getIdList().stream()
							.filter(id -> id.startsWith(args[1].toLowerCase()) || id.contains(args[1].toLowerCase()))
							.collect(Collectors.toList());
			} else
				return new ArrayList<>();

		} else if (label.equalsIgnoreCase("mobhax")) {
			if (args.length == 1) {
				if (args[0].equals(""))
					return CustomEntitiesManager.getIdList();
				else
					return CustomEntitiesManager.getIdList().stream()
							.filter(id -> id.startsWith(args[0].toLowerCase()) || id.contains(args[0].toLowerCase()))
							.collect(Collectors.toList());
			} else
				return new ArrayList<>();

		}
		return null;
	}

}
