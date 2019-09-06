package me.dariopassarello.battleroyale.handlers;

import me.dariopassarello.battleroyale.exceptions.GrrrrException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class RandomManager
{
    public static int randomRange(int minValue, int maxValue)
    {
        Random rand = new Random();
        if (minValue == maxValue)
        {
            return minValue;
        }
        return rand.nextInt(maxValue - minValue) + minValue;
    }

    public static boolean isInRandomRange(int minValue, int maxValue, int rangeMin, int rangeMax)
    {
        int num = RandomManager.randomRange(minValue, maxValue);
        return isInRange(rangeMin, rangeMax, num);
    }

    private static boolean isInRange(int minValue, int maxValue, int value)
    {
        return value >= minValue && value <= maxValue;
    }

    private static int randomRangeExcluded(int minValue, int maxValue, Integer... valuesNotAllowed)
    {
        if (minValue > maxValue)
        {
            int temp = minValue;
            minValue = maxValue;
            maxValue = temp;
        }
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        for (int value : valuesNotAllowed)
        {
            if (isInRange(minValue, maxValue, value))
            {
                arrayList.add(value);
            }
        }
        if (arrayList.size() == 0)
        {
            return randomRange(minValue, maxValue);
        }
        if (arrayList.size() > maxValue - minValue)
        {
            try
            {
                throw new GrrrrException();
            } catch (GrrrrException e)
            {
                e.printStackTrace();
            }
            return 0;
        }
        int rand;
        do
        {
            rand = RandomManager.randomRange(minValue, maxValue);
        }
        while (arrayList.contains(rand));
        return rand;
    }

    public static int multiRange(int num, int... args)
    {
        Arrays.sort(args);
        if (num < args[0]) return 0;

        if (num > args[args.length - 1]) return args.length;

        for (int i = 0; i < args.length - 1; i++)
        {
            if (num >= args[i] && num <= args[i + 1])
            {
                return i + 1;
            }
        }
        return -1;
    }

    private static int multiRandomRange(int minValue, int maxValue, int... args)
    {
        int num = RandomManager.randomRange(minValue, maxValue);
        return multiRange(num, args);
    }

    public static int[] randomPermutation(int[] toPermutate)
    {
        ArrayList<Integer> list = RandomManager.toArrayList(toPermutate);
        Collections.shuffle(list);
        return toIntArray(list);
    }

    private static ArrayList<Integer> toArrayList(int[] toConvert, boolean condition)
    {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int value : toConvert)
        {
            if (condition)
            {
                list.add(value);
            }
        }
        return list;
    }

    private static int[] toIntArray(ArrayList<Integer> list)
    {
        return list.stream().mapToInt(i -> i).toArray();
    }

    private static ArrayList<Integer> toArrayList(int toConvert[])
    {
        return RandomManager.toArrayList(toConvert, true);
    }
}
