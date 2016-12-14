package com.zesters.solarsmart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String mBit = "0";
    private Button mLedButton;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mLedButton = (Button) findViewById(R.id.led_button);
        mLedButton.setOnClickListener(this);

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
            Intent intent = new Intent(this, PreferenceActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getmBit() {
        return mBit;
    }

    public void setmBit(String mBit) {
        this.mBit = mBit;
    }

    @Override
    public void onClick(View v) {
        if (getmBit().equals("0")) {
            mLedButton.setBackground(getDrawable(R.drawable.led_on));
            setmBit("1");
        } else {
            mLedButton.setBackground(getDrawable(R.drawable.led_off));
            setmBit("0");
        }
        new SendData().execute();
    }

    public class SendData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Socket sock = null;
            PrintWriter out = null;
            BufferedReader indata = null;
            char[] input = new char[9];
            try {
                //create socket
                sock = new Socket(
                        prefs.getString(getString(R.string.ip_preference), ""),
                        Integer.parseInt(prefs.getString(getString(R.string.port_preference), "10001"))
                );
                //print something
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())));
                //send the bit
                out.print(getmBit());
                //clean
                out.flush();
                sock.setSoTimeout(100);
                //read response
                indata = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                indata.read(input, 0, 1);
                Log.i("MyActivity", "indata = " + input[0]);
                out.close();
                indata.close();
                sock.close();
                Snackbar.make(findViewById(android.R.id.content), getString(R.string.data_send_success), Snackbar.LENGTH_LONG).show();
            } catch (ConnectException ce) {
                Snackbar.make(findViewById(android.R.id.content), getString(R.string.data_send_error), Snackbar.LENGTH_LONG).show();
            } catch (Exception e) {
                Snackbar.make(findViewById(android.R.id.content), getString(R.string.data_send_error), Snackbar.LENGTH_LONG).show();
            }
            return null;
        }
    }
}
