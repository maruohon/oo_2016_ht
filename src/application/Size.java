package application;

public class Size
{
	private int width;
	private int height;
	private int length;

	public Size(int width, int height, int length)
	{
		this.width = width;
		this.height = height;
		this.length = length;
	}

	public int getWidth()
	{
		return this.width;
	}

	public int getHeight()
	{
		return this.height;
	}

	public int getLength()
	{
		return this.length;
	}

	/**
	 * Checks whether all the dimensions of this object are equal or
	 * larger than those of the object <b>other</b>.
	 * @param other
	 * @return true if all the dimensions of this object are >= than those of <b>other</b>.
	 */
	public boolean canContain(Size other)
	{
		return this.getWidth() >= other.getWidth() &&
				this.getHeight() >= other.getHeight() &&
				this.getLength() >= other.getLength();
	}
}
