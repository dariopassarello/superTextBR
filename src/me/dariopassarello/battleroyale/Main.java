package me.dariopassarello.battleroyale;

import me.dariopassarello.battleroyale.components.Location;
import me.dariopassarello.battleroyale.components.Player;
import me.dariopassarello.battleroyale.handlers.GameFlow;
import me.dariopassarello.battleroyale.handlers.LiveNarrator;
import me.dariopassarello.battleroyale.handlers.ReadFromFile;
import me.dariopassarello.battleroyale.lootables.Lootable;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main
{
    private final CopyOnWriteArrayList<Lootable> lootables;
    private final CopyOnWriteArrayList<Player> players;
    private final CopyOnWriteArrayList<Location> locations;
    private final File config_folder;

    private Main(File config_folder)
    {
        this.config_folder = config_folder;
        this.lootables = new CopyOnWriteArrayList<>();
        this.players = new CopyOnWriteArrayList<>();
        this.locations = new CopyOnWriteArrayList<>();
    }

    public void start_game()
    {
        if(!config_folder.exists()) return;

        lootables.addAll(ReadFromFile.readWeapons(new File(config_folder, "weapons.txt")));
        lootables.addAll(ReadFromFile.readArmours(new File(config_folder, "armours.txt")));
        lootables.addAll(ReadFromFile.readPotions(new File(config_folder, "potions.txt")));
        players.addAll(ReadFromFile.readPlayers(new File(config_folder, "players.txt")));
        System.out.println(players.toString());
        locations.addAll(ReadFromFile.readLocations(new File(config_folder, "locations.txt")));
        GameFlow game = new GameFlow(0, players, locations, lootables);
        printTitle();
        promptEnterKey();
        LiveNarrator.live = getInt("Premi 0 per una simulazione a tempo, 1 altrimenti...") == 0;
        game.startGame();
    }

    public static void main(String args[])
    {
        File directory = new File(System.getProperty("user.dir"), "config");
        Main main = new Main(directory);
        main.start_game();
    }

    private static void printTitle()
    {
        System.out.println(" ______     __  __     ______   ______     ______        ______   ______     __  __     ______                                        ");
        System.out.println("/\\  ___\\   /\\ \\/\\ \\   /\\  == \\ /\\  ___\\   /\\  == \\      /\\__  _\\ /\\  ___\\   /\\_\\_\\_\\   /\\__  _\\                                       ");
        System.out.println("\\ \\___  \\  \\ \\ \\_\\ \\  \\ \\  _-/ \\ \\  __\\   \\ \\  __<      \\/_/\\ \\/ \\ \\  __\\   \\/_/\\_\\/_  \\/_/\\ \\/                                       ");
        System.out.println(" \\/\\_____\\  \\ \\_____\\  \\ \\_\\    \\ \\_____\\  \\ \\_\\ \\_\\       \\ \\_\\  \\ \\_____\\   /\\_\\/\\_\\    \\ \\_\\                                       ");
        System.out.println("  \\/_____/   \\/_____/   \\/_/     \\/_____/   \\/_/ /_/        \\/_/   \\/_____/   \\/_/\\/_/     \\/_/                                       ");
        System.out.println("                                                                                                                                      ");
        System.out.println(" ______     ______     ______   ______   __         ______           ______     ______     __  __     ______     __         ______    ");
        System.out.println("/\\  == \\   /\\  __ \\   /\\__  _\\ /\\__  _\\ /\\ \\       /\\  ___\\         /\\  == \\   /\\  __ \\   /\\ \\_\\ \\   /\\  __ \\   /\\ \\       /\\  ___\\   ");
        System.out.println("\\ \\  __<   \\ \\  __ \\  \\/_/\\ \\/ \\/_/\\ \\/ \\ \\ \\____  \\ \\  __\\         \\ \\  __<   \\ \\ \\/\\ \\  \\ \\____ \\  \\ \\  __ \\  \\ \\ \\____  \\ \\  __\\   ");
        System.out.println(" \\ \\_____\\  \\ \\_\\ \\_\\    \\ \\_\\    \\ \\_\\  \\ \\_____\\  \\ \\_____\\        \\ \\_\\ \\_\\  \\ \\_____\\  \\/\\_____\\  \\ \\_\\ \\_\\  \\ \\_____\\  \\ \\_____\\ ");
        System.out.println("  \\/_____/   \\/_/\\/_/     \\/_/     \\/_/   \\/_____/   \\/_____/         \\/_/ /_/   \\/_____/   \\/_____/   \\/_/\\/_/   \\/_____/   \\/_____/ ");
        System.out.println("                                                                                                                                      ");
    }

    private static void promptEnterKey()
    {
        System.out.println("\n\nPREMI INVIO PER INIZIARE...");
        try
        {
            int read = System.in.read(new byte[2]);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static int getInt(String text)
    {
        Scanner reader = new Scanner(System.in);
        System.out.println(text);
        return reader.nextInt();
    }

}
