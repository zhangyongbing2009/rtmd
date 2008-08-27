%%Setup analog input
AI = analoginput('nidaq',1);
%Load Cell 1,2,3,4 on NI channels 0,1,2,3
i_chans_lc = addchannel(AI,0:3);
%Displacement Transducer 1,2,3,4 on NI channels 0,1,2,3
i_chans_dt = addchannel(AI,20:23);
set(AI,'SampleRate',1000);
set(AI,'SamplesPerTrigger',1000);
