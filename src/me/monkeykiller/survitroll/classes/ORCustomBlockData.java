package me.monkeykiller.survitroll.classes;

import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.block.data.type.NoteBlock;

public class ORCustomBlockData {

	public Instrument instrument;
	public Note note;
	public boolean powered;

	public ORCustomBlockData(Instrument instrument, Note note, boolean powered) {
		this.instrument = instrument;
		this.note = note;
		this.powered = powered;
	}

	public ORCustomBlockData(NoteBlock data) {
		this.instrument = data.getInstrument();
		this.note = data.getNote();
		this.powered = data.isPowered();
	}

	public boolean hasSameData(NoteBlock data) {
		if (data.getInstrument() == this.instrument && data.getNote().equals(this.note) && data.isPowered() == this.powered)
			return true;
		return false;
	}
}
