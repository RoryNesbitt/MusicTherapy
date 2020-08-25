package com.rorynesbittdesign.uws.musictherapy.Background;

import android.content.res.AssetFileDescriptor;


import com.rorynesbittdesign.uws.musictherapy.MainActivity;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class ANN {
    private static final String MODEL_PATH = "model.tflite";
    public Interpreter tflite;

    public static String[] moods = new String[3];

    public void initialise() {

        moods[0] = "Normal";
        moods[1] = "Stressed";
        moods[2] = "Exercising";


    }

    public int predict(){
        if (MQTT.heart == 0 || MQTT.blood == "0" || MQTT.breath == 0) {
            // return the default value
            // if any of these are zero there is a connection issue
            return 0;
        }else {
            try {
                tflite = new Interpreter(loadModelFile());
            } catch (IOException e) {
                e.printStackTrace();
            }

            float[] input = new float[4];
            float[][] output = new float[1][moods.length];

            String[] blood = splitBlood();
            input[0] = MQTT.heart;
            input[1] = Integer.parseInt(blood[0]);
            input[2] = Integer.parseInt(blood[1]);
            input[3] = MQTT.breath;

            tflite.run(input, output);

            //DashboardFragment.checkOutput(outputVals[0][0]); //test code
            return parsePrediction(output[0]);
        }
    }

    private int parsePrediction(float[] vals) {
        int highest = 0;
        float result = 0;
        for (int i=0; i < vals.length; i++) {
            if (vals[i] > result){
                highest = i;
                result = vals[i];
            }
        }
        return highest;
    }


    private static String[] splitBlood(){
        String[] blood;
        try {
            blood = MQTT.blood.split("\\/");
        } catch (NumberFormatException e) {
            blood = null;
            e.printStackTrace();
        }
        return blood;
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = MainActivity.context.getAssets().openFd(MODEL_PATH);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

}
