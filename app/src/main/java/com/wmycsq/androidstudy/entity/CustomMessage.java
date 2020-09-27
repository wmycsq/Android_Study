package com.wmycsq.androidstudy.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class CustomMessage implements Parcelable {

    private String content;
    private boolean isSendSuccess;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSendSuccess() {
        return isSendSuccess;
    }

    public void setSendSuccess(boolean sendSuccess) {
        isSendSuccess = sendSuccess;
    }

    public CustomMessage(String content) {
        this.content = content;
    }

    protected CustomMessage(Parcel in) {
        content = in.readString();
        isSendSuccess = in.readByte() != 0;
    }

    public static final Creator<CustomMessage> CREATOR = new Creator<CustomMessage>() {
        @Override
        public CustomMessage createFromParcel(Parcel in) {
            return new CustomMessage(in);
        }

        @Override
        public CustomMessage[] newArray(int size) {
            return new CustomMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeByte((byte) (isSendSuccess ? 1 : 0));
    }
}
