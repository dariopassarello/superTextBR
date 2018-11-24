package battleRoyale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class RandomManager 
{
	public static int randomRange(int minValue,int maxValue)
	{
		Random rand = new Random();
		return rand.nextInt(maxValue - minValue) + minValue;
	}
	public static boolean isInRandomRange(int minValue, int maxValue, int rangeMin,int rangeMax)
	{
		int num = RandomManager.randomRange(minValue, maxValue);
		return isInRange(minValue,maxValue,num);
	}
	public static boolean isInRange(int minValue, int maxValue, int value)
	{
		if(value >= minValue && value <= maxValue)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public static int randomRangeExcluded(int minValue,int maxValue, int... valuesNotAllowed)
	{
		if(minValue > maxValue)
		{
			int temp = minValue;
			minValue = maxValue;
			maxValue = temp;
		}
		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		for(int value : valuesNotAllowed)
		{
			if(isInRange(minValue,maxValue,value))
			{
				arrayList.add(value);
			}
		}
		if(arrayList.size() == 0)
		{
			return randomRange(minValue,maxValue);
		}
		if(arrayList.size() > maxValue - minValue)
		{
			try
			{
				throw new GrrrrException();
			}
			catch(GrrrrException e)
			{
				e.printStackTrace();
			}
			return 0;
		}
		int pos = randomRange(0,maxValue - minValue - arrayList.size());
		int i;
		for(i = minValue; i < pos + minValue;)
		{
			if(!arrayList.contains(i)) i++;
		}
		return i;
	}
	

	public static int multiRange(int minValue, int maxValue, int... args)
	{
		int num = RandomManager.randomRange(minValue, maxValue);
		Arrays.sort(args);
		int res = -1;
		for(int i = 0; i < args.length - 1; i++)
		{
			if(num >= args[i] && num <= args[i+1])
			{
				res = i;
				break;
			}

		}
		return res;
	}
	
	public static int[] randomPermutation(int toPermutate[])
	{
		ArrayList<Integer> list = RandomManager.toArrayList(toPermutate);
		Collections.shuffle(list);
		return toIntArray(list);
	}
	
	public static ArrayList<Integer> toArrayList(int toConvert[],boolean condition)
	{
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int value : toConvert)
		{
			if(condition)
			{
				list.add(value);
			}
		}
		return list;
	}
	
	public static int[] toIntArray(ArrayList<Integer> list)
	{
		return list.stream().mapToInt(i -> i).toArray();
	}
	
	public static ArrayList<Integer> toArrayList(int toConvert[])
	{
		return RandomManager.toArrayList(toConvert,true);
	}
	


}
