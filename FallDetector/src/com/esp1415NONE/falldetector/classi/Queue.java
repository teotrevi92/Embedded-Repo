package com.esp1415NONE.falldetector.classi;

import android.graphics.Bitmap;



public class Queue
	{
		private float[] box;
		private int back, box_length, sensMax, sensMin;
		
		public Queue(int size, int min, int max)
		{
			box = new float[size];
			box_length = size;
			makeEmpty();
			sensMax = max;
			sensMin = min;
		}
		public void makeEmpty()
		{back=box_length-1;}
		public boolean isEmpty()
		 { return back==box_length-1;}
		 public void enqueue(float obj)
		 { if(increment(back)<0) resize();
		   box[back]=obj;
		   if (back > 0) back=increment(back);
		 }
		 public int increment(int index)
		 { return index-1; }
		 public void resize()
		 {
			 for (int i=box_length-1;i>0;i--)
			 {
				 box[i]=box[i-1];
			 }
		 }
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
					if(getFloat(box_length/2)>=sensMax)
					{
						return true;
					}
				}
			 return false;
		 }
		 public Bitmap getGraphQueue()
		 {
			 int i;
			 Grafico graph;
			 graph = new Grafico(300,300);
			 graph.doBase();
			 for(i=0;i<box_length;i++)
				 graph.disegna(box[i]);	
			 return graph.getGrafico();
		 }
		 
	}
