package org.waremon.metro;

import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.FindCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class Bet extends Activity {

	ParseApplication app;
	Intent intent;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bet);
		
		app = (ParseApplication) this.getApplication();
		
		intent = getIntent();
		
		Resources res = getResources();
		
		final TextView backLayout = (TextView)findViewById(R.id.RankingBack);
		backLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		int StatinNormalFontSize = 10;
		int StatinSmallFontSize = 8;
		
		// 千代田線
		LinearLayout linearLayout = (LinearLayout)findViewById(R.id.BetListChiyoda);
		LinearLayout innerLinearLayout1 = new LinearLayout(this);
		innerLinearLayout1.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout innerLinearLayout2 = new LinearLayout(this);
		innerLinearLayout2.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout innerLinearLayout3 = new LinearLayout(this);
		innerLinearLayout3.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout innerLinearLayout4 = new LinearLayout(this);
		innerLinearLayout4.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout innerLinearLayout5 = new LinearLayout(this);
		innerLinearLayout5.setOrientation(LinearLayout.HORIZONTAL);
		for (int i = 1; i <= 20; i++) {
			LinearLayout panelLayout = new LinearLayout(this);
			panelLayout.setOrientation(LinearLayout.VERTICAL);
			panelLayout.setPadding(4, 4, 4, 4);
			panelLayout.setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams panelParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			panelParams.weight = 1;
			panelLayout.setLayoutParams(panelParams);
			
			ImageView iv = new ImageView(this);
			TextView tx = new TextView(this);
			tx.setTextColor(Color.parseColor("#555555"));
			
			if (i <= 20) {
				int dId = res.getIdentifier("c"+i, "drawable", getPackageName());
				iv.setImageResource(dId);
				tx.setText(app.stationEnToJa.get(app.stationNumToEn.get("c"+i)));
			} else {
				int dId = res.getIdentifier("station_blank", "drawable", getPackageName());
				iv.setImageResource(dId);
				tx.setText("");
			}
			
			if (i == 7) {
				tx.setTextSize(StatinSmallFontSize);
			} else {
				tx.setTextSize(StatinNormalFontSize);
			}
			tx.setGravity(Gravity.CENTER);
			
			panelLayout.addView(iv);
			panelLayout.addView(tx);
			panelLayout.setClickable(true);
			panelLayout.setTag("c"+i);
			panelLayout.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Log.d("DEBUG", "touched !" + v.getTag());
					BetCustomDialog((String)v.getTag());
				}
				
			});
			
			if (i <= 5) {
				innerLinearLayout1.addView(panelLayout);
				if (i == 5) {
					linearLayout.addView(innerLinearLayout1);			
				}
			} else if (i <= 10) {
				innerLinearLayout2.addView(panelLayout);
				if (i == 10) {
					linearLayout.addView(innerLinearLayout2);
				}
			} else if (i <= 15) {
				innerLinearLayout3.addView(panelLayout);
				if (i == 15) {
					linearLayout.addView(innerLinearLayout3);
				}
			} else if (i <= 20) {
				innerLinearLayout4.addView(panelLayout);
				if (i == 20) {
					linearLayout.addView(innerLinearLayout4);
				}
			}
		}
		
		// 副都心線
		linearLayout = (LinearLayout)findViewById(R.id.BetListFukutoshin);
		innerLinearLayout1 = new LinearLayout(this);
		innerLinearLayout1.setOrientation(LinearLayout.HORIZONTAL);
		innerLinearLayout2 = new LinearLayout(this);
		innerLinearLayout2.setOrientation(LinearLayout.HORIZONTAL);
		innerLinearLayout3 = new LinearLayout(this);
		innerLinearLayout3.setOrientation(LinearLayout.HORIZONTAL);
		innerLinearLayout4 = new LinearLayout(this);
		innerLinearLayout4.setOrientation(LinearLayout.HORIZONTAL);
		for (int i = 1; i <= 20; i++) {
			LinearLayout panelLayout = new LinearLayout(this);
			panelLayout.setOrientation(LinearLayout.VERTICAL);
			panelLayout.setPadding(4, 4, 4, 4);
			panelLayout.setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams panelParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			panelParams.weight = 1;
			panelLayout.setLayoutParams(panelParams);
			
			ImageView iv = new ImageView(this);
			TextView tx = new TextView(this);
			tx.setTextColor(Color.parseColor("#555555"));
			
			if (i <= 16) {
				int dId = res.getIdentifier("f"+i, "drawable", getPackageName());
				iv.setImageResource(dId);
				tx.setText(app.stationEnToJa.get(app.stationNumToEn.get("f"+i)));
			} else {
				int dId = res.getIdentifier("station_blank", "drawable", getPackageName());
				iv.setImageResource(dId);
				tx.setText("");
			}
			
			tx.setTextSize(StatinNormalFontSize);
			tx.setGravity(Gravity.CENTER);
			
			panelLayout.addView(iv);
			panelLayout.addView(tx);
			panelLayout.setClickable(true);
			panelLayout.setTag("f"+i);
			panelLayout.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Log.d("DEBUG", "touched !" + v.getTag());
					BetCustomDialog((String)v.getTag());
				}
				
			});
			
			if (i <= 5) {
				innerLinearLayout1.addView(panelLayout);
				if (i == 5) {
					linearLayout.addView(innerLinearLayout1);			
				}
			} else if (i <= 10) {
				innerLinearLayout2.addView(panelLayout);
				if (i == 10) {
					linearLayout.addView(innerLinearLayout2);
				}
			} else if (i <= 15) {
				innerLinearLayout3.addView(panelLayout);
				if (i == 15) {
					linearLayout.addView(innerLinearLayout3);
				}
			} else if (i <= 20) {
				innerLinearLayout4.addView(panelLayout);
				if (i == 20) {
					linearLayout.addView(innerLinearLayout4);
				}
			}
		}
		
		// 銀座線
		linearLayout = (LinearLayout)findViewById(R.id.BetListGinza);
		innerLinearLayout1 = new LinearLayout(this);
		innerLinearLayout1.setOrientation(LinearLayout.HORIZONTAL);
		innerLinearLayout2 = new LinearLayout(this);
		innerLinearLayout2.setOrientation(LinearLayout.HORIZONTAL);
		innerLinearLayout3 = new LinearLayout(this);
		innerLinearLayout3.setOrientation(LinearLayout.HORIZONTAL);
		innerLinearLayout4 = new LinearLayout(this);
		innerLinearLayout4.setOrientation(LinearLayout.HORIZONTAL);
		for (int i = 1; i <= 20; i++) {
			LinearLayout panelLayout = new LinearLayout(this);
			panelLayout.setOrientation(LinearLayout.VERTICAL);
			panelLayout.setPadding(4, 4, 4, 4);
			panelLayout.setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams panelParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			panelParams.weight = 1;
			panelLayout.setLayoutParams(panelParams);
			
			ImageView iv = new ImageView(this);
			TextView tx = new TextView(this);
			tx.setTextColor(Color.parseColor("#555555"));
			
			if (i <= 19) {
				int dId = res.getIdentifier("g"+i, "drawable", getPackageName());
				iv.setImageResource(dId);
				tx.setText(app.stationEnToJa.get(app.stationNumToEn.get("g"+i)));
			} else {
				int dId = res.getIdentifier("station_blank", "drawable", getPackageName());
				iv.setImageResource(dId);
				tx.setText("");
			}
				
			tx.setTextSize(StatinNormalFontSize);
			tx.setGravity(Gravity.CENTER);
			
			panelLayout.addView(iv);
			panelLayout.addView(tx);
			panelLayout.setClickable(true);
			panelLayout.setTag("g"+i);
			panelLayout.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Log.d("DEBUG", "touched !" + v.getTag());
					BetCustomDialog((String)v.getTag());
				}
				
			});
			
			if (i <= 5) {
				innerLinearLayout1.addView(panelLayout);
				if (i == 5) {
					linearLayout.addView(innerLinearLayout1);			
				}
			} else if (i <= 10) {
				innerLinearLayout2.addView(panelLayout);
				if (i == 10) {
					linearLayout.addView(innerLinearLayout2);
				}
			} else if (i <= 15) {
				innerLinearLayout3.addView(panelLayout);
				if (i == 15) {
					linearLayout.addView(innerLinearLayout3);
				}
			} else if (i <= 20) {
				innerLinearLayout4.addView(panelLayout);
				if (i == 20) {
					linearLayout.addView(innerLinearLayout4);
				}
			}
		}
		
		// 日比谷線
		linearLayout = (LinearLayout)findViewById(R.id.BetListHibiya);
		innerLinearLayout1 = new LinearLayout(this);
		innerLinearLayout1.setOrientation(LinearLayout.HORIZONTAL);
		innerLinearLayout2 = new LinearLayout(this);
		innerLinearLayout2.setOrientation(LinearLayout.HORIZONTAL);
		innerLinearLayout3 = new LinearLayout(this);
		innerLinearLayout3.setOrientation(LinearLayout.HORIZONTAL);
		innerLinearLayout4 = new LinearLayout(this);
		innerLinearLayout4.setOrientation(LinearLayout.HORIZONTAL);
		innerLinearLayout5 = new LinearLayout(this);
		innerLinearLayout5.setOrientation(LinearLayout.HORIZONTAL);
		for (int i = 1; i <= 25; i++) {
			LinearLayout panelLayout = new LinearLayout(this);
			panelLayout.setOrientation(LinearLayout.VERTICAL);
			panelLayout.setPadding(4, 4, 4, 4);
			panelLayout.setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams panelParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			panelParams.weight = 1;
			panelLayout.setLayoutParams(panelParams);
			
			ImageView iv = new ImageView(this);
			TextView tx = new TextView(this);
			tx.setTextColor(Color.parseColor("#555555"));
			
			if (i <= 21) {
				int dId = res.getIdentifier("h"+i, "drawable", getPackageName());
				iv.setImageResource(dId);
				tx.setText(app.stationEnToJa.get(app.stationNumToEn.get("h"+i)));
			} else {
				int dId = res.getIdentifier("station_blank", "drawable", getPackageName());
				iv.setImageResource(dId);
				tx.setText("");
			}
			
			tx.setTextSize(StatinNormalFontSize);
			tx.setGravity(Gravity.CENTER);
			
			panelLayout.addView(iv);
			panelLayout.addView(tx);
			panelLayout.setClickable(true);
			panelLayout.setTag("h"+i);
			panelLayout.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Log.d("DEBUG", "touched !" + v.getTag());
					BetCustomDialog((String)v.getTag());
				}
				
			});
			
			if (i <= 5) {
				innerLinearLayout1.addView(panelLayout);
				if (i == 5) {
					linearLayout.addView(innerLinearLayout1);			
				}
			} else if (i <= 10) {
				innerLinearLayout2.addView(panelLayout);
				if (i == 10) {
					linearLayout.addView(innerLinearLayout2);
				}
			} else if (i <= 15) {
				innerLinearLayout3.addView(panelLayout);
				if (i == 15) {
					linearLayout.addView(innerLinearLayout3);
				}
			} else if (i <= 20) {
				innerLinearLayout4.addView(panelLayout);
				if (i == 20) {
					linearLayout.addView(innerLinearLayout4);
				}
			} else if (i <= 25) {
				innerLinearLayout5.addView(panelLayout);
				if (i == 25) {
					linearLayout.addView(innerLinearLayout5);
				}
			}
		}
		
		// 丸の内線
		linearLayout = (LinearLayout)findViewById(R.id.BetListMarunouchi);
		innerLinearLayout1 = new LinearLayout(this);
		innerLinearLayout1.setOrientation(LinearLayout.HORIZONTAL);
		innerLinearLayout2 = new LinearLayout(this);
		innerLinearLayout2.setOrientation(LinearLayout.HORIZONTAL);
		innerLinearLayout3 = new LinearLayout(this);
		innerLinearLayout3.setOrientation(LinearLayout.HORIZONTAL);
		innerLinearLayout4 = new LinearLayout(this);
		innerLinearLayout4.setOrientation(LinearLayout.HORIZONTAL);
		innerLinearLayout5 = new LinearLayout(this);
		innerLinearLayout5.setOrientation(LinearLayout.HORIZONTAL);
		for (int i = 1; i <= 25; i++) {
			LinearLayout panelLayout = new LinearLayout(this);
			panelLayout.setOrientation(LinearLayout.VERTICAL);
			panelLayout.setPadding(4, 4, 4, 4);
			panelLayout.setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams panelParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			panelParams.weight = 1;
			panelLayout.setLayoutParams(panelParams);
			
			ImageView iv = new ImageView(this);
			TextView tx = new TextView(this);
			tx.setTextColor(Color.parseColor("#555555"));
			
			if (i <= 25) {
				int dId = res.getIdentifier("m"+i, "drawable", getPackageName());
				iv.setImageResource(dId);
				tx.setText(app.stationEnToJa.get(app.stationNumToEn.get("m"+i)));
			} else {
				int dId = res.getIdentifier("station_blank", "drawable", getPackageName());
				iv.setImageResource(dId);
				tx.setText("");
			}
			
			if (i == 14) {
				tx.setTextSize(StatinSmallFontSize);
			} else {
				tx.setTextSize(StatinNormalFontSize);
			}
			tx.setGravity(Gravity.CENTER);
			
			panelLayout.addView(iv);
			panelLayout.addView(tx);
			panelLayout.setClickable(true);
			panelLayout.setTag("m"+i);
			panelLayout.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Log.d("DEBUG", "touched !" + v.getTag());
					BetCustomDialog((String)v.getTag());
				}
				
			});
			
			if (i <= 5) {
				innerLinearLayout1.addView(panelLayout);
				if (i == 5) {
					linearLayout.addView(innerLinearLayout1);			
				}
			} else if (i <= 10) {
				innerLinearLayout2.addView(panelLayout);
				if (i == 10) {
					linearLayout.addView(innerLinearLayout2);
				}
			} else if (i <= 15) {
				innerLinearLayout3.addView(panelLayout);
				if (i == 15) {
					linearLayout.addView(innerLinearLayout3);
				}
			} else if (i <= 20) {
				innerLinearLayout4.addView(panelLayout);
				if (i == 20) {
					linearLayout.addView(innerLinearLayout4);
				}
			} else if (i <= 25) {
				innerLinearLayout5.addView(panelLayout);
				if (i == 25) {
					linearLayout.addView(innerLinearLayout5);
				}
			}
		}
		
		// 南北線
		linearLayout = (LinearLayout)findViewById(R.id.BetListNanboku);
		innerLinearLayout1 = new LinearLayout(this);
		innerLinearLayout1.setOrientation(LinearLayout.HORIZONTAL);
		innerLinearLayout2 = new LinearLayout(this);
		innerLinearLayout2.setOrientation(LinearLayout.HORIZONTAL);
		innerLinearLayout3 = new LinearLayout(this);
		innerLinearLayout3.setOrientation(LinearLayout.HORIZONTAL);
		innerLinearLayout4 = new LinearLayout(this);
		innerLinearLayout4.setOrientation(LinearLayout.HORIZONTAL);
		for (int i = 1; i <= 20; i++) {
			LinearLayout panelLayout = new LinearLayout(this);
			panelLayout.setOrientation(LinearLayout.VERTICAL);
			panelLayout.setPadding(4, 4, 4, 4);
			panelLayout.setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams panelParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			panelParams.weight = 1;
			panelLayout.setLayoutParams(panelParams);
			
			ImageView iv = new ImageView(this);
			TextView tx = new TextView(this);
			tx.setTextColor(Color.parseColor("#555555"));
			
			if (i <= 19) {
				int dId = res.getIdentifier("n"+i, "drawable", getPackageName());
				iv.setImageResource(dId);
				tx.setText(app.stationEnToJa.get(app.stationNumToEn.get("n"+i)));
			} else {
				int dId = res.getIdentifier("station_blank", "drawable", getPackageName());
				iv.setImageResource(dId);
				tx.setText("");
			}
			
			if (i == 5) {
				tx.setTextSize(StatinSmallFontSize);
			} else {
				tx.setTextSize(StatinNormalFontSize);
			}
			tx.setGravity(Gravity.CENTER);
			
			panelLayout.addView(iv);
			panelLayout.addView(tx);
			panelLayout.setClickable(true);
			panelLayout.setTag("n"+i);
			panelLayout.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Log.d("DEBUG", "touched !" + v.getTag());
					BetCustomDialog((String)v.getTag());
				}
				
			});
			
			if (i <= 5) {
				innerLinearLayout1.addView(panelLayout);
				if (i == 5) {
					linearLayout.addView(innerLinearLayout1);			
				}
			} else if (i <= 10) {
				innerLinearLayout2.addView(panelLayout);
				if (i == 10) {
					linearLayout.addView(innerLinearLayout2);
				}
			} else if (i <= 15) {
				innerLinearLayout3.addView(panelLayout);
				if (i == 15) {
					linearLayout.addView(innerLinearLayout3);
				}
			} else if (i <= 20) {
				innerLinearLayout4.addView(panelLayout);
				if (i == 20) {
					linearLayout.addView(innerLinearLayout4);
				}
			}
		}
		
		// 東西線
		linearLayout = (LinearLayout)findViewById(R.id.BetListTozai);
		innerLinearLayout1 = new LinearLayout(this);
		innerLinearLayout1.setOrientation(LinearLayout.HORIZONTAL);
		innerLinearLayout2 = new LinearLayout(this);
		innerLinearLayout2.setOrientation(LinearLayout.HORIZONTAL);
		innerLinearLayout3 = new LinearLayout(this);
		innerLinearLayout3.setOrientation(LinearLayout.HORIZONTAL);
		innerLinearLayout4 = new LinearLayout(this);
		innerLinearLayout4.setOrientation(LinearLayout.HORIZONTAL);
		innerLinearLayout5 = new LinearLayout(this);
		innerLinearLayout5.setOrientation(LinearLayout.HORIZONTAL);
		for (int i = 1; i <= 25; i++) {
			LinearLayout panelLayout = new LinearLayout(this);
			panelLayout.setOrientation(LinearLayout.VERTICAL);
			panelLayout.setPadding(4, 4, 4, 4);
			panelLayout.setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams panelParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			panelParams.weight = 1;
			panelLayout.setLayoutParams(panelParams);
			
			ImageView iv = new ImageView(this);
			TextView tx = new TextView(this);
			tx.setTextColor(Color.parseColor("#555555"));
			
			if (i <= 23) {
				int dId = res.getIdentifier("t"+i, "drawable", getPackageName());
				iv.setImageResource(dId);
				tx.setText(app.stationEnToJa.get(app.stationNumToEn.get("t"+i)));
			} else {
				int dId = res.getIdentifier("station_blank", "drawable", getPackageName());
				iv.setImageResource(dId);
				tx.setText("");
			}
			
			tx.setTextSize(StatinNormalFontSize);
			tx.setGravity(Gravity.CENTER);
			
			panelLayout.addView(iv);
			panelLayout.addView(tx);
			panelLayout.setClickable(true);
			panelLayout.setTag("t"+i);
			panelLayout.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Log.d("DEBUG", "touched !" + v.getTag());
					BetCustomDialog((String)v.getTag());
				}
				
			});
			
			if (i <= 5) {
				innerLinearLayout1.addView(panelLayout);
				if (i == 5) {
					linearLayout.addView(innerLinearLayout1);			
				}
			} else if (i <= 10) {
				innerLinearLayout2.addView(panelLayout);
				if (i == 10) {
					linearLayout.addView(innerLinearLayout2);
				}
			} else if (i <= 15) {
				innerLinearLayout3.addView(panelLayout);
				if (i == 15) {
					linearLayout.addView(innerLinearLayout3);
				}
			} else if (i <= 20) {
				innerLinearLayout4.addView(panelLayout);
				if (i == 20) {
					linearLayout.addView(innerLinearLayout4);
				}
			} else if (i <= 25) {
				innerLinearLayout5.addView(panelLayout);
				if (i == 25) {
					linearLayout.addView(innerLinearLayout5);
				}
			}
		}
		
		// 有楽町
		linearLayout = (LinearLayout)findViewById(R.id.BetListYurakucho);
		innerLinearLayout1 = new LinearLayout(this);
		innerLinearLayout1.setOrientation(LinearLayout.HORIZONTAL);
		innerLinearLayout2 = new LinearLayout(this);
		innerLinearLayout2.setOrientation(LinearLayout.HORIZONTAL);
		innerLinearLayout3 = new LinearLayout(this);
		innerLinearLayout3.setOrientation(LinearLayout.HORIZONTAL);
		innerLinearLayout4 = new LinearLayout(this);
		innerLinearLayout4.setOrientation(LinearLayout.HORIZONTAL);
		innerLinearLayout5 = new LinearLayout(this);
		innerLinearLayout5.setOrientation(LinearLayout.HORIZONTAL);
		for (int i = 1; i <= 25; i++) {
			LinearLayout panelLayout = new LinearLayout(this);
			panelLayout.setOrientation(LinearLayout.VERTICAL);
			panelLayout.setPadding(4, 4, 4, 4);
			panelLayout.setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams panelParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			panelParams.weight = 1;
			panelLayout.setLayoutParams(panelParams);
			
			ImageView iv = new ImageView(this);
			TextView tx = new TextView(this);
			tx.setTextColor(Color.parseColor("#555555"));
			
			if (i <= 24) {
				int dId = res.getIdentifier("y"+i, "drawable", getPackageName());
				iv.setImageResource(dId);
				tx.setText(app.stationEnToJa.get(app.stationNumToEn.get("y"+i)));
			} else {
				int dId = res.getIdentifier("station_blank", "drawable", getPackageName());
				iv.setImageResource(dId);
				tx.setText("");
			}
			
			tx.setTextSize(StatinNormalFontSize);
			tx.setGravity(Gravity.CENTER);
			
			panelLayout.addView(iv);
			panelLayout.addView(tx);
			panelLayout.setClickable(true);
			panelLayout.setTag("y"+i);
			panelLayout.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Log.d("DEBUG", "touched !" + v.getTag());
					BetCustomDialog((String)v.getTag());
				}
				
			});
			
			if (i <= 5) {
				innerLinearLayout1.addView(panelLayout);
				if (i == 5) {
					linearLayout.addView(innerLinearLayout1);			
				}
			} else if (i <= 10) {
				innerLinearLayout2.addView(panelLayout);
				if (i == 10) {
					linearLayout.addView(innerLinearLayout2);
				}
			} else if (i <= 15) {
				innerLinearLayout3.addView(panelLayout);
				if (i == 15) {
					linearLayout.addView(innerLinearLayout3);
				}
			} else if (i <= 20) {
				innerLinearLayout4.addView(panelLayout);
				if (i == 20) {
					linearLayout.addView(innerLinearLayout4);
				}
			} else if (i <= 25) {
				innerLinearLayout5.addView(panelLayout);
				if (i == 25) {
					linearLayout.addView(innerLinearLayout5);
				}
			}
		}
		
		// 半蔵門
		linearLayout = (LinearLayout)findViewById(R.id.BetListHanzomon);
		innerLinearLayout1 = new LinearLayout(this);
		innerLinearLayout1.setOrientation(LinearLayout.HORIZONTAL);
		innerLinearLayout2 = new LinearLayout(this);
		innerLinearLayout2.setOrientation(LinearLayout.HORIZONTAL);
		innerLinearLayout3 = new LinearLayout(this);
		innerLinearLayout3.setOrientation(LinearLayout.HORIZONTAL);
		for (int i = 1; i <= 15; i++) {
			LinearLayout panelLayout = new LinearLayout(this);
			panelLayout.setOrientation(LinearLayout.VERTICAL);
			panelLayout.setPadding(4, 4, 4, 4);
			panelLayout.setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams panelParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			panelParams.weight = 1;
			panelLayout.setLayoutParams(panelParams);
			
			ImageView iv = new ImageView(this);
			TextView tx = new TextView(this);
			tx.setTextColor(Color.parseColor("#555555"));
			
			if (i <= 14) {
				int dId = res.getIdentifier("z"+i, "drawable", getPackageName());
				iv.setImageResource(dId);
				tx.setText(app.stationEnToJa.get(app.stationNumToEn.get("z"+i)));
			} else {
				int dId = res.getIdentifier("station_blank", "drawable", getPackageName());
				iv.setImageResource(dId);
				tx.setText("");
			}
			
			tx.setTextSize(StatinNormalFontSize);
			tx.setGravity(Gravity.CENTER);
			
			panelLayout.addView(iv);
			panelLayout.addView(tx);
			panelLayout.setClickable(true);
			panelLayout.setTag("z"+i);
			panelLayout.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Log.d("DEBUG", "touched !" + v.getTag());
					BetCustomDialog((String)v.getTag());
				}
				
			});
			
			if (i <= 5) {
				innerLinearLayout1.addView(panelLayout);
				if (i == 5) {
					linearLayout.addView(innerLinearLayout1);			
				}
			} else if (i <= 10) {
				innerLinearLayout2.addView(panelLayout);
				if (i == 10) {
					linearLayout.addView(innerLinearLayout2);
				}
			} else if (i <= 15) {
				innerLinearLayout3.addView(panelLayout);
				if (i == 15) {
					linearLayout.addView(innerLinearLayout3);
				}
			}
		}
	}
	
	public void BetCustomDialog(final String stationMark) {
		// カスタムビューを設定
	    LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
	    final View layout = inflater.inflate(R.layout.bet_dialog, (ViewGroup)findViewById(R.id.BetLayout));

	    Resources res = getResources();
	    
	    ImageView iv = (ImageView)layout.findViewById(R.id.BetLayoutImage);
		TextView tx = (TextView)layout.findViewById(R.id.BetLayoutString);
		
		int dId = res.getIdentifier(stationMark, "drawable", getPackageName());
		iv.setImageResource(dId);
		tx.setText(app.stationEnToJa.get(app.stationNumToEn.get(stationMark)));
		
		final EditText ed = (EditText)layout.findViewById(R.id.BetLayoutPoint);
		
		final TextView currentPointTextView = (TextView)layout.findViewById(R.id.BetLayoutPointCurrentPoint);
		SharedPreferences accountInfo = getSharedPreferences("accountInfo", MODE_PRIVATE);
		String currentPointMessage = "現在のポイント:" + accountInfo.getLong("myPoint", 0);
		currentPointTextView.setText(currentPointMessage);
		
	    // アラーとダイアログ を生成
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle("ベットポイントの決定");
	    builder.setView(layout);
	    builder.setPositiveButton("決定", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				SharedPreferences accountInfo = getSharedPreferences("accountInfo", MODE_PRIVATE);
				
				if (String.valueOf(ed.getText()).matches("")) {
					Log.d("DEBUG", "no input");
					openErrorDialog("ポイントが未入力です");
				} else if (Integer.parseInt(String.valueOf(ed.getText())) == 0) {
					Log.d("DEBUG", "input is 0");
					openErrorDialog("１以上を入力してください");
				} else if (accountInfo.getLong("myPoint", 999999999) < Integer.parseInt(String.valueOf(ed.getText()))) {
					Log.d("DEBUG", "input is bigger");
					openErrorDialog("現在のポイント以上には入力できません");
				} else {
					Log.d("DEBUG", String.valueOf(intent.getIntExtra("index", -1)) + " " + String.valueOf(ed.getText()));
					saveBet(stationMark, intent.getIntExtra("index", -1), Integer.parseInt(String.valueOf(ed.getText())));
				}
			}
	    	
	    });
	    
	    builder.setNegativeButton("戻る", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
	    	
	    });
	    
	    // 表示
	    builder.create().show();
	}
	
	public void openErrorDialog(String errorMessage) {
		// アラーとダイアログ を生成
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle("エラー");
	    builder.setMessage(errorMessage);
	    builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
	    });
	    
	    // 表示
	    builder.create().show();
	}
	
	public void saveBet(final String stationMark, final int betIndex, final int betPoint) {
		
		final ProgressDialog dlg = new ProgressDialog(this);
		dlg.setIndeterminate(true);
		dlg.setMessage("データ保存中");
		dlg.show();
		
		final ParseUser currentUser = ParseUser.getCurrentUser();
		
		ParseObject betHistory = new ParseObject("BetHistory");
		betHistory.put("stationMark", stationMark);
		betHistory.put("betIndex", betIndex);
		betHistory.put("betPoint", betPoint);
		betHistory.put("currentPoint", betPoint);
		betHistory.put("userObjectId", currentUser.getObjectId());
		betHistory.saveInBackground(new SaveCallback(){
			@Override
			public void done(ParseException e) {
				if (e == null) {
					// betの保存に成功したら accountInfo更新のmyPointとbet情報更新
					// ParseUser更新
					SharedPreferences accountInfo = getSharedPreferences("accountInfo", MODE_PRIVATE);
					Editor editor = accountInfo.edit();
					final long currentPoint = accountInfo.getLong("myPoint", 0) - betPoint;
					editor.putLong("myPoint", currentPoint);
					editor.putString("betStation" + betIndex, stationMark);
					editor.putLong("betPoint" + betIndex, betPoint);
					editor.commit();
					
					ParseQuery<ParseObject> infoQuery = ParseQuery.getQuery("UserInfo");
					infoQuery.whereEqualTo("userObjectId", currentUser.getObjectId());
					infoQuery.findInBackground(new FindCallback<ParseObject>() {

						@Override
						public void done(List<ParseObject> objects, ParseException e) {
							if (e == null) {
								ParseObject object = objects.get(0);
								object.put("myPoint", currentPoint);
								object.saveInBackground(new SaveCallback(){
									@Override
									public void done(ParseException e) {
										dlg.hide();
										finish();
									}
								});
							}
						}
					});
				} else {
					// TODO
					dlg.hide();
				}
			}
		});
	}
}
