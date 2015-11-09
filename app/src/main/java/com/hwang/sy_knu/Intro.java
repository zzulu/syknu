package com.hwang.sy_knu;

import java.util.Observable;
import java.util.Observer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Toast;

import com.hwang.sy_knu.Data.HttpClient;
import com.hwang.sy_knu.Data.Data;

public class Intro extends Activity implements Observer {

	final private String GUBUN_YEAR_TERM="1";
	final private String GUBUN_SEASON_TERM="2";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sy_knu_intro);
		
		
		HttpClient getYearTerm = new HttpClient(HttpClient.HTTP_GET_TERM, "http://yes.knu.ac.kr/cour/cour/course/listLectPln/list.action?search_gubun="+GUBUN_YEAR_TERM+"&search_subj_sub_class_cde='CLTR101001'");
		getYearTerm.addObserver(this);
		getYearTerm.connect();
		
		HttpClient getSeasonTerm = new HttpClient(HttpClient.HTTP_GET_SEASON, "http://yes.knu.ac.kr/cour/cour/course/listLectPln/list.action?search_gubun="+GUBUN_SEASON_TERM+"&search_subj_sub_class_cde='CLTR101001'");
		getSeasonTerm.addObserver(this);
		getSeasonTerm.connect();

		Handler hd = new Handler();
		
		hd.postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent goSyMain = new Intent(Intro.this,Main.class);
				startActivity(goSyMain);
				finish();
			}
		}, 1000);
		
		
	}

	
	private void parseHtml(String str){
		
		Document doc = Jsoup.parse(str);
		Elements rows = doc.select("table.courTable tbody tr a");
		for (Element row : rows) {
			if(str.substring(0, 1).equals(GUBUN_YEAR_TERM)){
				Data.yearTerm=row.attr("href").substring(row.attr("href").indexOf("searchOpenYrTrm='")+17,row.attr("href").indexOf("searchOpenYrTrm='")+22);
			} else if (str.substring(0, 1).equals(GUBUN_SEASON_TERM)) {
				Data.seasonTerm=row.attr("href").substring(row.attr("href").indexOf("searchOpenYrTrm='")+17,row.attr("href").indexOf("searchOpenYrTrm='")+22);
			} else {
				Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	
	@Override
	public void update(Observable observable, Object data) {
		parseHtml((String)data);
	}

}