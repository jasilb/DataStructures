package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.MinHeap;


import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import java.util.NoSuchElementException;


public class MinHeapImpl<E extends Comparable> extends MinHeap<E>{ 
	  //protected E[] elements;
	    //protected int count=0;
	   // protected Map<E,Integer> elementsToArrayIndex; //used to store the index in the elements array
	public MinHeapImpl() {
		// TODO Auto-generated constructor stub
		this.elements = (E[]) new Comparable[5];
		elementsToArrayIndex = new HashMap<E,Integer>();
		
	}
	
		@Override
		public void reHeapify(E element) {
			// TODO Auto-generated method stub
			System.out.println("reHeapify: ");
			int location = getArrayIndex(element);
			System.out.println("location: " + location);
			upHeap(location);
			location = getArrayIndex(element);
			System.out.println("new location: " + location);
			downHeap(location);
			System.out.println("final location: " + getArrayIndex(element));
		}
		@Override
		protected int getArrayIndex(E element) {
			  if (isEmpty())
		        {
		            throw new NoSuchElementException("Heap is empty");
		        }
			// TODO Auto-generated method stub
			return elementsToArrayIndex.get(element);
		}
		@Override
		protected void doubleArraySize() {
			// TODO Auto-generated method stub
			Comparable[] NewArray = new Comparable[elements.length*2];
			for(int i=1; i< elements.length; i++) {
				NewArray[i] =elements[i];
			}
			elements=(E[]) NewArray;
			
		}
		@Override
	    protected  boolean isGreater(int i, int j)
	    {
	    	System.out.println("i: " + i + ", j; " +j);
	        return (this.elements[i]).compareTo(this.elements[j]) > 0;
	    }
		
		

		@Override 
		protected  boolean isEmpty()
	    {
	        return this.count == 0;
	    }
		@Override 
		protected  void swap(int i, int j)
	    {
			//System.out.println("swap i: " + i + ", swap j: " +j);
	        E temp = this.elements[i];
	        this.elements[i] = this.elements[j];
	        this.elements[j] = temp;
	        elementsToArrayIndex.put(elements[i], i);
	        elementsToArrayIndex.put(elements[j], j);
	    }
		@Override 
		protected  void upHeap(int k)
	    {
			System.out.println("k : " + k);
	        while (k > 1 && this.isGreater(k / 2, k))
	        {
	        	System.out.println("smaller");
	            this.swap(k, k / 2);
	            k = k / 2;
	        }
	    }
		@Override 
		 protected  void downHeap(int k)
		    {
		        while (2 * k <= this.count)
		        {
		            //identify which of the 2 children are smaller
		            int j = 2 * k;
		            System.out.println("check");
		            if (j < this.count && this.isGreater(j, j + 1))
		            {
		                j++;
		            }
		            //if the current value is < the smaller child, we're done
		            if (!this.isGreater(k, j))
		            {
		            	//System.out.println("bigger");
		                break;
		            }
		            //if not, swap and continue testing
		            System.out.println("smaller");
		            this.swap(k, j);
		            k = j;
		        }
		    }
			@Override
		 	public void insert(E x)
		    {
		        // double size of array if necessary
		        if (this.count >= this.elements.length - 1)
		        {
		        	
		            //System.out.println("yes");
		            this.doubleArraySize();
		        }
		        //add x to the bottom of the heap
		        System.out.println("array length: " + elements.length);
		        System.out.println("count before: " + count);
		        this.elements[++this.count] = x;
		        elementsToArrayIndex.put(x, count);
		        System.out.println("count : " + count);
		        //percolate it up to maintain heap order property
		        this.upHeap(this.count);
		        System.out.println("done");
		    }
		 	@Override 
		    public E removeMin()
		    {
		        if (isEmpty())
		        {
		            throw new NoSuchElementException("Heap is empty");
		        }
		        E min = this.elements[1];
		        //swap root with last, decrement count
		        this.swap(1, this.count--);
		        //move new root down as needed
		        this.downHeap(1);
		        this.elements[this.count + 1] = null; //null it to prepare for GC
		        elementsToArrayIndex.remove(min);
		        //System.out.println("remove " + elementsToArrayIndex.remove(min));
		        System.out.println(min);
		        System.out.println("count= " + count);
		        return min;
		    }
		 	
		

}
