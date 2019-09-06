package me.dariopassarello.battleroyale.components;

import me.dariopassarello.battleroyale.handlers.RandomManager;
import me.dariopassarello.battleroyale.lootables.Armour;
import me.dariopassarello.battleroyale.lootables.Potion;
import me.dariopassarello.battleroyale.lootables.Weapon;

import java.util.Comparator;

@SuppressWarnings("Access can be package-private")
public class Player
{
    //NAME
    private String name;
    //EQUIPMENT
    private Weapon primaryWeapon;
    private Weapon secondaryWeapon;
    private Armour armour;
    private Potion potion;
    //STATUS
    private int HP;
    private int poisonAmount;
    private boolean disarmed;
    private int discipline;
    //STATS
    private int kills;
    private int damageDealt;
    private int totalHeal;
    private int weaponUpgrades;

    public static final int MAX_HP = 100;
    public static final int[] XP = {35, 80, 135, 200, 265, 330, 400};
    public static final int HEAL_PER_XP = 3;
    public static final int XP_PER_UPGRADE = 5;
    public static final int XP_PER_KILL = 20;

    public Player(String name)
    {
        this.name = name;
        this.HP = 100;
        this.primaryWeapon = new Weapon("", "(Slot vuoto)", null, 0, null);
        this.secondaryWeapon = new Weapon("", "(Slot vuoto)", null, 0, null);
        this.kills = 0;
        this.damageDealt = 0;
        this.poisonAmount = 0;
        this.disarmed = false;
        this.discipline = 0;
        this.potion = null;
        this.totalHeal = 0;
        this.weaponUpgrades = 0;
    }

    public static class KillComparator implements Comparator<Player>
    {
        public int compare(Player p1, Player p2)
        {
            if (p1.getKills() == p2.getKills())
            {
                if (p1.getDamageDealt() == p2.getDamageDealt()) return 0;
                else if (p1.getDamageDealt() > p2.getDamageDealt()) return 1;
                else return -1;
            } else if (p1.getKills() > p2.getKills()) return 1;
            else return -1;

        }
    }

    public String getName()
    {
        return this.name;
    }

    public void incrementUpgrade()
    {
        this.weaponUpgrades++;
    }

    public Weapon getPrimaryWeapon()
    {
        return primaryWeapon;
    }

    public void setPrimaryWeapon(Weapon primaryWeapon)
    {
        this.primaryWeapon = primaryWeapon;
    }

    public Weapon getSecondaryWeapon()
    {
        return secondaryWeapon;
    }

    public void setSecondaryWeapon(Weapon secondaryWeapon)
    {
        this.secondaryWeapon = secondaryWeapon;
    }

    public int attack(int damage, boolean perforating)
    {
        int trueDamage = damage;
        boolean perforation = false;
        if (this.armour != null)
        {
            perforation = !(RandomManager.isInRandomRange(0, 100, 0, (int) (this.armour.getDamageProtection() * 100))) && perforating;
            trueDamage = this.armour.attackAndCalculateTrueDamage(damage, perforation);
        }
        this.HP -= trueDamage;
        if (this.HP < 0) this.HP = 0;

        return trueDamage;
    }

    public void heal(int healAmount)
    {
        this.poisonAmount = 0;
        this.HP += healAmount;
        if (this.HP > Player.MAX_HP) this.HP = Player.MAX_HP;
    }

    public void updateHP()
    {
        this.HP -= this.poisonAmount;
        if (this.HP <= 1)
        {
            this.HP = 1;
            this.poisonAmount = 0;
        }
    }

    public int getHP()
    {
        return this.HP;
    }

    public boolean isAlive()
    {
        return HP > 0;
    }

    public int getMaxWeaponTier()
    {
        return Math.max(this.primaryWeapon.getLevel(), this.secondaryWeapon.getLevel());
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setHP(int HP)
    {
        this.HP = HP > 0 ? HP : 0;
    }

    /**
     * figa che roba
     *
     * @param cacca
     * @return culo
     */
    public int givePotion(Potion cacca)
    {
        int oldHP = this.HP;
        this.HP = Math.min(this.HP + cacca.getHealing(), cacca.getHealthCap());
        this.totalHeal += this.HP - oldHP;
        return this.HP - oldHP;
    }

    public int getKills()
    {
        return kills;
    }

    public void addKill()
    {
        this.kills++;
    }

    public void addDamageDealt(int damageDealt)
    {
        this.damageDealt += damageDealt;
    }

    public int getDamageDealt()
    {
        return damageDealt;
    }

    public boolean isDisarmed()
    {
        return disarmed;
    }

    public void setDisarmed(boolean disarmed)
    {
        this.disarmed = disarmed;
    }

    public Armour getArmour()
    {
        return armour;
    }

    public void setArmour(Armour armour)
    {
        this.armour = armour;
    }

    public int getPoisonAmount()
    {
        return poisonAmount;
    }

    public void setPoisonAmount(int poisonAmount)
    {
        this.poisonAmount = poisonAmount;
    }

    public int getXP()
    {
        return this.damageDealt + this.totalHeal / HEAL_PER_XP + this.kills * XP_PER_KILL + this.weaponUpgrades * XP_PER_UPGRADE;
    }

    public int getLevel()
    {
        return RandomManager.multiRange(getXP(), XP);
    }

    public Potion getPotion()
    {
        return potion;
    }

    public int getDiscipline()
    {
        return discipline;
    }

    public void setDiscipline(int discipline)
    {
        this.discipline = discipline;
    }

    public void setPotion(Potion potion)
    {
        this.potion = potion;
    }

    public void usePotion()
    {
        if (this.potion != null)
        {
            this.potion.healPlayer(this);
            this.potion = null;
        }
    }
}
