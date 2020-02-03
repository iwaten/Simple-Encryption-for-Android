package secretswamp.simpleencryption;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(secretswamp.simpleencryption.R.layout.activity_main);

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = findViewById(secretswamp.simpleencryption.R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = findViewById(secretswamp.simpleencryption.R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new GenerateKeyPairFragment(), "GENERATE");
        adapter.addFragment(new DecryptMessageFragment(), "DECRYPT");
        adapter.addFragment(new EncryptMessageFragment(), "ENCRYPT");
        viewPager.setAdapter(adapter);

    }


}
