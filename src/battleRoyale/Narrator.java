package battleRoyale;

public class Narrator 
{
	public static void narratePreFight(Player p1,Player p2,Location loc)
	{
		System.out.printf("\n%s attacca %s %s %s\n",p1.getName().toUpperCase(),p2.getName().toUpperCase(),loc.getPreposition(),loc.getName());
	}
	
	public static void narratePoison(Player player)
	{
		
		if(player.getHP() > 0)
		{
			System.out.printf("\n%s (HP %d) � stato danneggiato dal veleno (DANNO: %d HP)", player.getName(), player.getHP(), player.getPoisonAmount());
		}
		else
		{
			System.out.printf("\n%s � stato ucciso dal veleno (DANNO: %d HP)", player.getName(), player.getPoisonAmount());
		}
	}
	
	public static void narrateFight(FightResult res)
	{
		
		if(res.fightStatus == Fight.STATUS_ESCAPE)
		{
			System.out.printf("\n%s � fuggito da %s, gli rimangono %d HP\n\n",res.playerHitter.getName(),res.playerToHit.getName(),res.playerHitter.getHP());
		}
		else if(res.fightStatus == Fight.STATUS_STILL_FIGHTING)
		{
			if(res.damageStats.typeOfDamage == HitStats.DAMAGE_CRITICAL_HIT)
			{
				System.out.printf("\n%s (%d HP) ha colpito MOLTO FORTE %s (%d HP) %s%s, (Danno: %d HP)\n",res.playerHitter.getName(),res.playerHitter.getHP(),
						res.playerToHit.getName(),res.playerToHit.getHP(),res.weaponUsed.getPrefix(),res.weaponUsed.getName(),res.damageStats.damageDealt);
			}
			else if(res.damageStats.typeOfDamage == HitStats.DAMAGE_FULL_HIT)
			{
				System.out.printf("\n%s (%d HP) ha colpito %s (%d HP) %s%s, (Danno: %d HP)",res.playerHitter.getName(),res.playerHitter.getHP(),
						res.playerToHit.getName(),res.playerToHit.getHP(),res.weaponUsed.getPrefix(),res.weaponUsed.getName(),res.damageStats.damageDealt);
			}
			else if(res.damageStats.typeOfDamage == HitStats.DAMAGE_PARTIAL_HIT)
			{
				System.out.printf("\n%s (%d HP) ha colpito di striscio %s (%d HP) %s%s, (Danno: %d HP)",res.playerHitter.getName(),res.playerHitter.getHP(),
						res.playerToHit.getName(),res.playerToHit.getHP(),res.weaponUsed.getPrefix(),res.weaponUsed.getName(),res.damageStats.damageDealt);
			}
			else
			{
				System.out.printf("\n%s (%d HP) ha provato a colpire %s (%d HP) %s%s",res.playerHitter.getName(),res.playerHitter.getHP(),
						res.playerToHit.getName(),res.playerToHit.getHP(),res.weaponUsed.getPrefix(),res.weaponUsed.getName());
			}

			
		}
		else
		{
			System.out.printf("\n%s (%d HP) ha ucciso %s (%d HP) %s%s, %s %s (Danno: %d HP)",res.playerHitter.getName(),res.playerHitter.getHP(),
					res.playerToHit.getName(),res.playerToHit.getHP(),res.weaponUsed.getPrefix(),res.weaponUsed.getName(),res.location.getPreposition(),res.location.getName(),res.damageStats.damageDealt);
		}
		pressAnyKeyToContinue();
	}
	
	public static void narrateUpgradeWeapon(Player player,Weapon weaponOld, Weapon weaponNew)
	{
		System.out.printf("\n%s ha potenziato \"%s \" in \"%s \" (LIVELLO %d) \n",player.getName().toUpperCase(),weaponOld.getName(),weaponNew.getName().toUpperCase(),
				weaponNew.getLevel());
		System.out.printf("ARMI: [%s(%d) |%s(%d) ]\n",player.getPrimaryWeapon().getName(),player.getPrimaryWeapon().getLevel(),
				player.getSecondaryWeapon().getName(),player.getSecondaryWeapon().getLevel());
		pressAnyKeyToContinue();
	}
	public static void narrateFindWeapon(Player player,Weapon weapon)
	{
		System.out.printf("\n%s ha trovato \"%s \" (LIVELLO %d) \n",player.getName().toUpperCase(),weapon.getName().toUpperCase(),weapon.getLevel(),player.getPrimaryWeapon().getName(),
				player.getSecondaryWeapon().getName());
		System.out.printf("ARMI: [%s (%d) |%s (%d) ]\n",player.getPrimaryWeapon().getName(),player.getPrimaryWeapon().getLevel(),
				player.getSecondaryWeapon().getName(),player.getSecondaryWeapon().getLevel());
		pressAnyKeyToContinue();
	}
	 private static
	 void pressAnyKeyToContinue()
	 { 
	        //
		 
		 //System.out.println("Press Enter key to continue...");
	        try
	        {
	            System.in.read();
	        }  
	        catch(Exception e)
	        {}  
	 }
}
