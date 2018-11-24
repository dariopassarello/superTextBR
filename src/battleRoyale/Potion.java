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
		player.heal(effectiveHealing);
		
	}
	@Override
	public int getMetric() 
	{
		return this.healing + this.healthCap;
	}
	
	
}
