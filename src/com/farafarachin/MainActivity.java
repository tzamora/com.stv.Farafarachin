package com.farafarachin;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.farafarachin.carousel.CarouselPagerAdapter;

public class MainActivity extends FragmentActivity 
{
	//
	// config info for the carousel
	//
	
	public final static int PAGES = 10;
	
	// You can choose a bigger number for LOOPS, but you know, nobody will fling
	// more than 1000 times just in order to test your "infinite" ViewPager :D 
	public final static int LOOPS = 1000; 
	
	public final static int FIRST_PAGE = PAGES * LOOPS / 2;
	
	public final static float BIG_SCALE = 1.0f;
	
	public final static float SMALL_SCALE = 0.7f;
	
	public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;
	
	//
	//
	//
	
	public CarouselPagerAdapter adapter;
	
	public ViewPager pager;
	
	//
	//
	//
	
	private ShakeListener shaker;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        final Vibrator vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        shaker = new ShakeListener(this);
        
        shaker.setOnShakeListener(new ShakeListener.OnShakeListener () {
          public void onShake()
          {
            vibe.vibrate(100);
            
            //final MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.redoblante);
            
            //mp.start();
            
            new AlertDialog.Builder(MainActivity.this)
              .setPositiveButton(android.R.string.ok, null)
              .setMessage("Shooken!")
              .show();
          }
        });
        
        //
        //
        //
        
        final Button button = (Button) findViewById(R.id.goToCamera);
        
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
            	
            	Intent intent = new Intent(MainActivity.this, TakePhotoActivity.class);
            	
            	startActivity(intent);
            	
            	// Canvas c = new Canvas();
            	
            	// c.drawBitmap();
            	
            }
        });
        
        addCarousel();
    }
	
	private void addCarousel()
	{
		pager = (ViewPager) findViewById(R.id.carouselViewPager);

		adapter = new CarouselPagerAdapter(this, this.getSupportFragmentManager());
		
		pager.setAdapter(adapter);
		
		pager.setOnPageChangeListener(adapter);
		
		
		// Set current item to the middle page so we can fling to both
		// directions left and right
		pager.setCurrentItem(FIRST_PAGE);
		
		// Necessary or the pager will only have one extra page to show
		// make this at least however many pages you can see
		pager.setOffscreenPageLimit(3);
		
		// Set margin for pages as a negative number, so a part of next and 
		// previous pages will be showed
		pager.setPageMargin(-200);
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
