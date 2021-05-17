import java.util.*;
import java.io.*;
import java.util.Scanner ;
import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.InputMismatchException;
//mafias can only be voted out, not killed if HP falls to 0
class MafiaSim{
    private static Scanner  s= new Scanner(System.in);
    private static genlist<Integer> playlist1=new genlist<Integer>(); //player no list1(removing fron this)
    private static genlist<Integer> playlist2=new genlist<Integer>(); //player no lsit2
    private static genlist<Mafia> mlist=new genlist<Mafia>(); //Mafia list 
    private static genlist<Detective> dlist=new genlist<Detective>(); //Detective list 
    private static genlist<Healer> hlist= new genlist<Healer>(); //Healer list 
    private static genlist<Commoner> clist=new genlist<Commoner>(); //Commoner list 
    private static genlist<Player> all= new genlist<Player>();  //all players list 
    private static genlist<Integer> votes=new genlist<Integer>(); //votes list
    private static Map<Integer, Integer> hm=new HashMap<Integer, Integer>(); //for storing votes of each player 

    private static Mafia m =new Mafia(0, "Mafia", 0, 0); 
    private static Detective d =new Detective(0, "Detective", 0, 0);
    private static Healer h =new Healer(0, "Healer", 0, 0);
    private static Commoner c =new Commoner(0, "Commoner", 0, 0); 

    private static int maf=0; //no of mafias 
    private static int det=0; //no of detectives 
    private static int hea=0; //no of healers 
    private static int com=0; //no of commoners 
    private static Random rand =new Random();
    private static int userID=0;
    private static int round=1;
    private static boolean userstat=true;

    public static void main(String[] args){
        System.out.println("Welcome to Mafia");
        System.out.println("Enter Number of Players:");
        int n =s.nextInt(); //enter players
       
        while(true){
            if(n<6){ //min players=6
                n=s.nextInt();
            }
            else {
                break;
            }
        }
        for(int i=1; i<=n; i++){ //adding players(players ID) to list 
            playlist1.add(i);
            playlist2.add(i);
        }
        //N/5 mafia, N/5 detective, max(1, N/10) healers, rest common
        maf=n/5; //no of mafia
        det=n/5; //no of detectives
        hea=Math.max(1, n/10); //no of healers
        com=n-(maf+det+hea); //no of commoners 
        System.out.println("Choose a Character");
        System.out.println("1) "+m);
        System.out.println("2) "+d);
        System.out.println("3) "+h);
        System.out.println("4) "+c);
        System.out.println("5) Assign Randomly");
        int index;
        int val;
        //assigning random IDs to each player 
        for(int i=0; i<maf; i++){
            index=rand.nextInt(playlist1.size());
            val=playlist1.get(index);
            mlist.add(new Mafia(val, "Mafia", 2500, 0));
            all.add(new Mafia(val, "Mafia", 2500, 0));
            playlist1.remove(index);
        }
        for(int i =0; i<det; i++){
            index=rand.nextInt(playlist1.size());
            val=playlist1.get(index);
            dlist.add(new Detective(val, "Detective", 800, 0));
            all.add(new Detective(val, "Detective", 800, 0));
            playlist1.remove(index);
        }
        for(int i =0; i<hea; i++){
            index=rand.nextInt(playlist1.size());
            val=playlist1.get(index);
            hlist.add(new Healer(val, "Healer", 800, 0));
            all.add(new Healer(val, "Healer", 800, 0));
            playlist1.remove(index);
        }   
        for(int i =0; i<com; i++){
            index=rand.nextInt(playlist1.size());
            val=playlist1.get(index);
            clist.add(new Commoner(val, "Commoner", 1000, 0));
            all.add(new Commoner(val, "Commoner", 1000, 0));
            playlist1.remove(index);
        }
        int choose=s.nextInt(); //choose char
        if(choose==1){
            index=rand.nextInt(mlist.size()); //random index selection 
            userID=mlist.get(index).getID(); //user ID selected at that index 
        }
        else if(choose==2){
            index=rand.nextInt(dlist.size()); //random index selection 
            userID=dlist.get(index).getID(); //user ID selected at that index 
            
        }
        else if(choose==3){
            index=rand.nextInt(hlist.size()); //random index selection 
            userID=hlist.get(index).getID(); //user ID selected at that index 
            
        }
        else if(choose==4){
            index=rand.nextInt(clist.size()); //random index selection 
            userID=clist.get(index).getID(); //user ID selected at that index 

        }
        else if(choose==5){
            index=rand.nextInt(playlist2.size()); //random index selection 
            userID=playlist2.get(index);         //user ID selected at that index 

        }
        System.out.println("You are Player"+userID);
        //if user Mafia, do something 
        //if user Detective, do something 
        //if user Healer, do something 
        //if user Commoner, do something 
        for(int i =0; i<all.size(); i++){
            
            if(all.get(i).getID()==userID && all.get(i).getType().equals("Mafia")){ 
                userM();
            }
            else if(all.get(i).getID()==userID && all.get(i).getType().equals("Detective")){
                userD();
            }
            else if(all.get(i).getID()==userID && all.get(i).getType().equals("Healer")){
                userH();
            }
            else if(all.get(i).getID()==userID && all.get(i).getType().equals("Commoner")){
                userC();
            }
        }
    }

    //MAFIA
    public static void userM(){ //if user is mafia
        System.out.println("You are a "+m);
        System.out.print("Other Mafias are: ");
        //finding other mafias 
        for(int j =0; j<mlist.size(); j++){
            if(mlist.get(j).getType().equals("Mafia") && mlist.get(j).getID()!=userID){
                System.out.print("Player"+mlist.get(j).getID()+" ");
            }
        }

        System.out.println();

        while(maf>0 && maf!=det+hea+com ){
        System.out.println("Round"+round);
        System.out.print(all.size()+" "+"players are remaining: ");
        for(int i =0; i<all.size(); i++){
            System.out.print("Player"+all.get(i).getID()+",");
        }
        System.out.print("are alive.");
        System.out.println();

        int target=0; //mafia target ID
        //System.out.println("Choose a target:");
        //int ind=s.nextInt(); //choosing ID of target 
        int ind=0;
        boolean done=false;
        while(!done){
            if(userstat==true){
                System.out.println("Choose a target:");
                ind=s.nextInt();
                for(int i =0; i<all.size(); i++){
                    if(all.get(i).getID()==ind && !all.get(i).equals(new Mafia())){
                        done=true;
                        break;
                    }
                }
            }
            else if(userstat==false && maf>0){
                ind=rand.nextInt(all.size());
                for(int i =0; i<all.size(); i++){
                    //if(all.get(i).getID()==all.get(ind).getID() && !all.get(i).getType().equals("Mafia")){
                    if(all.get(i).getID()==all.get(ind).getID() && ! all.get(i).equals(new Mafia())){
                        done=true;
                        break;
                    }
                }

            }
            if(done){
                
                break;
            }
        }
        target=ind;
        System.out.println();
       

        //calculating mafias total HP
        float totmafiaHP=0;
        for(int i =0; i<all.size(); i++){
            if(all.get(i).getType().equals("Mafia")){
                totmafiaHP=totmafiaHP+all.get(i).getHP();
            }
        }
        
        //no of alive mafias whose HP>0
        int Y=0;
        for(int i =0; i<all.size(); i++){
            if(all.get(i).getType().equals("Mafia") && all.get(i).getHP()>0){
                Y++;
            }
        }
        
        //HP reducing/increasing ) and damages taken by mafias 
        float X=0;
        for(int i =0; i<all.size(); i++){
            if(all.get(i).getID()==target && totmafiaHP>=all.get(i).getHP()){
                X=all.get(i).getHP(); //initial HP of target
                all.get(i).setHP(0);
            }
            else if(all.get(i).getID()==target && totmafiaHP<all.get(i).getHP()){
                X=all.get(i).getHP();
                all.get(i).setHP(all.get(i).getHP()-totmafiaHP);
            }
        }
        
        //damages 
        float rem=0;
        for(int i =0; i<all.size(); i++){
            if(all.get(i).getType().equals("Mafia")){
                if((X/Y)<=all.get(i).getHP()){
                    all.get(i).setHP(all.get(i).getHP()-(float)(X/Y -rem));
                }
                else if((X/Y)>all.get(i).getHP()){
                    rem=(X/Y)-all.get(i).getHP();
                }
            }
        }
        
        //detective testing 
        int test=0; 
        if(det>0){
            test=rand.nextInt(all.size()); //index
            //System.out.println("Detectives have chosen a player to test.");
        }
        boolean done2=false;
        if(det>0){
            while(!done2){
                test=rand.nextInt(all.size());
                for(int i=0; i<all.size(); i++){
                    if(all.get(i).getID()==all.get(test).getID() && !all.get(i).getType().equals("Detective")){
                        done2=true;
                        System.out.println("Detectives have chosen a player to test.");
                    }
                }
            }
        }

        for(int i=0; i<all.size(); i++){
                //checking if mafia or not 
            if(all.get(i).getID()==all.get(test).getID() && all.get(i).getType().equals("Mafia")){
                //System.out.println("Player"+test+" is a Mafia");
                break;
            }
            else if(all.get(i).getID()==all.get(test).getID() && ! all.get(i).getType().equals("Mafia")){
                //System.out.println("Player"+test+" is not a Mafia");
                break;
                
            }
        }
        //choosing player to heal if healers left 
        if(hea>0){
            int healind=rand.nextInt(all.size());
            int healID=all.get(healind).getID(); //player ID
            all.get(healind).setHP(all.get(healind).getHP()+500);
            System.out.println("Healers have chosen someone to heal");
        }
        System.out.println("--End of Actions--");

        //player dies condition(if HP==0 of target even after healing process  )
        for(int i =0; i<all.size(); i++){
            if(all.get(i).getID()==target && all.get(i).getHP()==0){
                System.out.println("Player"+all.get(i).getID()+" has died."); 
            
                if(all.get(i).getType().equals("Mafia")){

                    maf--;
                    
                }
                else if(all.get(i).getType().equals("Detective")){
                    det--;
                    
                }
                else if(all.get(i).getType().equals("Healer")){
                    hea--;
                   
                }
                else if(all.get(i).getType().equals("Commoner")){
                    com--;
                   
                }
                all.remove(i); //removing died player from list 
            }
            else if(all.get(i).getID()==target && all.get(i).getHP()>0){
                System.out.println("No one died");
                break;
            }
        }

        //checking if voting possible or not 
        int flag=0; //change to 1 if mafia detected by detective 
        for(int i =0; i<all.size(); i++){
            //detective tests positive on mafia 
            if(all.get(i).getID()==all.get(test).getID() && all.get(i).getType().equals("Mafia")){
                //no voting , voted out by default
                if(all.get(i).getID()==userID){
                    userstat=false;
                }
                flag =1;
                System.out.println("Player"+all.get(i).getID()+ " has been voted out");
                all.remove(i);
                maf--;
                break;
            }
            else if(all.get(i).getID()==test && !all.get(i).getType().equals("Mafia")){
                flag=0;
                break;
            }
        }

        //voting if flag==0 ,not if flag== 1 
        int ch;
    
        if(flag==0){
            for(int i=0; i<all.size(); i++){
                //choosing user opinion to vote out if user still alive
                if(all.get(i).getID()==userID){
                    int vote=0;
                    while(vote==0){
                        System.out.println("Select a person to vote out: ");
                        vote =s.nextInt();
                        if(vote==userID ){
                            vote=0;
                            continue;

                        }
                    }
                    //vote=s.nextInt();
                    all.get(i).setVote(vote);
                    votes.add(all.get(i).getVote());
                }
                //random voting by rest 
                else if(all.get(i).getID()!=userID){
                    ch=rand.nextInt(all.size());
                    all.get(i).setVote(all.get(ch).getID()); //check this line r
                    votes.add(all.get(i).getVote());
                }
            }
            int maxv=maxvotes(); //ID of person with max votes
            for(int i=0; i<all.size(); i++){
                if(all.get(i).getID()==maxv){
                    System.out.println("Player"+maxv+" has been voted out.");

                    if(all.get(i).getType().equals("Mafia")){
                        
                        maf--;
                        
                    }
                    else if(all.get(i).getType().equals("Detective")){
                        
                        det--;
                        
                    }
                    else if(all.get(i).getType().equals("Healer")){
                        
                        hea--;
                       
                    }
                    else if(all.get(i).getType().equals("Commoner")){
                       
                        com--;
                        
                    }
                    all.remove(i);
                }
            }
        }
        System.out.println("End of Round"+round);
        votes=new genlist<Integer>(); //clearing previous votes after end of round 
        hm=new HashMap<Integer, Integer>(); //clearing max frequency votes after end of round 
        round++;
    }
     
    System.out.println("Game Over.");
    //mafias lose
    if(maf==0){
        System.out.println("The Mafias have lost");
    }
    //mafias win
    else{
        System.out.println("The Mafias have won");
    }

    System.out.print("Mafias were: ");
    for(int i =0; i<mlist.size(); i++){
        System.out.print("Player"+mlist.get(i).getID()+" ");
    }
    System.out.println();
    System.out.print("Detectives were: ");
    for(int i =0; i<dlist.size(); i++){
        System.out.print("Player"+dlist.get(i).getID()+" ");
    }
    System.out.println();
    System.out.print("Healers were: ");
    for(int i =0; i<hlist.size(); i++){
        System.out.print("Player"+hlist.get(i).getID()+" ");
    }
    System.out.println();
    System.out.print("Commoners were: ");
    for(int i =0; i<clist.size(); i++){
        System.out.print("Player"+clist.get(i).getID()+" ");
    }

    }

    //DETECTIVE
    public static void userD(){ //if user is detective
        System.out.println("You are a "+d);
        System.out.print("Other Detectives are: ");
        for(int j=0; j<dlist.size(); j++){
            if(dlist.get(j).getType().equals("Detective") && dlist.get(j).getID()!=userID){
                System.out.print("Player"+dlist.get(j).getID()+" ");
            }
        }
       
        System.out.println();

        while(maf>0 && maf!=det+hea+com ){
        System.out.println("Round"+round);
        System.out.print(all.size()+" "+"players are remaining: ");
        for(int i =0; i<all.size(); i++){
            System.out.print("Player"+all.get(i).getID()+",");
        }
        System.out.print("are alive.");
        System.out.println();


        //choosing mafia target 
        int target=0; //mafia target ID
        int ind=rand.nextInt(all.size()); //choosing random index 
        boolean done=false;
        while(!done){
            ind=rand.nextInt(all.size());
            for(int i =0; i<all.size(); i++){
                if(all.get(i).getID()==ind && !all.get(i).equals(new Mafia())){
                    done=true;
                    System.out.print("Mafias have chosen their target");
                }
            }
            if(done){
                break;
            }
        }
        target=all.get(ind).getID();
        System.out.println();

        //calculating mafias total HP
        float totmafiaHP=0;
        for(int i =0; i<all.size(); i++){
            if(all.get(i).getType().equals("Mafia")){
                totmafiaHP=totmafiaHP+all.get(i).getHP();
            }
        }
        //no of alive mafias whose HP>0
        int Y=0;
        for(int i =0; i<all.size(); i++){
            if(all.get(i).getType().equals("Mafia") && all.get(i).getHP()>0){
                Y++;
            }
        }

        //HP reducing/increasing ) and damages taken by mafias 
        float X;
        X=0;
        for(int i =0; i<all.size(); i++){
            if(all.get(i).getID()==target && totmafiaHP>=all.get(i).getHP()){
                X=all.get(i).getHP(); //initial HP of target
                all.get(i).setHP(0);
            }
            else if(all.get(i).getID()==target && totmafiaHP<all.get(i).getHP()){
                X=all.get(i).getHP();
                all.get(i).setHP(all.get(i).getHP()-totmafiaHP);
            }
        }

        //damages 
        float rem;
        rem=0;
        for(int i =0; i<all.size(); i++){
            if(all.get(i).getType().equals("Mafia")){
                if((X/Y)<=all.get(i).getHP()){
                    all.get(i).setHP(all.get(i).getHP()-(float)(X/Y -rem));
                }
                else if((X/Y)>all.get(i).getHP()){
                    rem=(X/Y)-all.get(i).getHP();
                }
            }
        }
        
        //detective testing 
        int test=0;
        if(det>0 ){
            System.out.println("Choose a player to test:");
            test=s.nextInt(); //choosing ID of player 
        }
        boolean done2=false;
        if(det>0){
            while(!done2){
                test=s.nextInt();
                for(int i =0; i<all.size(); i++){
                    if(all.get(i).getID()==test && !all.get(i).getType().equals("Detective")){
                        done2=true;
                    }
                }
            }
        }
        

        for(int i=0; i<all.size(); i++){
            //checkin if mafia or not 
            if(all.get(i).getID()==test && all.get(i).getType().equals("Mafia")){
                System.out.println("Player"+test+" is a Mafia");
                break;
            }
            else if(all.get(i).getID()==test && ! all.get(i).getType().equals("Mafia")){
                System.out.println("Player"+test+" is not a Mafia");
                break;
            }
        }
        //choosing player to heal(can choose anyone) if healer(s) left
        if(hea>0){
            int healind=rand.nextInt(all.size());
            int healID=all.get(healind).getID(); //player ID of person chosen to heal
            all.get(healind).setHP(all.get(healind).getHP()+500);
            System.out.println("Healers have chosen someone to heal");
        }
        System.out.println("--End of Actions--");

        //player dies condition(if HP==0 of target even after healing process  )
        for(int i =0; i<all.size(); i++){
            if(all.get(i).getID()==target && all.get(i).getHP()==0){
                System.out.println("Player"+all.get(i).getID()+" has died.");
                //all.remove(i); //removing died player from list 
                
                if(all.get(i).getType().equals("Mafia")){
                    maf--;
                    
                }
                else if(all.get(i).getType().equals("Detective")){
                    det--;
                   
                }
                else if(all.get(i).getType().equals("Healer")){
                    hea--;
                    
                }
                else if(all.get(i).getType().equals("Commoner")){
                    com--;
                   
                }
                all.remove(i);
            }
            else if(all.get(i).getID()==target && all.get(i).getHP()>0){
                System.out.println("No one died");
                break;
            }
        }

        //checking if voting possible or not
        int flag=0; //change to 1 if mafia detected by detective 
        for(int i =0; i<all.size(); i++){
            if(all.get(i).getID()==test && all.get(i).getType().equals("Mafia")){
                //no voting , voted out by default 
                if(all.get(i).getID()==userID){
                    userstat=false;
                }
                flag=1;
                System.out.println("Player"+all.get(i).getID()+ " has been voted out");
                all.remove(i);
                maf--;
                break;
            }
            else if(all.get(i).getID()==test && !all.get(i).getType().equals("Mafia")){
                flag=0;
                break;
            }
        }

        //voting if flag==0 ,not if flag== 1
        int ch;
        if(flag==0){
            for(int i=0; i<all.size(); i++){
                //choosing user opinion to vote out if user still alive
                if(all.get(i).getID()==userID){
                    int vote=0;
                    while(vote==0){
                        System.out.println("Select a person to vote out: ");
                        vote =s.nextInt();
                        if(vote==userID){
                            vote=0;
                            continue;
                        }
                    }
                    all.get(i).setVote(vote);
                    votes.add(all.get(i).getVote());
                }
                //random voting by rest 
                else if(all.get(i).getID()!=userID){
                    ch=rand.nextInt(all.size());
                    all.get(i).setVote(all.get(ch).getID()); //check this line r
                    votes.add(all.get(i).getVote());

                }
            }
            int maxv=maxvotes(); //ID of person with max votes
            for(int i=0; i<all.size(); i++){
                if(all.get(i).getID()==maxv){
                    System.out.println("Player"+maxv+" has been voted out.");

                    if(all.get(i).getType().equals("Mafia")){
                       
                        maf--;
                       
                    }
                    else if(all.get(i).getType().equals("Detective")){
                       
                        det--;
                       
                    }
                    else if(all.get(i).getType().equals("Healer")){
                       
                        hea--;
                        
                    }
                    else if(all.get(i).getType().equals("Commoner")){
                        
                        com--;
                        
                    }
                    all.remove(i);

                }
            }
        }
        System.out.println("End of Round"+round);
        votes=new genlist<Integer>(); //clearing previous votes after end of round 
        hm=new HashMap<Integer, Integer>(); //clearing max frequency votes after end of round 
        round++;
    }
    System.out.println("Game Over.");
    //mafias lose
    if(maf==0){
        System.out.println("The Mafias have lost");
    }
    //mafias win
    else{
        System.out.println("The Mafias have won");
    }

    System.out.print("Mafias were: ");
    for(int i =0; i<mlist.size(); i++){
        System.out.print("Player"+mlist.get(i).getID()+" ");
    }
    System.out.println();
    System.out.print("Detectives were: ");
    for(int i =0; i<dlist.size(); i++){
        System.out.print("Player"+dlist.get(i).getID()+" ");
    }
    System.out.println();
    System.out.print("Healers were: ");
    for(int i =0; i<hlist.size(); i++){
        System.out.print("Player"+hlist.get(i).getID()+" ");
    }
    System.out.println();
    System.out.print("Commoners were: ");
    for(int i =0; i<clist.size(); i++){
        System.out.print("Player"+clist.get(i).getID()+" ");
    }

    }

    public static void userH(){ //if user is healer
        System.out.println("You are a "+h);
        System.out.print("Other Healers are: ");
        for(int j =0; j<hlist.size(); j++){
            if(hlist.get(j).getType().equals("Healer") && hlist.get(j).getID()!=userID){
                System.out.print("Player"+hlist.get(j).getID()+" ");
            }
        }
        
       
        System.out.println();

        while(maf>0 && maf!=det+hea+com ){
        System.out.println("Round"+round);
        System.out.print(all.size()+" "+"players are remaining: ");
        for(int i =0; i<all.size(); i++){
            System.out.print("Player"+all.get(i).getID()+",");
        }
        System.out.print("are alive.");
        System.out.println();

        
        //choosing mafia target 
        int target=0; //mafia target ID
        int ind=rand.nextInt(all.size()); //choosing random index 
        boolean done=false;
        while(!done){
            ind=rand.nextInt(all.size());
            for(int i =0; i<all.size(); i++){
                if(all.get(i).getID()==ind && !all.get(i).equals(new Mafia())){
                    done=true;
                    System.out.print("Mafias have chosen their target");
                }
            }
            if(done){
                break;
            }
        }
        target=all.get(ind).getID();
        System.out.println();

        //calculating mafias total HP
        float totmafiaHP=0;
        for(int i =0; i<all.size(); i++){
            if(all.get(i).getType().equals("Mafia")){
                totmafiaHP=totmafiaHP+all.get(i).getHP();
            }
        }
        //no of alive mafias whose HP>0
        int Y=0;
        for(int i =0; i<all.size(); i++){
            if(all.get(i).getType().equals("Mafia") && all.get(i).getHP()>0){
                Y++;
            }
        }

        //HP reducing/increasing ) and damages taken by mafias 
        float X=0;
        for(int i =0; i<all.size(); i++){
            if(all.get(i).getID()==target && totmafiaHP>=all.get(i).getHP()){
                X=all.get(i).getHP(); //initial HP of target
                all.get(i).setHP(0);
            }
            else if(all.get(i).getID()==target && totmafiaHP<all.get(i).getHP()){
                X=all.get(i).getHP();
                all.get(i).setHP(all.get(i).getHP()-totmafiaHP);
            }
        }

        //damages 
        float rem=0;
        for(int i =0; i<all.size(); i++){
            if(all.get(i).getType().equals("Mafia")){
                if((X/Y)<=all.get(i).getHP()){
                    all.get(i).setHP(all.get(i).getHP()-(float)(X/Y -rem));
                }
                else if((X/Y)>all.get(i).getHP()){
                    rem=(X/Y)-all.get(i).getHP();
                }
            }
        }
   
        //detective testing
        int test=0; //index random choosing 
        if(det>0){
            test=rand.nextInt(all.size());
            //System.out.println("Detectives have chosen a player to test.");
        }
        boolean done2=false;
        if(det>0){
            while(!done2){
                test=rand.nextInt(all.size());
                for(int i =0; i<all.size(); i++){
                    if(all.get(i).getID()==all.get(test).getID() && all.get(i).getType().equals("Detective")){
                    //System.out.println("You cannot test a detective");
                    done2=true;
                    System.out.println("Detectives have chosen a player to test.");
                    }
                }
            }
        }
                

        for(int i=0; i<all.size(); i++){
                //checkin if mafia or not 
            if(all.get(i).getID()==all.get(test).getID() && all.get(i).getType().equals("Mafia")){
                    //System.out.println("Player"+test+" is a Mafia");
                break;
            }
            else if(all.get(i).getID()==all.get(test).getID() && ! all.get(i).getType().equals("Mafia")){
                    //System.out.println("Player"+test+" is not a Mafia");
                break;
            
            }
        }

        //choosing player to heal(can choose anyone) if healer(s) left
        if(hea>0 && userstat==true){
            System.out.println("Choose a player to heal");
            int healID=s.nextInt(); //player ID of person chosen 
            for(int i =0; i<all.size(); i++){
                if(all.get(i).getID()==healID){
                    all.get(i).setHP(all.get(i).getHP()+500);
                    break;
                }
            }
        }
        else if(hea>0 && userstat==false){
            int healind=rand.nextInt(all.size());
            int healID=all.get(healind).getID(); //player ID
            all.get(healind).setHP(all.get(healind).getHP()+500);
            System.out.println("Healers have chosen someone to heal");
        }

        System.out.println("--End of Actions--");

        //player dies condition(if HP==0 of target even after healing process  )
        for(int i =0; i<all.size(); i++){
            if(all.get(i).getID()==target && all.get(i).getHP()==0){
                System.out.println("Player"+all.get(i).getID()+" has died.");
                //all.remove(i); //removing died player from list 

                if(all.get(i).getType().equals("Mafia")){
                    maf--;
                    
                }
                else if(all.get(i).getType().equals("Detective")){
                    det--;
                 
                }
                else if(all.get(i).getType().equals("Healer")){
                    hea--;
                    
                }
                else if(all.get(i).getType().equals("Commoner")){
                    com--;
                    
                }
                all.remove(i);
            }
            else if(all.get(i).getID()==target && all.get(i).getHP()>0){
                System.out.println("No one died");
                break;
            }
        }

        //checking if voting possible or not 
        int flag=0; //change to 1 if mafia detected by detective 
        for(int i =0; i<all.size(); i++){
            if(all.get(i).getID()==all.get(test).getID() && all.get(i).getType().equals("Mafia")){
                //no voting , voted out by default
                if(all.get(i).getID()==userID){
                    userstat=false;
                }
                flag =1;
                System.out.println("Player"+all.get(i).getID()+ " has been voted out");
                all.remove(i);
                maf--;
                break;
            }
            else if(all.get(i).getID()==all.get(test).getID() && !all.get(i).getType().equals("Mafia")){
                flag=0;
                break;
            }
        }

        //voting if flag==0 ,not if flag== 1
        int ch;
        if(flag==0){
            for(int i=0; i<all.size(); i++){
                //choosing user opinion to vote out if user still alive
                if(all.get(i).getID()==userID){
                    int vote=0;
                    while(vote==0){
                        System.out.println("Select a person to vote out: ");
                        vote =s.nextInt();
                        if(vote==userID){
                            vote=0;
                            continue;
                        }
                    }
                    all.get(i).setVote(vote);
                    votes.add(all.get(i).getVote());
                }
                //random voting by rest 
                else if(all.get(i).getID()!=userID){
                    ch=rand.nextInt(all.size());
                    all.get(i).setVote(all.get(ch).getID()); //check this line r
                    votes.add(all.get(i).getVote());

                }
            }
            int maxv=maxvotes(); //ID of person with max votes
            for(int i=0; i<all.size(); i++){
                if(all.get(i).getID()==maxv){
                    System.out.println("Player"+maxv+" has been voted out.");

                    if(all.get(i).getType().equals("Mafia")){
                       
                        maf--;
                        
                    }
                    else if(all.get(i).getType().equals("Detective")){
                        
                        det--;
                      
                    }
                    else if(all.get(i).getType().equals("Healer")){
                      
                        hea--;
                       
                    }
                    else if(all.get(i).getType().equals("Commoner")){
                       
                        com--;
                       
                    }
                    all.remove(i);

                }
            }
        }
        System.out.println("End of Round"+round);
        votes=new genlist<Integer>(); //clearing previous votes after end of round 
        hm=new HashMap<Integer, Integer>(); //clearing max frequency votes after end of round 
        round++;
    }

    System.out.println("Game Over.");
    //mafias lose
    if(maf==0){
        System.out.println("The Mafias have lost");
    }
    //mafias win
    else{
        System.out.println("The Mafias have won");
    }

    System.out.print("Mafias were: ");
    for(int i =0; i<mlist.size(); i++){
        System.out.print("Player"+mlist.get(i).getID()+" ");
    }
    System.out.println();
    System.out.print("Detectives were: ");
    for(int i =0; i<dlist.size(); i++){
        System.out.print("Player"+dlist.get(i).getID()+" ");
    }
    System.out.println();
    System.out.print("Healers were: ");
    for(int i =0; i<hlist.size(); i++){
        System.out.print("Player"+hlist.get(i).getID()+" ");
    }
    System.out.println();
    System.out.print("Commoners were: ");
    for(int i =0; i<clist.size(); i++){
        System.out.print("Player"+clist.get(i).getID()+" ");
    }

    }

    public static void userC(){ //if user is commoner 
        System.out.println("You are a "+c);
        System.out.print("Other Commoners are: ");
        for(int j =0; j<clist.size(); j++){
            if(clist.get(j).getType().equals("Commoner") && clist.get(j).getID()!=userID){
                System.out.print("Player"+clist.get(j).getID()+" ");
            }
        }
        
        System.out.println();

        while(maf>0 && maf!=det+hea+com ){
        System.out.println("Round"+round);
        System.out.print(all.size()+" "+"players are remaining: ");
        for(int i =0; i<all.size(); i++){
            System.out.print("Player"+all.get(i).getID()+",");
        }
        System.out.print("are alive.");
        System.out.println();

        //choosing mafia target 
        int target=0; //mafia target ID
        int ind=rand.nextInt(all.size()); //choosing random index 
        boolean done=false;
        while(!done){
            ind=rand.nextInt(all.size());
            for(int i =0; i<all.size(); i++){
                if(all.get(i).getID()==ind && !all.get(i).equals(new Mafia())){
                    done=true;
                    System.out.print("Mafias have chosen their target");
                    
                }
            }
            if(done){
                break;
            }
        }
        target=all.get(ind).getID();

        System.out.println();

        //calculating mafias total HP
        float totmafiaHP=0;
        for(int i =0; i<all.size(); i++){
            if(all.get(i).getType().equals("Mafia")){
                totmafiaHP=totmafiaHP+all.get(i).getHP();
            }
        }
        //no of alive mafias whose HP>0
        int Y=0;
        for(int i =0; i<all.size(); i++){
            if(all.get(i).getType().equals("Mafia") && all.get(i).getHP()>0){
                Y++;
            }
        }

        //HP reducing/increasing ) and damages taken by mafias 
        float X=0;
        for(int i =0; i<all.size(); i++){
            if(all.get(i).getID()==target && totmafiaHP>=all.get(i).getHP()){
                X=all.get(i).getHP(); //initial HP of target
                all.get(i).setHP(0);
            }
            else if(all.get(i).getID()==target && totmafiaHP<all.get(i).getHP()){
                X=all.get(i).getHP();
                all.get(i).setHP(all.get(i).getHP()-totmafiaHP);
            }
        }

        //damages 
        float rem=0;
        for(int i =0; i<all.size(); i++){
            if(all.get(i).getType().equals("Mafia")){
                if((X/Y)<=all.get(i).getHP()){
                    all.get(i).setHP(all.get(i).getHP()-(float)(X/Y -rem));
                }
                else if((X/Y)>all.get(i).getHP()){
                    rem=(X/Y)-all.get(i).getHP();
                }
            }
        }
                
        //detective testing 
        int test=0; //index random choosing 
        if(det>0){
            test=rand.nextInt(all.size());
            //System.out.println("Detectives have chosen a player to test.");
        }
        boolean done2=false;
        if(det>0){
            while(!done2){
                test=rand.nextInt(all.size());
                for(int  i=0; i<all.size(); i++){
                    if(all.get(i).getID()==all.get(test).getID() && !all.get(i).getType().equals("Detective")){
                        done2=true;
                        System.out.println("Detectives have chosen a player to test.");
                    }
                }
            }
        }





        for(int i=0; i<all.size(); i++){
                //checkin if mafia or not 
            if(all.get(i).getID()==all.get(test).getID() && all.get(i).getType().equals("Mafia")){
                    //System.out.println("Player"+test+" is a Mafia");
                    
                break;
            }
            else if(all.get(i).getID()==all.get(test).getID() && ! all.get(i).getType().equals("Mafia")){
                    //System.out.println("Player"+test+" is not a Mafia");
                    
                break;
            }
        }
        //choosig player to heal if healers left 
        if(hea>0){
            int healind=rand.nextInt(all.size());
            int healID=all.get(healind).getID(); //player ID
            all.get(healind).setHP(all.get(healind).getHP()+500);
            System.out.println("Healers have chosen someone to heal");
        }
        System.out.println("--End of Actions--");

        //player dies condition(if HP==0 of target even after healing process  )
        for(int i =0; i<all.size(); i++){
            if(all.get(i).getID()==target && all.get(i).getHP()==0){
                System.out.println("Player"+all.get(i).getID()+" has died.");
                //all.remove(i); //removing died player from list 
                
                if(all.get(i).getType().equals("Mafia")){
                    maf--;
                   
                }
                else if(all.get(i).getType().equals("Detective")){
                    det--;
                    
                }
                else if(all.get(i).getType().equals("Healer")){
                    hea--;
                    
                }
                else if(all.get(i).getType().equals("Commoner")){
                    com--;
                   
                }
                all.remove(i);
            }
            else if(all.get(i).getID()==target && all.get(i).getHP()>0){
                System.out.println("No one died");
                break;
            }
        }

        //checking if voting possible or not 
        int flag=0; //change to 1 if mafia detected by detective 
        for(int i =0; i<all.size(); i++){
            if(all.get(i).getID()==all.get(test).getID() && all.get(i).getType().equals("Mafia")){
                //no voting , voted out byn default
                if(all.get(i).getID()==userID){
                    userstat=false;
                }
                flag =1;
                System.out.println("Player"+all.get(i).getID()+ " has been voted out");
                all.remove(i);
                maf--;
                break;
            }
            else if(all.get(i).getID()==all.get(test).getID() && !all.get(i).getType().equals("Mafia")){
                flag=0;
                break;
            }
        }

        int ch;
        if(flag==0){
            for(int i =0; i<all.size(); i++){
                //choosing user opinion to vote out if user alive 
                if(all.get(i).getID()==userID){
                    int vote=0;
                    while(vote==0){
                        System.out.println("Select a person to vote out:");
                        vote =s.nextInt();
                        if(vote==userID){
                            vote=0;
                            continue;
                        }
                    }
                    all.get(i).setVote(vote);
                    votes.add(all.get(i).getVote());
                }
                //random voting by rest 
                else if(all.get(i).getID()!=userID){
                    ch=rand.nextInt(all.size());
                    all.get(i).setVote(all.get(ch).getID());
                    votes.add(all.get(i).getVote());

                }
            }
            int maxv=maxvotes(); //ID of person with max votes 
            for(int i =0; i<all.size(); i++){
                if(all.get(i).getID()==maxv){
                    System.out.println("Player"+maxv+" has been voted out.");

                    if(all.get(i).getType().equals("Mafia")){
                        
                        maf--;
                       
                    }
                    else if(all.get(i).getType().equals("Detective")){
                        
                        det--;
                        
                    }
                    else if(all.get(i).getType().equals("Healer")){
                       
                        hea--;
                       
                    }
                    else if(all.get(i).getType().equals("Commoner")){
                        
                        com--;
                       
                    }
                    all.remove(i);

                }
            }
        }
        System.out.println("End of Round"+round);
        votes=new genlist<Integer>(); //clearing previous votes after end of round 
        hm=new HashMap<Integer, Integer>(); //clearing max frequency votes after end of round 
        round++;
    }
    System.out.println("Game Over.");
    //mafias lose
    if(maf==0){
        System.out.println("The Mafias have lost");
    }
    //mafias win
    else{
        System.out.println("The Mafias have won");
    }

    System.out.print("Mafias were: ");
    for(int i =0; i<mlist.size(); i++){
        System.out.print("Player"+mlist.get(i).getID()+" ");
    }
    System.out.println();
    System.out.print("Detectives were: ");
    for(int i =0; i<dlist.size(); i++){
        System.out.print("Player"+dlist.get(i).getID()+" ");
    }
    System.out.println();
    System.out.print("Healers were: ");
    for(int i =0; i<hlist.size(); i++){
        System.out.print("Player"+hlist.get(i).getID()+" ");
    }
    System.out.println();
    System.out.print("Commoners were: ");
    for(int i =0; i<clist.size(); i++){
        System.out.print("Player"+clist.get(i).getID()+" ");
    }

    }

    //for max votes fucntion 
    public static int maxvotes(){
        for(int i =0; i<votes.size(); i++){
            int key=votes.get(i);
            if(hm.containsKey(key)){
                int freq=hm.get(key);
                freq++;
                hm.put(key, freq);
            }
            else {
                hm.put(key, 1 );
            }
        }
        int maxvote=0;
        int res=-1; //max freq element 
        for(Entry<Integer, Integer> val:hm.entrySet()){
            if(maxvote<val.getValue()){
                res=val.getKey();
                maxvote=val.getValue();
            }
        }
        return res;
    }
    //wildcard
    public static void print(ArrayList<?> list){
        for(Object o: list){
            System.out.println(o);
        }
    }

}

interface Comparator<T> {
    public float compare(T first, T second);

}
abstract class MafiaComp implements Comparator<Mafia>{
    @Override 
    public float compare(Mafia m1, Mafia m2){
        return m1.getID()-m2.getID();
    }

}

//generic programming
class genlist <T> {
    private ArrayList <T> mylist;
    public genlist() {
        mylist = new ArrayList<T>();
    }
    public void add(T o) {
        mylist.add(o);
    }
    public T get(int i) {
        return mylist.get(i);
    }
    public int size(){
        return mylist.size();
    }
    public void remove(int i){
        mylist.remove(i);
    }

}

//abstract class
abstract class Player {
   
    public boolean equals(Object o) {
        return this.getClass()==o.getClass();
    }
    public Player(){

    }
    public abstract void setType(String type);
    public abstract String getType();
    public abstract void setID(int ID);
    public abstract int getID();
    public abstract void setHP(float  HP);
    public abstract float  getHP();
    public abstract void setVote(int vote);
    public abstract int getVote();

}
class Mafia extends Player {
    private int ID;
    private String type;
    private float HP;
    private int vote;

    public Mafia(){

    }
    public Mafia(int ID, String type, float  HP, int vote){
        this.ID=ID;
        this.type="Mafia";
        this.HP=2500;
        this.vote=vote;
    }
    @Override
    public void setID(int ID){
        this.ID=ID;
    }
    @Override
    public int getID(){
        return this.ID;
    }
    @Override
    public void setType(String type){
        this.type=type;
    }
    @Override 
    public String getType(){
        return this.type;
    }
    @Override 
    public void setHP(float HP){
        this.HP=HP;
    }
    @Override 
    public float getHP(){
        return this.HP;
    }
    @Override 
    public void setVote(int vote){
        this.vote=vote;
    }
    @Override 
    public int getVote(){
        return this.vote;
    }
    //overriding tostring method of java 
    public String toString(){
        return type;
    }


}
class Detective extends Player {
    private int ID;
    private String type;
    private float  HP;
    private int vote;

    public Detective(){

    }
    public Detective(int ID, String type, float HP, int vote){
        this.ID=ID;
        this.type="Detective";
        this.HP=800;
        this.vote=vote;
    }
    @Override
    public void setID(int ID){
        this.ID=ID;
    }
    @Override
    public int getID(){
        return this.ID;
    }
    @Override 
    public void setType(String type){
        this.type=type;
    }
    @Override 
    public String getType(){
        return this.type;
    }
    @Override 
    public void setHP(float  HP){
        this.HP=HP;
    }
    @Override 
    public float  getHP(){
        return this.HP;
    }
    @Override 
    public void setVote(int vote){
        this.vote=vote;
    }
    @Override 
    public int getVote(){
        return this.vote;
    }
    //overriding tostring method of java 
    public String toString(){
        return type;
    }

}
class Healer extends Player {
    private int ID;
    private String type;
    private float HP;
    private int vote;

    public Healer(){

    }
    public Healer(int ID, String type, float HP, int vote){
        this.ID=ID;
        this.type="Healer";
        this.HP=800;
        this.vote=vote;
    }
    @Override
    public void setID(int ID){
        this.ID=ID;
    }
    @Override
    public int getID(){
        return this.ID;
    }
    @Override 
    public void setType(String type){
        this.type=type;
    }
    @Override
    public String getType(){
        return this.type;
    }
    @Override
    public void setHP(float  HP){
        this.HP=HP;
    }
    @Override
    public float  getHP(){
        return this.HP;
    }
    @Override
    public void setVote(int vote){
        this.vote=vote;
    }
    @Override
    public int getVote(){
        return this.vote;
    }
    //overriding tostring method of java 
    public String toString(){
        return type;
    }
}

class Commoner extends Player {
    private int ID;
    private String type;
    private float HP;
    private int vote;

    public Commoner(){

    }
    public Commoner(int ID, String type, float HP, int vote){
        this.ID=ID;
        this.type="Commoner";
        this.HP=1000;
        this.vote=vote;
    }
    @Override
    public void setID(int ID){
        this.ID=ID;
    }
    @Override
    public int getID(){
        return this.ID;
    }
    @Override
    public void setType(String type){
        this.type=type;
    }
    @Override
    public String getType(){
        return this.type;
    }
    @Override
    public void setHP(float HP){
        this.HP=HP;
    }
    @Override
    public float  getHP(){
        return this.HP;
    }
    @Override
    public void setVote(int vote){
        this.vote=vote;
    }
    @Override
    public int getVote(){
        return this.vote;
    }
    public String toString(){
        return type;
    }
}
