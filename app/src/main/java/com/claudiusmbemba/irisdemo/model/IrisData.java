package com.claudiusmbemba.irisdemo.model;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IrisData implements Parcelable {

    @SerializedName("Project")
    @Expose
    private String project;
    @SerializedName("Iteration")
    @Expose
    private String iteration;
    @SerializedName("Timestamp")
    @Expose
    private String timestamp;
    @SerializedName("Classifications")
    @Expose
    private List<Classification> classifications = null;

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getIteration() {
        return iteration;
    }

    public void setIteration(String iteration) {
        this.iteration = iteration;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<Classification> getClassifications() {
        return classifications;
    }

    public void setClassifications(List<Classification> classifications) {
        this.classifications = classifications;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.project);
        dest.writeString(this.iteration);
        dest.writeString(this.timestamp);
        dest.writeTypedList(this.classifications);
    }

    public IrisData() {
    }

    protected IrisData(Parcel in) {
        this.project = in.readString();
        this.iteration = in.readString();
        this.timestamp = in.readString();
        this.classifications = in.createTypedArrayList(Classification.CREATOR);
    }

    public static final Parcelable.Creator<IrisData> CREATOR = new Parcelable.Creator<IrisData>() {
        @Override
        public IrisData createFromParcel(Parcel source) {
            return new IrisData(source);
        }

        @Override
        public IrisData[] newArray(int size) {
            return new IrisData[size];
        }
    };
}