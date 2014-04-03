package com.example.alztracker;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.os.Build;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.app.AlertDialog.Builder;
import java.lang.Math;
import java.util.ArrayList;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

@TargetApi(Build.VERSION_CODES.HONEYCOMB) @SuppressLint("ValidFragment") public class MainActivity extends ActionBarActivity {
	final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        
        LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        		LocationListener mlocListener = new MyLocationListener();


        		mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
    
    
    public class MyLocationListener implements LocationListener

    {

    @Override

    public void onLocationChanged(Location loc)

    {

    double lat1deg = loc.getLatitude();
    double long1deg = loc.getLongitude();
    
    double R = 6371.0;
    double lat2deg = 41.637530;
    double long2deg = -70.949057;
    
    double dLat = Math.toRadians(lat2deg-lat1deg);
    double dLong = Math.toRadians(long2deg-long1deg);
    double lat1 = Math.toRadians(lat1deg);
    double lat2 = Math.toRadians(lat2deg);
    double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.sin(dLong/2) * Math.sin(dLong/2) * Math.cos(lat1) * Math.cos(lat2); 
    double  c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    double d = R * c;
    d = (double) Math.round(d * 100) / 100;

    String Text = "Home: (" + (double)Math.round(lat2deg*100)/100 + "," + (double)Math.round(long2deg*100)/100+ ")\n Current location: (" + loc.getLatitude() + "," + loc.getLongitude() + ")\nDistance = " + d + " km";
    final String output = String.valueOf(lat1deg) +"," + String.valueOf(long1deg);
    if (d > 10){
    new AlertDialog.Builder(context).setTitle("Current Location")
    .setMessage(Text)
    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) { 
        	RetreiveFeedTask r = new RetreiveFeedTask();
      	  	
      	  	
        	r.getData(output);
            r.execute();
        }
     })
    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) { 
            // do nothing
        }
     })
    .show();}
    
    
    
    

    //Toast.makeText( getApplicationContext(), Text, Toast.LENGTH_SHORT).show();

    }


    @Override

    public void onProviderDisabled(String provider)
    {
    Toast.makeText( getApplicationContext(),"Gps Disabled", Toast.LENGTH_SHORT ).show();

    }


    @Override

    public void onProviderEnabled(String provider)

    {

    Toast.makeText( getApplicationContext(),"Gps Enabled", Toast.LENGTH_SHORT).show();

    }


    @Override

    public void onStatusChanged(String provider, int status, Bundle extras)

    {


    }

    }/* End of Class MyLocationListener */
    
    
   
    

}

class RetreiveFeedTask extends AsyncTask<String, Void, Void> {

    private Exception exception;
    private String data;
    
    public void getData(String a){
  	  data = a;
  	  
    }

    protected Void doInBackground(String... arg0) {
  	  ArrayList<String> stringData = new ArrayList<String>();
        DefaultHttpClient httpClient = new DefaultHttpClient();
        ResponseHandler <String> resonseHandler = new BasicResponseHandler();
        HttpPost postMethod = new HttpPost("http://shekarsapps.appspot.com/post");

        try{
      	  
        JSONObject json = new JSONObject();
        json.put("C",data);
             
        postMethod.setEntity(new ByteArrayEntity(json.toString().getBytes("UTF8")));
        String response = httpClient.execute(postMethod,resonseHandler);
        
        
        Log.e("response :", response);
        }
        catch(Exception e){
      	  System.out.println("EXCEPTION");
      	  System.out.println(e);
        }
		return null;
        }
    
    
    protected void onPostExecute() {
        // TODO: check this.exception 
        // TODO: do something with the feed
    		}



	
    }
