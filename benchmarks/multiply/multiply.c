#include "rocc.h"
#include "util.h"

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

static inline long retrieve()
{
	long value;
	// Request the result of multiplication into the mem address of value
	ROCC_INSTRUCTION_D(1, value, 0b100);
	return value;
}

int main(void)
{
	long a = 5;
	long b = 12;
	
	printf("%d * %d\n", a, b);
	printf("Expected: %d\n", a * b);
	
	matrix_loadA(a);
	printf("Loaded A\n");

	matrix_loadB(b);
	printf("Loaded B\n");
	
	multiply();
	
	long c = retrieve();
	
	printf("Got: %d\n", c);
	
	return 0;
}
