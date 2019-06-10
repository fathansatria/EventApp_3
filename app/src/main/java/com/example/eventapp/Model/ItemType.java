package com.example.eventapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Hp on 7/24/2017.
 */

public class ItemType implements Parcelable {

    @SerializedName("id")
    private String id;
    @SerializedName("harga")
    private String harga;
    @SerializedName("discount")
    private String discount;
    @SerializedName("description")
    private String description;
    private boolean isSelected;

    public ItemType(String id, String harga, String description, boolean isSelected) {
        this.id = id;
        this.harga = harga;
        this.description = description;
        this.isSelected = isSelected;
    }

    protected ItemType(Parcel in) {
        id = in.readString();
        harga = in.readString();
        discount = in.readString();
        description = in.readString();
    }

    public static final Creator<ItemType> CREATOR = new Creator<ItemType>() {
        @Override
        public ItemType createFromParcel(Parcel in) {
            return new ItemType(in);
        }

        @Override
        public ItemType[] newArray(int size) {
            return new ItemType[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHarga() {
        return harga;
    }
    public void setHarga(String harga) {
        this.harga = harga;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
    public String getDiscount() {
        return discount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(harga);
        dest.writeString(discount);
        dest.writeString(description);
    }
}