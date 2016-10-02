#include <vector>
#include <unistd.h>
#include <map>
#include <algorithm>
#include <utility>
#include <cstdlib>
#include <ctime>
#include <stdio.h>
#include <string>
#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <functional>
class Deck{
 public:
  std::pair<short,short> PlayerHand(short pl);
  std::vector<short> Flop();
  Deck(char _numPlayers=10);
  short Turn();
  short River();
  int   SingleRank(int h);
  short Permute();
  short FixOneHandPermute();
  short PrintAll(); // prints all player hands as well as flop turn and river
  short PrintHand(short i);
  short GetCard(short i){ return cards[i];}
  short Winners();
  short WinnersFast();
  short ClasifyBoardFlush();
  short ClasifyBoardPairs();
  short bb(short  c);
  short HandPermute();
  short HandMap();// Precomputes rank and value of all possible pocker hands C(52,7) values
  std::vector<std::pair<short,short> > playerHands;
  short              winners[10]; // assumed that no more then 10 winners possible
  short numPlayers;
 private:
  short CheckForErrors();
  long HandToIndex();
  std::map<std::string,int> handMap; // any 7 card hand in ascending order is mapped to its ranking
  int   ConvertToInt(int h);
  short AllocateMemory();
  short RankHand(int i);
  short createCardsMap();
  short PrintCard(short i);
  short cards[52];
  std::vector<std::pair<std::string,char> > cardsMap;
  std::vector<short> handFlushCards;
  std::vector<short> hand; // one of C(52,7) hands for HandMap() above

  short              flop[3];
  short              turn;
  short              river;
  short              boardCardsDeSuted[13]; 
  bool               isFlushPossibleFlop;
  bool               isFlushPossibleTurn;
  bool               isFlushPossibleRiver;
  short              FlushSute;
  short              FlashBoardCards[7];
  short              NumFlashBrdCards;
  std::vector<std::vector<short> >  bestHands;
  std::vector<short>   ranks;
  int**         hashFinalValue;
  long**        hashFinalKey;
  char*         hashBinSizes;
  int           size;
};

