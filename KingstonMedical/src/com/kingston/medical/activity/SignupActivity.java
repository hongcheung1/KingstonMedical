package com.kingston.medical.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.kingston.medical.R;
import com.kingston.medical.util.APIManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class SignupActivity extends Activity implements OnClickListener {

	TextView txt_email, txt_password, txt_login, txt_register, txt_forgot;
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		
		context = this;
		
		txt_email = (TextView) findViewById(R.id.txt_email);
		txt_email.setBackgroundResource(android.R.color.transparent);
		
		txt_password = (TextView) findViewById(R.id.txt_password);
		txt_password.setBackgroundResource(android.R.color.transparent);

		txt_login = (TextView) findViewById(R.id.txt_login);
		txt_login.setOnClickListener(this);
		
		txt_register = (TextView) findViewById(R.id.txt_register);
		txt_register.setOnClickListener(this);
		
		txt_forgot = (TextView) findViewById(R.id.txt_forgot);
		txt_forgot.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		Intent intent = null;
		
		switch(v.getId()) {
			case R.id.txt_login:
				finish();
				break;
			case R.id.txt_register:
				if(isConfirm()) {
					new LoadSignupTask().execute(txt_email.getText().toString(), txt_password.getText().toString());
				}
				break;
			case R.id.txt_forgot:
				
				break;
		}
	}
	
	private boolean isConfirm() {

		if(txt_email.length() == 0) {
			Toast.makeText(this, "Please enter email",
		              Toast.LENGTH_SHORT).show();
			txt_email.requestFocus();
			return false;
		}
		if(txt_password.length() == 0) {
			Toast.makeText(this, "Please enter password",
		              Toast.LENGTH_SHORT).show();
			txt_password.requestFocus();
			return false;
		}

		return true;
	}
	
	class LoadSignupTask extends AsyncTask<String, Integer, String> {
        private ProgressDialog progressDialog;
        String value = "";
        
        protected void onPreExecute() {
        	progressDialog = ProgressDialog.show(context, "", "Login...", true);
        }
        
        @Override
        protected void onPostExecute(String result) {
    		
            progressDialog.dismiss();
            
            if(!result.equals("")) {
				Toast.makeText(context, result,
			              Toast.LENGTH_SHORT).show();
            }
        }
 
        @Override
        protected String doInBackground(String... param) {
        	
        	List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("email", param[0]));
	        params.add(new BasicNameValuePair("password", param[1]));
	        
        	JSONObject result = null;
       		result =  APIManager.getInstance().callPost(context, "user", params, false);
        	
        	try {
        		
        		if(result.getString("status").equals("200")) {
        			
        			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
	                SharedPreferences.Editor e = prefs.edit();
	                
	                e.putString("uid", result.getString("uid"));
	                e.putString("sid", result.getString("sid"));
	                
	                JSONObject data = result.getJSONObject("data");
	                e.putString("mail", data.getString("mail"));
	                e.putString("status", data.getString("status"));
	                e.putString("name", data.getString("name"));
	                e.putString("user_birthdate", data.getString("user_birthdate"));
	                e.putString("language", data.getString("language"));

	                e.commit();

        		} else {
        			value = result.getString("message");
        		}
        	} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

            return value;
        }
    }
}
