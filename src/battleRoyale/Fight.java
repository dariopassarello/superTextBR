package battleRoyale;

import java.util.Random;

public class Fight
{
	private daryx77.superTextBattleRoyale.mainPackage.Player players[];
	private int discipline[];
	private Location location;
	private int turn;
	private int player1Discipline;
	private int player2Discipline; //The least is this value the more is the chance to escape the fight
	
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
			this.discipline[i] = Fight.BASE_ESCAPE_SCORE + players[i].getHP() + Fight.ROUND_ESCAPE_BONUS * round;
			if(i == 0) discipline[i] += Fight.FIGHT_STARTER_DISCIPLINE_BONUS;
			if(players[i].isUnarmed()) this.discipline[i] += Fight.DISARMED_ESCAPE_BONUS;
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

	public FightResult nextHit()
	{
		//Numero giocatore che attacca/fugge in questo turno
		int numPlayerTurn = this.turn % players.length;
		//Numero round (numero di volte in cui i giocatori hanno attaccato almeno una volta)
		int round = this.turn / players.length;
		//Se il giocatore riesce a scappare escape = true
		boolean escape;
		boolean unarmingInThisRound = false;
		float damageMultiplier = 1.0f;
		float hitMultiplier = 1.0f;
		int playerAttacker;
		Weapon weaponUsed;
		Player playerToHit;
		Player playerHitter;
		FightResult res;

		//
		if(this.turn > 1)
		{
			this.updateDiscipline();
			escape = this.tryEscapeFight(discipline[numPlayerTurn]);
			
			if(escape == true)
			{
				
				res = new FightResult(playerToHit,playerHitter,null,null,Fight.STATUS_ESCAPE,this.location);
				return res;
			}
		}
		//Sorteggia l'arma
		//Se al turno 0 il giocatore 1 ha un arma stealth attacca con quella
		if(playerHitter.getMaxWeaponTier() == 0 || playerHitter.isDisarmed())
		{
			weaponUsed = Weapon.randomWeapon(0,0,true);
		}
		else
		{
			if(this.turn == 0 && this.players[1].getSecondaryWeapon().isStealth() == true)
			{
				weaponUsed = this.players[i].getSecondaryWeapon();
				damageMultiplier = Fight.STEALTH_FIRST_HIT_RATE_MULTIPLIER;
			}
			else
			{
				
				
			}
		}
		HitStats damageStats = weaponUsed.tryHit(playerToHit,damageMultiplier,hitMultiplier); //hit the player
		playerHitter.addDamageDealt(damageStats.damageDealt);
		if(damageStats.typeOfDamage == HitStats.DAMAGE_CRITICAL_HIT)
		{
			playerToHit.setUnarmed(true);
			unarmingInThisRound = true;
		}
		if(playerToHit.isAlive() == true)
		{
			res = new FightResult(playerToHit,playerHitter,damageStats,weaponUsed,Fight.STATUS_STILL_FIGHTING,this.location);
			
		}
		else if(playerToHit.isAlive() == false && this.turn > 0)
		{
			res = new FightResult(playerToHit,playerHitter,damageStats,weaponUsed,Fight.STATUS_KILL,this.location);
			playerHitter.addKill();
		}
		else
		{
			res = new FightResult(playerToHit,playerHitter,damageStats,weaponUsed,Fight.STATUS_STEALTH_KILL,this.location);
			playerHitter.addKill();
		}
		this.turn++;
		if(unarmingInThisRound == true)
		{
			res.unarmed = true;
		}
		return res;

	}

}


