/**
 * evolutionary sudoku
 *
 * fitness is: #digits in sudoku - #conflicts
 *
 * where a conflict is an impossible situation, i.e. two digits in the same
 * row/column/sub-matrix
 *
 * lorenzo.grespan@gmail.com
 */
#include <stdio.h>
#include <strings.h>

#define DATA_SIZE 	81
#define MAX_FIT		81

static void usage(void) {
	printf("Insert meaningful help message here\n");
}

static void mutate(int *gene_r[9][9], int *gene_c[9][9], int *gene_s[9][9], 
		int mutation[4]) {
	// pick a random gene pool
	// pick a random value [0,9]
	// mutate it 
}

static int eval_fitness(int *gene_r[9][9], int *gene_c[9][9], int *gene_s[9][9]) {
	/* eval fitness */
	int fit;
	int i, j;
	int conflicts_row = 0, conflicts_col = 0, conflicts_sub = 0;
	int digits = 0;
	int values_row[9], values_col[9], values_sub[9];
	memset(values_row, 0, sizeof values_row[0] * 9);
	memset(values_col, 0, sizeof values_col[0] * 9);
	memset(values_sub, 0, sizeof values_sub[0] * 9);

	for ( i = 0 ; i < 9 ; i++ )
	{
		for ( j = 0 ; j < 9 ; j++ )
		{
			/* count the number of digits */
			values_row[*gene_r[i][j]]++;
			values_col[*gene_c[i][j]]++;
			values_sub[*gene_s[i][j]]++;
			if ( *gene_r[i][j] != 0 )
				digits++;
		}

		/* we start from 1 ; we do not care about the zeros */
		for ( j = 1 ; j < 9 ; j++ )
		{
			/* if there is more than one digit per row/col, 
			 * it's a conflict */
			if ( values_row[j] > 1 )
				conflicts_row += values_row[j]-1;
			if ( values_col[j] > 1 )
				conflicts_col += values_col[j]-1;
			if ( values_sub[j] > 1 )
				conflicts_sub += values_sub[j]-1;
		}

		/* reset counters */
		bzero(values_row, 9* sizeof values_row[0]);
		bzero(values_col, 9* sizeof values_col[0]);
		bzero(values_sub, 9* sizeof values_sub[0]);
	}
	fit = digits - conflicts_row - conflicts_col - conflicts_sub;
	printf("digits: %d\n", digits);
	printf("conflicts per row: %d\n", conflicts_row);
	printf("conflicts per col: %d\n", conflicts_col);
	printf("conflicts per sub: %d\n", conflicts_sub);

	printf("fitness: %d\n", fit);
	return fit;
}

int main ( int argc, char **argv ) {
	if ( argc != 2 ) {
		usage();
		return -1;
	}

	FILE *in = fopen(argv[1], "r");
	if ( in == NULL ) {
		perror("Unable to open file");
		return -1;
	}

	int c, i = 0 ;
	int data[DATA_SIZE];

	while ( (c = fgetc(in)) != EOF && i <= DATA_SIZE ) 
		if ( isdigit(c) )
			data[i++] = atoi(&c);
		else
			if ( isspace(c) )
				continue;	// for newline
			else
				data[i++] = 0;	// everything else
	

	int *gene_r[9][9], *gene_c[9][9], *gene_s[9][9];

	/* generate the genotypes */
	{
		int offset = 0;
		int A, B = 0;
		//int sub_col;
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

			// personal note
			// a[i][j is *(*(a+i)+j)

			if ( i%9 == 8 )
				offset++;
		}
	}

	int j;
	/* some pretty printing, in local scope */
	{
		printf("gene\trow genes\t\tcolumn genes\t\tsub-square genes\n");
		for ( i = 0 ; i < 9 ; i++ ) {
			printf("#%d:\t", i);
			for ( j = 0 ; j < 9 ; j++ )
				printf("%d ", *gene_r[i][j]);
			printf("\t");
			for ( j = 0 ; j < 9 ; j++ )
				printf("%d ", *gene_c[i][j]);
			printf("\t");
			// for when it will work..
			for ( j = 0 ; j < 9 ; j++ )
				printf("%d ", *gene_s[i][j]);

			printf("\n");
		}
	}

	int fit_winner = 0, fit_loser, winner = 0;

	/* mutation[0] is the gene pool (row, col, sq)
	 * mutation[1] is the gene number (gene_c[])
	 * mutation[2] is the chromosome (gene_c[][])
	 * mutation[3] is the old value */
	int mutation[4];	

	/* TODO 
	 * fix this.
	 *
	 * ideally -> if the original one wins, don't
	 * re-evaluate its fitness; 
	 */
	while ( fit_winner <= MAX_FIT ) {
		fit = eval_fitness(gene_r, gene_c, gene_s);
		mutate(gene_r, gene_c, gene_s, mutation);
		new_fit = eval_fitness(gene_r, gene_c, gene_s);

		if ( fit > new_fit ) {
			winner = 1;
			/* first wins; discard second */
			reverse_mutation(gene_r, gene_c, gene_s, mutation);
		} else {
			winner = 2;

			/* second wins, keep it */
			fit = new_fit;
		}
	}
		

}
