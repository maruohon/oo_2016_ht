package application;

public class Item
{
	private final String name;
	private final boolean fragile;
	private final Size size;
	private final float weight;

	public Item(String name, boolean fragile, Size size, float weight)
	{
		this.name = name;
		this.fragile = fragile;
		this.size = size;
		this.weight = weight;
	}

	public String getName()
	{
		return this.name;
	}

	public boolean isFragile()
	{
		return this.fragile;
	}

	public Size getSize()
	{
		return this.size;
	}

	public float getWeight()
	{
		return this.weight;
	}

	@Override
	public String toString()
	{
		return this.name;
	}
}
