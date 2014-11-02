package com.example.amelies2;

import java.util.HashMap;
import java.util.Map;

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

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class MainActivity extends ActionBarActivity {
	EditText user;
	EditText pass;
	Button login;
	Button signup;
	Activity thisone;
	Firebase ref; 
	String userID;

	public final static String UID = "com.example.amelies2.UID";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Remove title bar
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);

	    //Remove notification bar
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);		
	    Firebase.setAndroidContext(this);
		thisone = this;
		ref = new Firebase("https://amelies.firebaseio.com");
		ref.addAuthStateListener(new Firebase.AuthStateListener() {
		    @Override
		    public void onAuthStateChanged(AuthData authData) {
		        if (authData != null) {
		        	Toast.makeText(thisone, "Logged In", Toast.LENGTH_LONG).show();
		        } else {
		        }
		    }
		});
		user = (EditText)findViewById(R.id.username);
		pass = (EditText)findViewById(R.id.password);
		login = (Button)findViewById(R.id.login);
		login.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				String u = user.getText().toString();
				String p = pass.getText().toString();
				ref.authWithPassword(u, p, new Firebase.AuthResultHandler() {
				    @Override
				    public void onAuthenticated(AuthData authData) {
				        
				        Intent intent = new Intent(thisone, HomePageActivity.class);
				        userID = authData.getUid();
				        intent.putExtra(UID, userID);
						startActivity(intent);
				    }

				    @Override
				    public void onAuthenticationError(FirebaseError firebaseError) {
			        	Toast.makeText(thisone, "Fuck\n" +firebaseError.toString(), Toast.LENGTH_LONG).show();

				    }
				});
				

			}			
			
		});
		signup = (Button)findViewById(R.id.signup);
		signup.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub				
				String u = user.getText().toString();
				String p = pass.getText().toString();
				ref.createUser(u, p, new Firebase.ResultHandler() {
				    @Override
				    public void onSuccess() {
			        	Toast.makeText(thisone, "User Created", Toast.LENGTH_LONG).show();
			        	
				    }

				    @Override
				    public void onError(FirebaseError firebaseError) {
			        	Toast.makeText(thisone, "User NOT Created\nPlease try again.", Toast.LENGTH_LONG).show();
				    }
				});
			}
			
		});
		
		
		

		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
