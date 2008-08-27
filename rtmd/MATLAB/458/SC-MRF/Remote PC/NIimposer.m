clear imposer;

%%Setup analog output
AO = analogoutput('nidaq',2);
%Set sample rate
%set(AO,'SampleRate',1);
%Actuator 1,2,3,4 on NI channels 0,1,2,3
o_chans = addchannel(AO,0:3);

%%Set up TCP/IP Server
javaaddpath(pwd);
imposer = TCPServerNew(8090,4,'NIimposer',0);
imposer.start;

%%Load scaling coefficients for converting commands to volts
scalingfactors;

pause(1);

%%Wait for commands
while (true)
    %Create a data holder
    o_data = zeros(1,4);
    
    %Get next commands in inches and allow process to grab them
    commands = imposer.getNewCommands;
    pause(0.02);
    if (~imposer.isRunning) 
        break;
    end
    
    %Convert the commands to proper format for AO
    %Requires data variable set with data=[1.0 1.0 1.0 1.0];    
    o_data = cast(commands','double');    
    
    %Convert from inches to volt
    for i = 1:4
        o_data(i) = o_data(i) / dispfeedbackscaling(i);
    end
    
    %Convert data to calibrated output
    o_data(1) = ch1_cal(o_data(1));
    o_data(2) = ch2_cal(o_data(2));
    o_data(3) = ch3_cal(o_data(3));
    o_data(4) = ch4_cal(o_data(4));
    
    %Push data to the AO object
    %putdata(AO,o_data);
    putsample(AO,o_data);


    %Check if the AO board is ready to send
    %while (strcmp(get(AO,'Running'),'On') == 1) 
    %    disp('AO Still running, waiting for release');
%        pause(0.5);
    %    pause(0.05);
    %end
    %Send the output
    %start(AO);         
%    pause(0.1);
    %pause(0.05);
    %disp(sprintf('\n---\ncmd1: %f\ncmd2: %f\ncmd3: %f\ncmd4: %f\n',o_data(1),o_data(2),o_data(3),o_data(4)));
    %while (strcmp(AO.Sending,'On') == 1) 
    %    disp('AO Still running, waiting for done');
%        pause(0.5);
    %    pause(0.05);
   % end
    %Tell network that the output was completed
    imposer.sendResponse([0;1]);
    
    clear commands;
    clear o_data;  
end

delete(AO);
imposer.close;