package battleRoyale;
public class Player 
{
	//NAME
	private String name;
	//EQUIPMENT
	private daryx77.superTextBattleRoyale.mainPackage.Weapon primaryWeapon;
	private Weapon secondaryWeapon;
	private Armour armour;
	//STATUS
	private int HP;
	private int poisonAmount;
	private boolean disarmed;
	//STATS
	private int kills;
	private int damageDealt;

	private static final int MAX_HP = 100;


	public Player(String name)
	{
		this.name = name;
		this.HP = 100;
		this.primaryWeapon = Weapon.randomWeapon(0,0,true);
		this.secondaryWeapon = Weapon.randomWeapon(0,0,true);
		this.kills = 0;
		this.damageDealt = 0;
		this.poisonAmount = 0;
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
		if(this.HP < 0)
		{
			this.HP = 0;
		}
	}
	
	public String getName()
	{
		return this.name;
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
		return Math.max(this.primaryWeapon.getTier(), this.secondaryWeapon.getTier());
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
	
	

}
