package com.example.ace.vary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by ace on 9/14/18.
 */

public class mainViewPagerAdapter extends FragmentPagerAdapter {
    public mainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        position+=1;
        if(position==1)
        {LoginFragment loginFragment = new LoginFragment();

        Bundle bundle = new Bundle();
        bundle.putString("message","Login"+position);
        loginFragment.setArguments(bundle);
        return loginFragment;}
        else {
            SignUpFragment signUpFragment = new SignUpFragment();

            Bundle bundle = new Bundle();
            bundle.putString("message","Login"+position);
            signUpFragment.setArguments(bundle);
            return signUpFragment;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        position+=1;
        if(position==1) { return "Log In"; }
        else return "Sign Up";
    }
}
