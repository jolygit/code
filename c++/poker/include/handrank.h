#include "Deck.h"
class HandRank{
  Deck deck;
 public:
  HandRank(int pl);
  double  stat[13][13];
  double  count[13][13];
  int  BuildStat();
  int  BuildSingleStat();
  int  PrintHand();
  int  CreateHandMap();
  int  PrintGame();
  int  CheckDistrib();
};
