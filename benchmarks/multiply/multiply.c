#include "rocc.h"
#include "util.h"

// Using custom opcode 1

static inline void matrix_loadA(int *matrixA) {
	// Load memory address of A
	// rs1 is the identifier for which item to load
	ROCC_INSTRUCTION_SS(1, 0, matrixA, 0b011);
}

static inline void matrix_loadB(int *matrixB) {
	// Load memory address of B
	ROCC_INSTRUCTION_SS(1, 1, matrixB, 0b011);
}

static inline void multiply() {
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

int main(void)
{
	// int a = 5;
	// int b = 12;
	int a[4] = {5,4,5,6};
	int b[4] = {12,1395,2,3};
	
	printf("%d * %d\n", a[1], b[1]);
	printf("Expected: %d\n", a[1] * b[1]);
	
	matrix_loadA(a);
	printf("Loaded A\n");

	matrix_loadB(b);
	printf("Loaded B\n");
	
	multiply();
	
	int c = retrieve();
	
	printf("Got: %d\n", c);
	
	return 0;
}
