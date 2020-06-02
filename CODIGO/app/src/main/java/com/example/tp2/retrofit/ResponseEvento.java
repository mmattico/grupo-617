package com.example.tp2.retrofit;

public class ResponseEvento {

    private String state;
    private String env;
    private ResponseEventItem event;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public ResponseEventItem getEvent() {
        return event;
    }

    public void setEvent(ResponseEventItem event) {
        this.event = event;
    }
}
