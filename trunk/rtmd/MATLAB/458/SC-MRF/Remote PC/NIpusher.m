clear pusher;

%%Setup analog input
AI = analoginput('nidaq',1);
%Load Cell 1,2,3,4 on NI channels 0,1,2,3
i_chans_lc = addchannel(AI,0:3);
%Displacement Transducer 1,2,3,4 on NI channels 0,1,2,3
i_chans_dt = addchannel(AI,20:23);
set(AI,'SampleRate',1000);
%samplesToGet = 100;
%set(AI,'SamplesPerTrigger',samplesToGet);
set(AI,'Timeout',5);

%%Set up TCP/IP Server
javaaddpath(pwd);
pusher = TCPServerNew(8091,4,'NIpusher',0);
pusher.start;

%%Load scaling coefficients into 'scaling' variable
scalingfactors;

pause(1);

%%Wait for commands
while (true)
    if (~pusher.isRunning) 
        break;
    end
    
    % Wait until the AI object is free
    while (strcmp(get(AI,'Running'),'On') == 1) 
        disp('AI Still running, waiting for release');
        pause(1.0);
    end
    %Get feedbacks     
    %start(AI);  
    %Check if we got the right number of samples
    %samplesReceived = get(AI,'SamplesAvailable');
    %while (samplesReceived ~= samplesToGet)        
    %    samplesReceived = get(AI,'SamplesAvailable');
    %end
    %i_data = getdata(AI); 
    i_data = getsample(AI);
    pause(0.09);
    %Take the mean of the data to filter out noise
    %i_data = mean(i_data);
    %Convert to EU 
    for i = 1:8
        i_data(i) = i_data(i) * scaling(i);
    end
    %Cast to float for compatibility
    data = cast(i_data','single');  
    %Send to client
    pusher.sendResponse(data);
end

delete(AI);
pusher.close;
