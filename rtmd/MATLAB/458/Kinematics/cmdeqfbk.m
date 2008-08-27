%%Command equal feedback
scalingfactors
rdata = getsample(AI);
wdata = [ch1_cal(rdata(1,1)) ch2_cal(rdata(1,2)) ch3_cal(rdata(1,3)) 0.0];
putsample(AO,wdata);
disp(sprintf('\n--\nAct1: %fV %fin\nAct2: %fV %fin\nAct3: %fV %fin\n',wdata(1),wdata(1)*dispscaling(1),wdata(2),wdata(2)*dispscaling(2),wdata(3),wdata(3)*dispscaling(3)));
