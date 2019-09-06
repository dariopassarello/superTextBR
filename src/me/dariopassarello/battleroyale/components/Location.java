package me.dariopassarello.battleroyale.components;

public class Location
{
    private final String name, preposition;

    public Location(String preposition, String name)
    {
        this.name = name;
        this.preposition = preposition;
    }

    public Location(String name)
    {
        this.name = name;
        this.preposition = "in";
    }

    public String getName()
    {
        return name;
    }

    public String getPreposition()
    {
        return preposition;
    }
}
