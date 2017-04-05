package com.google.sample.echo;




import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    Context context;
    public PageAdapter(FragmentManager fm, int NumOfTabs,Context context) {//,Context context
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                mainFragment tab1 = new mainFragment();
                return tab1;
            case 1:
                requestFragment tab2 = new requestFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    public View getTabView(int position,Context context) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v = LayoutInflater.from(context).inflate(R.layout.costom_tab, null);
        TextView tv = (TextView) v.findViewById(R.id.count1);
        if(position>0) {
            tv.setBackgroundResource(R.drawable.circle);
            tv.setText("" + position);
        }
        else{
            tv.setText("N");
        }
        return v;
    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}

