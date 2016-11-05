/*
 * http://broadcast.oreilly.com/2009/06/may-column-multithreaded-algor.html#gen.
 */
package qsort;

/**
 *
 * @author berni
 */
public class Qsort {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
/**********************************************************
 * Sort using quicksort method with separate helper thread.
 * Both left and right and bounded by [0, ar.length).
 * @param left     The left-bounds within which to sort 
 * @param right    The right-bounds within which to sort 
 */
public void qsort (final int left, final int right) {
  qsort2 (left, right);
  	
  // wait until helper is done (if it is still executing).
  while (helpRequested) { }
}
  
/*
 * Sort using quicksort method with separate helper thread.
 */
public void qsort2 (final int left, final int right) {
  if (right <= left) { return; }
       
  // partition
  int p = pi.selectPivotIndex (ar, left, right);
  final int pivotIndex = partition (left, right, p);
      
  // If helper working or problem too big, continue with recursion
  int n = pivotIndex - left;
  if (helpRequested || n >= threshold) {
    qsort2 (left, pivotIndex-1);
  } else {
    helpRequested = true;
   	
    // complete in separate thread
    new Thread () {
      public void run () {
        qsort2 (left, pivotIndex - 1);
        helpRequested = false;
      }
    }.start();
  }
   
  // If helper working or problem too big, continue with recursion
  n = right - pivotIndex;
  if (helpRequested || n >= threshold) {
    qsort2 (pivotIndex+1, right);
  } else {
    // complete in separate thread
    helpRequested = true;
  
    new Thread () {
      public void run () {
        qsort2 (pivotIndex+1, right);
        helpRequested = false;
       }
    }.start();
  }
}	

/**********************************************************
 * Multi-threaded quicksort method entry point.
 * Both left and right and bounded by [0, ar.length).
 * @param left     The left-bounds within which to sort 
 * @param right    The right-bounds within which to sort 
 */
private void qsort2 (final int left, final int right) {
  if (right <= left) { return; }
   
  // partition
  int p = pi.selectPivotIndex (ar, left, right);
  final int pivotIndex = partition (left, right, p);
  
  // are all helper threads working OR is problem too big? 
  // Continue with recursion if so.
  int n = pivotIndex - left;
  if (helpersWorking == numThreads || n >= threshold) {
    qsort2 (left, pivotIndex-1);
  } else {
    // otherwise, complete in separate thread
    synchronized(helpRequestedMutex) {
      helpersWorking++;
    }
			
    new Thread () {
      public void run () {
        // invoke single-thread qsort
        qsortN (left, pivotIndex - 1);
        synchronized(helpRequestedMutex) {
           helpersWorking--;
        }
      }
    }.start();
  }
   
  // are all helper threads working OR is problem too big? 
  // Continue with recursion if so.
  n = right - pivotIndex;
  if (helpersWorking == numThreads || n >= threshold) {
    qsort2 (pivotIndex+1, right);
  } else {
    // otherwise, complete in separate thread
    synchronized(helpRequestedMutex) {
      helpersWorking++;
    }
  	
    new Thread () {
      public void run () {
        // invoke single-thread qsort
        qsortN (pivotIndex+1, right);
        synchronized(helpRequestedMutex) {
          helpersWorking--;
        }
      }
    }.start();
  }
}
