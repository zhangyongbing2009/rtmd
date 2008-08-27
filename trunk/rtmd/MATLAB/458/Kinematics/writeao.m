%%Write analog output
wdata = [ch1_cal(wdata(1)) ch2_cal(wdata(2)) ch3_cal(wdata(3)) 0];
putsample(AO,wdata);

