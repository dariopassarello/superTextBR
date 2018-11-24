package battleRoyale;
@Deprecated
public class UpgradedWeapon extends Weapon 
{

	private Weapon baseWeapon;
	private int level;
	public UpgradedWeapon(Weapon baseWeapon,String prefix,String name,int hitRate,int maxDamage,int level)
	{
		super(prefix,name,baseWeapon.isStealth(),hitRate,maxDamage,
				(baseWeapon.getTier() + level - 1 < Weapon.MAX_TIER) ? baseWeapon.getTier() + level - 1 : Weapon.MAX_TIER);
		
		this.baseWeapon = baseWeapon;
		this.level = level;
	}
	
	/***
	 * Getter for BaseWeapon
	 * @return Returns the base weapon of this UpgradedWeapon
	 */
	public Weapon getBaseWeapon() 
	{
		return baseWeapon;
	}
	
	
	/***
	 * Getter for Weapon Level
	 * @return Returns the upgrade level of this UpgrededWeapon
	 */
	@Override
	public int getLevel() {
		return level;
	}
	
	/***
	 * Search for an upgraded version of this weapon in Weapon.weaponlist
	 * @return The UpgradedWeapon that is
	 */
	@Override
	public UpgradedWeapon getLevelUpWeapon()
	{
		
		for(int i = 0; i < Weapon.weaponList.size(); i++)
		{
			if(Weapon.weaponList.get(i) instanceof UpgradedWeapon)
			{
				UpgradedWeapon candidate = ((UpgradedWeapon)Weapon.weaponList.get(i));
				if(candidate.getBaseWeapon() == this.baseWeapon && candidate.getLevel() == this.level + 1)
				{
					return candidate;
				}
			}
		}
		return null;
	}
	
	public boolean isUpgradable()
	{
		
		for(int i = 0; i < Weapon.weaponList.size(); i++)
		{
			if(Weapon.weaponList.get(i) instanceof UpgradedWeapon)
			{
				UpgradedWeapon candidate = ((UpgradedWeapon)Weapon.weaponList.get(i));
				if(candidate.getBaseWeapon() == this.baseWeapon && candidate.getLevel() == this.level + 1)
				{
					return true;
				}
			}
		}
		return false;
	}


}
