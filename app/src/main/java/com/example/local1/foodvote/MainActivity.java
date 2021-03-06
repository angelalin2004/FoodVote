package com.example.local1.foodvote;

import android.os.Bundle;
import android.os.Looper;
import android.os.Handler;

import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.content.Context;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.ConnectionResult;
import android.location.Location;
import com.google.android.gms.common.GooglePlayServicesUtil;

import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;



import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.content.Intent;
import android.widget.EditText;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import android.util.Log;


public class MainActivity extends AppCompatActivity implements
        ConnectionCallbacks, OnConnectionFailedListener{
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";



    /*
     * Update OAuth credentials below from the Yelp Developers API site:
     * http://www.yelp.com/developers/getting_started/api_access
     */
    private static final String CONSUMER_KEY = "FQFe1MpY3PGvxKy-Aq702g";
    private static final String CONSUMER_SECRET = "u_ifYEaonk6W5sf24SCXiGPKx6I";
    private static final String TOKEN = "6YSX1I448VpE2WQ1rrQv0NJRNJ9E9rOX";
    private static final String TOKEN_SECRET = "_iN4GhZsdgojn1WYZTHOi-q2jzM";

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    String mLatitudeText;
    String mLongitudeText;

    YelpAPI yAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        /*Create a YelpAPI Instance*/
        //YelpAPI yAPI = new YelpAPI(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);

        // First we need to check availability of play services
        if (checkPlayServices()) {

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            // Building the GoogleApi client
            //buildGoogleApiClient();
        }
        /*mGoogleApiClient.connect();
        //getLocation();


        while(!mGoogleApiClient.isConnected()){
            if(!mGoogleApiClient.isConnecting()){
                Log.println(Log.ERROR, "MAIN:", "Not Connecting");
            }
        }*/



        //Log.println(Log.ERROR, "MAIN:", "mLatitudeText = " + mLatitudeText);
        //Log.println(Log.ERROR, "MAIN:", "mLongitudeText = " + mLongitudeText);
        //String Location = mLatitudeText + ", " + mLongitudeText;
        /*Call Helper method to get results of API Call in JSON Format*/
        //String JSONOutput = setUpAPIRet(yAPI, Location);

        /*Display the Relevent Data from the YELP Api Call*/
        //ParseAndDisplayRestaurantOutput(JSONOutput);


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.println(Log.ERROR, "MAIN:", "In OnStart");
        yAPI = new YelpAPI(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);



        //LocationConnector LC = new LocationConnector(this);
        String location = "";

        mGoogleApiClient.connect();
        /*try {

            Thread mainT = Thread.currentThread();
            location = LC.execute(mainT).get();
            Thread.sleep(10000);
        }
        catch(Exception e){
            System.out.print("Hi");
        }*/

        //getLocation();

        //int time = 0;

        //String YelpJSON = setUpAPIRet(yAPI, location);


    }

    /*public void getLocation(){
        Log.println(Log.ERROR, "MAIN:", "In getLocation");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        Log.println(Log.ERROR, "MAIN:", "LastLocation: " + mLastLocation);
        if (mLastLocation != null) {
            mLatitudeText = String.valueOf(mLastLocation.getLatitude());
            mLongitudeText = String.valueOf(mLastLocation.getLongitude());
        }
    }*/

    //@Override
    public void onConnected(Bundle bundle){
        Log.println(Log.ERROR, "MAIN:", "In onConnected");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mLastLocation != null) {
            mLatitudeText = String.valueOf(mLastLocation.getLatitude());
            mLongitudeText = String.valueOf(mLastLocation.getLongitude());
            String location = mLatitudeText + ", " + mLongitudeText;
            String YelpJSON = setUpAPIRet(yAPI, location);
            ParseAndDisplayRestaurantOutput(YelpJSON);
        }
    }


    @Override
    public void onConnectionSuspended(int x){
        Log.println(Log.ERROR, "MAIN:", "Connection Suspended");
        Context context = getApplicationContext();
        CharSequence text = "Connection Lost";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult c){
        Log.println(Log.ERROR, "MAIN:", "Connection Failed");
        Context context = getApplicationContext();
        CharSequence text = "Connection Failed";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /*Method gets the API call in JSON format*/
    public String setUpAPIRet(YelpAPI Y, String Location){
        /*To Return*/
        String Ret = "";

        try {
            /*Get the return from the API call*/
            Ret = Y.execute(Location).get();
        }
        catch(Exception e){
            System.out.print("Hi");
        }

        Log.println(Log.ERROR, "MAIN:", "result = " + Ret);
        return Ret;
    }

    public void ParseAndDisplayRestaurantOutput(String YelpJSON){
        JSONParser parser = new JSONParser();
        JSONObject response = null;
        try {
            response = (JSONObject) parser.parse(YelpJSON);
        } catch (ParseException pe) {
            System.out.println("Error: could not parse JSON response:");
            System.out.println(YelpJSON);
            System.exit(1);
        }

        /*Each buisness is now represented as an array entry*/
        JSONArray businesses = (JSONArray) response.get("businesses");

        /*FOR NOW MANUALLY PICK THE 1st 5 RESTUARANTS*/
        JSONObject Buisness1 = (JSONObject) businesses.get(0);
        JSONObject Buisness2 = (JSONObject) businesses.get(1);
        JSONObject Buisness3 = (JSONObject) businesses.get(2);
        JSONObject Buisness4 = (JSONObject) businesses.get(3);
        JSONObject Buisness5 = (JSONObject) businesses.get(4);

        /*GET THE NAME OF EACH RESTUARANT*/
        String R1 = Buisness1.get("name").toString();
        String R2 = Buisness2.get("name").toString();
        String R3 = Buisness3.get("name").toString();
        String R4 = Buisness4.get("name").toString();
        String R5 = Buisness5.get("name").toString();

        /*GET EACH TEXT VIEW BOX*/
        Button B1 = (Button)findViewById(R.id.Button1);
        Button B2 = (Button)findViewById(R.id.Button2);
        Button B3 = (Button)findViewById(R.id.Button3);
        Button B4 = (Button)findViewById(R.id.Button4);
        Button B5 = (Button)findViewById(R.id.Button5);

        /*SET EACH EXT VIEW BOX TO THE RESTUARANT NAME*/
        B1.setText(R1);
        B2.setText(R2);
        B3.setText(R3);
        B4.setText(R4);
        B5.setText(R5);
    }

    /*protected synchronized void buildGoogleApiClient() {
        Log.println(Log.ERROR, "MAIN:", "In buildGoogleApiClient");
        //Context context = getApplicationContext();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }*/

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

    public void RestaurantClicked(View v){
        Context context = getApplicationContext();
        CharSequence text = "Restaurant Clicked";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        1000).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    /** Called when the user clicks the Send button */
    /*public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.location_message);
        String message = editText.getText().toString();

        try {
            URL url = new URL("https://api.yelp.com/v2/search" + "?limit=1" + "&location=" + "San Diego");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                Log.i("sendMessage",stringBuilder.toString());
                intent.putExtra(EXTRA_MESSAGE, stringBuilder.toString());
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("sendMessage", e.getMessage(), e);
        }

        Log.i("sendMessage",message);
        startActivity(intent);
    }*/

}

