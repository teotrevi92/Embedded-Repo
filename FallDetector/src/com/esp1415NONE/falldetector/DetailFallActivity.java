package com.esp1415NONE.falldetector;

import com.esp1415NONE.falldetector.classi.DbAdapter;
import com.esp1415NONE.falldetector.classi.MyGraph;
import com.esp1415NONE.falldetector.classi.Queue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailFallActivity extends Activity {

	private DbAdapter dbAdapter;
	private TextView name_S,id_s,id_f,date_f,lat_,longit_;
	private String ids,idf,nameS,datef,lat,longit,dateS,array;
	private Intent i;
	private ImageView logo,graph;
	private float[] acc;
	private Queue que;
	
	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);
		setContentView(R.layout.activity_detail_fall);
		
		i = getIntent();
		ids = i.getStringExtra("ids");
		nameS = i.getStringExtra("nomeS");
		dateS = i.getStringExtra("dataS");
		lat = i.getStringExtra("lat");
		longit = i.getStringExtra("long");
		idf = i.getStringExtra("idf");
		datef = i.getStringExtra("dataf");
		array = i.getStringExtra("array");

		

		name_S = (TextView) findViewById(R.id.nameS);
		id_s = (TextView) findViewById(R.id.ids);
		id_f = (TextView) findViewById(R.id.idfall);
		date_f = (TextView) findViewById(R.id.datef);
		lat_ = (TextView) findViewById(R.id.lat);
		longit_ = (TextView) findViewById(R.id.longit);
		logo = (ImageView) findViewById(R.id.imageS);
		graph = (ImageView) findViewById(R.id.graph); 

		name_S.setText(nameS);
		id_s.setText(ids);
		id_f.setText(idf);
		date_f.setText(datef);
		lat_.setText(lat);
		longit_.setText(longit);
		
		dbAdapter = new DbAdapter(this);
		
		int sizeArray = dbAdapter.getSens(ids);
		acc = new float[sizeArray];
		acc = dbAdapter.convertStringToArray(array);
		que = new Queue(150);
		for(int i=0;i<sizeArray;i++)
		{
			que.enqueue(acc[i]);
		}
		graph.setImageBitmap(que.getGraphQueue(150));
		
		
		
		
		int[] dateA = new int[6];
		dateA = dbAdapter.getDate(dateS);
		int size = 30;
		//String nomeImmagine = date.toLowerCase().replace(' ', '_').replace('\'', '_') + ".png";
		MyGraph rndBitmap = new MyGraph(size,size);
		rndBitmap.doRandomImg(dateA[0], dateA[1], dateA[2], dateA[3], dateA[4], dateA[5], size);
		logo.setImageBitmap(rndBitmap.getRandomImg());
		




	}
}
