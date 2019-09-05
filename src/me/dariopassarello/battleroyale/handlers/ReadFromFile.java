package me.dariopassarello.battleroyale.handlers;

import me.dariopassarello.battleroyale.components.Location;
import me.dariopassarello.battleroyale.components.Player;
import me.dariopassarello.battleroyale.lootables.Armour;
import me.dariopassarello.battleroyale.lootables.Potion;
import me.dariopassarello.battleroyale.lootables.Weapon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

public class ReadFromFile
{

    public static CopyOnWriteArrayList<Player> readPlayers(File file)
    {
        if(!file.exists()) return null;

        BufferedReader reader;
        CopyOnWriteArrayList<Player> players = new CopyOnWriteArrayList<>();
        try
        {
            reader = new BufferedReader(new FileReader(file));
        } catch (Exception e)
        {
            e.printStackTrace();

            return null;
        }
        try
        {
            String st;
            while ((st = reader.readLine()) != null)
            {
                Player p = new Player(st);
                players.add(p);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            try
            {
                reader.close();
            } catch (IOException e1)
            {
                e1.printStackTrace();
            }
            return null;
        }
        try
        {
            reader.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return players;
    }

    public static CopyOnWriteArrayList<Location> readLocations(File file)
    {
        BufferedReader reader;
        CopyOnWriteArrayList<Location> locations = new CopyOnWriteArrayList<Location>();
        try
        {
            reader = new BufferedReader(new FileReader(file));
        } catch (Exception e)
        {
            e.printStackTrace();

            return null;
        }
        try
        {
            String st;
            String split[];
            while ((st = reader.readLine()) != null)
            {
                split = st.split(";[ \\\\t\\\\n\\\\x0b\\\\r\\\\f]*");
                locations.add(new Location(split[0], split[1]));

            }
        } catch (Exception e)
        {
            e.printStackTrace();
            try
            {
                reader.close();
            } catch (IOException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            return null;
        }
        try
        {
            reader.close();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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

        BufferedReader reader;
        CopyOnWriteArrayList<Weapon> lootable = new CopyOnWriteArrayList<Weapon>();
        try
        {
            reader = new BufferedReader(new FileReader(file));
        } catch (Exception e)
        {
            e.printStackTrace();

            return null;
        }
        try
        {
            String st;
            String split[];
            while ((st = reader.readLine()) != null)
            {
                if (st.charAt(0) != '#')
                {
                    //Regex: [ \\t\\n\\x0b\\r\\f]*;[ \\t\\n\\x0b\\r\\f]*
                    split = st.split(";[ \\t\\n\\x0b\\r\\f]*");

                    String[] propertiesStr = split[3].split(",");
                    ArrayList<Integer> properties = new ArrayList<Integer>();
                    for (int i = 0; i < propertiesStr.length; i++)
                    {
                        properties.add(Integer.parseInt(propertiesStr[i]));
                    }
                    lootable.add(new Weapon(split[0], split[1]
                            , Arrays.stream(properties.toArray(new Integer[properties.size()])).mapToInt(Integer::intValue).toArray()
                            , Integer.parseInt(split[2]), findFather(lootable, split[4], st)));
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            try
            {
                reader.close();
            } catch (IOException e1)
            {
                e1.printStackTrace();
            }
            return null;
        }
        try
        {
            reader.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return lootable;
    }

    public static CopyOnWriteArrayList<Armour> readArmours(File file)
    {
        BufferedReader reader;
        CopyOnWriteArrayList<Armour> armours = new CopyOnWriteArrayList<Armour>();
        try
        {
            reader = new BufferedReader(new FileReader(file));
        } catch (Exception e)
        {
            e.printStackTrace();

            return null;
        }
        try
        {
            String st;
            String split[];
            while ((st = reader.readLine()) != null)
            {
                split = st.split(";[ \\\\t\\\\n\\\\x0b\\\\r\\\\f]*");
                armours.add(new Armour(split[0], Float.parseFloat(split[1]), Integer.parseInt(split[2])));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            try
            {
                reader.close();
            } catch (IOException e1)
            {
                e1.printStackTrace();
            }
            return null;
        }
        try
        {
            reader.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return armours;
    }

    public static CopyOnWriteArrayList<Potion> readPotions(File file)
    {
        BufferedReader reader;
        CopyOnWriteArrayList<Potion> potions = new CopyOnWriteArrayList<Potion>();
        try
        {
            reader = new BufferedReader(new FileReader(file));
        } catch (Exception e)
        {
            e.printStackTrace();

            return null;
        }
        try
        {
            String st;
            String split[];
            while ((st = reader.readLine()) != null)
            {
                split = st.split(";[ \\\\t\\\\n\\\\x0b\\\\r\\\\f]*");
                potions.add(new Potion(split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2])));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            try
            {
                reader.close();
            } catch (IOException e1)
            {
                e1.printStackTrace();
            }
            return null;
        }
        try
        {
            reader.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return potions;
    }


}
