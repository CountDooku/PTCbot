package gui;

import gui.controller.AccountEditorController;
import gui.controller.AccountOverviewController;
import gui.controller.AlephController;
import gui.controller.ConsoleController;
import gui.model.AccountWrapper;
import gui.model.Accounts;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Main extends Application {

	private Pane splashLayout;
	private Stage mainStage;
    private Stage primaryStage;
    private BorderPane rootLayout;
    private static final int SPLASH_WIDTH = 616;
	private static final int SPLASH_HEIGHT = 400;
    public String appTitle = "JillsClickCorner.com - PTC bot - Release Date: 09/10/15 - v2.0.0";

    //set up ObservableList for var "accountData"
    private ObservableList<Accounts> accountData = FXCollections.observableArrayList();

    //default Constructor
    public Main() {

        //prepopulated data: for when no user values are present
        accountData.add(new Accounts("PTC Bot", "f@kEpasswrd01", "127.0.0.1", "default"));

    }

    //return var accountData as ObservableList
    public ObservableList<Accounts> getAccountData() {
        return accountData;
    }


    @Override public void init() {

        ImageView splash = new ImageView(new Image("res/splashLogo.png"));
        splashLayout = new VBox();
        splashLayout.getChildren().addAll(splash);

      }



    //start method: sets up primaryStage w/"AccountOverview.FXML" as default Scene
    @Override
    public void start(final Stage initStage) {

    	showSplash(initStage);

        if (initStage.isShowing()) {

            initStage.toFront();
            initStage.centerOnScreen();
            initStage.getIcons().add(new Image("res/bot32.png"));

            FadeTransition displaySplash = new FadeTransition(Duration.seconds(6), splashLayout);
            displaySplash.setFromValue(1.0);

            displaySplash.setOnFinished(new EventHandler<ActionEvent>() {
              @Override public void handle(ActionEvent actionEvent) {

                initStage.hide();
                showMainStage();
            	showAccountOverview();

              }
            });
            displaySplash.play();

          }

    }


    private void showSplash(Stage initStage) {

        Scene splashScene = new Scene(splashLayout);
        splashScene.setFill(null);

        initStage.initStyle(StageStyle.TRANSPARENT);
        final Rectangle2D bounds = Screen.getPrimary().getBounds();
        initStage.setScene(splashScene);
        initStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2);
        initStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2);
        initStage.show();

      }


    private void showMainStage() {

        mainStage = new Stage(StageStyle.DECORATED);

        mainStage.setTitle(appTitle);
        mainStage.getIcons().add(new Image("res/bot32.png"));
        mainStage.setMinWidth(616);
        mainStage.setMaxWidth(616);
        mainStage.setMinHeight(400);
        mainStage.setMaxHeight(400);

        try {

            //load rootLayout from "Aleph.FXML"
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/Aleph.fxml"));

            rootLayout = (BorderPane) loader.load();

            //display scene containing rootLayout
            Scene scene = new Scene(rootLayout);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

            mainStage.setScene(scene);
            mainStage.show();

            //give controller access to the MainApp
            AlephController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
         * load last opened "account_list.XML" file from OS-specific registry,
         * via call to method "getAccountXMLFilePath()"
         *
         * "account_list.XML" is included with program by default, though user may rename
         * &&|| delete this at any time.
         */
        File file = getAccountXMLFilePath();
        if (file != null) {
        	loadAccountsXML(file);
        }

      }


    //load "AccountOverview.FXML" as default scene in rootLayout for primaryStage
    public void showAccountOverview() {

        try {
            //display "AccountOverview.FXML"
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/AccountOverview.fxml"));
            AnchorPane accountOverview = (AnchorPane) loader.load();

            //set "AccountOverview.FXML" in center of rootLayout
            rootLayout.setCenter(accountOverview);

            //give controller access to MainApp
            AccountOverviewController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /*
     * load "ConsoleView.FXML" as next scene in rootLayout for primaryStage
     * once method buttonLogIn() is executed in "AccountOverview.FXML"
     */
    public void showConsoleView(Accounts itemSelected) {

    	try {
            //display "ConsoleView.FXML"
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/ConsoleView.fxml"));

            AnchorPane ConsoleView = (AnchorPane) loader.load();

            //set "ConsoleView.FXML" in center of rootLayout
            rootLayout.setCenter(ConsoleView);

            //give controller access to MainApp
            ConsoleController controller = loader.getController();
            controller.botLogin(itemSelected);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /*
     * displays "AccountEditor.FXML" dialogStage for account specified.
     *
     * if end-user clicks "OK", changes are saved and "isOkClicked()" returns True,
     * otherwise, boolean method showAccountEditor() remains False.
     */
    public boolean showAccountAdd(Accounts accSelector) {

        try {
            //display "AccountEditor.fxml" && create a new stage for the pop-up dialog
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/AccountEditor.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            //create the dialogStage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Account");
            dialogStage.getIcons().add(new Image("res/bot32.png"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            //set accounts from "account_list.XML" into "AccountEditorController.java"
            AccountEditorController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setAccount(accSelector);
            controller.setData(accSelector);

            //display dialogStage && wait until user closes it
            dialogStage.showAndWait();

            //if button "OK" clicked: return True && save persist change in data to Object
            //otherwise: remain False/Unclicked, && do not persist change in data
            return controller.isOkClicked();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    /*
     * displays "AccountEditor.FXML" dialogStage for account specified.
     *
     * if end-user clicks "OK", changes are saved and "isOkClicked()" returns True,
     * otherwise, boolean method showAccountEditor() remains False.
     */
    public boolean showAccountEditor(Accounts accSelector) {

        try {
            //display "AccountEditor.fxml" && create a new stage for the pop-up dialog
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/AccountEditor.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            //create the dialogStage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Account");
            dialogStage.getIcons().add(new Image("res/bot32.png"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            //set accounts from "account_list.XML" into "AccountEditorController.java"
            AccountEditorController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setAccount(accSelector);
            controller.setData(accSelector);

            //display dialogStage && wait until user closes it
            dialogStage.showAndWait();

            //if button "OK" clicked: return True && save persist change in data to Object
            //otherwise: remain False/Unclicked, && do not persist change in data
            return controller.isOkClicked();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    /*
     * returns "account_list.XML" file location as defined by user-preference,
     * which is persisted by end-user, via OS-specific registry.
     *
     * if user preference isn't found, or if user hasn't set file path preference: return null
     */
    public File getAccountXMLFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }


    /*
     * sets file-path of currently loaded "account_list.XML" file
     *
     * the path is persisted via end-user's OS-specific registry
     */
    public void setAccountXMLFilePath(File file) {

        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());

            //update Stage title
            primaryStage.setTitle(appTitle);

        } else {
            prefs.remove("filePath");

        }
    }


    /*
     * load accountData from "account_list.XML"
     *
     * first: clear all data (current accountData, if already loaded)
     * then: add it to accountTable (replace with loaded accountData from "account_list.XML")
     */
    public void loadAccountsXML(File file) {

        try {
            JAXBContext context = JAXBContext
                    .newInstance(AccountWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            //read XML from "account_list.XML" && unmarshall it
            AccountWrapper helper = (AccountWrapper) um.unmarshal(file);

            //clear all data *first* before we add it to the accountTable
            accountData.clear();
            accountData.addAll(helper.getAccountData());

            //save user specified OS-specific file path to registry for future access
            //via method: setAccountXMLFilePath()
            setAccountXMLFilePath(file);

          //catch Exception(s)
        } catch (Exception e) {

        }


    }

    //save accountData to specified "account_list.XML" file
    public void saveAccountsXML(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(AccountWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            //wrap accountData
            AccountWrapper helper = new AccountWrapper();
            helper.setAccountData(accountData);

            //marshall accountData and save XML to "account_list.XML"
            m.marshal(helper, file);

            //save end-user appointed file path to OS-specific registry
            setAccountXMLFilePath(file);

          //catch Exception(s)
        } catch (Exception e) {

        }
    }

    //returns primaryStage
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    //main() == EntryPoint
    public static void main(String[] args) {
        launch(args);
    }

}