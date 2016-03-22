# Introduction #

Evolutionary Sudoku for Arduino + Blinkm


# Details #

This page details to some extent the evolutionary sudoku to be run on the Arduino/Blinkm. See [here](http://code.google.com/p/codalyze/wiki/CyzRgb) for details.

[Download](http://codalyze.googlecode.com/files/evo_sudoku-v0.1.tar.gz) it here.

# README #

Compile with
```
make console
```
to get the console version. Default
```
make
```
will produce a version with no output ready to be flashed (more or less). You
need avr-gcc and a bunch of stuff. So far it works on a Mac OSX with avr-gcc,
avr-libc etc installed via mac ports.

Run it with
```
./evo_console data $RANDOM
```
where $RANDOM is the usual BASH random variable (it's used to initialize the
random number generator). It will produce a file named
```
out
```
which contains a diff of the changes in the sudoku; first byte is position (from
1 to 81), second byte is the digit (from 0 to 9, where 0 is 'black' or empty
> square). Feed it through a serial connection to the Arduino board or through a
> pipe to a blinkm simulator (to be released some time soon between here and
> infinity).

The file named
```
data 
```
is the file containing the sudoku. When ran in non-console mode it has a default sudoku inside (see source code for that). The file format is whatever you want, just use non-digit values for spaces in the sudoku and digits for the rest.

Usual disclaimer apply (no liability, do whatever you want with it, etc).

Code is released with GNU GPL. The Makefile has been hacked together by
incorporating an Eclipse-generated makefile, which in turn has been
probably inspired/taken from the Arduino/blinkm/avr-gcc makefiles.

lorenzo.grespan at gmail.com

This is the ugliest README I've ever seen in my life. And the code is full of
#ifdef. I will fix them.

# Genetic Algorithm #

More detail will follow. For now, suffice to say that

  * it produces a random individual via mutation
  * it evaluates it fitness, as:

fitness = number of digits placed on the sudoku - number of conflicts

where a 'conflict' is a digit repeated on a row, column or sub-matrix.

  * if the progeny has a better fitness it will be kept and generate a new mutation. Otherwise the progeny will be discarded.
  * if the algorithm gets stuck (i.e. the ratio of mutations leading to a less-fit offspring over the number of iterations approaches 0.2) the sudoku will start from scratch. This is due to the fact that once you're stuck in a dead-end, it's much more likely to find a solution if you restart from scratch than if you try to correct things (going back to older mutations). And it saves space.

And the whole point is not to find the solution in a short time, but rather to have a meaningful 'something' to show on your blinkms. A genetic algorithm is **not** the best approach; it's just fun.