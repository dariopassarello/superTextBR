package battleRoyale;




import java.io.IOException;
import java.util.Scanner;
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
		printTitle();
		promptEnterKey();
		LiveNarrator.live = getInt("Premi 0 per una simulazione a tempo, 1 altrimenti...") == 0;
		game.startGame();
	}

	static void printTitle()
	{
		System.out.println( " ______     __  __     ______   ______     ______        ______   ______     __  __     ______                                        ");
		System.out.println( "/\\  ___\\   /\\ \\/\\ \\   /\\  == \\ /\\  ___\\   /\\  == \\      /\\__  _\\ /\\  ___\\   /\\_\\_\\_\\   /\\__  _\\                                       ");
		System.out.println( "\\ \\___  \\  \\ \\ \\_\\ \\  \\ \\  _-/ \\ \\  __\\   \\ \\  __<      \\/_/\\ \\/ \\ \\  __\\   \\/_/\\_\\/_  \\/_/\\ \\/                                       ");
		System.out.println( " \\/\\_____\\  \\ \\_____\\  \\ \\_\\    \\ \\_____\\  \\ \\_\\ \\_\\       \\ \\_\\  \\ \\_____\\   /\\_\\/\\_\\    \\ \\_\\                                       ");
		System.out.println( "  \\/_____/   \\/_____/   \\/_/     \\/_____/   \\/_/ /_/        \\/_/   \\/_____/   \\/_/\\/_/     \\/_/                                       ");
		System.out.println( "                                                                                                                                      ");
		System.out.println( " ______     ______     ______   ______   __         ______           ______     ______     __  __     ______     __         ______    ");
		System.out.println( "/\\  == \\   /\\  __ \\   /\\__  _\\ /\\__  _\\ /\\ \\       /\\  ___\\         /\\  == \\   /\\  __ \\   /\\ \\_\\ \\   /\\  __ \\   /\\ \\       /\\  ___\\   ");
		System.out.println( "\\ \\  __<   \\ \\  __ \\  \\/_/\\ \\/ \\/_/\\ \\/ \\ \\ \\____  \\ \\  __\\         \\ \\  __<   \\ \\ \\/\\ \\  \\ \\____ \\  \\ \\  __ \\  \\ \\ \\____  \\ \\  __\\   ");
		System.out.println( " \\ \\_____\\  \\ \\_\\ \\_\\    \\ \\_\\    \\ \\_\\  \\ \\_____\\  \\ \\_____\\        \\ \\_\\ \\_\\  \\ \\_____\\  \\/\\_____\\  \\ \\_\\ \\_\\  \\ \\_____\\  \\ \\_____\\ ");
		System.out.println( "  \\/_____/   \\/_/\\/_/     \\/_/     \\/_/   \\/_____/   \\/_____/         \\/_/ /_/   \\/_____/   \\/_____/   \\/_/\\/_/   \\/_____/   \\/_____/ ");
		System.out.println( "                                                                                                                                      ");
	}

	static void promptEnterKey()
	{

		System.out.println("\n\nPREMI INVIO PER INIZIARE...");
		try {
			int read = System.in.read(new byte[2]);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static int getInt(String text)
	{
		Scanner reader = new Scanner(System.in);
		System.out.println(text);
		int n = reader.nextInt();
		return n;
	}

}
