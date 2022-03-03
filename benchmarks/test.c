#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>

typedef struct {
	long int number1;
	long int number2;
	long int array[4];
	long int padder;
	long int multidim[2][2];
	long int padder2;
} data;

int main() {

	data Data;
	FILE *outputfile;
	int howmany;

	Data.number1 = 5;
	Data.number2 = -1;
	Data.array[0] = 10;
	Data.array[1] = 11;
	Data.array[2] = 12;
	Data.array[3] = 13;
	Data.padder = -1;
	Data.multidim[0][0] = 14;
	Data.multidim[0][1] = 15;
	Data.multidim[1][0] = 16;
	Data.multidim[1][1] = 17;
	Data.padder2 = -1;

	printf("%ld, %ld, %ld, %ld\n", Data.multidim[0][0], Data.multidim[0][1], Data.multidim[1][0], Data.multidim[1][1]);

	// open file
	if ( ( outputfile = fopen("output.dat", "w") ) == NULL ) {
		perror("output.dat");
		exit(1);
	}

	// write data
	howmany = fwrite(&Data, sizeof(data), 1, outputfile);

	printf("Wrote %d items of size %d bytes.\n", howmany, (int)sizeof(data));

	fclose(outputfile);
	return 0;
}
