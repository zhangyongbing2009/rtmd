%%%
% Self Centering Steel Column Algorithm
%
% Input file format: 
% Target Displacement (inches), Target Rotation (radians)
%
% Program uses target displacement to determine velocity and tries to converge to specified
% rotation.
%%%


%%% NOTES: SCRAMNet Displacement and Load responses are negated for
%%% direction

%%%% Initialized variables
targetFile = 'historyFile.csv';    % Target Displacement/Rotation File
daqFile = 'daq.xml';               % DAQ XML file
simulation = 0;                    % 1 = Ideal simulation (feedback = command, no DAQ)
                                   % 0 = DAQ, SCRAMNet, Real test!   
DEBUG = 0;                         % Set to 1 for many print statements, 0 for minimal
%% Experimental parameters
Ff = 0;             
L = 132;                
Mptpos = 4920;        
Mptneg = 4605;
rpos = 27.97;
rneg = 10.39;
Es = 29000;
Ib = 2670;

%% Algorithm parameters
epsilon = 0.0001;               % +/- tolerance for rotational target
NumRamps = 3000;                % NumRamps/1024 = approx seconds to ramp
disp1_cmd_scr = 1;              % This should remain the same (1 = Act1, 6 = Act2)
disp1_scr = 66;                 % Set to disp1_cmd_scr for loopback, 66 for Controller feedback
disp1_cmd_UpperLimit = 20;      % Displacement command upper limit in inches
disp1_cmd_LowerLimit = -20;     % Displacement command limit limit in inches
load1_scr = 67;                 % 67 from Controller feedback
NWPSloc = 1;                    % NWPS Location in DAQ XML
NEPSloc = 3;                    % NEPS Location in DAQ XML
SWPSloc = 2;                    % SWPS Location in DAQ XML
SEPSloc = 4;                    % SEPS Location in DAQ XML
disp1_trigger_interval = 10000.2;    % Disp Interval to trigger camera on (inches)  (0.2 prev)
load1_trigger_interval = 100010.0;   % Load Interval to trigger camera on (kips) (10.0 prev)
gc_trigger_interval = 300;      % Counter Interval to trigger camera on (NumRamps/this = num samples per ramping)
%%%% 

%% Create a SCRAMNet Object
scr = edu.lehigh.nees.scramnet.ScramNetIO;
scr.initScramnet;
scr.clearSCR;    
%% SCRAMNet Addresses for control bits
SIM_BIT = 0;
PAUSE_BIT = 62;
CAMERA_TRIGGER = 61;

%% Camera trigger
cameraTrigger = 0;

%% Read in DAQ configuration
daqxmlfile = java.io.File('daq.xml');       %Open the DAQ XML config file
daqxml = edu.lehigh.nees.xml.ReadDAQXMLConfig(daqxmlfile); %Parse the DAQ XML file
daq = daqxml.getDAQConfig;                              %Save the configuration

%% Create CSV Writer to log data
csv = edu.lehigh.nees.util.CSVWriter;
dateFormat = java.text.SimpleDateFormat('MM-dd-yyyy--HH-mm-ss');
theDate = java.util.Date;
theDateNow = dateFormat.format(theDate);
csvFilename = ['Output_' datestr(now,'yyyy-mm-dd--HH-MM-SS') '.csv'];
csv.open(csvFilename);
header = {  'Step',
            'Ramp',
            'Trigger',
            'Disp Command (in)',
            'Displacement (in)',
            'Target Rot (rad)',
            'Rot (rad)',
            'Load (kips)',
            'Disp1 - Horz Top (in)',
            'Disp2 - Vert NW (in)',
            'Disp3 - Vert SW (in)',
            'Disp4 - Vert NE (in)',
            'Disp5 - Vert SW (in)',            
            'LC1 - Top West end (kips)',
            'LC2 - Top West mid (kips)',
            'LC3 - Top East mid (kips)',
            'LC4 - Top East end (kips)'};            
csv.writeHeader(header);

% Open the Displacement-Rotation target file
fc = java.io.File(targetFile);
fis = java.io.FileInputStream(fc);
isr = java.io.InputStreamReader(fis);
br = java.io.BufferedReader(isr);

% Read # lines (targets)
numcommands = 0;
while (br.readLine ~= null(0))    
    numcommands = numcommands + 1;
end
% close the file so it can be reopened
br.close;
isr.close;
fis.close;

% Open the file again to get the targets
fis = java.io.FileInputStream(fc);
isr = java.io.InputStreamReader(fis);
br = java.io.BufferedReader(isr);
cmds = (0:numcommands-1);
rots = (0:numcommands-1);
i = 1;
% Store displacement and associated rotational target
while (i <= numcommands)  
    target = br.readLine.split(',');
    cmds(i) = java.lang.Double.parseDouble(target(1));
    rots(i) = java.lang.Double.parseDouble(target(2));
    i = i + 1;
end
% close file
br.close;
isr.close;
fis.close;

%% Show plot of history file to see where we are in test
%load historyFile.csv
%len = length(historyFile(:,1));
%Y = historyFile(:,1);
%X = [1:len];
%plot(X,Y,'r',0,0,'b.');
%grid on;

% Local variables for loop process
previous_disp = 0;  %previous displacement
displacement = 0;   %displacement command
dm = 0;             %measured displacement
dc = 0;             %displ0acement command to go to SCRAMNet
dinc = 0;           %displacement command interval (displacement/N)
target_rot = 0;     %target rotation
rotm = 0;           %measured rotation
load = 0;           %measured load
disp1_trigger_prev = scr.readSCRscaled(disp1_scr,-500) / 25.4; % Baseline for checking displacement change
load1_trigger_prev = scr.readSCRscaled(load1_scr,-2000) / 4.448; % Baseline for checking load change
gc_trigger_prev = scr.readGlobalCounter; % Baseline for checking gc change
direction = 0;      %direction of displacement commands  
accepted = 0;       %if measured rotation is accepted
% For plotting after test is over
dchist = [1:numcommands]; 
dmhist = [1:numcommands];
rothist = [1:numcommands];
rotmhist = [1:numcommands];
i = 1;
while (i <= numcommands)
    dchist(i) = 0;
    dmhist(i) = 0;
    rothist(i) = 0;
    rotmhist(i) = 0;
    i = i + 1;
end

%% Get LVDT  current positions to provide as offset
NWPSoffset = scr.readDAQ(daq.getDaqOffset(NWPSloc),daq.getDaqGain(NWPSloc),daq.getDaqVoffset(NWPSloc),daq.getDaqVslope(NWPSloc),daq.getDaqEUoffset(NWPSloc),daq.getDaqEUslope(NWPSloc));
NEPSoffset = scr.readDAQ(daq.getDaqOffset(NEPSloc),daq.getDaqGain(NEPSloc),daq.getDaqVoffset(NEPSloc),daq.getDaqVslope(NEPSloc),daq.getDaqEUoffset(NEPSloc),daq.getDaqEUslope(NEPSloc));
SWPSoffset = scr.readDAQ(daq.getDaqOffset(SWPSloc),daq.getDaqGain(SWPSloc),daq.getDaqVoffset(SWPSloc),daq.getDaqVslope(SWPSloc),daq.getDaqEUoffset(SWPSloc),daq.getDaqEUslope(SWPSloc));
SEPSoffset = scr.readDAQ(daq.getDaqOffset(SEPSloc),daq.getDaqGain(SEPSloc),daq.getDaqVoffset(SEPSloc),daq.getDaqVslope(SEPSloc),daq.getDaqEUoffset(SEPSloc),daq.getDaqEUslope(SEPSloc));
%% Another telepresence variable for totalPTforce
LC1 = scr.readDAQ(daq.getDaqOffset(5),daq.getDaqGain(5),daq.getDaqVoffset(5),daq.getDaqVslope(5),daq.getDaqEUoffset(5),daq.getDaqEUslope(5));
LC2 = scr.readDAQ(daq.getDaqOffset(6),daq.getDaqGain(6),daq.getDaqVoffset(6),daq.getDaqVslope(6),daq.getDaqEUoffset(6),daq.getDaqEUslope(6));
LC3 = scr.readDAQ(daq.getDaqOffset(7),daq.getDaqGain(7),daq.getDaqVoffset(7),daq.getDaqVslope(7),daq.getDaqEUoffset(7),daq.getDaqEUslope(7));
LC4 = scr.readDAQ(daq.getDaqOffset(8),daq.getDaqGain(8),daq.getDaqVoffset(8),daq.getDaqVslope(8),daq.getDaqEUoffset(8),daq.getDaqEUslope(8));
totalPTforce = LC1 + LC2 + LC3 + LC4;

%% Wait for Sim Running Button (Start)
disp(sprintf('Waiting for User...'));
% Pause for start
disp(sprintf('Hit any key to continue'));
pause;
scr.writeSCR(SIM_BIT, 1);
   
%% Initialize the DSP tick       
tick = scr.readGlobalCounter;
while(tick == scr.readGlobalCounter);end
tick = scr.readGlobalCounter;

%%% Begin loop 
i = 1;              %commands counter for while loop
while (i <= numcommands && scr.readSCR(SIM_BIT) == 1)
    % Pause for cases
    disp(sprintf('Paused... Hit any key to continue'));
    pause;

    % Check if test paused
    while (scr.readSCR(PAUSE_BIT) == 1);end
    
    % Reset accepted measured rotation flag
    accepted = 0;
    
    % Get Next target displacement and rotation
    %plot(X,Y,'r',X(i),Y(i),'b.');
    previous_disp = dc;
    displacement = cmds(i);
    target_rot = rots(i);
    direction = displacement - previous_disp;  
    disp(sprintf('>>Displacement    %i/%i: %f',i,numcommands,displacement));
    disp(sprintf('  Target Rotation      : %f',target_rot));
    if (DEBUG == 1)
        if (direction >= 0)
            disp(sprintf('>>Direction %i: positive',i));
        else
            disp(sprintf('>>Direction %i: negative',i));
        end
    end

    % Measure the LVDTs to compute measured rotation
    if (simulation == 1)
        % Feedback = command and equate rotation
        dm = dc;        
        if (dm >= 0)
            rotm = (dm/L) - ((Mptpos + Ff * rpos)/(L^2)) * ((L^3)/(3 * Es * Ib));
        else
            rotm = (dm/L) + ((Mptneg + Ff * rneg)/(L^2)) * ((L^3)/(3 * Es * Ib));
        end
        if (DEBUG == 1)
            disp(sprintf('  The measured rotation is %f',rotm));
        end
    else
        dm = scr.readSCRscaled(disp1_scr,-500) / 25.4;   
        load = scr.readSCRscaled(load1_scr,-2000) / 4.448;
        NWPS = scr.readDAQ(daq.getDaqOffset(NWPSloc),daq.getDaqGain(NWPSloc),daq.getDaqVoffset(NWPSloc),daq.getDaqVslope(NWPSloc),daq.getDaqEUoffset(NWPSloc),daq.getDaqEUslope(NWPSloc));
        NEPS = scr.readDAQ(daq.getDaqOffset(NEPSloc),daq.getDaqGain(NEPSloc),daq.getDaqVoffset(NEPSloc),daq.getDaqVslope(NEPSloc),daq.getDaqEUoffset(NEPSloc),daq.getDaqEUslope(NEPSloc));
        SWPS = scr.readDAQ(daq.getDaqOffset(SWPSloc),daq.getDaqGain(SWPSloc),daq.getDaqVoffset(SWPSloc),daq.getDaqVslope(SWPSloc),daq.getDaqEUoffset(SWPSloc),daq.getDaqEUslope(SWPSloc));
        SEPS = scr.readDAQ(daq.getDaqOffset(SEPSloc),daq.getDaqGain(SEPSloc),daq.getDaqVoffset(SEPSloc),daq.getDaqVslope(SEPSloc),daq.getDaqEUoffset(SEPSloc),daq.getDaqEUslope(SEPSloc));
        delta_gap_neg = ((NWPS - NWPSoffset) + (NEPS - NEPSoffset))/2;
        delta_gap_pos = ((SEPS - SEPSoffset) + (SWPS - SWPSoffset))/2;
        rotm = (delta_gap_neg - delta_gap_pos)/20.75;        
        if (DEBUG == 1)
            disp(sprintf('  The measured rotation is %f',rotm));
        end
        LC1 = scr.readDAQ(daq.getDaqOffset(5),daq.getDaqGain(5),daq.getDaqVoffset(5),daq.getDaqVslope(5),daq.getDaqEUoffset(5),daq.getDaqEUslope(5));
        LC2 = scr.readDAQ(daq.getDaqOffset(6),daq.getDaqGain(6),daq.getDaqVoffset(6),daq.getDaqVslope(6),daq.getDaqEUoffset(6),daq.getDaqEUslope(6));
        LC3 = scr.readDAQ(daq.getDaqOffset(7),daq.getDaqGain(7),daq.getDaqVoffset(7),daq.getDaqVslope(7),daq.getDaqEUoffset(7),daq.getDaqEUslope(7));
        LC4 = scr.readDAQ(daq.getDaqOffset(8),daq.getDaqGain(8),daq.getDaqVoffset(8),daq.getDaqVslope(8),daq.getDaqEUoffset(8),daq.getDaqEUslope(8));
        totalPTforce = LC1 + LC2 + LC3 + LC4;
    end
    
    % Check rotation within tolerance
    % If the rotational target is 0 ignore this check
    if (target_rot ~= 0)
        % With positive direction, measured rotation is within tolerance once
        % it passes the target minus epsilon
        if (direction > 0 && (rotm >= target_rot-epsilon))
            accepted = 1;
            if (DEBUG == 1)
                disp(sprintf('  Accepted Rotation'));
            end
        % With negative direction, measured rotation is within tolerance once
        % it passes the target plus epsilon
        elseif (direction < 0 && (rotm <= target_rot+epsilon))
            accepted = 1;
            if (DEBUG == 1)
                disp(sprintf('  Accepted Rotation'));
            end
        else
            % Not within any tolerance
            accepted = 0;
        end
    end
           
    % Begin ramps until measured rotational target is met
    % Or if not converging to rotation, until ramps are over
    if (accepted ~= 1) 
        N = NumRamps;               %get default # ramping steps
        dinc = (displacement - previous_disp)/N;      %get displacement intervals for ramping
        j = 1;                      %ramp counter
        % Start ramp  
        if (DEBUG == 1)
            disp(sprintf('***Start Ramping***'));
        end
        % Loop until the ramp is over or stopped
        while (j <= N && scr.readSCR(SIM_BIT) == 1)   
            % Check if test paused
            while (scr.readSCR(PAUSE_BIT) == 1);end
                        
            if (DEBUG == 1)
                disp(sprintf('<Command: %i = (%f in,%f rot), Ramp Step: %i>',i,displacement,target_rot,j));   
            end
            
            % Calculate displacement
            dc = dc + dinc;     % add increment to current location to get next location
            if (DEBUG == 1)
                disp(sprintf('   The displacement command is %f',dc));
            end                        

            % Impose displacement
            if (DEBUG == 1)
                disp(sprintf('   Imposing displacement command of %f',dc));
            end
            %% Convert to Millimeters and scale by 500
            %% Also check limits
            if (dc > disp1_cmd_UpperLimit || dc < disp1_cmd_LowerLimit)
                if ('y' == input(sprintf('COMMAND LIMIT EXCEEDED, %f.\nContinue? (y/n)',dc),'s'))
                    scr.writeSCRscaled(disp1_cmd_scr,dc * 25.4,-500);
                else
                    return
                end
            else
                scr.writeSCRscaled(disp1_cmd_scr,dc * 25.4,-500);
            end
    
            % Wait for clock tick then increment
            while(tick == scr.readGlobalCounter);end 
            %%if (scr.readGlobalCounter > tick + 1) 
                %%disp(sprintf('%i',scr.readGlobalCounter - tick + 1));
            %%end
            tick = scr.readGlobalCounter;

            % Measure the LVDTs to compute measured rotation
            if (simulation == 1)
                % Feedback = command and equate rotation
                dm = dc;
                if (dm >= 0)
                    rotm = (dm/L) - ((Mptpos + Ff * rpos)/(L^2)) * ((L^3)/(3 * Es * Ib));
                else
                    rotm = (dm/L) + ((Mptneg + Ff * rneg)/(L^2)) * ((L^3)/(3 * Es * Ib));
                end
                if (DEBUG == 1)
                    disp(sprintf('  The measured rotation is %f',rotm));
                end
            else
                dm = scr.readSCRscaled(disp1_scr,-500) / 25.4;
                load = scr.readSCRscaled(load1_scr,-2000) / 4.448;
                NWPS = scr.readDAQ(daq.getDaqOffset(NWPSloc),daq.getDaqGain(NWPSloc),daq.getDaqVoffset(NWPSloc),daq.getDaqVslope(NWPSloc),daq.getDaqEUoffset(NWPSloc),daq.getDaqEUslope(NWPSloc));
                NEPS = scr.readDAQ(daq.getDaqOffset(NEPSloc),daq.getDaqGain(NEPSloc),daq.getDaqVoffset(NEPSloc),daq.getDaqVslope(NEPSloc),daq.getDaqEUoffset(NEPSloc),daq.getDaqEUslope(NEPSloc));
                SWPS = scr.readDAQ(daq.getDaqOffset(SWPSloc),daq.getDaqGain(SWPSloc),daq.getDaqVoffset(SWPSloc),daq.getDaqVslope(SWPSloc),daq.getDaqEUoffset(SWPSloc),daq.getDaqEUslope(SWPSloc));
                SEPS = scr.readDAQ(daq.getDaqOffset(SEPSloc),daq.getDaqGain(SEPSloc),daq.getDaqVoffset(SEPSloc),daq.getDaqVslope(SEPSloc),daq.getDaqEUoffset(SEPSloc),daq.getDaqEUslope(SEPSloc));
                delta_gap_neg = ((NWPS - NWPSoffset) + (NEPS - NEPSoffset))/2;
                delta_gap_pos = ((SEPS - SEPSoffset) + (SWPS - SWPSoffset))/2;
                rotm = (delta_gap_neg - delta_gap_pos)/20.75;
                if (DEBUG == 1)
                    disp(sprintf('  The measured rotation is %f',rotm));
                end
                LC1 = scr.readDAQ(daq.getDaqOffset(5),daq.getDaqGain(5),daq.getDaqVoffset(5),daq.getDaqVslope(5),daq.getDaqEUoffset(5),daq.getDaqEUslope(5));
                LC2 = scr.readDAQ(daq.getDaqOffset(6),daq.getDaqGain(6),daq.getDaqVoffset(6),daq.getDaqVslope(6),daq.getDaqEUoffset(6),daq.getDaqEUslope(6));
                LC3 = scr.readDAQ(daq.getDaqOffset(7),daq.getDaqGain(7),daq.getDaqVoffset(7),daq.getDaqVslope(7),daq.getDaqEUoffset(7),daq.getDaqEUslope(7));
                LC4 = scr.readDAQ(daq.getDaqOffset(8),daq.getDaqGain(8),daq.getDaqVoffset(8),daq.getDaqVslope(8),daq.getDaqEUoffset(8),daq.getDaqEUslope(8));
                totalPTforce = LC1 + LC2 + LC3 + LC4;
            end    
                        
            % Check triggers
            % If any trigger hits, write data to file                        
            % Trigger cameras depending on milestones 
            if (abs(dm - disp1_trigger_prev) > disp1_trigger_interval)
                scr.writeSCRint(CAMERA_TRIGGER,cameraTrigger);
                cameraTrigger = cameraTrigger + 1;
                %%disp(sprintf('Displacement trigger'));
                disp1_trigger_prev = dm;
            elseif (abs(load - load1_trigger_prev) > load1_trigger_interval)
                scr.writeSCRint(CAMERA_TRIGGER,cameraTrigger);
                cameraTrigger = cameraTrigger + 1;
                %%disp(sprintf('Load trigger'));
                load1_trigger_prev = load;    
            elseif (abs(scr.readGlobalCounter - gc_trigger_prev) > gc_trigger_interval)
                % Write data to CSV file
                data = [i,
                        j,
                        cameraTrigger,
                        dc,
                        dm,
                        target_rot,
                        rotm,
                        load,
                        scr.readDAQ(daq.getDaqOffset(0),daq.getDaqGain(0),daq.getDaqVoffset(0),daq.getDaqVslope(0),daq.getDaqEUoffset(0),daq.getDaqEUslope(0)),
                        scr.readDAQ(daq.getDaqOffset(1),daq.getDaqGain(1),daq.getDaqVoffset(1),daq.getDaqVslope(1),daq.getDaqEUoffset(1),daq.getDaqEUslope(1)),
                        scr.readDAQ(daq.getDaqOffset(2),daq.getDaqGain(2),daq.getDaqVoffset(2),daq.getDaqVslope(2),daq.getDaqEUoffset(2),daq.getDaqEUslope(2)),
                        scr.readDAQ(daq.getDaqOffset(3),daq.getDaqGain(3),daq.getDaqVoffset(3),daq.getDaqVslope(3),daq.getDaqEUoffset(3),daq.getDaqEUslope(3)),
                        scr.readDAQ(daq.getDaqOffset(4),daq.getDaqGain(4),daq.getDaqVoffset(4),daq.getDaqVslope(4),daq.getDaqEUoffset(4),daq.getDaqEUslope(4)),
                        scr.readDAQ(daq.getDaqOffset(5),daq.getDaqGain(5),daq.getDaqVoffset(5),daq.getDaqVslope(5),daq.getDaqEUoffset(5),daq.getDaqEUslope(5)),
                        scr.readDAQ(daq.getDaqOffset(6),daq.getDaqGain(6),daq.getDaqVoffset(6),daq.getDaqVslope(6),daq.getDaqEUoffset(6),daq.getDaqEUslope(6)),
                        scr.readDAQ(daq.getDaqOffset(7),daq.getDaqGain(7),daq.getDaqVoffset(7),daq.getDaqVslope(7),daq.getDaqEUoffset(7),daq.getDaqEUslope(7)),
                        scr.readDAQ(daq.getDaqOffset(8),daq.getDaqGain(8),daq.getDaqVoffset(8),daq.getDaqVslope(8),daq.getDaqEUoffset(8),daq.getDaqEUslope(8))];                
                csv.write(data);
                % Write SCRAMNet data
                scr.writeSCR(2000,load);        % Load (kips)
                scr.writeSCR(2001,dm);          % Displacement (inches)
                scr.writeSCR(2002,load * L);    % Moment
                scr.writeSCR(2003,rotm);        % Theta r
                scr.writeSCR(2004,totalPTforce);% Total PT Force (kips)                
                scr.writeSCR(2005,i);           % Step
                
                scr.writeSCRint(CAMERA_TRIGGER,cameraTrigger);
                cameraTrigger = cameraTrigger + 1;
                %%disp(sprintf('Global Counter trigger'));
                gc_trigger_prev = scr.readGlobalCounter;  
            else ;
            end            
            
            % Store data for Plots
            dchist(i) = dc;
            dmhist(i) = dm;
            rothist(i) = target_rot;
            rotmhist(i) = rotm;            
            
            % Check if within tolerance
            % If the rotational target is 0 ignore
            if (target_rot ~= 0)
                % With positive direction, measured rotation is within tolerance once
                % it passes the target minus epsilon
                if (direction > 0 && (rotm >= target_rot-epsilon))
                    accepted = 1;
                    if (DEBUG == 1)
                        disp(sprintf('  Accepted Rotation'));
                    end
                    break;
                % With negative direction, measured rotation is within tolerance once
                % it passes the target plus epsilon
                elseif (direction < 0 && (rotm <= target_rot+epsilon))
                    accepted = 1;
                    if (DEBUG == 1)
                        disp(sprintf('  Accepted Rotation'));
                    end
                    break;
                else
                    % Not within any tolerance
                    accepted = 0;
                    %% Continue Ramp
                    j = j + 1;
                    if (j > N)
                        N = N + 1;                                     
                    end
                end
            else
                % Impose displacement when target rotation = 0
                if (j <= N)
                    j = j + 1;
                end
            end
        end
    end
    % Next targets
    i = i + 1;
end

% No more commands!

%% End Simulation by writing 0.0 to SIM RUNNING 
scr.writeSCR(SIM_BIT, 0);

%% Plots
figure(1)
plot(1:numcommands,dchist(1:numcommands),1:numcommands,dmhist(1:numcommands))
xlabel 'Step';
ylabel 'Displacement, In';
legend('Command','Measured');
title('Displacement');
grid on;
figure(2)
plot(1:numcommands,rothist(1:numcommands),1:numcommands,rotmhist(1:numcommands))
xlabel 'Step';
ylabel 'Rotation';
legend('Target','Measured');
title('Rotation');
grid on;

%% Clear SCRAMNet
scr.unmapScramnet;
clear scr;
clear daq;
clear daqxml;

%% Clean Up
csv.close;
clear csv;
clear fc;
clear fis;
clear isr;
clear br;
disp(sprintf('DONE!'));
save SteelColumn;
