package TreasureHuntAPI;

import java.util.*; 

public class Clue {
	private int clueId;
	private String clueText;
	private GPSCoordinate location;

	public Clue (String clueText, GPSCoordinate location) {
		this.clueId = (int)(Math.random()*10000);
		this.clueText = clueText;
		this.location = location;
	}

	public void setText (String clueText) {
		this.clueText = clueText;
	}
}