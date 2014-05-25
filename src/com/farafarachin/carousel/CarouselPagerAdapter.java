package com.farafarachin.carousel;

import java.util.ArrayList;
import java.util.List;

import com.farafarachin.MainActivity;
import com.farafarachin.R;
import com.farafarachin.vo.InstrumentVO;

import android.provider.Telephony.Mms.Addr;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class CarouselPagerAdapter extends FragmentPagerAdapter implements
		ViewPager.OnPageChangeListener {

	private CarouselLinearLayout cur = null;
	private CarouselLinearLayout next = null;
	private MainActivity context;
	private FragmentManager fm;
	private float scale;
	
	private ArrayList<InstrumentVO> instrumentList;

	public CarouselPagerAdapter(MainActivity context, FragmentManager fm) {
		super(fm);
		this.fm = fm;
		this.context = context;
		
		//
		// init instrument data
		//
		
		initInstrumentData();
	}
	
	private void initInstrumentData()
	{
		instrumentList = new ArrayList<InstrumentVO>();
		
		// bombo
		
		InstrumentVO inst1 = new InstrumentVO();
		
		inst1.name = "Bombo";
		
		inst1.imageResID = R.drawable.bombo;
		
		inst1.soundResID = R.raw.bombo_hit;
		
		inst1.melodyResID = R.raw.bombo_melodia;
		
		instrumentList.add(inst1);
		
		// platillos
		
		InstrumentVO inst2 = new InstrumentVO();
		
		inst2.name = "Platillos";
		
		inst2.imageResID = R.drawable.platillos;
		
		inst2.soundResID = R.raw.platillos_hit;
		
		inst2.melodyResID = R.raw.platillos_melodia;
		
		instrumentList.add(inst2);
		
		// redoblante
		
		InstrumentVO inst3 = new InstrumentVO();
		
		inst3.name = "Redoblante";
		
		inst3.imageResID = R.drawable.redoblante;
		
		inst3.soundResID = R.raw.redoblante_hit;
		
		inst3.melodyResID = R.raw.redoblante_melodia;
		
		instrumentList.add(inst3);
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
        
        return CarouselFragment.newInstance(context, position,instrumentList.get(position), scale);
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
		// extract the selected instrument based on its position
		// awful !!
		//
		
		int index = position % MainActivity.INSTRUMENT_PAGES;
		
		context.selectedInstrument = instrumentList.get(index);
		
		//
		//
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
	public void onPageSelected(int position) {}
	
	@Override
	public void onPageScrollStateChanged(int state) {}
	
	private CarouselLinearLayout getRootView(int position)
	{
		Fragment frag = fm.findFragmentByTag(this.getFragmentTag(position));
		
		View view = frag.getView();
		
		CarouselLinearLayout carouselLinearLayout = 
				(CarouselLinearLayout) view.findViewById(R.id.root);
		
		return carouselLinearLayout;
	}
	
	private String getFragmentTag(int position)
	{
	    return "android:switcher:" + context.carouselPager.getId() + ":" + position;
	}
}
