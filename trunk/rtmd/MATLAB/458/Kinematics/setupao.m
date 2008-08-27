%%Setup analog output
AO = analogoutput('nidaq','Dev2');
%Set sample rate
set(AO,'SampleRate',1);
%Actuator 1,2,3 and Trillion Signal
o_chans = addchannel(AO,[0 1 2 4]);




