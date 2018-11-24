package battleRoyale;


import java.io.FileReader;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main 
{
	static CopyOnWriteArrayList<Weapon> weapons;
	static CopyOnWriteArrayList<Player> players;
	static CopyOnWriteArrayList<Location> locations;
	public static void main(String args[])
	{
		System.out.println("Ciao");
		weapons = ReadFromFile.readWeapons("C:\\Users\\Dario\\Desktop\\Programmazione\\weapons_spazi.txt");
		Weapon.weaponList = weapons;
		for (Weapon weapon : weapons) 
		{
			if(weapon.getBaseWeapon() == null)
			{
				System.out.printf("\n%s\t\t\t\t\t %d\t\t null",weapon.getName(),weapon.getLevel());
			}
			else
			{
				System.out.printf("\n%s\t\t\t\t\t %d\t\t %s",weapon.getName(),weapon.getLevel(),weapon.getBaseWeapon().getName());
			}
		}
		players = ReadFromFile.readPlayers("C:\\Users\\Dario\\Desktop\\Programmazione\\Nomi_Spazi.txt");
		locations = new CopyOnWriteArrayList<Location>();
		/**
		weapons.add(new Weapon("a","pugni",false,90,15,0));
		weapons.add(new Weapon("con delle","pisellate",false,90,10,0));
		weapons.add(new Weapon("con del","terriccio in faccia",false,50,5,0));
		weapons.add(new Weapon("con un","calcio in culo",false,60,20,0));
		weapons.add(new Weapon("con un","coltellino a gasolio",false,60,45,1));
		weapons.add(new Weapon("con","menta scaduta da 7 anni",true,40,55,1));
		weapons.add(new Weapon("con una","sfangatrice elettrica",false,80,60,2));
		weapons.add(new Weapon("con","audio earrape",true,30,30,1));
		Weapon martello_arita = new Weapon("con un","martello di arit� 1",true,50,35,1);
		weapons.add(martello_arita);
		weapons.add(new Weapon("con un","martello di arit� 2",true,50,35,1,martello_arit));
		weapons.add(new Weapon("con un","martello di arit� 3",true,50,35,1));
		weapons.add(new Weapon("con un","martello di arit� 4",true,50,35,1));
		System.out.println("Ciao");
		Weapon.weaponList = weapons;
		players.add(new Player("Dario"));
		players.add(new Player("Paso"));
		players.add(new Player("Pit"));
		players.add(new Player("Marco Petri"));
		players.add(new Player("Vittorino Pata"));
		players.add(new Player("Giulio Fontana"));
		players.add(new Player("Humbert Vigl"));
		System.out.println("Ciao");
		*/
		locations.add(new Location("in","L26"));
		locations.add(new Location("al","Deib"));
		locations.add(new Location("a","Casa Paso"));
		locations.add(new Location("al","Parziale di GAL"));
		locations.add(new Location("al","primo piano di casa Pit al lago"));
		
		System.out.println("Ciao");
		System.out.println("Ciao");
		GameFlow battle = new GameFlow(weapons, players, locations);
		battle.startBattleRoyale();
	}
	
	

}
