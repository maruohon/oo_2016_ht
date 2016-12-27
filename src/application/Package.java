package application;

public abstract class Package
{
	private final int packageClass;
	private final boolean fragile;
	private final Size size;
	private final float maxWeight;
	private Item item;
	private SmartPost source;
	private SmartPost destination;

	private Package(int packageClass, boolean fragile, Size size, float weight)
	{
		this.packageClass = packageClass;
		this.fragile = fragile;
		this.size = size;
		this.maxWeight = weight;
	}

	public int getPackageClass()
	{
		return this.packageClass;
	}

	public boolean isFragile()
	{
		return this.fragile;
	}

	public Size getSize()
	{
		return this.size;
	}

	public float getMaxWeight()
	{
		return this.maxWeight;
	}

	public Item getItem()
	{
		return this.item;
	}

	public Package setItem(Item item) throws InvalidPackageException
	{
		// Only the 2. class packages can contain fragile items
		if (item.isFragile() && this.getPackageClass() != 2)
		{
			throw new InvalidPackageException("Only Class 2 packages can contain fragile items!");
		}

		if (item.getWeight() > this.getMaxWeight())
		{
			throw new InvalidPackageException("The item weight (" + item.getWeight() +
					") exceeds the maximum allowed weight (" + this.getMaxWeight() + ") for type " + this.getPackageClass() + " packages!");
		}

		// The size of this package can't fit the item that is being added
		if (! this.size.canContain(item.getSize()))
		{
			throw new InvalidPackageException("The item was larger than the package size!");
		}

		this.item = item;
		return this;
	}

	/*public boolean isValid(Item item)
	{

		if (item.isFragile() && this.getPackageClass() != 2)
		{
			return false;
		}

		return this.getMaxWeight() >= item.getWeight() && this.size.canContain(item.getSize());
	}*/

	public Package setSmartPostRoute(SmartPost source, SmartPost destination)
	{
		this.source = source;
		this.destination = destination;
		return this;
	}

	public SmartPost getSource()
	{
		return this.source;
	}

	public SmartPost getDestination()
	{
		return this.destination;
	}

	// These are just silly, but they are a requirement, so...

	public static class PackageClass1 extends Package
	{
		public PackageClass1(boolean fragile, Size size, float weight)
		{
			super(1, fragile, size, weight);
		}
	}

	public static class PackageClass2 extends Package
	{
		public PackageClass2(boolean fragile, Size size, float weight)
		{
			super(2, fragile, size, weight);
		}
	}

	public static class PackageClass3 extends Package
	{
		public PackageClass3(boolean fragile, Size size, float weight)
		{
			super(3, fragile, size, weight);
		}
	}

	public static class InvalidPackageException extends Exception
	{
		private static final long serialVersionUID = -5235438661523448327L;

		public InvalidPackageException()
		{
			super();
		}

		public InvalidPackageException(String message)
		{
			super(message);
		}
	}
}
