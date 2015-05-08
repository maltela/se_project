package de.fh_muenster.xbankandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import de.fh_muenster.xbank.Customer;
import de.fh_muenster.xbank.exceptions.InvalidLoginException;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Button OnClickListener setzen (Deklaration des Eventhandlers siehe unten)
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(eventHandler);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Wenn "Settings" gedrückt wurde, rufen wir die PrefsActivity auf
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, PrefsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    View.OnClickListener eventHandler = new View.OnClickListener() {
        public void onClick(View ausloeser) {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ausloeser.getContext());
            String username = prefs.getString("username", "");
            String password = prefs.getString("password", "");

            if(!"".equals(username) && !"".equals(password))
            {
                LoginTask loginTask = new LoginTask(ausloeser.getContext());
                //Proxy asynchron aufrufen
                loginTask.execute(username, password);
            }
            else
            {
                //Toast anzeigen
                CharSequence text = "Fehlende Logindaten bitte in den Einstellungen eintragen!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(ausloeser.getContext(), text, duration);
                toast.show();
            }
        }
    };

    private class LoginTask extends AsyncTask<String, Integer, Customer>
    {
        private Context context;

        //Dem Konstruktor der Klasse wird der aktuelle Kontext der Activity übergeben
        //damit auf die UI-Elemente zugegriffen werden kann und Intents gestartet werden können, usw.
        public LoginTask(Context context)
        {
            this.context = context;
        }

        @Override
        protected Customer doInBackground(String... params){
            if(params.length != 2)
                return null;
            String username = params[0];
            String password = params[1];
            XbankAndroidApplication myApp = (XbankAndroidApplication) getApplication();
            try {
                Customer myCustomer = myApp.getXbankOnlineService().login(username, password);
                return myCustomer;
            } catch (InvalidLoginException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onProgessUpdate(Integer... values)
        {
            //wird in diesem Beispiel nicht verwendet
        }

        protected void onPostExecute(Customer result)
        {
            if(result != null)
            {
                //erfolgreich eingeloggt
                XbankAndroidApplication myApp = (XbankAndroidApplication) getApplication();
                myApp.setUser(result);

                //Toast anzeigen
                CharSequence text = "Login erfolgreich! Angemeldeter Benutzername: " + result.getUserName();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                //Nächste Activity anzeigen
                Intent i = new Intent(context, BankingActivity.class);
                startActivity(i);
            }
            else
            {
                //Toast anzeigen
                CharSequence text = "Login fehlgeschlagen!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    }
}
