package battleRoyale;

import java.util.Comparator;

public class Player
{
	//NAME
	private String name;
	//EQUIPMENT
	private Weapon primaryWeapon; 
	private Weapon secondaryWeapon;
	private Armour armour;
	private Potion potion;
	//STATUS
	private int HP;
	private int poisonAmount;
	private boolean disarmed;
	private int discipline;
	//STATS
	private int kills;
	private int damageDealt;

	private static final int MAX_HP = 100;


	public Player(String name)
	{
		this.name = name;
		this.HP = 100;
		this.primaryWeapon = new Weapon("", "(Slot vuoto)", null, 0, null);
		this.secondaryWeapon = new Weapon("", "(Slot vuoto)", null, 0, null);
		this.kills = 0;
		this.damageDealt = 0;
		this.poisonAmount = 0;
		this.disarmed = false;
		this.discipline = 0;
		this.potion = null;
	}


	static class KillComparator implements Comparator<Player>
	{
		public int compare(Player p1,Player p2)
		{
			if(p1.getKills() == p2.getKills())
			{
				if(p1.getDamageDealt() == p2.getDamageDealt())
				{
					return 0;
				}
				else if(p1.getDamageDealt() > p2.getDamageDealt())
				{
					return 1;
				}
				else
				{
					return -1;
				}

			}
			else if(p1.getKills() > p2.getKills())
			{
				return 1;
			}
			else return -1;

		}
	}
	public String getName()
	{
		return this.name;
	}



	public Weapon getPrimaryWeapon() {
		return primaryWeapon;
	}

	public void setPrimaryWeapon(Weapon primaryWeapon) {
		this.primaryWeapon = primaryWeapon;
	}

	public Weapon getSecondaryWeapon() {
		return secondaryWeapon;
	}

	public void setSecondaryWeapon(Weapon secondaryWeapon) {
		this.secondaryWeapon = secondaryWeapon;
	}

	public int attack(int damage,boolean perforating)
	{
		int trueDamage = damage;
		if(this.armour != null && perforating == false)
		{
			trueDamage = this.armour.attackAndCalculateTrueDamage(damage);
		}
		this.HP -= trueDamage;
		if(this.HP < 0) this.HP = 0;
		return trueDamage;
	}
	
	public void heal(int healAmount)
	{
		this.poisonAmount = 0;
		this.HP += healAmount;
		if(this.HP > Player.MAX_HP)
		{
			this.HP = Player.MAX_HP;
		}
	}
	
	public void updateHP()
	{
		this.HP -= this.poisonAmount;
		if(this.HP <= 0)
		{
			this.HP = 0;
			this.poisonAmount = 0;
		}
		else
		{
			if(this.potion != null)
			{
				if(this.HP < 20 || (this.poisonAmount > 0) || this.HP + this.potion.getHealing() - this.potion.getHealthCap() < this.potion.getHealing() / 2)
				{
					Potion pot = this.potion;
					int HPprec = this.HP;
					this.usePotion();
					Narrator.narrateHealing(this, pot, HPprec);
					
				}
			}
		}

	}
	


	public int getHP()
	{
		return this.HP;
	}
	
	public boolean isAlive()
	{
		if(HP > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public int getMaxWeaponTier()
	{
		return Math.max(this.primaryWeapon.getLevel(), this.secondaryWeapon.getLevel());
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setHP(int HP)
	{
		if(HP > 0)
		{
			this.HP = HP;
		}
		else
		{
			this.HP = 0;
		}
	}
	
	
	public int getKills() 
	{
		return kills;
	}

	
	public void addKill() 
	{
		this.kills++;
	}

	public void addDamageDealt(int damageDealt) 
	{
		this.damageDealt += damageDealt;
	}

	public int getDamageDealt() 
	{
		return damageDealt;
	}
	
	public boolean isDisarmed() 
	{
		return disarmed;
	}

	public void setDisarmed(boolean disarmed) 
	{
		this.disarmed = disarmed;
	}


	public Armour getArmour() 
	{
		return armour;
	}


	public void setArmour(Armour armour) {
		this.armour = armour;
	}


	public int getPoisonAmount() 
	{
		return poisonAmount;
	}


	public void setPoisonAmount(int poisonAmount) 
	{
		this.poisonAmount = poisonAmount;
	}


	public Potion getPotion() 
	{
		return potion;
	}

	public int getDiscipline()
	{
		return discipline;
	}

	public void setDiscipline(int discipline)
	{
		this.discipline = discipline;
	}

	public void setPotion(Potion potion)
	{
		this.potion = potion;
	}
	
	public void usePotion()
	{
		if(this.potion != null)
		{
			this.potion.healPlayer(this);
			this.potion = null;
		}
	}
	
	

}
