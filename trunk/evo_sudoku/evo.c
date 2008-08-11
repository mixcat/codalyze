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
 * this file is released under the GNU General Public License
 */

/* isdigit() */
#include <ctype.h>	
#include <stdio.h>
#include <stdlib.h>

#ifdef CONSOLE
#include <unistd.h>
#endif

#define DATA_SIZE 	81
#define MAX_FIT		81

int original[DATA_SIZE];


#ifdef CONSOLE
static void 
usage(char* progname) {
	printf("Usage: %s <input file> <random value>\n", progname);
	printf("Where <random value> can be also $RANDOM\n");
}
#endif

#ifdef CONSOLE
static inline void print_genes(int *gene_r[9][9], int *gene_c[9][9], int *gene_s[9][9]) {
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
#endif

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
	do {
		mutation[1] = random()%9+1;
	} while ( old[1] == mutation[1] );
	/* update the individual */
	data[pos] = mutation[1];
}

static int 
eval_fitness(int *gene_r[9][9], int *gene_c[9][9], int *gene_s[9][9]) {
	int i, j;
	int conflicts_row = 0, conflicts_col = 0, conflicts_sub = 0;
	int digits = 0;
	int values_row[10], values_col[10], values_sub[10];

	for ( i = 0 ; i < 9 ; i++ ) {
		/* zero the arrays before starting */
		for ( j = 0 ; j < 10 ; j++ ) {
			values_row[j] = 0;
			values_col[j] = 0;
			values_sub[j] = 0;
		}

		for ( j = 0 ; j < 9 ; j++ ) {
			/* count the number of digits */
			values_row[*gene_r[i][j]]++;
			values_col[*gene_c[i][j]]++;
			values_sub[*gene_s[i][j]]++;
			if ( *gene_r[i][j] != 0 )
				digits++;
		}

		/* we start from 1 ; we do not care about the zeros */
		for ( j = 1 ; j <= 9 ; j++ ) {
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
	/*
	fit = digits - conflicts_row - conflicts_col - conflicts_sub;
	printf("digits: %d\n", digits);
	printf("conflicts per row: %d\n", conflicts_row);
	printf("conflicts per col: %d\n", conflicts_col);
	printf("conflicts per sub: %d\n", conflicts_sub);

	printf("fitness: %d\n", fit);
	*/
	return digits - conflicts_row - conflicts_col - conflicts_sub;
}

/*********/

#ifdef CONSOLE
int main ( int argc, char **argv ) {
#else
int main () {
#endif
	/* C90 forbids mixed declaration and code */
	int i;
	int *gene_r[9][9], *gene_c[9][9], *gene_s[9][9];
	int fit, new_fit;
	int iter = 0;
	int mutations;	/* the number of successful mutations */
	int orig_fit;
	float jumps_threshold = 0.01;
	int peak = 0, old_peak;

	/*
	 * old[0] is the position
	 * old[1] is the old value
	 */
	int old[2], mutation[2];	

#ifdef CONSOLE
	/* if CONSOLE is defined -> code for console, read from file */
	int data[DATA_SIZE];
	FILE *in, *out;
	out = fopen("out", "w");
	if ( out == NULL ) {
		perror("Unable to open file for writing");
		return -1;
	}

	{
		char c[2];

		if ( argc != 3 ) {
			usage(argv[0]);
			fclose(out);
			return -1;
		}

		in = fopen(argv[1], "r");
		if ( in == NULL ) {
			perror("Unable to open file");
			fclose(out);
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
		/* FIXME no console -> need to produce a random seed
		 * via other means (timing, interrupt, whatever */
		srandom(atoi(argv[2]));
	}
	/* write to file current values of cells */
	for ( i = 0 ; i < DATA_SIZE ; i++ )
		fprintf(out, "%c%c", i+1, data[i]);

#else
	/* if no console -> is code for arduino */

	/* a sample sudoku (correct) */
	int data[] = { 
			8, 4, 0, 0, 0, 0, 0, 5, 7,
			0, 7, 2, 0, 0, 0, 9, 8, 0,
			0, 0, 9, 0, 0, 0, 2, 0, 0,
			0, 0, 0, 4, 0, 6, 0, 0, 0,
			1, 0, 0, 5, 0, 3, 0, 0, 8,
			0, 0, 0, 7, 0, 9, 0, 0, 0,
			0, 0, 8, 0, 0, 0, 6, 0, 0,
			0, 9, 5, 0, 0, 0, 7, 1, 0,
			7, 3, 0, 0, 0, 0, 0, 4, 5
	};

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

			if ( i%9 == 8 )
				offset++;

			/* save the original data set */
			original[i] = data[i];
		}
	}

	fit = eval_fitness(gene_r, gene_c, gene_s);
	orig_fit = fit;
	mutations = 0;

	/* main genetic algorithm */
	while ( fit <= MAX_FIT ) {
		/* count the number of iterations to detect local maxima */
		iter++;
		mutate(data, old, mutation);

#ifdef CONSOLE
		/* send data to arduino / save to file */
		fprintf(out, "%c%c", mutation[0]+1, mutation[1]);
		fflush(out);
#endif

		new_fit = eval_fitness(gene_r, gene_c, gene_s);

		if ( fit >= new_fit ) {
			/* go back to before the mutation */
			reverse_mutation(data, old);

			/* avoid doing 0/value */
			if ( mutations > 0 
					&& (float)mutations/iter < jumps_threshold ) {

#ifdef CONSOLE
				printf("Probable local minima detected; rollback\n");
				printf("mutations: %d, iterations: %d, ratio: %f\n", 
						mutations, iter, (float)mutations/iter);
				printf("peak fitness: %d, old peak fitness: %d\n", 
						fit, old_peak);
				printf("rollback! old genes:\n");
				print_genes(gene_r, gene_c, gene_s);
#endif

				/* we're in a local maximum; rollback */
				for ( i = 0 ; i < DATA_SIZE ; i++ ) {
					data[i] = original[i];

#ifdef CONSOLE
					/* send reset to file/arduino */
					fprintf(out, "%c%c", i+1, data[i]);
#endif

				}

				/* save the old best value */
				old_peak = peak;
				peak = fit;

				/* reset everything */
				fit = orig_fit;
				mutations = 0;
				iter = 0;

				if ( old_peak > peak )
					jumps_threshold /= 2;
				else
					if ( old_peak < peak ) 
						jumps_threshold *= 2;
						
#ifdef CONSOLE
				printf("new jump threshold: %f\n\n", jumps_threshold);
				/* XXX debugging */
				/* sleep(7); */
#endif

			}
		}
		else {

#ifdef CONSOLE
			printf("mutation pos: %d %d->%d", 
					old[0], old[1], mutation[1]);
			printf("\n(%d): got a valid offspring; ", iter);
			printf("old fitness: %d, new fit: %d\n", fit, new_fit);
			print_genes(gene_r, gene_c, gene_s);
#endif

			fit = new_fit;
			mutations++;
		}
	}

#ifdef CONSOLE
	printf("Found solution after %d iterations:\n", iter);
	print_genes(gene_r, gene_c, gene_s);

	/* send to file/leds..*/
	for ( i = 0 ; i < DATA_SIZE ; i++ ) 
		fprintf(out, "%c%c", i+1, data[i]);

	fclose(in);
	fclose(out);
#endif

	return 0;
		
}
