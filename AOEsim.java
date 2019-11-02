import javax.swing.*;
import java.util.*;
import java.io.*;

public class AOEsim{
	public static void main(String [] arg) throws IOException{
		
		boolean repeatWhile=true;
		while (repeatWhile==true){
		
		//version nr
		double ver = 0.3;
		//välkommen och version nummer
		System.out.println("\nWelcome to AOEsim v."+ver+"\n\n");
		
		//välj civ
		System.out.println("Please select a civilization\nAll civs:\n1. Britons\n2. Byzantines\n3. Celts\n4. Chinese\n5. Franks\n6. Japanese\n7. Persians\n8. Teutons\n9. Turks\n10. Generic");
		//input för civ val
		int choice, stop;
		BufferedReader rawInput = new BufferedReader (new InputStreamReader(System.in));
		String input = rawInput.readLine();
		//try{
			
		choice = Integer.parseInt(input);
		//} catch()
		
		//initierar standard variabler
		//om flera resurser: food, wood, stone, gold
		boolean wonderCom = false;
		int[] wonder = {0,1000,1000,1000};
		//start resurser
		double food = 200, wood = 200, stone = 200, gold = 100, vil = 3, pop = 5, prePop = 5, clutter = 1;
		
		
		//vil occupation
		//fVil wVil sVil gVil builder working-total total
		int[] villagers = {2,1,0,0,0,3,3};
		//int fVil = 3, wVil = 0, sVil = 0, gVil = 0, builder = 0;
		
		//gather rates
		double fGather = 0.33, wGather = 0.388, sGather = 0.359, gGather = 0.379;
		double[] gather={0.33,0.388,0.359,0.379};
		//diverse
		int startF = 1000, age = 0, timeSize, farmCap = 175, time=0, townCount=0, buildingCo = 1, vilCost=50, farmCost=60, totFarm=0;
		boolean wonderProg = false;
		String typeGet = "", civ = "";
		double tcWork1=1, tcWork2=1, tcWork3=1;
		//byggnads kostnader
		//building time wood stone gold 
	    int[] lumber={35,100,0,0}, mill={35,100,0,0}, blacksmith={40,150,0,0}, market={60,175,0,0}, mining={35,100,0,0}, monastery={40,175,0,0}, university={60,200,0,0}, mining2={35,100,0,0}, house={25,50,0,0}, farm={15,60,0,0};
		//building completion
		boolean lumberC=false, millC=false, miningC=false, blacksmithC=false, marketC=false, universityC=false, monasteryC=false, mining2C=false;
		
		//variabler för tider och krav
		//time, food, gold, wood, stone age-number
		//dark already, feudal 1, castle 2, imperial 3, (wonder 4)
		ages age1 = new ages(130,500,0,525,0,1),age2 = new ages(160,800,200,575,0,2),age3 = new ages(190,1000,800,400,0,3), age4 = new ages(3503,0,1000,1000,1000,4);
		
		ages[] ageComp = {age1, age2, age3, age4};
		
		//final values stop checks
		double finalG=gold, finalS=stone, finalF=food, finalW=wood;
		
		//frank farm stop
		boolean f1=false, f2=false, f3=false;
		
		//lista för at hålla timer objekt för timekeeping
		ArrayList<timer> keeper = new ArrayList<timer>();
		//lista för farms
		ArrayList<farmLand> farmLots = new ArrayList<>(); 
		
		//lisat med klara objekt
		ArrayList<timer> completed = new ArrayList<>();
		
		//lägger till civ specifika ändringar, inkl tidskravs uträknande ändringar
		//case sats för civs
		switch (choice){
			case 1: //britons
			civ = "brit";
			gather[0]*=1.25;
			break;
			case 2: //byzantines
			civ = "byz";
			age3.foodCost=(int)((age3.foodCost*0.67)+0.5);
			age3.goldCost=(int)((age3.goldCost*0.67)+0.5);
			break;
			case 3: //celts
			civ = "celt";
			wGather = 0.444;
			break;
			case 4: //chinese
			civ = "chin";
			villagers[0]+=2;
			villagers[1]+=1;
			villagers[5]+=3;
			wood-=50;
			food-=200;
			pop+=5;
			prePop+=5;
			farmCap+=45;
			break;
			case 5: //franks
			civ = "frank";
			break;
			case 6: //japanese
			civ = "jap";
			lumber[1]=50;
			mining[1]=50;
			mill[1]=50;
			break;
			case 7: //persians
			civ = "pers";
			food+=50;
			wood+=50;
			tcWork1=0.9;
			tcWork2=0.85;
			tcWork3=0.8;
			break;
			case 8: //teutons
			civ = "teut";
			farmCost*=0.67;
			break;
			case 9: //turks
			civ = "turk";
			gGather*=1.15;
			break;
			case 10: //generic
			civ = "generic";
			break;
		}
		
		//räknar ut behövda villagers för varje ålder
		//needed(innan)ålder
		//age1 time, age2 cost, gather
		int[] needed0 = AOEtools.ageCalc(age1, age2, gather, food, wood, stone, gold, villagers[6]);
		int[] needed1 = AOEtools.ageCalc(age2, age3, gather, food, wood, stone, gold, villagers[6]);
		int[] needed2 = AOEtools.ageCalc(age3, age4, gather, food, wood, stone, gold, villagers[6]);
		int[][] neededVil = {needed0, needed1, needed2};
		
		
		System.out.println("needed: "+needed0[0]+", "+needed0[1]+", "+needed0[2]+", "+needed0[3]+", "+needed0[4]);
		System.out.println("needed: "+needed1[0]+", "+needed1[1]+", "+needed1[2]+", "+needed1[3]+", "+needed1[4]);
		System.out.println("needed: "+needed2[0]+", "+needed2[1]+", "+needed2[2]+", "+needed2[3]+", "+needed2[4]);
		
		
		
		//sekund loop
		while (wonderCom==false){
			//
			//
			//
			if (civ=="brit"){
			    if (startF<500){
				gather[0]=0.33;
			    }
			}
			//
			if (civ=="frank"){
				if (startF<500&&startF>0){
					gather[0]*=1.25;
				}
				if (startF<0){
					gather[0]=0.33;
				}
				if ((age==1)&&(f1==false)){
					farmCap+=75;
					f1=true;
				}
				if ((age==2)&&(f2==false)){
					farmCap+=125;
					f2=true;
				}
				if ((age==3)&&(f3==false)){
					farmCap+=175;
					f3=true;
			    }
			}
		    //
    		//gather for sec
			food += AOEtools.gather(villagers[0], fGather, 1);
			wood += AOEtools.gather(villagers[1], wGather, clutter);
			stone += AOEtools.gather(villagers[2], sGather, 1);
			gold += AOEtools.gather(villagers[3], gGather, 1);
			//
			//
			//
			//timer array
			for (int i=0; i<keeper.size();){
				
				typeGet=keeper.get(i).type;
				
				//check completion correlation
				if (keeper.get(i).count==time){ //finished timer
				    
				    if (keeper.get(i).type=="farm"){
						farmLots.add(new farmLand(farmCap));
						/*
						neededVil[age][1]++;
						*/
						if(age==0)
						    needed0[0]++;
					    if(age==1)
						    needed1[0]++;
					    if(age==2)
						    needed2[0]++;
						villagers[0]+=1;
						villagers[4]-=1;
						villagers[5]++;
					}
					else if(keeper.get(i).type=="villager"){
						villagers[5]++;
						villagers[6]++;
						villagers = AOEtools.vilAl(villagers, age, neededVil, wonderProg, food, wood, stone, gold, ageComp);
						vil++;
					}
					else if(keeper.get(i).type=="age"){
						age++;
						System.out.println(time+", "+food+" food, "+wood+" wood, "+stone+" stone, "+gold+" gold. (New age: "+age+")");
						
						if (age<3)
						    neededVil[age-1] = AOEtools.ageCalc(ageComp[age], ageComp[age+1], gather, food, wood, stone, gold, villagers[6]);
					
					}
					else if(keeper.get(i).type=="house"){
						pop+=5;
						if(age==0)
						    needed0[1]++;
					    if(age==1)
						    needed1[1]++;
					    if(age==2)
						    needed2[1]++;
						villagers[1]++;
						villagers[4]--;
						villagers[5]++;
					}
					else if(keeper.get(i).type=="wonder"){
						wonderCom=true;
					}
					else{					
					completed.add(keeper.get(i));
					if(age==0)
						needed0[1]++;
					if(age==1)
						needed1[1]++;
					if(age==2)
						needed2[1]++;
					villagers[1]++;
					villagers[4]--;
					villagers[5]++;
					}
					keeper.remove(i);
				}
				else{
					i++;
				}
			}
			//
			//
			//kollar om wonder ska byggas
			//
			//om ja, börja bygga
			if ((wood >= 1000)&&(gold >= 1000)&&(stone >= 1000)&&(age==3)){
				wonderProg = true;
				villagers = AOEtools.vilAl(villagers, age, neededVil, wonderProg, food, wood, stone, gold, ageComp);
				keeper.add(new timer((time+((buildingCo*(age4.timeCost*3))/(villagers[4]+2))),"wonder"));
				wood-=age4.woodCost;
				gold-=age4.goldCost;
				stone-=age4.stoneCost;
			}
			
			
			if (startF>=0){
			startF-=(villagers[0]*gather[0]);
			//food depletion
			}
			else{
				for (int i=0; i<farmLots.size();){
					if (i<=villagers[0]){
						farmLots.get(i).fCap-=gather[0];
						if (farmLots.get(i).fCap<0){
							farmLots.remove(i);
						}
						else{
							i++;
						}
					}
					else
						break;
				}
			}
			
			//houses
			if((villagers[5]>=(prePop-1))&&(wood>=house[1])){
				if (age==0)
					needed0[1]--;
				if (age==1)
					needed1[1]--;
				if (age==2)
					needed2[1]--;
				villagers[1]--;
				villagers[4]++;
				villagers[5]--;
				wood-=50;
				prePop+=5;
				keeper.add(new timer((time+25),"house"));
			}
			
			//farms
			if((startF<=0)&&(millC==true)){
				if((farmLots.size()<villagers[0])&&(wood>=farm[1])){
					keeper.add(new timer((time+15),"farm"));
					if(age==0)
						needed0[0]--;
					if(age==1)
						needed1[0]--;
					if(age==2){
						needed2[0]--;
					}
					if (civ=="teut"){
						wood-=40;
						totFarm+=(int)(60*0.67);
					}
					else{
					    wood-=60;
						totFarm+=60;
					}
					
					villagers[0]--;
					villagers[4]++;
					villagers[5]--;
				}
			}
			
			//building:
			//
			//mill lumbercamp: 0
			//
			//blacksmith market: 1
			//
			//monastery university: 2
			//age 0
			if (age==0){
			    if ((wood>=lumber[1])&&(lumberC==false)&&(time>60)){
				    wood-=lumber[1];
				    lumberC=true;
				    keeper.add(new timer(time+(35*buildingCo),"lumber"));
					needed0[1]--;
					villagers[1]--;
					villagers[4]++;
					villagers[5]--;
			    }
				if ((wood>=mill[1])&&(millC==false)&&(startF<100)){
					wood-=mill[1];
					millC=true;
					keeper.add(new timer(time+(35*buildingCo),"mill"));
					needed0[1]--;
					villagers[1]--;
					villagers[4]++;
					villagers[5]--;
				}
			}
			if (age==1){
				if ((wood>=market[1])&&(marketC==false)){
				    wood-=market[1];
				    marketC=true;
				    keeper.add(new timer(time+(60*buildingCo),"market"));
					needed1[1]--;
					villagers[1]--;
					villagers[4]++;
					villagers[5]--;
			    }
				if ((wood>=blacksmith[1])&&(blacksmithC==false)){
				    wood-=blacksmith[1];
				    blacksmithC=true;
				    keeper.add(new timer(time+(40*buildingCo),"blacksmith"));
					needed1[1]--;
					villagers[1]--;
					villagers[4]++;
					villagers[5]--;
			    }
				if ((wood>=mining[1])&&(miningC==false)){
				    wood-=mining[1];
				    miningC=true;
				    keeper.add(new timer(time+(35*buildingCo),"mining"));
					needed1[1]--;
					villagers[1]--;
					villagers[4]++;
					villagers[5]--;
			    }
			}
			if (age==2){
				if ((wood>=monastery[1])&&(monasteryC==false)){
				    wood-=monastery[1];
				    monasteryC=true;
				    keeper.add(new timer(time+(40*buildingCo),"monastery"));
					needed1[1]--;
					villagers[1]--;
					villagers[4]++;
					villagers[5]--;
			    }
				if ((wood>=university[1])&&(universityC==false)){
				    wood-=university[1];
				    universityC=true;
				    keeper.add(new timer(time+(60*buildingCo),"university"));
					needed1[1]--;
					villagers[1]--;
					villagers[4]++;
					villagers[5]--;
			    }
				if ((wood>=mining2[1])&&(mining2C==false)){
				    wood-=mining2[1];
				    mining2C=true;
				    keeper.add(new timer(time+(35*buildingCo),"mining2"));
					needed1[1]--;
					villagers[1]--;
					villagers[4]++;
					villagers[5]--;
			    }
			}
			//age 2
			//if age 3, imperial, then wonder
			
			/*if (age<3){
			    neededVil[age] = AOEtools.ageCalc(ageComp[age+1], ageComp[age+2], gather, food, wood, stone, gold, villagers[6]);
			}*/
			villagers = AOEtools.vilAl(villagers, age, neededVil, wonderProg, food, wood, stone, gold, ageComp);
			
			
			
			
			//age progression/villager production
			if (townCount<=time){
			    if (age==0){
				    if ((villagers[6]<needed0[4])&&(food>=vilCost)&&(villagers[6]<pop)){
				    	keeper.add(new timer((time+25),"villager"));
					    food-=vilCost;
						townCount+=25;
					}
					if ((food>=age1.foodCost)&&(lumberC==true)&&(millC==true)&&(villagers[5]>=needed0[4])&&(age==0)){
						keeper.add(new timer((time+130),"age"));
						food-=age1.foodCost;
						townCount+=130;
					}
				}
				else if (age==1){
					if ((villagers[6]<needed1[4])&&(food>=vilCost)&&(villagers[6]<pop)){
						keeper.add(new timer((int)(time+(tcWork1*25)),"villager"));
						townCount+=25;
						food-=vilCost;
					}
					if ((food>=age2.foodCost)&&(gold>=age2.goldCost)&&(marketC==true)&&(blacksmithC==true)&&(villagers[5]>=needed1[4])&&(age==1)){
						keeper.add(new timer((time+160),"age"));
						food-=age2.foodCost;
						gold-=age2.goldCost;
						townCount+=160;
					}
				}
				else if (age==2){
					if ((villagers[6]<needed2[4])&&(food>=vilCost)&&(villagers[6]<pop)){
						keeper.add(new timer((int)(time+(25*tcWork2)),"villager"));
						townCount+=25;
						food-=vilCost;
					}
					if ((food>=age3.foodCost)&&(gold>=age3.goldCost)&&(universityC==true)&&(monasteryC==true)&&(villagers[5]>=needed2[4])&&(age==2)){
						keeper.add(new timer((time+190),"age"));
						food-=age3.foodCost;
					    gold-=age3.goldCost;
						townCount+=190;
					}
				}
			}
			
			time++; //har gått en sekund
			if (time%20==0)
				System.out.println(time+",f:"+food+",w:"+wood+",s:"+stone+",g:"+gold+", vil:"+villagers[6]+", buil:"+villagers[4]+",pop:"+pop+", vils:"+villagers[0]+","+villagers[1]+","+villagers[2]+","+villagers[3]+", fc:"+totFarm+",startF:"+startF+", age:"+age);
			
        	}  //end while
			
			//ge resultat
			System.out.println("\n\nCiv: "+civ+"\nFinal time: "+time+"\nFood: "+food+"\nWood: "+wood+"\nStone: "+stone+"\nGold: "+gold);
			
			System.out.println("\nGo again?\n1. yes 2. no");
			input = rawInput.readLine();
			
			stop = Integer.parseInt(input);
			
			if (stop==2)
				repeatWhile=false;
				//break;
			
		}  //end repeat while
		}  //end main
	}  //end class


class timer{
	int count;
	String type;
	boolean state = false;
	
	public timer(int a, String b){
		count = a;
		type = b;
	}
}

class farmLand{
	double fCap = -1.0; //check init, byter vid list check till farmCap
	
	public farmLand(int a){
		fCap = a;
	}
}

class ages{
	int timeCost;
	int foodCost;
	int goldCost;
	int woodCost;
	int stoneCost;
	int number;
	
	public ages(double a,double b,double c,double d,double e, int f) {
		timeCost = (int)a;
		foodCost = (int)b;
		goldCost = (int)c;
		woodCost = (int)d;
		stoneCost = (int)e;
		number = f;
	}
}