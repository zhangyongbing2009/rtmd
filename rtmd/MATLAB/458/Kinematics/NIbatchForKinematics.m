%%% IMPOSE COMMANDS
    %Convert from inches to volt
    commands(2) = deltadlA1 / dispscaling(2);
    commands(3) = deltadlA2 / dispscaling(3);
    commands(1) = deltadlA3 / dispscaling(1);    
    % impose commands
    disp(sprintf('Imposing Step %i',i));
    putsample(AO, [ch1_cal(offset(1) + commands(1))...
                   ch2_cal(offset(2) + commands(2))...
                   ch3_cal(offset(3) + commands(3))...
                   0]);
    wait(AO,5);    
    pause(0.5);
    % read measurement
    feedback = getsample(AI);
    wait(AI,5);
    %Convert from volts to inches
    deltadlM3tot = (feedback(1) - offset(1)) * dispscaling(1);   
    deltadlM1tot = (feedback(2) - offset(2)) * dispscaling(2);   
    deltadlM2tot = (feedback(3) - offset(3)) * dispscaling(3);   
    deltadlM4tot = (feedback(4) - offset(4)) * dispscaling(4);   
    results = cat(1,results,[deltadlM1tot deltadlM2tot deltadlM3tot deltadlM4tot]);
    %Trigger Trillion
    putsample(AO, [ch1_cal(offset(1) + commands(1))...
                   ch2_cal(offset(2) + commands(2))...
                   ch3_cal(offset(3) + commands(3))...
                   5]);
    pause(0.01);
    putsample(AO, [ch1_cal(offset(1) + commands(1))...
                   ch2_cal(offset(2) + commands(2))...
                   ch3_cal(offset(3) + commands(3))...
                   0]);
    pause(0.01);
    %%%%% END NI