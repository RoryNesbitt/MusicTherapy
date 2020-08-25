package com.rorynesbittdesign.uws.musictherapy.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.rorynesbittdesign.uws.musictherapy.R;
import com.rorynesbittdesign.uws.musictherapy.Background.Media;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);

        final Button mood0 = root.findViewById(R.id.button0);
        mood0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Media.mood = 0;
            }
        });

        final Button mood1 = root.findViewById(R.id.button1);
        mood1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Media.mood = 1;
            }
        });

        return root;
    }
}
