package me.dariopassarello.battleroyale.handlers;

import me.dariopassarello.battleroyale.components.Player;
import me.dariopassarello.battleroyale.components.HitStats;
import me.dariopassarello.battleroyale.lootables.Armour;
import me.dariopassarello.battleroyale.lootables.Lootable;
import me.dariopassarello.battleroyale.lootables.Potion;
import me.dariopassarello.battleroyale.lootables.Weapon;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class LiveNarrator
{
    private CopyOnWriteArrayList<Player> allPlayers;
    private CopyOnWriteArrayList<Player> playerWatchList;
    private GameFlow actualGameFlow;
    private Fight currentFight;
    private Player[] playersInFight;
    private CopyOnWriteArrayList<String> printEventStack;
    private int loggingLevel;
    public static boolean live = false;

    public final static int LOG_ALL_PLAYERS = 0;
    public final static int LOG_WATCHED_PLAYERS = 1;
    public static final int DO_NOT_PRINT_HEALTH = -1;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[1;31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final int STACK_SIZE = 5;
    public static final String escapeFailCause[] = {"ma scivola su una merda","ma inciampa","ma viene fermato da un Comunista","ma fallisce","ma fallisce miseramente","ma trova l'amore della sua vita","ma si dimentica di fuggire"};
    public static final String missCause[] = {"ma lo manca perchè ha una mira di merda","ma viene distratto","ma per poco non si colpisce da solo","ma lo manca","ma lo manca di un pelo","ma fallisce ","ma fallisce miseramente"};

    public LiveNarrator(GameFlow actualGameFlow,CopyOnWriteArrayList<Player> allPlayers,CopyOnWriteArrayList<Player> playerWatchList,int loggingLevel)
    {
        this.actualGameFlow = actualGameFlow;
        this.allPlayers = allPlayers;
        this.playerWatchList = playerWatchList;
        this.loggingLevel = loggingLevel;
        this.printEventStack = new CopyOnWriteArrayList<String>();
    }



    private void printHeader()
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.printf("\n\nGIORNO %d, ORE %d:00\n", actualGameFlow.getDay(),actualGameFlow.getHour());
        System.out.printf("\nGIOCATORI IN VITA: %d\n", actualGameFlow.getAlivePlayers());
    }

    private void printPlayer(Player player,int previousHealthValue,boolean printEquipment)
    {

        if(player.isAlive())
        {
            System.out.printf("\n%s  (LEVEL: %d) ",player.getName().toUpperCase(),player.getLevel());
            if(player.getPoisonAmount() > 0)
            {
                System.out.printf("%s[AVVELENATO]%s  ",ANSI_PURPLE,ANSI_RESET);
            }
            if(player.isDisarmed())
            {
                System.out.printf("%s[DISARMATO]%s   ",ANSI_YELLOW,ANSI_RESET);
            }
            if(this.currentFight.getNumberOfPlayers() > 1 && !Arrays.asList(this.currentFight.getPlayers()).contains(player))
            {
                System.out.printf("%s[FUGGITO]%s   ",ANSI_CYAN,ANSI_RESET);
            }
        }
        else
        {
            System.out.printf("\n%s%s (KILLS: %d) ",ANSI_RED,player.getName().toUpperCase(),player.getKills());
        }
        if(previousHealthValue != DO_NOT_PRINT_HEALTH)
        {
            String postText;
            if(previousHealthValue == player.getHP())
            {
                postText = String.format("]%d HP",player.getHP());
            }
            else
            {
                postText = String.format("]%d HP (%d HP)",player.getHP(),player.getHP() - previousHealthValue);
            }
            printAdvancedHealthBar("\nHP [", postText,previousHealthValue,player.getHP(),100,40,'X');
            if(player.equals(currentFight.getTargetPlayer()))
            {
                System.out.printf("  %s<- [%s %s %s]%s\n",ANSI_YELLOW,currentFight.getActivePlayer().getName(),currentFight.getWeaponUsed().getPrefix(),currentFight.getWeaponUsed().getName(),ANSI_RESET);
            }
            else
            {
                System.out.print("\n");
            }
        }
        if(printEquipment)
        {
            System.out.printf("ARMI[%s (LIV %d)|%s (LIV %d)]",player.getPrimaryWeapon().getName(),player.getPrimaryWeapon().getLevel(),player.getSecondaryWeapon().getName(),player.getSecondaryWeapon().getLevel());
            if(player.getArmour() != null)
            {
                printHealthBar("\nARMOUR [",String.format("] %d HP %s",player.getArmour().getHP(),player.getArmour().getName()),player.getArmour().getHP(),player.getArmour().getMaxHealth(),40,'A');
            }
        }
        System.out.printf("%s\n\n",ANSI_RESET);

    }

    private void printFightHeader()
    {
        for(Player p : this.playersInFight)
        {
            if(p.equals(this.currentFight.getTargetPlayer()))
            {
                this.printPlayer(p,this.currentFight.getTargetPlayer_previousHP(),true);
            }
            else
            {
                this.printPlayer(p,p.getHP(),true);
            }

        }
    }

    private void printStack()
    {
        while(this.printEventStack.size() > LiveNarrator.STACK_SIZE)
        {
            this.printEventStack.remove(0);
        }
        if(this.printEventStack.size() > 0)
        {
            System.out.printf("%s\n\n-----------------------------------------------------------------------------\n\n",this.printEventStack.get(this.printEventStack.size() - 1));
        }
        for(int i = this.printEventStack.size() - 2; i >= 0; i--)
        {
            System.out.print(this.printEventStack.get(i));
        }
    }

    private void printStackFile()
    {
        //TODO stampare su file lo stack
        Stack<String> s = (Stack<String>) this.printEventStack.clone();
        if(!s.empty())
        {
            System.out.printf("%s\n\n",s.pop());
        }
        while(!s.empty())
        {
            System.out.print(s.pop());
        }
    }

    private void emptyStack()
    {
        while(this.printEventStack.size() > 0)
        {
            this.printEventStack.remove(0);
        }
    }

    private void pushInEventStack(String s)
    {
        this.printEventStack.add(s);
    }

    private String getFightString(Fight fight)
    {
        String playerName = fight.getActivePlayer().getName().toUpperCase();
        String randomEscape = LiveNarrator.escapeFailCause[RandomManager.randomRange(0,LiveNarrator.escapeFailCause.length)];
        String randomMiss = LiveNarrator.missCause[RandomManager.randomRange(0,LiveNarrator.missCause.length)];
        switch(fight.getStatus())
        {
            case Fight.STATUS_ESCAPE:
                return String.format("\n[%s] fugge (HP: %d)\n",playerName,fight.getActivePlayer().getHP());
            case Fight.STATUS_ESCAPE_FAILED:
                return String.format("\n[%s] prova a fuggire %s\n",playerName,randomEscape);
            case  Fight.STATUS_STILL_FIGHTING:
                switch(fight.getHit().typeOfDamage)
                {
                    case HitStats.DAMAGE_MISS:
                        return String.format("\n[%s] prova a colpire [%s] %s %s %s\n",playerName,fight.getTargetPlayer().getName().toUpperCase(),fight.getWeaponUsed().getPrefix()
                                ,fight.getWeaponUsed().getName(),randomMiss);
                    case HitStats.DAMAGE_PARTIAL_HIT:
                        return String.format("\n[%s] colpisce di striscio [%s] %s %s (DANNO: %d HP)\n",playerName,fight.getTargetPlayer().getName().toUpperCase(),fight.getWeaponUsed().getPrefix()
                                ,fight.getWeaponUsed().getName(),fight.getHit().damageDealt);
                    case HitStats.DAMAGE_FULL_HIT:
                        return String.format("\n[%s] colpisce [%s] %s %s  (DANNO: %d HP) \n",playerName,fight.getTargetPlayer().getName().toUpperCase(),fight.getWeaponUsed().getPrefix()
                                ,fight.getWeaponUsed().getName(),fight.getHit().damageDealt);
                    case HitStats.DAMAGE_CRITICAL_HIT:
                        return String.format("\n[%s] colpisce MOLTO FORTE [%s] %s %s (DANNO: %d HP)\n",playerName,fight.getTargetPlayer().getName().toUpperCase(),fight.getWeaponUsed().getPrefix()
                                ,fight.getWeaponUsed().getName(),fight.getHit().damageDealt);
                     default: return "";
                }
             case Fight.STATUS_KILL:
             case Fight.STATUS_STEALTH_KILL:
                 return String.format("\n%s[%s] ha ucciso [%s] %s %s %s %s (DANNO: %d)%s\n GIOCATORI RIMANENTI: %d\n",ANSI_RED,playerName,fight.getTargetPlayer().getName().toUpperCase(),fight.getWeaponUsed().getPrefix()
                         ,fight.getWeaponUsed().getName(),fight.getLocation().getPreposition(),fight.getLocation().getName(),fight.getHit().damageDealt,ANSI_RESET,this.actualGameFlow.getAlivePlayers());
             default: return "";
        }
    }

    public void startFight(Fight fight)
    {
        emptyStack();
        printHeader();
        this.currentFight = fight;
        this.playersInFight = new Player[this.currentFight.getPlayers().length];
        for(int i = 0; i < this.currentFight.getPlayers().length; i++)
        {
            this.playersInFight[i] = this.currentFight.getPlayers()[i];
        }
        this.printFightHeader();
        System.out.printf("\nInizia un combattimento %s %s: \n",this.currentFight.getLocation().getPreposition(),this.currentFight.getLocation().getName().toUpperCase());
        hold(3000,4000);
    }

    public void updatePrintFight(Fight fight)
    {
        printHeader();
        printFightHeader();
        pushInEventStack(getFightString(fight));
        printStack();
        hold(2500,4000);

    }

    public void endFight()
    {
        emptyStack();
    }
    public void updateFindLootable(Player p, Lootable l)
    {
        printHeader();
        String s;
        String k = "";
        s = String.format("\n[%s] ha equipaggiato \"%s\" ",p.getName(),l.getName());
        if(l instanceof Weapon) k = String.format("(LIVELLO %d)\n",((Weapon) l).getLevel());
        if(l instanceof Armour) k = String.format("(PROTEZIONE: %d %%)\n",(int)(((Armour) l).getDamageProtection()*100));
        if(l instanceof Potion) k = String.format("(CURA: %d/MAX: %d)\n",((Potion) l).getHealing(),((Potion) l).getHealthCap());
        pushInEventStack(s.concat(k));
        printStack();
        hold(2000,2500);
    }


    public void playerLevelUp(Player p)
    {
        playerLevelUp(p, false);
    }

    public void playerLevelUp(Player p,boolean inFight)
    {
        printHeader();
        if(inFight) printFightHeader();
        pushInEventStack(String.format("\n%s^^^[%s] sale al livello %d ^^^%s\n",ANSI_CYAN,p.getName().toUpperCase(),p.getLevel(),ANSI_RESET));
        printStack();
        hold(2000,2500);
    }


    public void upgradeWeapon(Player p,Weapon w)
    {
        printHeader();
        String s = String.format("\n[%s] ha potenziato %s in %s (LIVELLO %d)\n",p.getName().toUpperCase(),w.getName(),w.getLevelUpWeapon().getName(),w.getLevelUpWeapon().getLevel());
        pushInEventStack(s);
        printStack();
        hold(2000,2500);
    }

    public void poisonedPlayers()
    {
        printHeader();

        boolean pois = false;
        for(Player p : this.allPlayers)
        {
            if(p.getHP() > 0 && p.getPoisonAmount() > 0)
            {
                pois = true;
            }
        }
        if(pois)
        {
            String s = "\nDANNI DA VELENO: \n";
            for(Player p : this.allPlayers)
            {
                if(p.getHP() > 0 && p.getPoisonAmount() > 0)
                {
                    s += (String.format("[%s] (HP: %d, -%d/ora)\n",p.getName().toUpperCase(),p.getHP() - p.getPoisonAmount() > 0 ? p.getHP() - p.getPoisonAmount() : 0,p.getPoisonAmount()));
                }
            }
            pushInEventStack(s);
        }

        printStack();
        hold(3000,4000);

    }

    public void playerHealing(Player p, Potion potion, int heal)
    {
        printHeader();
        String s = String.format("\n[%s] si è curato con [%s] (HEAL: %d HP)\n", p.getName(), potion.getName(), heal);
        pushInEventStack(s);
        printStack();
        hold(1000,2000);
    }


    public void announceWinner(Player p)
    {
        clearScreen();
        System.out.printf("La partita è conclusa. \n\nIl vincitore è \n \n %s \n \n KILLS. %d, DANNO: %d HP\n",p.getName().toUpperCase(),p.getKills(),p.getDamageDealt());
    }

    public void printKillStats(int max, boolean onlyAlivePlayers)
    {
        clearScreen();
        printHeader();
        CopyOnWriteArrayList<Player> cloneList = new CopyOnWriteArrayList<Player>();
        cloneList.addAll(this.allPlayers);
        cloneList = LiveNarrator.sortPlayers(cloneList,new Player.KillComparator(),true);
        if(onlyAlivePlayers)
        {
            for(Player p : cloneList)
            {
                if(!p.isAlive())
                {
                    cloneList.remove(p);
                }
            }
        }
        int maxPlayerLength = 0;
        int maxPlayers = Math.min(this.allPlayers.size(),max);
        String printString;
        for(int i = 0; i < maxPlayers; i++)
        {
            if(cloneList.get(i).getName().length() >= maxPlayerLength)
            {
                maxPlayerLength = cloneList.get(i).getName().length();
            }
        }
        printString = "\n%-2d. [%-" + String.format("\n%d",maxPlayerLength) + "s] HP: %-3d KILLS: %-2d DANNO: %-4d ARMI [ %s (%d) | %s (%d)]%s %s";
        for(int i = 0; i < maxPlayers; i++)
        {
            String color;
            String status;
            Player p = this.allPlayers.get(i);
            if(p.isAlive())
            {
                color = ANSI_RESET;
                status = "";
            }
            else
            {
                status = "[DEAD]";
                color = ANSI_RED;
            }

            System.out.printf(printString,color, i + 1, p.getName().toUpperCase(), p.getHP(), p.getKills(), p.getDamageDealt()
                    , p.getPrimaryWeapon().getName(), p.getSecondaryWeapon().getName(),status,ANSI_RESET);
        }
        // "1. [NOME PLAYER] HP: %d KILLS:%d  DAMAGE_DEALT:%d [WEAPON 1 | WEAPON 2] Staus..."
        hold(5000,7000);
        clearScreen();
    }

    public void poisonDeaths(Player p)
    {
        printHeader();
        String s = String.format("\n%s[%s] è morto avvelenato%s\nGIOCATORI RIMANENTI: %d\n",ANSI_RED,p.getName().toUpperCase(),ANSI_RESET,actualGameFlow.getAlivePlayers());
        pushInEventStack(s);
        printStack();
        LiveNarrator.randomSleep(2000,3000);
    }

    //Utility methods
    private void hold(int minWait,int maxWait)
    {
        if(live)
        {
            randomSleep(minWait,maxWait);
        }
        else
        {
            System.out.println("\n.");
            try {
                int read = System.in.read(new byte[2]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void printHealthBar(String preText, String postText, int value, int maxValue, int lenght, char barSymbol)
    {
        System.out.printf("%s" ,preText);
        int numberOfChars = Math.min((int) Math.ceil(((float) value/maxValue)*lenght),lenght);
        for(int i = 0; i < lenght; i++)
        {
            if(i < numberOfChars)
            {
                System.out.printf("%c",barSymbol);
            }
            else {
                System.out.printf(" ");
            }
        }
        System.out.printf("%s",postText);
    }

    private static void printAdvancedHealthBar(String preText, String postText, int previousValue, int value, int maxValue, int lenght, char barSymbol)
    {
        System.out.printf("%s" ,preText);
        int numberOfChars = Math.min((int) Math.ceil(((float) value/maxValue)*lenght),lenght);
        int numberOfPreviousChars = Math.min((int) Math.ceil(((float) previousValue/maxValue)*lenght),lenght);
        for(int i = 0; i < lenght; i++)
        {
            if(i < numberOfChars)
            {
                System.out.printf("%c",barSymbol);
            }
            else if(i >= numberOfChars && i <numberOfPreviousChars)
            {
                System.out.printf("%s%c%s",ANSI_RED,barSymbol,ANSI_RESET);
            }
            else
            {
                System.out.printf(" ");
            }
        }
        System.out.printf("%s",postText);
    }

    private static void randomSleep(int minSleep,int maxSleep)
    {
        Random rand = new Random();
        if(minSleep == maxSleep)
        {
            try {
                Thread.sleep(minSleep);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return;
        }
        int sleepTime = rand.nextInt(maxSleep - minSleep) + minSleep;
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void clearScreen()
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static CopyOnWriteArrayList<Player> sortPlayers(CopyOnWriteArrayList<Player> players, Comparator<Player> c, boolean reverse)
    {
        for(int i = 0; i < players.size() - 1; i++)
        {
            for(int j = i + 1; j < players.size(); j++)
            {
                int comp = c.compare(players.get(i),players.get(j));
                if((comp > 0 && reverse == false) || (comp < 0 && reverse == true))
                {
                    Player temp = players.get(i);
                    players.set(i,players.get(j));
                    players.set(j,temp);
                }
            }
        }
        return players;
    }
    // do your thing.
}




