package com.datang.miou.views.percept.page;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.datang.miou.R;
import com.datang.miou.views.data.DataUploadSettingFragment;
import com.datang.miou.views.data.MapFileDownloadFragment;
import com.datang.miou.views.data.PlanManagerFragment;
import com.datang.miou.views.percept.BasePageFragment;
import com.datang.miou.views.percept.result.ConnectFragment;
import com.datang.miou.views.percept.result.FtpFragment;
import com.datang.miou.views.percept.result.VideoFragment;
import com.datang.miou.views.percept.result.WebFragment;
import com.viewpagerindicator.TabPageIndicator;


public class ResultPageFragment extends BasePageFragment {

    private View root;

    @Override
    protected View initUI(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_page_result, null);

        FragmentPagerAdapter adapter = new DataFragmentAdapter(this.getActivity().getSupportFragmentManager());

        ViewPager pager = (ViewPager) root.findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TabPageIndicator indicator = (TabPageIndicator) root.findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        return root;

    }

    class DataFragmentAdapter extends FragmentPagerAdapter {
        private String[] CONTENT = new String[]{"网页", "视频", "测速","连接"};
        private Fragment[] fragments = new Fragment[]{new WebFragment(), new VideoFragment(), new FtpFragment(),new ConnectFragment()};

        public DataFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position % fragments.length];
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length].toUpperCase();
        }

        @Override
        public int getCount() {
            return CONTENT.length;
        }
    }
}