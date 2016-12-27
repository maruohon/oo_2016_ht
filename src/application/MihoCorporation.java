package application;

import java.util.ArrayList;
import java.util.List;

/**
 * Mahdollisesti Itellaakin Huonompi Organisaatio
 */
public class MihoCorporation
{
	private static MihoCorporation instance;
	private Warehouse warehouse;
	private List<SmartPost> smartPosts;
	private List<String> cities;
	private List<Item> items;

	private MihoCorporation()
	{
		this.warehouse = new Warehouse();
		this.smartPosts = new ArrayList<SmartPost>();
		this.cities = new ArrayList<String>();
		this.items = new ArrayList<Item>();
		this.fetchSmartPosts();
		this.addDefaultItems();
	}

	public static MihoCorporation getInstance()
	{
		if (instance == null)
		{
			instance = new MihoCorporation();
		}

		return instance;
	}

	public Warehouse getWarehouse()
	{
		return this.warehouse;
	}

	public List<SmartPost> getSmartPosts()
	{
		return this.smartPosts;
	}

	public List<String> getCities()
	{
		return this.cities;
	}

	public List<Item> getItems()
	{
		return this.items;
	}

	public void addItem(Item item)
	{
		this.items.add(item);
	}

	/**
	 * Fetch the SmartPost data from the web, and parse it and then add all the
	 * SmartPost stations to the list, and then get all unique cities to a separate list.
	 */
	private void fetchSmartPosts()
	{
		this.smartPosts.clear();
		SmartPostXMLParser parser = new SmartPostXMLParser();
		parser.parseSmartPostData(this.smartPosts);
		this.updateCities();
	}

	/**
	 * Update the list of unique cities from the list of all SmartPost stations.
	 */
	private void updateCities()
	{
		this.cities.clear();

		for (SmartPost post : this.smartPosts)
		{
			if (! this.cities.contains(post.getCity()))
			{
				this.cities.add(post.getCity());
			}
		}
	}

	/**
	 * Add some default items
	 */
	private void addDefaultItems()
	{
		this.addItem(new Item("Samsung Galaxy S2", true, new Size(5, 1, 10), 0.12f));
		this.addItem(new Item("Black & Decker", false, new Size(20, 5, 20), 1.2f));
		this.addItem(new Item("Loreal Shampoo", false, new Size(5, 12, 2), 0.25f));
		this.addItem(new Item("Kingston HyperX Cloud II", true, new Size(20, 15, 30), 0.6f));
	}
}
