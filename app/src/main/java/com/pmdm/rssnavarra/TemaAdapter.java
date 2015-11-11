package com.pmdm.rssnavarra;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TemaAdapter extends ArrayAdapter<String> {

    Activity context;
    String[] datos;

    public TemaAdapter(Activity context, String[] datos) {
        super(context, R.layout.listitem_tema, datos);
        this.context = context;
        this.datos = datos;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        String tema = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_tema, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.LblListTemaTitle);
        tv.setText(tema);
        return convertView;
    }
}
