package com.example.amelies2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class HomePageActivity extends ActionBarActivity {
	Activity thisone;
	TextView reward;
	TextView points;
	TextView buy;
	String userID;
	Firebase ref;
	int uPoints;
	public final static String UID = "com.example.amelies2.UID";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Remove title bar
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);

	    //Remove notification bar
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_home_page);
		uPoints = 0;

		Intent intent = getIntent();
		userID  = intent.getStringExtra(MainActivity.UID);
	    Firebase.setAndroidContext(this);
		ref = new Firebase("https://amelies.firebaseio.com/users/"+userID);

		points = (TextView)findViewById(R.id.pointsdoe);

		ref.addValueEventListener(new ValueEventListener() {
		    @Override
		    public void onDataChange(DataSnapshot snapshot) {
		    	try{
		    		uPoints = Integer.parseInt(snapshot.child("points").getValue().toString());
		    	}catch(NullPointerException e){
		    		//Toast.makeText(thisone, e.toString(), Toast.LENGTH_LONG).show();
		    		ref.child("points").setValue("0");
		    		uPoints = 0;
		    	}
		    	//Toast.makeText(thisone, uPoints+"", Toast.LENGTH_LONG).show();
		    	points.setText(uPoints+"");
		    }
		    @Override
		    public void onCancelled(FirebaseError firebaseError) {
		        System.out.println("The read failed: " + firebaseError.getMessage());
		    }
		});
		
		reward = (TextView)findViewById(R.id.reward);
		buy = (TextView)findViewById(R.id.plus);
		//points.setText(uPoints);
		thisone = this;
		reward.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(thisone, RewardActivity.class);
		        intent.putExtra(UID, userID);
				startActivity(intent);
				
			}
			
		});
		buy.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(thisone, BuyActivity.class);
		        intent.putExtra(UID, userID);
				startActivity(intent);
				
			}
			
		});
		String[] pics = {"brownie","coffee","mouse","pie","tart","tart2"};
		
		LinearLayout mealList = (LinearLayout)findViewById(R.id.mealList);
		
		for (int i=1; i<50; i++){
			LinearLayout tmp = new LinearLayout(this);
			tmp.setOrientation(LinearLayout.HORIZONTAL);
			ImageView image = new ImageView(this);
			int p = (int)(Math.random()*6);
			Log.e("THIS APP", p+"");
			int ImageResource = getResources().getIdentifier("@drawable/"+pics[p], null, getPackageName());
			Drawable Res = getResources().getDrawable(ImageResource);
			image.setImageDrawable(Res);
			image.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			TextView sometext = new TextView(this);
			LinearLayout.LayoutParams temp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			temp.setMargins(10, 10, 10, 10);
			sometext.setText(pics[p] +" doe.");
			sometext.setLayoutParams(temp);
			sometext.setTextColor(Color.parseColor("#FFFFFF"));
			image.setLayoutParams(temp);
			tmp.addView(image);
			tmp.addView(sometext);
			mealList.addView(tmp);
			//tmp.removeAllViews();
		}
			
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_page, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_home_page,
					container, false);
			return rootView;
		}
	}

}
