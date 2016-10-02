#include "handrank.h"
#include <stdio.h>
HandRank::HandRank(int pl){
  deck=Deck(pl);
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
  for(int numPl=2;numPl<11;numPl++){
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
  // for(int i=0;i<13;i++){
  //   for(int j=0;j<13;j++){
  //     double val=169*count[i][j]/(deck.numPlayers*iter);
  //     sum+=val;
  //     printf(" %1.4f",val);
  //   }
  //   printf("\n");
  // }
  //  printf(" %1.4f\n",sum/deck.numPlayers);
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
