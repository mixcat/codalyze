RM := rm -rf

# All Target
all:  clean out/master.hex

# Master-mode Firmware
out/master.hex: out/master.elf sizedummy-master
	@echo 'Create Flash image (ihex format)'
	-avr-objcopy -R .eeprom -O ihex out/master.elf  "out/master.hex"
	@echo 'Finished building: $@'
	@echo ' '

sizedummy-master: out/master.elf
	@echo 'Invoking: Print Size'
	-avr-size --format=avr --mcu=attiny45 out/master.elf
	@echo 'Finished building: $@'
	@echo ' '

out/master.elf: out/master.o out/usiTwiMaster.o
	@echo 'Building target: $@'
	@echo 'Invoking: AVR C Linker'
	avr-gcc -Wl,-Map,out/master.map -L/opt/local/avr/include -mmcu=attiny45 -o"out/master.elf"  out/master.o  out/usiTwiMaster.o   
	@echo 'Finished building target: $@'
	@echo ' '

out/master.o:
	@echo 'Building file: $<'
	@echo 'Invoking: AVR Compiler'
	avr-gcc -Wall -Os -fpack-struct -fshort-enums -funsigned-char -funsigned-bitfields -mmcu=attiny45 -DF_CPU=1000000UL -MMD -MP -MF"out/master.d" -MT"out/master.d" -c -o"out/master.o" "../master.c"
	@echo 'Finished building: $<'
	@echo ' '
	
out/usiTwiMaster.o:
	@echo 'Building file: $<'
	@echo 'Invoking: AVR Compiler'
	avr-gcc -Wall -Os -fpack-struct -fshort-enums -funsigned-char -funsigned-bitfields -mmcu=attiny45 -DF_CPU=1000000UL -MMD -MP -MF"out/usiTwiMaster.d" -MT"out/usiTwiMaster.d" -c -o"out/usiTwiMaster.o" "../usiTwi/usiTwiMaster.c"
	@echo 'Finished building: $<'
	@echo ' '

# Other Targets
clean:
	-$(RM) out/*
	-@echo ' '

#secondary-outputs: out/master.hex out/sizedummy

.PHONY: all clean dependents
.SECONDARY:
