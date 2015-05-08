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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.fh_muenster.xbank.Account;
import de.fh_muenster.xbank.Customer;
import de.fh_muenster.xbank.exceptions.InvalidLoginException;
import de.fh_muenster.xbank.exceptions.NoSessionException;
import de.fh_muenster.xbankandroid.tasks.LogoutTask;
import de.fh_muenster.xbankandroid.tasks.TransferTask;
import de.fh_muenster.xbankandroid.tasks.UpdateListTask;


public class BankingActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banking);

        //Button OnClickListener setzen (Deklaration des Eventhandlers siehe unten)
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(eventHandler);

        //Verschiedene Serveraufrufe durchführen und Elemente der Oberfläche ändern
        BankingTask bankingTask = new BankingTask(this);
        //Proxy asynchron aufrufen
        bankingTask.execute();

        //Toast anzeigen
        CharSequence text = "Testlogik: Klick auf ein Konto in der Liste fügt dem nachfolgenden Konto weitere 500 Guthaben hinzu.";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_banking, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    View.OnClickListener eventHandler = new View.OnClickListener() {
        public void onClick(View ausloeser) {
            //Logout asynchron ausfuehren:
            LogoutTask logoutTask = new LogoutTask(ausloeser.getContext(),(XbankAndroidApplication) getApplication());
            logoutTask.execute();
        }
    };


    private class BankingTask extends AsyncTask<Void, Void, List<Account>>
    {
        private Context context;

        //Dem Konstruktor der Klasse wird der aktuelle Kontext der Activity übergeben
        //damit auf die UI-Elemente zugegriffen werden kann und Intents gestartet werden können, usw.
        public BankingTask(Context context)
        {
            this.context = context;
        }

        @Override
        protected List<Account> doInBackground(Void... params){
            XbankAndroidApplication myApp = (XbankAndroidApplication) getApplication();
            try {
                List<Account> myAccounts = myApp.getXbankOnlineService().getMyAccounts();
                return myAccounts;
            } catch (NoSessionException e) {
                e.printStackTrace();
            }
            return null;
        }

        //Vorsicht bei onPostExecute, onProgressUpdate und onPreExecute!
        //Diese drei Methoden werden im UI-Thread ausgeführt, lediglich doInBackground ist wirklich "asynchron".
        protected void onPostExecute(final List<Account> myAccounts)
        {
            if(myAccounts != null)
            {
                final XbankAndroidApplication myApp = (XbankAndroidApplication) getApplication();

                //UserName-TextView holen und Usernamen setzen
                //Es muss kein Aufruf zum Server erfolgen, da das Customer-Objekt bereits beim Login geladen wurde.
                TextView userNametextView = (TextView) findViewById(R.id.userNameTextView);
                userNametextView.setText(myApp.getUser().getUserName());

                //AccountCount-TextView holen und Anzahl der Konten als Text setzen
                Integer count = myAccounts.size();
                TextView accountCountTextView = (TextView) findViewById(R.id.accountCountTextView);
                accountCountTextView.setText(count.toString());

                //Liste holen und Adapter sowie OnClickListener anhängen
                final ListView listView = (ListView) findViewById(R.id.listView);
                final ArrayAdapter<Account> adapter;
                try {
                    //Aufruf zum "Server" (getMyAccounts) im dritten Parameter!
                    adapter = new ArrayAdapter<Account>(context, android.R.layout.simple_list_item_1, myAccounts);
                    listView.setAdapter(adapter);

                    //OnItemClickListener zu der Liste hinzufügen. Erst jetzt ist der ArrayAdapter bekannt, der für den TransferTask erforderlich ist.
                    //Die Referenz auf den Adapter könnte auch über andere Wege abgespeichert werden, z.B. über eine Klassenvariable etc
                    //--> damit könnte der nachfolgende OnItemClickListener ausgelagert werden.
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {

                            XbankAndroidApplication myApp = (XbankAndroidApplication) getApplication();

                            //Das Geld wird hier testweise dem nächsten Account in der Liste hinzugefügt.
                            Account fromAccount = myAccounts.get(position);
                            Account toAccount = myAccounts.get((position + 1) % myAccounts.size());

                            //TransferTask ausfuehren. Dies startet einen neuen Asynchronen Task.
                            TransferTask transferTask = new TransferTask(myApp);
                            transferTask.execute(fromAccount.getId(), toAccount.getId(), 500);

                            //Liste aktualisieren, dafuer Kontostaende neu abfragen.
                            UpdateListTask updateListTask= new UpdateListTask(myAccounts, adapter, myApp);
                            updateListTask.execute();

                            //Hier koennte alternativ eine weitere Activity gestartet werden, die eine "echte" Ueberweisung durchfuehrt.
                            //Intent i = new Intent(view.getContext(), xxx.class);
                            //startActivity(i);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            else
            {
                //Toast anzeigen
                CharSequence text = "Auslesen der Konten fehlgeschlagen!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    }
}
