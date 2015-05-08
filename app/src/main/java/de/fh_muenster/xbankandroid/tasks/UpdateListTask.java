package de.fh_muenster.xbankandroid.tasks;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import java.util.List;

import de.fh_muenster.xbank.Account;
import de.fh_muenster.xbank.exceptions.NoSessionException;
import de.fh_muenster.xbankandroid.XbankAndroidApplication;

/**
 * Updating the List of Accounts as AsyncTask
 */
public class UpdateListTask extends AsyncTask<Void, Void, List<Account>> {
    private List<Account> list;
    private ArrayAdapter<Account> adapter;
    private XbankAndroidApplication myApp;

    public UpdateListTask(List<Account> list, ArrayAdapter<Account> adapter,  XbankAndroidApplication myApp) {
        this.list = list;
        this.adapter = adapter;
        this.myApp = myApp;
    }

    @Override
    protected List<Account> doInBackground(Void... params){
        try {
            List<Account> myAccounts = myApp.getXbankOnlineService().getMyAccounts();
            return myAccounts;
        } catch (NoSessionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(final List<Account> newList) {
        if(newList != null){
            this.list.clear();
            this.list.addAll(newList);
            adapter.notifyDataSetChanged();
        }
    }
}
