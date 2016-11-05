/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mergesort;

/**
 *
 * @author berni
 */
// CSE 373, Winter 2013, Marty Stepp
// https://courses.cs.washington.edu/courses/cse373/13wi/lectures/03-13/Sorter.java
// A Sorter represents a task that can be run in a thread.
// It performs a merge sort on a given array.
// The idea is that the overall parallel merge sort method can create
// several Sorters, each for a given range of the array, and ask them to sort
// different portions of the array in parallel.
// Then it will merge the pieces in a single thread.

public class Sorter implements Runnable {
	private int[] a;
	private int threadCount;
	
	public Sorter(int[] a, int threadCount) {
		this.a = a;
		this.threadCount = threadCount;
	}
	
	public void run() {
		MergeSOrt.parallelMergeSort(a, threadCount);
	}
}