%%Setup analog input
AI = analoginput('nidaq','Dev1');
%Displacement devices
i_chans_dt = addchannel(AI,3:6);
set(AI,'SampleRate',1);
%Setup input range
for i = 1:4
    AI.Channel(i).InputRange = [-10 10];
    AI.Channel(i).SensorRange = [-10 10];
    AI.Channel(i).UnitsRange = [-10 10];
end