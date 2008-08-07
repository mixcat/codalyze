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
/* isdigit() */
#include <ctype.h>	
#include <stdio.h>
#include <stdlib.h>
#include <strings.h>

#define DATA_SIZE 	81
#define MAX_FIT		81

static void 
usage(void) {
	printf("Insert meaningful help message here\n");
}

static void 
reverse_mutation(int *gene_r[9][9], int *gene_c[9][9], int *gene_s[9][9], 
		int mutation[4]) {
	switch (mutation[0]) {
		case 0:
			*gene_r[mutation[1]][mutation[2]] = mutation[3];
		case 1:
			*gene_c[mutation[1]][mutation[2]] = mutation[3];
		case 2:
			*gene_s[mutation[1]][mutation[2]] = mutation[3];
	}
}

/*
 * mutation[0] is the gene pool (row, col, sq)
 * mutation[1] is the gene number (gene_c[])
 * mutation[2] is the chromosome (gene_c[][])
 * mutation[3] is the old value
 */
static void 
mutate(int *gene_r[9][9], int *gene_c[9][9], int *gene_s[9][9], 
		int mutation[4]) {
	/* pick a random value [0,9] for the gene number */
	mutation[1] = random()%9;
	/* and for the chromosome */
	mutation[2] = random()%9;

	/* pick a random gene pool; it's not the best
	 * random you can get but will have to suffice */
	mutation[0] = random()%3;	
	switch (mutation[0]) {
		case 0:
			/* save the old value */
			mutation[3] = *gene_r[mutation[1]][mutation[2]];
			/* mutate it */
			*gene_c[mutation[1]][mutation[2]] = random()%9;
			break;
		case 1:
			mutation[3] = *gene_c[mutation[1]][mutation[2]];
			*gene_c[mutation[1]][mutation[2]] = random()%9;
			break;
		case 2:
			mutation[3] = *gene_s[mutation[1]][mutation[2]];
			*gene_s[mutation[1]][mutation[2]] = random()%9;
			break;
	}

	/* TODO
	 * check that it's not in the original data set
	 */

}

static int 
eval_fitness(int *gene_r[9][9], int *gene_c[9][9], int *gene_s[9][9]) {
	int fit;
	int i, j;
	int conflicts_row = 0, conflicts_col = 0, conflicts_sub = 0;
	int digits = 0;
	int values_row[9], values_col[9], values_sub[9];

	for ( i = 0 ; i < 9 ; i++ )
	{
		/* arrays aren't zeroed - memset is better than bzero */
		memset(values_row, 0, sizeof values_row);
		memset(values_col, 0, sizeof values_col);
		memset(values_sub, 0, sizeof values_sub);
		
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
	/* C90 forbids mixed declaration and code */
	int i, j;
	char c[2];
	FILE *in;
	int data[DATA_SIZE];
	int *gene_r[9][9], *gene_c[9][9], *gene_s[9][9];
	int fit, new_fit;

	/* mutation[0] is the gene pool (row, col, sq)
	 * mutation[1] is the gene number (gene_c[])
	 * mutation[2] is the chromosome (gene_c[][])
	 * mutation[3] is the old value */
	int mutation[4];	

	if ( argc != 3 ) {
		usage();
		return -1;
	}

	in = fopen(argv[1], "r");
	if ( in == NULL ) {
		perror("Unable to open file");
		return -1;
	}

	/* init random number generator */
	srandom(atoi(argv[2]));

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
		}
	}

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
			for ( j = 0 ; j < 9 ; j++ )
				printf("%d ", *gene_s[i][j]);

			printf("\n");
		}
	}

	fit = eval_fitness(gene_r, gene_c, gene_s);

	while ( fit <= MAX_FIT ) {
		mutate(gene_r, gene_c, gene_s, mutation);
		new_fit = eval_fitness(gene_r, gene_c, gene_s);

		if ( fit > new_fit ) { 
			printf("parent's fit: %d, offspring: %d; reverse_mut\n", fit, new_fit);
			/* go back to before the mutation */
			reverse_mutation(gene_r, gene_c, gene_s, mutation);
		}
		else {
			printf("paret's fit: %d, offspring: %d; don't reverse mut\n", fit, new_fit);
			fit = new_fit;
		}
	}

	return 0;
		
}
