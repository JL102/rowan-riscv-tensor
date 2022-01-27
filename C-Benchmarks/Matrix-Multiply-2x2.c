#include<stdio.h>
    void printMatrix(int width, int height, int c[width][height]);
    int main() {
        int a[3][3] = {
            {1, 2},
            {7, 8}};
        int b[3][3] = {
            {9, 8},
            {3, 2}};


        int c[3][3];
        int tot = 0;
        int m = 2;
        int q = 2;
        int p = 2;
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

