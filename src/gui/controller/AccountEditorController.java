package gui.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import gui.model.Accounts;
import gui.model.UserAgent;

public class AccountEditorController implements UserAgent {

	@FXML
    private TextField UsernameField;
    @FXML
    private TextField PasswordField;
    @FXML
    private TextField ProxyField;
    @FXML
    private ComboBox<String> UserAgentBox;

    private Stage dialogStage;
    private Accounts accSelector;
    private boolean okClicked = false;


    /*
     * initializes this controller class.
     *
     * initialize() is automatically called after "AccountEditor.FXML" has been loaded
     */
    @FXML
    private void initialize() {
    }


    /*
     * set stage of this dialog
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }


    /*
     * sets the account to be edited in dialog
     */
    public void setAccount(Accounts accSelector) {
        this.accSelector = accSelector;

        UsernameField.setText(accSelector.getUsername());
        PasswordField.setText(accSelector.getPassword());
        ProxyField.setText(accSelector.getProxy());
        UserAgentBox.setValue(accSelector.getUserAgent());
    }


    //sets UA info from "UserAgent.java" interface to ComboBox in "AccountEditor.FXML"
    public void setData(Accounts accSelector) {

    	this.accSelector = accSelector;

    	UserAgentBox.setPromptText(accSelector.getUserAgent());

    	UserAgentBox.getItems().addAll(
    				applicationVersionFF_25_a,
    				applicationVersionFF_25_b,
    				applicationVersionC_310165016_a,
    				applicationVersionC_31016230_a,
    				applicationVersionC_300159917_a,
    				applicationVersionC_290154762_a,
    				applicationVersionC_290154757_a,
    				applicationVersionC_29015472_a,
    				applicationVersionC_28014680_a,
    				applicationVersionC_28014670_a,
    				applicationVersionC_28014640_a,
    				applicationVersionC_270145393_a,
    				applicationVersionC_270145393_b,
    				applicationVersionC_270145393_c,
    				applicationVersionC_270145393_d,
    				applicationVersionC_270145393_e,
    				applicationVersionC_270145393_f,
    				applicationVersionC_2701453116_a,
    				applicationVersionC_240131260_a,
    				applicationVersionC_24013090_a,
    				applicationVersionC_24012950_a,
    				applicationVersionC_24012920_a,
    				applicationVersionC_24012901_a,
    				applicationVersionC_24012901_b,
    				applicationVersionC_24012901_c,
    				applicationVersionC_24012901_d,
    				applicationVersionFF_25_a,
    				applicationVersionFF_25_b,
    				applicationVersionFF_24_a,
    				applicationVersionFF_24_b,
    				applicationVersionFF_24_c,
    				applicationVersionFF_23_a,
    				applicationVersionFF_23_b,
    				applicationVersionFF_23_c,
    				applicationVersionFF_22_a,
    				applicationVersionFF_22_b,
    				applicationVersionFF_22_c,
    				applicationVersionFF_21001_a,
    				applicationVersionFF_21001_b,
    				applicationVersionFF_2100_a,
    				applicationVersionFF_21_b,
    				applicationVersionFF_21_c,
    				applicationVersionFF_21_d,
    				applicationVersionFF_21_e,
    				applicationVersionFF_21_f,
    				applicationVersionFF_21_g,
    				applicationVersionFF_21_h,
    				applicationVersionFF_21_i,
    				applicationVersionFF_21_j,
    				applicationVersionFF_21_k,
    				applicationVersionFF_21_l,
    				applicationVersionFF_21_m,
    				applicationVersionFF_21_n,
    				applicationVersionFF_21_o,
    				applicationVersionFF_21_p,
    				applicationVersionIE_10_a,
    				applicationVersionIE_10_b,
    				applicationVersionIE_10_c,
    				applicationVersionIE_10_d,
    				applicationVersionIE_10_e,
    				applicationVersionIE_10_f,
    				applicationVersionIE_10_g,
    				applicationVersionIE_9_a,
    				applicationVersionIE_9_b,
    				applicationVersionIE_9_c,
    				applicationVersionIE_9_d,
    				applicationVersionIE_9_e,
    				applicationVersionIE_9_f,
    				applicationVersionIE_9_g,
    				applicationVersionIE_9_h,
    				applicationVersionIE_9_i,
    				applicationVersionIE_9_j,
    				applicationVersionIE_9_k,
    				applicationVersionIE_9_l,
    				applicationVersionIE_9_m,
    				applicationVersionIE_9_n,
    				applicationVersionIE_9_o,
    				applicationVersionIE_9_p,
    				applicationVersionIE_9_q,
    				applicationVersionIE_9_r,
    				applicationVersionIE_9_s,
    				applicationVersionIE_9_t,
    				applicationVersionS_60_a,
    				applicationVersionS_517_a,
    				applicationVersionS_513_a,
    				applicationVersionS_51_a,
    				applicationVersionS_505_a,
    				applicationVersionS_505_b,
    				applicationVersionS_504_a,
    				applicationVersionS_504_b,
    				applicationVersionS_504_c,
    				applicationVersionS_504_d,
    				applicationVersionS_504_e,
    				applicationVersionS_504_f,
    				applicationVersionS_504_g,
    				applicationVersionS_504_h,
    				applicationVersionS_504_i,
    				applicationVersionS_504_j,
    				applicationVersionS_504_k,
    				applicationVersionS_504_l,
    				applicationVersionS_504_m,
    				applicationVersionS_504_n,
    				applicationVersionS_504_o,
    				applicationVersionS_504_p,
    				applicationVersionS_504_q,
    				applicationVersionS_504_r,
    				applicationVersionS_504_s,
    				applicationVersionS_504_t,
    				applicationVersionS_503_a,
    				applicationVersionS_503_b,
    				applicationVersionS_503_c,
    				applicationVersionS_503_d,
    				applicationVersionS_503_e,
    				applicationVersionS_503_f,
    				applicationVersionS_503_g,
    				applicationVersionS_503_h,
    				applicationVersionS_503_i,
    				applicationVersionS_503_j,
    				applicationVersionS_503_k,
    				applicationVersionS_503_l,
    				applicationVersionS_503_m,
    				applicationVersionS_503_n,
    				applicationVersionS_503_o);

    	}


    public boolean isOkClicked() {
        return okClicked;
    }


    /*
     * called when user clicks "OK"
     */
    @FXML
    private void handleOk() {

        if (isInputValid()) {

        	accSelector.setUsername(UsernameField.getText());
        	accSelector.setPassword(PasswordField.getText());
        	accSelector.setProxy(ProxyField.getText());
        	accSelector.setUserAgent(UserAgentBox.getValue());

            okClicked = true;
            dialogStage.close();
        }
    }


    @FXML
    private void handleCancel() {
        dialogStage.close();
    }


    /*
     * validates user input in text fields
     */
    private boolean isInputValid() {

        String errorCheck = "";

        if (UsernameField.getText() == null || UsernameField.getText().length() == 0) {
            errorCheck += "Please enter a Username.\n";
        }

        if (PasswordField.getText() == null || PasswordField.getText().length() == 0) {
            errorCheck += "Please enter a Password.\n";
        }


        if (errorCheck.length() == 0) {
            return true;

        } else {

        	//display helpful pop-up for end-user
        	validStage();

        }
		return okClicked;
    }



    //Invalid Fields : pop-up if no value for username && || password
  	private void validStage() {

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

  	    dialogStage.setTitle("Invalid Fields");
  	    dialogStage.initModality(Modality.WINDOW_MODAL);

  	    Label lab_alert = new Label("Please enter a Username and Password combo.");

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

  	    grd_pan.add(btn_ok, 5, 2);

  	    dialogStage.show();

  	}

}