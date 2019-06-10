package com.example.eventapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Hp on 7/21/2017.
 */

public class LabelItem implements Parcelable {


    @SerializedName("id")
    private String id;
    @SerializedName("label")
    private String label;
    @SerializedName("value")
    private String value;


    public LabelItem(String id,String label, String value) {
        this.id = id;
        this.label = label;
        this.value = value;
    }

    public static final Creator<LabelItem> CREATOR = new Creator<LabelItem>() {
        @Override
        public LabelItem createFromParcel(Parcel in) {
            return new LabelItem(in);
        }

        @Override
        public LabelItem[] newArray(int size) {
            return new LabelItem[size];
        }
    };



    public String getID() {
        return id;
    }
    public String getLabel() {
        return label;
    }

    public String getItemName() {
        return value;
    }


    private LabelItem(Parcel in) {
        id = in.readString();
        label = in.readString();
        value = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(label);
        dest.writeString(value);

    }
}



