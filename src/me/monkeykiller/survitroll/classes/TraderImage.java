package me.monkeykiller.survitroll.classes;

public enum TraderImage {
	FARMER("솙");

	private String imgChar;

	TraderImage(String imgChar) {
		this.imgChar = imgChar;
	}

	public String getChar() {
		return imgChar;
	}
}
