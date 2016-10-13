#include "handrank.h"
#include <stdio.h>
HandRank::HandRank(int pl){
  flopHandSize=FlopHandSize();
  deck=Deck(pl);
  hand=std::vector<std::pair<int,int> >(5);
  flopWinners=std::vector<int>(flopHandSize,0);
  flopAll=std::vector<int>(flopHandSize,0);
}
int HandRank::PrintHand(){
    deck.Permute();
    deck.PrintHand(0);
  return 0;
}
int HandRank::CreateHandMap(){
  deck.HandMap();
  return 0;
}
///////////////////////// Stat table////////////////////////////////////////////
// (0,0)  (0,1)s  (0,2)s  (0,3)s  (0,4)s  (0,5)s  (0,6)s  (0,7)s  (0,8)s  (0,9)s  (0,10)s  (0,11)s  (0,12)s
// (1,0)  (1,1)   (1,2)s  (1,3)s  (1,4)s  (1,5)s  (1,6)s  (1,7)s  (1,8)s  (1,9)s  (1,10)s  (1,11)s  (1,12)s
// (2,0)  (2,1)   (2,2)   (2,3)s  (2,4)s  (2,5)s  (2,6)s  (2,7)s  (2,8)s  (2,9)s  (2,10)s  (2,11)s  (2,12)s
// (3,0)  (3,1)   (3,2)   (3,3)   (3,4)s  (3,5)s  (3,6)s  (3,7)s  (3,8)s  (3,9)s  (3,10)s  (3,11)s  (3,12)s
// (4,0)  (4,1)   (4,2)   (4,3)   (4,4)   (4,5)s  (4,6)s  (4,7)s  (4,8)s  (4,9)s  (4,10)s  (4,11)s  (4,12)s
// (5,0)  (5,1)   (5,2)   (5,3)   (5,4)   (5,5)   (5,6)s  (5,7)s  (5,8)s  (5,9)s  (5,10)s  (5,11)s  (5,12)s
// (6,0)  (6,1)   (6,2)   (6,3)   (6,4)   (6,5)   (6,6)   (6,7)s  (6,8)s  (6,9)s  (6,10)s  (6,11)s  (6,12)s
// (7,0)  (7,1)   (7,2)   (7,3)   (7,4)   (7,5)   (7,6)   (7,7)   (7,8)s  (7,9)s  (7,10)s  (7,11)s  (7,12)s
// (8,0)  (8,1)   (8,2)   (8,3)   (8,4)   (8,5)   (8,6)   (8,7)   (8,8)   (8,9)s  (8,10)s  (8,11)s  (8,12)s
// (9,0)  (9,1)   (9,2)   (9,3)   (9,4)   (9,5)   (9,6)   (9,7)   (9,8)   (9,9)   (9,10)s  (9,11)s  (9,12)s
// (10,0) (10,1)  (10,2)  (10,3)  (10,4)  (10,5)  (10,6)  (10,7)  (10,8)  (10,9)  (10,10)  (10,11)s (10,12)s
// (11,0) (11,1)  (11,2)  (11,3)  (11,4)  (11,5)  (11,6)  (11,7)  (11,8)  (11,9)  (11,10)  (11,11)  (11,12)s
// (12,0) (12,1)  (12,2)  (12,3)  (12,4)  (12,5)  (12,6)  (12,7)  (12,8)  (12,9)  (12,10)  (12,11)  (12,12)
int HandRank::BuildStat(){
  for(int numPl=2;numPl<3;numPl++){
    deck.numPlayers=numPl;
    printf("Number of Players: %d\n",numPl);
    for(int i=0;i<13;i++){
      for(int j=0;j<13;j++){
	stat[i][j]=0;
	count[i][j]=0;
      }
    }
    long long iter=1000000000;  
    for(long long it=0;it<iter;it++){
      // printf("iteration %ld\n",it);
      deck.Permute();
      deck.Winners();//deck.Winners()
      for(int i=0;i<deck.numPlayers;i++){
        short first=deck.playerHands[i].first;
	short second=deck.playerHands[i].second;
	int index=HandAndFlopIndex(i);
	if(index==32850720)
	  deck.PrintAll();
	flopAll[index]++;
	bool sute=false;
	if(first/13==second/13)
	  sute=true;
	short Fdesuted=first%13;
	short Sdesuted=second%13;
	if(sute)
	  Fdesuted<Sdesuted?count[Fdesuted][Sdesuted]++:count[Sdesuted][Fdesuted]++;
	else
	  Fdesuted>=Sdesuted?count[Fdesuted][Sdesuted]++:count[Sdesuted][Fdesuted]++;
	if(deck.winners[i]==1){
	  if(sute)
	    Fdesuted<Sdesuted?stat[Fdesuted][Sdesuted]++:stat[Sdesuted][Fdesuted]++;
	  else
	    Fdesuted>=Sdesuted?stat[Fdesuted][Sdesuted]++:stat[Sdesuted][Fdesuted]++;
	  flopWinners[index]++;
	}
      }
    }
    float sum=0;
    for(int i=0;i<13;i++){
      for(int j=0;j<13;j++){
	if(count[i][j])
	  stat[i][j]/=count[i][j];
	else
	  stat[i][j]=0;
	printf(" %2.0f",100*stat[i][j]);
      }
      printf("\n");
    }
    printf("\n");
  }
   int cnt=0;
   for(int i=0;i<flopHandSize;i++){
     if(flopAll[i]){
       cnt++;
       printf("%d out of 58513,%d,%d,%d,%g\n",cnt,i,flopAll[i],flopWinners[i],((float)flopWinners[i])/flopAll[i]);
     }
   }
}
struct sort_pred {
    bool operator()(const std::pair<int,int> &left, const std::pair<int,int> &right) {
        return left.first > right.first;
    }
};
int HandRank::FlashMap(){// total 16 different values
  int val=0; // for example if three of a kind 10101 means first highest third and last are same sute
  for(int i=0;i<5;i++){
    int one=1;
    if(hand[i].second==flashChance){
      one<<=(4-i);
      val+=one;
    }
  }
  val<<=20;// first 20 bits used
  // int index=-1;
  // if(val==31) //11111
  //   index=0;
  
  // else if(val==30) //11110
  //   index=1;
  // else if(val==29) //11101
  //   index=2;
  // else if(val==27) //11011
  //   index=3;
  // else if(val==23) //10111
  //   index=4;
  // else if(val==15) //01111
  //   index=5;

  // else if(val==7)  //00111
  //   index=6;
  // else if(val==14) //01110
  //   index=7;
  // else if(val==28) //11100
  //   index=8;
  // else if(val==19) //10011
  //   index=9;
  // else if(val==22) //10110
  //   index=10;
  // else if(val==25) //11001
  //   index=11;
  // else if(val==21) //10101
  //   index=12;
  // else if(val==11) //01011
  //   index=13;
  // else if(val==26) //11010
  //   index=14;
  // else if(val==13) //01101
  //   index=15;
  // else
  //   printf("error this val incorrect %d\n",val);
  return val;
}
int HandRank::HandAndFlopIndex(int i){
  int index=0;
  hand[0].first=deck.playerHands[i].first;
  hand[1].first=deck.playerHands[i].second;
  hand[2].first=deck.flop[0];
  hand[3].first=deck.flop[1];
  hand[4].first=deck.flop[2];
  for(int i=0;i<4;i++){
    tmpFlash[i]=0;
  }
  flashChance=0;
  for(int i=0;i<5;i++){
    tmpFlash[hand[i].first/13]++;
     if(tmpFlash[hand[i].first/13]==3)
      flashChance=hand[i].first/13; //flash is possible bit
     hand[i].second=hand[i].first/13;
     hand[i].first=hand[i].first%13;
  }
  std::sort(hand.begin(),hand.end(),sort_pred());
  if(flashChance)// for a given desuted hand there are max 16 possible suted possibilities
    index=FlashMap();
  for(int i=0;i<5;i++){
    int tmph=hand[i].first;
    tmph<<=(4-i)*4;
    index+=tmph;
  }
  return index;
}
int HandRank::TestFlopIndex(){ //58513 max number of hands
  deck.Permute();//to create playerHands
  for(int fi=0;fi<48;fi++){
    for(int se=fi+1;se<49;se++){
      for(int th=se+1;th<50;th++){
	for(int fo=th+1;fo<51;fo++){
	  for(int fv=fo+1;fv<52;fv++){
	    deck.playerHands[0].first=fv;
	    deck.playerHands[0].second=fo;
	    deck.flop[0]=th;
	    deck.flop[1]=se;
	    deck.flop[2]=fi;
	    int index=HandAndFlopIndex(0);
	    flopAll[index]++;
	  }
	}
      }
    }
  }
   int cnt=0;
   for(int i=0;i<flopHandSize;i++){
     if(flopAll[i]>0){
       cnt++;
       printf("%d,%d\n",cnt,flopAll[i]);
     }
   }
  return 0;
}
int HandRank::FlopHandSize(){
  int size=0;
  int tmp=31;//5 bits are used for sute 
  tmp<<=20;
  size+=tmp;
  for(int i=0;i<5;i++){
    tmp=12;
    tmp<<=(4-i)*4;
    size+=tmp;
  }
  size++;// to be able to access the highest index
  return size;
}
int HandRank::BuildSingleStat(){
  for(int numPl=2;numPl<11;numPl++){
    deck.numPlayers=numPl;
    printf("Number of Players: %d\n",numPl);
    for(int i=0;i<13;i++){
      for(int j=0;j<13;j++){
	stat[i][j]=0;
	count[i][j]=0;
      }
    }
    long long iter=1000;  
    for(long long it=0;it<iter;it++){
      // printf("iteration %ld\n",it);
      deck.FixOneHandPermute();
      deck.Winners();//deck.Winners()
      for(int i=0;i<1;i++){
        short first=deck.playerHands[i].first;
	short second=deck.playerHands[i].second;
	bool sute=false;
	if(first/13==second/13)
	  sute=true;
	short Fdesuted=first%13;
	short Sdesuted=second%13;
	if(sute)
	  Fdesuted<Sdesuted?count[Fdesuted][Sdesuted]++:count[Sdesuted][Fdesuted]++;
	else
	  Fdesuted>=Sdesuted?count[Fdesuted][Sdesuted]++:count[Sdesuted][Fdesuted]++;
	if(deck.winners[i]==1){
	  if(sute)
	    Fdesuted<Sdesuted?stat[Fdesuted][Sdesuted]++:stat[Sdesuted][Fdesuted]++;
	  else
	    Fdesuted>=Sdesuted?stat[Fdesuted][Sdesuted]++:stat[Sdesuted][Fdesuted]++;
	}
      }
    }
    float sum=0;
    for(int i=0;i<13;i++){
      for(int j=0;j<13;j++){
	if(count[i][j])
	  stat[i][j]/=count[i][j];
	else
	  stat[i][j]=0;
	printf(" %2.0f",100*stat[i][j]);
      }
      printf("\n");
    }
    printf("\n");
  }
  return 0;
}
int HandRank::PrintGame(){
  for(int i=0;i<1000;i++){
    deck.FixOneHandPermute();//Permute
    deck.Winners();
     deck.PrintAll();
  }
  //deck.HandPermute();
  // deck.PrintAll();
  return 0;
}
int HandRank::CheckDistrib(){
  double distr[52];
  for(int i=0;i<52;i++)
    distr[i]=0;
  int times=50000000;
  for(int i=0;i<times;i++){
    deck.Permute();
    char card=deck.GetCard(0);
    distr[card]++;
  }
  float sum=0;
  for(int i=0;i<52;i++){
    distr[i]=distr[i]/times;
    printf("%f\n",distr[i]);
    sum+=distr[i];
  }
  printf("%f\n",sum);
    return 0;
}
int HandRank::HandToIndex(){
  int index=0;
  int desutedCards[5]={5,4,3,2,0};
  int Sute[5]={1,1,1,1,1};
  int sute=0;
  for(int i=0;i<5;i++){
    int sutetmp=Sute[i];
    sutetmp<<=(4-i);
    sute+=sutetmp;
  }
  sute<<=20;
  index+=sute;
  for(int i=0;i<5;i++){
    int h=desutedCards[i];
    h<<=(4-i)*4;
    index+=h;
  }
  printf("%d\n",index);
  return 0;
}
