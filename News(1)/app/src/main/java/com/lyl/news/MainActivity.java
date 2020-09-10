package com.lyl.news;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    private NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSharedVals();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
//        Bundle bundle = data.getExtras();
//        ArrayList list  = bundle.getParcelableArrayList("list");
//        List<String> mVals = (List<String>) list.get(0);
        navController.navigate(R.id.navigation_home);
        //Log.d("Laze",mVals.get(0));
    }

    private void setSharedVals(){
        SharedPreferences my_tags = getSharedPreferences("my_tags", 0);
        boolean isFirstIn = my_tags.getBoolean("is_first_open", true);
        if (isFirstIn){
            SharedPreferences.Editor editor = my_tags.edit();
            editor.putBoolean("is_first_open", false);
            editor.putString("tags","新闻-论文" );
            editor.putString("untags","" );
            editor.commit();
        }
        //System.out.println(my_tags.getBoolean("is_first_open",true));
    }

}