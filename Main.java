import java.util.*;
public class Main
{
	public static void DoTurn(PlanetWars pw)
	{
		int n=pw.MyPlanets().size();
		int q=n;
		Planet src=null;
		double score=0,sourceScore = Double.MIN_VALUE;
		if(pw.EnemyPlanets().size()==0)
			return;
		if(n==1)
		{
			
			
			
			for (Planet p : pw.MyPlanets())//finding my strongest planet.. 
			{
			    score = (double)p.NumShips() / (1 + p.GrowthRate());
			    if (score > sourceScore) {
				sourceScore = score;
				src = p;
			    }
			}
			int srcShips=src.NumShips();
			//defining a radius whithin which the attack will take place 
			// if the destination planet has N ships than N+1 ships will be sent to attack that planet 
 			for(Planet p:pw.NotMyPlanets())    
			{
				if(pw.Distance(src.PlanetID(), p.PlanetID())<=13)
				{
					if(srcShips<=src.NumShips()/4)
						break;
					else
					{
						if(pw.EnemyPlanets().contains(p))
						{	if(src.NumShips()>(pw.Distance(src.PlanetID(), p.PlanetID())*p.GrowthRate()))
							{	
								pw.IssueOrder(src, p, p.NumShips()+(pw.Distance(src.PlanetID(), p.PlanetID())*p.GrowthRate()));
								srcShips-=p.NumShips()+(pw.Distance(src.PlanetID(), p.PlanetID())*p.GrowthRate());
							}
						}
						else
						{
							if(srcShips>p.NumShips()+1)
							{
								pw.IssueOrder(src, p, p.NumShips()+1);
								srcShips-=p.NumShips()+1;
							}
						}
					}
				}
			}
		}
		else
		{
			Planet enemy=null;
			int m=0;
			double enScore = Double.MIN_VALUE;
			for (Planet p : pw.EnemyPlanets())//finding the weakest enemy 
			{
			    score = (double)p.GrowthRate() / (1 + p.NumShips());
			    if (score > enScore) 
			    {
				enScore = score;
				enemy = p;
			    }
			}
			List<Planet> nonSourceable = new ArrayList<Planet>();int e=0; //list of those planets which cannot become the source planet i.e attack other planets
			Planet saviour=null;
			double savScore=0;
			for(Planet p:pw.MyPlanets())//attack of the  enemy 
			{	
				 if(pw.AttackingFleets(p.PlanetID())!=0)
				 {
					savScore=0;
					if(p.NumShips()<pw.AttackingFleets(p.PlanetID()))
					{	
						for(Planet p1:pw.MyPlanets())
						{	
							e=((pw.AttackingFleets(p.PlanetID())-p.NumShips())+(pw.Distance(p.PlanetID(), p1.PlanetID())*p.GrowthRate()));
							if(p1.PlanetID()!=p.PlanetID() && p1.NumShips()>e)
							{
								score = (double)(1 + p1.NumShips()) /( pw.Distance(p.PlanetID(),p1.PlanetID()));
								if(score > savScore) 
								{	
									savScore = score;
									saviour=p1;
								}
							}
						}
						nonSourceable.add(saviour);
						if(saviour !=null && saviour.NumShips()>e	)
						{
							
							nonSourceable.add(p);
							pw.IssueOrder(saviour,	p ,	e);
							if(q>2)
								q=q-2;
						}
					}
					else
					{
						nonSourceable.add(p);
						if(q>1)
							--q;
					}
				}
			}
			if(q>0)
			{
				for(Planet p:pw.MyPlanets()) //attacking the enemy planets
				{
					if(enemy.NumShips()/q<p.NumShips() && !nonSourceable.contains(p))
					{

						e=((enemy.NumShips()/(q)))+(pw.Distance(p.PlanetID(),enemy.PlanetID())*enemy.GrowthRate());
						m=p.NumShips();
						if(m>=e)	
							pw.IssueOrder(p,enemy,e);
					}
				}
				
			}
		}
	}
	  public static void main(String[] args) 
	  {
			String line = "";
			String message = "";
			int c;
			try {
			    while ((c = System.in.read()) >= 0) {
				switch (c) {
				case '\n':
				    if (line.equals("go")) {
					PlanetWars pw = new PlanetWars(message);
					DoTurn(pw);
				        pw.FinishTurn();
					message = "";
				    } else {
					message += line + "\n";
				    }
				    line = "";
				    break;
				default:
				    line += (char)c;
				    break;
				}
			    }
			} catch (Exception e) {
			    // Owned.
			}
	  }
}
