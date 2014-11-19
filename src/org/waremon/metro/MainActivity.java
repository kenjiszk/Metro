package org.waremon.metro;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomStringUtils;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQuery.CachePolicy;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.LogInCallback;
import com.parse.CountCallback;
import com.parse.DeleteCallback;
import com.parse.SaveCallback;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import org.waremon.metro.R;

public class MainActivity extends Activity {

	ParseApplication app;
	Timer delayInfoTimer = new Timer();
	DelayInfoTimerTask delayInfoTimerTask;
	Handler mHandler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		app = (ParseApplication) this.getApplication();
	}

	@Override
	protected void onResume() {
		super.onResume();

		// ログイン周り
		ParseUser currentUser = ParseUser.getCurrentUser();
		// 未ログイン
		if (currentUser.getObjectId() == null) {
			Log.d("DEBUG", "no user");
			SharedPreferences accountInfo = getSharedPreferences("accountInfo", MODE_PRIVATE);
			// 会員登録済み
			if (accountInfo.getString("username", "") != "") {
				Log.d("DEBUG", "user data exist " + accountInfo.getString("username", ""));
				ParseUser.logInInBackground(accountInfo.getString("username", ""), accountInfo.getString("password", ""), new LogInCallback() {
					public void done(ParseUser user, ParseException e) {
						if (user != null) {
						} else {
						}
					}
				});
			} else {
				Intent intent = new Intent(MainActivity.this, Intro.class);
				startActivity(intent);
			}
		} else {
			Log.d("DEBUG", "user already login id " + currentUser.getObjectId());
			redrawDetInfo();

			delayInfoTimer = new Timer();
			delayInfoTimerTask = new DelayInfoTimerTask();
			delayInfoTimer.schedule(delayInfoTimerTask, 0, 30000);

			// ランキングリスナー
			ImageView checkRanking = (ImageView) findViewById(R.id.CheckRanking);
			checkRanking.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MainActivity.this, Ranking.class);
					startActivity(intent);
				}

			});
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		delayInfoTimer.cancel();
	}

	public void redrawDetInfo() {
		LinearLayout betLinearLayoutContainer = (LinearLayout) findViewById(R.id.BetContentLayoutScrollViewInnerLayout);
		betLinearLayoutContainer.removeAllViews();

		SharedPreferences accountInfo = getSharedPreferences("accountInfo", MODE_PRIVATE);

		TextView myPointTextView = (TextView) findViewById(R.id.point_label_value);
		myPointTextView.setText(String.valueOf(accountInfo.getLong("myPoint", 0)));

		int numOfBlank = 0;

		for (int i = 1; i <= 10; i++) {
			long currentPoint = accountInfo.getLong("currentPoint" + i, 0);
			String station = accountInfo.getString("betStation" + i, "");

			LinearLayout betLinearLayout = new LinearLayout(this);
			betLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
			betLinearLayout.setTag(i);

			Resources res = getResources();

			// index設置
			TextView indexTx = new TextView(this);
			indexTx.setText(String.valueOf(i));
			LinearLayout.LayoutParams indexParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
			indexParams.weight = 1;
			indexTx.setLayoutParams(indexParams);
			indexTx.setGravity(Gravity.CENTER);
			indexTx.setTextSize(20);
			indexTx.setTextColor(Color.parseColor("#555555"));
			betLinearLayout.addView(indexTx);

			ImageView iv = new ImageView(this);
			iv.setLayoutParams(indexParams);
			TextView tx = new TextView(this);
			tx.setLayoutParams(indexParams);
			tx.setTextColor(Color.parseColor("#555555"));

			int dId;
			if (station.matches("")) {
				dId = res.getIdentifier("station_blank", "drawable", getPackageName());
				tx.setText("ベットしましょう");
				numOfBlank++;

				betLinearLayout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Log.d("DEBUG", "touched ! " + v.getTag());
						Intent intent = new Intent(MainActivity.this, Bet.class);
						intent.putExtra("index", Integer.parseInt(v.getTag().toString()));
						startActivity(intent);
					}

				});
			} else {
				if (currentPoint == -1) {
					dId = res.getIdentifier(station, "drawable", getPackageName());
					tx.setText(app.stationEnToJa.get(app.stationNumToEn.get(station)) + "\n" + "遅延発生 R:0");

					betLinearLayout.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Log.d("DEBUG", "touched ! " + v.getTag());
							showBetResultDialog(Integer.parseInt(v.getTag().toString()));
						}
					});

				} else {
					dId = res.getIdentifier(station, "drawable", getPackageName());
					tx.setText(app.stationEnToJa.get(app.stationNumToEn.get(station)) + "\n" + "B:" + accountInfo.getLong("betPoint" + i, 0) + " / " + "R:" + accountInfo.getLong("currentPoint" + i, 0));

					betLinearLayout.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Log.d("DEBUG", "touched ! " + v.getTag());
							showBetResultDialog(Integer.parseInt(v.getTag().toString()));
						}
					});
				}
			}

			iv.setImageResource(dId);
			LinearLayout.LayoutParams ivParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
			ivParams.weight = 1;
			iv.setPadding(0, 5, 0, 5);
			iv.setLayoutParams(ivParams);
			betLinearLayout.addView(iv);

			LinearLayout.LayoutParams txParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
			txParams.weight = 5;
			tx.setLayoutParams(txParams);
			tx.setGravity(Gravity.CENTER);
			tx.setTextSize(20);
			betLinearLayout.addView(tx);

			betLinearLayoutContainer.addView(betLinearLayout);
		}

		if (numOfBlank == 10) {
			Intent intent = new Intent(MainActivity.this, Bet.class);
			intent.putExtra("index", 1);
			startActivity(intent);
		}
	}

	public void showBetResultDialog(final int betIndex) {
		final SharedPreferences accountInfo = getSharedPreferences("accountInfo", MODE_PRIVATE);
		String station = accountInfo.getString("betStation" + betIndex, "");
		long currentPoint = accountInfo.getLong("currentPoint" + betIndex, 0);

		// カスタムビューを設定
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View layout = inflater.inflate(R.layout.bet_result, (ViewGroup) findViewById(R.id.BetResultLayout));

		Resources res = getResources();

		ImageView iv = (ImageView) layout.findViewById(R.id.BetResultLayoutImage);
		TextView tx = (TextView) layout.findViewById(R.id.BetResultLayoutString);

		int dId = res.getIdentifier(station, "drawable", getPackageName());
		iv.setImageResource(dId);
		tx.setText(app.stationEnToJa.get(app.stationNumToEn.get(station)));

		TextView tv1 = (TextView) layout.findViewById(R.id.BetResultLayoutBet);
		TextView tv2 = (TextView) layout.findViewById(R.id.BetResultLayoutReturn);
		if (currentPoint != -1) {
			tv1.setText("ベット：" + accountInfo.getLong("betPoint" + betIndex, 0));
			tv2.setText("リターン：" + accountInfo.getLong("currentPoint" + betIndex, 0));

			// アラーとダイアログ を生成
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("ポイント確認");
			builder.setView(layout);
			builder.setPositiveButton("ポイント確定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					fixReturn(betIndex);
				}

			});

			builder.setNegativeButton("戻る", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

				}

			});

			// 表示
			builder.create().show();

		} else {
			tv1.setText("ベット : " + accountInfo.getLong("betPoint" + betIndex, 0));
			tv2.setText("リターン : 遅延発生のため０");

			final ParseUser currentUser = ParseUser.getCurrentUser();

			final ProgressDialog dlg = new ProgressDialog(this);
			dlg.setIndeterminate(true);
			dlg.setMessage("データ保存中");
			dlg.show();

			// BetHistory消す
			ParseQuery<ParseObject> betQuery = ParseQuery.getQuery("BetHistory");
			betQuery.whereEqualTo("userObjectId", currentUser.getObjectId());
			betQuery.whereEqualTo("betIndex", betIndex);
			betQuery.findInBackground(new FindCallback<ParseObject>() {

				@Override
				public void done(List<ParseObject> objects, ParseException e) {
					if (e == null) {
						ParseObject object = objects.get(0);
						object.deleteInBackground(new DeleteCallback() {
							@Override
							public void done(ParseException e) {
								if (e == null) {
									Editor editor = accountInfo.edit();
									editor.putLong("betPoint" + betIndex, 0);
									editor.putLong("currentPoint" + betIndex, 0);
									editor.putString("betStation" + betIndex, "");
									editor.commit();

									redrawDetInfo();
								}
								dlg.hide();
							}
						});
					}
					dlg.hide();
				}
			});

			redrawDetInfo();

			// アラーとダイアログ を生成
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("ポイント確認");
			builder.setView(layout);
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});

			// 表示
			builder.create().show();
		}
	}

	public void fixReturn(final int betIndex) {
		final ProgressDialog dlg = new ProgressDialog(this);
		dlg.setIndeterminate(true);
		dlg.setMessage("データ保存中");
		dlg.show();

		final SharedPreferences accountInfo = getSharedPreferences("accountInfo", MODE_PRIVATE);
		final long currentPoint = accountInfo.getLong("currentPoint" + betIndex, 0);
		final long myPoint = accountInfo.getLong("myPoint", 0);

		final ParseUser currentUser = ParseUser.getCurrentUser();

		// Point追加
		ParseQuery<ParseObject> infoQuery = ParseQuery.getQuery("UserInfo");
		infoQuery.whereEqualTo("userObjectId", currentUser.getObjectId());
		infoQuery.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					ParseObject object = objects.get(0);
					object.put("myPoint", myPoint + currentPoint);
					object.saveInBackground();
				}
			}
		});

		// BetHistory消す
		ParseQuery<ParseObject> betQuery = ParseQuery.getQuery("BetHistory");
		betQuery.whereEqualTo("userObjectId", currentUser.getObjectId());
		betQuery.whereEqualTo("betIndex", betIndex);
		betQuery.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					ParseObject object = objects.get(0);
					object.deleteInBackground(new DeleteCallback() {
						@Override
						public void done(ParseException e) {
							if (e == null) {
								Editor editor = accountInfo.edit();
								editor.putLong("myPoint", myPoint + currentPoint);
								editor.putLong("betPoint" + betIndex, 0);
								editor.putLong("currentPoint" + betIndex, 0);
								editor.putString("betStation" + betIndex, "");
								editor.commit();

								redrawDetInfo();
							}
							dlg.hide();
						}
					});
				}
				dlg.hide();
			}
		});
	}

	public class DelayInfoTimerTask extends TimerTask {
		@Override
		public void run() {
			Log.d("DEBUG", "Start delayInfoTimerTask");
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					Log.d("DEBUG", "run!");

					ParseUser currentUser = ParseUser.getCurrentUser();

					// point更新
					ParseQuery<ParseObject> infoQuery = ParseQuery.getQuery("UserInfo");
					infoQuery.whereEqualTo("userObjectId", currentUser.getObjectId());
					infoQuery.findInBackground(new FindCallback<ParseObject>() {
						@Override
						public void done(List<ParseObject> objects, ParseException e) {
							if (e == null) {
								if (objects.size() != 0) {
									ParseObject userInfo = objects.get(0);
									long myPoint = userInfo.getLong("myPoint");
									TextView myPointTextView = (TextView) findViewById(R.id.point_label_value);
									myPointTextView.setText(String.valueOf(myPoint));

									SharedPreferences accountInfo = getSharedPreferences("accountInfo", MODE_PRIVATE);
									Editor editor = accountInfo.edit();
									editor.putLong("myPoint", myPoint);
									editor.commit();
								}
							}
						}
					});

					setRanking(currentUser.getObjectId());

					// betInfo更新
					ParseQuery<ParseObject> historyQuery = ParseQuery.getQuery("BetHistory");
					historyQuery.whereEqualTo("userObjectId", currentUser.getObjectId());
					historyQuery.setLimit(1000);
					historyQuery.findInBackground(new FindCallback<ParseObject>() {
						@Override
						public void done(List<ParseObject> objects, ParseException e) {
							if (e == null) {
								for (ParseObject betHistory : objects) {
									long betIndex = betHistory.getLong("betIndex");
									long betPoint = betHistory.getLong("betPoint");
									long currentPoint = betHistory.getLong("currentPoint");
									String stationMark = betHistory.getString("stationMark");

									SharedPreferences accountInfo = getSharedPreferences("accountInfo", MODE_PRIVATE);
									Editor editor = accountInfo.edit();
									editor.putString("betStation" + betIndex, stationMark);
									editor.putLong("betPoint" + betIndex, betPoint);
									editor.putLong("currentPoint" + betIndex, currentPoint);
									editor.commit();

									redrawDetInfo();
								}
							}
						}
					});

					// delayInfo更新
					final ScrollView sv = (ScrollView) findViewById(R.id.DelayContentLayoutScrollView);
					final LinearLayout linearLayout = new LinearLayout(getApplicationContext());
					linearLayout.setPadding(20, 20, 20, 20);
					linearLayout.setOrientation(LinearLayout.VERTICAL);

					ParseQuery<ParseObject> logQuery = ParseQuery.getQuery("TrainLog");
					logQuery.orderByDescending("createdAt");
					logQuery.setLimit(100);
					logQuery.findInBackground(new FindCallback<ParseObject>() {
						public void done(List<ParseObject> logs, ParseException e) {
							if (e == null) {
								sv.removeAllViews();
								if (logs.size() == 0) {
								} else {
									int validCount = 0;
									for (int i = 0; i < logs.size(); i++) {
										ParseObject log = logs.get(i);

										Time time = new Time("Asia/Tokyo");
										time.setToNow();
										String currentTime = String.format("%1$04d", time.year) + String.format("%1$02d", time.month + 1) + String.format("%1$02d", time.monthDay) + String.format("%1$02d", time.hour) + String.format("%1$02d", time.minute) + String.format("%1$02d", time.second);

										String regex = "(\\d\\d\\d\\d)-(\\d\\d)-(\\d\\d)T(\\d\\d):(\\d\\d):(\\d\\d)\\+09:00";
										Pattern p = Pattern.compile(regex);
										Matcher m = p.matcher((CharSequence) log.get("valid"));

										String validTime;

										if (m.find()) {
											validTime = m.group(1) + m.group(2) + m.group(3) + m.group(4) + m.group(5) + m.group(6);
										} else {
											continue;
										}

										if (Long.parseLong(currentTime) > Long.parseLong(validTime)) {
											continue;
										}
										
										// date取得
										String delayDate = "";
										Matcher m2 = p.matcher((CharSequence) log.get("date"));
										if (m2.find()) {
											delayDate = m.group(1) +'/'+ m.group(2) +'/'+ m.group(3) +' '+ m.group(4) +':'+ m.group(5) +':'+ m.group(6);
										} else {
											continue;
										}

										String fromStation = log.get("fromStation").toString();
										String[] fromStationArray = fromStation.split("\\.", 0);

										int min = (Integer) log.get("delay") / 60;

										LinearLayout innerLayout = new LinearLayout(getApplicationContext());
										innerLayout.setOrientation(LinearLayout.VERTICAL);
										innerLayout.setPadding(0, 0, 0, 15);

										TextView timeView = new TextView(getApplicationContext());
										timeView.setTextSize(15);
										timeView.setText((CharSequence) delayDate);
										timeView.setTextColor(Color.parseColor("#555555"));
										innerLayout.addView(timeView);

										TextView delayView = new TextView(getApplicationContext());
										delayView.setTextSize(20);
										// 2:線 3:駅
										delayView.setText(app.lineEnToJa.get(fromStationArray[2]) + " " + app.stationEnToJa.get(fromStationArray[3]) + " " + String.valueOf(min) + "分遅延");
										delayView.setTextColor(Color.parseColor("#555555"));
										innerLayout.addView(delayView);

										linearLayout.addView(innerLayout);
										validCount++;
									}
									if (validCount == 0) {
										linearLayout.setGravity(Gravity.CENTER);
										TextView tv = new TextView(getApplicationContext());
										tv.setText("現在 遅延は発生していません");
										tv.setGravity(Gravity.CENTER);
										tv.setTextSize(20);
										tv.setTextColor(Color.parseColor("#555555"));
										linearLayout.addView(tv);
										
										Button btn = new Button(getApplicationContext());
										btn.setText("いいね！");
										btn.setGravity(Gravity.CENTER);
										btn.setTextSize(20);
										btn.setTextColor(Color.parseColor("#FFFFFF"));
										btn.setBackgroundColor(Color.parseColor("#BBBBBB"));
										LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
										btn.setLayoutParams(btnParams);
										btn.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												openIineDialog();
												// Point追加
												ParseUser currentUser = ParseUser.getCurrentUser();
												ParseQuery<ParseObject> infoQuery = ParseQuery.getQuery("UserInfo");
												infoQuery.whereEqualTo("userObjectId", currentUser.getObjectId());
												infoQuery.findInBackground(new FindCallback<ParseObject>() {

													@Override
													public void done(List<ParseObject> objects, ParseException e) {
														if (e == null) {
															ParseObject object = objects.get(0);
															long currentPoint = object.getLong("myPoint") + 1;
															object.put("myPoint", currentPoint);
															object.saveInBackground();
															
															SharedPreferences accountInfo = getSharedPreferences("accountInfo", MODE_PRIVATE);
															Editor editor = accountInfo.edit();
															editor.putLong("myPoint", currentPoint);
															editor.commit();
															
															TextView tv = (TextView)findViewById(R.id.point_label_value);
															tv.setText(String.valueOf(currentPoint));
														}
													}
												});
												
												// 良いね追加
												ParseQuery<ParseObject> goodQuery = ParseQuery.getQuery("GoodCount");
												goodQuery.setCachePolicy(CachePolicy.NETWORK_ONLY);
												goodQuery.setLimit(1);
												goodQuery.findInBackground(new FindCallback<ParseObject>() {
													@Override
													public void done(List<ParseObject> objects, ParseException e) {
														if (e == null) {
															Log.d("DEBUG", String.valueOf(objects.size()));
															long currentPoint = objects.get(0).getLong("count") + 1;
															objects.get(0).put("count", currentPoint);
															objects.get(0).saveInBackground();
															
															SharedPreferences accountInfo = getSharedPreferences("accountInfo", MODE_PRIVATE);
															Editor editor = accountInfo.edit();
															editor.putLong("iine", currentPoint);
															editor.commit();
															
															TextView tv = (TextView)findViewById(1);
															tv.setText("現在 " + accountInfo.getLong("iine", 0) + " いいね！\n(facebookに投稿するものではありません)");
														}
													}
												});
											}
										});
										linearLayout.addView(btn);
										
										SharedPreferences accountInfo = getSharedPreferences("accountInfo", MODE_PRIVATE);
										TextView subTv = new TextView(getApplicationContext());
										subTv.setId(1);
										subTv.setText("現在 " + accountInfo.getLong("iine", 0) + " いいね！\n(facebookに投稿するものではありません)");
										subTv.setGravity(Gravity.CENTER);
										subTv.setTextSize(13);
										subTv.setTextColor(Color.parseColor("#CCCCCC"));
										linearLayout.addView(subTv);
									}
									sv.addView(linearLayout);
								}
							} else {
								Log.d("LOG_DEBUG", "Error: " + e.getMessage());
							}
						}
					});
				}
			});
		}
	}

	public void setRanking(final String userObjectId) {
		ParseQuery<ParseObject> infoQuery = ParseQuery.getQuery("UserInfo");
		infoQuery.whereEqualTo("userObjectId", userObjectId);
		infoQuery.setCachePolicy(CachePolicy.NETWORK_ONLY);
		infoQuery.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					countRankingRecursive(objects.get(0).getLong("myPoint"), 1000, 0);
				}
			}
		});
	}

	public void countRankingRecursive(final long myPoint, final int limit, final int skip) {
		ParseQuery<ParseObject> infoQuery = ParseQuery.getQuery("UserInfo");
		infoQuery.whereGreaterThan("myPoint", myPoint);
		infoQuery.setLimit(limit);
		infoQuery.setSkip(skip);
		infoQuery.setCachePolicy(CachePolicy.NETWORK_ONLY);
		infoQuery.countInBackground(new CountCallback() {

			@Override
			public void done(int count, ParseException e) {
				if (count == limit) {
					countRankingRecursive(myPoint, limit, skip + limit);
				} else {
					TextView rankingTextView = (TextView) findViewById(R.id.ranking_label_value);
					rankingTextView.setText(String.valueOf(skip + count + 1));
				}
			}

		});
	}
	
	public void openIineDialog() {
		// アラーとダイアログ を生成
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle("いいねポイント");
	    builder.setMessage("いいねありがとうございます\n一回につき１ポイント贈呈します！");
	    builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
	    });
	    // 表示
	    builder.create().show();
	}
}
