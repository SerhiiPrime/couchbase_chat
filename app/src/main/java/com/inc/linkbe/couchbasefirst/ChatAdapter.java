package com.inc.linkbe.couchbasefirst;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.couchbase.lite.Document;
import com.couchbase.lite.LiveQuery;

/**
 * Created by linkbe on 7/21/14.
 */
public class ChatAdapter extends LiveQueryAdapter {

    public ChatAdapter(Context context, LiveQuery query) {
        super(context, query);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.frag_fri_list_item, null);
        }

        final Document document = (Document) getItem(position);
        String t = (String) document.getProperty("text");

        TextView text = (TextView) convertView.findViewById(R.id.friend_name);
        text.setText(t);

        return convertView;
    }
}
