% Assume NI setup already

% input file
input = [0 0.1 0.2 0.3 0.4 0.5 0.4 0.3 0.2 0.1 0];

% read initial offsets
offset = getsample(AI);

k = 1;
results = [0 0 0 0 0 0 0 0];
% impose all the commands
for i = 1:length(input)
    
    %Convert from inches to volt
    for m = 1:3
        commands(m) = input(i) / dispscaling(m);
    end
    
    % impose commands
    disp(sprintf('Imposing %f in ',input(i)));
    putsample(AO, [ch1_cal(offset(1) + commands(1))...
                   ch2_cal(offset(2) + commands(2))...
                   ch3_cal(offset(3) + commands(3))...
                   0]);
    wait(AO,5);    
    
    % read 10 samples back
    for j = 1:12
        % read measurement
        feedback = getsample(AI);
        %Convert from volts to inces
        for m = 1:4
            converted(m) = (feedback(m) - offset(m)) * dispscaling(m);              
        end
        results = cat(1,results,[i j k input(i) converted]);
        k = k + 1;
        wait(AI,5);
        pause(0.09);             
    end
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
end

% Write to file
csvwrite('ramping_res.csv',results);