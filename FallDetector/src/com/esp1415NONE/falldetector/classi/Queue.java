package com.esp1415NONE.falldetector.classi;

import android.graphics.Bitmap;



public class Queue
	{
		private float[] box;
		private int box_length, sensMax, sensMin;
		
		//Viene chiamato per inserire i dati e rilevare la caduta
		public Queue(int size, int min, int max)
		{
			box = new float[size];
			box_length = size;
//			makeEmpty();
			sensMax = max;
			sensMin = min;
		}
		//Viene chiamato quando mi serve per inserire i dati per poi creare il grafico
		public Queue(int size) 
		{
			box = new float[size];
			box_length = size;
//			makeEmpty();
			sensMax = 0;
			sensMin = 0;
		}
		
//		public void makeEmpty()
//		{back=box_length-1;}
//		
//		public boolean isEmpty()
//		 { return back==box_length-1;}
		
		 public void enqueue(float obj)
		 { 
//		if(increment(back)<0) resize();
//		   box[back]=obj;
//		   if (back > 0) back=increment(back);
		   
		   for (int i=box_length-1;i>0;i--)
			 {
				 box[i]=box[i-1];
			 }
		   box[0]=obj;
		   
		 }
//		 
//		 public int increment(int index)
//		 { return index-1; }
//		 public void resize()
//		 {
//			 for (int i=box_length-1;i>0;i--)
//			 {
//				 box[i]=box[i-1];
//			 }
//		 }
		 public float getFloat(int a)
		 {
			 return box[a];
		 }
		 public float[] getBox()
		 {
			 return box;
		 }
		 //ALGORITMO CADUTA
		 public boolean isFall()
		 {
			 if (getFloat((box_length/2)+1)<sensMin)
				{
					if(getFloat(box_length/2)>sensMax)
					{
						return true;
					}
				}
			 return false;
		 }
		 public Bitmap getGraphQueue(int size)
		 {
			 int i;
			 MyGraph graph;
			 graph = new MyGraph(size,size);
			 graph.doBase();
			 for(i=0;i<box_length;i++)
				 graph.drowPoint(box[i]);	
			 return graph.getGrafico();
		 }

	}
