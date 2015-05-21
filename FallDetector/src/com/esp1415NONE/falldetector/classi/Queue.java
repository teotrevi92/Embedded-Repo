package com.esp1415NONE.falldetector.classi;

import android.graphics.Bitmap;



public class Queue
	{
		private float[] box;
		private int back,max;
		
		public Queue(int size)
		{
			box = new float[size];
			max = size;
			makeEmpty();
		}
		public void makeEmpty()
		{back=max-1;}
		public boolean isEmpty()
		 { return back==max-1;}
		 public void enqueue(float obj)
		 { if(increment(back)<0) resize();
		   box[back]=obj;
		   if (back > 0) back=increment(back);
		 }
		 public int increment(int index)
		 { return index-1; }
		 public void resize()
		 {
			 for (int i=max-1;i>0;i--)
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
			 if (getFloat((max/2)+1)<7)
				{
					if(getFloat(max/2)>=15)
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
			 for(i=0;i<max;i++)
				 graph.disegna(box[i]);	
			 return graph.getGrafico();
		 }
		 
	}
