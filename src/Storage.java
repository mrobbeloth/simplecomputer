/**
 * Java class: Storage
 * @author:  Curt Hill
 * Purpose:  Store integers for retrieval
*/
import java.awt.*;
public class Storage{
    int arr[];
    int last,size;

 public Storage(int sz) {
    size = sz;
    last = 0;
    arr = new int [size];
  }

 public boolean add(int it) {
  if(last>=size) 
    return false;
  arr[last++] = it;    
  return true;
  }

 public int [] get(){
  int a[] = new int [last];
  for(int i=0;i<last;i++)
     a[i]=arr[i];
  return a; 
 }

 public void clear(){
  last = 0;
 }

  }; // end of class Storage
