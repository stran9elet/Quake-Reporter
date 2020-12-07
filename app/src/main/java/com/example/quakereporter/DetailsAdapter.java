package com.example.quakereporter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class DetailsAdapter extends ArrayAdapter<QuakeDetails> {

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView ==  null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        final QuakeDetails quakeDetails = getItem(position);

        TextView magnitudeTextView = (TextView) listItemView.findViewById(R.id.magnitude_text_view);
        magnitudeTextView.setText(quakeDetails.getMagnitude());

        TextView placeTextView = (TextView) listItemView.findViewById(R.id.place_text_view);
        placeTextView.setText(quakeDetails.getPlace_1());

        TextView placeTextView_2 = (TextView) listItemView.findViewById(R.id.place_text_view_2);
        placeTextView_2.setText(quakeDetails.getPlace_2());

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_text_view);
        dateTextView.setText(quakeDetails.getDate());

        GradientDrawable magCircle = (GradientDrawable) magnitudeTextView.getBackground();
        int magColor = ContextCompat.getColor(getContext(),quakeDetails.getMagColorResourceId());
        magCircle.setColor(magColor);


        return listItemView;
    }

    public DetailsAdapter(Context context, ArrayList<QuakeDetails> quakeDetails){
        super(context, 0, quakeDetails);
    }
}
