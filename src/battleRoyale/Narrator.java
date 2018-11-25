package battleRoyale;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.CopyOnWriteArrayList;

public final class Narrator 
{
	public static CopyOnWriteArrayList<Player> watchlist;
	public static int minPriority;
	private static SimpleDateFormat sdf = new SimpleDateFormat();
	
	public static void sayDayHour(int day, int hour) 
	{
		System.out.printf("\nGIORNO: %d,ORE %d:00\n",day,hour);
	}
	
	public static void narratePlayersRemaing(int players)
	{
		System.out.printf("\nGIOCATORI RIMANENTI: %d",players);
	}
	
	public static void narratePreFight(Player[] players,Location loc)
	{
		for(Player p : players)
		{	
			if(p.getArmour() != null)
			{
				System.out.printf("\n %s (HP: %d ,ARMOUR: %s)",p.getName(),p.getHP(),p.getArmour().getName());
			}
			else
			{
				System.out.printf("\n %s (HP: %d)",p.getName(),p.getHP());
			}
		}
		System.out.printf("\niniziano a combattere %s %s! BUONA FORTUNA!",loc.getPreposition(),loc.getName());
	}
	
	public static void narratePostFight()
	{
		System.out.printf("\n");
	}
	
	
	public static void narrateHealing(Player player, Potion potion, int HPprec)
	{
		System.out.printf("\n%s (%d HP) ha usato %s, recuperando %d HP",player.getName(),player.getHP(),potion.getName(),player.getHP() - HPprec);
	}
	
	public static void narratePoison(CopyOnWriteArrayList<Player> players)
	{
		boolean damageFromPoison = false;

		System.out.printf("\n DANNI DA VELENO: ");
		for(Player player : players)
		{
			
			if(player.getPoisonAmount() > 0)
			{
				System.out.printf("\n%s (HP %d, DANNO: %d HP)", player.getName().toUpperCase()
						, player.getHP() - player.getPoisonAmount()> 0 ? player.getHP() - player.getPoisonAmount() : 0
						, player.getPoisonAmount());
				damageFromPoison = true;
			}
			
		}
		if(damageFromPoison == false)
		{
			System.out.printf("(nessuno)");
		}
		for(Player player : players)
		{
			if(player.getPoisonAmount() > 0 && player.getHP() == 0)
			{
				System.out.printf("\n%s è morto di avvelenamento(DANNO: %d HP)", player.getName().toUpperCase(),player.getPoisonAmount());
			}
		}

	}
	
	public static void narrateFight(FightResult res)
	{
		
		if(res.fightStatus == Fight.STATUS_ESCAPE)
		{
			System.out.printf("\n%s e' fuggito, gli rimangono %d HP",res.playerHitter.getName(),res.playerHitter.getHP());
		}
		else if(res.fightStatus == Fight.STATUS_STILL_FIGHTING)
		{
			if(res.damageStats.typeOfDamage == HitStats.DAMAGE_CRITICAL_HIT)
			{
				System.out.printf("\n%s (%d HP) ha colpito MOLTO FORTE %s (%d HP) %s %s, (Danno: %d HP)\n",res.playerHitter.getName(),res.playerHitter.getHP(),
						res.playerToHit.getName(),res.playerToHit.getHP(),res.weaponUsed.getPrefix(),res.weaponUsed.getName(),res.damageStats.damageDealt);
			}
			else if(res.damageStats.typeOfDamage == HitStats.DAMAGE_FULL_HIT)
			{
				System.out.printf("\n%s (%d HP) ha colpito %s (%d HP) %s %s, (Danno: %d HP)",res.playerHitter.getName(),res.playerHitter.getHP(),
						res.playerToHit.getName(),res.playerToHit.getHP(),res.weaponUsed.getPrefix(),res.weaponUsed.getName(),res.damageStats.damageDealt);
			}
			else if(res.damageStats.typeOfDamage == HitStats.DAMAGE_PARTIAL_HIT)
			{
				System.out.printf("\n%s (%d HP) ha colpito di striscio %s (%d HP) %s %s, (Danno: %d HP)",res.playerHitter.getName(),res.playerHitter.getHP(),
						res.playerToHit.getName(),res.playerToHit.getHP(),res.weaponUsed.getPrefix(),res.weaponUsed.getName(),res.damageStats.damageDealt);
			}
			else
			{
				System.out.printf("\n%s (%d HP) ha provato a colpire %s (%d HP) %s %s",res.playerHitter.getName(),res.playerHitter.getHP(),
						res.playerToHit.getName(),res.playerToHit.getHP(),res.weaponUsed.getPrefix(),res.weaponUsed.getName());
			}

			
		}
		else
		{
			System.out.printf("\n%s (%d HP) ha ucciso %s (%d HP) %s %s, %s %s (Danno: %d HP)",res.playerHitter.getName(),res.playerHitter.getHP(),
					res.playerToHit.getName(),res.playerToHit.getHP(),res.weaponUsed.getPrefix(),res.weaponUsed.getName(),res.location.getPreposition(),res.location.getName(),res.damageStats.damageDealt);
		}
	}
	
	public static void narrateUpgradeWeapon(Player player,Weapon weaponOld, Weapon weaponNew)
	{
		System.out.printf("\n%s ha potenziato \"%s \" in \"%s \" (LIVELLO %d) \n",player.getName().toUpperCase(),weaponOld.getName(),weaponNew.getName().toUpperCase(),
				weaponNew.getLevel());
		System.out.printf("ARMI: [%s(%d) |%s(%d) ]\n",player.getPrimaryWeapon().getName(),player.getPrimaryWeapon().getLevel(),
				player.getSecondaryWeapon().getName(),player.getSecondaryWeapon().getLevel());
	}
	public static void narrateEquipArmour(Player player,Armour armour)
	{
		System.out.printf("\n%s ha equipaggiato \"%s \" (HEALTH:%d) , \n",player.getName().toUpperCase(),armour.getName().toUpperCase(),armour.getMaxHealth());
	}
	public static void narrateEquipPotion(Player player,Potion potion)
	{
		System.out.printf("\n%s ha raccolto \"%s \" (HEAL:%d/%d) , \n",player.getName().toUpperCase(),potion.getName(),potion.getHealing(),potion.getHealthCap());
	}
	public static void narrateFindWeapon(Player player,Weapon weapon)
	{
		System.out.printf("\n%s ha trovato \"%s \" (LIVELLO %d) \n",player.getName().toUpperCase(),weapon.getName().toUpperCase(),weapon.getLevel(),player.getPrimaryWeapon().getName(),
				player.getSecondaryWeapon().getName());
		System.out.printf("ARMI: [%s (%d) |%s (%d) ]\n",player.getPrimaryWeapon().getName(),player.getPrimaryWeapon().getLevel(),
				player.getSecondaryWeapon().getName(),player.getSecondaryWeapon().getLevel());
	}

}
