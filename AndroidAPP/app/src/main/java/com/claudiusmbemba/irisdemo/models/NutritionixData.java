package com.claudiusmbemba.irisdemo.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NutritionixData implements Parcelable {

    @SerializedName("total_hits")
    @Expose
    private int totalHits;
    @SerializedName("max_score")
    @Expose
    private double maxScore;
    @SerializedName("hits")
    @Expose
    private List<Hit> hits = null;

    public int getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(int totalHits) {
        this.totalHits = totalHits;
    }

    public double getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(double maxScore) {
        this.maxScore = maxScore;
    }

    public List<Hit> getHits() {
        return hits;
    }

    public void setHits(List<Hit> hits) {
        this.hits = hits;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.totalHits);
        dest.writeDouble(this.maxScore);
        dest.writeTypedList(this.hits);
    }

    public NutritionixData() {
    }

    protected NutritionixData(Parcel in) {
        this.totalHits = in.readInt();
        this.maxScore = in.readDouble();
        this.hits = in.createTypedArrayList(Hit.CREATOR);
    }

    public static final Parcelable.Creator<NutritionixData> CREATOR = new Parcelable.Creator<NutritionixData>() {
        @Override
        public NutritionixData createFromParcel(Parcel source) {
            return new NutritionixData(source);
        }

        @Override
        public NutritionixData[] newArray(int size) {
            return new NutritionixData[size];
        }
    };
}