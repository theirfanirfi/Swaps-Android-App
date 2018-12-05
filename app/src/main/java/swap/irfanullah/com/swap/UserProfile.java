package swap.irfanullah.com.swap;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import swap.irfanullah.com.swap.Adapters.ProfilePagerAdapter;

public class UserProfile extends AppCompatActivity {
    private ViewPager viewPager;
    private ProfilePagerAdapter profilePagerAdapter;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        viewPager = findViewById(R.id.profileViewPage);
        profilePagerAdapter = new ProfilePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(profilePagerAdapter);

        tabLayout = findViewById(R.id.profileTabLayout);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

    }
}
