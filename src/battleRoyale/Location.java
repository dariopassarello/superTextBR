package battleRoyale;


public class Location 
{
	private String name;
	private String preposition;
	
	

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
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getPreposition() {
		return preposition;
	}

	public void setPreposition(String preposition) {
		this.preposition = preposition;
	}

	
}
