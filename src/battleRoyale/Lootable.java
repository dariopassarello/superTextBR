package battleRoyale;

import java.util.concurrent.CopyOnWriteArrayList;

public interface Lootable 
{
	public abstract int getMetric();
	
	public static CopyOnWriteArrayList<Weapon> getWeapons(CopyOnWriteArrayList<Lootable> lootables)
	{
		CopyOnWriteArrayList<Weapon> wp = new CopyOnWriteArrayList<Weapon>();
		for(Lootable lootable : lootables)
		{
			if(lootable instanceof Weapon)
			{
				wp.add((Weapon) lootable);
			}
		}
		return wp;
	}
	public static CopyOnWriteArrayList<Potion> getPotions(CopyOnWriteArrayList<Lootable> lootables)
	{
		CopyOnWriteArrayList<Potion> wp = new CopyOnWriteArrayList<Potion>();
		for(Lootable lootable : lootables)
		{
			if(lootable instanceof Potion)
			{
				wp.add((Potion) lootable);
			}
		}
		return wp;
	}
	public static CopyOnWriteArrayList<Armour> getArmours(CopyOnWriteArrayList<Lootable> lootables)
	{
		CopyOnWriteArrayList<Armour> wp = new CopyOnWriteArrayList<Armour>();
		for(Lootable lootable : lootables)
		{
			if(lootable instanceof Armour)
			{
				wp.add((Armour) lootable);
			}
		}
		return wp;
	}
}
