package solveur;

public class Navette extends Transition 
{
  public enum TypeNavette {
    BUS, METRO
  };

  // variables
  TypeNavette type;
  double time;

  // constructor
  public Navette(int numN, String nameN, Point depN, Point arrN, TypeNavette typeN, double timeN) 
  {
    super(numN, nameN, depN, arrN);
    type = typeN;
    dep.add_succ(arr,this); // add info in Succ hashmap after super finished
    time = timeN;
    
  }

  public Double getTime ()
  {
	  System.out.println(time);
	  //super.getTime();
	  return this.time;
  }
  
  public String getType()
  {
	  return super.getType() + type;
  }

  // toString method
  public String toString() {
    return super.toString() + " : by " + type + ".";
  }
}
