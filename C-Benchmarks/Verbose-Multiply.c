#include<stdio.h>
    void printMatrix(int width, int height, int c[width][height]);
    int main() {
        int a[3][3] = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}};
        int b[3][3] = {
            {9, 8, 7},
            {6, 5, 4},
            {3, 2, 1}};

/*
        int a[2][2]={
                {1, 2},
                {3, 4}};
        int b[2][2]={
                {5, 6},
                {7, 8}};
*/


        int c[3][3];
        int tot = 0;
        int m = 3;
        int q = 3;
        int p = 3;
        printf("Initial matrix\n");
        printMatrix(3, 3, c);
        printf("-----------------------\n");
        for (int s = 0; s < m; ++s) {
            for (int d = 0; d < q; ++d) {
                for (int k = 0; k < p; ++k) {
                    tot = tot + a[s][k] * b[k][d];
                }
            c[s][d] = tot;
            tot = 0;
            printMatrix(3, 3, c);
            printf("-----------------\n");
            }
        }

        printf("\n This is it!!!\n");

        printMatrix(3, 3, c);
  }

        void printMatrix(int width, int height, int c[width][height]){
                int x = 0;
                int y = 0;
                for(x = 0 ; x < width; ++x) {
                        printf(" (");
                        for(y = 0 ; y < height ; ++y){
                                printf("%d     ", c[x][y]);
                        }
                        printf(")\n");
                }

        }
