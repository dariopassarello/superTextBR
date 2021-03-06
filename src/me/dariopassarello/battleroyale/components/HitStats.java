package me.dariopassarello.battleroyale.components;

public class HitStats
{
    public int damageDealt;
    public int typeOfDamage;
    public boolean perforating;
    public static final int DAMAGE_MISS = 0;
    public static final int DAMAGE_PARTIAL_HIT = 1;
    public static final int DAMAGE_FULL_HIT = 2;
    public static final int DAMAGE_CRITICAL_HIT = 3;

    public HitStats(int damageDealt, int typeOfDamage, boolean perforating)
    {
        this.damageDealt = damageDealt;
        this.typeOfDamage = typeOfDamage;
        this.perforating = perforating;
    }
}
