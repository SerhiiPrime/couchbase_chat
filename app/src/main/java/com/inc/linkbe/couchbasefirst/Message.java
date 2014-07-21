package com.inc.linkbe.couchbasefirst;

import com.couchbase.lite.Database;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by linkbe on 7/21/14.
 */
public class Message implements Serializable {

    private static final String VIEW_NAME_DEFAULT = "messages";
    public static final String DOC_TYPE = "message";

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @JsonProperty("text")
    private String mText;

    @JsonProperty("time")
    private Date mTimeStamp;


    public Message() {
    }

//    public Message(Document document) {
//
//        if (document == null) {
//            throw new IllegalArgumentException("doc cannot be null");
//        }
//
//        Map<String, Object> map = document.getProperties();
//
//        this.mId = Long.parseLong(map.get("id").toString());
//        this.mSender = Long.parseLong(map.get("sender").toString());
//        this.mReceiver = Long.parseLong(map.get("receiver").toString());
//        this.mText = (String) map.get("text");
//
//        try {
//            this.mTimeStamp = formatter.parse(map.get("time").toString());
//        } catch (ParseException e) {
//            Log.e("ololo", "Date parsing failed: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }

    public Message(Map<String, Object> map) {

        this.mText = String.valueOf(map.get("text"));

        try {
            this.mTimeStamp = formatter.parse(map.get("time").toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public static Query getQuery(Database db) {
        com.couchbase.lite.View cbView = db.getView(String.format("%s/%s", db.getName(), VIEW_NAME_DEFAULT));

        if (cbView.getMap() == null) {
            Mapper mapper = new Mapper() {
                @Override
                public void map(Map<String, Object> stringObjectMap, Emitter emitter) {
                    String type = (String) stringObjectMap.get("type");
                    if (DOC_TYPE.equals(type)) {
                        emitter.emit(stringObjectMap.get("id"), stringObjectMap);
                    }
                }
            };
            cbView.setMap(mapper, "0.1");
        }

        return cbView.createQuery();
    }

    public String getText() {
        return mText;
    }

    public Date getTimeStamp() {
        return mTimeStamp;
    }

    public static SimpleDateFormat getFormatter() {
        return formatter;
    }
}