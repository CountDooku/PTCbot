package gui.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Accounts {

    private final StringProperty Username;
    private final StringProperty Password;
    private final StringProperty Proxy;
    private final StringProperty UserAgent;

    //default constructor
    public Accounts() {
        this(null, null, null, null);
    }
    
    
    //constructor w/initial data, defined in MainApp.java's default constructor MainApp()
    public Accounts(String Username, String Password, String Proxy, String UserAgent) {
    	
    	this.Username = new SimpleStringProperty(Username);
        this.Password = new SimpleStringProperty(Password);
        this.Proxy = new SimpleStringProperty(Proxy);
        this.UserAgent = new SimpleStringProperty(UserAgent);
        
    }
    
    
    //get && set these var values
    public String getUsername() {
        return Username.get();
    }

    public void setUsername(String Username) {
        this.Username.set(Username);
    }

    public StringProperty UsernameProperty() {
        return Username;
    }

    public String getPassword() {
        return Password.get();
    }

    public void setPassword(String Password) {
        this.Password.set(Password);
    }

    public StringProperty PasswordProperty() {
        return Password;
    }
    
    public String getProxy() {
        return Proxy.get();
    }

    public void setProxy(String Proxy) {
        this.Proxy.set(Proxy);
    }

    public StringProperty ProxyProperty() {
        return Proxy;
    }

    public String getUserAgent() {
        return UserAgent.get();
    }

    public void setUserAgent(String UserAgent) {
        this.UserAgent.set(UserAgent);
    }

    public StringProperty UserAgentProperty() {
        return UserAgent;
    }

}