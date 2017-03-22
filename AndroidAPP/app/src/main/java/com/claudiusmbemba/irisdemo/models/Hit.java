package com.claudiusmbemba.irisdemo.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Hit implements Parcelable {

    @SerializedName("_index")
    @Expose
    private String index;
    @SerializedName("_type")
    @Expose
    private String type;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("_score")
    @Expose
    private double score;
    @SerializedName("fields")
    @Expose
    private Fields fields;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.index);
        dest.writeString(this.type);
        dest.writeString(this.id);
        dest.writeDouble(this.score);
        dest.writeParcelable(this.fields, flags);
    }

    public Hit() {
    }

    protected Hit(Parcel in) {
        this.index = in.readString();
        this.type = in.readString();
        this.id = in.readString();
        this.score = in.readDouble();
        this.fields = in.readParcelable(Fields.class.getClassLoader());
    }

    public static final Parcelable.Creator<Hit> CREATOR = new Parcelable.Creator<Hit>() {
        @Override
        public Hit createFromParcel(Parcel source) {
            return new Hit(source);
        }

        @Override
        public Hit[] newArray(int size) {
            return new Hit[size];
        }
    };
}