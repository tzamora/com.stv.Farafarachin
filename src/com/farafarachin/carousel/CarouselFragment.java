package com.farafarachin.carousel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farafarachin.MainActivity;
import com.farafarachin.R;

public class CarouselFragment extends Fragment {
	
	public static Fragment newInstance(MainActivity context, int pos, 
			float scale)
	{
		Bundle b = new Bundle();
		b.putInt("pos", pos);
		b.putFloat("scale", scale);
		return Fragment.instantiate(context, CarouselFragment.class.getName(), b);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		
		LinearLayout l = (LinearLayout) 
				inflater.inflate(R.layout.mf, container, false);
		
		int pos = this.getArguments().getInt("pos");
		
		TextView tv = (TextView) l.findViewById(R.id.text);
		
		//ImageView imageView = (imageView) l.findViewById(R.id.instrumentImage);
		
		tv.setText("Position = " + pos);
		
		CarouselLinearLayout root = (CarouselLinearLayout) l.findViewById(R.id.root);
		float scale = this.getArguments().getFloat("scale");
		root.setScaleBoth(scale);
		
		return l;
	}
}
