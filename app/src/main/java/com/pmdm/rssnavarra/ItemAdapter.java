package com.pmdm.rssnavarra;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

public class ItemAdapter extends ArrayAdapter<Item> {

    Activity context;
    Item[] datos;

    public ItemAdapter(Activity context, Item[] datos) {
        super(context, R.layout.listitem_item, datos);
        this.context = context;
        this.datos = datos;
    }

    @Override
    public int getPosition(Item item) {
        return super.getPosition(item);
    }

    @Override
    public Item getItem(int position) {
        return datos[position];
    }

    @Override
    public int getCount() {
        return datos.length;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Item item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_item, parent, false);
        }
        TextView tvTitle = (TextView) convertView.findViewById(R.id.LblListItemTitle);
        TextView tvPubDateParsed = (TextView) convertView.findViewById(R.id.LblListItemPubDateParsed);
        //TextView tvDescription = (TextView) convertView.findViewById(R.id.LblListItemDescription);

        tvTitle.setText(item.getTitle());
        tvPubDateParsed.setText(item.getPubDateParsed());
        //tvDescription.setText(item.getDescription());

        new DownloadImageTask((ImageView) convertView.findViewById(R.id.ImgListItemEnclosureUrl))
                .execute(item.getEnclosureUrl());

        return convertView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            bmImage.setFocusable(false);
            bmImage.setFocusableInTouchMode(false);
        }
    }
}