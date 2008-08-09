################################################################################
# Automatically-generated file. Do not edit!
################################################################################

-include ../makefile.init

#RM := rm -rf
RM := rm -f

CFLAGS +=-pedantic -Wextra -Werror 

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
evo.c 

OBJS += \
evo.o 

C_DEPS += \
evo.d 


ifneq ($(MAKECMDGOALS),clean)
ifneq ($(strip $(ASM_DEPS)),)
-include $(ASM_DEPS)
endif
ifneq ($(strip $(S_UPPER_DEPS)),)
-include $(S_UPPER_DEPS)
endif
ifneq ($(strip $(S_DEPS)),)
-include $(S_DEPS)
endif
ifneq ($(strip $(C_DEPS)),)
-include $(C_DEPS)
endif
endif

# Add inputs and outputs from these tool invocations to the build variables 
LSS += \
evo_sudoku.lss \

FLASH_IMAGE += \
evo_sudoku.hex \

EEPROM_IMAGE += \
evo_sudoku.eep \

SIZEDUMMY += \
sizedummy \


# All Target
all: evo_sudoku.elf secondary-outputs

%.o: %.c
	@echo 'Building file: $<'
	@echo 'Invoking: AVR Compiler'
	avr-gcc $(CFLAGS) -Wall -Os -fpack-struct -fshort-enums -funsigned-char -funsigned-bitfields -mmcu=atmega168 -DF_CPU=16000000UL -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -c -o"$@" $?
	#avr-gcc -$(MY_FLAGS) -Wall -Os -fpack-struct -fshort-enums -funsigned-char -funsigned-bitfields -mmcu=atmega168 -DF_CPU=16000000UL -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -c -o"$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '

# Tool invocations
evo_sudoku.elf: $(OBJS) $(USER_OBJS)
	@echo 'Building target: $@'
	@echo 'Invoking: AVR C Linker'
	avr-gcc -Wl,-Map,evo_sudoku.map -mmcu=atmega168 -o"evo_sudoku.elf" $(OBJS) $(USER_OBJS) $(LIBS)
	@echo 'Finished building target: $@'
	@echo ' '

evo_sudoku.lss: evo_sudoku.elf
	@echo 'Invoking: AVR Create Extended Listing'
	-avr-objdump -h -S evo_sudoku.elf  >"evo_sudoku.lss"
	@echo 'Finished building: $@'
	@echo ' '

evo_sudoku.hex: evo_sudoku.elf
	@echo 'Create Flash image (ihex format)'
	-avr-objcopy -R .eeprom -O ihex evo_sudoku.elf  "evo_sudoku.hex"
	@echo 'Finished building: $@'
	@echo ' '

evo_sudoku.eep: evo_sudoku.elf
	@echo 'Create eeprom image (ihex format)'
	-avr-objcopy -j .eeprom --no-change-warnings --change-section-lma .eeprom=0 -O ihex evo_sudoku.elf  "evo_sudoku.eep"
	@echo 'Finished building: $@'
	@echo ' '

sizedummy: evo_sudoku.elf
	@echo 'Invoking: Print Size'
#-avr-size --format=avr --mcu=atmega168 evo_sudoku.elf
	-avr-size evo_sudoku.elf
	$(RM) evo.d
	@echo 'Finished building: $@'
	@echo ' '

# Other Targets
clean:
	-$(RM) $(ELFS)$(ASM_DEPS)$(EEPROM_IMAGE)$(OBJS)$(S_UPPER_DEPS)$(LSS)$(FLASH_IMAGE)$(S_DEPS)$(C_DEPS)$(SIZEDUMMY) evo_sudoku.elf
	-@echo ' '

secondary-outputs: $(LSS) $(FLASH_IMAGE) $(EEPROM_IMAGE) $(SIZEDUMMY)

.PHONY: all clean dependents
.SECONDARY:

