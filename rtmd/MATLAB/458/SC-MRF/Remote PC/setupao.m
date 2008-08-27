%%Setup analog output
AO = analogoutput('nidaq',2);
%Actuator 1,2,3,4 on NI channels 0,1,2,3
o_chans = addchannel(AO,0:3);




