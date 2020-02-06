package TreasureHuntAPI;

import java.util.*; 

public class TreasureHunt {
	private int treasureHuntId;
	private String treasureHuntName;
	private int numberOfSteps;
	private int difficulty;
	private int coins;
	private List<Clue> clues;
	private User admin;

	public TreasureHunt (String treasureHuntName, int numberOfSteps, int difficulty, int coins, User admin) {
		this.treasureHuntId = (int)(Math.random()*10000);
		this.treasureHuntName = treasureHuntName;
		this.numberOfSteps = numberOfSteps;
		this.difficulty = difficulty;
		this.coins = coins;
		this.clues = new ArrayList<Clue>();
		this.admin = admin;
	}

	// public void addClue (Clue newClue) {

	// }

	// public void removeClue (Clue oldClue) {

	// }
}