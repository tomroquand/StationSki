package solveur;

public class Remontee extends Transition 
{  
  public enum TypeRemontee
  {
    TK,TST,TSD,TC,TPH,TS
  };

  // variables
  TypeRemontee type;
  double speed;
  double time_install; 

  // constructor
  public Remontee(int numR, String nameR, Point depR, Point arrR, TypeRemontee typeR, double speedR, double t_install) 
  {
    super(numR, nameR, depR, arrR);
    type = typeR;
    speed = speedR;
    time_install = t_install;
    dep.add_succ(arr,this); // add info in Succ hashmap
  }

  public Double getTime()
  {
    //super.getTime();
    //System.out.println("alt " + (dep.alt-arr.alt) + " speed " + speed + " time "+ time_install);
    return Math.abs(dep.alt-arr.alt)*speed/100 + time_install;
  }
  
  public String getType()
  {
	  return super.getType() + type;
  }
  
  // toString method
  public String toString()
  {
    return super.toString() + " : by " + type + ".";
  }
}
