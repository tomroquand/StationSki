package solveur;

public class Transition {

  // variables
  int num;
  Point dep, arr;
  String name;

  // constructor
  public Transition(int numT, String nameT, Point depT, Point arrT) 
  {
    num = numT;
    name = nameT;
    dep = depT;
    arr = arrT;
  }

  // toString method
  public String toString()
  {
    return "Transition n." + num + " : \"" + name + "\".";
  }

  public Point getDep()
  {
    return dep;
  }

  public Point getArr()
  {
    return arr;
  }

  // we have to define this function in order to overload it with super()
  public Double getTime()
  {
    return 0.;
  }
  
  public String getName()
  {
	  return name;
  }
  
  public int getNum()
  {
	  return num;
  }
  
  public Double getAlt()
  {
	  return arr.alt-dep.alt;
  }
  
  public String getType()
  {
	  return "";
  }
}
