#include <map>
#include <set>
#include <list>
#include <cmath>
#include <ctime>
#include <deque>
#include <queue>
#include <stack>
#include <bitset>
#include <cstdio>
#include <vector>
#include <cstdlib>
#include <numeric>
#include <sstream>
#include <iostream>
#include <algorithm>
#include <deque>
#include <utility>      // std::pair
using namespace std;
class node{
public:
  node(int val,node* parent=NULL);
  void addChild(node* ch);
  node* _parent;
  vector<node*> children;
  int _val;
  int numOfspring;
};
node::node(int val,node* parent){
  _parent=parent;
  _val=val;
  numOfspring=0;
}
void node::addChild(node* ch){
  children.push_back(ch);
  numOfspring=0;
  for(int i=0;i<children.size();i++)
    numOfspring+=children[i]->numOfspring+1;
  node* parent=_parent;
    while(parent!=NULL){
      parent->numOfspring=0;
      for(int i=0;i<parent->children.size();i++)
	parent->numOfspring+=parent->children[i]->numOfspring+1;
      parent=parent->_parent;
    }
}
vector<node*> nodes;
int splits=0;
void split(node* node){
  for(int i=0;i<node->children.size();i++){
    if(node->children[i]->numOfspring%2==1)
      splits++;
      split(node->children[i]);
  }
}
int main(void) {
  long long N=30,M=29;
  cin >>N>>M;
  nodes.resize(N,NULL);
  // vector<pair<int,int> > in;
  // in.push_back(make_pair(2,1));
  // in.push_back(make_pair(3, 2));
  // in.push_back(make_pair(4, 3));
  // in.push_back(make_pair(5, 2));
  // in.push_back(make_pair(6, 4));
  // in.push_back(make_pair(7, 4));
  // in.push_back(make_pair(8, 1));
  // in.push_back(make_pair(9, 5));
  // in.push_back(make_pair(10, 4));
  // in.push_back(make_pair(11, 4));
  // in.push_back(make_pair(12, 8));
  // in.push_back(make_pair(13, 2));
  // in.push_back(make_pair(14, 2));
  // in.push_back(make_pair(15, 8));
  // in.push_back(make_pair(16, 10));
  // in.push_back(make_pair(17, 1));
  // in.push_back(make_pair(18, 17));
  // in.push_back(make_pair(19, 18));
  // in.push_back(make_pair(20, 4));
  // in.push_back(make_pair(21, 15));
  // in.push_back(make_pair(22, 20));
  // in.push_back(make_pair(23, 2));
  // in.push_back(make_pair(24, 12));
  // in.push_back(make_pair(25, 21));
  // in.push_back(make_pair(26, 17));
  // in.push_back(make_pair(27, 5));
  // in.push_back(make_pair(28, 27));
  // in.push_back(make_pair(29, 4));
  // in.push_back(make_pair(30, 25));
  while(M--){
    int v1,v2;
    cin >>v1>>v2;
    // v1=in[in.size()-1-M].second;
    // v2=in[in.size()-1-M].first;
    int tmp;
    if(v1>v2){
      tmp=v2;
      v2=v1;
      v1=tmp;
    }
    nodes[v1-1]=nodes[v1-1]==NULL?new node(v1-1):nodes[v1-1];
    nodes[v2-1]=nodes[v2-1]==NULL?new node(v2-1,nodes[v1-1]):nodes[v2-1];
    nodes[v2-1]->_parent=nodes[v1-1];
    nodes[v1-1]->addChild(nodes[v2-1]);
  }
  split(nodes[0]);
  cout<<splits<<endl;
  return 0;
}
