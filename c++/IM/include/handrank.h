#include "Deck.h"
class HandRank{
 public:
  Deck deck;
  HandRank(int pl);
  double  stat[13][13];
  double  count[13][13];
  std::vector<std::pair<int,int> > hand;
  std::vector<int> flopWinners;
  std::vector<int> flopAll;
  int flopHandSize;
  short tmpFlash[4];
  int  flashChance;
  int  FlopHandSize();
  int  FlashMap();
  int  HandToIndex();
  int  HandAndFlopIndex(int i);
  int  BuildStat();
  int  BuildSingleStat();
  int  PrintHand();
  int  CreateHandMap();
  int  PrintGame();
  int  CheckDistrib();
  int  TestFlopIndex();
};
