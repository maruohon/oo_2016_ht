package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventLogging
{
	private static EventLogging instance;
	private List<String> lines = new ArrayList<String>();

	private EventLogging()
	{
	}

	public static EventLogging getInstance()
	{
		if (instance == null)
		{
			instance = new EventLogging();
		}

		return instance;
	}

	private String getDateString()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	private void addHeader()
	{
		this.lines.add("====================================");
		this.lines.add(this.getDateString());
	}

	public void createItem(Item i)
	{
		this.addHeader();

		this.lines.add(String.format("Luotiin esine '%s', paino: %.2f kg, koko: %dx%dx%d cm",
				i.getName(), i.getWeight(), i.getSize().getWidth(), i.getSize().getHeight(), i.getSize().getLength()));
	}

	public void createPackage(Package p)
	{
		this.addHeader();

		this.lines.add(String.format("Luotiin %d:n luokan paketti esineelle '%s'", p.getPackageClass(), p.getItem().getName()));
	}

	public void sendPackage(Package p, int distance)
	{
		this.addHeader();

		this.lines.add(String.format("Paketti (%d luokka) sisältäen esineen '%s' lähti matkaan\n   Lähtöpaikka: %s\n   Määränpää: %s\n   Matkan pituus: %d km",
				p.getPackageClass(), p.getItem().getName(),
				this.getLocationString(p.getSource()), this.getLocationString(p.getDestination()), distance));
	}

	private String getLocationString(SmartPost post)
	{
		return post.getAddress() + ", " + post.getPostalCode() + " " + post.getCity();
	}

	@Override
	public String toString()
	{
		return String.join("\n", this.lines);
	}

	public void writeLogToFile(File file)
	{
		try
		{
			FileWriter fw = new FileWriter(file, true);
			fw.append(this.toString() + "\n");
			fw.close();
		}
		catch (IOException e)
		{
		}
	}
}
