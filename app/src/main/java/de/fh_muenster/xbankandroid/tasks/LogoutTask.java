package de.fh_muenster.xbankandroid.tasks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import de.fh_muenster.xbank.exceptions.NoSessionException;
import de.fh_muenster.xbankandroid.MainActivity;
import de.fh_muenster.xbankandroid.XbankAndroidApplication;

/**
 * Logout as AsyncTask
 */
public class LogoutTask extends AsyncTask<Void, Integer, Boolean>  {

    private Context context;
    private XbankAndroidApplication myApp;

    public LogoutTask(Context context, XbankAndroidApplication myApp) {
        this.context = context;
        this.myApp = myApp;
    }

    @Override
    protected Boolean doInBackground(Void... params){
        try {
            myApp.getXbankOnlineService().logout();
            return true;
        } catch (NoSessionException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected void onPostExecute(Boolean result) {
        if(result)
        {
            //erfolgreich ausgeloggt
            myApp.setUser(null);

            //Toast anzeigen
            CharSequence text = "Logout erfolgreich!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            //Nächste Activity anzeigen
            Intent i = new Intent(context, MainActivity.class);
            context.startActivity(i);
        }
        else
        {
            //Toast anzeigen
            CharSequence text = "Logout fehlgeschlagen!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            //Nächste Activity anzeigen
            Intent i = new Intent(context, MainActivity.class);
            context.startActivity(i);
        }
    }
}
