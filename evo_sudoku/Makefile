SOURCES = evo.c

DEBUG=-g
#OPT=-O3
FLAGS=-Wall -lc -pedantic -Wextra -Werror 

evo: evo.c
	gcc $(DEBUG) $(OPT) $(FLAGS) -o $@ $?

clean: evo
	rm evo