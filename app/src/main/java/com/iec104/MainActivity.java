package com.iec104;

import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.openmuc.j60870.ClientConnectionBuilder;
import org.openmuc.j60870.Connection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private ConnectionTask Task = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        println("Ave satan");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // check if already existing
                if (Task == null) {
                    Task = new ConnectionTask();
                    Task.execute();
                }

                Snackbar.make(view, "Connect", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void println(String msg) {
        TextView text = findViewById(R.id.debug);
        text.setText((msg + "\n"));
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ConnectionTask extends AsyncTask<Void, Void, String> {
        // Added AsyncTask to handle background activity connection
        // TODO need to get some data from the server and visualize it on the screen
        private Connection connection;
        private String resultAll;

        // This is run in a background thread
        @Override
        protected String doInBackground(Void... params) {

            InetAddress address;
            try {
                address = InetAddress.getByName("192.168.2.100");
            } catch (UnknownHostException e) {
                resultAll = "Unknown host: " + e.toString();
                return "";
            }

            ClientConnectionBuilder clientConnectionBuilder = new ClientConnectionBuilder(address)
                    .setMessageFragmentTimeout(5_000)
                    .setConnectionTimeout(20_000)
                    .setPort(2404);

            try {
                connection = clientConnectionBuilder.build();
            } catch (IOException e) {
                resultAll = "Unable to connect to remote host." + e.toString();
                return "";
            }
            resultAll = "Seems ok";
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            println(resultAll);
        }
    }
}