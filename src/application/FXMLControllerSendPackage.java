package application;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Package.InvalidPackageException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

public class FXMLControllerSendPackage implements Initializable
{
	@FXML private ComboBox<Item> choiceboxItem;
	@FXML private RadioButton radioPackageClass1;
	@FXML private RadioButton radioPackageClass2;
	@FXML private RadioButton radioPackageClass3;
	@FXML private Tooltip tooltipClass1;
	@FXML private Tooltip tooltipClass2;
	@FXML private Tooltip tooltipClass3;
	@FXML private TextField textfieldName;
	@FXML private TextField textfieldWidth;
	@FXML private TextField textfieldLength;
	@FXML private TextField textfieldHeight;
	@FXML private TextField textfieldWeight;
	@FXML private CheckBox checkboxFragile;
	@FXML private Button buttonCreateItem;
	@FXML private Button buttonClearItem;
	@FXML private ComboBox<String> choiceboxSourceCity;
	@FXML private ComboBox<SmartPost> choiceboxSourcePost;
	@FXML private ComboBox<String> choiceboxTargetCity;
	@FXML private ComboBox<SmartPost> choiceboxTargetPost;
	@FXML private Button buttonSendPackage;
	@FXML private TextArea textareaMessages;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		List<String> cities = MihoCorporation.getInstance().getCities();
		this.choiceboxSourceCity.getItems().clear();
		this.choiceboxTargetCity.getItems().clear();
		this.choiceboxSourceCity.getItems().addAll(cities);
		this.choiceboxTargetCity.getItems().addAll(cities);
		this.choiceboxItem.getItems().clear();
		this.choiceboxItem.getItems().addAll(MihoCorporation.getInstance().getItems());

		this.choiceboxSourceCity.getSelectionModel().selectedItemProperty().addListener(
			new ChangeListener<String>()
			{
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
				{
					FXMLControllerSendPackage.this.updateSmartPosts(FXMLControllerSendPackage.this.choiceboxSourcePost, newValue);
				}
			});

		this.choiceboxTargetCity.getSelectionModel().selectedItemProperty().addListener(
				new ChangeListener<String>()
				{
					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
					{
						FXMLControllerSendPackage.this.updateSmartPosts(FXMLControllerSendPackage.this.choiceboxTargetPost, newValue);
					}
				});

		this.tooltipClass1.setText("1. luokan paketti on kaikista nopein pakettiluokka, jonka vuoksi sitä ei voi lähettää pidemmälle kuin 150 km päähän.\n" +
									"Onhan yleisesti tiedossa, että sen pidempää matkaa ei TIMO-mies jaksa pakettia kuljettaa.\n" +
									"1. luokan paketti on myös nopea, koska sen turvallisuudesta ei välitetä niin paljon,\n" +
									"jolloin kaikki särkyvät esineet tulevat menemään rikki matkan aikana.");
		this.tooltipClass2.setText("2. luokan paketit ovat turvakuljetuksia, jolloin ne kestävät parhaiten kaiken\n" +
									"särkyvän tavaran kuljettamisen. Näitä paketteja on mahdollista kuljettaa jopa\n" +
									"Lapista Helsinkiin, sillä matkan aikana käytetään useampaa kuin yhtä TIMO-miestä,\n" +
									"jolloin turvallinen kuljetus on taattu. Paketissa on kuitenkin huomattava, että jos\n" +
									"se on liian suuri, ei särkyvä esine voi olla heilumatta, joten paketin koon on oltava\n" +
									"pienempi kuin muilla pakettiluokilla.");
		this.tooltipClass3.setText("3. luokan paketti on TIMO-miehen stressinpurkupaketti. Tämä tarkoittaa sitä,\n" +
									"että TIMO-miehellä ollessa huono päivä pakettia paiskotaan seinien kautta automaatista\n" +
									"toiseen, joten paketin sisällön on oltava myös erityisen kestävää materiaalia.\n" +
									"Myös esineen suuri koko ja paino ovat eduksi, jolloin TIMO-mies ei jaksa heittää pakettia\n" +
									"seinälle kovin montaa kertaa. Koska paketit päätyvät kohteeseensa aina seinien kautta,\n" +
									"on tämä hitain mahdollinen kuljetusmuoto paketille. ");
	}

	@FXML
	void handleRadioBoxPackageClass(ActionEvent event)
	{
		this.radioPackageClass1.setSelected(false);
		this.radioPackageClass2.setSelected(false);
		this.radioPackageClass3.setSelected(false);

		((RadioButton) event.getSource()).setSelected(true);
	}

	@FXML
	void handleButtonClearItem(ActionEvent event)
	{
		this.clearItemFields();
	}

	@FXML
	void handleButtonCreateItem(ActionEvent event)
	{
		try
		{
			this.clearMessageArea();

			String name = this.textfieldName.getText();
			int width = Integer.parseInt(this.textfieldWidth.getText());
			int length = Integer.parseInt(this.textfieldLength.getText());
			int height = Integer.parseInt(this.textfieldHeight.getText());
			float weight = Float.parseFloat(this.textfieldWeight.getText());
			boolean fragile = this.checkboxFragile.isSelected();
			Size size = new Size(width, height, length);
			Item item = new Item(name, fragile, size, weight);

			MihoCorporation.getInstance().addItem(item);
			this.choiceboxItem.getItems().add(item);

			this.clearItemFields();
			this.choiceboxItem.setValue(item);
		}
		catch (NumberFormatException e)
		{
			this.textareaMessages.setText("Virhe luotaessa esinettä:\n" + e.getMessage());
		}
	}

	@FXML
	void handleButtonSendPackage(ActionEvent event)
	{
		this.clearMessageArea();

		SmartPost source = this.choiceboxSourcePost.getValue();
		SmartPost destination = this.choiceboxTargetPost.getValue();
		Item item = this.choiceboxItem.getValue();

		if (source != null && destination != null && item != null)
		{
			try
			{
				Package p = null;

				if (this.radioPackageClass1.isSelected())
				{
					p = new Package.PackageClass1(item.isFragile(), item.getSize(), item.getWeight());
				}
				else if (this.radioPackageClass2.isSelected())
				{
					p = new Package.PackageClass2(item.isFragile(), item.getSize(), item.getWeight());
				}
				else if (this.radioPackageClass3.isSelected())
				{
					p = new Package.PackageClass3(item.isFragile(), item.getSize(), item.getWeight());
				}

				if (p != null)
				{
					p.setItem(item);
					this.sendPackage(p, source, destination);
				}
				else
				{
					this.textareaMessages.setText("Virhe: Paketin luonti epäonnistui");
				}
			}
			catch (InvalidPackageException e)
			{
				this.textareaMessages.setText("Virhe: Esine ei sovi valittuun pakettiin.\n\n" + e.getMessage());
			}
		}
		else
		{
			this.textareaMessages.setText("Virhe: Valitse lähtöpaikka, kohdepaikka ja esine");
		}
	}

	private void sendPackage(Package p, SmartPost source, SmartPost destination)
	{
		p.setSmartPostRoute(source, destination);
		MihoCorporation.getInstance().getWarehouse().addPackage(p);
		this.textareaMessages.setText("Paketti '" + p.getItem().getName() + "' lisättiin varastoon");
	}

	private void clearItemFields()
	{
		this.textfieldName.setText("");
		this.textfieldWidth.setText("");
		this.textfieldLength.setText("");
		this.textfieldHeight.setText("");
		this.textfieldWeight.setText("");
		this.checkboxFragile.setSelected(false);
	}

	private void clearMessageArea()
	{
		this.textareaMessages.setText("");
	}

	private void updateSmartPosts(ComboBox<SmartPost> box, String city)
	{
		box.getItems().clear();
		List<SmartPost> posts = MihoCorporation.getInstance().getSmartPosts();

		for (SmartPost post : posts)
		{
			if (post.getCity().equals(city))
			{
				box.getItems().add(post);
			}
		}
	}
}
