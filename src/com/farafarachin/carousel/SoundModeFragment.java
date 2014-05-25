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

public class SoundModeFragment extends Fragment {
	
	public static Fragment newInstance(MainActivity context, int pos, String mode,
			float scale)
	{
		Bundle b = new Bundle();
		
		b.putInt("pos", pos);
		
		b.putFloat("scale", scale);
		
		b.putString("mode", mode);
		
		SoundModeFragment soundModeFragment = (SoundModeFragment)Fragment.instantiate(context, SoundModeFragment.class.getName(), b); 
		
		return soundModeFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		
		LinearLayout l = (LinearLayout) 
				inflater.inflate(R.layout.sound_mode_fragment, container, false);
		
		String mode = this.getArguments().getString("mode");
		
		TextView tv = (TextView) l.findViewById(R.id.text);
		
		tv.setText(mode);
		
		//
		// change the image based on the selected instrument 
		//
		
		CarouselLinearLayout root = (CarouselLinearLayout) l.findViewById(R.id.root);
		
		float scale = this.getArguments().getFloat("scale");
		
		root.setScaleBoth(scale);
		
		return l;
	}
}
