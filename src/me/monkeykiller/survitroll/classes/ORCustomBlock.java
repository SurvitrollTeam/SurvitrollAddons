package me.monkeykiller.survitroll.classes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.inventory.ItemStack;

public class ORCustomBlock extends CustomBlock {
	protected ORCustomBlockData data;

	public ORCustomBlock(String id, ItemStack loot, ItemStack silkloot, boolean allowFortune, boolean allowSilkTouch,
			int xpLoot, ORCustomBlockData data) {
		super(id, 0, loot, silkloot, allowFortune, allowSilkTouch, xpLoot);
		this.data = data;
	}

	@Override
	public void place(Location loc) {
		Block block = loc.getBlock();
		block.setType(Material.NOTE_BLOCK, false);
		if (block.getBlockData() instanceof NoteBlock) {
			NoteBlock state = (NoteBlock) block.getBlockData();
			state.setInstrument(data.instrument);
			state.setNote(data.note);
			state.setPowered(data.powered);
			block.setBlockData(state);
		}
	}

	public ORCustomBlockData getCustomBlockData() {
		return data;
	}

	public boolean compareData(NoteBlock data) {
		if (new ORCustomBlockData(data).equals(this.data))
			return true;
		return false;
	}

}
