package me.dariopassarello.battleroyale.lootables;

public class Armour implements Lootable
{
	//NAME
	private String name;
	//STATS
	private float damageProtection; //Less than 1
	private int maxHealth;
	private int HP;

	public static final int PERFORATING_PROTECTION_REDUCTION = 2;

	public Armour(String name, float damageProtection, int maxHealth) 
	{
		
		this.name = name;
		this.damageProtection = damageProtection;
		this.maxHealth = maxHealth;
		this.HP = maxHealth;
	}
	
	public int attackAndCalculateTrueDamage(int attackDamage,boolean perforating)
	{
		int trueDamage, armourDamage;
		if(this.HP > 0)
		{
			this.HP--;
			if(perforating)
			{
				 damageProtection = damageProtection / Armour.PERFORATING_PROTECTION_REDUCTION;
			}
			return (int)Math.ceil((float)attackDamage*(1 - this.damageProtection));
		}
		else
		{
			return attackDamage;
		}
	}
	
	public Armour getNewArmour()
	{
		return new Armour(this.name, this.damageProtection, this.maxHealth);
	}

	public int getMetric() 
	{
		return (int) ((this.damageProtection*this.HP));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getDamageProtection() {
		return damageProtection;
	}

	public void setDamageProtection(float damageProtection) {
		this.damageProtection = damageProtection;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public int getHP() {
		return HP;
	}

	public void setHP(int hP) {
		HP = hP;
	}
	
	

}
