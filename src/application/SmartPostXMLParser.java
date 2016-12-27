package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class SmartPostXMLParser
{
	private String url = "http://smartpost.ee/fi_apt.xml";
	private String content = "";

	public SmartPostXMLParser()
	{
	}

	public SmartPostXMLParser setUrl(String url)
	{
		this.url = url;
		return this;
	}

	private SmartPostXMLParser loadContent() throws IOException
	{
		URL url = new URL(this.url);
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
		this.content = "";
		String line;

		while ((line = br.readLine()) != null)
		{
			this.content += line + "\n";
		}

		return this;
	}

	private void parseXmlData(List<SmartPost> smartPostList) throws IOException, DOMException, SAXException, ParserConfigurationException, NumberFormatException
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new InputSource(new StringReader(this.content)));
		doc.getDocumentElement().normalize();

		NodeList nodes = ((Element) doc.getElementsByTagName("pprv2").item(0)).getElementsByTagName("place");
		int len = nodes.getLength();

		for (int i = 0; i < len; i++)
		{
			Node node = nodes.item(i);
			Element elementPlace = (Element) node;
			//System.out.println("i: " + i + " " + elementPlace.getTextContent());
			
			String postalCode	= this.getNodeValue(elementPlace, "code");
			String city			= this.getNodeValue(elementPlace, "city");
			String address		= this.getNodeValue(elementPlace, "address");
			String latitude		= this.getNodeValue(elementPlace, "lat");
			String longitude	= this.getNodeValue(elementPlace, "lng");
			String availability = this.getNodeValue(elementPlace, "availability");
			String postOffice	= this.getNodeValue(elementPlace, "postoffice");

			SmartPost post = new SmartPost(postalCode, city, address,
					Double.parseDouble(latitude), Double.parseDouble(longitude), availability, postOffice);

			smartPostList.add(post);
		}
	}

	private String getNodeValue(Element element, String tag) throws DOMException
	{
		Element el = (Element) element.getElementsByTagName(tag).item(0);

		if (el != null)
		{
			return el.getTextContent();
		}

		return "";
	}

	public void parseSmartPostData(List<SmartPost> smartPostList)
	{
		try
		{
			this.loadContent();
			this.parseXmlData(smartPostList);
		}
		catch (IOException | DOMException | ParserConfigurationException | SAXException | NumberFormatException e)
		{
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Exception while trying to load or parse data from '" + url + "'", e);
		}
	}
}
