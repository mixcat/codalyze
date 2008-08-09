/**
 * evolutionary sudoku
 *
 * fitness is: #digits in sudoku - #conflicts
 *
 * where a conflict is an impossible situation, i.e. two digits in the same
 * row/column/sub-matrix
 *
 * lorenzo.grespan@gmail.com
 *
 *
 * TODO
 * genetic algo needs rethinking. 
 * if code gets stuck into a loop, perhaps it should
 * 'go back' and unroll the last mutation and try a different one
 * i.e. if fitness doesn't improve after trying all mutations
 *
 * idea: either save the mutations somewhere and randomly roll-back 
 */

/* isdigit() */
#include <ctype.h>	
#include <stdio.h>
#include <stdlib.h>

#define DATA_SIZE 	81
#define MAX_FIT		81

int original[DATA_SIZE];


#ifdef CONSOLE
static void 
usage(void) {
	printf("Insert meaningful help message here\n");
}
#endif

static inline void print(int *gene_r[9][9], int *gene_c[9][9], int *gene_s[9][9]) {
	int i, j;
	printf("gene\trow genes\t\tcolumn genes\t\tsub-square genes\n");
	for ( i = 0 ; i < 9 ; i++ ) {
		printf("#%d:\t", i);
		for ( j = 0 ; j < 9 ; j++ )
			printf("%d ", *gene_r[i][j]);
		printf("\t");
		for ( j = 0 ; j < 9 ; j++ )
			printf("%d ", *gene_c[i][j]);
		printf("\t");
		for ( j = 0 ; j < 9 ; j++ )
			printf("%d ", *gene_s[i][j]);

		printf("\n");
	}
}

/*
 * old[0] is the position
 * old[1] is the old value
 */
static inline void 
reverse_mutation(int data[DATA_SIZE], int old[2]) {
	data[old[0]] = old[1];
}

/*
 * mutation[0] is the position
 * mutation[1] is the old value
 */
static void
mutate(int data[DATA_SIZE], int old[2], int mutation[2]) {
	int pos;
	do {
		pos = random()%DATA_SIZE;
	} while ( original[pos] != 0 );
	/* save the old value */
	old[0] = pos;
	old[1] = data[pos];
	/* save the mutation to be sent to the blinkms*/
	mutation[0] = pos;
	mutation[1] = random()%9;
	/* update the individual */
	data[pos] = mutation[1];
}

static int 
eval_fitness(int *gene_r[9][9], int *gene_c[9][9], int *gene_s[9][9]) {
	int fit;
	int i, j;
	int conflicts_row = 0, conflicts_col = 0, conflicts_sub = 0;
	int digits = 0;
	int values_row[9], values_col[9], values_sub[9];

	/* zero the arrays before starting */
	for ( i = 0 ; i < 9 ; i++ ) {
		values_row[i] = 0;
		values_col[i] = 0;
		values_sub[i] = 0;
	}

	for ( i = 0 ; i < 9 ; i++ ) {
		for ( j = 0 ; j < 9 ; j++ ) {
			/* count the number of digits */
			values_row[*gene_r[i][j]]++;
			values_col[*gene_c[i][j]]++;
			values_sub[*gene_s[i][j]]++;
			if ( *gene_r[i][j] != 0 )
				digits++;
		}

		/* we start from 1 ; we do not care about the zeros */
		for ( j = 1 ; j < 9 ; j++ ) {
			/* if there is more than one digit per row/col, 
			 * it's a conflict */
			if ( values_row[j] > 1 )
				conflicts_row += values_row[j]-1;
			if ( values_col[j] > 1 )
				conflicts_col += values_col[j]-1;
			if ( values_sub[j] > 1 )
				conflicts_sub += values_sub[j]-1;
		}

	}
	fit = digits - conflicts_row - conflicts_col - conflicts_sub;
	/*
	printf("digits: %d\n", digits);
	printf("conflicts per row: %d\n", conflicts_row);
	printf("conflicts per col: %d\n", conflicts_col);
	printf("conflicts per sub: %d\n", conflicts_sub);

	printf("fitness: %d\n", fit);
	*/
	return fit;
}

#ifdef CONSOLE
int main ( int argc, char **argv ) {
#else
int main () {
#endif
	/* C90 forbids mixed declaration and code */
	int i;
	int data[DATA_SIZE];
	int *gene_r[9][9], *gene_c[9][9], *gene_s[9][9];
	int fit, new_fit;
	int iter = 0;

	/*
	 * old[0] is the position
	 * old[1] is the old value
	 */
	int old[2], mutation[2];	

#ifdef CONSOLE
	char c[2];
	FILE *in;

	if ( argc != 3 ) {
		usage();
		return -1;
	}

	in = fopen(argv[1], "r");
	if ( in == NULL ) {
		perror("Unable to open file");
		return -1;
	}

	/* atoi() reads char* until \0;
	 * if the next thing in memory after 'c' were a number
	 * I'd be screwed :)
	 * */
	c[1] = '\0';	
	i = 0 ;

	while ( (c[0] = fgetc(in)) != EOF && i <= DATA_SIZE ) 
		if ( isdigit(c[0]) )
			data[i++] = atoi(c);
		else
			if ( isspace(c[0]) )
				/* for newline */
				continue;	
			else
				/* everything else */
				data[i++] = 0;	

	/* init random number generator */
	srandom(atoi(argv[2]));
#endif

	/* generate the genotypes */
	{
		int offset = 0;
		int A, B = 0;
		/* read by rows */
		for ( i = 0 ; i < DATA_SIZE ; i++ ) {
			gene_r[offset][i%9] = &data[i];
			/* transpose matrix */
			gene_c[offset][i%9] = &data[(i%9)*9+offset];

			/* the sub-square selection */
			/* A is the sub-square column */
			if ( i%9 < 3 )
				A = 0;
			else
				if ( i%9 > 5 )
					A = 2;
				else
					A = 1;
			/* B is the sub-square row */
			if ( i>0 && i%27 == 0 )
				B++;

			/* get the actual data */
			gene_s[A+B*3][i%3+3*(offset%3)] = &data[i];

			/* personal note
			 * a[i][j is *(*(a+i)+j)
			 */

			if ( i%9 == 8 )
				offset++;

			/* save the original data set */
			original[i] = data[i];
		}
	}
	print(gene_r, gene_c, gene_s);

	fit = eval_fitness(gene_r, gene_c, gene_s);

	while ( fit <= MAX_FIT ) {
		iter++;
		printf(".");
		mutate(data, old, mutation);
		/* TODO 
		 * insert code here to send data to blinkm
		 * to show mutations 
		 *
		 * ideally, send out the mutation[] array
		 */
		new_fit = eval_fitness(gene_r, gene_c, gene_s);

		if ( fit > new_fit )  
			/* go back to before the mutation */
			reverse_mutation(data, old);
		else {
			printf("\n(%d): got a valid offspring; old fitness: %d, new fit: %d\n", iter, fit, new_fit);
			print(gene_r, gene_c, gene_s);
			fit = new_fit;
		}
	}

	return 0;
		
}
