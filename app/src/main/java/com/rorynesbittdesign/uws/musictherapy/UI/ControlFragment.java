package com.rorynesbittdesign.uws.musictherapy.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.rorynesbittdesign.uws.musictherapy.MainActivity;
import com.rorynesbittdesign.uws.musictherapy.Background.Media;
import com.rorynesbittdesign.uws.musictherapy.R;

public class ControlFragment extends Fragment {
    private static TextView textView;
    private static Spinner spinner;
    private static ImageButton stop;
    private static ImageButton skip;
    private static Switch shuffle;
    private static TextView test;
    private Media media = new Media();

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_control, container, false);
        textView = root.findViewById(R.id.text_controls);
        spinner = root.findViewById(R.id.spinnerType);
        stop = root.findViewById(R.id.stop);
        skip = root.findViewById(R.id.skip);
        shuffle = root.findViewById(R.id.shuffle);
        test = root.findViewById(R.id.test);

        setDisplay(true);

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                media.stop();
                setDisplay(true);
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                media.skip();
            }
        });

        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Media.shuffle) {
                    Media.shuffle = false;
                } else {
                    Media.shuffle = true;
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (Media.preferences[Media.currentTrack.mood] == null && item != null) Media.preferences[Media.currentTrack.mood] = item.toString();
                if (item != null && Media.preferences[Media.currentTrack.mood] != item.toString()) {
                    Media.preferences[Media.currentTrack.mood] = item.toString();
                    Media.preferenceChanged = true;
                    media.skip();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { } //this is not used but is needed for the function to compile
        });

        return root;
    }

    public static void setDisplay(boolean moodChanged){
        if (Media.mediaPlayer != null) {
            textView.setText("Currently playing:\n" + Media.currentTrack.artist + "\n" + Media.currentTrack.title);
            shuffle.setChecked(Media.shuffle);
            if (moodChanged) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.context, R.layout.spinner_item, addAll(Media.types[Media.currentTrack.mood]));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setSelection(Media.getPreference());
            }
        }else{
            textView.setText("No song playing");
            spinner.setAdapter(null);
        }
    }

    private static String[] addAll(String[] oldArray) {
        int size = oldArray.length;
        String[] newArray = new String[size+1];
        newArray[0] = "All";
        for (int i =0; i<size; i++) {
            newArray[i+1] = oldArray[i];
        }
        return newArray;
    }

    public static void countTest(int time) {
        test.setText(String.valueOf(time));
    }
}
