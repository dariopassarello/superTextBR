package battleRoyale;
import java.util.ArrayList;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;




public class Weapon implements Lootable
{
	protected ArrayList<Integer> weaponAttributes;
	protected int level;
	protected int hitRate;
	protected int maxDamage;
	protected String prefix;
	protected String name;
	protected Weapon baseWeapon;
	
	public static final int TYPE_GENERIC = 0;
	public static final int TYPE_STEALTH = 1;
	public static final int TYPE_GUN = 2;
	public static final int TYPE_MAGIC = 3;
	public static final int TYPE_PSYCO = 4;
	public static final int TYPE_POISONUS = 5;
	public static final int TYPE_PERFORATING = 6;
	//Per calcolare il danno genero numero da 0-99, Lancio = 0-40, Damage = (60 + Lancio)*Max Damage
	protected static final int FULL_HIT_TRESH = 40; //40-89 Damage = Max Damage
	protected static final int CRITICAL_HIT_TRESH = 90; //90-99 Damage = CRITICAL_MULTIPLIER*Max Damage
	protected static final float CRITICAL_MULTIPLIER = 2.0f;
	protected static final int DAMAGE_LEVEL[] = {25,35,50,65,75,85,95,105,120};
	protected static final int HIT_RATE_LEVEL[] = {72,75,78,81,84,87,90,93,95};
	protected static final int POISON_AMOUNT[] = {0,1,2,2,3,5,7,9};
	
	
	public static CopyOnWriteArrayList<Weapon> weaponList; //Lista statica di tutte le armi

	/**
	 * 
	 * @param prefix The article for the weapon when is printed to screen
	 * @param name The name of the weapon
	 * @param types The attributes for the weapon
	 * @param level The level of the weapon
	 * @param baseWeapon The "father weapon"
	 */
	public Weapon(String prefix, String name,int types[],int level,Weapon baseWeapon)
	{
		this.prefix = prefix;
		this.name = name;
		this.weaponAttributes = new ArrayList<Integer>(); 
		if(types != null)
		{
			for(int attribute : types)
			{
				if(!this.weaponAttributes.contains(attribute)) weaponAttributes.add(attribute);
			}
		}

		this.hitRate = HIT_RATE_LEVEL[this.level];
		this.maxDamage = DAMAGE_LEVEL[this.level];
		this.level = level;
		this.baseWeapon = baseWeapon;
	}

	public boolean isStealth() 
	{
		return this.weaponAttributes.contains(Weapon.TYPE_STEALTH);
	}
	
	public ArrayList<Integer> getWeaponAttributesArray() 
	{
		return this.weaponAttributes;
	}
	
	public boolean hasAttribute(int attribute)
	{
		return weaponAttributes.contains(attribute);
	}
	
	public int getHitRate() 
	{
		return hitRate;
	}

	public void setHitRate(int hitRate) 
	{
		this.hitRate = hitRate;
	}

	public int getMaxDamage() 
	{
		return maxDamage;
	}

	public void setMaxDamage(int maxDamage) 
	{
		this.maxDamage = maxDamage;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}
	
	public String getPrefix() 
	{
		return prefix;
	}

	public void setPrefix(String prefix) 
	{
		this.prefix = prefix;
	}
	
	public int getTier() 
	{
		return level;
	}

	public void setTier(int tier) 
	{
		this.level = tier;
	}

	public HitStats tryHit(Player player)
	{
		return tryHit(player,1.0f,1.0f);
	}
	
	public int getLevel()
	{
		return this.level;
	}
	
	
	
	public Weapon getBaseWeapon() 
	{
		return baseWeapon;
	}
	public void setBaseWeapon(Weapon baseWeapon) {
		this.baseWeapon = baseWeapon;
	}
	public HitStats tryHit(Player player,float damageMultiplier, float hitMultiplier)
	{
		Random rand = new Random();
		int hitRes = rand.nextInt(100);
		int damageDealt;
		int typeOfDamage = HitStats.DAMAGE_MISS;
		if((int)(hitMultiplier*hitRes) > this.hitRate)
		{
			//hit failed
			damageDealt = 0;
		}
		else
		{
			//0/39 - Partial Hit
			//40/95 - Total Hit
			//91/99 - Critical Hit (*2 Damage)	
			int hit = rand.nextInt(100);
			
			if(hit < Weapon.FULL_HIT_TRESH)
			{
				float damage = ((100 - Weapon.FULL_HIT_TRESH)*this.maxDamage)/100f;
				damageDealt = (int) damage;
				typeOfDamage = HitStats.DAMAGE_PARTIAL_HIT;
			}
			else if(hit < Weapon.CRITICAL_HIT_TRESH)
			{
				damageDealt = this.maxDamage;
				typeOfDamage = HitStats.DAMAGE_FULL_HIT;
			}
			else
			{
				damageDealt = (int) (this.maxDamage*Weapon.CRITICAL_MULTIPLIER);
				typeOfDamage = HitStats.DAMAGE_CRITICAL_HIT;
			}
		}
		int damageCorrected = (int) (damageDealt*damageMultiplier);
		
		int trueDamage = player.attack(damageCorrected,this.weaponAttributes.contains(Weapon.TYPE_PERFORATING));
		if(damageCorrected > 0 && trueDamage > 0 && this.weaponAttributes.contains(Weapon.TYPE_POISONUS));
		{
			player.setPoisonAmount(Weapon.POISON_AMOUNT[this.level]);
		}
		return new HitStats(trueDamage,typeOfDamage);
	}
	
	
	

	public static Weapon randomWeapon(int minLevel, int maxLevel, boolean onlyBaseWeapon)
	{
		Random rand = new Random();
		Weapon aWeapon;
		do
		{
			aWeapon = weaponList.get(rand.nextInt(Weapon.weaponList.size()));
		}
		while((onlyBaseWeapon == true && aWeapon.baseWeapon != null)||(aWeapon.level < minLevel || aWeapon.level > maxLevel)); 
		
		
		return aWeapon;
	}
	


	
	public Weapon getLevelUpWeapon()
	{
		Weapon candidate;
		for(int i = 0; i < Weapon.weaponList.size(); i++)
		{
			candidate = Weapon.weaponList.get(i);
			//System.out.printf("\nthis:%s, this father: %s,candidate: %s, candidate father: %s",this.getName(),this.getBaseWeapon()!= null ? this.getBaseWeapon().getName() : "null",candidate.getName(),candidate.getBaseWeapon()!= null ? candidate.getBaseWeapon().getName() : "null");
			if(candidate.baseWeapon != null && (candidate.baseWeapon == this ||  candidate.baseWeapon == this.baseWeapon) && candidate.level == this.level + 1)
			{
				return candidate;
			}
		}
		return null;
	}
	/***
	 * Search for an upgraded version of this weapon in Weapon.weaponlist
	 * @return Returns true if exists The UpgradedWeapon that is one level up than this weapon else returns false
	 */
	public boolean isUpgradable()
	{
		if(getLevelUpWeapon() == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public int getMeanDamage()
	{
		return this.maxDamage*this.hitRate;
	}
	
	public static Weapon bestWeapon(Weapon weap1, Weapon weap2)
	{
		Weapon best;
		if(weap1.getTier() >= weap2.getTier())
		{
			best = weap1;
		}
		else
		{
			best = weap2;
		}

		return best;
	}
	public static Weapon worstWeapon(Weapon weap1, Weapon weap2)
	{
		Weapon worst;
		if(weap1.getTier() < weap2.getTier())
		{
			worst = weap1;
		}
		else
		{
			worst = weap2;
		}
		return worst;
	}
	@Override
	
	public int getMetric() 
	{
		return this.level;
	}
	
	
}
