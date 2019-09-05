package me.dariopassarello.battleroyale.handlers;

import me.dariopassarello.battleroyale.components.Player;
import me.dariopassarello.battleroyale.components.FightResult;
import me.dariopassarello.battleroyale.components.HitStats;
import me.dariopassarello.battleroyale.components.Location;
import me.dariopassarello.battleroyale.lootables.Armour;
import me.dariopassarello.battleroyale.lootables.Potion;
import me.dariopassarello.battleroyale.lootables.Weapon;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public final class Narrator 
{
	public static CopyOnWriteArrayList<Player> watchlist;
	public static int minPriority;
	private static SimpleDateFormat sdf = new SimpleDateFormat();
	public static boolean wait = true;

	public static void sayDayHour(int day, int hour, int players)
	{
		System.out.printf("\n\n\nGIORNO: %d\n ORE %d:00\n GIOCATORI IN VITA: %d",day,hour,players);
	}
	
	public static void narratePlayersRemaing(int players)
	{
		System.out.printf("\nGIOCATORI RIMANENTI: %d",players);
	}
	
	public static void narratePreFight(Player[] players, Location loc)
	{
		//Clear screen
		System.out.print("\033[H\033[2J");
		System.out.flush();
		for(Player p : players)
		{	
			Narrator.printHealtBar(String.format("\n%-70s [",p.getName()),String.format("] %d HP\n",p.getHP()),p.getHP(),100,40,'X');
		}
		//System.out.printf("\niniziano a combattere %s %s! BUONA FORTUNA!",loc.getPreposition(),loc.getName());
	}
	
	public static void narratePostFight()
	{
		System.out.printf("\n");
	}
	
	
	public static void narrateHealing(Player player, Potion potion, int HPprec)
	{
		System.out.printf("\n%s (%d HP) ha usato %s, recuperando %d HP",player.getName(),player.getHP(),potion.getName(),player.getHP() - HPprec);
		if(wait == true)
		{
			Narrator.randomSleep(800,1600);
		}
	}
	
	public static void narratePoison(CopyOnWriteArrayList<Player> players)
	{
		boolean damageFromPoison = false;

		System.out.printf("\n DANNI DA VELENO: ");
		for(Player player : players)
		{
			
			if(player.getPoisonAmount() > 0)
			{
				System.out.printf("\n%s (HP %d, DANNO: %d HP)", player.getName().toUpperCase()
						, player.getHP() - player.getPoisonAmount()> 0 ? player.getHP() - player.getPoisonAmount() : 0
						, player.getPoisonAmount());
				damageFromPoison = true;
			}
			
		}
		if(damageFromPoison == false)
		{
			System.out.printf("(nessuno)");
		}
		for(Player player : players)
		{
			if(player.getPoisonAmount() > 0 && player.getHP() == 0)
			{
				System.out.printf("\n%s è morto di avvelenamento(DANNO: %d HP)", player.getName().toUpperCase(),player.getPoisonAmount());
			}
		}

	}

	public static void printHealtBar(String preText,String postText,int value,int maxValue,int lenght,char barSymbol)
	{
		System.out.printf("%s" ,preText);
		int numberOfChars = Math.min((int) Math.ceil(((float) value/maxValue)*lenght),lenght);
		for(int i = 0; i < lenght; i++)
		{
			if(i < numberOfChars)
			{
				System.out.printf("%c",barSymbol);
			}
			else {
				System.out.printf(" ");
			}
		}
		System.out.printf("%s",postText);
	}

	//echo -e "\xE2\x98\xA0"
	public static void narrateFight(FightResult res)
	{
		narratePreFight(res.fight.getPlayers(),res.location);
		if(res.fightStatus == Fight.STATUS_ESCAPE)
		{
			System.out.printf("\n%s e' fuggito, gli rimangono %d HP",res.playerHitter.getName(),res.playerHitter.getHP());
		}
		else if(res.fightStatus == Fight.STATUS_STILL_FIGHTING)
		{
			if(res.damageStats.typeOfDamage == HitStats.DAMAGE_CRITICAL_HIT)
			{
				System.out.printf("\n%s (%d HP) ha colpito MOLTO FORTE %s (%d HP) %s %s, (Danno: %d HP)\n%s è Disarmato",res.playerHitter.getName(),res.playerHitter.getHP(),
						res.playerToHit.getName(),res.playerToHit.getHP(),res.weaponUsed.getPrefix(),res.weaponUsed.getName(),res.damageStats.damageDealt,res.playerToHit.getName());
			}
			else if(res.damageStats.typeOfDamage == HitStats.DAMAGE_FULL_HIT)
			{
				System.out.printf("\n%s (%d HP) ha colpito %s (%d HP) %s %s, (Danno: %d HP)",res.playerHitter.getName(),res.playerHitter.getHP(),
						res.playerToHit.getName(),res.playerToHit.getHP(),res.weaponUsed.getPrefix(),res.weaponUsed.getName(),res.damageStats.damageDealt);
			}
			else if(res.damageStats.typeOfDamage == HitStats.DAMAGE_PARTIAL_HIT)
			{
				System.out.printf("\n%s (%d HP) ha colpito di striscio %s (%d HP) %s %s, (Danno: %d HP)",res.playerHitter.getName(),res.playerHitter.getHP(),
						res.playerToHit.getName(),res.playerToHit.getHP(),res.weaponUsed.getPrefix(),res.weaponUsed.getName(),res.damageStats.damageDealt);
			}
			else
			{
				System.out.printf("\n%s (%d HP) ha provato a colpire %s (%d HP) %s %s",res.playerHitter.getName(),res.playerHitter.getHP(),
						res.playerToHit.getName(),res.playerToHit.getHP(),res.weaponUsed.getPrefix(),res.weaponUsed.getName());
			}

			
		}
		else
		{
			System.out.printf("\n%s (%d HP) ha ucciso %s (%d HP) %s %s, %s %s (Danno: %d HP)",res.playerHitter.getName(),res.playerHitter.getHP(),
					res.playerToHit.getName(),res.playerToHit.getHP(),res.weaponUsed.getPrefix(),res.weaponUsed.getName(),res.location.getPreposition(),res.location.getName(),res.damageStats.damageDealt);
		}
	}
	public static void pseudoClearScreen()
	{
		for(int i = 0; i < 50; i++)
		{
			System.out.printf("\n");
		}
	}


	public static void killersStat(CopyOnWriteArrayList<Player> players,int top)
	{

		players = Narrator.sortPlayers(players,new Player.KillComparator(), true);

		for(int i = 0; i < Math.min(top,players.size()); i++)
		{
			System.out.format("\n%3d.%70s%3d%5s",i + 1,players.get(i).getName(),players.get(i).getKills(),players.get(i).getDamageDealt());
		}
	}

	public static void narrateUpgradeWeapon(Player player, Weapon weaponOld, Weapon weaponNew)
	{
		System.out.printf("\n%s ha potenziato \"%s \" in \"%s \" (LIVELLO %d) \n",player.getName().toUpperCase(),weaponOld.getName(),weaponNew.getName().toUpperCase(),
				weaponNew.getLevel());
		System.out.printf("ARMI: [%s(%d) |%s(%d) ]\n",player.getPrimaryWeapon().getName(),player.getPrimaryWeapon().getLevel(),
				player.getSecondaryWeapon().getName(),player.getSecondaryWeapon().getLevel());
		if(wait == true)
		{
			Narrator.randomSleep(800,1600);
		}
	}
	public static void narrateEquipArmour(Player player, Armour armour)
	{
		System.out.printf("\n%s ha equipaggiato \"%s \" (HEALTH:%d) , \n",player.getName().toUpperCase(),armour.getName().toUpperCase(),armour.getMaxHealth());
		if(wait == true)
		{
			Narrator.randomSleep(800,1600);
		}
	}
	public static void narrateEquipPotion(Player player,Potion potion)
	{
		System.out.printf("\n%s ha raccolto \"%s \" (HEAL:%d/%d) , \n",player.getName().toUpperCase(),potion.getName(),potion.getHealing(),potion.getHealthCap());
		if(wait == true)
		{
			Narrator.randomSleep(800,1600);
		}
	}
	public static void narrateFindWeapon(Player player,Weapon weapon)
	{
		System.out.printf("\n%s ha trovato \"%s \" (LIVELLO %d) \n",player.getName().toUpperCase(),weapon.getName().toUpperCase(),weapon.getLevel(),player.getPrimaryWeapon().getName(),
				player.getSecondaryWeapon().getName());
		System.out.printf("ARMI: [%s (%d) |%s (%d) ]\n",player.getPrimaryWeapon().getName(),player.getPrimaryWeapon().getLevel(),
				player.getSecondaryWeapon().getName(),player.getSecondaryWeapon().getLevel());
		if(wait == true)
		{
			Narrator.randomSleep(800,1600);
		}
	}

	public static void randomSleep(int minSleep,int maxSleep)
	{
		Random rand = new Random();
		if(minSleep == maxSleep)
		{
			try {
				Thread.sleep(minSleep);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		int sleepTime = rand.nextInt(maxSleep - minSleep) + minSleep;
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static CopyOnWriteArrayList<Player> sortPlayers(CopyOnWriteArrayList<Player> players,Comparator<Player> c,boolean reverse)
	{
		for(int i = 0; i < players.size() - 1; i++)
		{
			for(int j = i + 1; j < players.size(); j++)
			{
				int comp = c.compare(players.get(i),players.get(j));
				if((comp > 0 && reverse == false) || (comp < 0 && reverse == true))
				{
					Player temp = players.get(i);
					players.set(i,players.get(j));
					players.set(j,temp);
				}
			}
		}
		return players;
	}


}
