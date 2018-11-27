package battleRoyale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Fight
{
	
	private Player players[];
	private Location location;
	private int turn;
	private Player targetPlayer;
	private int targetPlayer_previousHP;
	private Player activePlayer;
	private int status;
	private Weapon weaponUsed;
	private HitStats hit;

	public static final int STATUS_STILL_FIGHTING = 0;
	public static final int STATUS_STEALTH_KILL = 1;
	public static final int STATUS_KILL = 2;
	public static final int STATUS_ESCAPE = 3;
	public static final int STATUS_ESCAPE_FAILED = 4;
	
	private static final int USE_WEAKEST_WEAPON_CHANCE = 15;
	private static final float STEALTH_FIRST_DAMAGE_MULTIPLIER = 2.0f;
	private static final float STEALTH_FIRST_HIT_RATE_MULTIPLIER = 2.0f;

	private static final int ESCAPE_FAIL_RATE = 30;
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

	public HitStats getHit() {
		return hit;
	}

	public Player getTargetPlayer() {
		return targetPlayer;
	}

	public void setTargetPlayer(Player targetPlayer) {
		this.targetPlayer = targetPlayer;
	}

	public Player getActivePlayer() {
		return activePlayer;
	}

	public void setActivePlayer(Player activePlayer) {
		this.activePlayer = activePlayer;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Weapon getWeaponUsed() {
		return weaponUsed;
	}

	public void setWeaponUsed(Weapon weaponUsed) {
		this.weaponUsed = weaponUsed;
	}

	public void setHit(HitStats hit) {
		this.hit = hit;
	}

	public int getTargetPlayer_previousHP() {
		return targetPlayer_previousHP;
	}

	public void setTargetPlayer_previousHP(int targetPlayer_previousHP) {
		this.targetPlayer_previousHP = targetPlayer_previousHP;
	}

	private void updateDiscipline(int round)
	{
		//Base discipline
		int maxHP = 0;
		int i = 0;
		for(Player p : this.players)
		{
			p.setDiscipline(Fight.BASE_ESCAPE_SCORE + p.getHP() + Fight.ROUND_ESCAPE_BONUS * (round - 4)); //Calcola la disciplina di base
			if(i == 0) p.setDiscipline(p.getDiscipline() + Fight.FIGHT_STARTER_DISCIPLINE_BONUS); //Il giocatore che attaca per primo fugge piu difficilmente
			if(p.isDisarmed()) p.setDiscipline(p.getDiscipline() + Fight.DISARMED_ESCAPE_BONUS); //Se il giocatore è disarmato fugge piu facikente
			if(p.getHP() > maxHP) //Calcola il giocatore con piu HP
			{
				maxHP = p.getHP();
			}
			i++;
		}
		for(Player p : this.players)
		{
			if(p.getHP() >= maxHP) p.setDiscipline(p.getDiscipline() + Fight.HEALTIEST_DISCIPLINE_BONUS); //Aggiungi bonus per il giocatore con piu HP (fugge + difficilmente)
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

	public int nextHit()
	{
		//Numero giocatore che attacca/fugge in questo turno
		int activePlayerNum = this.turn % players.length;
		//Numero round (numero di volte in cui i giocatori hanno attaccato almeno una volta)
		int round = this.turn / players.length;
		//Se il giocatore riesce a scappare escape = true
		boolean escape;
		int playerToHitNumber;
		float damageMultiplier = 1.0f;
		float hitMultiplier = 1.0f;


		this.activePlayer = this.players[activePlayerNum];

		if(this.players.length > 1)
		{
			if(round > 0 && activePlayerNum == 0)
			{
				this.shuffleArray();
			}
			if(this.turn > 1)
			{
				this.updateDiscipline(round);
				escape = this.tryEscapeFight(this.activePlayer.getDiscipline());
				if(escape == true)
				{
					if(RandomManager.isInRandomRange(0,100,0,Fight.ESCAPE_FAIL_RATE))
					{
						this.status = Fight.STATUS_ESCAPE_FAILED;
						return 0;
					}
					else
					{
						this.status = Fight.STATUS_ESCAPE;
						this.activePlayer.setDisarmed(false);
						this.removePlayerFromFight(activePlayer);
						return 0;
					}

				}
			}
			//Sorteggia l'arma
			//Se al turno 0 il primo giocatore ha un arma stealth attacca con quella facendo piu danno se colpisce
			if(this.activePlayer.getMaxWeaponTier() == 0 || this.activePlayer.isDisarmed())
			{
				this.weaponUsed = Weapon.randomWeapon(0,0,true);
			}
			else
			{
				//Se il giocatore � disarmato o non ha armi di livello >0. Utilizza un arma di livello 0 (corpo a corpo)
				if(this.turn == 0 && (this.activePlayer.getPrimaryWeapon().isStealth() || this.activePlayer.getSecondaryWeapon().isStealth()))
				{
					if(this.activePlayer.getPrimaryWeapon().isStealth())
					{
						this.weaponUsed = this.activePlayer.getPrimaryWeapon();
					}
					else
					{
						this.weaponUsed = this.activePlayer.getSecondaryWeapon();
					}
					damageMultiplier = Fight.STEALTH_FIRST_DAMAGE_MULTIPLIER;
					hitMultiplier = Fight.STEALTH_FIRST_HIT_RATE_MULTIPLIER;
				}
				else
				{
					//Utilizza con molta probabilita l'arma di livello piu alto
					Weapon bestWeapon = Weapon.bestWeapon(this.activePlayer.getPrimaryWeapon(), this.activePlayer.getSecondaryWeapon());
					Weapon worstWeapon = Weapon.worstWeapon(this.activePlayer.getPrimaryWeapon(), this.activePlayer.getSecondaryWeapon());
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
				playerToHitNumber = RandomManager.randomRange(0,this.getNumberOfPlayers());
				this.targetPlayer = this.players[playerToHitNumber];

			}
			while(this.targetPlayer.equals(this.activePlayer) && this.getNumberOfPlayers() > 1);

			this.targetPlayer = this.players[playerToHitNumber];
			this.targetPlayer_previousHP = this.targetPlayer.getHP();
			HitStats damageStats = weaponUsed.tryHit(this.targetPlayer,damageMultiplier,hitMultiplier); //hit the player
			this.hit = damageStats;
			this.activePlayer.addDamageDealt(damageStats.damageDealt);
			//Se colpisci critico disarma l'avversario
			if(damageStats.typeOfDamage == HitStats.DAMAGE_CRITICAL_HIT)
			{
				targetPlayer.setDisarmed(true);
			}
			//Crea il resoconto del fight per il narratore
			if(targetPlayer.isAlive() == true)
			{
				this.status = STATUS_STILL_FIGHTING;
			}
			else if(targetPlayer.isAlive() == false && this.turn > 0)
			{
				this.status = STATUS_KILL;
				this.activePlayer.addKill();
				this.removePlayerFromFight(this.targetPlayer);
			}
			else
			{
				this.status = STATUS_STEALTH_KILL;
				this.activePlayer.addKill();
				this.removePlayerFromFight(targetPlayer);
			}
			if(this.players.length == 1)
			{
				this.players[0].setDisarmed(false);
			}
			//Incrementa numero turno
			this.turn++;
			return 0;
		}
		return 1;
	}

	public Player[] getPlayers()
	{
		return this.players;
	}

	public int getNumberOfPlayers()
	{
		return this.players.length;
	}

	private void shuffleArray()
	{
	    int index;
	    Player tempP;
	    Random random = new Random();
	    for (int i = players.length - 1; i > 0; i--)
	    {
	        index = random.nextInt(i + 1);
	        tempP = this.players[index];
	        this.players[index] = this.players[i];
	        this.players[i] = tempP;
	    }
	}

}


