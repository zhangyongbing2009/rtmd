%%Write calibrated analog output
correctedwdata = [ch1_cal(wdata(1)) ch2_cal(wdata(2)) ch3_cal(wdata(3)) ch4_cal(wdata(4))];
%putdata(AO,correctedwdata);
%start(AO);
putsample(AO,correctedwdata);

