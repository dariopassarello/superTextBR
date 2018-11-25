package battleRoyale;

public class Potion implements Lootable
{
	//NAME
	private String name;
	//STATS
	private int healing;
	private int healthCap;
	public Potion(String name, int healing, int healthCap) 
	{
		super();
		this.name = name;
		this.healing = healing;
		this.healthCap = healthCap;
	}
	public void healPlayer(Player player)
	{
		int HP = player.getHP();
		int effectiveHealing = this.healing;
		if(HP + this.healing > this.healthCap)
		{
			effectiveHealing = HP + this.healing - this.healthCap;
		}
		player.setPoisonAmount(0);
		player.heal(effectiveHealing);
		
	}
	
	
	
	public String getName() 
	{
		return name;
	}
	public void setName(String name) 
	{
		this.name = name;
	}
	public int getHealing() 
	{
		return healing;
	}
	public void setHealing(int healing) 
	{
		this.healing = healing;
	}
	public int getHealthCap() 
	{
		return healthCap;
	}
	public void setHealthCap(int healthCap) 
	{
		this.healthCap = healthCap;
	}
	@Override
	public int getMetric() 
	{
		return this.healing + this.healthCap;
	}
	
	
}
