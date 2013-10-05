package edu.stu.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import edu.stu.db.CityAdapter;

public class Locate implements LocationListener {
    public static String address = null;
    private final Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public Locate(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(mContext.LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    /**
     * Stop using GPS listener Calling this function will stop using GPS in your
     * app
     * */
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(Locate.this);
        }
    }

    public String getPosition() {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        return latitude + "," + longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     * 
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog On pressing Settings button will
     * lauch Settings Options
     * */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Message
        alertDialog.setMessage("若不開啟 GPS 警方可能無法正確判斷您的位置");

        // On pressing Settings button
        alertDialog.setPositiveButton("開啟",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mContext.startActivity(intent);
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    public String getAddressByLocation(String location) {
        String returnAddress = "";
        try {
            if (location != null) {
                String locaitons[] = location.split(",");
                Geocoder gc = new Geocoder(mContext, Locale.TRADITIONAL_CHINESE); // 地區:台灣
                List<Address> lstAddress = gc.getFromLocation(
                        Double.parseDouble(locaitons[0]),
                        Double.parseDouble(locaitons[1]), 1);
                returnAddress = lstAddress.get(0).getAddressLine(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnAddress;
    }

    public String getCityName(double x, double y) {
        CityAdapter test = new CityAdapter(mContext);
        test.createDatabase();
        test.open();
        String cityName = test.getCityName(getCityId(x, y));
        test.close();
        return cityName;
    }

    public String getCityPhone(double x, double y) {
        CityAdapter test = new CityAdapter(mContext);
        test.createDatabase();
        test.open();
        String cityPhone = test.getCityPhoneNumber(getCityId(x, y));
        test.close();
        return cityPhone;
    }

    private int getCityId(double x, double y) {
        CityAdapter test = new CityAdapter(mContext);
        test.createDatabase();
        test.open();
        for (HashMap<String, Integer> map : getCitiesByPoint(x, y)) {
            for (int cityGroupIndex = 0; cityGroupIndex < map.get("city_size"); cityGroupIndex++) {
                Cursor cursor = test.getCityCoordinates(map.get("id"),
                        cityGroupIndex);

                int right_node = 0, left_node = 0;
                double lastY = 0.0, lastX = 0.0;
                while (cursor.moveToNext()) {
                    if (lastX == 0.0) {
                        lastX = cursor.getDouble(cursor
                                .getColumnIndex("longitude"));
                        lastY = cursor.getDouble(cursor
                                .getColumnIndex("latitude"));
                        continue;
                    }

                    double theX = cursor.getDouble(cursor
                            .getColumnIndex("longitude"));
                    double theY = cursor.getDouble(cursor
                            .getColumnIndex("latitude"));

                    if ((theY >= y && y >= lastY) || (lastY >= y && y >= theY)) {
                        if (x >= theX && x >= lastX) {
                            right_node++;
                        } else if (x <= theX && x <= lastX) {
                            left_node++;
                        } else {
                            double deltax, deltay, tempx;
                            deltax = theX - lastX;
                            deltay = theY - lastY;
                            tempx = (y - lastY) * deltax / deltay + lastX;
                            if (x >= tempx) {
                                right_node++;
                            } else {
                                left_node++;
                            }
                        }
                    }

                    lastX = theX;
                    lastY = theY;

                }

                if (left_node % 2 == 1 && right_node % 2 == 1) {
                    return map.get("id");
                }
                cursor.close();
            }

        }
        test.close();
        return -1;
    }

    private List<HashMap<String, Integer>> getCitiesByPoint(double x, double y) {
        CityAdapter test = new CityAdapter(mContext);
        test.createDatabase();
        test.open();
        Cursor cursor = test.getTestData();
        List<HashMap<String, Integer>> cityData = new ArrayList<HashMap<String, Integer>>();
        while (cursor.moveToNext()) {
            double maxX = cursor.getDouble(cursor.getColumnIndex("x_max"));
            double maxY = cursor.getDouble(cursor.getColumnIndex("y_max"));
            double minX = cursor.getDouble(cursor.getColumnIndex("x_min"));
            double minY = cursor.getDouble(cursor.getColumnIndex("y_min"));
            CityRect rect = new CityRect(maxX, maxY, minX, minY);
            if (rect.isPointInRect(x, y)) {
                HashMap<String, Integer> cityMap = new HashMap<String, Integer>();
                cityMap.put("id", cursor.getInt(cursor.getColumnIndex("id")));
                cityMap.put("city_size",
                        cursor.getInt(cursor.getColumnIndex("city_size")));
                cityData.add(cityMap);

            }
        }
        cursor.close();
        test.close();
        return cityData;
    }

    class CityRect {
        double maxX, maxY, minX, minY;

        CityRect(double maxX, double maxY, double minX, double minY) {
            this.maxX = maxX;
            this.maxY = maxY;
            this.minX = minX;
            this.minX = minX;
        }

        public double getMaxX() {
            return maxX;
        }

        public double getMaxY() {
            return maxY;
        }

        public double getMinX() {
            return minX;
        }

        public double getMinY() {
            return minY;
        }

        public boolean isPointInRect(double x, double y) {
            if (x <= this.maxX && y <= this.maxY && x >= this.minX
                    && y >= this.minY) {
                return true;
            }
            return false;
        }
    }

    public String getAddress() throws InterruptedException, ExecutionException {
        return new locationToAddressTask(this.getPosition()).execute().get();
    }

    class locationToAddressTask extends AsyncTask<Void, Void, String> {

        final String LOCATION;

        locationToAddressTask(String Location) {
            this.LOCATION = Location;
        }

        protected String doInBackground(Void... Void) {
            StringBuilder json = new StringBuilder();
            String address = "";
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(
                    "http://maps.googleapis.com/maps/api/geocode/json?latlng="
                            + LOCATION + "&sensor=false&language=zh-tw");
            HttpResponse response;
            try {
                response = client.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (!(statusCode == 200)) {
                    Log.e(getClass().getName(), "連線錯誤：" + statusCode);
                }

                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }
                JSONObject mainObject = new JSONObject(json.toString());
                JSONArray addressArray = mainObject.getJSONArray("results");
                address = addressArray.getJSONObject(0).getString(
                        "formatted_address");

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return address;
        }
    }

}
