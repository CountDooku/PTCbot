package gui.controller;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import gui.model.Accounts;
import gui.model.UserAgent;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ConsoleController implements UserAgent {

	@FXML
	private TextArea ta;

	static String ptcPage = "http://jillsclickcorner.com/members/clickPTC.php";
	static String loginPage = "http://jillsclickcorner.com/members/login.php?path=members%2FclickPTC.php";

	public void botLogin(Accounts itemSelected) {

			 //display informative message to user Immediately, so they know it's Instantly-Responding
			 ta.appendText("starting...");

			 /*
			  * performs worker-task on non-UI thread, displays message to user on the UI thread
			  *
			  * background work is done on a non-UI thread to prevent UI lag
			  */
			 Service<Void> service = new Service<Void>() {
			        @Override
			        protected Task<Void> createTask() {
			            return new Task<Void>() {
			                @Override
			                protected Void call() throws Exception {


			                	final CountDownLatch latch = new CountDownLatch(1);

			                	//WebDriver
			                	driverConstructor(latch, itemSelected);


			                    //next process...


			                    return null;
			                }
			            };
			        }
			    };
			    service.start();

		}


	//construct the FireFoxDriver instance and execute the suite
	public void driverConstructor(CountDownLatch latch, Accounts itemSelected) {

		 /*
		 * turn off ALL javascript/css errors in developer IDE, this has no effect in the
 	     * javaFX developer-created console for end-user
 	     */
 		 java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
 		 System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");


 		 //PROXY
 		 String tempProxy = itemSelected.getProxy();
 		 org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
 			proxy.setHttpProxy(tempProxy)
 			     .setFtpProxy(tempProxy)
 			     .setSslProxy(tempProxy);
 			DesiredCapabilities cap = new DesiredCapabilities();
 			cap.setCapability(CapabilityType.PROXY, proxy);

 			//blocks all spammy pop-ups via jills click affiliates which would otherwise throw the program off sync
 			cap.setCapability(CapabilityType.SUPPORTS_ALERTS, true);
 			cap.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, "dismiss");


 			//establish/create new FirefoxProfile();
 			FirefoxProfile profile = new FirefoxProfile();

 			//set user-configured UA entry and override default UA setting
 			String tempUA = itemSelected.getUserAgent();
 			profile.setPreference("general.useragent.override", tempUA);


 			//FF Binary for FirefoxDriver(); constructor
 			FirefoxBinary ffBinary = new FirefoxBinary();


 			//configure WebDriver
			WebDriver driver = new FirefoxDriver(ffBinary, profile, cap);

			//get Proxy
			String prox_OUT = proxy.getHttpProxy();

			//get userAgent
			String s = (String)
					((JavascriptExecutor) driver).executeScript("return navigator.userAgent;");


			//EXECUTE THE SUITE
 			proxyAuthenticator(proxy, prox_OUT, itemSelected, driver, latch);
 			uaAuthenticator(driver, latch, s, itemSelected);
 			proxyUserAgentMatch(driver, latch, prox_OUT, s, itemSelected);
 			LogIn(itemSelected, driver, latch);
			clickLinks(driver, latch);

	}



	//PROPER PROXY CONFIGURATION WILL REQUIRE ASSISTANCE FROM a service like newipnow.com
	//otherwise: I can say that the proxy is anything, that it matches
	//but... someone can see that it's not routing through that proxy and am using my default IP
	public void proxyAuthenticator(Proxy proxy, String prox_OUT, Accounts itemSelected,
			WebDriver driver, CountDownLatch latch) {

			String tempProxy = itemSelected.getProxy();

			/*
			 * internal proxy check via Selenium (as opposed to external w/URL)
			 *
			 * String prox_OUT = proxy.getHttpProxy();
			 * if prox_OUT is == to user-specified Proxy "tempProxy" continue
			 * if not: ABORT!
			 *
			 */
			if(prox_OUT.equals(tempProxy)) {

      	    Platform.runLater(new Runnable() {
                  @Override
                  public void run() {
                      try{
                      	ta.appendText(System.lineSeparator() + System.lineSeparator()
                      			+ prox_OUT + " @ " + tempProxy + " (Successful: Proxy Matched)");

                      }finally{
                          latch.countDown();
                      }
                  }
              });

			} else {

				Platform.runLater(new Runnable() {
	                   @Override
	                   public void run() {
	                       try{

	                       	ta.appendText(System.lineSeparator() + System.lineSeparator()
	                       			+ prox_OUT + " @ " + tempProxy + " (ABORT!: Proxy Not Matched)");

	                       }finally{
	                           latch.countDown();
	                       }
	                   }
	               });

				driver.quit();

			}

	}


	//gui.Model.UserAgent (120+ UserAgent models to choose from, for absolute anonymity)
	public void uaAuthenticator(WebDriver driver, CountDownLatch latch, String s, Accounts itemSelected) {

		    String tempUA = itemSelected.getUserAgent();

			//if userAgent is matched
			if(s.equals(tempUA)) {

				Platform.runLater(new Runnable() {
                   @Override
                   public void run() {
                       try{
                       	ta.appendText(System.lineSeparator() + System.lineSeparator()
                       			+ s + " @ " + tempUA + " (Successful: UserAgent Matched)");

                       }finally{
                           latch.countDown();
                       }
                   }
               });

			} else {

				Platform.runLater(new Runnable() {
	                   @Override
	                   public void run() {
	                       try{

	                       	ta.appendText(System.lineSeparator() + System.lineSeparator()
	                       			+ s + " @ " + tempUA + " (ABORT!: UserAgent Not Matched)");

	                       }finally{
	                           latch.countDown();
	                       }
	                   }
	               });

				driver.quit();

			}

	}


	public void proxyUserAgentMatch(WebDriver driver, CountDownLatch latch, String prox_OUT,
			String s, Accounts itemSelected) {

		String tempUA = itemSelected.getUserAgent();
		String tempProxy = itemSelected.getProxy();

			//if proxy && userAgent matched
			if(prox_OUT.equals(tempProxy) && s.equals(tempUA)) {

				driver.get(loginPage);

				Platform.runLater(new Runnable() {
                   @Override
                   public void run() {
                       try{

                       	ta.appendText(System.lineSeparator() + System.lineSeparator()
                       			+ loginPage + " @ " + loginPage + " (Successful: LogIn Page Accessed)");

                       }finally{
                           latch.countDown();
                       }
                   }
               });


			} else if(!prox_OUT.equals(tempProxy) || !s.equals(tempUA)) {

				Platform.runLater(new Runnable() {
                   //@Override
                   public void run() {
                       try{

                       	ta.appendText(System.lineSeparator() + System.lineSeparator()
                       			+ "(ABORT!: Proxy and/or UserAgent do not match.)");

                       }finally{
                           latch.countDown();
                       }
                   }
               });

				driver.quit();

			}

	}


	public void LogIn(Accounts itemSelected, WebDriver driver, CountDownLatch latch) {

			//if not logged in : log in
			String i2 = driver.getCurrentUrl();

			if(i2.equals(loginPage)) {

				//user-specified login info
				String tempUser = itemSelected.getUsername();
				String tempPass = itemSelected.getPassword();

				//auto-fill username into form field for end-user
				driver.findElement(By.xpath("html/body/table/tbody/tr[3]/td[2]/table/tbody/tr[2]/td/form/table/tbody/tr[1]/td[2]/input")).clear();
				driver.findElement(By.xpath("html/body/table/tbody/tr[3]/td[2]/table/tbody/tr[2]/td/form/table/tbody/tr[1]/td[2]/input")).sendKeys(tempUser);

				//auto-fill password into form field for end-user
				driver.findElement(By.xpath("html/body/table/tbody/tr[3]/td[2]/table/tbody/tr[2]/td/form/table/tbody/tr[2]/td[2]/input")).clear();
				driver.findElement(By.xpath("html/body/table/tbody/tr[3]/td[2]/table/tbody/tr[2]/td/form/table/tbody/tr[2]/td[2]/input")).sendKeys(tempPass);

				//auto-select captcha field for end-user focus
				driver.findElement(By.xpath("html/body/table/tbody/tr[3]/td[2]/table/tbody/tr[2]/td/form/table/tbody/tr[3]/td[2]/div[1]/img")).click();

				//helpful end-user message
				ta.appendText(System.lineSeparator() + System.lineSeparator() + "do captcha prompt...");

				//nl : formatting the console TextArea
				ta.appendText(System.lineSeparator() + "");



				//send helpful prompt: end-user still has to enter captcha and click "login" (due to captcha)
				Platform.runLater(new Runnable() {
                   @Override
                   public void run() {
                       try{

                    	    //display helpful message to end-user : "Manually enter captcha, then press ENTER to begin."
                    	    captchaPrompt(driver);

                       } finally {
                           latch.countDown();
                       }
                   }

               });

			}

	}


	/*
	 * check for any available PTC links.
	 * if no links found: start timer, and wait.
	 * else, if PTC links are available: click them every X seconds.
	 */
	public void clickLinks(WebDriver driver, CountDownLatch latch) {


		//perform check for visibility of links before attempting to click them - By.xpath() [EXPLICIT WAIT : 30 SECONDS]
		WebElement noLinks = (new WebDriverWait(driver, 30)).
			until(ExpectedConditions.visibilityOfElementLocated(By.
						xpath("/html/body/table/tbody/tr[3]/td[2]/table/tbody/tr[2]/td/div[3]")));

		WebElement noLinksText = (new WebDriverWait(driver, 30)).
				until(ExpectedConditions.visibilityOfElementLocated(By.
						xpath("/html/body/table/tbody/tr[3]/td[2]/table/tbody/tr[2]/td/div[4]")));


		//rand() mins to wait before next PTC click (simulates human-user)
		Random randNum0 = new Random();
		int minRand0 = 900000;  //15mins
		int maxRand0 = 1800000; //30mins

		int clickLinksTimer = randNum0.nextInt((maxRand0 - minRand0) + 1) + minRand0;
		long minutes = TimeUnit.MILLISECONDS.toMinutes(clickLinksTimer);


		//if there are no available PTC links: display this info to the user console, and a visible count down until next attempt.
		if(noLinks.getText().equals("There is no paid to click advertisements at this time.") ||
				noLinksText.getText().equals("There is no paid to click advertisements at this time.")) {

			//reports "Checking back in... minutes"
			checkingBackIn(minutes, driver, latch);

			Platform.runLater(new Runnable() {
	               @Override
	               public void run() {
	                   try{

	                		ta.appendText(System.lineSeparator() + System.lineSeparator() +
	                				"No PTC links available at this time.");

	                   } finally {
	                       latch.countDown();
	                   }
	               }
				});


			try {
				Thread.sleep(clickLinksTimer);
				zombieClickLinks(driver, latch);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}


		} else {

		//detect visibility of link element before attempting to click it (by cssSelector)
		//note: does not work with xPath
		WebElement element = (new WebDriverWait(driver, 10)).
				until(ExpectedConditions.visibilityOfElementLocated(By.
						cssSelector("html body table tbody tr td table tbody tr td.content div a b")));


		if(element.isDisplayed()) {

			//detect visibility of link element before attempting to click it (by cssSelector)
			//note: does not work with xPath
			WebElement onElement = driver.findElement(By.
					cssSelector("html body table tbody tr td table tbody tr td.content div a b"));

					//copy link URL to clipboard
					Actions action = new Actions(driver);
					action.contextClick(onElement).sendKeys("a").
					build().perform();


					//paste link to create accessible variable
					String getHashURL = "";
					try {

						String tempHash = (String) Toolkit.getDefaultToolkit().
								getSystemClipboard().getData(DataFlavor.stringFlavor);

						getHashURL = tempHash;

					} catch (HeadlessException | UnsupportedFlavorException
							| IOException e) {
						e.printStackTrace();
					}


					//go to PTC link
					driver.get(getHashURL);

					//count down in UI: user sets min and max value to wait
					//(user control for this feature not implemented... yet)
					int minRand = 20000;
					int maxRand = 30000;
					Random randNum = new Random();

					int sleepClick = randNum.nextInt((maxRand - minRand) + 1) + minRand;
					long seconds = TimeUnit.MILLISECONDS.toSeconds(sleepClick);


					//reports "Clicking next link in ... seconds"
					clickingIn(seconds, latch);


					try {
						Thread.sleep(sleepClick);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}


					//handles unsolicited window popups coming from PTC click ads
					//if unhandled: program execution flow is botched
					String parent = driver.getWindowHandle();
			        Set<String> children = driver.getWindowHandles();
			        Iterator<String> it = children.iterator();

			        while (it.hasNext()) {

			            String popupHandle=it.next().toString();

			            if(!popupHandle.contains(parent)) {

			            driver.switchTo().window(popupHandle);
			            System.out.println(driver.switchTo().window(popupHandle).getTitle());
			            driver.close();

			            }

			        }


					zombieClickLinks(driver, latch);


			} else {

				Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try{

                        	ta.appendText(System.lineSeparator()
                        			+ "Greetings Earthling, there appears to be a problem. If program " +
                        			"fails to execute within the next, oh... let's say two minutes. Just "
                        			+ "shut it down and start it up again. Hail, Lord Xenu!");

                        }finally{
                            latch.countDown();
                        }
                    }
                });

				driver.quit();

			}
		}
	}


	//an "infinite loop" back in to clickLinks(), until there are no more PTC links to click
	public void zombieClickLinks(WebDriver driver, CountDownLatch latch) {

	            driver.get(ptcPage);

	            checkAlert(driver);

	            clickLinks(driver, latch);

	}


		//check for pop-ups from PTC ads, and handle them automatically
		public void checkAlert(WebDriver driver) {
		    try {
		        WebDriverWait wait = new WebDriverWait(driver, 2);
		        wait.until(ExpectedConditions.alertIsPresent());
		        Alert alert = driver.switchTo().alert();
		        alert.accept();
		    } catch (Exception e) {

		    }
		}


		//after the "infinite loop" between clickLinks() and zombieClickLinks() has expired
		//try again: anywhere from 15 to 30 minutes (simulating human-user input)
		public void checkingBackIn(Long minutes, WebDriver driver, CountDownLatch latch) {

			String ofMinutes = String.valueOf(minutes);

			int delay = 1000;
		    int period = 60000; //for every minute, display one minute less
		    Timer timer = new Timer();

			timer.scheduleAtFixedRate(new TimerTask() {

				Integer interval = Integer.parseInt(ofMinutes);

		        public void run() {

		        	if(interval != 0) {

		        		Platform.runLater(new Runnable() {
		                    @Override
		                    public void run() {
		                        try{

		                        	ta.appendText(System.lineSeparator()
		                        			+ "Checking back in " + interval-- + " minute(s)...");

		                        }finally{
		                            latch.countDown();

		                        }
		                    }
		                });

		        	} else {

		        		timer.cancel();

		        	}

		        }

		    }, delay, period);

		}


	//helpful prompt to end-user via Console: displaying how many seconds until next PTC link click attempt
	public void clickingIn(Long seconds, CountDownLatch latch) {

				//nl : formatting the console area
				ta.appendText(System.lineSeparator());

				String ofSeconds = String.valueOf(seconds);

				int delay = 1000;
			    int period = 1000; //for every second, display one second less
			    Timer timer = new Timer();

				timer.scheduleAtFixedRate(new TimerTask() {

					Integer interval = Integer.parseInt(ofSeconds);

			        public void run() {

			        	if(interval != 0) {

			        		Platform.runLater(new Runnable() {
			                    @Override
			                    public void run() {
			                        try{

			                        	ta.appendText(System.lineSeparator() + "Clicking next link in " +
			                        	interval-- + " second(s)...");

			                        }finally{
			                            latch.countDown();
			                        }
			                    }
			                });

			        	} else {

			        		timer.cancel();

			        	}

			        }

			    }, delay, period);

		}


	//helpful window for end-user to enter CAPTCHA ... THEN: auto-launch $Terminal for Power Users
	private void captchaPrompt(WebDriver driver) {

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

	    dialogStage.setTitle("Captcha Prompt");
	    dialogStage.initModality(Modality.WINDOW_MODAL);

	    Label lab_alert = new Label("Manually enter captcha, then press ENTER to begin.");

	    grd_pan.add(lab_alert, 0, 1);

	    TextField tf = new TextField();
	    tf.autosize();


	    grd_pan.setOnKeyPressed(new EventHandler<KeyEvent>() {
	        @Override
	        public void handle(KeyEvent event) {

	            if (event.getCode().equals(KeyCode.ENTER)) {

	            	String kL = tf.getText();
						String uL = tf.getText(kL.length()-6, kL.length());

						//fill captcha into site form after manual entry via user console
						driver.findElement(By.xpath("html/body/table/tbody/tr[3]/td[2]/table/tbody/tr[2]/td/form/table/tbody/tr[3]/td[2]/div[2]/input[1]")).clear();
						driver.findElement(By.xpath("html/body/table/tbody/tr[3]/td[2]/table/tbody/tr[2]/td/form/table/tbody/tr[3]/td[2]/div[2]/input[1]")).sendKeys(uL);

					    //auto-click login button on site
						driver.findElement(By.xpath("html/body/table/tbody/tr[3]/td[2]/table/tbody/tr[2]/td/form/table/tbody/tr[5]/td[2]/input")).click();

						//helpful end-user message
						ta.appendText(System.lineSeparator() + "select captcha drop-down in firefox web-browser (Hurry!)");

						//nl : formatting the console TextArea
						//ta.appendText(System.lineSeparator() + "");

					//close captchaPrompt() on ENTER && display terminalStage() by default
	            	dialogStage.hide();
	            	terminalBash(driver);

	            } else {

	            	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	            	//need to handle this error if something else [can enter different keys here]
	            	dialogStage.hide();

	            	}

	        }

	    });

	    //grd_pan.add(btn_ok, 0, 2);
	    grd_pan.add(tf, 0, 2);

	    dialogStage.show();

	}


	//$Terminal for Power Users
	protected void terminalBash(WebDriver driver) {

		//build the $Terminal window
		TextArea terminalTextArea = new TextArea();
		terminalTextArea.autosize();
		terminalTextArea.setWrapText(true);

	    Scene scene = new Scene(terminalTextArea, 550, 300);
	    scene.getStylesheets().clear();
	    scene.getStylesheets().add("gui/terminal.css");

	    Stage terminalStage = new Stage();
		terminalStage.setTitle("@PTCbot:~$ TERMINAL");
	    terminalStage.initModality(Modality.WINDOW_MODAL);
	    terminalStage.setScene(scene);
	    terminalStage.getIcons().add(new Image("res/bot32.png"));
	    terminalStage.setMinWidth(550);
	    terminalStage.setMaxWidth(550);
	    terminalStage.setMinHeight(300);
	    terminalStage.setMaxHeight(300);


	    //display "pdb help" text : easy access for first-time users
	    terminalTextArea.appendText("pdb help");

	    //process $Terminal commands...
	    terminalTextArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
	        @Override
	        public void handle(KeyEvent event) {

	        	String help = "pdb help"; //redirects end-user to youtube video for start time of $ Terminal specific tutorial
	        	String quit = "pdb quit-terminal"; //closes $ Terminal window programmatically (end-user can still close the window manually)
	        	String refresh = "pdb refresh"; //refresh the active Selenium WebDriver web page
	        	String shutdown = "pdb shutdown"; //quits program entirely- closing the firefox browser, Console, and $Terminal session
	        	String auth = "pdb auth"; //displays author's github web page

        		if (event.getCode().equals(KeyCode.ENTER) && terminalTextArea.getText().equals(help)) {

        			String url = "https://www.youtube.com/watch?v=kxnFbE5pTPc"; //SEND TO YOUTUBE TUTORIAL LINK
        	    	try {
						java.awt.Desktop.getDesktop().browse(new URI(url));
					} catch (IOException | URISyntaxException e) {
						e.printStackTrace();
					}

    	    		event.consume();
					terminalTextArea.clear();


    			} else {

    				if(event.getCode().equals(KeyCode.ENTER) && terminalTextArea.getText().equals(quit)) {

    					event.consume();
    					terminalTextArea.clear();
    					terminalStage.close();

    				} else {

    					if(event.getCode().equals(KeyCode.ENTER) && terminalTextArea.getText().equals(refresh)) {

    						driver.navigate().refresh();
        					event.consume();
        					terminalTextArea.clear();


    					} else {

    						if(event.getCode().equals(KeyCode.ENTER) && terminalTextArea.getText().equals(shutdown)) {

    							event.consume();
								driver.quit();
								terminalStage.close();
    	    					Platform.exit();

    						} else {

    							if(event.getCode().equals(KeyCode.ENTER) && terminalTextArea.getText().equals(auth)) {

    								String url = "http://countdooku.github.io/"; //MY PROJECTS
    			        	    	try {
    									java.awt.Desktop.getDesktop().browse(new URI(url));
    								} catch (IOException | URISyntaxException e) {
    									e.printStackTrace();
    								}

    								event.consume();
        	    					terminalTextArea.clear();

    							}}}}}}});

	    terminalStage.show();

	}

}