package com.claudiusmbemba.irisdemo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Classification implements Parcelable {

    @SerializedName("ClassId")
    @Expose
    private String classId;
    @SerializedName("Class")
    @Expose
    private String _class;
    @SerializedName("Probability")
    @Expose
    private double probability;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClass_() {
        return _class;
    }

    public void setClass_(String _class) {
        this._class = _class;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.classId);
        dest.writeString(this._class);
        dest.writeDouble(this.probability);
    }

    public Classification() {
    }

    protected Classification(Parcel in) {
        this.classId = in.readString();
        this._class = in.readString();
        this.probability = in.readDouble();
    }

    public static final Parcelable.Creator<Classification> CREATOR = new Parcelable.Creator<Classification>() {
        @Override
        public Classification createFromParcel(Parcel source) {
            return new Classification(source);
        }

        @Override
        public Classification[] newArray(int size) {
            return new Classification[size];
        }
    };
}