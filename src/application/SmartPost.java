package application;

public class SmartPost
{
	private final String postalCode;
	private final String city;
	private final String address;
	private final GeoLocation location;
	private final String availability;
	private final String postOffice;

	public SmartPost(String postalCode, String city, String address,
					double latitude, double longitude,
					String availability, String postOffice)
	{
		this.postalCode = postalCode;
		this.city = city;
		this.address = address;
		this.location = new GeoLocation(latitude, longitude);
		this.availability = availability;
		this.postOffice = postOffice;
	}

	public String getPostalCode()
	{
		return this.postalCode;
	}

	public String getCity()
	{
		return this.city;
	}

	public String getAddress()
	{
		return this.address;
	}

	public GeoLocation getLocation()
	{
		return this.location;
	}

	public String getAvailability()
	{
		return this.availability;
	}

	public String getPostOffice()
	{
		return this.postOffice;
	}

	@Override
	public String toString()
	{
		return this.city + " " + this.postalCode + ", " + this.address;
	}
}
