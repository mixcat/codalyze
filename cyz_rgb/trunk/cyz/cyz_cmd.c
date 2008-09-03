#include "cyz_cmd.h"
#include <avr/eeprom.h>


boot_parms temp;
boot_parms xtemp;

Cyz_cmd cyz_cmd;

void CYZ_CMD_GET_INSTANCE() {
	cyz_cmd.rcv_cmd_buf_cnt = 0;
	cyz_cmd.rcv_cmd_len = 0xFF;
	cyz_cmd.play_script = 0;
	cyz_cmd.script_end = 0;
	cyz_cmd.script_pos = 0;
	cyz_cmd.script_repeats = 0;
	cyz_cmd.script_repeated = 0;
}

/* returns the length of the command, command+payload. */
/* example "fadeToColor" is 'f',R,G,B: the returned length is 4 */
/* 0xFF means error */
uint8_t CYZ_CMD_get_cmd_len (char cmd) {
	switch(cmd) {
	case CMD_GO_TO_RGB:
		return 4;
	case CMD_FADE_TO_RGB:
		return 4;
	case CMD_WRITE_SCRIPT_LINE:
		return 8;
	case CMD_PLAY_LIGHT_SCRIPT:
		return 4;
	case CMD_STOP_SCRIPT:
		return 1;
	case CMD_SET_BOOT_PARMS:
		return 6;

	}
	return 0xFF;
}


void _CYZ_CMD_execute(uint8_t* cmd) {
	switch (cmd[0]) {
	case CMD_GO_TO_RGB:
		_CYZ_RGB_set_color(cmd[1],cmd[2],cmd[3]);
		break;
	case CMD_FADE_TO_RGB:
		_CYZ_RGB_set_fade_color(cmd[1],cmd[2],cmd[3]);
		break;
	case CMD_WRITE_SCRIPT_LINE:
	{
		if (cmd[2] > MAX_SCRIPT_LEN-1)
			break;
		//TODO: cmd[1] (script id) is ignored, only one script can be written
		cyz_cmd.script[cmd[2]].dur = cmd[3];
		cyz_cmd.script[cmd[2]].cmd[0] = cmd[4];
		cyz_cmd.script[cmd[2]].cmd[1] = cmd[5];
		cyz_cmd.script[cmd[2]].cmd[2] = cmd[6];
		cyz_cmd.script[cmd[2]].cmd[3] = cmd[7];
		if (cmd[2] > cyz_cmd.script_end) {
			cyz_cmd.script_end = cmd[2];
		}
	}
	break;
	case CMD_PLAY_LIGHT_SCRIPT:
		//cmd[1] is script number, currently ignore, we only play script 0
		cyz_cmd.script_repeats = cmd[2];
		cyz_cmd.script_pos = cmd[3];
		cyz_cmd.play_script = 1;
		break;
	case CMD_STOP_SCRIPT:
		cyz_cmd.play_script = 0;
		break;
	case CMD_SET_BOOT_PARMS:
	{
		temp.magic = CYZ_CMD_BOOTP_MAGIC;
		temp.mode = cmd[1];
		temp.repeats = 0;
		temp.scriptno = 0;
		temp.fadespeed = 0;
		temp.timeadjust = 0;
		eeprom_busy_wait();
		eeprom_write_block( (void*)&temp,(void*)30, sizeof(boot_parms));
	}
	break;
	}
}

void CYZ_CMD_load_boot_params() {
	boot_parms temp;
	eeprom_busy_wait();
	eeprom_read_block((void*)&temp, (const void*)30, sizeof(boot_parms));
	if (temp.magic == CYZ_CMD_BOOTP_MAGIC) {
		if (temp.mode == 1) {
			cyz_cmd.play_script = 1;
		}
	}
}

/* to be invoked inside a timer, every time its called plays next script line, */
/* if script is playing and there are more lines to play */
void _CYZ_CMD_play_next_script_line() {
	if (cyz_cmd.play_script == 1) {
		_CYZ_CMD_execute(cyz_cmd.script[cyz_cmd.script_pos++].cmd);
		if (cyz_cmd.script_pos > cyz_cmd.script_end) {
			cyz_cmd.script_pos = 0;
			cyz_cmd.script_repeated++;
			if (cyz_cmd.script_repeats > 0 && cyz_cmd.script_repeated >= cyz_cmd.script_repeats){
				cyz_cmd.play_script = 0;
				cyz_cmd.script_repeated = 0;
			}
		}
	}
}

void _CYZ_CMD_receive_one_byte(uint8_t in) {
	cyz_cmd.rcv_cmd_buf[cyz_cmd.rcv_cmd_buf_cnt] = in;
	/* first byte contains the command, use it to decide length of payload */
	if (cyz_cmd.rcv_cmd_buf_cnt == 0) {
		cyz_cmd.rcv_cmd_len = CYZ_CMD_get_cmd_len(in);
	}

	if (cyz_cmd.rcv_cmd_buf_cnt==(cyz_cmd.rcv_cmd_len-1)) {
		_CYZ_CMD_execute(cyz_cmd.rcv_cmd_buf);
		cyz_cmd.rcv_cmd_buf_cnt = 0;
	}
	else {
		cyz_cmd.rcv_cmd_buf_cnt++;
	}
}
