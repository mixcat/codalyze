################################################################################
# Automatically-generated file. Do not edit!
################################################################################

RM := rm -rf

# All Target
all:  master secondary-outputs

master: out/master.o out/usiTwiMaster.o
	@echo 'Building target: $@'
	@echo 'Invoking: AVR C Linker'
	avr-gcc -Wl,-Map,blinkm-new-firmware.map -L/opt/local/avr/include -mmcu=attiny45 -o"out/master.elf"  out/master.o  out/usiTwiMaster.o   
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
		
# Tool invocations
out/master.elf: $(OBJS) $(USER_OBJS)
	@echo 'Building target: $@'
	@echo 'Invoking: AVR C Linker'
	avr-gcc -Wl,-Map,blinkm-new-firmware.map -L/opt/local/avr/include -mmcu=attiny45 -o"out/master.elf" $(OBJS) $(USER_OBJS) $(LIBS)
	@echo 'Finished building target: $@'
	@echo ' '

out/master.hex: master
	@echo 'Create Flash image (ihex format)'
	-avr-objcopy -R .eeprom -O ihex out/master.elf  "out/master.hex"
	@echo 'Finished building: $@'
	@echo ' '

out/sizedummy: master
	@echo 'Invoking: Print Size'
	-avr-size --format=avr --mcu=attiny45 out/master.elf
	@echo 'Finished building: $@'
	@echo ' '

# Other Targets
clean:
	-$(RM) out/*
	-@echo ' '

secondary-outputs: out/master.hex out/sizedummy

.PHONY: all clean dependents
.SECONDARY:
