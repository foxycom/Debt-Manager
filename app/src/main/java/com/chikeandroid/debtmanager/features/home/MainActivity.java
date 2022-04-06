package com.chikeandroid.debtmanager.features.home;

import android.app.Activity;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.VisibleForTesting;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.test.espresso.IdlingResource;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.chikeandroid.debtmanager.R;
import com.chikeandroid.debtmanager.data.Debt;
import com.chikeandroid.debtmanager.databinding.ActivityMainBinding;
import com.chikeandroid.debtmanager.event.MainViewPagerSwipeEvent;
import com.chikeandroid.debtmanager.features.addeditdebt.AddEditDebtActivity;
import com.chikeandroid.debtmanager.features.home.adapter.HomeFragmentPagerAdapter;
import com.chikeandroid.debtmanager.util.EspressoIdlingResource;
import com.chikeandroid.debtmanager.util.ViewUtil;

import org.greenrobot.eventbus.EventBus;


public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    public static final String EXTRA_DEBT_TYPE = "com.chikeandroid.debtmanager20.features.home.debt_type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Toolbar toolbar = binding.toolbarMainIncluded.toolbarMain;
        setSupportActionBar(toolbar);

        mViewPager = binding.viewPagerMain;
        mViewPager.setAdapter(new HomeFragmentPagerAdapter(getSupportFragmentManager()));
        mViewPager.setOffscreenPageLimit(3);

        TabLayout tabLayout = binding.tabLayoutMain;
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = binding.fabMain;
        fab.setOnClickListener(view -> AddEditDebtActivity.start(MainActivity.this, AddEditDebtActivity.REQUEST_ADD_DEBT));

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            // optional
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                EventBus.getDefault().post(new MainViewPagerSwipeEvent("Swiped"));
            } });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (AddEditDebtActivity.REQUEST_ADD_DEBT == requestCode && Activity.RESULT_OK == resultCode) {

            ViewUtil.showToast(this, getString(R.string.msg_debt_save_success));

            if (data != null && data.getIntExtra(EXTRA_DEBT_TYPE, -1) == Debt.DEBT_TYPE_IOWE) {
                mViewPager.setCurrentItem(1, true);
            }else if (data != null && data.getIntExtra(EXTRA_DEBT_TYPE, -1) == Debt.DEBT_TYPE_OWED) {
                mViewPager.setCurrentItem(0, true);
            }
        }
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
