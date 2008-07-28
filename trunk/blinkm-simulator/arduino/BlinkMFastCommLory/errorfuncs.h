#ifndef _ERRORFUNCS_H
#define _ERRORFUNCS_H

/* fast blink for 1 sec */
inline static void errorblink_mem() {
  for ( int i = 0 ; i < 20 ; i++ ) 
  {
    digitalWrite(recordingPin, HIGH);
    delay(100);
    digitalWrite(recordingPin, LOW);
    delay(100);
  }
}

/* slow blink for 1 sec */
inline static void errorblink_cmd() {
  for ( int i = 0 ; i < 10 ; i++ ) 
  {
    digitalWrite(recordingPin, HIGH);
    delay(200);
    digitalWrite(recordingPin, LOW);
    delay(200);
  }
}


#endif
