#include "rocc.h"
#include "util.h"

// Using custom opcode 1

static inline long multiply(int num1, int num2)
{
	long value;
	// the last value is [xd, xs1, xs2]
	ROCC_INSTRUCTION_DSS(1, value, num1, num2, 0b111);
	return value;
}

int main(void)
{
	long a = 5;
	long b = 12;
	
	printf("%d * %d\n", a, b);
	printf("Expected: %d\n", a * b);
	
	long c = multiply(a, b);
	
	printf("Got: %d\n", c);
	
	return 0;
}
