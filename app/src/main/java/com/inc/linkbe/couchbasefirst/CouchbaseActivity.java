package com.inc.linkbe.couchbasefirst;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Query;

/**
 * Created by linkbe on 7/17/14.
 */
public class CouchbaseActivity extends Activity implements View.OnClickListener {



    private ListView mChatListView;
    private EditText mChatEditText;
    private Button mChatButton;

    private Query mQuery;
    private ChatAdapter mChatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.couch_layout);

        mChatEditText = (EditText) findViewById(R.id.chat_edit_text);
        mChatButton = (Button) findViewById(R.id.btn_chat_send);
        mChatButton.setOnClickListener(this);

        mQuery = Message.getQuery(CBHelper.INSTANCE.getGroceryDB());

        mChatAdapter = new ChatAdapter(this, mQuery.toLiveQuery());

        mChatListView = (ListView) findViewById(R.id.chat_list_view);
        mChatListView.setAdapter(mChatAdapter);
    }




    @Override
    public void onClick(View view) {

        String inputText = mChatEditText.getText().toString();
        if (!inputText.equals("")) {

            try {
                Message.createMessage(CBHelper.INSTANCE.getGroceryDB(), inputText.toString());
            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            }
        }
        mChatEditText.setText("");
    }
}
