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


        int c[3][3];
        int tot = 0;
        int m = 3;
        int q = 3;
        int p = 3;
        for (int s = 0; s < m; ++s) {
            for (int d = 0; d < q; ++d) {
                for (int k = 0; k < p; ++k) {
                    tot = tot + a[s][k] * b[k][d];
                }
            c[s][d] = tot;
            tot = 0;
            }
        }

  }

