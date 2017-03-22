package com.claudiusmbemba.irisdemo.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fields implements Parcelable {

    @SerializedName("item_id")
    @Expose
    private String itemId;
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("brand_name")
    @Expose
    private String brandName;
    @SerializedName("nf_calories")
    @Expose
    private double nfCalories;
    @SerializedName("nf_calories_from_fat")
    @Expose
    private double nfCaloriesFromFat;
    @SerializedName("nf_total_fat")
    @Expose
    private double nfTotalFat;
    @SerializedName("nf_saturated_fat")
    @Expose
    private double nfSaturatedFat;
    @SerializedName("nf_trans_fatty_acid")
    @Expose
    private int nfTransFattyAcid;
    @SerializedName("nf_polyunsaturated_fat")
    @Expose
    private double nfPolyunsaturatedFat;
    @SerializedName("nf_monounsaturated_fat")
    @Expose
    private double nfMonounsaturatedFat;
    @SerializedName("nf_cholesterol")
    @Expose
    private int nfCholesterol;
    @SerializedName("nf_sodium")
    @Expose
    private double nfSodium;
    @SerializedName("nf_total_carbohydrate")
    @Expose
    private double nfTotalCarbohydrate;
    @SerializedName("nf_dietary_fiber")
    @Expose
    private double nfDietaryFiber;
    @SerializedName("nf_sugars")
    @Expose
    private double nfSugars;
    @SerializedName("nf_protein")
    @Expose
    private double nfProtein;
    @SerializedName("nf_vitamin_a_dv")
    @Expose
    private double nfVitaminADv;
    @SerializedName("nf_vitamin_c_dv")
    @Expose
    private double nfVitaminCDv;
    @SerializedName("nf_calcium_dv")
    @Expose
    private double nfCalciumDv;
    @SerializedName("nf_iron_dv")
    @Expose
    private double nfIronDv;
    @SerializedName("nf_serving_size_qty")
    @Expose
    private int nfServingSizeQty;
    @SerializedName("nf_serving_size_unit")
    @Expose
    private String nfServingSizeUnit;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

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

    public double getNfCaloriesFromFat() {
        return nfCaloriesFromFat;
    }

    public void setNfCaloriesFromFat(double nfCaloriesFromFat) {
        this.nfCaloriesFromFat = nfCaloriesFromFat;
    }

    public double getNfTotalFat() {
        return nfTotalFat;
    }

    public void setNfTotalFat(double nfTotalFat) {
        this.nfTotalFat = nfTotalFat;
    }

    public double getNfSaturatedFat() {
        return nfSaturatedFat;
    }

    public void setNfSaturatedFat(double nfSaturatedFat) {
        this.nfSaturatedFat = nfSaturatedFat;
    }

    public int getNfTransFattyAcid() {
        return nfTransFattyAcid;
    }

    public void setNfTransFattyAcid(int nfTransFattyAcid) {
        this.nfTransFattyAcid = nfTransFattyAcid;
    }

    public double getNfPolyunsaturatedFat() {
        return nfPolyunsaturatedFat;
    }

    public void setNfPolyunsaturatedFat(double nfPolyunsaturatedFat) {
        this.nfPolyunsaturatedFat = nfPolyunsaturatedFat;
    }

    public double getNfMonounsaturatedFat() {
        return nfMonounsaturatedFat;
    }

    public void setNfMonounsaturatedFat(double nfMonounsaturatedFat) {
        this.nfMonounsaturatedFat = nfMonounsaturatedFat;
    }

    public int getNfCholesterol() {
        return nfCholesterol;
    }

    public void setNfCholesterol(int nfCholesterol) {
        this.nfCholesterol = nfCholesterol;
    }

    public double getNfSodium() {
        return nfSodium;
    }

    public void setNfSodium(double nfSodium) {
        this.nfSodium = nfSodium;
    }

    public double getNfTotalCarbohydrate() {
        return nfTotalCarbohydrate;
    }

    public void setNfTotalCarbohydrate(double nfTotalCarbohydrate) {
        this.nfTotalCarbohydrate = nfTotalCarbohydrate;
    }

    public double getNfDietaryFiber() {
        return nfDietaryFiber;
    }

    public void setNfDietaryFiber(double nfDietaryFiber) {
        this.nfDietaryFiber = nfDietaryFiber;
    }

    public double getNfSugars() {
        return nfSugars;
    }

    public void setNfSugars(double nfSugars) {
        this.nfSugars = nfSugars;
    }

    public double getNfProtein() {
        return nfProtein;
    }

    public void setNfProtein(double nfProtein) {
        this.nfProtein = nfProtein;
    }

    public double getNfVitaminADv() {
        return nfVitaminADv;
    }

    public void setNfVitaminADv(double nfVitaminADv) {
        this.nfVitaminADv = nfVitaminADv;
    }

    public double getNfVitaminCDv() {
        return nfVitaminCDv;
    }

    public void setNfVitaminCDv(double nfVitaminCDv) {
        this.nfVitaminCDv = nfVitaminCDv;
    }

    public double getNfCalciumDv() {
        return nfCalciumDv;
    }

    public void setNfCalciumDv(double nfCalciumDv) {
        this.nfCalciumDv = nfCalciumDv;
    }

    public double getNfIronDv() {
        return nfIronDv;
    }

    public void setNfIronDv(double nfIronDv) {
        this.nfIronDv = nfIronDv;
    }

    public int getNfServingSizeQty() {
        return nfServingSizeQty;
    }

    public void setNfServingSizeQty(int nfServingSizeQty) {
        this.nfServingSizeQty = nfServingSizeQty;
    }

    public String getNfServingSizeUnit() {
        return nfServingSizeUnit;
    }

    public void setNfServingSizeUnit(String nfServingSizeUnit) {
        this.nfServingSizeUnit = nfServingSizeUnit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.itemId);
        dest.writeString(this.itemName);
        dest.writeString(this.brandName);
        dest.writeDouble(this.nfCalories);
        dest.writeDouble(this.nfCaloriesFromFat);
        dest.writeDouble(this.nfTotalFat);
        dest.writeDouble(this.nfSaturatedFat);
        dest.writeInt(this.nfTransFattyAcid);
        dest.writeDouble(this.nfPolyunsaturatedFat);
        dest.writeDouble(this.nfMonounsaturatedFat);
        dest.writeInt(this.nfCholesterol);
        dest.writeDouble(this.nfSodium);
        dest.writeDouble(this.nfTotalCarbohydrate);
        dest.writeDouble(this.nfDietaryFiber);
        dest.writeDouble(this.nfSugars);
        dest.writeDouble(this.nfProtein);
        dest.writeDouble(this.nfVitaminADv);
        dest.writeDouble(this.nfVitaminCDv);
        dest.writeDouble(this.nfCalciumDv);
        dest.writeDouble(this.nfIronDv);
        dest.writeInt(this.nfServingSizeQty);
        dest.writeString(this.nfServingSizeUnit);
    }

    public Fields() {
    }

    protected Fields(Parcel in) {
        this.itemId = in.readString();
        this.itemName = in.readString();
        this.brandName = in.readString();
        this.nfCalories = in.readDouble();
        this.nfCaloriesFromFat = in.readDouble();
        this.nfTotalFat = in.readDouble();
        this.nfSaturatedFat = in.readDouble();
        this.nfTransFattyAcid = in.readInt();
        this.nfPolyunsaturatedFat = in.readDouble();
        this.nfMonounsaturatedFat = in.readDouble();
        this.nfCholesterol = in.readInt();
        this.nfSodium = in.readDouble();
        this.nfTotalCarbohydrate = in.readDouble();
        this.nfDietaryFiber = in.readDouble();
        this.nfSugars = in.readDouble();
        this.nfProtein = in.readDouble();
        this.nfVitaminADv = in.readDouble();
        this.nfVitaminCDv = in.readDouble();
        this.nfCalciumDv = in.readDouble();
        this.nfIronDv = in.readDouble();
        this.nfServingSizeQty = in.readInt();
        this.nfServingSizeUnit = in.readString();
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