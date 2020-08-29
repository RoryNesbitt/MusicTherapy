package com.rorynesbittdesign.uws.musictherapy.Background;

import android.util.Log;

import com.rorynesbittdesign.uws.musictherapy.MainActivity;
import com.rorynesbittdesign.uws.musictherapy.UI.ControlFragment;
import com.rorynesbittdesign.uws.musictherapy.UI.DashboardFragment;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTT {

    private static MqttAndroidClient client;
    private static String clientId = "";
    private static String host = "tcp://mqtt.rorynesbittdesign.com";
    private static String username = "";
    private static String password = "";
    private static long time = 0;
    private static String TAG = "MQTT";
    private static MqttConnectOptions mqttConnectOptions;

    public static String status;
    public static int col;
    public static int heart;
    public static String blood;
    public static int breath;

    public static void initialise() {

        //initialise defaults
        status = "Disconnected";
        col = 0xFFFF0000;
        heart = 0;
        blood = "0";
        breath = 0;

        //Generate unique client id for MQTT broker connection
        int r = (int) Math.random()*1000;
        clientId = "MusicTherapy" + r;

        mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        if(username.length() > 0) {
            mqttConnectOptions.setUserName(username);
        }
        mqttConnectOptions.setPassword(password.toCharArray());

        generateClient();
        connect();
    }

    private static void generateClient() {
        client = new MqttAndroidClient(MainActivity.context, host, clientId);
        client.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                status = "Connected";
                col = 0xFF00FF00; //Green if connected
                subscribe("#");
                try {
                    DashboardFragment.getStatus();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                status = "Disconnected";
                col = 0xFFFF0000; //Red if not connected
                try {
                    DashboardFragment.getStatus();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Boolean updated = false;
                switch (topic) {
                    case "test": //No messages will be sent with this topic out with testing
                        String msg = "topic: " + topic + "\r\nMessage: " + message.toString() + "\r\n";
                        DashboardFragment.statsText.setText(msg);
                        break;
                    case "heart":
                        heart = Integer.parseInt(message.toString());
                        updated = true;
                        break;
                    case "blood":
                        blood = message.toString();
                        updated = true;
                        break;
                    case "breath":
                        breath = Integer.parseInt(message.toString());
                        updated = true;
                        break;
                    /*case "mood": //test code
                        Media.mood = Integer.parseInt(message.toString());
                        updated = true;
                        break;
                     */
                }
                if (updated){
                    int target = 300; //number of second until it forces the music to update
                    target*=1000;// convert to milliseconds

                    ANN ann = new ANN();
                    int newMood = ann.predict();
                    long newTime = System.currentTimeMillis();
                    long duration = newTime - time;
                    if ( newMood != Media.mood){
                        Media.mood = newMood;
                        time = newTime;
                    } else if (duration >= target && Media.currentTrack.mood != Media.mood){
                        Media media = new Media();
                        media.skip();
                        time = newTime;
                    }
                    try {
                        ControlFragment.countTest((int) duration/1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    DashboardFragment.showStats();
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                //This app doesn't send messages
            }
        });
    }

    public static void connect() {

        if (client.isConnected()) {
            //Disconnect and Reconnect to  Broker
            try {
                //Disconnect from Broker
                client.disconnect();
            } catch (MqttException e) {
            }
        }

        try {
            client.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i(TAG, "connect succeed");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i(TAG, "connect failed");
                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public static void subscribe(String topic) {
        try {
            client.subscribe(topic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i(TAG, "subscribed succeed");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i(TAG, "subscribed failed");
                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
