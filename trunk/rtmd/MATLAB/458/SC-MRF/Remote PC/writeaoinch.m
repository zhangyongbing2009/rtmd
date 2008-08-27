%%Write analog output
scalingfactors
correctedwdata = [ch1_cal(winchdata(1)/dispfeedbackscaling(1)) ch2_cal(winchdata(2)/dispfeedbackscaling(2)) ch3_cal(winchdata(3)/dispfeedbackscaling(3)) ch4_cal(winchdata(4)/dispfeedbackscaling(4))];
%putdata(AO,correctedwdata);
%start(AO);
putsample(AO,correctedwdata);

