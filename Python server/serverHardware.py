import socket
from pyA20.gpio import gpio
from pyA20.gpio import port
import serial
import time
import operator
import os
	
led = port.PA12
gpio.init()
gpio.setcfg(led, gpio.OUTPUT)
ledStatus = 0
gpio.output(led, ledStatus)	

ser = serial.Serial(
    port='/dev/ttyS3',
    baudrate = 9600,
    parity=serial.PARITY_NONE,
    stopbits=serial.STOPBITS_ONE,
    bytesize=serial.EIGHTBITS,
    timeout=1
)

irListen = False
	
sock = socket.socket()
sock.bind(('', 9090))
sock.listen(1)
while True:
  conn, addr = sock.accept()
  print 'connected:', addr
  while True:
    data = conn.recv(1024)
    if not data:
      break
    conn.send(data.upper())
    print data
    if data[0] == 'r':
     irListen = True
     data = data[1:len(data)]
    else:
      if irListen:
        break
    if data[0] == '1' or data[0] == '2':
      ser.write(data+'\n')
    elif data == 'playpause':
      if ledStatus == 0:
        ledStatus = 1
      else:
        ledStatus = 0
      gpio.output(led, ledStatus)
    elif data == 'stopIR':
      irListen = False
    elif data == 'laserON':
      ledStatus = 1
      gpio.output(led, ledStatus)
    elif data == 'laserOFF':
      ledStatus = 0
      gpio.output(led, ledStatus)
  conn.close()
  print "close"
