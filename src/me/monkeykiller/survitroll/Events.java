package me.monkeykiller.survitroll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Illusioner;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Rabbit.Type;
import org.bukkit.entity.Shulker;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.NotePlayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scoreboard.Team;

import de.tr7zw.nbtapi.NBTContainer;
import de.tr7zw.nbtapi.NBTItem;
import me.monkeykiller.survitroll.classes.CustomBlock;
import me.monkeykiller.survitroll.classes.CustomBlockManager;
import me.monkeykiller.survitroll.classes.ORCustomBlock;
import me.monkeykiller.survitroll.classes.ORCustomBlockMultiFace;
import me.monkeykiller.survitroll.classes.WrenchUtils;
import me.monkeykiller.survitroll.classes.custom_entities.CustomEntitiesManager;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.EntityHuman;
import net.minecraft.server.v1_16_R3.EnumHand;
import net.minecraft.server.v1_16_R3.ItemActionContext;
import net.minecraft.server.v1_16_R3.MovingObjectPositionBlock;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.TileEntityMobSpawner;
import net.minecraft.server.v1_16_R3.Vec3D;

@SuppressWarnings("static-access")
public class Events implements Listener {
	private Main plugin;
	private Methods m;

	private Set<UUID> ChorusPearls = new HashSet<>();
	private Set<UUID> Switchers = new HashSet<>();

	private HashMap<UUID, Long> chorusCooldown = new HashMap<>();
	private HashMap<UUID, Long> switcherCooldown = new HashMap<>();
	private HashMap<UUID, Long> mirrorCooldown = new HashMap<>();

	private List<EntityType> allowedEntities = Arrays
			.asList(new EntityType[] { EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER, EntityType.SKELETON,
					EntityType.WITHER_SKELETON, EntityType.HUSK, EntityType.STRAY, EntityType.PIGLIN });

	public Events(Main plugin) {
		this.plugin = plugin;
		this.m = plugin.methods;
	}

	@EventHandler
	public void onProyectileLaunch(ProjectileLaunchEvent event) {
		Entity entity = event.getEntity();
		if (!(entity instanceof Snowball))
			return;
		Snowball ball = (Snowball) entity;
		ProjectileSource shooter = ball.getShooter();
		if (!(shooter instanceof Player))
			return;
		Player player = (Player) ball.getShooter();

		checkChorus(event, player, ball);
		checkSwitcher(event, player, ball);

	}

	@EventHandler
	public void onEntityDamagebyEntity(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Snowball))
			return;

		Snowball ball = (Snowball) event.getDamager();

		if (ChorusPearls.contains(ball.getUniqueId())) {
			Entity entity = event.getEntity();

			entity.getWorld().spawnParticle(Particle.PORTAL, entity.getLocation(), 100, 0.3D, 0.5D, 0.3D);
			entity.getWorld().playSound(entity.getLocation(), Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1, 1);
			Location spreadLocation = m.spread(entity.getLocation(), 8);
			entity.teleport(spreadLocation);
			ChorusPearls.remove(ball.getUniqueId());
		} else if (Switchers.contains(ball.getUniqueId())) {
			Entity shooter = (Entity) ball.getShooter();
			Entity entity = event.getEntity();
			entity.getWorld().spawnParticle(Particle.PORTAL, entity.getLocation(), 100, 0.3D, 0.5D, 0.3D);
			entity.getWorld().playSound(entity.getLocation(), Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1, 1);
			entity.getWorld().spawnParticle(Particle.PORTAL, shooter.getLocation(), 100, 0.3D, 0.5D, 0.3D);

			Location loc1 = shooter.getLocation().clone();
			Location loc2 = entity.getLocation().clone();
			shooter.teleport(loc2);
			entity.teleport(loc1);
			Switchers.remove(ball.getUniqueId());
		}

	}

	/*
	 * @EventHandler public void onInventoryClick(InventoryClickEvent event) {
	 * plugin.twmanager.getTradeById(new
	 * NBTItem(event.getClickedInventory().getItem(53)).getString("TradeWindowId"))
	 * .onClick(event); }
	 */

	@EventHandler
	public void onShulkerAttack(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof ShulkerBullet && event.getEntity() instanceof Shulker))
			return;
		if (new Random().nextInt(80) != 0)
			return;
		Location loc = event.getEntity().getLocation();
		loc.add(0, 1, 0);
		loc.getWorld().spawnEntity(loc, EntityType.SHULKER);
	}

	@EventHandler
	public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
		if (event.getItem() == null || event.getItem().getType() != Material.CHORUS_FRUIT)
			return;
		String itemId = "survitroll:golden_chorus_fruit";
		NBTItem nbtItem = new NBTItem(event.getItem());

		if (nbtItem.getString("ItemId").equalsIgnoreCase(itemId)) {
			if (event.getPlayer().getWorld().getEnvironment().equals(World.Environment.THE_END))
				return;
			event.getPlayer().teleport(Bukkit.getServer().getWorld("world_the_end").getSpawnLocation());

		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		NBTItem itemNBT = new NBTItem(event.getItemInHand());
		CustomBlockManager cbmanager = plugin.cbmanager;
		CustomBlock cblock = cbmanager.getBlockById(itemNBT.getString("ItemId"));
		if (cblock == null)
			return;
		if (cblock instanceof ORCustomBlockMultiFace) {
			List<BlockFace> bfList = Arrays.asList(BlockFace.UP, BlockFace.DOWN, BlockFace.EAST, BlockFace.WEST,
					BlockFace.NORTH, BlockFace.SOUTH);
			BlockFace face = BlockFace.UP;
			for (BlockFace bf : bfList)
				if (event.getBlock().getRelative(face).getLocation().equals(event.getBlockAgainst().getLocation()))
					face = bf;

			((ORCustomBlockMultiFace) cblock).place(event.getPlayer(), event.getBlock().getLocation(), face);
		} else
			cblock.place(event.getBlock().getLocation());
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		if (block.getType() != Material.SPAWNER || event.getPlayer().getGameMode() == GameMode.CREATIVE)
			return;

		BlockPosition blockpos = new BlockPosition(block.getX(), block.getY(), block.getZ());
		TileEntityMobSpawner spawner = (TileEntityMobSpawner) ((CraftWorld) block.getWorld()).getHandle()
				.getTileEntity(blockpos);

		NBTTagCompound tileData = spawner.b();
		// 0:"END", 1:"BYTE", 2:"SHORT", 3:"INT", 4:"LONG", 5:"FLOAT", 6:"DOUBLE",
		// 7:"BYTE[]", 8:"STRING", 9:"LIST", 10:"COMPOUND", 11:"INT[]"
		NBTTagCompound SpawnData = tileData.getCompound("SpawnData");
		NBTTagCompound armor = SpawnData.getList("ArmorItems", 10).getCompound(3);

		int fortuneLevel = 0;
		boolean hasSilkTouch = false;
		ArrayList<Material> pickable = new ArrayList<Material>();
		pickable.add(Material.STONE_PICKAXE);
		pickable.add(Material.GOLDEN_PICKAXE);
		pickable.add(Material.IRON_PICKAXE);
		pickable.add(Material.DIAMOND_PICKAXE);
		pickable.add(Material.NETHERITE_PICKAXE);
		if (event.getPlayer().getInventory().getItemInMainHand() == null
				|| !pickable.contains(event.getPlayer().getInventory().getItemInMainHand().getType()))
			return;

		if (event.getPlayer().getInventory().getItemInMainHand() != null)
			fortuneLevel = event.getPlayer().getInventory().getItemInMainHand()
					.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
		if (event.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH))
			hasSilkTouch = true;

		if (armor == null)
			return;
		if (armor.getString("id").equalsIgnoreCase("minecraft:barrier")
				&& plugin.cbmanager.getBlockById(armor.getCompound("tag").getString("ItemId")) != null) {
			plugin.cbmanager.getBlockById(armor.getCompound("tag").getString("ItemId")).mine(event, fortuneLevel,
					hasSilkTouch);
		}

	}

	@EventHandler
	public void noUproot(PlayerInteractEvent event) {
		if (event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.FARMLAND)
			if (event.getPlayer().getInventory().getBoots().containsEnchantment(Enchantment.PROTECTION_FALL))
				event.setCancelled(true);
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if (event.getDeathMessage().contains("fell out of the world")
				&& event.getEntity().getWorld().getEnvironment() == World.Environment.THE_END) {
			event.setDeathMessage(getTeamDisplayname(event.getEntity(), ChatColor.RESET)
					+ " has made a TheDiamond_xd_ (fell out of the world)");
		} else if (event.getDeathMessage().contains("was slain by Phantom"))
			event.setDeathMessage(
					getTeamDisplayname(event.getEntity(), ChatColor.RESET) + " has lost the control of their reality");

	}

	public void checkChorus(ProjectileLaunchEvent event, Player player, Snowball ball) {
		String itemId = "survitroll:chorus_pearl";
		if (player.getInventory().getItemInOffHand() != null
				&& player.getInventory().getItemInOffHand().getType() != Material.AIR
				&& new NBTItem(player.getInventory().getItemInOffHand()).getString("ItemId").equalsIgnoreCase(itemId)
				|| (player.getInventory().getItemInMainHand() == null
						&& player.getInventory().getItemInMainHand().getType() == Material.AIR)) {
			player.sendMessage(ChatColor.RED + "La Chorus Pearl solo sirve si está en la mano principal!");
			event.setCancelled(true);
			return;
		}

		NBTItem mainHandItem = new NBTItem(player.getInventory().getItemInMainHand());

		if (mainHandItem.getString("ItemId").equalsIgnoreCase(itemId))
			if (!chorusCooldown.containsKey(player.getUniqueId())
					|| chorusCooldown.get(player.getUniqueId()) <= (System.currentTimeMillis() / 1000)) {
				if (chorusCooldown.containsKey(player.getUniqueId()))
					chorusCooldown.remove(player.getUniqueId());
				chorusCooldown.put(player.getUniqueId(), (System.currentTimeMillis() / 1000) + 5);
				ChorusPearls.add(ball.getUniqueId());
				return;
			} else {
				double time = (chorusCooldown.get(player.getUniqueId()) - (System.currentTimeMillis() / 1000));
				player.sendMessage(ChatColor.RED + "Debes esperar " + (int) time + " "
						+ (time != 1.0D ? "segundos" : "segundo") + " antes de volver a usar la Chorus Pearl!");
				event.setCancelled(true);
				return;
			}

	}

	public void checkSwitcher(ProjectileLaunchEvent event, Player player, Snowball ball) {
		String itemId = "survitroll:switcher";
		if (player.getInventory().getItemInOffHand() != null
				&& player.getInventory().getItemInOffHand().getType() != Material.AIR
				&& new NBTItem(player.getInventory().getItemInOffHand()).getString("ItemId").equalsIgnoreCase(itemId)
				|| (player.getInventory().getItemInMainHand() == null
						&& player.getInventory().getItemInMainHand().getType() == Material.AIR)) {
			player.sendMessage(ChatColor.RED + "El Switcher solo sirve si está en la mano principal!");
			event.setCancelled(true);
			return;
		}

		NBTItem mainHandItem = new NBTItem(player.getInventory().getItemInMainHand());

		if (mainHandItem.getString("ItemId").equalsIgnoreCase(itemId))
			if (!switcherCooldown.containsKey(player.getUniqueId())
					|| switcherCooldown.get(player.getUniqueId()) <= (System.currentTimeMillis() / 1000)) {
				if (switcherCooldown.containsKey(player.getUniqueId()))
					switcherCooldown.remove(player.getUniqueId());
				switcherCooldown.put(player.getUniqueId(), (System.currentTimeMillis() / 1000) + 12);
				Switchers.add(ball.getUniqueId());
				return;
			} else {
				double time = (switcherCooldown.get(player.getUniqueId()) - (System.currentTimeMillis() / 1000));
				player.sendMessage(ChatColor.RED + "Debes esperar " + time + " "
						+ (time != 1.0D ? "segundos" : "segundo") + " antes de volver a usar el Switcher!");
				event.setCancelled(true);
				return;
			}
	}

	/*
	 * @EventHandler public void onMobSpawn(CreatureSpawnEvent event) { if
	 * (event.getEntity() instanceof WanderingTrader) { LivingEntity a =
	 * (WanderingTrader) event.getEntity(); a.setInvulnerable(true);
	 * a.setCustomName("Pigman trader"); a.setCustomNameVisible(true);
	 * a.setInvisible(true); a.setAI(false); a.setSilent(true); ItemStack hat = new
	 * ItemStack(Material.DIAMOND_HOE); ItemMeta meta = (ItemMeta)
	 * hat.getItemMeta(); meta.setCustomModelData(1); hat.setItemMeta(meta);
	 * a.getEquipment().setHelmet(hat); a.getEquipment().setHelmetDropChance(0.0F);
	 * 
	 * List<MerchantRecipe> trades = new ArrayList<MerchantRecipe>(); MerchantRecipe
	 * aa = new MerchantRecipe(new ItemStack(Material.STONE), 20);
	 * aa.addIngredient(new ItemStack(Material.EMERALD));
	 * 
	 * trades.add(aa); ((WanderingTrader) event.getEntity()).setRecipes(trades); } }
	 */

	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (event.getEntityType() == EntityType.RABBIT) {
			if (new Random().nextInt(100) != 0) // 0,1% probability - 0-99
				return;
			Rabbit rabbit = (Rabbit) event.getEntity();
			rabbit.setRabbitType(Type.THE_KILLER_BUNNY);
		} else if (event.getEntityType() == EntityType.VINDICATOR) {
			if (new Random().nextInt(4) != 0) // 25% probability - 0-3
				return;
			Illusioner illusioner = (Illusioner) event.getEntity().getWorld()
					.spawnEntity(event.getEntity().getLocation(), EntityType.ILLUSIONER);
			Entity vehicle = null;
			if (event.getEntity().isInsideVehicle())
				vehicle = event.getEntity().getVehicle();
			event.getEntity().remove();
			if (vehicle != null)
				vehicle.addPassenger(illusioner);
		}
	}

	@EventHandler
	public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
		FileConfiguration messages = plugin.messages.get();
		FileConfiguration ipsecurity = plugin.ipsecurity.get();

		String userPath = "users." + event.getUniqueId().toString();
		String ip = event.getAddress().getHostAddress();
		if (Bukkit.getServer().hasWhitelist() && !Bukkit.getOfflinePlayer(event.getUniqueId()).isWhitelisted())
			return;
		if (ipsecurity.getStringList("disabled-users").contains(event.getUniqueId().toString())) {
			broadcastIpMsg(
					m.color(String.format(messages.getString("ipsecurity.info-disabled-protection"), event.getName())));
			return;
		}
		if (!ipsecurity.contains(userPath)) {
			String[] tmp = { ip };
			ipsecurity.set(userPath, tmp);
			plugin.ipsecurity.save();
			return;
		}
		if (!ipsecurity.getBoolean("enabled")) {
			Bukkit.getLogger().warning("Ip security off, please turn it on in ipconfig.yml");
			return;
		}
		if (!ipsecurity.getStringList(userPath).contains(ip)) {
			event.setLoginResult(Result.KICK_OTHER);
			String kickmsg = new String();
			for (String s : messages.getStringList("ipsecurity.kick-message"))
				kickmsg += m.color(s) + "\n";
			kickmsg = String.format(kickmsg, ip);
			event.setKickMessage(kickmsg);
			return;
		}
	}

	private String getTeamDisplayname(Player player, ChatColor color) {
		Team team = player.getScoreboard().getEntryTeam(player.getName());
		if (team == null)
			return player.getName();
		return team.getPrefix() + color + player.getName() + ChatColor.WHITE + team.getSuffix();
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		event.setJoinMessage(
				String.format(m.color("%s&e joined the game"), getTeamDisplayname(player, ChatColor.YELLOW)));
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		event.setQuitMessage(
				String.format(m.color("%s&e left the game"), getTeamDisplayname(player, ChatColor.YELLOW)));
	}

	@EventHandler
	public void onPlayerClick(PlayerInteractEvent event) {
		if (event.getHand() != EquipmentSlot.HAND
				|| (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR)
				|| event.getItem() == null)
			return;

		NBTItem itemNBT = new NBTItem(event.getItem());
		Player player = event.getPlayer();

		if (itemNBT.getString("ItemId").equalsIgnoreCase("survitroll:magic_mirror")) {
			event.setCancelled(true);
			if (player.getWorld().getEnvironment() != World.Environment.NORMAL) {
				player.sendMessage(m.color("&cDebes estar en el &6Overworld &cpara teletransportarte a tu spawnpoint"));
				return;
			}

			if (!mirrorCooldown.containsKey(player.getUniqueId())
					|| mirrorCooldown.get(player.getUniqueId()) <= (System.currentTimeMillis() / 1000)) {
				if (mirrorCooldown.containsKey(player.getUniqueId()))
					mirrorCooldown.remove(player.getUniqueId());
				mirrorCooldown.put(player.getUniqueId(), (System.currentTimeMillis() / 1000) + 60);

				if (m.damage(event.getItem().containsEnchantment(Enchantment.DURABILITY)
						? event.getItem().getEnchantmentLevel(Enchantment.DURABILITY)
						: 0) && player.getGameMode() == GameMode.SURVIVAL
						&& !event.getItem().getItemMeta().isUnbreakable()) {

					ItemMeta meta = event.getItem().getItemMeta();
					((Damageable) meta).setDamage(((Damageable) meta).getDamage() + 1);

					if (((Damageable) meta).getDamage() >= 25) {
						player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1F, 1F);
						player.spawnParticle(Particle.ITEM_CRACK, player.getLocation(), 1, event.getItem());
						player.getInventory().remove(event.getItem());
					} else {
						event.getItem().setItemMeta(meta);
					}
				}
				player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 99, 0.8D, 0.8D, 0.8D,
						new Particle.DustOptions(Color.fromRGB(68, 152, 157), 1));
				player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 99, 0.8D, 0.8D, 0.8D,
						new Particle.DustOptions(Color.fromRGB(255, 255, 255), 1));
				player.getWorld().playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 1, 0);
				player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 255, false, false, false));
				player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 255, false, false, false));
				player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, 245, false, false, false));
				Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
					@Override
					public void run() {
						player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 0);
						player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 99, 0.8D, 0.8D, 0.8D,
								new Particle.DustOptions(Color.fromRGB(68, 152, 157), 1));
						player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 99, 0.8D, 0.8D, 0.8D,
								new Particle.DustOptions(Color.fromRGB(255, 255, 255), 1));

						Location spawn = player.getBedSpawnLocation();
						if (spawn == null || !spawn.getWorld().equals(player.getWorld()))
							spawn = player.getWorld().getSpawnLocation();

						if (player.isInsideVehicle()) {
							Entity vehicle = player.getVehicle();
							List<Entity> passengers = player.getVehicle().getPassengers();
							vehicle.eject();
							vehicle.teleport(spawn);
							for (Entity p : passengers) {
								p.teleport(vehicle);
								vehicle.addPassenger(p);
							}
						} else
							player.teleport(spawn);

						player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 99, 0.8D, 0.8D, 0.8D,
								new Particle.DustOptions(Color.fromRGB(68, 152, 157), 1));
						player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 99, 0.8D, 0.8D, 0.8D,
								new Particle.DustOptions(Color.fromRGB(255, 255, 255), 1));
					}
				}, 35L);

				return;
			} else {
				int time = (int) (mirrorCooldown.get(player.getUniqueId()) - (System.currentTimeMillis() / 1000));
				player.sendMessage(ChatColor.RED + "Debes esperar " + time + " " + (time != 1 ? "segundos" : "segundo")
						+ " antes de volver a usar el Magic Mirror!");
				event.setCancelled(true);
				return;
			}
		}
		if (itemNBT.getString("ItemId").equalsIgnoreCase("survitroll:copper_wrench")) {
			if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
				return;

			Block block = event.getClickedBlock();
			if (!WrenchUtils.isRotatable(block, player))
				return;
			if (m.damage(event.getItem().containsEnchantment(Enchantment.DURABILITY)
					? event.getItem().getEnchantmentLevel(Enchantment.DURABILITY) + 2
					: 2) && player.getGameMode() == GameMode.SURVIVAL
					&& !event.getItem().getItemMeta().isUnbreakable()) {

				ItemMeta meta = event.getItem().getItemMeta();
				((Damageable) meta).setDamage(((Damageable) meta).getDamage() + 1);

				if (((Damageable) meta).getDamage() >= 25) {
					player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1F, 1F);
					player.spawnParticle(Particle.ITEM_CRACK, player.getLocation(), 1, event.getItem());
					player.getInventory().remove(event.getItem());
				} else {
					event.getItem().setItemMeta(meta);
				}
			}

			BlockState state = block.getState();
			state.setBlockData(WrenchUtils.handleRotation(block));
			event.setCancelled(true);
			state.update(true, true);
			block.getWorld().playSound(block.getLocation(), Sound.ENTITY_FISHING_BOBBER_RETRIEVE, 1.0F, 0.4F);
		}
		/*
		 * if (itemNBT.getString("ItemId").equalsIgnoreCase("survitroll:copper_ingot"))
		 * { EntityPlayer ep = ((CraftPlayer) player).getHandle();
		 * PacketPlayOutEntityStatus status = new PacketPlayOutEntityStatus(ep, (byte)
		 * 35); ep.playerConnection.sendPacket(status); }
		 */
		if (itemNBT.getString("ItemId").equalsIgnoreCase("survitroll:experience_book")) {
			event.setCancelled(true);
			int levels = itemNBT.getInteger("Levels");
			if (levels <= 0) {
				if (player.getLevel() <= 0) {
					player.sendMessage(m.color("&cNecesitas tener al menos 1 nivel para depositar!"));
					return;
				}

				itemNBT.mergeCompound(new NBTContainer(plugin.getXPBookMeta(player.getLevel())));
				player.sendMessage(
						m.color(String.format("&aHas depositado &7%s &aniveles en el libro.", player.getLevel())));
				player.setLevel(0);
				ItemMeta meta = itemNBT.getItem().getItemMeta();
				meta.addEnchant(Enchantment.ARROW_FIRE, 1, true);
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				event.getItem().setItemMeta(meta);
			}
			if (levels > 0) {
				if (!event.getItem().containsEnchantment(Enchantment.ARROW_FIRE)) {
					player.sendMessage(
							"&c&lEl bug de experiencia infinita por grindstone ha sido parcheado, tu libro ha quedado inutilizable por cheater");
					return;
				}
				ExperienceOrb xp = (ExperienceOrb) player.getWorld().spawnEntity(player.getLocation(),
						EntityType.EXPERIENCE_ORB);
				xp.setExperience(getXPForLevel(levels) + 1);

				player.sendMessage(m.color(String.format("&aHas retirado &7%s &aniveles del libro.", levels)));
				itemNBT.mergeCompound(new NBTContainer(plugin.getXPBookMeta(0)));

				itemNBT.getCompound("display").removeKey("Lore");
				ItemMeta meta = itemNBT.getItem().getItemMeta();
				meta.removeEnchant(Enchantment.ARROW_FIRE);
				meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
				event.getItem().setItemMeta(meta);
			}
			return;

		}

	}

	public static int getXPForLevel(int lvl) {
		if (lvl <= 16) {
			return (int) Math.pow(lvl, 2) + 6 * lvl;
		} else if (lvl > 16 && lvl < 32) {
			return (int) (2.5 * Math.pow(lvl, 2) - 40.5 * lvl + 360);
		} else if (lvl > 31) {
			return (int) (4.5 * Math.pow(lvl, 2) - 162.5 * lvl + 2220);
		}
		return 0;
	}

	public void broadcastIpMsg(String text) {
		plugin.getServer().getConsoleSender().sendMessage(text);
		for (Player p : plugin.getServer().getOnlinePlayers())
			if (checkPermission(p, "survitroll.ipsecurity.info"))
				p.sendMessage(text);
	}

	private boolean checkPermission(Player player, String permission) {
		if (!player.hasPermission(permission) && !player.isOp())
			return false;
		return true;
	}

	@EventHandler
	public void onCreatureSpawnInChristmasWeek(CreatureSpawnEvent event) {
		if (Calendar.getInstance(TimeZone.getTimeZone("GMT-3")).get(Calendar.MONTH) < 11)
			return;
		int day = Calendar.getInstance(TimeZone.getTimeZone("GMT-3")).get(Calendar.DAY_OF_MONTH);
		if (!(day >= 22 && day <= 26))
			return;
		if (!allowedEntities.contains(event.getEntityType()))
			return;
		if (new Random().nextInt(4) != 0)
			return;
		event.getEntity().getEquipment().setHelmet(plugin.cimanager.getItemById("survitroll:christmas_hat").get());
		event.getEntity().getEquipment().setHelmetDropChance(0.015f);
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if (event.getEntityType().equals(EntityType.ILLUSIONER)) {
			if (new Random().nextInt(20) != 0)
				return;
			ItemStack mirror = plugin.cimanager.getItemById("survitroll:magic_mirror").get();
			ItemMeta meta = mirror.getItemMeta();
			((Damageable) meta).setDamage(new Random().nextInt(26) + 1);
			mirror.setItemMeta(meta);
			event.getDrops().add(mirror);
		}
	}

	@EventHandler
	public void onEndermanSpawn(CreatureSpawnEvent event) {
		if (!event.getEntityType().equals(EntityType.ENDERMAN)
				|| !event.getEntity().getWorld().getEnvironment().equals(World.Environment.THE_END))
			return;
		Location loc = event.getEntity().getLocation().clone();
		event.setCancelled(true);

		if (new Random().nextInt(5) == 0)
			/*
			 * if (new Random().nextInt(100) >= 2) { // 2% DWSGenerator(loc); } else {
			 * DWSGenerator(loc); }
			 */
			CustomEntitiesManager.getCustomEntitybyId("survitroll:demonic_wither_skeleton").spawn(loc);
		else if (new Random().nextInt(20) == 0) {
			loc.setY(loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()) + 10);
			CustomEntitiesManager.getCustomEntitybyId("survitroll:ender_phantom").spawn(loc);
		} else
			event.setCancelled(false);
	}

	@EventHandler
	public void DemonicWitherSkeletonDeath(EntityDeathEvent event) {
		if (!event.getEntityType().equals(EntityType.WITHER_SKELETON)
				|| !ChatColor.stripColor(event.getEntity().getCustomName()).equalsIgnoreCase("Demonic Wither Skeleton"))
			return;
		event.getDrops().clear();
		ItemStack scrap = plugin.cimanager.getItemById("survitroll:infernal_netherite_scrap").get();
		scrap.setAmount(new Random().nextInt(3));

		if (new Random().nextInt(3) == 0)
			event.getDrops().add(scrap);
		event.getDrops().add(new ItemStack(Material.BONE, new Random().nextInt(6)));

	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockPhysics(BlockPhysicsEvent event) {
		Block aboveBlock = event.getBlock().getLocation().clone().add(0, 1, 0).getBlock();
		if (aboveBlock.getType() == Material.NOTE_BLOCK) {
			updateAndCheck(event.getBlock().getLocation());
			event.setCancelled(true);
			// aboveBlock.getState().update(true, true);
		}
		if (event.getBlock().getType() == Material.NOTE_BLOCK)
			event.setCancelled(true);
		if (event.getBlock().getType().toString().toLowerCase().contains("sign"))
			return;
		event.getBlock().getState().update(true, false);

	}

	@EventHandler
	public void onPistonExtends(BlockPistonExtendEvent event) {
		for (Block b : event.getBlocks())
			if (b.getType().equals(Material.NOTE_BLOCK))
				event.setCancelled(true);

	}

	@EventHandler
	public void onPistonRestract(BlockPistonRetractEvent event) {
		for (Block b : event.getBlocks())
			if (b.getType().equals(Material.NOTE_BLOCK))
				event.setCancelled(true);

	}

	@EventHandler
	public void onNotePlay(NotePlayEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerInteractBlock(PlayerInteractEvent event) {

		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
				&& event.getClickedBlock().getType() == Material.NOTE_BLOCK)
			event.setCancelled(true);

		PlayerInventory inv = event.getPlayer().getInventory();
		ItemStack item = inv.getItemInMainHand().getType().isBlock()
				&& inv.getItemInMainHand().getType() != Material.AIR
						? inv.getItemInMainHand()
						: inv.getItemInOffHand().getType().isBlock() && inv.getItemInOffHand().getType() != Material.AIR
								? inv.getItemInOffHand()
								: null;

		if (!event.getPlayer().isSneaking() && item != null && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
				&& event.getClickedBlock().getType() == Material.NOTE_BLOCK) {
			// event.setCancelled(false);
			NBTItem itemNBT = new NBTItem(item);
			CustomBlockManager cbmanager = plugin.cbmanager;
			CustomBlock cblock = cbmanager.getBlockById(itemNBT.getString("ItemId"));
			Block block = event.getClickedBlock().getRelative(event.getBlockFace());
			if (cblock != null) {
				if (cblock instanceof ORCustomBlockMultiFace)
					((ORCustomBlockMultiFace) cblock).place(event.getPlayer(), block.getLocation(),
							event.getBlockFace());
				else
					cblock.place(block.getLocation());
			} else {
				// block.setType(item.getType());
				// if (item.getType().toString().toLowerCase().contains("sign"))
				// return;
				EntityHuman human = (EntityHuman) ((CraftPlayer) event.getPlayer()).getHandle();
				EnumHand enumHand = inv.getItemInMainHand().equals(item) ? EnumHand.MAIN_HAND
						: inv.getItemInOffHand().equals(item) ? EnumHand.OFF_HAND : null;

				Location eyeLoc = event.getPlayer().getEyeLocation();
				CraftItemStack.asNMSCopy(item)
						.placeItem(new ItemActionContext(human, enumHand,
								new MovingObjectPositionBlock(new Vec3D(eyeLoc.getX(), eyeLoc.getY(), eyeLoc.getZ()),
										human.getDirection(),
										new BlockPosition(block.getX(), block.getY(), block.getZ()), false)),
								enumHand);

			}
			if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
				if (item.getAmount() <= 1)
					event.getPlayer().getInventory().removeItem(item);
				else
					item.setAmount(item.getAmount() - 1);
			}

		}

		if (event.getPlayer().isSneaking() && item != null && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
				&& event.getClickedBlock().getType() == Material.NOTE_BLOCK) {
			event.setCancelled(false);
			// EntityPlayer ep = ((CraftPlayer) event.getPlayer()).getHandle();
		}
	}

	/*
	 * @EventHandler public void onPlayerInteractWithBarrier(PlayerInteractEvent
	 * event) { if (!event.getClickedBlock().getType().equals(Material.BARRIER) ||
	 * !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
	 * plugin.twmanager.getTradeById("survitroll:test").openWindow(event.getPlayer()
	 * ); }
	 */

	/*
	 * @EventHandler public void onBlockBreakOR(BlockBreakEvent event) { Block b =
	 * event.getBlock().getLocation().clone().add(0, 1, 0).getBlock(); if
	 * (b.getType() == Material.NOTE_BLOCK) { event.setCancelled(true); NoteBlock
	 * data = (NoteBlock) b.getBlockData().clone(); event.setCancelled(false);
	 * event.getBlock().setType(Material.AIR); b.setBlockData(data);
	 * updateAndCheck(b.getLocation()); } }
	 * 
	 * @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	 * public void onBlockPlaceOR(BlockPlaceEvent event) { Block b =
	 * event.getBlock().getLocation().clone().add(0, 1, 0).getBlock(); if
	 * (b.getType() == Material.NOTE_BLOCK) { event.setCancelled(true); NoteBlock
	 * data = (NoteBlock) b.getBlockData().clone(); event.setCancelled(false);
	 * updateAndCheck(event.getBlock().getLocation());
	 * 
	 * new BukkitRunnable() {
	 * 
	 * @Override public void run() { b.setBlockData(data); } }.runTaskLater(plugin,
	 * 0L);
	 * 
	 * } }
	 */
	public void updateAndCheck(Location loc) {
		Block b = loc.clone().add(0, 1, 0).getBlock();
		if (b.getType() == Material.NOTE_BLOCK) {
			// NoteBlock data = (NoteBlock) b.getBlockData().clone();

			// b.setBlockData(data);
			b.getState().update(true, true);

		}
		Location nextBlock = b.getLocation().clone().add(0, 1, 0);
		if (nextBlock.getBlock().getType() == Material.NOTE_BLOCK)
			updateAndCheck(b.getLocation().clone());
	}

	@EventHandler
	public void customBlockBreakOR(BlockBreakEvent event) {
		if (event.getBlock().getType() != Material.NOTE_BLOCK
				|| event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
			return;
		int fortuneLevel = 0;
		boolean hasSilkTouch = false;

		if (event.getPlayer().getInventory().getItemInMainHand() != null)
			fortuneLevel = event.getPlayer().getInventory().getItemInMainHand()
					.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
		if (event.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH))
			hasSilkTouch = true;
		if (event.getBlock().getBlockData() instanceof NoteBlock) {
			ORCustomBlock ORCB = searchORCBbyData((NoteBlock) event.getBlock().getBlockData().clone());
			if (ORCB == null)
				return;
			event.setDropItems(false);
			ORCB.mine(event, fortuneLevel, hasSilkTouch);
		}
	}

	public ORCustomBlock searchORCBbyData(NoteBlock Data) {
		for (CustomBlock CB : plugin.cbmanager.getBlockList())
			if (CB instanceof ORCustomBlock) {
				ORCustomBlock ORCB = (ORCustomBlock) CB;
				if (ORCB.getCustomBlockData().hasSameData(Data))
					return ORCB;

			}
		return null;
	}

}
