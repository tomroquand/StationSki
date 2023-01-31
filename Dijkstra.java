package solveur;

import java.util.*;
import java.util.Random;

public class Dijkstra {
  // variables

  Station stat;
  Point dep, arr;
  List<Double> Pi;
  List<Boolean> Mark;
  List<Integer> Pere;
  int reel; // equals 0 or 1 if time is absolute or not (0=abs, 1=real)
  

  // constructor using Station object 
  public Dijkstra(Station stat, int indice_pt_1, int indice_pt_2, int reelOrNot) throws PointInexistantException,PathNonExistant
  {
    /////////////// INITIALIZATION //////////////////
    this.stat = stat;
    Pi = new ArrayList<Double>(Arrays.asList(new Double[stat.getNbPoints()]));
    Mark = new ArrayList<>(Arrays.asList(new Boolean[stat.getNbPoints()]));
    Pere = new ArrayList<>(Arrays.asList(new Integer[stat.getNbPoints()]));
    reel = reelOrNot;
    
    
    // verify if both points index are between 0 and the max of index
    if(!((indice_pt_1 < stat.Lpoint.size())&&(indice_pt_1 > 0)))
    	throw new PointInexistantException("Point de départ introuvable dans le fichier...");
    else if(!((indice_pt_2 < stat.Lpoint.size())&&(indice_pt_2 > 0)))
    	throw new PointInexistantException("Point d'arrivée introuvable dans le fichier...");
    
	dep = stat.Lpoint.get(indice_pt_1);
	arr = stat.Lpoint.get(indice_pt_2);
    
    Point actP = dep;
    
    
    // List filling
    Collections.fill(Mark,Boolean.FALSE);  // fills all entries with FALSE
    Collections.fill(Pere,0);              // fills all entries with 0
    Collections.fill(Pi,Double.MAX_VALUE); // fills all entries with MAX_VALUE
    
    
    // first values
    Pere.set(dep.getID(),dep.getID()); // departure father is the departure
    Pi.set(dep.getID(),0.0);           // distance between dep and dep is 0
    
    ///////////////////////////////////////////////////
    
    /* verification
    System.out.println(Mark);
    System.out.println(Pere);
    System.out.println(Pi);*/
    
    
    
    /////////////////////// BEGIN LOOP ////////////////////////
    
    while (Mark.get(arr.getID()) == Boolean.FALSE) // WHILE ARRIVAL POINT NOT MARKED
    {   
        double valMin=Double.MAX_VALUE;
        int indMin=-1;
        
        for (int i = 0; i<Mark.size();i++) // LOOP ON MARK
        {
            if ((Mark.get(i) == Boolean.FALSE) && (Pi.get(i)<valMin)) // CHOOSE IND : PI MIN AND NOT MARKED
            {
                valMin = Pi.get(i);
                indMin = i;
            }
        }
        if (indMin==-1) throw new PathNonExistant("Aucun chemin trouvé");
        actP = stat.Lpoint.get(indMin); // UPDATE ACTUAL POINT
        Mark.set(indMin,Boolean.TRUE);  // MARK ACTUAL POINT
        
        // LOOP THAT LOOKS ALL SUCC (add values to Pi of actual point successors)
        for (Map.Entry<Point, Transition> entry : stat.Lpoint.get(indMin).Succ_transi.entrySet()) 
        {
            Point key = entry.getKey();
            Double value = entry.getValue().getTime();
            
            // CHECK IF THE NEW WAY IS THE BEST
            if(value + Pi.get(actP.getID()) < Pi.get(key.getID()))
            {
                value += Pi.get(actP.getID());      // ADD ALL THE DISTANCE FROM DEPARTURE TO ACTUAL POINT
                Pi.set(key.getID(),value);          // UPDATE PI
                Pere.set(key.getID(),actP.getID()); // UPDATE Pere
            }
           
            
        }
    }
    
    ////////////////////// END LOOP //////////////////////////////////
  }
  
  
  public List<Transition> getPCC() 
  {  
    /////////////// INITIALIZATION //////////////////
    int actP = arr.getID();
    LinkedList<Integer> UnsettledListPoints = new LinkedList<>();
    LinkedList<Integer> ListPoints = new LinkedList<>();
    LinkedList<Transition> PCC = new LinkedList<>();
    Transition trans_parcourue = new Transition(0, "", arr, arr);
    
    //////////////////// BEGIN FIRST LOOP //////////////////////
    
    while (actP != dep.getID()) // BROWSING LIST PERE TO CREATE A LIST ListPoints REVERSED  
    {   
        UnsettledListPoints.add(actP); 
        actP = Pere.get(actP);
    }
    
    UnsettledListPoints.add(actP);
    
    /////////////////// END FIRST LOOP /////////////////////////
    
    /////////////////// BEGIN SECOND LOOP //////////////////////
    
    while (UnsettledListPoints.size() != 0) // STORAGE OF THE LIST
    {   
        ListPoints.addFirst(UnsettledListPoints.pollFirst()); // ADD AND REMOVE THE FIRST VALUE OF UNSETTLEDListPoints IN ListPoints 
    }
    /////////////////// END SECOND LOOP ////////////////////////
    
    for (int i=0;i<ListPoints.size()-1;i++)
    {
        for (Map.Entry<Point, Transition> entry : stat.Lpoint.get(ListPoints.get(i)).Succ_transi.entrySet()) 
        {   
            double minValue = Double.MAX_VALUE;
            double value;
            
            Point key = entry.getKey();
            Transition trans = entry.getValue();
            if (key==stat.Lpoint.get(ListPoints.get(i+1)))
            {   
                value = trans.getTime();
                if(minValue > value)
                {
                    minValue = value;
                    trans_parcourue = trans;
                }
            }
        }
        PCC.add(trans_parcourue);
    
    }
    
    return (PCC);
  }

  
  public String toString() 
  {
	  List<Transition> PCC = this.getPCC();
	  String itineraire = "";
	  double totalTime = 0.;
	  double totalAlt = 0.;
	  
	  for(int i = 0; i < PCC.size(); i++)
	  {
		  itineraire += "Transition " + PCC.get(i).getNum() + " : "  + PCC.get(i).getName() + " (" + PCC.get(i).getType() + ")"
				     + ", from " + PCC.get(i).getDep() + " to " + PCC.get(i).getArr() + "\n";
		  
		  totalTime += PCC.get(i).getTime() + Math.random() * 1800 * reel;
		  //random between 0 and 1 -> times 1800 between 0 and 30 minutes
		  
		  totalAlt += Math.abs(PCC.get(i).getAlt());
	  }
	  
    return "Plus court chemin entre " + this.dep + " et " + this.arr + " : \n" + itineraire
    		+ "\nTotal time : " + totalTime
    		+ "\nTotal cumulative altitude difference : " + totalAlt;
  }

}

