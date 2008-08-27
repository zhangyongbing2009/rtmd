%%Read analog input
scalingfactors
rdata = getsample(AI);
wdata = [rdata(1,1) rdata(1,2) rdata(1,3) 0];
disp(sprintf('\n--\nAct1: %fV %fin\nAct2: %fV %fin\nAct3: %fV %fin\nLVDT: %fV %fin\n',wdata(1),wdata(1)*dispscaling(1),wdata(2),wdata(2)*dispscaling(2),wdata(3),wdata(3)*dispscaling(3),rdata(1,4),rdata(1,4)*dispscaling(4)));

