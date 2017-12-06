package com.example.cmgoe.medtechkijenzi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by cmgoe on 10/31/2017.
 */

public class DesignListAdapter extends BaseAdapter {
    private Context mContext;
    private FirebaseFiles fireb;
    private File imageThumbnail;
    ImageView thumbnailImageView;
    private LayoutInflater mInflater;
    private ArrayList<Design> mDataSource;

    public DesignListAdapter(Context context, ArrayList<Design> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        fireb = new FirebaseFiles();
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
        thumbnailImageView =
                (ImageView) rowView.findViewById(R.id.design_list_thumbnail);

        Design design = (Design) getItem(position);

        titleTextView.setText(design.title);
        subtitleTextView.setText(design.desc);
        detailTextView.setText(design.url);

        StorageReference ref = fireb.getStorageRef(getImageUrl(design.url));

        GlideApp.with(mContext)
                .load(ref)
                .into(thumbnailImageView);

//        imageThumbnail = fireb.getFile(getImageUrl(design.url), ".jpg");
//        System.out.println(getImageUrl(design.url) + " here is the image url");
//
//        if(imageThumbnail.exists()){
//            System.out.println("image thumbnail exists");
//
//        }
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //updatePicture();
//            }
//        }, 5000);
//        Picasso.with(mContext).load(imageThumbnail).into(thumbnailImageView);
//        //.placeholder(R.mipmap.ic_launcher)
//        //thumbnailImageView
//
//        //Picasso.with(mContext).load(design.imageUrl).placeholder(R.mipmap.ic_launcher).into(thumbnailImageView);

        return rowView;
    }

    private String getImageUrl(String designUrl){
        return designUrl.split("\\.")[0] + ".jpg";
    }

    public void updatePicture () {
        System.out.println("update picture");
        Picasso.with(mContext).load(imageThumbnail).into(thumbnailImageView);
    }
}
