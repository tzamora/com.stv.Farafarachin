package com.farafarachin.carousel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farafarachin.MainActivity;
import com.farafarachin.R; 
import com.farafarachin.vo.InstrumentVO;

public class CarouselFragment extends Fragment {
	
	private InstrumentVO _instrumentVO;
	
	public static Fragment newInstance(MainActivity context, int pos, InstrumentVO instrumentVO,
			float scale)
	{
		Bundle b = new Bundle();
		
		b.putInt("pos", pos);
		
		b.putFloat("scale", scale);
		
		CarouselFragment carouselFragment = (CarouselFragment)Fragment.instantiate(context, CarouselFragment.class.getName(), b); 
		
		carouselFragment.setInstrumentData(instrumentVO);
		
		return carouselFragment;
	}
	
	private void setInstrumentData(InstrumentVO instrumentVO) {
		
		_instrumentVO = instrumentVO;
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		
		LinearLayout l = (LinearLayout) 
				inflater.inflate(R.layout.instrument_fragment, container, false);
		
		int pos = this.getArguments().getInt("pos");
		
		TextView tv = (TextView) l.findViewById(R.id.text);
		
		tv.setText(_instrumentVO.name);
		
		//
		//
		//
		
		ImageView imageView = (ImageView) l.findViewById(R.id.instrumentImage);
		
		imageView.setImageResource(_instrumentVO.imageResID);
		
		//
		// change the image based on the selected instrument 
		//
		
		CarouselLinearLayout root = (CarouselLinearLayout) l.findViewById(R.id.root);
		
		float scale = this.getArguments().getFloat("scale");
		
		root.setScaleBoth(scale);
		
		return l;
	}
}
