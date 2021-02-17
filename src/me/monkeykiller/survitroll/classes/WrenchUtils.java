package me.monkeykiller.survitroll.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.FaceAttachable;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.type.Piston;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;

public class WrenchUtils {
	private static final BlockFace[] orderedRotatableBlockFaces = new BlockFace[] { BlockFace.NORTH,
			BlockFace.NORTH_NORTH_EAST, BlockFace.NORTH_EAST, BlockFace.EAST_NORTH_EAST, BlockFace.EAST,
			BlockFace.EAST_SOUTH_EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_SOUTH_EAST, BlockFace.SOUTH,
			BlockFace.SOUTH_SOUTH_WEST, BlockFace.SOUTH_WEST, BlockFace.WEST_SOUTH_WEST, BlockFace.WEST,
			BlockFace.WEST_NORTH_WEST, BlockFace.NORTH_WEST, BlockFace.NORTH_NORTH_WEST };

	public static boolean isRotatable(Block block, Player player) {
		if (block == null)
			return false;
		BlockData blockData = block.getBlockData();
		if (!(blockData instanceof Directional) && !(blockData instanceof Rotatable)
				&& !(blockData instanceof Orientable))
			return false;
		BlockBreakEvent fakeEvent = new BlockBreakEvent(block, player);
		Bukkit.getPluginManager().callEvent((Event) fakeEvent);
		if (fakeEvent.isCancelled())
			return false;
		return !isBlockBlacklisted(block);
	}

	public static boolean isBlockBlacklisted(Block block) {
		return (block.getBlockData() instanceof org.bukkit.block.data.type.Bed
				|| block.getBlockData() instanceof org.bukkit.block.data.type.WallSign
				|| block.getBlockData() instanceof org.bukkit.block.data.type.RedstoneWallTorch
				|| block.getType() == Material.WALL_TORCH
				|| (block.getState() instanceof org.bukkit.block.Banner && block.getType().name().contains("WALL"))
				|| block.getType() == Material.TRIPWIRE_HOOK || block.getType() == Material.COCOA
				|| isDoubleChest(block) || block.getType() == Material.LADDER
				|| block.getType() == Material.PLAYER_WALL_HEAD || block.getType() == Material.SKELETON_WALL_SKULL
				|| block.getType() == Material.WITHER_SKELETON_WALL_SKULL
				|| block.getType() == Material.CREEPER_WALL_HEAD || block.getType() == Material.DRAGON_WALL_HEAD
				|| block.getType() == Material.ZOMBIE_WALL_HEAD || block.getType() == Material.PISTON_HEAD
				|| isWallButtonOrLever(block) || isExtendedPiston(block));
	}

	private static boolean isDoubleChest(Block block) {
		BlockState state = block.getState();
		if (state instanceof Chest) {
			Chest chest = (Chest) state;
			Inventory inventory = chest.getInventory();
			return inventory instanceof DoubleChestInventory;
		}
		return false;
	}

	private static boolean isWallButtonOrLever(Block block) {
		BlockData blockData = block.getBlockData();
		if (blockData instanceof FaceAttachable)
			return (((FaceAttachable) blockData).getAttachedFace() == FaceAttachable.AttachedFace.WALL);
		return false;
	}

	private static boolean isExtendedPiston(Block block) {
		BlockData blockData = block.getBlockData();
		if (blockData.getMaterial() == Material.PISTON || blockData.getMaterial() == Material.STICKY_PISTON)
			return ((Piston) blockData).isExtended();
		return false;
	}

	public static BlockData handleRotation(Block block) {
		BlockData blockData = block.getBlockData();
		if (blockData instanceof Directional) {
			Directional directional = (Directional) blockData;
			directional.setFacing(getNextBlockFace(directional));
			return (BlockData) directional;
		}
		if (blockData instanceof Rotatable) {
			List<BlockFace> possibleRotations = Arrays.asList(getOrderedRotatableBlockFaces());
			Rotatable rotatable = (Rotatable) blockData;
			rotatable.setRotation(possibleRotations
					.get((possibleRotations.indexOf(rotatable.getRotation()) + 1) % possibleRotations.size()));
			return (BlockData) rotatable;
		}
		if (blockData instanceof Orientable) {
			Orientable orientable = (Orientable) blockData;
			ArrayList<Axis> orientations = new ArrayList<>(orientable.getAxes());
			Collections.sort(orientations);
			orientable
					.setAxis(orientations.get((orientations.indexOf(orientable.getAxis()) + 1) % orientations.size()));
			return (BlockData) orientable;
		}
		return blockData;
	}

	private static BlockFace getNextBlockFace(Directional directional) {
		List<BlockFace> possibleFaces = new ArrayList<>(directional.getFaces());
		Collections.sort(possibleFaces);
		int currentIndex = possibleFaces.indexOf(directional.getFacing());
		return possibleFaces.get((currentIndex + 1) % possibleFaces.size());
	}

	public static BlockFace[] getOrderedRotatableBlockFaces() {
		return orderedRotatableBlockFaces;
	}
	

}
