package solveur;

import java.util.*;

public class Point {

  // variables
  public int num;
  public String name;
  double alt;
  Map<Point, Transition> Succ_transi = new HashMap<>(); // successor transitions to get itinerary 

  // constructor
  public Point(int numP, String nameP, double altP) 
  {
    num = numP;
    name = nameP;
    alt = altP;
 
  }

  // toString method
  public String toString() 
  {
    return "pt n." + num + " : " + name + " (" + alt + "m)";
  }

  // getID method (for utilisation in Dijkstra class)
  public int getID() 
  {
    return num;
  }

  public void add_succ(Point arr, Transition t) 
  {
    if(this.Succ_transi.containsKey(arr))
    {
      if(t.getTime() < this.Succ_transi.get(arr).getTime())
      {
        this.Succ_transi.put(arr, t);
      }     
    }
    else
    {
      //System.out.println(t.getTime() + " ici");
      this.Succ_transi.put(arr, t);
    }

  }
  
}
