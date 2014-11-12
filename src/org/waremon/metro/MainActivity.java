package org.waremon.metro;

import android.app.Activity;
import android.app.AlertDialog;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.LogInCallback;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import org.waremon.metro.R;

public class MainActivity extends Activity implements LocationListener {

	ParseApplication app;
	Point point;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		app = (ParseApplication) this.getApplication();
		point = new Point();

		LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		String provider = mLocationManager.getBestProvider(criteria, true);
		mLocationManager.requestLocationUpdates(provider, 0, 0, this);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// ログイン周り
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser.getObjectId() == null) {
			Log.d("DEBUG", "no user");
			SharedPreferences accountInfo = getSharedPreferences("accountInfo",
					MODE_PRIVATE);
			if (accountInfo.getString("username", "") != "") {
				Log.d("DEBUG",
						"user data exist "
								+ accountInfo.getString("username", ""));
				ParseUser.logInInBackground(
						accountInfo.getString("username", ""),
						accountInfo.getString("password", ""),
						new LogInCallback() {
							public void done(ParseUser user, ParseException e) {
								if (user != null) {
									// Hooray! The user is logged in.
									setBaseLine();
								} else {
									// Signup failed. Look at the ParseException
									// to see what happened.
								}
							}
						});
			} else {
				Log.d("DEBUG", "user data not found");
				final String username = RandomStringUtils
						.randomAlphanumeric(32);
				final String password = RandomStringUtils
						.randomAlphanumeric(32);

				ParseUser user = new ParseUser();
				user.setUsername(username);
				user.setPassword(password);

				Log.d("DEBUG", "sign up new user");
				user.signUpInBackground(new SignUpCallback() {
					public void done(ParseException e) {
						if (e == null) {
							Log.d("DEBUG", "sign up succeeded");
							SharedPreferences accountInfo = getSharedPreferences(
									"accountInfo", MODE_PRIVATE);
							Editor editor = accountInfo.edit();
							editor.putString("username", username);
							editor.putString("password", password);
							editor.commit();
							setBaseLine();
						} else {
							Log.d("LOG_DEBUG", "error in sign in");
						}
					}
				});
			}
		} else {
			Log.d("DEBUG", "user already login id " + currentUser.getObjectId());
			setBaseLine();
		}
		
		// テスト
		ScrollView sv = (ScrollView)findViewById(R.id.delay_info_content);
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		for (int i = 0; i < 12; i++) {  
			Button button = new Button(this);  
		    button.setText("Button" + (i+1));  
		    linearLayout.addView(button);  
		}
		sv.addView(linearLayout);
	}

	public void setBaseLine() {
		Log.d("DEBUG", "setBaseLine");
		ParseUser currentUser = ParseUser.getCurrentUser();
		Log.d("DEBUG", "currentUser " + currentUser);
		Log.d("DEBUG", "objectId " + currentUser.getObjectId());
		ParseQuery<ParseObject> infoQuery = ParseQuery.getQuery("UserInfo");
		infoQuery.whereEqualTo("userObjectId", currentUser.getObjectId());
		infoQuery.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> userInfo, ParseException e) {
				if (e == null) {
					if (userInfo.size() == 0) {
						Intent intent = new Intent(MainActivity.this, ChooseBaseLine.class);
						startActivity(intent);
					} else {
						String baseLine = (String) userInfo.get(0).get("baseLine");
						app.myBaseLine = baseLine;
						ImageView iv = (ImageView) findViewById(R.id.line_mark);
						Log.d("DEBUG", baseLine);
						if (baseLine.equals("Chiyoda")) {
							iv.setImageDrawable(getResources().getDrawable(R.drawable.chiyoda));
						} else if (baseLine.equals("Fukutoshin")) {
							iv.setImageDrawable(getResources().getDrawable(R.drawable.fukutoshin));
						} else if (baseLine.equals("Ginza")) {
							iv.setImageDrawable(getResources().getDrawable(R.drawable.ginza));
						} else if (baseLine.equals("Hibiya")) {
							iv.setImageDrawable(getResources().getDrawable(R.drawable.hibiya));
						} else if (baseLine.equals("Marunouchi")) {
							iv.setImageDrawable(getResources().getDrawable(R.drawable.marunouchi));
						} else if (baseLine.equals("Nanboku")) {
							iv.setImageDrawable(getResources().getDrawable(R.drawable.nanboku));
						} else if (baseLine.equals("Tozai")) {
							iv.setImageDrawable(getResources().getDrawable(R.drawable.tozai));
						} else if (baseLine.equals("Yurakucho")) {
							iv.setImageDrawable(getResources().getDrawable(R.drawable.yurakucho));
						} else if (baseLine.equals("Hanzomon")) {
							iv.setImageDrawable(getResources().getDrawable(R.drawable.hanzomon));
						}
					}
				} else {
					Log.d("LOG_DEBUG", "Error: " + e.getMessage());
				}
			}
		});
	}

	public void onClickCheckIn(View v) {
		int addUserAttackPoint = 0;
		int addBaseLineHp = 0;
		
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View layout = inflater.inflate(R.layout.checkin_dialog, (ViewGroup) findViewById(R.id.checkin_dialog_layout));

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("CHECK IN STATION");
		alertDialogBuilder.setView(layout);

		if (app.currentStation.equals("")) {
			alertDialogBuilder
					.setMessage("メトロの駅が周辺にありません。100m以内になるとチェックインできます。");
		} else {
			Date currentDate = new Date();
			int diff = (int) ((currentDate.getTime() - app.lastCheckin.getTime()) / (60 * 1000));
			if (app.lastCheckin == null || app.lastStation != app.currentStation || diff >= app.sameStationInterval) {
				String msgStr = getString(R.string.checked_in_station, app.currentStation);
				msgStr += "\n";
				msgStr += "\n" + getString(R.string.checked_in_station_recover_hp, 30);
				msgStr += "\n" + getString(R.string.checked_in_station_op_point, 30);
				
				addUserAttackPoint += 30;
				addBaseLineHp += 30;
				
				app.lastCheckin = new Date();
				app.lastStation = app.currentStation;
				String baseLinePattern = app.myBaseLine + "[a-zA-z]*";
				for (int i = 0; i < app.currentLines.size(); i++) {
					Log.d("DEBUG", "currentLines " + app.currentLines.get(i));
					ImageView iv = null;
					if (i == 0) {
						iv = (ImageView) layout.findViewById(R.id.checkin_line0);
					} else if (i == 1) {
						iv = (ImageView) layout.findViewById(R.id.checkin_line1);
					} else if (i == 2) {
						iv = (ImageView) layout.findViewById(R.id.checkin_line2);
					} else if (i == 3) {
						iv = (ImageView) layout.findViewById(R.id.checkin_line3);
					}
					iv.setVisibility(View.VISIBLE);
					if (app.currentLines.get(i).equals("Chiyoda")) {
						iv.setImageDrawable(getResources().getDrawable(R.drawable.chiyoda));
					} else if (app.currentLines.get(i).equals("Fukutoshin")) {
						iv.setImageDrawable(getResources().getDrawable(R.drawable.fukutoshin));
					} else if (app.currentLines.get(i).equals("Ginza")) {
						iv.setImageDrawable(getResources().getDrawable(R.drawable.ginza));
					} else if (app.currentLines.get(i).equals("Hibiya")) {
						iv.setImageDrawable(getResources().getDrawable(R.drawable.hibiya));
					} else if (app.currentLines.get(i).equals("Marunouchi")) {
						iv.setImageDrawable(getResources().getDrawable(R.drawable.marunouchi));
					} else if (app.currentLines.get(i).equals("Nanboku")) {
						iv.setImageDrawable(getResources().getDrawable(R.drawable.nanboku));
					} else if (app.currentLines.get(i).equals("Tozai")) {
						iv.setImageDrawable(getResources().getDrawable(R.drawable.tozai));
					} else if (app.currentLines.get(i).equals("Yurakucho")) {
						iv.setImageDrawable(getResources().getDrawable(R.drawable.yurakucho));
					} else if (app.currentLines.get(i).equals("Hanzomon")) {
						iv.setImageDrawable(getResources().getDrawable(R.drawable.hanzomon));
					}

					if (app.currentLines.get(i).matches(baseLinePattern)) {
						msgStr += "\n\n自拠点ボーナス\n";
						msgStr += "\n" + getString(R.string.checked_in_station_recover_hp, 50);
						msgStr += "\n" + getString(R.string.checked_in_station_op_point, 50);
						addUserAttackPoint += 50;
						addBaseLineHp += 50;
					}
				}
				Log.d("DEBUG", "aaa");
				alertDialogBuilder.setMessage(msgStr);
				Log.d("DEBUG", "bbb");
				point.updateMyAttackPoint(addUserAttackPoint);
				point.updateBaseLineHp(addBaseLineHp, app.myBaseLine);
			} else {
				alertDialogBuilder.setMessage(diff + "分前にチェックインしています。同じ駅には" + String.valueOf(app.sameStationInterval) + "分間はチェックインできません。");
			}
		}

		alertDialogBuilder.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		alertDialogBuilder.setCancelable(true);
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.d("DEBUG", String.valueOf(location.getLatitude()));
		Log.d("DEBUG", String.valueOf(location.getLongitude()));

		final double currentLatitude = location.getLatitude();
		final double currentLongitude = location.getLongitude();

		ParseQuery<ParseObject> query = ParseQuery.getQuery("StationInfo");
		query.setLimit(1000);
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> stationList, ParseException e) {
				if (e == null) {
					if (stationList.size() == 0) {
						app.currentLine = "";
						app.currentStation = "";
						return;
					}
					double distance = 10000000;
					for (int i = 0; i < stationList.size(); i++) {
						ParseObject station = stationList.get(i);
						if (station.getString("station").equals("恵比寿")) {
							continue;
						}
						if (Math.abs(currentLatitude
								- station.getDouble("latitude")) < app.LATITUDE_DIFF) {
							if (Math.abs(currentLongitude
									- station.getDouble("longitude")) < app.LONGITUDE_DIFF) {
								Log.d("DEBUG", station.getString("station")
										+ " " + station.getString("line"));
								double stationDistance = Math.sqrt(Math.pow(
										currentLatitude
												- station.getDouble("latitude"),
										2)
										+ Math.pow(
												currentLongitude
														- station
																.getDouble("longitude"),
												2));
								Log.d("DEBUG", "stationDistance "
										+ stationDistance);
								if (stationDistance < distance) {
									distance = stationDistance;
									app.currentLine = station.getString("line");
									app.currentStation = station
											.getString("station");
								}
							}
						}
					}
					app.currentLines = new ArrayList<String>();
					for (int i = 0; i < stationList.size(); i++) {
						ParseObject station = stationList.get(i);
						if (app.currentStation.equals(station
								.getString("station"))) {
							app.currentLines.add(station.getString("line"));
						}
					}
					Log.d("DEBUG", "nearest station " + app.currentStation);
				} else {
					Log.d("score", "Error: " + e.getMessage());
				}
			}
		});
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
}
