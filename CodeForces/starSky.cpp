#include <bits/stdc++.h>

typedef long long ll;
typedef long long llong;
typedef long double ld;
typedef unsigned long long ull;

using namespace std;
int f[13][101][101];
int ss[13][101][101];

int main() {
    ios_base::sync_with_stdio(false); cout.setf(ios::fixed); cout.precision(20);
    //freopen("c:/Users/alex/code/HackerRank/JavaCode/input.txt", "r", stdin);
    int n,q,c;
    scanf("%d%d%d", &n,&q,&c);
    c++;
    for(int i=0;i<11;i++){
        for(int j=0;j<101;j++){
            for(int k=0;k<101;k++){
                f[i][j][k]=0;
                ss[i][j][k]=0;
            }
        }
    }
    for(int i=0;i<n;i++){
        int x,y,s;
        scanf("%d%d%d", &x,&y,&s);
        for(int v=0;v<c;v++){
            f[v][x][y]+=s;
            s++;
            if(s==c)
                s=0;
        }
    }
     for(int i=0;i<11;i++){
         for(int j=1;j<101;j++){
             for(int k=1;k<101;k++){
                 ss[i][j][k]+=f[i][j][k]+ss[i][j][k-1]+ss[i][j-1][k]-ss[i][j-1][k-1];
             }
         }
     }
    int t,X1,Y1,X2,Y2;
    for(int i=0;i<q;i++){
        scanf("%d%d%d%d%d", &t,&X1,&Y1,&X2,&Y2);
        int TB=0;
        t%=c;
        TB+=ss[t][X2][Y2]+ss[t][X1-1][Y1-1]-ss[t][X1-1][Y2]-ss[t][X2][Y1-1];
        printf("%d\n",TB);
    }
    return 0;
}

