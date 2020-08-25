package com.rorynesbittdesign.uws.musictherapy;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rorynesbittdesign.uws.musictherapy.Background.ANN;
import com.rorynesbittdesign.uws.musictherapy.Background.MQTT;
import com.rorynesbittdesign.uws.musictherapy.Background.Media;

public class MainActivity extends AppCompatActivity {

    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        Media media = new Media();
        ANN ann = new ANN();

        //Start the background class'
        MQTT.initialise();
        ann.initialise();
        if (media.mediaPlayer == null) media.initialise();


        ///////////////////////////////////////////////////////////////////////////////////////////////////

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_controls)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }
}
