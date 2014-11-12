package org.waremon.metro;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ChooseBaseLine extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_base_line);
	}
	
	// ”÷–­
	public void onClickGinza(View v) {
		setBaseLine("Ginza");
	}
	
	public void onClickMarunouchi(View v) {
		setBaseLine("Marunouchi");
	}
	
	public void onClickHibiya(View v) {
		setBaseLine("Hibiya");
	}
	
	public void onClickTozai(View v) {
		setBaseLine("Tozai");
	}
	
	public void onClickChiyoda(View v) {
		setBaseLine("Chiyoda");
	}
	
	public void onClickYurakucho(View v) {
		setBaseLine("Yurakucho");
	}
	
	public void onClickHanzomon(View v) {
		setBaseLine("Hanzomon");
	}
	
	public void onClickNanboku(View v) {
		setBaseLine("Nanboku");
	}
	
	public void onClickHukutoshin(View v) {
		setBaseLine("Hukutoshin");
	}
	
	public void setBaseLine(String lineName) {
		ParseUser currentUser = ParseUser.getCurrentUser();
		ParseObject userInfo = new ParseObject("UserInfo");
		Log.d("DEBUG", "objectId2" + currentUser.getObjectId());
		Log.d("DEBUG", lineName);
		userInfo.put("userObjectId", currentUser.getObjectId());
		userInfo.put("baseLine", lineName);
		userInfo.put("attackPoint", 0);
		userInfo.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				finish();
			}
		});
	}
}
