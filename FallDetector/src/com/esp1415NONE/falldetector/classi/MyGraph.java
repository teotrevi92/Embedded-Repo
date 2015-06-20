package com.esp1415NONE.falldetector.classi;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class MyGraph {

	private Bitmap bg;
	private Canvas canvas;
	private int ingrandimento;
	private Paint paint;
	private int xLarghezza;
	private int yLarghezza;
	private float asseY;
	private float tempo;
	private float starX;
	private float starY;
	private float stopX;
	private float stopY;
	private Bitmap rnd;
	private Canvas canvas2;
	private Paint paint2;

	public MyGraph(int larghezza,int altezza)
	{
		ingrandimento = 6;
		paint = new Paint();
		xLarghezza = larghezza;
		yLarghezza = altezza;
		asseY=(yLarghezza/5)*4;//metto l'asse y a meta' del disegno
		tempo=0;
		starX = 0;
		starY =0;
		stopX=0;
		stopY=0;
	}

	public void doBase()
	{
		tempo=5;// ho messo 5,e gli altri due valori seguenti per farlo partire dall'origine deli assi
		stopX=5;
		stopY=asseY; 
		bg=Bitmap.createBitmap(xLarghezza, yLarghezza, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bg);
		paint.setColor(Color.parseColor("black"));
		canvas.drawLine(5,0,5,yLarghezza, paint);
		canvas.drawLine(0,asseY,xLarghezza-20,asseY, paint);
		canvas.drawText("0", xLarghezza-15, asseY, paint);

		//disegno la freccia
		canvas.drawLine(5,0,0,5,paint);
		canvas.drawLine(5,0,10,5,paint);

		//disego linee di scala
		canvas.drawLine(0, asseY-5*ingrandimento, xLarghezza-20, asseY-5*ingrandimento, paint);
		canvas.drawText("5", xLarghezza-15, asseY-5*ingrandimento, paint);
		canvas.drawLine(0, asseY-10*ingrandimento, xLarghezza-20, asseY-10*ingrandimento, paint);
		canvas.drawText("10", xLarghezza-15, asseY-10*ingrandimento, paint);
		canvas.drawLine(0, asseY-15*ingrandimento, xLarghezza-20, asseY-15*ingrandimento, paint);
		canvas.drawText("15", xLarghezza-15, asseY-15*ingrandimento, paint);
		canvas.drawLine(0, asseY-20*ingrandimento, xLarghezza-20, asseY-20*ingrandimento, paint);
		canvas.drawText("20", xLarghezza-15, asseY-20*ingrandimento, paint);
		canvas.drawLine(0, asseY-25*ingrandimento, xLarghezza-20, asseY-25*ingrandimento, paint);
		canvas.drawText("25", xLarghezza-15, asseY-25*ingrandimento, paint);
		canvas.drawLine(0, asseY-30*ingrandimento, xLarghezza-20, asseY-30*ingrandimento, paint);
		canvas.drawText("30", xLarghezza-15, asseY-30*ingrandimento, paint);
		canvas.drawLine(0, asseY-35*ingrandimento, xLarghezza-20, asseY-35*ingrandimento, paint);
		canvas.drawText("35", xLarghezza-15, asseY-35*ingrandimento, paint);

		paint.setColor(Color.parseColor("blue"));
	}

	public void drowPoint(float punto)
	{
		tempo=tempo+2f;
		starX = tempo;
		starY =asseY-punto*ingrandimento;
		canvas.drawLine(starX, starY, stopX, stopY, paint);
		stopX=starX;
		stopY=starY;
	}

	//creo un immagine random
	public void doRandomImg(int year, int month, int day, int hours, int minutes, int seconds,int size)
	{
		rnd = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);;
		canvas2 = new Canvas(rnd);
		paint2 = new Paint();
		canvas2.drawColor(Color.BLUE);
		paint2.setColor(Color.parseColor("white"));
		float[] xrand = new float[10]; 
		float[] yrand = new float[10]; 
		int i;
		xrand[6]=year-2000;
		xrand[1]=(month*size)/12;
		xrand[2]=(day*size)/31;
		xrand[3]=(hours*size)/24;
		xrand[4]=(minutes*size)/60;
		xrand[5]=(seconds*size)/60;
		for (i=0;i<6;i++)
		{
			yrand[i]=(i+1)*(size/6);
		}
		for(i=0;i<6;i++)
			canvas2.drawCircle(xrand[i], yrand[i], 3 , paint2);	
	}

	public Bitmap getRandomImg()
	{
		return Bitmap.createBitmap(rnd);
	}

	//ritora il grafico
	public Bitmap getGrafico()
	{	
		return Bitmap.createBitmap(bg);
	}

}
