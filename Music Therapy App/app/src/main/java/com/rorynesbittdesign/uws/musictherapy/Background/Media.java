package com.rorynesbittdesign.uws.musictherapy.Background;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

import com.rorynesbittdesign.uws.musictherapy.MainActivity;
import com.rorynesbittdesign.uws.musictherapy.UI.ControlFragment;

import java.io.IOException;
import java.util.ArrayList;

public class Media {
    private static int moodCount = ANN.moods.length;
    private static int current = 0;
    private static ArrayList allTracks = new ArrayList<Track>();
    private static ArrayList playlist = new ArrayList<Track>();

    public static MediaPlayer mediaPlayer;
    public static int  mood = 0;
    public static String[][] types = new String[moodCount][];
    public static String[] preferences = new String[moodCount];
    public static boolean preferenceChanged;
    public static Track currentTrack;
    public static boolean shuffle = false;


    public class Track {
        public String artist;
        public String title;
        public AssetFileDescriptor ref;
        public int mood;
        public String type;
    }


    public void initialise(){
        try {
            addTracks();
        } catch (IOException e) {
            e.printStackTrace();
        }
        start();
    }


    public void start(){
        if (mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
            generatePlaylist();
            currentTrack = (Track)playlist.get(current);
            AssetFileDescriptor ref = currentTrack.ref;
            try {
                mediaPlayer.setDataSource(ref.getFileDescriptor(),ref.getStartOffset(),ref.getLength());
                mediaPlayer.prepare();
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        skip();
                    }
                });
                trackInfo(moodChanged());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void stop(){
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer = null;
        } else { // this will fire if the stop button is hit a second time
            skip();
        }
    }


    public void skip(){
        if (mediaPlayer != null) stop();
        generatePlaylist();
        if (shuffle){
            current = (int) (Math.random()*playlist.size());
        } else {
            if (current >= playlist.size() - 1) {
                current = 0;
            } else {
                current++;
            }
        }
        start();
    }


    private void generatePlaylist() {
        if (moodChanged() || preferenceChanged) {
            preferenceChanged = false;
            playlist.clear();
            for (int i = 0; i < allTracks.size(); i++) {
                Track ct = (Track) allTracks.get(i);
                if (preferences[mood] == null) preferences[mood] = "All";
                if (ct.mood == mood && (ct.type == preferences[mood] || preferences[mood] == "All")) {
                    playlist.add(ct);
                }
            }
        }
    }


    public static boolean moodChanged(){
        if (currentTrack == null) return true;
        if (currentTrack.mood == mood){
            return false;
        } else {
            return true;
        }
    }


    public static int getPreference() {
        int currentMood = currentTrack.mood;
        if (preferences[currentMood] == "All") return 0;
        int index = 0;
        for (int i = 0; i < types[currentMood].length; i++) {
            if (types[currentMood][i] == preferences[currentMood]) index = i;
        }
        return index +1;
    }


    private void trackInfo(boolean moodChanged){
        try {
            ControlFragment.setDisplay(moodChanged);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addTracks() throws IOException {

        for (int i=0; i<moodCount; i++) {
            AssetManager assets = MainActivity.context.getAssets();
            types[i] = assets.list("music/" + i);

            for (String type : types[i]) {
                String[] tracks = assets.list("music/" + i + "/" + type);

                for (String name : tracks) {

                    AssetFileDescriptor fd = assets.openFd("music/" + i + "/" + type + "/" + name);

                    String info[] = name.split("\\.");
                    String title = info[0];
                    String artist = "";
                    if (info.length > 2) {
                        artist = info[1];
                    }

                    Track track = new Track();
                    track.ref = fd;
                    track.title = title;
                    track.artist = artist;
                    track.mood = i;
                    track.type = type;
                    allTracks.add(track);
                }
            }
        }
    }
}
