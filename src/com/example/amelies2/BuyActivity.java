package com.example.amelies2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class BuyActivity extends ActionBarActivity {
	
	Activity thisone;
	Button buyButton;
	String userID;
	public final static String UID = "com.example.amelies2.UID";
	Firebase ref;
	int uPoints;
	String code;
	EditText codeIn; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Remove title bar
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);

	    //Remove notification bar
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_buy);
		
		Intent intent = getIntent();
		userID  = intent.getStringExtra(MainActivity.UID);
		
		Firebase.setAndroidContext(this);
		ref = new Firebase("https://amelies.firebaseio.com/");
		ref.addValueEventListener(new ValueEventListener() {
		    @Override
		    public void onDataChange(DataSnapshot snapshot) {
		    	uPoints = Integer.parseInt(snapshot.child("users/"+userID+"/points").getValue().toString());
		    	code = snapshot.child("code").getValue().toString();
		    }
		    @Override
		    public void onCancelled(FirebaseError firebaseError) {
		        System.out.println("The read failed: " + firebaseError.getMessage());
		    }
		});
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		thisone = this;
		
		codeIn = (EditText)findViewById(R.id.code);
		
		buyButton = (Button)findViewById(R.id.buyButton);
		buyButton.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View arg0) {
				if (codeIn.getText().toString().equals(code)){
					uPoints++;
					ref.child("users/"+userID+"/points").setValue(uPoints+"");
					
					Intent intent = new Intent(thisone, HomePageActivity.class);
			        intent.putExtra(UID, userID);
					startActivity(intent);
				}else{
					Toast.makeText(thisone, "Wrong Code", Toast.LENGTH_SHORT).show();
				}
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.buy, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_buy, container,
					false);
			return rootView;
		}
	}

}
