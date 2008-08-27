clear;
clc;
close all;

%% Server properties
import java.net.*;
import java.io.*;
Vars.ControllerIP = '128.180.53.20';
Vars.ControllerPort = 8090;
Vars.socket = Socket(Vars.ControllerIP,Vars.ControllerPort);
Vars.in = Vars.socket.getInputStream;
Vars.reader = InputStreamReader(Vars.in);
Vars.out = Vars.socket.getOutputStream;

%% Create a SCRAMNet Object
Vars.scr = edu.lehigh.nees.scramnet.ScramNetIO;
Vars.scr.initScramnet; 

%% SCRAMNet memory mapping
Vars.disp1_acmd_in_scr = 1;          % Disp 1 Actuator Command
Vars.disp1_cmd_in_scr = 2;           % Disp 1 Relative Command
Vars.disp1_in_scr = 3;               % Disp 1 
Vars.disp1_in_off_scr = 4;           % Disp 1 offset
Vars.load1_k_scr = 5;                % Load 1

Vars.disp2_acmd_in_scr = 8;          % Disp 2 Actuator Command
Vars.disp2_cmd_in_scr = 9;           % Disp 2 Relative Command
Vars.disp2_in_scr = 10;              % Disp 2 
Vars.disp2_in_off_scr = 11;          % Disp 2 offset
Vars.load2_k_scr = 12;               % Load 2

Vars.disp3_acmd_in_scr = 16;         % Disp 3 Actuator Command
Vars.disp3_cmd_in_scr = 17;          % Disp 3 Relative Command
Vars.disp3_in_scr = 18;              % Disp 3 
Vars.disp3_in_off_scr = 19;          % Disp 3 offset
Vars.load3_k_scr = 20;               % Load 3

Vars.disp4_acmd_in_scr = 24;         % Disp 4 Actuator Command
Vars.disp4_cmd_in_scr = 25;          % Disp 4 Relative Command
Vars.disp4_in_scr = 26;              % Disp 4 
Vars.disp4_in_off_scr = 27;          % Disp 4 offset
Vars.load4_k_scr = 28;               % Load 4

Vars.step_scr = 32;                  % Step
Vars.SIM_BIT = 0;
Vars.PAUSE_BIT = 62;
Vars.CAMERA_TRIGGER = 61;

Vars.F1PT_scr = 2000;                % 1st Floor Post Tensioning
Vars.F2PT_scr = 2001;                % 2nd Floor Post Tensioning
Vars.F3PT_scr = 2002;                % 3rd Floor Post Tensioning
Vars.F4PT_scr = 2003;                % 4th Floor Post Tensioning
Vars.F1Drift_scr = 2004;             % 1st Floor Drift
Vars.F2Drift_scr = 2005;             % 2nd Floor Drift
Vars.F3Drift_scr = 2006;             % 3rd Floor Drift
Vars.F4Drift_scr = 2007;             % 4th Floor Drift
Vars.F1StoryShear_scr = 2008;        % 1st Floor Story Shear
Vars.F2StoryShear_scr = 2009;        % 2nd Floor Story Shear
Vars.F3StoryShear_scr = 2010;        % 3rd Floor Story Shear
Vars.F4StoryShear_scr = 2011;        % 4th Floor Story Shear

%% Camera Trigger counter
Vars.cameratrigger = 1;

%% Read in DAQ configuration
daqfile = 'C:\RTMD\workspace\MRF2\daq config\daq.xml';
daqxmlfile = java.io.File(daqfile);                 %Open the DAQ XML config file
daqxml = edu.lehigh.nees.xml.ReadDAQXMLConfig(daqxmlfile); %Parse the DAQ XML file
Vars.daq = daqxml.getDAQConfig;                              %Save the configuration
Vars.numdaqchannels = Vars.daq.getnumDaqBlocks;           %Get number of daq channels
% Get the channel configuration 
for d = 0 : Vars.numdaqchannels-1
    Vars.daqchannelinfo.name(d+1) = Vars.daq.getDaqName(d);
    Vars.daqchannelinfo.offset(d+1) = Vars.daq.getDaqOffset(d);
    Vars.daqchannelinfo.gain(d+1) = Vars.daq.getDaqGain(d);
    Vars.daqchannelinfo.Voffset(d+1) = Vars.daq.getDaqVoffset(d);
    Vars.daqchannelinfo.Vslope(d+1) = Vars.daq.getDaqVslope(d);
    Vars.daqchannelinfo.EUoffset(d+1) = Vars.daq.getDaqEUoffset(d);
    Vars.daqchannelinfo.EUslope(d+1) = Vars.daq.getDaqEUslope(d);
end

%% LVDT configuration for control
% LVDT locations in daqxml for control
Vars.LVDT1loc = 27; % LVDT1 daq location 
Vars.LVDT2loc = 28; % LVDT2 daq location
Vars.LVDT3loc = 29; % LVDT3 daq location 
Vars.LVDT4loc = 30; % LVDT4 daq location

% Read in initial values
Vars.LVDT1 = Vars.scr.readDAQ(Vars.daqchannelinfo.offset(Vars.LVDT1loc),Vars.daqchannelinfo.gain(Vars.LVDT1loc),Vars.daqchannelinfo.Voffset(Vars.LVDT1loc),Vars.daqchannelinfo.Vslope(Vars.LVDT1loc),Vars.daqchannelinfo.EUoffset(Vars.LVDT1loc),Vars.daqchannelinfo.EUslope(Vars.LVDT1loc));        
Vars.LVDT2 = Vars.scr.readDAQ(Vars.daqchannelinfo.offset(Vars.LVDT2loc),Vars.daqchannelinfo.gain(Vars.LVDT2loc),Vars.daqchannelinfo.Voffset(Vars.LVDT2loc),Vars.daqchannelinfo.Vslope(Vars.LVDT2loc),Vars.daqchannelinfo.EUoffset(Vars.LVDT2loc),Vars.daqchannelinfo.EUslope(Vars.LVDT2loc));        
Vars.LVDT3 = Vars.scr.readDAQ(Vars.daqchannelinfo.offset(Vars.LVDT3loc),Vars.daqchannelinfo.gain(Vars.LVDT3loc),Vars.daqchannelinfo.Voffset(Vars.LVDT3loc),Vars.daqchannelinfo.Vslope(Vars.LVDT3loc),Vars.daqchannelinfo.EUoffset(Vars.LVDT3loc),Vars.daqchannelinfo.EUslope(Vars.LVDT3loc));        
Vars.LVDT4 = Vars.scr.readDAQ(Vars.daqchannelinfo.offset(Vars.LVDT4loc),Vars.daqchannelinfo.gain(Vars.LVDT4loc),Vars.daqchannelinfo.Voffset(Vars.LVDT4loc),Vars.daqchannelinfo.Vslope(Vars.LVDT4loc),Vars.daqchannelinfo.EUoffset(Vars.LVDT4loc),Vars.daqchannelinfo.EUslope(Vars.LVDT4loc));        

% Global configuration for LVDTs
Vars.LVDTnoise = 0.005; % Global LVDT noise threshold in inches
Vars.LVDTerror = 1.0; % Global LVDT error detect

%% Read initial displacement offsets
Vars.ch1offset = Vars.scr.readFloat(Vars.disp1_in_scr);
Vars.ch2offset = Vars.scr.readFloat(Vars.disp2_in_scr);
Vars.ch3offset = Vars.scr.readFloat(Vars.disp3_in_scr);
Vars.ch4offset = Vars.scr.readFloat(Vars.disp4_in_scr);
disp(sprintf('Using actuator offsets:\n ch1 = %fin\n ch2 = %fin\n ch3 = %fin\n ch4 = %fin\n',Vars.ch1offset,Vars.ch2offset,Vars.ch3offset,Vars.ch4offset));
Vars.ch1lvdtoffset = Vars.LVDT1;
Vars.ch2lvdtoffset = Vars.LVDT2;
Vars.ch3lvdtoffset = Vars.LVDT3;
Vars.ch4lvdtoffset = Vars.LVDT4;
disp(sprintf('Using LVDT offsets:\n lvdt1 = %fin\n lvdt2 = %fin\n lvdt3 = %fin\n lvdt4 = %fin\n',Vars.ch1lvdtoffset,Vars.ch2lvdtoffset,Vars.ch3lvdtoffset,Vars.ch4lvdtoffset));

% Store initial offsets
fid = fopen(['results\InitOffsets_' datestr(now,'yyyy-mm-dd--HH-MM-SS') '_offsets.txt'],'w');
fprintf(fid, 'ch1offset: %f\nch2offset: %f\nch3offset: %f\nch4offset: %f\n',Vars.ch1offset,Vars.ch2offset,Vars.ch3offset,Vars.ch4offset);
fclose(fid);
clear fid;

%% Start GUI
disp('Initialization Completed');
%Set the SIM BIT state to 1 for running
Vars.scr.writeFloat(Vars.SIM_BIT, 1);

%Pass the global variables to the GUI program
PredTarget(Vars);
