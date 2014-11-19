package org.waremon.metro;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class Ranking extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ranking);
		
		final ParseUser currentUser = ParseUser.getCurrentUser();
		
		final LinearLayout topLayout = (LinearLayout)findViewById(R.id.TopContentLayout);
		
		final LinearLayout scrollLayout = (LinearLayout)findViewById(R.id.CenterContentLayoutList);
		
		final ProgressDialog dlg = new ProgressDialog(this);
		dlg.setIndeterminate(true);
		dlg.setMessage("ランキング取得中");
		dlg.show();
		
		ParseQuery<ParseObject> infoQuery = ParseQuery.getQuery("UserInfo");
		infoQuery.orderByDescending("myPoint");
		infoQuery.setLimit(100);
		infoQuery.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					long rankingIndex = 1;
					for (ParseObject object : objects) {
						if (currentUser.getObjectId().matches(object.getString("userObjectId"))) {
							LinearLayout topInnerLayout = getRankingCell(rankingIndex, object.getString("nickname"), object.getLong("myPoint"), "");
							topInnerLayout.setGravity(Gravity.CENTER);
							LinearLayout.LayoutParams topParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
							topInnerLayout.setLayoutParams(topParams);
							topLayout.addView(topInnerLayout);
						}
						LinearLayout rankingLayout = getRankingCell(rankingIndex, object.getString("nickname"), object.getLong("myPoint"), object.getString("userObjectId"));
						scrollLayout.addView(rankingLayout);
				    	rankingIndex++;
					}
				}
				dlg.hide();
			}			
		});
		
		final TextView backLayout = (TextView)findViewById(R.id.RankingBack);
		backLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	public LinearLayout getRankingCell(Long rankingIndex, String nickname, Long myPoint, String userObjectId) {
		
		final ParseUser currentUser = ParseUser.getCurrentUser();
		
		LinearLayout rankingLayout = new LinearLayout(getApplicationContext());
		rankingLayout.setOrientation(LinearLayout.HORIZONTAL);
		
	    // index設置
		TextView indexTx = new TextView(getApplicationContext());
	    indexTx.setText(String.valueOf(rankingIndex));
		LinearLayout.LayoutParams indexParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
		indexParams.weight = 1;
		indexTx.setLayoutParams(indexParams);
		indexTx.setGravity(Gravity.CENTER);
		indexTx.setTextSize(20);
		if (currentUser.getObjectId().matches(userObjectId)) {
			indexTx.setTextColor(Color.parseColor("#FF5555"));
		} else {
			indexTx.setTextColor(Color.parseColor("#555555"));
		}
		rankingLayout.addView(indexTx);
		
	    // nickname設置
		TextView nicknameTx = new TextView(getApplicationContext());
		nicknameTx.setText(nickname);
		LinearLayout.LayoutParams nicknameParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
		nicknameParams.weight = 2;
		nicknameTx.setLayoutParams(nicknameParams);
		nicknameTx.setGravity(Gravity.CENTER);
		nicknameTx.setTextSize(20);
		if (currentUser.getObjectId().matches(userObjectId)) {
			nicknameTx.setTextColor(Color.parseColor("#FF5555"));
		} else {
			nicknameTx.setTextColor(Color.parseColor("#555555"));
		}
		rankingLayout.addView(nicknameTx);
		
	    // point設置
		TextView pointTx = new TextView(getApplicationContext());
		pointTx.setText(String.valueOf(myPoint));
		LinearLayout.LayoutParams pointParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
		pointParams.weight = 2;
		pointTx.setLayoutParams(pointParams);
		pointTx.setGravity(Gravity.CENTER);
		pointTx.setTextSize(20);
		if (currentUser.getObjectId().matches(userObjectId)) {
			pointTx.setTextColor(Color.parseColor("#FF5555"));
		} else {
			pointTx.setTextColor(Color.parseColor("#555555"));
		}
		rankingLayout.addView(pointTx);
		return rankingLayout;
	}
}
