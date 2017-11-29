package com.example.cmgoe.medtechkijenzi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by cmgoe on 10/31/2017.
 */

public class DesignListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Design> mDataSource;

    public DesignListAdapter(Context context, ArrayList<Design> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    //2
    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

    //4
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.list_item_design, parent, false);

        // Get title element
        TextView titleTextView =
                (TextView) rowView.findViewById(R.id.design_list_title);

        // Get subtitle element
        TextView subtitleTextView =
                (TextView) rowView.findViewById(R.id.design_list_subtitle);

        // Get detail element
        TextView detailTextView =
                (TextView) rowView.findViewById(R.id.design_list_detail);

        // Get thumbnail element
        ImageView thumbnailImageView =
                (ImageView) rowView.findViewById(R.id.design_list_thumbnail);

        Design design = (Design) getItem(position);

        titleTextView.setText(design.title);
        subtitleTextView.setText(design.desc);
        detailTextView.setText(design.url);

        //Picasso.with(mContext).load(design.imageUrl).placeholder(R.mipmap.ic_launcher).into(thumbnailImageView);

        return rowView;
    }
}
