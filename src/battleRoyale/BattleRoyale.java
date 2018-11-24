package battleRoyale;
import java.util.Random;

public class BattleRoyale extends GameFlow
{
	public void startBattleRoyale()
	{
		Random rand = new Random();
		while(this.survivors > 1)
		{
			int weap = rand.nextInt(100);
			int fight = rand.nextInt(100);
			int weapLUP = players.size() / MAX_LEVEL;
			//TODO implement level progressive weapos
			if(weap < GameFlow.OBJECT_CHANCE)
			{
				findOrUpgradeWeapon();
			}
			if(fight < GameFlow.FIGHT_CHANCE)
			{
				startFight(0,0);
			}
		}
	}
	
}
