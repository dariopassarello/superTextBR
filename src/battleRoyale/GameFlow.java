package battleRoyale;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameFlow 
{
	private CopyOnWriteArrayList<Player> allPlayers = new CopyOnWriteArrayList<Player>();
	private CopyOnWriteArrayList<Player> deadPlayers  = new CopyOnWriteArrayList<Player>();
	private CopyOnWriteArrayList<Player> players;
	private CopyOnWriteArrayList<Location> locations;
	private CopyOnWriteArrayList<Weapon> weapons;
	private CopyOnWriteArrayList<Armour> armours;
	private CopyOnWriteArrayList<Potion> potions;
	private CopyOnWriteArrayList<Player> watchlist;

	private int playersInGame;
	private int maxSpawnLevel;
	private int gamemode;
	private int tick;

	private static final int FIGHT_PROBABILITY = 80;
	private static final int FIND_OBJECT_RATE_MAX = 40;
	private static final int FIND_OBJECT_RATE_MIN = 3;
	private static final int UPGRADE_WEAPON_RATE = 20;
	private static final int[] WEAPON_TICKS = {5,35,60,90,120};
	private static final int[] NUMBER_OF_FIGHTERS_ODDS = {32,48,56,60,62};
												//0 - 50 Weapon, 51-80 Armour, 80-99 Potion
	private static final int[] TYPE_OF_LOOT_ODDS = {50,80};
	public static final int MODE_BATTLE_ROYALE = 0;
	
	public GameFlow(int gameMode, CopyOnWriteArrayList<Player> players, 
			CopyOnWriteArrayList<Location> locations, CopyOnWriteArrayList<Lootable> lootables)
	{
		
		this.players = players;
		this.allPlayers.addAll(players);
		
		this.locations = locations;
		this.weapons = Lootable.getWeapons(lootables);
		this.potions = Lootable.getPotions(lootables);
		this.armours = Lootable.getArmours(lootables);
		this.maxSpawnLevel = 1;
		this.playersInGame = 50;
		this.gamemode = gameMode;
		Weapon.weaponList = this.weapons;
		this.tick = 0;
	}
	
	
	public void startGame()
	{
		battleRoyale();
	}

	private void battleRoyale()               
	{
		while(this.getAlivePlayers() > 1)
		{
			
			
			promptEnterKey();
			Narrator.sayDayHour(day(this.tick), hour(this.tick));
			this.maxSpawnLevel = RandomManager.multiRange(this.tick, GameFlow.WEAPON_TICKS);
			//LOOT
			this.tryPlayersLooting();
			//BATTLE
			//Skip battle
			if(!RandomManager.isInRandomRange(0, 100, GameFlow.FIGHT_PROBABILITY, 100))
			{
				int numberOfFighters = RandomManager.multiRandomRange(0, 64, GameFlow.NUMBER_OF_FIGHTERS_ODDS) + 2;
				numberOfFighters = Math.min(this.getAlivePlayers(), numberOfFighters);
				CopyOnWriteArrayList<Integer> playerNumbers = new CopyOnWriteArrayList<Integer>();
				CopyOnWriteArrayList<Player> playersArrayList = new CopyOnWriteArrayList<Player>();
				Player playersArray[];

				for(int i = 0; i < numberOfFighters; i++)
				{
					playerNumbers.add(RandomManager.randomRangeExcluded(0, this.getAlivePlayers(), playerNumbers.toArray(new Integer[playerNumbers.size()])));
					playersArrayList.add(this.players.get(playerNumbers.get(i)));
				}

				playersArray = playersArrayList.toArray(new Player[playersArrayList.size()]);
				Location location = locations.get(RandomManager.randomRange(0, locations.size()));
				Narrator.narratePreFight(playersArray, location);
				Fight fight = new Fight(location,playersArray);
				FightResult res;
				do
				{
					res = fight.nextHit();
					if(res.fightStatus == Fight.STATUS_KILL || res.fightStatus == Fight.STATUS_STEALTH_KILL)
					{
						this.killPlayer(res.playerToHit);
					}
					Narrator.narrateFight(res);
				}
				while(res.playersRemaingInFight > 1);
				Narrator.narratePostFight();
			}
			Narrator.narratePoison(players);

			for(Player player : this.players)
			{
				player.updateHP();				
				if(player.getHP() <= 0)
				{
					this.killPlayer(player);
				}
			}
			this.tick++;
		}
	}
	
	private void tryPlayersLooting()
	{
		if(this.maxSpawnLevel > 0)
		{
			for(Player player : players)
			{
				int numberOfUpgradable = 0;
				if(player.getPrimaryWeapon().isUpgradable())
				{
					numberOfUpgradable += 2;

				}
				if(player.getSecondaryWeapon().isUpgradable())
				{
					numberOfUpgradable += 1;

				}
				if(numberOfUpgradable > 0 && RandomManager.isInRandomRange(0, 100, 0, GameFlow.UPGRADE_WEAPON_RATE))
				{
					
					if(numberOfUpgradable == 3) numberOfUpgradable = RandomManager.randomRange(1, 2);
					if(numberOfUpgradable == 2)
					{
						Narrator.narrateUpgradeWeapon(player, player.getPrimaryWeapon(), player.getPrimaryWeapon().getLevelUpWeapon());
						player.setPrimaryWeapon(player.getPrimaryWeapon().getLevelUpWeapon());
					}
					else
					{
						Narrator.narrateUpgradeWeapon(player, player.getSecondaryWeapon(), player.getSecondaryWeapon().getLevelUpWeapon());
						player.setSecondaryWeapon(player.getSecondaryWeapon().getLevelUpWeapon());
					}
				}
				else
				{
					
					if(RandomManager.isInRandomRange(0, 100, 0, this.getFindWeaponChance()))
					{
						Lootable itemFound = null;
						switch(RandomManager.multiRandomRange(0, 100, GameFlow.TYPE_OF_LOOT_ODDS))
						{
							case 0: itemFound = Weapon.randomWeapon(1, this.maxSpawnLevel, false);
									break;
							case 1: itemFound = this.armours.get(RandomManager.randomRange(0, this.armours.size()));
									break;
							case 2: itemFound = this.potions.get(RandomManager.randomRange(0, this.potions.size()));
								break;
								
						}
						if(itemFound instanceof Weapon)
						{
							Weapon weaponFound = (Weapon) itemFound;
							if(Weapon.worstWeapon(player.getPrimaryWeapon(), player.getSecondaryWeapon()) == player.getPrimaryWeapon())
							{
								if(Weapon.bestWeapon(player.getPrimaryWeapon(), weaponFound) == weaponFound)
								{
									
									player.setPrimaryWeapon(weaponFound);
									Narrator.narrateFindWeapon(player, weaponFound);
								}
							}
							else
							{
								if(Weapon.bestWeapon(player.getSecondaryWeapon(), weaponFound) == weaponFound)
								{
									
									player.setSecondaryWeapon(weaponFound);
									Narrator.narrateFindWeapon(player, weaponFound);
								}
							}
						}
						boolean ok = false;
						if(itemFound instanceof Armour)
						{
							
							if(player.getArmour() == null) ok = true;
							if( ok == false && player.getArmour().getMetric() < itemFound.getMetric() ) ok = true;
							if(ok)
							{
								player.setArmour(((Armour) itemFound).getNewArmour());
								Narrator.narrateEquipArmour(player, (Armour) itemFound);
							}
						}
						if(itemFound instanceof Potion)
						{
							if(player.getPotion() == null) ok = true;
							if(ok == false && player.getPotion().getMetric() < itemFound.getMetric()) ok = true;
							if(ok)
							{
								player.setPotion((Potion) itemFound);
								Narrator.narrateEquipPotion(player, (Potion) itemFound);
							}
						}
					}
				}
			}
		}
	}
	
	private int day(int tick)
	{
		return (tick + 12)/24 + 1;
	}
	private int hour(int tick)
	{
		return (tick + 12)%24;
	}
	
	private void killPlayer(Player player)
	{
		this.players.remove(player);
		this.deadPlayers.add(player);
		Narrator.narratePlayersRemaing(this.getAlivePlayers());
	}
	
	private int getAlivePlayers()
	{
		int alive = 0;
		for(Player player : this.players)
		{
			if(player.isAlive())
			{
				alive++;
			}
		}
		return alive;
	}
	
	private int getFindWeaponChance()
	{
		float k = (float) this.getAlivePlayers()/this.allPlayers.size();
		//System.out.println(((1 - k)*GameFlow.FIND_OBJECT_RATE_MAX + k*GameFlow.FIND_OBJECT_RATE_MIN));
		return (int) ((1 - k)*GameFlow.FIND_OBJECT_RATE_MAX + k*GameFlow.FIND_OBJECT_RATE_MIN);
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
	

	public void promptEnterKey()
	{

		 System.out.println("Press \"ENTER\" to continue...");
		        try {
		            int read = System.in.read(new byte[2]);
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
	}

}
