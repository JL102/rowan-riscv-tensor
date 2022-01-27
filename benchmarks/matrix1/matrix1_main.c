#include "rocc.h"
#include "util.h"

#define DATA_SIZE 3

int matrix_a[DATA_SIZE] = {
	4,20,3
};

int matrix_b[DATA_SIZE] = {
	31,2,11
};

// Using custom opcode 1

static inline void matrix_loadA(long int matrixA) {
	// Load memory address of A
	// rs1 is the identifier for which item to load
	ROCC_INSTRUCTION_SS(1, 0, &matrixA, 0b011);
}

static inline void matrix_loadB(long int matrixB) {
	// Load memory address of B
	ROCC_INSTRUCTION_SS(1, 1, &matrixB, 0b011);
}

static inline void multiply() {
	// Perform multiplication
	ROCC_INSTRUCTION_S(1, 0, 0b001);
}

int main() {
	
	int i;
	int verify_result = 0;
	
	for (i = 0; i < DATA_SIZE; i++) {
		verify_result += matrix_a[i] * matrix_b[i];
	}
	
	long int a = -40;
	long int b = 634101444901;
	
	printf("[%d, %d, %d] * [%d, %d, %d]\n", matrix_a[0], matrix_a[1], matrix_a[2], matrix_b[0], matrix_b[1], matrix_b[2]);
	printf("Expected: %d\n", verify_result);
	
	matrix_loadA(a);
	printf("Loaded A\n");
	
	matrix_loadB(b);
	printf("Loaded B\n");
	
	return 0;
}