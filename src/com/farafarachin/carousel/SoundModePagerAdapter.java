package com.farafarachin.carousel;

import java.util.ArrayList;
import java.util.List;

import com.farafarachin.MainActivity;
import com.farafarachin.R;
import com.farafarachin.vo.InstrumentVO;

import android.R.string;
import android.content.ClipData.Item;
import android.provider.Telephony.Mms.Addr;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class SoundModePagerAdapter extends FragmentPagerAdapter implements
		ViewPager.OnPageChangeListener {

	private CarouselLinearLayout cur = null;
	private CarouselLinearLayout next = null;
	private MainActivity context;
	private FragmentManager fm;
	private float scale;
	
	private ArrayList<String> modeList;

	public SoundModePagerAdapter(MainActivity context, FragmentManager fm) {
		super(fm);
		this.fm = fm;
		this.context = context;
		
		//
		// init instrument data
		//
		
		initsoundModeData();
	}
	
	private void initsoundModeData()
	{
		modeList = new ArrayList<String>();
		
		modeList.add("Sonido");
		
		modeList.add("Melodia");
		
		modeList.add("Comparsa");
		
		context.selectedMode = 0; // 0 == sonido; 1 == melodia; 2 == comparsa
	}

	@Override
	public Fragment getItem(int position)
	{
        // make the first pager bigger than others
        if (position == MainActivity.FIRST_PAGE)
        {
         	scale = MainActivity.BIG_SCALE;
        }
        else
        {
        	scale = MainActivity.SMALL_SCALE;
        }
        
        position = position % MainActivity.INSTRUMENT_PAGES;
        
        return SoundModeFragment.newInstance(context, position,modeList.get(position), scale);
	}

	@Override
	public int getCount()
	{		
		return MainActivity.INSTRUMENT_PAGES * MainActivity.LOOPS;
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) 
	{	
		//
		// extract the selected page based on its position
		// awful !!
		//
				
//		int index = position % MainActivity.MODE_PAGES;
//		
//		if(context!=null)
//		{
//			context.selectMode(index);
//		}
		
		//
		// scroll
		//
		
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
	public void onPageSelected(int position) {
		
		int index = position % MainActivity.MODE_PAGES;
		
		if(context!=null)
		{
			context.selectMode(index);
			
			Log.i("chucha", "ave maria " + index);
		}
	}
	
	@Override
	public void onPageScrollStateChanged(int state) {
		
		
		
	}
	
	private CarouselLinearLayout getRootView(int position)
	{
		return (CarouselLinearLayout) 
				fm.findFragmentByTag(this.getFragmentTag(position))
				.getView().findViewById(R.id.root);
	}
	
	private String getFragmentTag(int position)
	{
	    return "android:switcher:" + context.soundModePager.getId() + ":" + position;
	}
}
