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
#include <iomanip>
#include <numeric>
#include <sstream>
#include <utility>
#include <iostream>
#include <algorithm>
#include <functional>
using namespace std;

struct node {
   struct node *left,*right;
   int val, val1;
};
typedef struct node node;
node * addElement(node * root, int x);
node * addRandomElement (node * root, int x, int index);
/* Write your custom functions here */
int max(int a,int b, int c){
  int max=a;
  max=b>max?b:max;
  max=c>max?c:max;
  return max;
}
int maxDepth(node * root) {
  int maxLength= root==NULL ? 0: (max(maxDepth(root->left),maxDepth(root->right),0)+1);
  return maxLength;
}

int diameterOfTree(node * root) {
  int diam=root==NULL?0:max(diameterOfTree(root->left),diameterOfTree(root->right),maxDepth(root->left)+maxDepth(root->right)+1);
  return diam;
}

int main(){
       
 node * root = NULL;
 int a[100],K=0,i = 0,j = 0;
  int isBst = 0;    
  scanf("%d",&isBst);
  scanf("%d",&K);
  for( j = 0; j < K;j++ ) {
   scanf("%d",&a[i++]);    
  }
  for( i = 0; i < K;i++ ){
       if( !isBst ){            
           root = addRandomElement(root,a[i],i);
       } else {
           root = addElement(root,a[i]);
       }            

   }
printf("%d",diameterOfTree(root));
   
return 0;
}
node * addElement(node * root, int x ){
   if( root == NULL ) {
       root = (node *) (malloc(sizeof(node))); 
               root->left = NULL; root->right = NULL;
       root->val = x;root->val1 = -1;
       return root;
   }
   if( x < root->val ) {
        root->left = addElement(root->left,x);
   }
   else {
       root->right = addElement(root->right,x);                            
   }
   return root;
}

node * addRandomElement(node *root, int x, int index){
   if( root == NULL ) {
       root = (node *) (malloc(sizeof(node))); 
       root->left = NULL; root->right = NULL;
               root->val = x;root->val1 = -1;
       return root;
   }

   if( index <=2 ){
       root->left = addRandomElement(root->left,x,index);
   } else {
       root->right = addRandomElement(root->right,x,index);
   }

   return root;
}
