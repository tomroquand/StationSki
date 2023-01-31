package solveur;

import java.util.*;

public class Station {

  // variables
  String name;
  boolean absTime;
  //int nb_points; // changer partout où on utilise pour mettre size(Lpoint) et utiliser
                 // getNbPoints
  public List<Point> Lpoint = new ArrayList<>();
  public List<Transition> Ltransition = new ArrayList<>();

  // constructor
  public Station(String name) {
    this.name = name;
    absTime = Boolean.FALSE; // de manière arbitraire
    //nb_points = 7;
    Point nul = new Point(0, "nul", 0);
    Lpoint.add(nul);

    Transition nulle = new Transition(0, "nulle", nul, nul);
    Ltransition.add(nulle);
  }

  // getNbPoints method for utilisation in Dijkstra class
  public int getNbPoints() {
    return Lpoint.size();
  }
  
  public Point getPoint(int i)
  {
	  return Lpoint.get(i);
  }
  
  public String toString()
  {
	  String Points ="";
	  
	  for(int i = 1; i < Lpoint.size(); i++)
	  {
		  Points += Lpoint.get(i) + "\n";
	  }
	  return name + "\n" + Points; 
  }

}
