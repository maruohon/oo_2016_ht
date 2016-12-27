package application;

public class GeoLocation
{
	private final double latitude;
	private final double longitude;

	public GeoLocation(double latitude, double longitude)
	{
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude()
	{
		return this.latitude;
	}

	public double getLongitude()
	{
		return this.longitude;
	}
}
