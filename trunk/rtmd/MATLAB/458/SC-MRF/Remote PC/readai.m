%%Read analog input
scalingfactors
%start(AI);
%rdata = getdata(AI);
rdata = getsample(AI);
%wdata = [mean(rdata(:,5)) mean(rdata(:,6)) mean(rdata(:,7)) mean(rdata(:,8))];
wdata = [rdata(:,5) rdata(:,6) rdata(:,7) rdata(:,8)];
winchdata = [wdata(1)*dispfeedbackscaling(1) wdata(2)*dispfeedbackscaling(2) wdata(3)*dispfeedbackscaling(3) wdata(4)*dispfeedbackscaling(4)];
%disp(sprintf('\n--\nch0: %fV\nch1: %fV\nch2: %fV\nch3: %fV\nch20: %fV %fin\nch21: %fV %fin\nch22: %fV %fin\nch23: %fV %fin\n',rdata(:,1),rdata(:,2),rdata(:,3),rdata(:,4),wdata(1),wdata(1)*dispfeedbackscaling(1),wdata(2),wdata(2)*dispfeedbackscaling(2),wdata(3),wdata(3)*dispfeedbackscaling(3),wdata(4),wdata(4)*dispfeedbackscaling(4)));

