package secretswamp.simpleencryption.com.tabian.tabfragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import secretswamp.simpleencryption.com.tabian.tabfragments.pgp.PGPUtils;

import java.security.KeyPair;

import java.io.*;
import java.security.*;
import android.content.Context;
import javax.crypto.*;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(secretswamp.simpleencryption.R.layout.activity_main);
        Log.d(TAG, "onCreate: Starting.");

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(secretswamp.simpleencryption.R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(secretswamp.simpleencryption.R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        //SharedPreferences sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        //checkSharedPreferences(sharedPref);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1Fragment(), "GENERATOR");
        adapter.addFragment(new Tab2Fragment(), "DECRYPT");
        adapter.addFragment(new Tab3Fragment(), "ENCRYPT");
        viewPager.setAdapter(adapter);

    }

//    private void checkSharedPreferences(SharedPreferences pref) {
//        KeyPair kp = PGPUtils.generateKeyPair();
//        if(!pref.contains("pub-key") || !pref.contains("priv-key")) {
//            SharedPreferences.Editor editor = pref.edit();
//            editor.putString("pub-key", PGPUtils.encodePublic(kp.getPublic()));
//            editor.putString("priv-key", PGPUtils.encodePrivate(kp.getPrivate()));
//            editor.commit();
//
//        }
//    }
}

