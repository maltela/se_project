package de.fh_muenster.xbankandroid.tasks;

import android.os.AsyncTask;
import java.math.BigDecimal;
import de.fh_muenster.xbank.exceptions.NoSessionException;
import de.fh_muenster.xbankandroid.XbankAndroidApplication;

/**
 * Transferring money as AsyncTask
 */
public class TransferTask extends AsyncTask<Integer, Void, BigDecimal> {

    private XbankAndroidApplication myApp;

    public TransferTask(XbankAndroidApplication myApp) {
        this.myApp = myApp;
    }

    @Override
    protected BigDecimal doInBackground(Integer... params) {
        if(params.length == 3) {
            try {
                //die Reihenfolge der Parameter haben wir selbst beim Aufruf festgelegt. Besser wäre hier ein Transferobjekt!
                int fromAccount = params[0];
                int toAccount = params[1];
                int balance = params[2];
                BigDecimal newFromAccountBalance = myApp.getXbankOnlineService().transfer(fromAccount, toAccount, new BigDecimal(balance));
                return newFromAccountBalance;
            }
            catch (NoSessionException e) {
                e.printStackTrace();
            }
        }
        //null zurückgeben, wenn der Wert nicht ausgelesen werden konnte
        return null;
    }
}
