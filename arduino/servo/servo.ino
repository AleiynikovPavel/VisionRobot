#include <Servo.h>
#include <SoftwareSerial.h>

SoftwareSerial opiSerial(12, 13);
Servo servo1;
Servo servo2;

void setup()
{
  servo1.attach(6);
  servo2.attach(5);
  opiSerial.begin(9600);
}

void loop()
{
  String res = readStr();
  if (res == "")
  {
    return;
  }
  if (res.startsWith("1"))
  {
    int angel = (res.substring(1)).toInt();
    if (angel > 180) {
      angel = 180;
    }
    if (angel < 0) {
      angel = 0;
    }
    servo1.write(angel);
  }
  else if (res.startsWith("2"))
  {
    int angel = (res.substring(1)).toInt();
    if (angel > 180) {
      angel = 180;
    }
    if (angel < 0) {
      angel = 0;
    }
    servo2.write(angel);
  }
}

String readStr() {
  String res = "";
  if (opiSerial.available()) {
    char ch = 0;
    while (true) {
      while (!opiSerial.available()) {}
      ch = opiSerial.read();
      if (ch == '\n') {
        break;
      }
      res += ch;
    }
  }
  return res;
}
