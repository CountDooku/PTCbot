package gui.controller;

import java.io.File;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import gui.Main;
import gui.model.Accounts;
import gui.model.UserAgent;

public class AccountOverviewController implements UserAgent {

    @FXML
	public TableView<Accounts> accountTable;
    @FXML
    private TableColumn<Accounts, String> UsernameColumn;

    @FXML
	private Label UsernameLabel;
    @FXML
    private Label PasswordLabel;
    @FXML
    private Label ProxyLabel;
    @FXML
    private Label UserAgentLabel;

    @FXML
    private TextField UsernameField;
    @FXML
    private TextField PasswordField;
    @FXML
    private TextField ProxyField;
    @FXML
    private ComboBox<String> UserAgentBox;

    //set a ref. to MainApp.java
    protected Main mainApp;

    //default constructor, called before the initialize() method
    public AccountOverviewController() {

    }

    /*
     * initializes this controller class: "AccountOverviewController.java"
     *
     * initialize() is automatically called AFTER "AccountOverview.FXML" is loaded
     */
    @FXML
    private void initialize() {

        //initialize accountTable with a single column
        UsernameColumn.setCellValueFactory(cellData -> cellData.getValue().UsernameProperty());

        //non-sortable UserColumn == pretty UI
        UsernameColumn.setSortable(false);

        //clear account details
        showAccountDetails(null);

        //listen for selection changes, show account specific details when selected
        accountTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showAccountDetails(newValue));

    }

    //set a ref. to MainApp
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;

        //add observable list data to accountTable
        accountTable.setItems(mainApp.getAccountData());

    }


    /**
     * Fills all text fields to show details about the person.
     * If the specified person is null, all text fields are cleared.
     *
     * @param person the person or null
     * @return
     */

    //displays account data to FXML labels
    public void showAccountDetails(Accounts itemSelected) {

    	if (itemSelected != null) {
            //pulls data from gui.model.Accounts
            UsernameLabel.setText(itemSelected.getUsername());
            PasswordLabel.setText(itemSelected.getPassword());
            ProxyLabel.setText(itemSelected.getProxy());
            UserAgentLabel.setText(itemSelected.getUserAgent());

    	} else {

    		UsernameLabel.setText("");
            PasswordLabel.setText("");
            ProxyLabel.setText("uses default if left unspecified");
            UserAgentLabel.setText("uses default if left unspecified");

    	}

    }


    /*
     * when end-user clicks the "Add" button via "AccountOverview.FXML"
     * user is able to add a new account to the list of saved accounts
     *
     * "AccountEditor.FXML" is displayed, allowing them to do so
     */
    @FXML
    private void buttonAdd() {
        Accounts tempAccount = new Accounts();
        boolean okClicked = mainApp.showAccountAdd(tempAccount);
        if (okClicked) {
            mainApp.getAccountData().add(tempAccount);

            //save account data: in case end-user forgets to save before closing app
        	File file = mainApp.getAccountXMLFilePath();
        	mainApp.saveAccountsXML(file);
        }
    }

    /*
     * when end-user clicks the "Edit" button via "AccountOverview.FXML"
     * user is able to edit an account from the list of saved accounts
     *
     * "AccountEditor.FXML" is displayed, allowing user to make editable saves
     */
    @FXML
    private void buttonEdit() {
        Accounts selectedAccount = accountTable.getSelectionModel().getSelectedItem();
        if (selectedAccount != null) {
            boolean okClicked = mainApp.showAccountEditor(selectedAccount);
            if (okClicked) {
                showAccountDetails(selectedAccount);

                //save account data: in case end-user forgets to save before closing app
            	File file = mainApp.getAccountXMLFilePath();
            	mainApp.saveAccountsXML(file);
            }

        } else {

        	//if no account is selected, prompt user to do so before editing account info
        	noSelection();

        }
    }


    /*
     * when end-user clicks the "Delete" button via "AccountOverview.FXML"
     * user is able to delete an account from the list of saved accounts
     *
     * "AccountEditor.FXML" is displayed, allowing user to make choice deletes
     */
    @FXML
    private void buttonDelete() {

    	int selectedIndex = accountTable.getSelectionModel().getSelectedIndex();

    	if (selectedIndex >= 0) {
            accountTable.getItems().remove(selectedIndex);

            //save account data: in case end-user forgets to save before closing app
        	File file = mainApp.getAccountXMLFilePath();
        	mainApp.saveAccountsXML(file);

        } else {

            //if no account is selected, prompt user to do so before executing delete logic
            noSelection();

        }

    }


    /*
     * buttonLogIn() in "AccountOverview.FXML" opens "ConsoleView.FXML"
     *
     * the corresponding controller class to "ConsoleView.FXML" is "ConsoleController.java"
     * wherein: all Selenium WebDriver logic is configured
     * and: prints, step-by-step, all relevant processes pertaining to the FireFox browser's
     * activity conveniently, for the end-user, to a JavaFX defined TextArea "Console:"
     */
    @FXML
    public void buttonLogin() {

    	Accounts selectedAccount = accountTable.getSelectionModel().getSelectedItem();
        if (selectedAccount != null) {

            boolean okClicked = true;
            if (okClicked) {

            	//save account data at login: in case end-user forgets to save before closing app
            	File file = mainApp.getAccountXMLFilePath();
            	mainApp.saveAccountsXML(file);

               //change default scene "AccountOverview.FXML" to "ConsoleView.FXML"
               mainApp.showConsoleView(selectedAccount);

            }

        } else {

        	//if no account selected, prompt user to do so before executing buttonLogIn() logic
        	noSelection();

        }

    }


    //if no account is selected : prior to clicking either "add", "edit", "delete" or "login"
  	private void noSelection() {

  		Stage dialogStage = new Stage();
  	    GridPane grd_pan = new GridPane();

  	    grd_pan.setAlignment(Pos.CENTER);
  	    grd_pan.setHgap(10);
  	    grd_pan.setVgap(10);

  	    Scene scene = new Scene(grd_pan, 400, 150);
  	    dialogStage.setScene(scene);
  	    dialogStage.getIcons().add(new Image("res/bot32.png"));
  	    dialogStage.setMinWidth(400);
  	    dialogStage.setMaxWidth(400);
  	    dialogStage.setMinHeight(150);
  	    dialogStage.setMaxHeight(150);

  	    grd_pan.setStyle( "-fx-text-fill: #FFFFFF;" );
  	    grd_pan.setStyle( "-fx-background-color: #1d1d1d;" );

  	    dialogStage.setTitle("No Account Selected");
  	    dialogStage.initModality(Modality.WINDOW_MODAL);

  	    Label lab_alert = new Label("Please select an Account.");

  	    grd_pan.add(lab_alert, 0, 1);

  	    Button btn_ok = new Button("OKAY");
  	    btn_ok.setAlignment(Pos.BOTTOM_RIGHT);
  	    //if btn_ok is clicked...
  	    btn_ok.setOnAction(event -> {
  		dialogStage.hide();
  	    });


  	    grd_pan.setOnKeyPressed(new EventHandler<KeyEvent>() {
  	        @Override
  	        public void handle(KeyEvent event) {

  	            if (event.getCode().equals(KeyCode.ENTER)) {

  					//close dialogStage on ENTER
  	            	dialogStage.hide();

  	            } else {

  	            	dialogStage.hide();

  	            	}

  	        }

  	    });

  	    grd_pan.add(btn_ok, 4, 2);

  	    dialogStage.show();

  	}


}