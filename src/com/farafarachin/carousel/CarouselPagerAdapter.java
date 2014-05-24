package com.farafarachin.carousel;

import com.farafarachin.MainActivity;
import com.farafarachin.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class CarouselPagerAdapter extends FragmentPagerAdapter implements
		ViewPager.OnPageChangeListener {

	private CarouselLinearLayout cur = null;
	private CarouselLinearLayout next = null;
	private MainActivity context;
	private FragmentManager fm;
	private float scale;

	public CarouselPagerAdapter(MainActivity context, FragmentManager fm) {
		super(fm);
		this.fm = fm;
		this.context = context;
	}

	@Override
	public Fragment getItem(int position) 
	{
        // make the first pager bigger than others
        if (position == MainActivity.FIRST_PAGE)
        	scale = MainActivity.BIG_SCALE;     	
        else
        	scale = MainActivity.SMALL_SCALE;
        
        position = position % MainActivity.PAGES;
        
        return CarouselFragment.newInstance(context, position, scale);
	}

	@Override
	public int getCount()
	{		
		return MainActivity.PAGES * MainActivity.LOOPS;
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) 
	{	
		if (positionOffset >= 0f && positionOffset <= 1f)
		{
			cur = getRootView(position);
			next = getRootView(position +1);

			cur.setScaleBoth(MainActivity.BIG_SCALE 
					- MainActivity.DIFF_SCALE * positionOffset);
			next.setScaleBoth(MainActivity.SMALL_SCALE 
					+ MainActivity.DIFF_SCALE * positionOffset);
		}
	}

	@Override
	public void onPageSelected(int position) {}
	
	@Override
	public void onPageScrollStateChanged(int state) {}
	
	private CarouselLinearLayout getRootView(int position)
	{
		return (CarouselLinearLayout) 
				fm.findFragmentByTag(this.getFragmentTag(position))
				.getView().findViewById(R.id.root);
	}
	
	private String getFragmentTag(int position)
	{
	    return "android:switcher:" + context.pager.getId() + ":" + position;
	}
}
