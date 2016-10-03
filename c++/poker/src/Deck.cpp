#include <Deck.h>
#include <sstream>
Deck::Deck(char _numPlayers):numPlayers(_numPlayers){
    for(short i=0;i<52;i++)
      cards[i]=i;
    createCardsMap();
    bestHands=std::vector<std::vector<short> > (_numPlayers,std::vector<short>(5,-1)); //should be init to 0
    ranks=std::vector<short> (_numPlayers);
    handFlushCards=std::vector<short>(7);
    playerHands=std::vector<std::pair<short,short> > (_numPlayers,std::pair<short,short>(-1,-1));
    hand=std::vector<short>(7);
    size=133784560; // C(52,7)
}
short Deck::createCardsMap(){
  for(short i=0;i<52;i++){
    int num=i%13;
    char sute=i/13;
    std::string strVal;
    if(num<=8){
       std::ostringstream s;
       s << num+2;
       strVal=s.str();
     }
     else if(num==9){
       strVal = std::string("J");
     }
     else if(num==10){
       strVal = std::string("Q");
     }
     else if(num==11){
       strVal = std::string("K");
     }
     else if(num==12){
       strVal = std::string("A");
     }
     cardsMap.push_back(std::pair<std::string,char>(strVal,sute));
  }
  return 0;
}
short Deck::Permute(){
  river=-1;
  turn=-1;
  flop[0]=-1;
  flop[1]=-1;
  flop[2]=-1;
  short tmp;
   std::srand(std::time(0));
  for(short i=0;i<25;i++){//for 10 players 25 cards are used
    float rand=float(std::rand())/RAND_MAX;
    short index=rand*(52-i)+i;//rand*(52-i)+i;
    std::swap(cards[i],cards[index]);
     // tmp=cards[index];
     // cards[index]=cards[i];
     // cards[i]=tmp;
   }
   for(short i=0;i<numPlayers;i++){
     playerHands[i].first=cards[i*2];
     playerHands[i].second=cards[i*2+1];
   }
  flop[0]=cards[numPlayers*2];
  flop[1]=cards[numPlayers*2+1];
  flop[2]=cards[numPlayers*2+2];
  turn=cards[numPlayers*2+3];
  river=cards[numPlayers*2+4];
  return 0;
}
short Deck::FixOneHandPermute(){
  river=-1;
  turn=-1;
  flop[0]=-1;
  flop[1]=-1;
  flop[2]=-1;
  std::srand(std::time(0));
  for(short i=0;i<52;i++){
    float rand=float(std::rand())/RAND_MAX;
    short index=rand*(52-i)+i;
    std::swap(cards[i],cards[index]);
  }
  int cnt=0;
  for(short i=0;i<52;i++){ // create pair of duces for pl #1
    if(cards[i]%13==0){
      std::swap(cards[i],cards[cnt]);
      cnt++;
    }
    if(cnt==2)
      break;
  }
  for(short i=0;i<numPlayers;i++){
    playerHands[i].first=cards[i*2];
    playerHands[i].second=cards[i*2+1];
  }
  flop[0]=cards[numPlayers*2];
  flop[1]=cards[numPlayers*2+1];
  flop[2]=cards[numPlayers*2+2];
  turn=cards[numPlayers*2+3];
  river=cards[numPlayers*2+4];
  return 0;
}
/////////////////////////Cards Map/////////////////////////////////////
// \ S   H  D  C
//2  0   13 26 39 
//3  1   14 27 40
//4  2   15 28 41
//5  3   16 29 42
//6  4   17 30 43
//7  5   18 31 44
//8  6   19 32 45
//9  7   20 33 46
//10 8   21 34 47
//J  9   22 35 48
//Q  10  23 36 49
//K  11  24 37 50
//A  12  25 38 51
short Deck::HandPermute(){
  river=-1;
  turn=-1;
  flop[0]=-1;
  flop[1]=-1;
  flop[2]=-1;
  numPlayers=2;
  cards[0]=1;
  cards[1]=14;
  
  cards[2]=27;
  cards[3]=40;
  //flop and turn river
  cards[4]=10;
  cards[5]=9;
  cards[6]=8;
  cards[7]=21;
  cards[8]=50;
  
  
  
  for(short i=0;i<numPlayers;i++)
    playerHands[i]=std::pair<short,short>(cards[i*2],cards[i*2+1]);
  flop[0]=cards[numPlayers*2];
  flop[1]=cards[numPlayers*2+1];
  flop[2]=cards[numPlayers*2+2];
  turn=cards[numPlayers*2+3];
  river=cards[numPlayers*2+4];
  return 0;
}
short Deck::PrintHand(short i){
  printf("%d,%d\n",cards[2*i+0],cards[2*i+1]);
  return 0;
}
short Deck::PrintCard(short i){
  std::pair<std::string,char> card=cardsMap[i];
  printf("%s",card.first.c_str());
  char sute=card.second;
  if(sute==0)
    printf("\u2660");
  else if(sute==1)
    printf("\u2661");
  else if(sute==2)
    printf("\u2662");
  else if(sute==3)
    printf("\u2663");
  return 0;
}
short Deck::PrintAll(){
  
    for(short i=0;i<numPlayers;i++){
      std::pair<short,short> hand=playerHands[i];
      printf("Player%d: ",i);
      PrintCard(hand.first);
      printf(" ");
      PrintCard(hand.second);
      printf(" %d %d\n",ranks[i],winners[i]);
    }
    printf("Flop Turn & River: ");
    for(short i=0;i<3;i++){
      PrintCard(flop[i]);
      printf(" ");
    }
    PrintCard(turn);
    printf(" ");
    PrintCard(river);
    printf("\n");
  
  return 0;
}

std::pair<short,short> Deck::PlayerHand(short pl){
  if(pl<numPlayers)
    return playerHands[pl];
  else
    std::pair<short,short>(-1,-1); // this should not happend 
}
short Deck::ClasifyBoardFlush(){
  short sutes[4]={0,0,0,0};
  isFlushPossibleFlop=false;
  isFlushPossibleTurn=false;
  isFlushPossibleRiver=false;
  FlushSute=-1;
  for(int i=0;i<7;i++)
    FlashBoardCards[i]=-1;
  for(int i=0;i<3;i++){
    short s=flop[i]/13;
    sutes[s]++;
    if(flop[i]>=0 && sutes[s]==3){
      isFlushPossibleFlop=true;
      FlushSute=s;
    }
  }
  short s=turn/13;
  sutes[s]++;
  if(turn>=0 && (isFlushPossibleFlop || sutes[s]==3)){
    isFlushPossibleTurn=true;
    if(FlushSute==-1)
      FlushSute=s;
  }
  s=river/13;
  sutes[s]++;
  if(river>=0 &&  (isFlushPossibleTurn || sutes[s]==3)){
    isFlushPossibleRiver=true;
    if(FlushSute==-1)
      FlushSute=s;
  }
  NumFlashBrdCards=0;
  if(isFlushPossibleRiver){
    for(int i=0;i<3;i++)
      if(flop[i]/13==FlushSute){
	FlashBoardCards[i]=flop[i]%13;
	NumFlashBrdCards++;
      }
    if(turn/13==FlushSute){
      FlashBoardCards[3]=turn%13;
      NumFlashBrdCards++;
    }
    if(river/13==FlushSute){
      FlashBoardCards[4]=river%13;
      NumFlashBrdCards++;
    }
  }
  return 0;
}
short Deck::ClasifyBoardPairs(){
  for(int i=0;i<13;i++)
    boardCardsDeSuted[i]=0;
  for(int i=0;i<3;i++)
    boardCardsDeSuted[flop[i]%13]++;
  boardCardsDeSuted[turn%13]++;
  boardCardsDeSuted[river%13]++;
}
short Deck::Winners(){
  ClasifyBoardFlush();
  ClasifyBoardPairs();
  short maxRankArr[numPlayers];
  short maxRank=0;
  for(int i=0;i<numPlayers;i++){
    ranks[i]=0; // for repeated permutations needed
    RankHand(i);
    // RankHand(i);
    if(ranks[i]>=maxRank)
      maxRank=ranks[i];
  }
  short numberOfmax=0;
  for(int i=0;i<numPlayers;i++){
    if(ranks[i]==maxRank){
      maxRankArr[i]=1;
      numberOfmax++;
    }
    else
      maxRankArr[i]=0;
  }
  if(numberOfmax==1){
    for(int i=0;i<numPlayers;i++)
      winners[i]=maxRankArr[i];
  }
  else{
    int tmpWinners[numPlayers];
    int max=0;
    for(int i=0;i<numPlayers;i++){
      if(maxRankArr[i]==1){
	tmpWinners[i]=ConvertToInt(i);
	if(tmpWinners[i]>max)
	  max=tmpWinners[i];
      }
    }
    for(int i=0;i<numPlayers;i++){
      if(tmpWinners[i]==max)
	winners[i]=1;
      else
	winners[i]=0;
    }
  }
  return 0;
}
short Deck::WinnersFast(){
  int maxRank=0;
  int PlayerRank[numPlayers];
  long index,ind;
  for(int i=0;i<numPlayers;i++){
    PlayerRank[i]=0;
    hand[0]=cards[i*2+0];
     hand[1]=cards[i*2+1];;
     hand[2]=cards[numPlayers*2+0];
     hand[3]=cards[numPlayers*2+1];
     hand[4]=cards[numPlayers*2+2];
     hand[5]=cards[numPlayers*2+3];
     hand[6]=cards[numPlayers*2+4];
     std::sort(hand.begin(),hand.end());// hand always in ascending order to compute index correctly below
     index=HandToIndex();
     ind=index%size;
     for(int len=0;len<hashBinSizes[ind];len++){// max 15 is the length here
       if(hashFinalKey[ind][len]==index){
	 PlayerRank[i]=hashFinalValue[ind][len];
	 break;
       }
     }
     if(PlayerRank[i]>=maxRank)
       maxRank=PlayerRank[i];
  }
  for(int i=0;i<numPlayers;i++){
    if(PlayerRank[i]==maxRank)
      winners[i]=1;
    else
      winners[i]=0;
  }
  return 0;
}
int Deck::ConvertToInt(int h){
  int sum=0;
  for(int i=0;i<5;i++){
    int val=bestHands[h][i];
    val<<=(16-4*i);
    sum+=val;
  }
  return sum;
}
int Deck::SingleRank(int h){
  int sum=0;
  int rank=ranks[h];
  rank<<=20;
  sum+=rank;
  for(int i=0;i<5;i++){
    int val=bestHands[h][i];
    val<<=(16-4*i);
    sum+=val;
  }
  return sum;
}
short Deck::RankHand(int h){
  std::pair<short,short> hand=playerHands[h];
  short    numHandFlsh=NumFlashBrdCards;
  if(NumFlashBrdCards>=3){//check for flash
    for(int i=0;i<7;i++)
      handFlushCards[i]=FlashBoardCards[i];
    short sute=hand.first/13;
    if(sute==FlushSute){
      numHandFlsh++;
      handFlushCards[5]=hand.first%13;
    }
    sute=hand.second/13;
    if(sute==FlushSute){
      numHandFlsh++;
      handFlushCards[6]=hand.second%13;
    }
    if(numHandFlsh>=5){
      std::sort(handFlushCards.begin(),handFlushCards.end(),std::greater<short>());
      short seq=0;
      for(int j=0;j<5;j++){
	bestHands[h][j]=handFlushCards[j];
	if(j<4 && (handFlushCards[j]-1)==handFlushCards[j+1])
	  seq++;
      }
      if(bestHands[h][0]==12 && seq==4) //royal flush
	ranks[h]=10;
      else if(seq==4) //streight flush
	ranks[h]=9;
      else
	ranks[h]=6; // flush
    }
  }
  short   DeSutedHand[13];
  for(int j=0;j<13;j++)
    DeSutedHand[j]=boardCardsDeSuted[j];
  DeSutedHand[hand.first%13]++;
  DeSutedHand[hand.second%13]++;
  ////////////////////////// Four of a kind test///////////////////////////////
  short fourOfaKind=-1;
  short prevMax=-1;
  short max=-1;
  for(int j=0;j<13;j++){
    if(DeSutedHand[j]>0 && j>max){// three max are maintained in case like this 3QQQQ2
      prevMax=max;
      max=j;
    }
    if(DeSutedHand[j]==4){
      fourOfaKind=j;
    }
  }
  if(!ranks[h] && fourOfaKind>-1){
    for(int j=0;j<4;j++)
      bestHands[h][j]=fourOfaKind;
    bestHands[h][4]=(max==fourOfaKind)?prevMax:max; // in case four of a kind is max choose prev max
    ranks[h]=8;
  }
  ////////////////////////// Full house test ///////////////////////////////
  short threeOfaKind=-1;
  short twoOfaKindHigh=-1;
  short twoOfaKindLow=-1;
  short kickerh=-1; // will use for two pairs
  short kickerm=-1; // will use for two pairs
  short kickerl=-1; // will use for two pairs
  short kickerll=-1; // will use for two pairs
  short kickerlll=-1; // will use for two pairs
  for(int j=0;j<13;j++){
    if(DeSutedHand[12-j]==3)
      threeOfaKind=12-j;
    if(DeSutedHand[12-j]==2 && twoOfaKindHigh==-1)
      twoOfaKindHigh=12-j;
    else if(DeSutedHand[12-j]==2 && twoOfaKindHigh>-1) // will use for two pairs
      twoOfaKindLow=12-j;
    if(DeSutedHand[12-j]==1 && kickerh==-1)
      kickerh=12-j;
    else if(DeSutedHand[12-j]==1 && kickerh>-1 && kickerm==-1)
      kickerm=12-j;
    else if(DeSutedHand[12-j]==1 && kickerm>-1 && kickerl==-1)
      kickerl=12-j;
    else if(DeSutedHand[12-j]==1 && kickerl>-1 && kickerll==-1)
      kickerll=12-j;
    else if(DeSutedHand[12-j]==1 && kickerll>-1 && kickerlll==-1)
      kickerlll=12-j;
  }
  if(ranks[h]<7 && threeOfaKind>-1 && twoOfaKindHigh>-1){
    for(int j=0;j<3;j++)
      bestHands[h][j]=threeOfaKind;
    for(int j=0;j<2;j++)
      bestHands[h][3+j]=twoOfaKindHigh;
    ranks[h]=7;
  }
  ////////////////////////// Is streight test ///////////////////////////////
  short streight=0;
  for(int j=0;j<13;j++){
    if(DeSutedHand[12-j])
      streight++;
    else
      streight=0;
    if(!ranks[h] && streight==5){
      for(int n=0;n<5;n++)//highest possible streight
	bestHands[h][n]=12-j+n;
      ranks[h]=5;
      break;
    }
    if(!ranks[h] && streight==4 && DeSutedHand[12] && j==12){
      for(int n=0;n<4;n++)//lowest possible streight
	bestHands[h][n]=n;
      bestHands[h][4]=1;
      ranks[h]=5;
    }
  }
  ////////////////////////// Is three of a kind test ///////////////////////////////
  if(!ranks[h] && threeOfaKind>-1 && twoOfaKindHigh==-1){
    for(int j=0;j<3;j++)
      bestHands[h][j]=threeOfaKind;
    short max=-1;
    short pmax=-1;
    for(int j=0;j<13;j++){
      if(DeSutedHand[j]==1){
	pmax=max;
	max=j;
      }
    }
    bestHands[h][3]=pmax;
    bestHands[h][4]=max;
    ranks[h]=4;
  }
  ////////////////////////// Is two pair ///////////////////////////////
  if(!ranks[h] && twoOfaKindHigh>-1 && twoOfaKindLow>-1){
    bestHands[h][0]=twoOfaKindHigh;
    bestHands[h][1]=twoOfaKindHigh;
    bestHands[h][2]=twoOfaKindLow;
    bestHands[h][3]=twoOfaKindLow;
    bestHands[h][4]=kickerh;
    ranks[h]=3;
  }
  ////////////////////////// Is one pair ///////////////////////////////
  if(!ranks[h] && twoOfaKindHigh>-1 && twoOfaKindLow==-1 && threeOfaKind==-1 && fourOfaKind==-1){
    bestHands[h][0]=twoOfaKindHigh;
    bestHands[h][1]=twoOfaKindHigh;
    bestHands[h][2]=kickerh;
    bestHands[h][3]=kickerm;
    bestHands[h][4]=kickerl;
    ranks[h]=2;
  }
  ////////////////////////// Worst hand ///////////////////////////////
  if(!ranks[h] && twoOfaKindHigh==-1 && twoOfaKindLow==-1 && threeOfaKind==-1 && fourOfaKind==-1){
    bestHands[h][0]=kickerh;
    bestHands[h][1]=kickerm;
    bestHands[h][2]=kickerl;
    bestHands[h][3]=kickerll;
    bestHands[h][4]=kickerlll;
    ranks[h]=1;
  }
}
long Deck::HandToIndex(){//make sure hand is sorted in ascending order
long index=0;
 for (int i=0;i<7;i++){
   long tmp=hand[i];
   tmp<<=(36-i*6);
   index+=tmp;
 }
 return index;
}
short Deck::AllocateMemory(){
  printf("Allocate memory\n");
  long index=0,ind=0;
  hashBinSizes=(char*)calloc(size,sizeof(char));
  hashFinalValue=(int**)calloc(size,sizeof(int*));
  hashFinalKey=(long**)calloc(size,sizeof(long*));
  for(int fi=0;fi<52-6;fi++){
    for(int se=fi+1;se<52-5;se++){
      for(int th=se+1;th<52-4;th++){
	for(int fo=th+1;fo<52-3;fo++){
	  for(int fv=fo+1;fv<52-2;fv++){
	    for(int sx=fv+1;sx<52-1;sx++){
	      for(int svn=sx+1;svn<52;svn++){
		 hand[0]=fi;
	         hand[1]=se;
		 hand[2]=th;
		 hand[3]=fo;
		 hand[4]=fv;
		 hand[5]=sx;
		 hand[6]=svn;
		 index=HandToIndex();
		 ind=index%size;
		 hashBinSizes[ind]++;
	      }
	    }
	  }
	}
      }
    }
    printf("%d\n",fi);
  }
  for(int fi=0;fi<size;fi++){
    int binSize=hashBinSizes[fi];
     if(binSize>0){
       hashFinalKey[fi]=(long*)calloc(binSize,sizeof(long));
       hashFinalValue[fi]=(int*)calloc(binSize,sizeof(int));
     }
  }
  return 0;
}
	      
short Deck::HandMap(){
  AllocateMemory();
  printf("Create hand Map\n");
   int cnt=0;
   numPlayers=1;
   long index=0,ind=0;
   for(int fi=0;fi<52-6;fi++){
     for(int se=fi+1;se<52-5;se++){
       for(int th=se+1;th<52-4;th++){
   	for(int fo=th+1;fo<52-3;fo++){
   	  for(int fv=fo+1;fv<52-2;fv++){
   	    for(int sx=fv+1;sx<52-1;sx++){
   	      for(int svn=sx+1;svn<52;svn++){
   		 hand[0]=fi;
   	         hand[1]=se;
   		 hand[2]=th;
   		 hand[3]=fo;
   		 hand[4]=fv;
   		 hand[5]=sx;
   		 hand[6]=svn;
   		 index=HandToIndex();
		 ind=index%size;
		 playerHands[0].first=fi;
		 playerHands[0].second=se;
		 flop[0]=th;
		 flop[1]=fo;
		 flop[2]=fv;
		 turn=sx;
		 river=svn;
		 cnt++;
		 ClasifyBoardFlush();
		 ClasifyBoardPairs();
		 ranks[0]=0; // for repeated permutations needed
		 RankHand(0);
		 int singleRank=SingleRank(0);
		  for(int len=0;len<hashBinSizes[ind];len++){// max 15 is the length here
		    if(hashFinalKey[ind][len]==0){
		      hashFinalKey[ind][len]=index;
		      hashFinalValue[ind][len]=singleRank;
		      break;
		    }
		  }
   	      }
   	    }
   	  }
   	}
       }
     }
     printf("%d\n",fi);
   }
   //printf("checking for errors\n");
   // CheckForErrors();
  return 0;
}
short Deck::CheckForErrors(){
  long index=0,ind=0;
  for(int fi=0;fi<52-6;fi++){//52-6
     for(int se=fi+1;se<52-5;se++){
       for(int th=se+1;th<52-4;th++){
   	for(int fo=th+1;fo<52-3;fo++){
   	  for(int fv=fo+1;fv<52-2;fv++){
   	    for(int sx=fv+1;sx<52-1;sx++){
   	      for(int svn=sx+1;svn<52;svn++){
   		 hand[0]=fi;
   	         hand[1]=se;
   		 hand[2]=th;
   		 hand[3]=fo;
   		 hand[4]=fv;
   		 hand[5]=sx;
   		 hand[6]=svn;
   		 index=HandToIndex();
		 ind=index%size;
		 bool found=false;
		 for(int len=0;len<hashBinSizes[ind];len++){// max 15 is the length here
		   if(hashFinalKey[ind][len]==index){
		     found=true;
		     break;
		   }
		 }
		 if(!found && hashBinSizes[ind])
		   printf("could not find %lld\n",index);
   	      }
   	    }
   	  }
   	}
       }
     }
     printf("%d\n",fi);
   }
  return 0;
}
short Deck::CountDesutedFiveTuples(){
  
  int cnt=4356;
  // cnt+=12*11; // four of a kind
  // cnt+=12*11*10/2; // three of a kind div by 2 cos 11123 same as 11132
  // cnt+=12*11; // full house
  // cnt+=12*11*10/2; // two pairs
  // cnt+=12*11*10*9/(3*2); // one pair
  // cnt+=12*11*10*9*8/(120); // no pair
  printf("%d\n",cnt);
  return 0;
}
