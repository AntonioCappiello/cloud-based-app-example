package com.antoniocappiello.cloudapp.service.event;

public class UpdateCurrentUserEmailEvent {
    private final String mEmail;

    public UpdateCurrentUserEmailEvent(String email) {
        mEmail = email;
    }

    public String getEmail() {
        return mEmail;
    }
}
