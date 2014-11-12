package org.waremon.metro;

import java.util.List;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class Point {
	
	public void updateMyAttackPoint(final int point) {
		Log.d("DEBUG", "point " + point);
		ParseUser currentUser = ParseUser.getCurrentUser();
		ParseQuery<ParseObject> infoQuery = ParseQuery.getQuery("UserInfo");
		infoQuery.whereEqualTo("userObjectId", currentUser.getObjectId());
		infoQuery.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					Log.d("DEBUG", "aaaaa " + objects.get(0));
					ParseObject object = objects.get(0);
					int newPoint = (Integer) object.get("attackPoint") + point;
					object.put("attackPoint", newPoint);
					object.saveInBackground();
				}
			}
		});
	}
	
	public void updateBaseLineHp(final int point, final String lineName) {
		Log.d("DEBUG", "point " + point + " lineName " + lineName);
		ParseQuery<ParseObject> infoQuery = ParseQuery.getQuery("LineMaster");
		infoQuery.whereEqualTo("lineName", lineName);
		infoQuery.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					Log.d("DEBUG", "aaaaa " + objects.get(0));
					ParseObject object = objects.get(0);
					int newPoint = (Integer) object.get("hp") + point;
					object.put("hp", newPoint);
					object.saveInBackground();
				}
			}
		});
	}
}
