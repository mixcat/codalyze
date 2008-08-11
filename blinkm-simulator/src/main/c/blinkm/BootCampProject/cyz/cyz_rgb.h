/*****************************************************************************
 *	Codalyze PWM RGB: A very basic implementation of pulse-width modulation (PWM) on an AVR controller with
 *  three leds of different color(R,G,B): display any 24-bit color using three leds.
 *
 *	PWM in Plain English:
 *	 PROBLEM:	We have three leds, each led has two positions (on/off): we can only get 8 colors.
 *				We fancy displaying 24-bit rgb colors.
 *		
 *	 SOLUTION:	We turn leds on and off very fast and trick the eye. ex: we want to display
 *				RGB{255, 165, 0} (orange): overy 255 ticks
 *				- red led is always on
 *				- green led is on for the first 165 ticks and then is off
 *				- blue led is always off
 *	 Much more detail on wikipedia.
 *
 *	This code has been implemented for and tested on ATtiny45 with 3 leds connected on PB3, PB4, PB1.
 *	It should work on all avr chips, just remember to update definitions of
 *	PWM_PORT,PWM_DDR, PINRED, PINGRN, PINBLU according to your setup.
 *	
 *	Compiler: AVR-GCC
 *	
 *	Usage:
 *		1. invoke CYZ_RGB_setup(); in global space
 *		2. invoke CYZ_RGB_init(); in main
 *		3. invoke CYZ_RGB_pulse(); at fixed intervals;
 *		   putting it in SIGNAL(SIG_OVERFLOW0) {} works good (initialize timers and signals, if you do)
 *		4. every time CYZ_RGB_set_color(red, green, blue) is called, the perceived color will change
 *
 *	Links:
 *	ATtiny45 - http://www.atmel.com/dyn/products/Product_card.asp?part_id=3618
 *
 *	Author: Matteo Caprari <matteo.caprari@gmail.com>
 *	Thanks to: Lorenzo Grespan, Todd E. Kurt, AVR Application note 136: Low-jitter Multi-channel Software PWM
 *
 *	License: new BSD license
 *******************************************************************************/

/* select port and data direction register on which pin leds are */
#define PWM_PORT PORTB
#define PWM_DDR DDRB 

/* map leds to actual pins */
#define PINRED PB3
#define PINGRN PB4
#define PINBLU PB1

/******************************/
/* turn off single leds */
#define RED_LED_OFF PWM_PORT &= ~(1<<PINRED)
#define GRN_LED_OFF PWM_PORT &= ~(1<<PINGRN)
#define BLU_LED_OFF PWM_PORT &= ~(1<<PINBLU)

/* turn on single leds */
#define RED_LED_ON PWM_PORT |= 1<<PINRED
#define GRN_LED_ON PWM_PORT |= 1<<PINGRN
#define BLU_LED_ON PWM_PORT |= 1<<PINBLU

/* turn on all leds */
#define ALL_LED_ON RED_LED_ON; GRN_LED_ON; BLU_LED_ON

typedef struct _color {
    unsigned char r;
	unsigned char g;
	unsigned char b;
} Color;

/* to be called in main file global space */
#define CYZ_RGB_setup() \
	Color _CYZ_RGB_color; \
	unsigned char _CYZ_RGB_pulse_count = 0xFF

#define CYZ_RGB_color_r _CYZ_RGB_color.r;
#define CYZ_RGB_color_g _CYZ_RGB_color.g;
#define CYZ_RGB_color_b _CYZ_RGB_color.b;

/* to be called only one time, usually in main */
#define CYZ_RGB_init() \
	CYZ_RGB_set_color(0,0,0);\
	_CYZ_RGB_setup_pins()

/* to be called once for each pulse, usually on interrupt SIG_OVERFLOW0 */
#define CYZ_RGB_pulse() \
	if (++_CYZ_RGB_pulse_count == 0) ALL_LED_ON; \
	if (_CYZ_RGB_pulse_count == _CYZ_RGB_color.r) RED_LED_OFF; \
	if (_CYZ_RGB_pulse_count == _CYZ_RGB_color.g) GRN_LED_OFF; \
	if (_CYZ_RGB_pulse_count == _CYZ_RGB_color.b) BLU_LED_OFF

/* set color for immediate display */
#define CYZ_RGB_set_color(R, G, B)\
	_CYZ_RGB_set_color(&_CYZ_RGB_color, R, G, B);

void _CYZ_RGB_set_color(Color* color, unsigned char r, unsigned char g, unsigned char b) {
	color->r = r;
	color->g = g;
	color->b = b; 
}

/* Configure DDR: put pins connected to leds in output mode */
void _CYZ_RGB_setup_pins() {
	PWM_DDR |= 1<<PINRED; 
	PWM_DDR |= 1<<PINGRN;
	PWM_DDR |= 1<<PINBLU;
}
