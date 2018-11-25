package battleRoyale;

public class Armour implements Lootable
{
	//NAME
	private String name;
	//STATS
	private float damageProtection; //Less than 1
	private int maxHealth;
	private int HP;
	public Armour(String name, float damageProtection, int maxHealth) 
	{
		
		this.name = name;
		this.damageProtection = damageProtection;
		this.maxHealth = maxHealth;
		this.HP = maxHealth;
	}
	
	public int attackAndCalculateTrueDamage(int attackDamage)
	{
		int trueDamage, armourDamage;
		if(this.HP > 0)
		{
			armourDamage = (int) Math.ceil(attackDamage*(1 - damageProtection));
			trueDamage = attackDamage - armourDamage;
			if(armourDamage < this.HP)
			{
				this.HP -= armourDamage;
				
			}
			else
			{
				trueDamage += armourDamage - this.HP;
				this.HP = 0;
			}
			return trueDamage;
				
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
