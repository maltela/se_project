package de.fh_muenster.xbankandroid;

import android.app.Application;
import de.fh_muenster.xbank.Customer;
import de.fh_muenster.xbank.XbankOnlineService;

/**
 * Created by FalkoHoefte on 29.03.15.
 */
public class XbankAndroidApplication extends Application{

    private Customer user;
    private XbankOnlineService xbankOnlineService;

    public XbankAndroidApplication() {
        this.xbankOnlineService = new XbankOnlineServiceImpl();
    }

    public Customer getUser() {
        return this.user;
    }

    public void setUser(Customer user) {
        this.user = user;
    }

    public XbankOnlineService getXbankOnlineService() {
        return this.xbankOnlineService;
    }
}







