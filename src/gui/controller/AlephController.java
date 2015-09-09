package gui.controller;

import gui.Main;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.fxml.FXML;
import javafx.stage.FileChooser;

	/*
	 * controller for rootLayout "Aleph.FXML"
	 *
	 * handles all associated menu-related actions
	 */
	public class AlephController {

	    //set a reference to Main.java
	    private Main mainApp;

	    //called by MainApp.java to give a reference to self
	    public void setMainApp(Main mainApp) {
	        this.mainApp = mainApp;
	    }

	    /*
	     * clears currently loaded accountData from accountTable
	     *
	     * allows user to create a new "account_list.XML" file
	     */
	    @FXML
	    private void handleNew() {
	        mainApp.getAccountData().clear();
	        mainApp.setAccountXMLFilePath(null);
	    }

	    /*
	     * opens a FileChooser
	     *
	     * user selects an "account_list.XML" file to load
	     * may be default "account_list.XML" file included w/program || custom/other
	     */
	    @FXML
	    private void handleOpen() {
	        FileChooser fileChooser = new FileChooser();

	        //set extension filter for JAXB (XML files only)
	        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
	                "XML files (*.xml)", "*.xml");
	        fileChooser.getExtensionFilters().add(extFilter);

	        //load selected file via method in "MainApp.java" loadAccountsXML()
	        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

	        if (file != null) {
	            mainApp.loadAccountsXML(file);
	        }
	    }

	    /*
	     * save currently open "account_list.XML file" w/call to method saveAccountsXML()
	     *
	     * if there is no currently open "account_list.XML" file,
	     * utilize method handleSaveAs() to save it
	     */
	    @FXML
	    private void handleSave() {
	        File personFile = mainApp.getAccountXMLFilePath();
	        if (personFile != null) {
	            mainApp.saveAccountsXML(personFile);
	        } else {
	            handleSaveAs();
	        }
	    }

	    /*
	     * open FileChooser w/XML file-type constraint to save current "account_list.XML" file
	     */
	    @FXML
	    private void handleSaveAs() {
	        FileChooser fileChooser = new FileChooser();

	        //extension filter (XML only)
	        FileChooser. ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
	                "XML files (*.xml)", "*.xml");
	        fileChooser.getExtensionFilters().add(extFilter);

	        //display save dialog
	        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

	        if (file != null) {
	            //check extension for valid XML file type
	            if (!file.getPath().endsWith(".xml")) {
	                file = new File(file.getPath() + ".xml");
	            }
	            mainApp.saveAccountsXML(file);
	        }
	    }


	    @FXML
	    private void handleExit() {
	        System.exit(0);
	    }


	    //Extras > "Spin The Wheel" (end-user can spin wheel once every 24 hours)
	    @FXML
	    public void spinWheelOpen() throws IOException, URISyntaxException {

	    	String url = "http://www.jillsclickcorner.com/games/view.php?id=1";
	    	java.awt.Desktop.getDesktop().browse(new URI(url));

	    }


	    //app "easter egg" : Help > "What is... ?"
	    @FXML
	    public void whatIsLove() throws IOException, URISyntaxException {

	    	String url = "https://www.youtube.com/watch?v=HEXWRTEbj1I";
	    	java.awt.Desktop.getDesktop().browse(new URI(url));

	    }


	    /*
	     * The infamous "About" Dialog: for company-theatrics, and other displays of
	     * viable adoration. Including, though not without pretense, a link to the
	     * owner's services &&|| other products of a commercial nature w/intention
	     * of securing even more revenue.
	     */
	    @FXML
	    public void displayMySite() throws IOException, URISyntaxException {

	    	String url = "http://countdooku.github.io/"; //MY PROJECTS
	    	java.awt.Desktop.getDesktop().browse(new URI(url));

	    }

}