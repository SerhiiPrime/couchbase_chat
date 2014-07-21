package com.inc.linkbe.couchbasefirst;

import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.replicator.Replication;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Class designed as the best practise of implementing singleton
 * Created by linkbe on 7/21/14.
 */
public enum CBHelper {

    INSTANCE;


    private static final String TAG = "CBHelper";

    public Manager manager;
    private Database groceryDB = null;

    private final String GROCERY_CB_DB_NAME = "db_grocery";

    private final String BASE_URL = "http://192.168.1.104:4984/";

    private final String GROCERY_SYNC_URL = BASE_URL + GROCERY_CB_DB_NAME;


    public void init() {

        try {
            manager = new Manager(new AndroidContext(CouchbaseFirstApp.getAppContext()), Manager.DEFAULT_OPTIONS);
        } catch (IOException e) {
            Log.e(TAG, "Cannot create Manager instance", e);
            e.printStackTrace();
        }

        try {
            groceryDB = manager.getDatabase(GROCERY_CB_DB_NAME);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }

        startSync();
    }


    private void startSync() {

        URL syncURL = null;

        try {
            syncURL = new URL(GROCERY_SYNC_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Replication pullReplication = groceryDB.createPullReplication(syncURL);
        pullReplication.setContinuous(true);
        pullReplication.start();


        Replication pushReplication = groceryDB.createPushReplication(syncURL);
        pushReplication.setContinuous(true);
        pushReplication.start();

    }


    public Database getGroceryDB() {
        return groceryDB;
    }
}
