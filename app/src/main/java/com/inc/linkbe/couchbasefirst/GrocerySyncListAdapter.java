package com.inc.linkbe.couchbasefirst;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.couchbase.lite.Document;
import com.couchbase.lite.QueryRow;

import java.util.List;


public class GrocerySyncListAdapter extends ArrayAdapter<QueryRow> {

    private List<QueryRow> list;
    private final Context context;

    public GrocerySyncListAdapter(Context context, int resource, int textViewResourceId, List<QueryRow> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
        this.list = objects;
    }

	private static class ViewHolder {
	   TextView label;
	}

	@Override
	public View getView(int position, View itemView, ViewGroup parent) {
        if (itemView == null) {
            LayoutInflater vi = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = vi.inflate(R.layout.frag_fri_list_item, null);
            ViewHolder vh = new ViewHolder();
            vh.label = (TextView) itemView.findViewById(R.id.friend_name);
            itemView.setTag(vh);
        }

        TextView label = ((ViewHolder)itemView.getTag()).label;
        QueryRow row = list.get(position);
        Document document = row.getDocument();
        try {
            label.setText((String)document.getCurrentRevision().getProperty("text"));
        } catch (Exception e) {
            label.setText("Error");
        }

        return itemView;
	}
}
