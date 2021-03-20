package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.fragment;

public interface Subject {
    void notifyObservers();

    void register(Observer observer);

    void unregister(Observer observer);
}
