package solveur;

public class Descente extends Transition
{
  public enum TypeDescente
  {
    V, B, R, N
  };

  // Variables 
  TypeDescente type;
  double speed;

  // Constructor
  public Descente(int numD, String nameD, Point depD, Point arrD, TypeDescente typeD, double speedD) 
  {
    super(numD, nameD, depD, arrD); // call Transition constructor
    type = typeD;
    this.speed = speedD;
    dep.add_succ(arr,this); // add info in Succ hashmap
  }

  public Double getTime()
  {
    //System.out.println((dep.alt -arr.alt) + " " + speed);
    return Math.abs(dep.alt-arr.alt)*this.speed/100;
  }
  
  public String getType()
  {
	  return super.getType() + type;
  }
  
  // toString method
  public String toString()
  {
    return super.toString() + " : pist level " + type + ".";
  }

}
