package gui.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

	/*
	 * helper class to wrap accountData to a list
	 *  
	 * saves accountData to XML
	 */
	@XmlRootElement(name = "root")
	public class AccountWrapper {

	    private List<Accounts> accountData;

	    @XmlElement(name = "root1")
	    public List<Accounts> getAccountData() {
	        return accountData;
	    }

	    public void setAccountData(List<Accounts> accountData) {
	        this.accountData = accountData;
	    }
	    
}