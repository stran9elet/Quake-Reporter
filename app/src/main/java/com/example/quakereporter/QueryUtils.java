package com.example.quakereporter;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.quakereporter.MainActivity.LOG_TAG;

public final class QueryUtils {

    private QueryUtils() {
    }

    public static String fetchQuakeData(URL url){
        String jsonObject = "";
        if (url == null){
            return "";
        }
        try {
            jsonObject = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }



    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */



    /**
     * Return a list of {@link QuakeDetails} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<QuakeDetails> extractEarthquakes(String jsonResponse) {

        int magColorResourceId;

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<QuakeDetails> earthquakes = new ArrayList<>();


        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray jsonArray = jsonObject.optJSONArray("features");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject feature = jsonArray.getJSONObject(i);
                JSONObject properties = feature.getJSONObject("properties");

                Double mag = properties.getDouble("mag");
                String place = properties.getString("place");
                long time = properties.getLong("time");
                String url = properties.getString("url");

                int mag1 = (int) Math.floor(mag);

                switch (mag1) {
                    case 0:
                        magColorResourceId = R.color.mag1;
                        break;
                    case 1:
                        magColorResourceId = R.color.mag2;
                        break;
                    case 2:
                        magColorResourceId = R.color.mag3;
                        break;
                    case 3:
                        magColorResourceId = R.color.mag4;
                        break;
                    case 4:
                        magColorResourceId = R.color.mag5;
                        break;
                    case 5:
                        magColorResourceId = R.color.mag6;
                        break;
                    case 6:
                        magColorResourceId = R.color.mag7;
                        break;
                    case 7:
                        magColorResourceId = R.color.mag8;
                        break;
                    case 8:
                        magColorResourceId = R.color.mag9;
                        break;
                    case 9:
                        magColorResourceId = R.color.mag10;
                        break;
                    default:
                        magColorResourceId = R.color.mag10;
                }

                Date date = new Date(time);

                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
                String dateToDisplay = dateFormat.format(date);

                SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
                String timeToDisplay = timeFormat.format(date);

                String dateAndTime = dateToDisplay + "\nat " + timeToDisplay;

                DecimalFormat dFormat = new DecimalFormat("0.0");
                String magg = dFormat.format(mag);

                String timee = String.valueOf(time);


                if (place.contains("of")) {
                    String[] placeSplit = place.split("of ");
                    String place_1 = placeSplit[0] + "of";
                    String place_2 = placeSplit[1];
                    earthquakes.add(new QuakeDetails(magg, place_1, place_2, dateAndTime, magColorResourceId, url));
                } else {
                    String place_1 = "Near the ";
                    String place_2 = place;
                    earthquakes.add(new QuakeDetails(magg, place_1, place_2, dateAndTime, magColorResourceId, url));
                }

            }

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

    public static URL createURL(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG,"Error while forming url: ",e);
        }
        return url;
    }

    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder output = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null){
                output.append(line);
                line = bufferedReader.readLine();
            }
            jsonResponse = output.toString();

        } catch (IOException e) {
            e.printStackTrace();

        }finally{
            if (urlConnection != null)
                urlConnection.disconnect();
            if (inputStream != null)
                inputStream.close();
        }
        return jsonResponse;
    }

}
