



#ifndef OSCCAL
// program memory location of the internal oscillator "calibration byte"
#define OSCCAL 0xFF  /* default to last byte of program memory */
#endif

#define _osccal(addr)                                \
        asm volatile (                               \
                "lpm" "\n\t"                         \
                "out 0x31,r0" /* OSCCAL register */  \
                : /* no outputs */                   \
                : "z" ((uint16_t)(addr))             \
                : "r0" /* clobbers */                \
        )
#define osccal() _osccal(OSCCAL) /* calibrate internal RC oscillator */
