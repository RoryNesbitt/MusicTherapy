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
import com.rorynesbittdesign.uws.musictherapy.Background.ANN;
import com.rorynesbittdesign.uws.musictherapy.Background.MQTT;
import com.rorynesbittdesign.uws.musictherapy.Background.Media;

public class DashboardFragment extends Fragment {

    public static TextView statusText;
    public static TextView statsText;
    public static TextView dashText;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        statusText = root.findViewById(R.id.text_status);
        statsText = root.findViewById(R.id.text_stats);
        dashText = root.findViewById(R.id.text_dashboard);
        getStatus();
        showStats();

        ///////////////////////////////////////////////////////////////////
        //Test Button to subscribe //Not required in final release
        final Button subscribe = root.findViewById(R.id.subscribe);
        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MQTT.subscribe("#");
            }
        });

        //Test Button to run nn //Not required in final release
        final Button infer = root.findViewById(R.id.infer);
        infer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ANN ann = new ANN();
                int x = ann.predict();
                String y = String.valueOf(x);
                infer.setText(y);

            }
        });

        return root;
    }

    public static void getStatus(){
        statusText.setText(MQTT.status);
        statusText.setTextColor(MQTT.col);
    }

    public static void checkOutputs(float x){ //test func
        dashText.setText(String.valueOf(x));
    }

    public static void showStats(){
        int heart = MQTT.heart;
        String blood = MQTT.blood;
        int breath = MQTT.breath;
        String mood = ANN.moods[Media.mood];
        statsText.setText("Heart rate:\n" + heart + " Beats/Minute\n\nBlood pressure:\n" + blood + " mmgHG\n\nRespiratory:\n" + breath + " Breaths/Minute\n\nHealth status:\n" + mood);
    }
}
