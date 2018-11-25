package battleRoyale;




import java.util.concurrent.CopyOnWriteArrayList;

public class Main 
{
	static CopyOnWriteArrayList<Lootable> lootables = new CopyOnWriteArrayList<Lootable>();
	static CopyOnWriteArrayList<Player> players = new CopyOnWriteArrayList<Player>();
	static CopyOnWriteArrayList<Location> locations = new CopyOnWriteArrayList<Location>();
	static final String FOLDER = "/home/dario/Desktop/Progetti/eclipse/SuperTextBR/config/";
	public static void main(String args[])
	{
		lootables.addAll(ReadFromFile.readWeapons(FOLDER.concat("weapons.txt")));
		lootables.addAll(ReadFromFile.readArmours(FOLDER.concat("armours.txt")));
		lootables.addAll(ReadFromFile.readPotions(FOLDER.concat("potions.txt")));
		players.addAll(ReadFromFile.readPlayers(FOLDER.concat("players.txt")));
		System.out.println(players.toString());
		locations = ReadFromFile.readLocations(FOLDER.concat("locations.txt"));
		GameFlow game = new GameFlow(0, players, locations, lootables);
		game.startGame();
	}	
	

}
