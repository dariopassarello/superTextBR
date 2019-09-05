package battleRoyale;

import java.io.IOException;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameFlow
{
    private CopyOnWriteArrayList<Player> allPlayers = new CopyOnWriteArrayList<Player>();
    private CopyOnWriteArrayList<Player> deadPlayers = new CopyOnWriteArrayList<Player>();
    private CopyOnWriteArrayList<Player> players;
    private CopyOnWriteArrayList<Location> locations;
    private CopyOnWriteArrayList<Weapon> weapons;
    private CopyOnWriteArrayList<Armour> armours;
    private CopyOnWriteArrayList<Potion> potions;
    private CopyOnWriteArrayList<Player> watchlist;

    private int playersInGame;
    private int maxSpawnLevel;
    private int gamemode;
    private int tick;
    private LiveNarrator liveNarrator;

    private static final int FIGHT_PROBABILITY = 80;
    private static final int FIND_OBJECT_RATE_MAX = 25;
    private static final int FIND_OBJECT_RATE_MIN = 15;
    private static final int UPGRADE_WEAPON_RATE = 20;
    private static final int[] WEAPON_TICKS = {3, 5, 8, 11, 15};
    private static final int[] NUMBER_OF_FIGHTERS_ODDS = {32, 48, 56, 60, 62};
    //0 - 50 Weapon, 51-80 Armour, 80-99 Potion
    private static final int[] TYPE_OF_LOOT_ODDS = {50, 80};
    public static final int MODE_BATTLE_ROYALE = 0;


    public GameFlow(int gameMode, CopyOnWriteArrayList<Player> players,
                    CopyOnWriteArrayList<Location> locations, CopyOnWriteArrayList<Lootable> lootables)
    {

        this.players = players;
        this.allPlayers.addAll(players);

        this.locations = locations;
        this.weapons = Lootable.getWeapons(lootables);
        this.potions = Lootable.getPotions(lootables);
        this.armours = Lootable.getArmours(lootables);
        this.maxSpawnLevel = 1;
        this.playersInGame = 50;
        this.gamemode = gameMode;
        Weapon.weaponList = this.weapons;
        this.tick = 0;
        this.liveNarrator = new LiveNarrator(this, this.players, null, 0);
        LiveNarrator.clearScreen();
    }


    public void startGame()
    {
        battleRoyale();
    }

    private void battleRoyale()
    {
        while (this.getAlivePlayers() > 1)
        {
            //LOOT
            this.tryPlayersLooting();
            //BATTLE
            if (!RandomManager.isInRandomRange(0, 100, GameFlow.FIGHT_PROBABILITY, 100))
            {
                this.battleRoyaleBattle();
            }
            liveNarrator.poisonedPlayers();
            for (Player player : this.players)
            {
                int prevLev = player.getLevel();
                player.updateHP();
                if (player.getPotion() != null)
                {
                    Potion pt = player.getPotion();
                    if (player.getPoisonAmount() > 0 || (Math.min(player.getHP() + pt.getHealing(), pt.getHealthCap()) - player.getHP() > pt.getHealing() / 2))
                    {
                        int h = player.givePotion(pt);
                        liveNarrator.playerHealing(player, pt, h);
                        player.setPotion(null);
                    }
                    if(player.getLevel() > prevLev) liveNarrator.playerLevelUp(player);
                }
            }
            //LOOT
            this.tryPlayersLooting();
            this.tick++;
        }
        if (players.size() == 1) liveNarrator.announceWinner(players.get(0));
    }

    private void tryPlayersLooting()
    {
        if (this.maxSpawnLevel > 0)
        {
            for (Player player : players)
            {
                int prevLevel = player.getLevel();
                int numberOfUpgradable = 0;
                if (player.getPrimaryWeapon().isUpgradable() && player.getLevel() - player.getPrimaryWeapon().getLevel() < 3)
                    numberOfUpgradable += 2;

                if (player.getSecondaryWeapon().isUpgradable() && player.getLevel() - player.getSecondaryWeapon().getLevel() < 3)
                    numberOfUpgradable += 1;


                if (numberOfUpgradable > 0 && RandomManager.isInRandomRange(0, 100, 0, GameFlow.UPGRADE_WEAPON_RATE))
                {

                    if (numberOfUpgradable == 3) numberOfUpgradable = RandomManager.randomRange(1, 2);
                    if (numberOfUpgradable == 2)
                    {
                        liveNarrator.upgradeWeapon(player, player.getPrimaryWeapon());
                        player.setPrimaryWeapon(player.getPrimaryWeapon().getLevelUpWeapon());
                    }
                    else
                    {
                        liveNarrator.upgradeWeapon(player, player.getSecondaryWeapon());
                        player.setSecondaryWeapon(player.getSecondaryWeapon().getLevelUpWeapon());
                    }
                }
                else
                {

                    if (RandomManager.isInRandomRange(0, 100, 0, this.getFindWeaponChance()))
                    {
                        Lootable itemFound = null;
                        switch (RandomManager.multiRandomRange(0, 100, GameFlow.TYPE_OF_LOOT_ODDS))
                        {
                            case 0:
                                itemFound = Weapon.randomWeapon(1, Math.max(player.getLevel(), 1), false);
                                break;
                            case 1:
                                itemFound = this.armours.get(RandomManager.randomRange(0, this.armours.size()));
                                break;
                            case 2:
                                itemFound = this.potions.get(RandomManager.randomRange(0, this.potions.size()));
                                break;

                        }
                        if (itemFound instanceof Weapon)
                        {
                            Weapon weaponFound = (Weapon) itemFound;
                            if (Weapon.worstWeapon(player.getPrimaryWeapon(), player.getSecondaryWeapon()) == player.getPrimaryWeapon())
                            {
                                if (Weapon.bestWeapon(player.getPrimaryWeapon(), weaponFound) == weaponFound)
                                {

                                    player.setPrimaryWeapon(weaponFound);
                                    liveNarrator.updateFindLootable(player, weaponFound);
                                }
                            }
                            else
                            {
                                if (Weapon.bestWeapon(player.getSecondaryWeapon(), weaponFound) == weaponFound)
                                {

                                    player.setSecondaryWeapon(weaponFound);
                                    liveNarrator.updateFindLootable(player, weaponFound);
                                }
                            }
                        }
                        boolean ok = false;
                        if (itemFound instanceof Armour)
                        {

                            if (player.getArmour() == null) ok = true;
                            if (!ok && player.getArmour().getMetric() < itemFound.getMetric()) ok = true;
                            if (ok)
                            {
                                player.setArmour(((Armour) itemFound).getNewArmour());
                                liveNarrator.updateFindLootable(player, itemFound);
                            }
                        }
                        if (itemFound instanceof Potion)
                        {
                            if (player.getPotion() == null) ok = true;
                            if (!ok && player.getPotion().getMetric() < itemFound.getMetric()) ok = true;
                            if (ok)
                            {
                                player.setPotion((Potion) itemFound);
                                liveNarrator.updateFindLootable(player, itemFound);
                            }
                        }
                    }
                }
                if (player.getLevel() > prevLevel) liveNarrator.playerLevelUp(player);
            }
        }
    }

    private void battleRoyaleBattle()
    {
        int numberOfFighters = RandomManager.multiRandomRange(0, 64, GameFlow.NUMBER_OF_FIGHTERS_ODDS) + 2;
        numberOfFighters = Math.min(this.getAlivePlayers(), numberOfFighters);
        CopyOnWriteArrayList<Integer> playerNumbers = new CopyOnWriteArrayList<Integer>();
        CopyOnWriteArrayList<Player> playersArrayList = new CopyOnWriteArrayList<Player>();
        Player playersArray[];

        for (int i = 0; i < numberOfFighters; i++)
        {
            playerNumbers.add(RandomManager.randomRangeExcluded(0, this.getAlivePlayers(), playerNumbers.toArray(new Integer[playerNumbers.size()])));
            playersArrayList.add(this.players.get(playerNumbers.get(i)));
        }
        playersArray = playersArrayList.toArray(new Player[playersArrayList.size()]);
        Location location = locations.get(RandomManager.randomRange(0, locations.size()));
        Fight fight = new Fight(location, playersArray);
        liveNarrator.startFight(fight);
        do
        {

            int prevLev = fight.nextHit();
            liveNarrator.updatePrintFight(fight);
            if (fight.getActivePlayer().getLevel() > prevLev) liveNarrator.playerLevelUp(fight.getActivePlayer(),true);
            if (fight.getStatus() == Fight.STATUS_KILL || fight.getStatus() == Fight.STATUS_STEALTH_KILL)
                this.killPlayer(fight.getTargetPlayer());

        }
        while (fight.getNumberOfPlayers() > 1);
        liveNarrator.endFight();
    }

    private int day(int tick)
    {
        return (tick + 12) / 24 + 1;
    }

    private int hour(int tick)
    {
        return (tick + 12) % 24;
    }

    public int getDay()
    {
        return day(this.tick);
    }

    public int getHour()
    {
        return hour(this.tick);
    }


    private void killPlayer(Player player)
    {
        this.players.remove(player);
        this.deadPlayers.add(player);
        Narrator.narratePlayersRemaing(this.getAlivePlayers());
    }

    public int getAlivePlayers()
    {
        return (int) this.players.stream().filter(Player::isAlive).count();
    }

    private int getFindWeaponChance()
    {
        float k = (float) this.getAlivePlayers() / this.allPlayers.size();
        return (int) ((1 - k) * GameFlow.FIND_OBJECT_RATE_MAX + k * GameFlow.FIND_OBJECT_RATE_MIN);
    }


}
