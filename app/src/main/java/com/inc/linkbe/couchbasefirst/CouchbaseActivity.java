package com.inc.linkbe.couchbasefirst;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.couchbase.lite.Document;
import com.couchbase.lite.LiveQuery;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.UnsavedRevision;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by linkbe on 7/17/14.
 */
public class CouchbaseActivity extends Activity implements View.OnClickListener {



    private ListView mChatListView;
    private EditText mChatEditText;
    private Button mChatButton;

    private LiveQuery liveQuery;
    private GrocerySyncListAdapter itemListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.couch_layout);

        mChatEditText = (EditText) findViewById(R.id.chat_edit_text);
        mChatButton = (Button) findViewById(R.id.btn_chat_send);
        mChatButton.setOnClickListener(this);




        mChatListView = (ListView) findViewById(R.id.chat_list_view);
    }


    private void startLiveQuery()  {

        if (liveQuery == null) {

            liveQuery = Message.getQuery(CBHelper.INSTANCE.getGroceryDB()).toLiveQuery();

            liveQuery.addChangeListener(new LiveQuery.ChangeListener() {
                @Override
                public void changed(LiveQuery.ChangeEvent event) {
                    displayRows(event.getRows());
                }
            });

            liveQuery.start();

        }

    }


    private void displayRows(QueryEnumerator queryEnumerator) {

        final List<QueryRow> rows = getRowsFromQueryEnumerator(queryEnumerator);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                itemListViewAdapter = new GrocerySyncListAdapter(
                        getApplicationContext(),
                        R.layout.frag_fri_list_item,
                        R.id.friend_name,
                        rows
                );
                mChatListView.setAdapter(itemListViewAdapter);

            }
        });
    }

    private List<QueryRow> getRowsFromQueryEnumerator(QueryEnumerator queryEnumerator) {
        List<QueryRow> rows = new ArrayList<QueryRow>();
        for (Iterator<QueryRow> it = queryEnumerator; it.hasNext(); ) {
            QueryRow row = it.next();
            rows.add(row);
        }
        return rows;
    }




    private Document createGroceryItem(String text) throws Exception {

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");


        Calendar calendar = GregorianCalendar.getInstance();

        String currentTimeString = dateFormatter.format(calendar.getTime());

        Document document = CBHelper.INSTANCE.getGroceryDB().createDocument();
        UnsavedRevision revision = document.createRevision();

        Map<String, Object> properties = new HashMap<String, Object>();

        properties.put("text", text);
        properties.put("time", currentTimeString);
        document.putProperties(properties);

        return document;
    }

    @Override
    public void onClick(View view) {
        String inputText = mChatEditText.getText().toString();
        if (!inputText.equals("")) {
            try {

                createGroceryItem(inputText);

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error creating document, see logs for details", Toast.LENGTH_LONG).show();

            }
        }
        mChatEditText.setText("");

    }
}
