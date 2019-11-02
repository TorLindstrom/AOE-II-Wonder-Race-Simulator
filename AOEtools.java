

public class AOEtools{
	
	//general gather sec
	public static double gather(int vil, double gatherRate, double clutter){
		double gatherResult = (vil*gatherRate)/clutter;
		return gatherResult;
	}
	
	//vil allocator
	public static int[] vilAl(int[] villagers, int age, int[][] needed, boolean prog, double food, double wood, double stone, double gold, ages[] costs){
		
		villagers[0]=0;
		villagers[1]=0;
		villagers[2]=0;
		villagers[3]=0;
		villagers[4]=0;
		
		for (int i=0;i<villagers[5];i++){
		
		if (age==0){  //"balanced" acceleration to get going
			if (villagers[0]<3)
				villagers[0]++;
			else if (villagers[1]<2)
				villagers[1]++;
			else if (villagers[0]<(needed[0][0]/4))
				villagers[0]++;
			else if (villagers[1]<(needed[0][1]/4))
				villagers[1]++;
			else if (villagers[0]<((needed[0][0])/2))
				villagers[0]++;
			else if (villagers[1]<(needed[0][1]/2))
				villagers[1]++;
			else if (villagers[3]<needed[0][3])
				villagers[3]++;
			else if (villagers[0]<needed[0][0])
				villagers[0]++;
			else 
				villagers[1]++;
		}
		if (age==1){  //throttling up to match needs
			if (villagers[0]<needed[1][0])
			    villagers[0]++;
			else if (villagers[1]<4)
				villagers[1]++;
			else if (villagers[1]<needed[1][1])
				villagers[1]++;
			else if (villagers[3]<needed[1][3])
				villagers[3]++;
			else if (villagers[2]<needed[1][2])
				villagers[2]++;
			else 
				villagers[1]++;
		}
		if (age==2){  //throttling
			if (villagers[0]<needed[2][0])
				villagers[0]++;
			else if (villagers[1]<needed[2][1])
				villagers[1]++;
			else if (villagers[2]<needed[2][2])
			    villagers[2]++;
			else if (villagers[3]<needed[2][3])
				villagers[3]++;
			else
				villagers[0]++;
		}
		if (age==3&&prog==false){
			if (wood<1000)
			    villagers[1]++;
		    else if (stone<1000)
				villagers[2]++;
			else if (gold<1000)
				villagers[3]++;
			else 
				villagers[1]++;
		}
		if (prog==true){ //all builder
			villagers[4]++;
		}
		}
		
		return villagers;
	
	} //end class allocator
	
	//age demand calculator
	public static int[] ageCalc(ages age1,ages age2, double[] gather, double food, double wood, double stone, double gold, int vil){
		//food wood stone gold total in villagers
		int[] villagers = {0,0,0,0,0};
		villagers[0] = (int) ((((age2.foodCost-food)/age1.timeCost)/gather[0])+0.5);
		villagers[1] = (int) (((((age2.woodCost-wood)/age1.timeCost)/gather[1])+0.5)+(villagers[0]/2));
		villagers[2] = (int) ((((age2.stoneCost-stone)/age1.timeCost)/gather[2])+0.5);
		villagers[3] = (int) ((((age2.goldCost-gold)/age1.timeCost)/gather[3])+0.5);
		villagers[4] = villagers[0]+villagers[1]+villagers[2]+villagers[3];
		if (vil<villagers[4]){
		    villagers[0] +=6;
			villagers[4] +=6;
		}
		
		for (int i=0;i<4;i++){
			if (villagers[i]<0)
				villagers[i]=0;
		}
		//villagers[0] = (int) (((age2.foodCost-food)/age1.timeCost)/gather[0])+0.5;
		
		
		return villagers;
	}		
	
}
