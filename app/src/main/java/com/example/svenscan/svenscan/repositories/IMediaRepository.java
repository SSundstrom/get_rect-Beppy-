package com.example.svenscan.svenscan.repositories;

import android.net.Uri;

import java.io.File;

public interface IMediaRepository {
    void addSound(String absSoundPath);
    void addImage(String absImagePath);
    void getSoundUri(String soundName, IMediaHandler handler);
    void getImageUri(String soundName, IMediaHandler handler);
    void initialize(File dataPath);

    interface IMediaHandler {
        void onMediaAvailable(Uri path);
    }
}
