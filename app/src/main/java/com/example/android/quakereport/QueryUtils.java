
package com.example.android.quakereport;
/**
 * Created by hp on 20/12/2016.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;


import android.text.TextUtils;
import android.util.Log;

import com.example.android.quakereport.Earthquake;

import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;

import static com.example.android.quakereport.EarthquakeActivity.LOG_TAG;
import static com.example.android.quakereport.R.id.magnitude;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {



    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the USGS dataset and return a list of {@link Earthquake} objects.
     */
    public static ArrayList<Earthquake> fetchEarthquakeData(String requestUrl) {




        //Create url object
        URL url = createUrl(requestUrl);
        String jsonResponse="";

        try {
            // Perform HTTP request to the URL and receive a JSON response back
             jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making Http request", e);

        }
        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        ArrayList<Earthquake> earthquakes;
        earthquakes= extractFeatureFromJson(jsonResponse);
        // Return the list of {@link Earthquake}s
        return earthquakes;
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing the given JSON response.
     */


    private static ArrayList<Earthquake> extractFeatureFromJson(String earthquakeJSON)
    {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Earthquake>earthquakes=new ArrayList<Earthquake>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.
            JSONObject baseJsonResponse=new JSONObject(earthquakeJSON);
            JSONArray earthquakeArray=baseJsonResponse.optJSONArray("features");
            for(int i=0;i<earthquakeArray.length();i++)
            {
                JSONObject currentearthquake=earthquakeArray.optJSONObject(i);
                JSONObject properties=currentearthquake.getJSONObject("properties");

                double magnitude=(properties.getDouble("mag"));
                String place=properties.getString("place");
                Long time= (properties.getLong("time"));
                String url=properties.getString(("url"));

                earthquakes.add(new Earthquake(magnitude,place,time,url));



            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

    /**
     * Returns new URL object from the given string URL.
     */

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;

        }
        return url;
    }
    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */

    private static String makeHttpRequest(URL url)throws IOException
    {
        String jsonResponse="";
        if(url==null)
            return jsonResponse;
        HttpURLConnection urlConnection=null;
        InputStream inputStream=null;
        try{


            urlConnection=(HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response

            if(urlConnection.getResponseCode()==200)
            {
                inputStream=urlConnection.getInputStream();
                jsonResponse=readFromStream(inputStream);
            }
            else
            {
                Log.e(LOG_TAG,"Error Response code:"+urlConnection.getResponseCode());
            }

    }
        catch (IOException io)
        {
            Log.e(LOG_TAG,"Problem retrieving earthquake data" ,io);
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }

        }
        return jsonResponse;
        }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */

    private static String readFromStream(InputStream inputStream)throws IOException
    {
        StringBuilder output=new StringBuilder();
        if(inputStream!=null)
        {
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader=new BufferedReader(inputStreamReader);
            String line=reader.readLine();
            while(line!=null)
            {
                output.append(line);
                line=reader.readLine();
            }
        }
        return output.toString();
    }



}
