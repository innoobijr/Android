package com.bitrider.survey.SignIn;

public interface DataManager{
    void saveName(String visible);
    void saveUserStatus(int status);
    int getUserStatus();
    String getName();
    void savePermissionStatus(boolean status);
    boolean getPermissionStatus();
}