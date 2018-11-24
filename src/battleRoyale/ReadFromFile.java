package battleRoyale;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.CopyOnWriteArrayList;

public class ReadFromFile 
{
	
	public static CopyOnWriteArrayList<Player> readPlayers(String path)
	{
		BufferedReader reader;
		CopyOnWriteArrayList<Player> players = new CopyOnWriteArrayList<Player>();
		try
		{
			File file = new File(path);
			reader = new BufferedReader(new FileReader(file));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			return null;
		}
		try
		{
			String st;
			while((st = reader.readLine())!= null)
			{
				players.add(new Player(st));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			try 
			{
				reader.close();
			} 
			catch (IOException e1) 
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
		return players;
	}
	public static CopyOnWriteArrayList<Location> readLocations(String path)
	{
		BufferedReader reader;
		CopyOnWriteArrayList<Location> locations = new CopyOnWriteArrayList<Location>();
		try
		{
			File file = new File(path);
			reader = new BufferedReader(new FileReader(file));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			return null;
		}
		try
		{
			String st;
			String split[];
			while((st = reader.readLine())!= null)
			{
				split = st.split(";");
				locations.add(new Location(split[0],split[1]));
				
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			try 
			{
				reader.close();
			} 
			catch (IOException e1) 
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
	public static CopyOnWriteArrayList<Weapon> readWeapons(String path)
	{
		BufferedReader reader;
		CopyOnWriteArrayList<Weapon> weapons = new CopyOnWriteArrayList<Weapon>();
		try
		{
			File file = new File(path);
			reader = new BufferedReader(new FileReader(file));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			return null;
		}
		try
		{
			String st;
			String split[];
			while((st = reader.readLine())!= null)
			{
				if(st.charAt(0) != '#')
				{
					split = st.split(";");
					for(int i = 0; i < split.length; i++)
					{
						System.out.printf("%s-",split[i]);
					}
					/**
					System.out.printf("\n");
					if(split[4].contains("null"))
					{
						System.out.println("OK");
						weapons.add(new Weapon(st, st, null, 0, null));
					}
					else
					{
						Weapon father;
						boolean found = false;
						for(int i = 0; i < weapons.size(); i++)
						{
							if(split[4].contains(weapons.get(i).getName()))
							{
								weapons.add(new Weapon(split[0], split[1], Integer.parseInt(split[3].replaceAll("\\s+","")) == 1 ? true : false, Integer.parseInt(split[2].replaceAll("\\s+","")), weapons.get(i)));
								System.out.println("\nPADRE TROVATO\n");
								found = true;
								break;
							}
						}
						if(found == false)
						{
							System.out.printf("\n ERRORE: PADRE NON TROVATO PER %s\n",split[1]);
							weapons.add(new Weapon(split[0], split[1], Integer.parseInt(split[3].replaceAll("\\s+","")) == 1 ? true : false, Integer.parseInt(split[2].replaceAll("\\s+","")), null));
						}
					}*/
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			try 
			{
				reader.close();
			} 
			catch (IOException e1) 
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
		return weapons;
	}
	
}
