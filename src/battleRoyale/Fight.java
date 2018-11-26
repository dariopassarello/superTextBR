package battleRoyale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Fight
{
	
	private Player players[];
	private int discipline[];
	private Location location;
	private int turn;

	
	public static final int STATUS_STILL_FIGHTING = 0;
	public static final int STATUS_STEALTH_KILL = 1;
	public static final int STATUS_KILL = 2;
	public static final int STATUS_ESCAPE = 3;
	
	
	private static final int USE_WEAKEST_WEAPON_CHANCE = 15;
	private static final float STEALTH_FIRST_DAMAGE_MULTIPLIER = 2.0f;
	private static final float STEALTH_FIRST_HIT_RATE_MULTIPLIER = 2.0f;
	
	private static final int BASE_ESCAPE_SCORE = 35;
	private static final int ROUND_ESCAPE_BONUS = -3;
	private static final int FIGHT_STARTER_DISCIPLINE_BONUS = 5;
	private static final int HEALTIEST_DISCIPLINE_BONUS = 20;
	private static final int DISARMED_ESCAPE_BONUS  = -20;
	
	public Fight(Location location, Player... players)
	{
		this.location = location;
		this.players = players;
		this.turn = 0;
		
		this.discipline = new int[players.length];
		this.updateDiscipline(0);
	}


	public Player getPlayer1() 
	{
		return players[0];
	}

	public void setPlayer1(Player attacker) 
	{
		this.players[0] = attacker;
	}

	public Player getPlayer2() 
	{
		return players[1];
	}

	public void setPlayer2(Player defender) 
	{
		this.players[1] = defender;
	}

	public Location getLocation() 
	{
		return location;
	}

	public void setLocation(Location location) 
	{
		this.location = location;
	}

	public int getTurn() 
	{
		return turn;
	}
	

	
	private void updateDiscipline(int round)
	{
		//Base discipline
		int maxHP = 0;
		for(int i = 0; i < this.players.length; i++)
		{
			this.discipline[i] = Fight.BASE_ESCAPE_SCORE + players[i].getHP() + Fight.ROUND_ESCAPE_BONUS * (round - 4);
			if(i == 0) discipline[i] += Fight.FIGHT_STARTER_DISCIPLINE_BONUS;
			if(players[i].isDisarmed()) this.discipline[i] += Fight.DISARMED_ESCAPE_BONUS;
			if(players[i].getHP() > maxHP)
			{
				maxHP = players[i].getHP();
			}
		}
		for(int i = 0; i < this.players.length; i++)
		{
			if(players[i].getHP() >= maxHP) this.discipline[i] += Fight.HEALTIEST_DISCIPLINE_BONUS;
		}
		
	}
	
	private boolean tryEscapeFight(int discipline)
	{
		if(discipline <= 0) return true;
		if(discipline >= 100) return false;
		return RandomManager.isInRandomRange(0,100,discipline,100);
	}
	
	public void removePlayerFromFight(Player player)
	{
		ArrayList<Player> p = new ArrayList<Player>(Arrays.asList(this.players));
		p.remove(player);
		this.players = p.toArray(new Player[p.size()]);
	}

	public FightResult nextHit()
	{
		//Numero giocatore che attacca/fugge in questo turno
		int activePlayerNum = this.turn % players.length;
		//Numero round (numero di volte in cui i giocatori hanno attaccato almeno una volta)
		int round = this.turn / players.length;
		//Se il giocatore riesce a scappare escape = true
		boolean escape;
		//Vero se un giocatore � stato disarmato in questo round
		boolean disarmingInThisRound = false;
		int playerToHitNumber;
		float damageMultiplier = 1.0f;
		float hitMultiplier = 1.0f;
		Weapon weaponUsed;
		Player playerToHit;
		FightResult res;

		if(this.players.length > 1)
		{
			if(round > 0 && activePlayerNum == 0)
			{
				this.shuffleArray();
			}
			if(this.turn > 1)
			{
				this.updateDiscipline(round);
				escape = this.tryEscapeFight(discipline[activePlayerNum]);
				
				if(escape == true)
				{
					
					res = new FightResult(null,this.players[activePlayerNum],null,null,Fight.STATUS_ESCAPE,this.location);
					this.players[activePlayerNum].setDisarmed(false);
					this.removePlayerFromFight(this.players[activePlayerNum]);
					res.playersRemaingInFight = this.players.length;
					return res;
				}
			}
			//Sorteggia l'arma
			//Se al turno 0 il primo giocatore ha un arma stealth attacca con quella facendo piu danno se colpisce
			if(this.players[activePlayerNum].getMaxWeaponTier() == 0 || this.players[activePlayerNum].isDisarmed())
			{
				weaponUsed = Weapon.randomWeapon(0,0,true);
			}
			else
			{
				//Se il giocatore � disarmato o non ha armi di livello >0. Utilizza un arma di livello 0 (corpo a corpo)
				if(this.turn == 0 && this.players[1].getSecondaryWeapon().isStealth() == true)
				{
					weaponUsed = this.players[activePlayerNum].getSecondaryWeapon();
					damageMultiplier = Fight.STEALTH_FIRST_DAMAGE_MULTIPLIER;
					hitMultiplier = Fight.STEALTH_FIRST_HIT_RATE_MULTIPLIER;
				}
				else
				{
					//Utilizza con molta probabilit� l'arma di livello piu alto
					Weapon bestWeapon = Weapon.bestWeapon(this.players[activePlayerNum].getPrimaryWeapon(), this.players[activePlayerNum].getSecondaryWeapon());
					Weapon worstWeapon = Weapon.worstWeapon(this.players[activePlayerNum].getPrimaryWeapon(), this.players[activePlayerNum].getSecondaryWeapon());
					if(RandomManager.isInRandomRange(0, 100, 0, Fight.USE_WEAKEST_WEAPON_CHANCE))
					{
						weaponUsed = worstWeapon;
					}
					else
					{
						weaponUsed = bestWeapon;
					}
				}
			}
			//Colpiscei un giocatore a caso
			do
			{
				playerToHitNumber = RandomManager.randomRangeExcluded(0, players.length, activePlayerNum);
				
			}
			while(playerToHitNumber == activePlayerNum);
			playerToHit = this.players[playerToHitNumber];
			HitStats damageStats = weaponUsed.tryHit(playerToHit,damageMultiplier,hitMultiplier); //hit the player
			this.players[activePlayerNum].addDamageDealt(damageStats.damageDealt);
			//Se colpisci critico disarma l'avversario
			if(damageStats.typeOfDamage == HitStats.DAMAGE_CRITICAL_HIT)
			{
				playerToHit.setDisarmed(true);
				disarmingInThisRound = true;
			}
			//Crea il resoconto del fight per il narratore
			if(playerToHit.isAlive() == true)
			{
				res = new FightResult(playerToHit,this.players[activePlayerNum],damageStats,weaponUsed,Fight.STATUS_STILL_FIGHTING,this.location);
				
			}
			else if(playerToHit.isAlive() == false && this.turn > 0)
			{
				res = new FightResult(playerToHit,this.players[activePlayerNum],damageStats,weaponUsed,Fight.STATUS_KILL,this.location);
				this.players[activePlayerNum].addKill();
				this.removePlayerFromFight(playerToHit);
			}
			else
			{
				res = new FightResult(playerToHit,this.players[activePlayerNum],damageStats,weaponUsed,Fight.STATUS_STEALTH_KILL,this.location);
				this.players[activePlayerNum].addKill();
				this.removePlayerFromFight(playerToHit);
			}
			if(this.players.length == 1)
			{
				this.players[0].setDisarmed(false);
			}
			//Incrementa numero turno
			this.turn++;
			if(disarmingInThisRound == true)
			{
				res.unarmed = true;
			}
			//
			res.playersRemaingInFight = this.players.length;
		
			return res;
		}
		return null;
	}
	
	private void shuffleArray()
	{
	    int index, temp;
	    Player tempP;
	    Random random = new Random();
	    for (int i = players.length - 1; i > 0; i--)
	    {
	        index = random.nextInt(i + 1);
	        tempP = this.players[index];
	        this.players[index] = this.players[i];
	        this.players[i] = tempP;
	        temp = this.discipline[index];
	        this.discipline[index] = this.discipline[i];
	        this.discipline[i] = temp;
	    }
	}

}


