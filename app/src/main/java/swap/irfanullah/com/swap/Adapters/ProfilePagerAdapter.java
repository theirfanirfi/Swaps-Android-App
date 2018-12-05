package swap.irfanullah.com.swap.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import swap.irfanullah.com.swap.Fragments.Profile.StatusesFragment;
import swap.irfanullah.com.swap.Fragments.Profile.SwapsFragment;

public class ProfilePagerAdapter extends FragmentPagerAdapter {
    public ProfilePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        switch (i)
        {
            case 0:
                StatusesFragment statusesFragment = new StatusesFragment();
                return statusesFragment;
            case 1:
                SwapsFragment swapsFragment = new SwapsFragment();
                return swapsFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
