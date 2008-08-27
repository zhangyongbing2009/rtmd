%%Command equal feedback
scalingfactors
%start(AI);
%rdata = getdata(AI);
rdata = getsample(AI);
%wdata = [ch1_cal(mean(rdata(:,5))) ch2_cal(mean(rdata(:,6))) ch3_cal(mean(rdata(:,7))) ch4_cal(mean(rdata(:,8)))];
wdata = [ch1_cal(rdata(:,5)) ch2_cal(rdata(:,6)) ch3_cal(rdata(:,7)) ch4_cal(rdata(:,8))];
%putdata(AO,wdata);
%start(AO);
putsample(AO, wdata);
disp(sprintf('\n--\nch20: %fV %fin\nch21: %fV %fin\nch22: %fV %fin\nch23: %fV %fin\n',wdata(:,1),wdata(:,1)*dispfeedbackscaling(1),wdata(:,2),wdata(:,2)*dispfeedbackscaling(2),wdata(:,3),wdata(:,3)*dispfeedbackscaling(3),wdata(:,4),wdata(:,4)*dispfeedbackscaling(4)));
