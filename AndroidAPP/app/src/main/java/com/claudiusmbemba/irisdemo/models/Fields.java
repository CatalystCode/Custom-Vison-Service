package com.claudiusmbemba.irisdemo.models;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fields implements Parcelable {

    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("brand_name")
    @Expose
    private String brandName;
    @SerializedName("nf_calories")
    @Expose
    private double nfCalories;
    @SerializedName("nf_total_fat")
    @Expose
    private double nfTotalFat;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public double getNfCalories() {
        return nfCalories;
    }

    public void setNfCalories(double nfCalories) {
        this.nfCalories = nfCalories;
    }

    public double getNfTotalFat() {
        return nfTotalFat;
    }

    public void setNfTotalFat(double nfTotalFat) {
        this.nfTotalFat = nfTotalFat;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.itemName);
        dest.writeString(this.brandName);
        dest.writeDouble(this.nfCalories);
        dest.writeDouble(this.nfTotalFat);
    }

    public Fields() {
    }

    protected Fields(Parcel in) {
        this.itemName = in.readString();
        this.brandName = in.readString();
        this.nfCalories = in.readDouble();
        this.nfTotalFat = in.readDouble();
    }

    public static final Parcelable.Creator<Fields> CREATOR = new Parcelable.Creator<Fields>() {
        @Override
        public Fields createFromParcel(Parcel source) {
            return new Fields(source);
        }

        @Override
        public Fields[] newArray(int size) {
            return new Fields[size];
        }
    };
}