package battleRoyale;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameFlow 
{
	private CopyOnWriteArrayList<Weapon> weapons;
	private CopyOnWriteArrayList<Player> players;
	private CopyOnWriteArrayList<Location> locations;
	private int playersInGame;
	private int maxSpawnLevel;
	
	private static final int FIGHT_CHANCE = 80;
	private static final int OBJECT_CHANCE = 80;
	private static final int FIND_OBJECT_RATE = 10;
	private static final int UPGRADE_WEAPON_RATE = 20;
	private static final int MAX_LEVEL = 5;
	private static final int[] WEAPON_LEVELS = {100,90,75,55,35,20};
	
	public GameFlow(CopyOnWriteArrayList<Weapon> weapons, CopyOnWriteArrayList<Player> players, CopyOnWriteArrayList<Location> locations)
	{
		this.weapons = weapons;
		this.players = players;
		this.locations = locations;
		this.maxSpawnLevel = 1;
		this.playersInGame = 50;
		Weapon.weaponList = this.weapons;
	}
	

	private void findOrUpgradeWeapon()
	{
		Random rand = new Random();
		if(this.maxSpawnLevel > 0)
		{
			for(int i = 0; i < this.playersInGame; i++)
			{

				Player player = players.get(i);
				if(player.getPrimaryWeapon().isUpgradable() || player.getSecondaryWeapon().isUpgradable())
				{
					int upgrade = rand.nextInt(100);
					if(upgrade < GameFlow.UPGRADE_WEAPON_RATE)
					{
						if(player.getPrimaryWeapon().isUpgradable())
						{
							Weapon newWeapon = player.getPrimaryWeapon().getLevelUpWeapon();
							Narrator.narrateUpgradeWeapon(player, player.getPrimaryWeapon(), newWeapon);
							player.setPrimaryWeapon(player.getPrimaryWeapon().getLevelUpWeapon());
						}
						else
						{
							Weapon newWeapon = player.getSecondaryWeapon().getLevelUpWeapon();
							Narrator.narrateUpgradeWeapon(player, player.getSecondaryWeapon(), newWeapon);
							player.setSecondaryWeapon(player.getSecondaryWeapon().getLevelUpWeapon());
						}
					}
				}
				int objectFind = rand.nextInt(100);
				if(objectFind < GameFlow.FIND_OBJECT_RATE)
				{
					int typeObject = rand.nextInt(4);
					if(typeObject < 4) //Trova un arma
					{
						Weapon weapon = Weapon.randomWeapon(1,this.maxSpawnLevel,true);
						if(weapon.getTier() >= player.getMaxWeaponTier())
						{
							Weapon toChange = Weapon.worstWeapon(player.getPrimaryWeapon(), player.getSecondaryWeapon());
							Weapon toKeep = Weapon.bestWeapon(toChange,weapon);
							if(toChange == player.getPrimaryWeapon())
							{
								if(toKeep != player.getPrimaryWeapon())
								{
									player.setPrimaryWeapon(toKeep);
									Narrator.narrateFindWeapon(player, toKeep);
								}
							}
							else
							{
								if(toKeep != player.getSecondaryWeapon())
								{
									player.setSecondaryWeapon(toKeep);
									Narrator.narrateFindWeapon(player, toKeep);
								}
							}
						}
					}
					//TODO altri oggetti
				}
			}
		}
	}
	
	private FightResult startFight(int minSleep,int maxSleep)
	{
		Random rand = new Random();
		if(playersInGame > 1)
		{
			int num1, num2, numLuogo;
			do
			{
				num1 = rand.nextInt(playersInGame);
				num2 = rand.nextInt(playersInGame);
			}
			while(num1 == num2);
			numLuogo = rand.nextInt(locations.size());
			Fight fight = new Fight(locations.get(numLuogo), players.get(num1), players.get(num2));
			Narrator.narratePreFight(players.get(num1), players.get(num2), locations.get(numLuogo));
			FightResult res;
			do
			{
				res = fight.nextHit();
				Narrator.narrateFight(res);
			}
			while(res.fightStatus == Fight.STATUS_STILL_FIGHTING);
			if(res.fightStatus == Fight.STATUS_KILL || res.fightStatus == Fight.STATUS_STEALTH_KILL)
			{
				Player temp;
				int killed = players.indexOf(res.playerToHit);
				temp = this.players.get(playersInGame - 1);
				this.players.set(playersInGame - 1, players.get(killed));
				this.players.set(killed, temp);
				playersInGame--;
				System.out.printf("\nGIOCATORI RIMANTENTI: %d\n\n", playersInGame);
			}
			return res;
		}
		else
		{
			return null;
		}
		
	}
	
	private void randomSleep(int minSleep,int maxSleep)
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
	 private void pressAnyKeyToContinue()
	 { 
	        //
		 
		 //System.out.println("Press Enter key to continue...");
	        try
	        {
	            System.in.read();
	        }  
	        catch(Exception e)
	        {}  
	 }
}
