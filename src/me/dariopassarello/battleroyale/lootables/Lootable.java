package me.dariopassarello.battleroyale.lootables;

import java.util.concurrent.CopyOnWriteArrayList;

public interface Lootable
{
    int getMetric();

    String getName();

    static CopyOnWriteArrayList<Weapon> getWeapons(CopyOnWriteArrayList<Lootable> lootables)
    {
        CopyOnWriteArrayList<Weapon> wp = new CopyOnWriteArrayList<Weapon>();
        for (Lootable lootable : lootables)
        {
            if (lootable instanceof Weapon)
            {
                wp.add((Weapon) lootable);
            }
        }
        return wp;
    }

    static CopyOnWriteArrayList<Potion> getPotions(CopyOnWriteArrayList<Lootable> lootables)
    {
        CopyOnWriteArrayList<Potion> wp = new CopyOnWriteArrayList<Potion>();
        for (Lootable lootable : lootables)
        {
            if (lootable instanceof Potion)
            {
                wp.add((Potion) lootable);
            }
        }
        return wp;
    }

    static CopyOnWriteArrayList<Armour> getArmours(CopyOnWriteArrayList<Lootable> lootables)
    {
        CopyOnWriteArrayList<Armour> wp = new CopyOnWriteArrayList<Armour>();
        for (Lootable lootable : lootables)
        {
            if (lootable instanceof Armour)
            {
                wp.add((Armour) lootable);
            }
        }
        return wp;
    }
}
