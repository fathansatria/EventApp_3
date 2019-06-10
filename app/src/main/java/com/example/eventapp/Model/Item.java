package com.example.eventapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Hp on 7/24/2017.
 */

public class Item implements Parcelable {

    @SerializedName("id")
    private String id;
    /* @SerializedName("harga")
     private int price; *///untuk harga yang berdasarkan kategori dikosongi
    @SerializedName("kategori")
    private String kategori;
    @SerializedName("title")
    private String title; //nama produk
    @SerializedName("content")
    private String content;
    @SerializedName("image")
    private String image;
    @SerializedName("author")
    private String author; //necessary
    @SerializedName("source")
    private String source;
    @SerializedName("tanggal")
    private String tanggal;
    @SerializedName("harga")
    private ArrayList<ItemType> harga;
    @SerializedName("detail")
    private ArrayList<LabelItem> detail;
    @SerializedName("type")
    private String type;
    @SerializedName("weight")
    private Integer weight;


    protected Item(Parcel in) {
        id = in.readString();
        kategori = in.readString();
        title = in.readString();
        content = in.readString();
        image = in.readString();
        author = in.readString();
        tanggal = in.readString();
        harga = in.createTypedArrayList(ItemType.CREATOR);
        detail = in.createTypedArrayList(LabelItem.CREATOR);
        weight = in.readInt();
        type = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getKategori() {
        return kategori;
    }
    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public Integer getWeight() {
        if (weight == null) {
            weight = 0;
        }
         return weight;
    }
    public void setWeight(int weight) {this.weight = weight;
    }

    public String getTanggal() {
        return tanggal;
    }
    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public ArrayList<ItemType> getHarga() { return harga;  }
    public void setType(ArrayList<ItemType> harga) {
        this.harga = harga;
    }

    public ArrayList<LabelItem> getDetail() { return detail;  }
    public void setDetail(ArrayList<LabelItem> detail) {
        this.detail = detail;
    }

    public Item(String id, String kategori, String title, String content, String image, String author, String tanggal, ArrayList<ItemType> harga, ArrayList<LabelItem> detail, Integer weight, String type) {

        this.id = id;
        this.kategori = kategori;
        this.title = title;
        this.content = content;
        this.image = image;
        this.author = author;
        this.tanggal = tanggal;
        this.harga = harga;
        this.detail = detail;
        this.weight = weight;
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(kategori);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(image);
        dest.writeString(author);
        dest.writeString(tanggal);
        dest.writeTypedList(harga);
        dest.writeTypedList(detail);
        dest.writeInt(weight);
        dest.writeString(type);
     }
}
