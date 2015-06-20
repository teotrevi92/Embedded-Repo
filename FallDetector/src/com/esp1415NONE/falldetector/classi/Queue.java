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
		sensMax = max;
		sensMin = min;
	}
	//Viene chiamato quando mi serve per inserire i dati per poi creare il grafico
	public Queue(int size) 
	{
		box = new float[size];
		box_length = size;
		sensMax = 0;
		sensMin = 0;
	}

	public void enqueue(float obj)
	{ 
		for (int i=box_length-1;i>0;i--)
		{
			box[i]=box[i-1];
		}
		box[0]=obj;

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
