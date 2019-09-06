package me.dariopassarello.battleroyale.handlers;

import me.dariopassarello.battleroyale.components.Location;
import me.dariopassarello.battleroyale.components.Player;
import me.dariopassarello.battleroyale.lootables.Armour;
import me.dariopassarello.battleroyale.lootables.Potion;
import me.dariopassarello.battleroyale.lootables.Weapon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ReadFromFile
{
    private static List<String> getLines(File file)
    {
        if (!file.exists()) return null;

        List<String> list = new ArrayList<>();

        BufferedReader reader;
        try
        {
            reader = new BufferedReader(new FileReader(file));

            String st;
            while ((st = reader.readLine()) != null)
            {
                list.add(st);
            }

            reader.close();
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        return list;
    }

    public static CopyOnWriteArrayList<Player> readPlayers(File file)
    {
        CopyOnWriteArrayList<Player> players = new CopyOnWriteArrayList<>();
        List<String> lines = getLines(file);

        if (lines == null) return null;

        lines.forEach(st -> players.add(new Player(st)));

        return players;
    }

    public static CopyOnWriteArrayList<Location> readLocations(File file)
    {
        CopyOnWriteArrayList<Location> locations = new CopyOnWriteArrayList<>();
        List<String> lines = getLines(file);

        if (lines == null) return null;

        lines.forEach(st ->
        {
            String[] split = st.split(";[ \\\\t\\\\n\\\\x0b\\\\r\\\\f]*");

            locations.add(new Location(split[0], split[1]));
        });

        return locations;
    }

    private static Weapon findFather(CopyOnWriteArrayList<Weapon> e, String name, String st)
    {
        if (name.equals("null")) return null;
        for (Weapon w : e)
        {
            if (name.equals(w.getName()))
            {
                return w;
            }
        }
        System.out.printf("ERRORE DI PADRINCONIA \"%s\" -> \"%s\"\n", name, st);
        throw new NullPointerException();
    }

    public static CopyOnWriteArrayList<Weapon> readWeapons(File file)
    {
        CopyOnWriteArrayList<Weapon> lootables = new CopyOnWriteArrayList<>();
        List<String> lines = getLines(file);

        if (lines == null) return null;

        lines.forEach(st ->
        {
            String[] split = st.split(";[ \\t\\n\\x0b\\r\\f]*");

            String[] propertiesStr = split[3].split(",");
            ArrayList<Integer> properties = new ArrayList<>();

            for (String prop : propertiesStr)
            {
                properties.add(Integer.parseInt(prop));
            }

            lootables.add(new Weapon(split[0], split[1]
                    , Arrays.stream(properties.toArray(new Integer[properties.size()])).mapToInt(Integer::intValue).toArray()
                    , Integer.parseInt(split[2]), findFather(lootables, split[4], st)));
        });

        return lootables;
    }

    public static CopyOnWriteArrayList<Armour> readArmours(File file)
    {
        CopyOnWriteArrayList<Armour> armours = new CopyOnWriteArrayList<>();
        List<String> lines = getLines(file);

        if (lines == null) return null;

        lines.forEach(st ->
        {
            String[] split = st.split(";[ \\\\t\\\\n\\\\x0b\\\\r\\\\f]*");

            armours.add(new Armour(split[0], Float.parseFloat(split[1]), Integer.parseInt(split[2])));
        });

        return armours;
    }

    public static CopyOnWriteArrayList<Potion> readPotions(File file)
    {
        CopyOnWriteArrayList<Potion> potions = new CopyOnWriteArrayList<Potion>();
        List<String> lines = getLines(file);

        if (lines == null) return null;

        lines.forEach(st ->
        {
            String[] split = st.split(";[ \\\\t\\\\n\\\\x0b\\\\r\\\\f]*");

            potions.add(new Potion(split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2])));
        });

        return potions;
    }
}
