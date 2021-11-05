
#include "util.h"
#include "dataset.h"

int main() {
	
	int i;
	int result = 0;
	
	for (i = 0; i < DATA_SIZE; i++) {
		result += input_matrix_1[i] * input_matrix_2[i];
	}
	
	return verify(1, &verify_data, &result);
	return 0;
}