package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.android.quakereport.R.id.date;
import static com.example.android.quakereport.R.id.list;
import static com.example.android.quakereport.R.id.magnitude;

/**
 * Created by hp on 19/12/2016.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {
    //Alternative  for string split
    //private static final String LOCATION_SEPARATOR = " of ";

    public EarthquakeAdapter(Context context, List<Earthquake>earthquakes)
    {
        super(context,0,earthquakes);
    }



    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView=convertView;
        if(listItemView==null)
        {
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item,parent,false);
        }
        Earthquake currentEarthquake=getItem(position);
        TextView magnitudeView=(TextView) listItemView.findViewById(magnitude);
        String formattedMagnitude=formatMagnitude(currentEarthquake.getmMagnitude());
        magnitudeView.setText(formattedMagnitude);
        TextView primaryLocationView=(TextView) listItemView.findViewById(R.id.primary_location);
        TextView locationOffsetView=(TextView) listItemView.findViewById(R.id.location_offset);
        String original=currentEarthquake.getmLocation();
        String locationOffset;
        String primaryLocation;
        //Alternative
        /* if (originalLocation.contains(LOCATION_SEPARATOR)) {
    String[] parts = originalLocation.split(LOCATION_SEPARATOR);
    locationOffset = parts[0] + LOCATION_SEPARATOR;
    primaryLocation = parts[1];
 } else {
    locationOffset = getContext().getString(R.string.near_the);
    primaryLocation = originalLocation;
 }*/
        if(original.contains("km"))
        {
            String[] parts=original.split("(?<=f)");
             locationOffset=parts[0];
             primaryLocation=parts[1];

        }
        else{
            locationOffset="Near the";
            primaryLocation=original;
        }

        primaryLocationView.setText(primaryLocation);
        locationOffsetView.setText(locationOffset);





        // Create a new Date object from the time in milliseconds of the earthquake
        Date dateObject = new Date(currentEarthquake.getTimeInMilliseconds());

        // Find the TextView with view ID date
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        // Format the date string (i.e. "Mar 3, 1984")
        String formattedDate = formatDate(dateObject);
        // Display the date of the current earthquake in that TextView
        dateView.setText(formattedDate);

        // Find the TextView with view ID time
        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        // Format the time string (i.e. "4:30PM")
        String formattedTime = formatTime(dateObject);
        // Display the time of the current earthquake in that TextView
        timeView.setText(formattedTime);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.

        GradientDrawable magnitudeCircle=(GradientDrawable) magnitudeView.getBackground();
        int magnitudeColor=getMagnitudeColor(currentEarthquake.getmMagnitude());
        magnitudeCircle.setColor(magnitudeColor);


        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
    /**
     * Return the formatted magnitude string showing 1 decimal place (i.e. "3.2")
     * from a decimal magnitude value.
     */
    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }
    private int getMagnitudeColor(double magnitude)
    {int magnitudeColorResourceId;
    int magnitudeFloor = (int) Math.floor(magnitude);
    switch (magnitudeFloor) {
        case 0:
        case 1:
            magnitudeColorResourceId = R.color.magnitude1;
            break;
        case 2:
            magnitudeColorResourceId = R.color.magnitude2;
            break;
        case 3:
            magnitudeColorResourceId = R.color.magnitude3;
            break;
        case 4:
            magnitudeColorResourceId = R.color.magnitude4;
            break;
        case 5:
            magnitudeColorResourceId = R.color.magnitude5;
            break;
        case 6:
            magnitudeColorResourceId = R.color.magnitude6;
            break;
        case 7:
            magnitudeColorResourceId = R.color.magnitude7;
            break;
        case 8:
            magnitudeColorResourceId = R.color.magnitude8;
            break;
        case 9:
            magnitudeColorResourceId = R.color.magnitude9;
            break;
        default:
            magnitudeColorResourceId = R.color.magnitude10plus;
            break;
    }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }





}
