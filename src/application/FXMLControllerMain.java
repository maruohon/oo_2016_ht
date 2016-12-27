package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class FXMLControllerMain implements Initializable
{
	@FXML private WebView webviewMap;
	@FXML private ComboBox<SmartPost> choiceSmartPost;
	@FXML private Button buttonAddSmartPost;
	@FXML private Button buttonAddAllSmartPosts;
	@FXML private Button buttonRemoveAllSmartPosts;
	@FXML private Button buttonManagePackages;
	@FXML private Button buttonRefreshMap;
	@FXML private Button buttonRemovePaths;

	private List<SmartPost> postsToAdd = new ArrayList<SmartPost>();
	private List<SmartPost> postsOnMap = new ArrayList<SmartPost>();

	@Override
	public void initialize(URL url, ResourceBundle bundle)
	{
		try
		{
			this.webviewMap.getEngine().load(this.getClass().getResource("index.html").toString());
		}
		catch (Exception e)
		{
		}

		this.resetSmartPostSelection();
	}

	/**
	 * Resets the SmartPost selection ComboBox so that the list
	 * contains all the available SmartPost units, and the selection is cleared.
	 */
	private void resetSmartPostSelection()
	{
		this.postsOnMap.clear();
		this.postsToAdd.clear();
		this.postsToAdd.addAll(MihoCorporation.getInstance().getSmartPosts());

		this.choiceSmartPost.getSelectionModel().clearSelection();
		this.choiceSmartPost.setValue(null);
		this.choiceSmartPost.getItems().clear();
		this.choiceSmartPost.getItems().addAll(this.postsToAdd);
	}

	/**
	 * Add one SmartPost unit to the map
	 */
	@FXML
	public void handleButtonAddSmartPost(ActionEvent event)
	{
		SmartPost post = this.choiceSmartPost.getValue();
		this.addSmartPostToMap(post);
	}

	/**
	 * Adds a SmartPost unit to the map, and removes that unit from the list
	 * and ComboBox of available units to-be-added.
	 */
	public void addSmartPostToMap(SmartPost post)
	{
		if (post != null && ! this.postsOnMap.contains(post))
		{
			String script = String.format("document.goToLocation('%s', '%s', '%s')",
					post.getAddress() + ", " + post.getPostalCode() + " " + post.getCity(),
					post.getPostOffice() + " " + post.getAvailability(),
					"blue"
			);

			this.webviewMap.getEngine().executeScript(script);
			this.postsToAdd.remove(post);
			this.choiceSmartPost.getItems().remove(post);
			this.choiceSmartPost.getSelectionModel().clearSelection();
			this.choiceSmartPost.setValue(null);
			this.postsOnMap.add(post);
		}
	}

	@FXML
	public void handleButtonAddAllSmartPosts(ActionEvent event)
	{
		int size = this.postsToAdd.size();

		for (int i = 0; i < size; i++)
		{
			// Always get the first element, because they get removed from the list
			// inside the method call!
			this.addSmartPostToMap(this.postsToAdd.get(0));
		}
	}

	/**
	 * Clears all the added SmartPost units from the map by reloading the webview element entirely,
	 * and then resets the list of available units and the ComboBox.
	 */
	@FXML
	public void handleButtonRemoveAllSmartPosts(ActionEvent event)
	{
		this.webviewMap.getEngine().load(this.getClass().getResource("index.html").toString());
		this.resetSmartPostSelection();
	}

	/**
	 * Send all the packages from the warehouse
	 */
	@FXML
	public void handleButtonSendPackagesFromWarehouse(ActionEvent event)
	{
		Warehouse warehouse = MihoCorporation.getInstance().getWarehouse();
		List<Package> packages = warehouse.getInventory();

		for (Package p : packages)
		{
			this.addPackageToMap(p);
		}

		packages.clear();
	}

	/**
	 * Remove all added paths from the Google map
	 */
	@FXML
	public void handleButtonRemovePaths(ActionEvent event)
	{
		this.webviewMap.getEngine().executeScript("document.deletePaths()");
	}

	/**
	 * Open another window to add items and create and send packages.
	 */
	@FXML
	public void handleButtonManagePackages(ActionEvent event)
	{
		try
		{
			Stage stage = new Stage();
			Parent page = FXMLLoader.load(this.getClass().getResource("FXMLPackage.fxml"));
			Scene scene = new Scene(page);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
		}
		catch (IOException e)
		{
			System.err.println("failed to load stuff");
		}
	}

	/**
	 * Adds a package to the Google map based on the source and destination SmartPost stations' GeoLocations.
	 * If the SmartPost stations themselves haven't been added to the map before, then they will also be added.
	 */
	private void addPackageToMap(Package p)
	{
		SmartPost source = p.getSource();
		SmartPost dest = p.getDestination();
		this.addSmartPostToMap(source);
		this.addSmartPostToMap(dest);

		ArrayList<Double> points = new ArrayList<Double>();
		points.add(source.getLocation().getLatitude());
		points.add(source.getLocation().getLongitude());
		points.add(dest.getLocation().getLatitude());
		points.add(dest.getLocation().getLongitude());

		String script = "document.createPath(" + points + ", 'red', " + p.getPackageClass() + ")";
		Object o = this.webviewMap.getEngine().executeScript(script);
		int dist = 0;

		try
		{
			dist = (int) o;
		}
		catch (Exception e)
		{
		}

		EventLogging.getInstance().sendPackage(p, dist);
	}
}
