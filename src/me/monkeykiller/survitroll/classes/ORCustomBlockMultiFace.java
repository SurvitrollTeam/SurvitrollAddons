package me.monkeykiller.survitroll.classes;

import java.util.List;

import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ORCustomBlockMultiFace extends ORCustomBlock {

	public ORCustomBlockMultiFace(String id, ItemStack loot, ItemStack silkloot, boolean allowFortune,
			boolean allowSilkTouch, int xpLoot, ORCustomBlockData data) {
		super(id, loot, silkloot, allowFortune, allowSilkTouch, xpLoot, data);
	}

	public void place(Player player, Location loc, BlockFace face) {
		Block block = loc.getBlock();

		if (face == BlockFace.UP || face == BlockFace.DOWN) {
			block.setType(Material.NOTE_BLOCK, false);
			if (block.getBlockData() instanceof NoteBlock) {
				NoteBlock state = (NoteBlock) block.getBlockData();
				state.setInstrument(Instrument.CHIME);
				state.setNote(new Note(15));
				state.setPowered(data.powered);
				block.setBlockData(state);
			}
		} else if (face == BlockFace.NORTH || face == BlockFace.SOUTH) {
			block.setType(Material.NOTE_BLOCK, false);
			if (block.getBlockData() instanceof NoteBlock) {
				NoteBlock state = (NoteBlock) block.getBlockData();
				state.setInstrument(Instrument.COW_BELL);
				state.setNote(new Note(17));
				state.setPowered(data.powered);
				block.setBlockData(state);
			}
		} else/* if (face == BlockFace.UP || face == BlockFace.DOWN) */ {
			block.setType(Material.NOTE_BLOCK, false);
			if (block.getBlockData() instanceof NoteBlock) {
				NoteBlock state = (NoteBlock) block.getBlockData();
				state.setInstrument(Instrument.COW_BELL);
				state.setNote(new Note(18));
				state.setPowered(data.powered);
				block.setBlockData(state);
			}
		}

	}

	public BlockFace getBlockFace(Player player) {
		List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(null, 100);
		if (lastTwoTargetBlocks.size() != 2 || !lastTwoTargetBlocks.get(1).getType().isOccluding())
			return null;
		Block targetBlock = lastTwoTargetBlocks.get(1);
		Block adjacentBlock = lastTwoTargetBlocks.get(0);
		return targetBlock.getFace(adjacentBlock);
	}

}
