package application;

import java.util.ArrayList;
import java.util.List;

public class Warehouse
{
	private List<Package> inventory;

	public Warehouse()
	{
		this.inventory = new ArrayList<Package>();
	}

	public List<Package> getInventory()
	{
		return this.inventory;
	}

	public Warehouse addPackage(Package p)
	{
		this.inventory.add(p);
		EventLogging.getInstance().createPackage(p);
		return this;
	}

	public void removePackage(Package p)
	{
		this.inventory.remove(p);
	}
}
