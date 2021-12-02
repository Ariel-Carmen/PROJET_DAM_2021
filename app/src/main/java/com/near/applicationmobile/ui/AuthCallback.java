package com.near.applicationmobile.ui;

public interface AuthCallback {

    void sendMessage(String phoneNumber);
    void verification(String code);

}
