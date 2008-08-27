clear;
clc;
close all;

%% Server properties
Mon.ControllerIP = '128.180.53.20';
Mon.ControllerPort = 8091;

%% Create a SCRAMNet Object
Mon.scr = edu.lehigh.nees.scramnet.ScramNetIO;
Mon.scr.initScramnet; 

%% SCRAMNet memory mapping
Mon.disp1_acmd_in_scr = 1;          % Disp 1 Actuator Command
Mon.disp1_cmd_in_scr = 2;           % Disp 1 Relative Command
Mon.disp1_in_scr = 3;               % Disp 1 
Mon.disp1_in_off_scr = 4;           % Disp 1 offset
Mon.load1_k_scr = 5;                % Load 1

Mon.disp2_acmd_in_scr = 8;          % Disp 2 Actuator Command
Mon.disp2_cmd_in_scr = 9;           % Disp 2 Relative Command
Mon.disp2_in_scr = 10;              % Disp 2 
Mon.disp2_in_off_scr = 11;          % Disp 2 offset
Mon.load2_k_scr = 12;               % Load 2

Mon.disp3_acmd_in_scr = 16;         % Disp 3 Actuator Command
Mon.disp3_cmd_in_scr = 17;          % Disp 3 Relative Command
Mon.disp3_in_scr = 18;              % Disp 3 
Mon.disp3_in_off_scr = 19;          % Disp 3 offset
Mon.load3_k_scr = 20;               % Load 3

Mon.disp4_acmd_in_scr = 24;         % Disp 4 Actuator Command
Mon.disp4_cmd_in_scr = 25;          % Disp 4 Relative Command
Mon.disp4_in_scr = 26;              % Disp 4 
Mon.disp4_in_off_scr = 27;          % Disp 4 offset
Mon.load4_k_scr = 28;               % Load 4

Mon.step_scr = 32;                  % Step
Mon.trigger_scr = 61;               % Trigger

%% Read in DAQ configuration
daqfile = [pwd '\daq config\daq.xml'];
daqxmlfile = java.io.File(daqfile);                 %Open the DAQ XML config file
daqxml = edu.lehigh.nees.xml.ReadDAQXMLConfig(daqxmlfile); %Parse the DAQ XML file
Mon.daq = daqxml.getDAQConfig;                              %Save the configuration
Mon.numdaqchannels = Mon.daq.getnumDaqBlocks;           %Get number of daq channels
% Get the channel configuration 
for d = 0 : Mon.numdaqchannels-1
    Mon.daqchannelinfo.name(d+1) = Mon.daq.getDaqName(d);
    Mon.daqchannelinfo.offset(d+1) = Mon.daq.getDaqOffset(d);
    Mon.daqchannelinfo.gain(d+1) = Mon.daq.getDaqGain(d);
    Mon.daqchannelinfo.Voffset(d+1) = Mon.daq.getDaqVoffset(d);
    Mon.daqchannelinfo.Vslope(d+1) = Mon.daq.getDaqVslope(d);
    Mon.daqchannelinfo.EUoffset(d+1) = Mon.daq.getDaqEUoffset(d);
    Mon.daqchannelinfo.EUslope(d+1) = Mon.daq.getDaqEUslope(d);
end

%% Start Monitor GUI
disp('Hit CTRL-C to stop');
PanelMonitor(Mon);