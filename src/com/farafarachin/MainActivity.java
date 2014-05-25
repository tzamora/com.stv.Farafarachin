package com.farafarachin;


import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.farafarachin.carousel.CarouselPagerAdapter;
import com.farafarachin.carousel.SoundModePagerAdapter;
import com.farafarachin.vo.InstrumentVO;

public class MainActivity extends FragmentActivity 
{
	//
	// config info for the carousel
	//
	
	public final static int INSTRUMENT_PAGES = 3;
	
	public final static int MODE_PAGES = 3;
	
	// You can choose a bigger number for LOOPS, but you know, nobody will fling
	// more than 1000 times just in order to test your "infinite" ViewPager :D 
	public final static int LOOPS = 1000; 
	
	public final static int FIRST_PAGE = INSTRUMENT_PAGES * LOOPS / 2;
	
	public final static float BIG_SCALE = 1.0f;
	
	public final static float SMALL_SCALE = 0.7f;
	
	public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;
	
	//
	//
	//
	
	public CarouselPagerAdapter carouselPageAdapter;
	
	public SoundModePagerAdapter soundModePageAdapter;
	
	public ViewPager carouselPager;
	
	public ViewPager soundModePager;
	
	//
	//
	//
	
	private ShakeListener shaker;
	
	//
	//
	//
	
	private MediaPlayer _mp;
	
	//
	// keep the selected instrument and mode
	//
	
	public int selectedMode = 0;
	
	public InstrumentVO selectedInstrument;
	
	private Vibrator vibe;
	
	private ScheduledExecutorService exec;
	
	private ScheduledFuture<?> finishMelodySchedule;
	
	private ScheduledFuture<?> finishFarachinSongSchedule;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        //
        //
        //
        
        exec = Executors.newScheduledThreadPool(1);
        
        //
        //
        //
        
        vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        
        shaker = new ShakeListener(this);
        
        shaker.setOnShakeListener(new ShakeListener.OnShakeListener () {
          public void onShake()
          {
            playInstrument();
          }
        });
        
        //
        //
        //
        
        initInstrumentCarousel();
        
        //
        //
        //
        
        initSoundModeCarousel();
        
        //
        // by default set the selected mode in "sound"
        //
        
        selectedMode = 0;
    }
	
	private void playInstrument()
	{	
		vibe.vibrate(100);
        
		switch(selectedMode)
		{
			case 0:
				playSound();
				break;
			case 1:
				toggleMelody();
				break;
			case 2:
				playFarachin();
				break;
		}
	}
	
	private void playResource(int resource) {
	    if (_mp != null) 
	    {
	        
	    	if (_mp.isPlaying())
	        {
	    		_mp.stop();
	        }
	        
	    	_mp.reset();
	        
	    	//from MediaPlayer implementation (link above)
	        try {
	            AssetFileDescriptor afd = getResources().openRawResourceFd(resource);
	            
	            if (afd == null) return;
	            
	            _mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
	            
	            afd.close();
	            
	            _mp.prepare();
	        } 
	        catch (IOException ex) 
	        {
	            Log.d("", "create failed:", ex);
	            // failed: return
	        } catch (IllegalArgumentException ex) {
	            Log.d("", "create failed:", ex);
	            // failed: return
	        } catch (SecurityException ex) {
	            Log.d("", "create failed:", ex);
	            // failed: return
	        }
	    }
	    else {
	        //player is null
	        //it will create new MediaPlayer instance, setDataSource and call prepare
	    	_mp = MediaPlayer.create(this, resource);
	    }
	    //if everything ok play file
	    //in case of any error return from method before (catch)
	    _mp.start();
	}
	
	private void playFarachin() {
		
		//
		// when playing melody, stop only if:
		// 1. has passed half a second since the vibration stopped
		// 2. the mode has been changed
		//
		
		if(finishFarachinSongSchedule == null || finishFarachinSongSchedule.isDone())
		{
			playResource(R.raw.farafarachin);
		}
		else
		{
			finishFarachinSongSchedule.cancel(true);
		}
		
		finishFarachinSongSchedule = exec.schedule(new Runnable(){
		    @Override
		    public void run(){
		    	
		    	// stop any sound
		    	
		        _mp.stop();
		    }
		}, 1300, TimeUnit.MILLISECONDS);
		
	}

	private void toggleMelody() {
		
		//
		// when playing melody, stop only if:
		// 1. has passed half a second since the vibration stopped
		// 2. the mode has been changed
		//
		
		if(finishMelodySchedule == null || finishMelodySchedule.isDone())
		{
			playResource(selectedInstrument.melodyResID);
		}
		else
		{
			finishMelodySchedule.cancel(true);
		}
		
		finishMelodySchedule = exec.schedule(new Runnable(){
		    @Override
		    public void run(){
		    	
		    	// stop any sound
		    	
		        _mp.stop();
		    }
		}, 1300, TimeUnit.MILLISECONDS);
		
	}

	private void playSound() {
        if(selectedInstrument!=null)
        {
        	playResource(selectedInstrument.soundResID);	
        }
		
	}

	private void initInstrumentCarousel()
	{
		carouselPager = (ViewPager) findViewById(R.id.carouselViewPager);

		carouselPageAdapter = new CarouselPagerAdapter(this, this.getSupportFragmentManager());
		
		carouselPager.setAdapter(carouselPageAdapter);
		
		carouselPager.setOnPageChangeListener(carouselPageAdapter);
		
		// Set current item to the middle page so we can fling to both
		// directions left and right
		carouselPager.setCurrentItem(FIRST_PAGE);
		
		// Necessary or the pager will only have one extra page to show
		// make this at least however many pages you can see
		carouselPager.setOffscreenPageLimit(3);
		
		// Set margin for pages as a negative number, so a part of next and 
		// previous pages will be showed
		carouselPager.setPageMargin(-200);
	}
	
	private void initSoundModeCarousel()
	{
		soundModePager = (ViewPager) findViewById(R.id.soundModeViewPager);

		soundModePageAdapter = new SoundModePagerAdapter(this, this.getSupportFragmentManager());
		
		soundModePager.setAdapter(soundModePageAdapter);
		
		soundModePager.setOnPageChangeListener(soundModePageAdapter);
		
		// Set current item to the middle page so we can fling to both
		// directions left and right
		soundModePager.setCurrentItem(FIRST_PAGE);
		
		// Necessary or the pager will only have one extra page to show
		// make this at least however many pages you can see
		soundModePager.setOffscreenPageLimit(3);
		
		// Set margin for pages as a negative number, so a part of next and 
		// previous pages will be showed
		soundModePager.setPageMargin(-200);
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.gotoCameraButton:
                Intent intent = new Intent(MainActivity.this, TakePhotoActivity.class);
            	startActivity(intent);
                return true;
            case R.id.gotoRatings:
            	new AlertDialog.Builder(MainActivity.this)
                .setPositiveButton(android.R.string.ok, null)
                .setMessage("GO TO RATINGS")
                .show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

	public void selectMode(int index) {
		
		selectedMode = index;
		
		Log.i("@##@#@", "index " + selectedMode );
		
		if(_mp != null && _mp.isPlaying())
		{
			//
			// each time the mode is changed stop the music
			//
			
			_mp.stop();
		}
	}
    
}
