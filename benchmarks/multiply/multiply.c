#include "rocc.h"
#include "util.h"

// Using custom opcode 1

static inline void matrix_load(int *matrixA, int *matrixB) {
	// Load memory address of both A and B
	// rs1 is matrix A address, rs2 is matrix B address
	ROCC_INSTRUCTION_SS(1, matrixA, matrixB, 0b011); // funct3 = 3
}

static inline int multiply(int *matrixC) {
	int value;
	// Perform multiplication, have the processor wait for it to be completed.
	ROCC_INSTRUCTION_DS(1, value, matrixC, 0b110); // funct3 = 6
	return value;
}

// Old methods that worked with CustomAccelerator.scala
static inline void matrix_loadA(int *matrixA) {
	// Load memory address of A
	// rs1 is the identifier for which item to load
	ROCC_INSTRUCTION_SS(1, 0, matrixA, 0b011);
}

static inline void matrix_loadB(int *matrixB) {
	// Load memory address of B
	ROCC_INSTRUCTION_SS(1, 1, matrixB, 0b011);
}

static inline void multiply_old() {
	// Perform multiplication
	ROCC_INSTRUCTION_S(1, 0, 0b001);
}

static inline int retrieve()
{
	int value;
	// Request the result of multiplication into the mem address of value
	ROCC_INSTRUCTION_D(1, value, 0b100);
	return value;
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

int main(void)
{
	// int a = 5;
	// int b = 12;
	int a[4][4] = {
		{1,2,3,4},
		{5,6,7,8},
		{9,10,11,12},
		{13,14,15,16}
	};
	int b[4][4] = {
		{2,12,11,5},
		{1,3,0,2},
		{6,1,0,5},
		{0,3,1,9999},
	};
	
	/*
		(22     33     15     40020     )
		(58     109     63     80064     )
		(94     185     111     120108     )
		(130     261     159     160152     )
	*/
	
	// int c[4][4];
	// int tot = 0;
	// int m = 4;
	// int q = 4;
	// int p = 4;
	// printf("Initial matrix\n");
	// printMatrix(4, 4, c);
	// printf("-----------------------\n");
	
	// for (int s = 0; s < m; ++s) {
	// 	for (int d = 0; d < q; ++d) {
	// 		for (int k = 0; k < p; ++k) {
	// 			tot = tot + a[s][k] * b[k][d];
	// 		}
	// 	c[s][d] = tot;
	// 	tot = 0;
	// 	printMatrix(3, 3, c);
	// 	printf("-----------------\n");
	// 	}
	// }
	
	// printf("\n This is it!!!\n");

	
	// printf("%d * %d\n", a[1], b[1]);
	// printf("Expected: %d\n", multiple);
	
	// matrix_loadA(a);
	// printf("Loaded A\n");

	// matrix_loadB(b);
	// printf("Loaded B\n");
	
	matrix_load(a, b);
	printf("Loaded\n");
	
	int c[4][4];
	int result = multiply(c);
	
	printMatrix(4, 4, c);
	
	// int c2 = retrieve();
	
	printf("Returned value: %d\n", result);
	
	return 0;
}

