package com.example.amelies2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
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

public class RewardActivity extends ActionBarActivity {
	Activity thisone;
	Firebase ref;
	public final static String UID = "com.example.amelies2.UID";
	String userID;
	int uPoints;
	int selected;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Remove title bar
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);

	    //Remove notification bar
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_reward);
		thisone = this;
		
		Intent intent = getIntent();
		userID  = intent.getStringExtra(MainActivity.UID);
		
		 Firebase.setAndroidContext(this);
		 ref = new Firebase("https://amelies.firebaseio.com/users/"+userID);
		 ref.addValueEventListener(new ValueEventListener() {
		    @Override
		    public void onDataChange(DataSnapshot snapshot) {
		    	uPoints = Integer.parseInt(snapshot.child("points").getValue().toString());
		    }
		    @Override
		    public void onCancelled(FirebaseError firebaseError) {
		        System.out.println("The read failed: " + firebaseError.getMessage());
		    }
		 });
		 
		 
		
		LinearLayout rewardList = (LinearLayout)findViewById(R.id.rewards);
		String[][] r={{"Free Side Salad", "5", "ic_launcher"},{"Free Iced Coffee", "5", "ic_launcher"},{"10% Off a Sandwich", "10", "ic_launcher"},{"Free Yogurt", "10","ic_launcher"},{"Free Breakfast Sandwich","20","ic_launcher"},{"A Date With Brooke","50","ic_launcher"}};
		
		String[] colors={"#2ecc71","#1abc9c","#f1c40f", "#e74c3c","#2c3e50", "#d35400","#f39c12", "#27ae60","#f1c40f","#3498db"};
		for (int i=0; i<r.length; i++){
			LinearLayout tmp = new LinearLayout(this);
			tmp.setOrientation(LinearLayout.HORIZONTAL);
			tmp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,400));
			tmp.setBackgroundColor(Color.parseColor(colors[(int)(Math.random()*10)]));
			tmp.setGravity(Gravity.RIGHT);
			tmp.setWeightSum(100);
			ImageView image = new ImageView(this);
			int ImageResource = getResources().getIdentifier("@drawable/"+r[i][2], null, getPackageName());
			Drawable Res = getResources().getDrawable(ImageResource);
			image.setImageDrawable(Res);
			image.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			TextView sometext = new TextView(this);
			TextView pointstext = new TextView(this);
			LinearLayout.LayoutParams temp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			temp.setMargins(10, 10, 10, 10);
			temp.weight = 37;
			LinearLayout.LayoutParams temp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			temp2.setMargins(10, 10, 10, 10);
			temp2.weight=25;
			pointstext.setGravity(Gravity.LEFT);
			sometext.setText(r[i][0]);
			sometext.setLayoutParams(temp);
			sometext.setTextColor(Color.parseColor("#FFFFFF"));
			pointstext.setLayoutParams(temp2);
			pointstext.setTextColor(Color.parseColor("#FFFFFF"));
			pointstext.setText(r[i][1]+"pt");
			pointstext.setTextSize(40);
			image.setLayoutParams(temp);
			
			pointstext.setOnClickListener(new OnClickListener(){
				
				@Override
				public void onClick(View point) {
					LinearLayout l = (LinearLayout) point.getParent();
					l.removeAllViews();
					l.setGravity(Gravity.CENTER);
					TextView code = new TextView(l.getContext());
					code.setTextSize(40);
					code.setText(Integer.parseInt(((TextView)point).getText().toString().split("pt")[0])+"pt: "+(int)(Math.random()*12435));
					code.setTextColor(Color.parseColor("#FFFFFF"));
					code.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View code) {
							uPoints-= Integer.parseInt(((TextView)code).getText().toString().split("pt")[0]);
				    		ref.child("points").setValue(uPoints);
				    		Toast.makeText(thisone, "You spent points!", Toast.LENGTH_LONG).show();
				    		Intent intent = new Intent(thisone, HomePageActivity.class);
					        intent.putExtra(UID, userID);
							startActivity(intent);
				    		
						}
						
					});
					
					l.addView(code);
				}
				
			});
			
			tmp.addView(pointstext);
			tmp.addView(sometext);
			tmp.addView(image);
			rewardList.addView(tmp);
		}
		
		
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reward, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_reward,
					container, false);
			return rootView;
		}
	}

}
