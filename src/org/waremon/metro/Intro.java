package org.waremon.metro;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.parse.ParseQuery.CachePolicy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Intro extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro);
	}
	
	public void onClickNicknameButton(View v) {
		Log.d("DEBUG", "onClickNicknameButton");
		
		EditText ed = (EditText)findViewById(R.id.NicknameEditText);
		final String nickname = ed.getText().toString();
		
		if (nickname.matches("")) {
			Log.d("DEBUG", "no nickname");
		} else {
			Log.d("DEBUG", "nickname set to " + nickname);
			
			final ProgressDialog dlg = new ProgressDialog(this);
			dlg.setIndeterminate(true);
			dlg.setMessage("データ保存中");
			dlg.show();
			
			final String username = RandomStringUtils.randomAlphanumeric(32);
			final String password = RandomStringUtils.randomAlphanumeric(32);

			final ParseUser user = new ParseUser();
			user.setUsername(username);
			user.setPassword(password);

			Log.d("DEBUG", "sign up new user");
			user.signUpInBackground(new SignUpCallback() {
				public void done(ParseException e) {
					if (e == null) {
						Log.d("DEBUG", "sign up succeeded");
						
						// 初期ポイント1000pt
						ParseObject userInfo = new ParseObject("UserInfo");
						userInfo.put("myPoint", 1000);
						userInfo.put("nickname", nickname);
						userInfo.put("userObjectId", user.getObjectId());
						userInfo.saveEventually();
						
						SharedPreferences accountInfo = getSharedPreferences("accountInfo", MODE_PRIVATE);
						Editor editor = accountInfo.edit();
						editor.putString("username", username);
						editor.putString("password", password);
						editor.putString("nickname", nickname);
						editor.putLong("myPoint", 1000);
						editor.putLong("iine", 0);
						
						ParseQuery<ParseObject> goodQuery = ParseQuery.getQuery("GoodCount");
						goodQuery.setCachePolicy(CachePolicy.NETWORK_ONLY);
						goodQuery.setLimit(1);
						goodQuery.findInBackground(new FindCallback<ParseObject>() {
							@Override
							public void done(List<ParseObject> objects, ParseException e) {
								if (e == null) {
									SharedPreferences accountInfo = getSharedPreferences("accountInfo", MODE_PRIVATE);
									Editor editor = accountInfo.edit();
									editor.putLong("iine", objects.get(0).getLong("count"));
									editor.commit();
								}
							}
						});
						
						// betポイント初期化
						for (int i = 1; i <= 10; i++) {
							editor.putString("betStation" + i, "");
							editor.putLong("betPoint" + i, 0);
						}
						editor.commit();
						dlg.hide();
						finish();
					} else {
						dlg.hide();
						Log.d("LOG_DEBUG", "error in sign in");
						// TODO show alert
					}
				}
			});
		}
	}
}
