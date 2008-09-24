clear;
clc;
close all;

%% Hybrid model properties
% Number of degrees of freedom for overall structure
Vars.nDOF = 4;

% Matrices 
Vars.M = [0.977  0       0       0;
         0      0.971   0       0;
         0      0       0.971   0;
         0      0       0       1.056];
Vars.Minv = inv(Vars.M);     %Mass matrix inverse

Vars.C = [1.301      -0.861      0.234       -0.038;
        -0.84     1.562       -0.889      0.197;
         0.212     -0.894      1.342      -0.482;
         -0.029     0.193       -0.484      0.498];
Vars.Co = Vars.C;
     
Vars.K = [1079       -802.544    210.252     -31.993;
         -809.295   1281      -787.293    172.234;
         244.119    -813.522    1042       -432.975;
         -39.681    167.941     -425.753    285.486];
     
Vars.Kgc = [292.684 -265.467 114.561 -19.198;
            -265.467 388.292 -278.524 80.004;
            114.561 -278.524 293.983 -111.513;
            -19.198 80.004 -111.513 47.603];

Vars.Ka = Vars.Kgc;    % Analytical Substructure     

% Timestep parameters
%Vars.dt = sqrt(0.6) * 0.02;                %Timestep for integration algorithm
Vars.dt = 0.015492;

% Integration parameters
Vars.beta1=(4*Vars.M+2*Vars.C*Vars.dt+Vars.K*Vars.dt^2)^(-1)*4*Vars.M;
Vars.beta2=Vars.beta1;

% External excitation force loading
Vars.DOF = ones(Vars.nDOF,1);                 % Define the DOF nodes
load 'history files\H_CXO225_FOE.txt';            %ground acceleration record
scalefactor = 1;
Vars.F=-386*scalefactor*Vars.M*Vars.DOF*H_CXO225_FOE';   %external excitation force calculation
shift=zeros(Vars.nDOF,1);
Vars.F=cat(2,shift,Vars.F);
Vars.steps=length(Vars.F(1,:));

% Initial Conditions are set to 0
Vars.dpred=zeros(Vars.nDOF,1);
Vars.u=zeros(Vars.nDOF,1);
Vars.ud=zeros(Vars.nDOF,1);
Vars.uold=zeros(Vars.nDOF,1);
Vars.drift=zeros(Vars.nDOF,1);
Vars.r=zeros(Vars.nDOF,1);
Vars.re=zeros(Vars.nDOF,1);
Vars.ra=zeros(Vars.nDOF,1);
Vars.um=zeros(Vars.nDOF,1);
Vars.udd=zeros(Vars.nDOF,1);
Vars.HybridStepCount = 1;

%% Arrays for holding results
Vars.results_commands=zeros(Vars.steps,Vars.nDOF);
Vars.results_drift=zeros(Vars.steps,Vars.nDOF);
Vars.results_force_exp=zeros(Vars.steps,Vars.nDOF);
Vars.results_force_ana=zeros(Vars.steps,Vars.nDOF);
Vars.results_force=zeros(Vars.steps,Vars.nDOF);

%% Create Spring Analytical Substructures
Vars.SpringA = SpringInitialization(242, 23, 126.7755, 0.24, 0, 0, 0);
Vars.SpringB = SpringInitialization(255, 13, 116.8522, 0.26, 0, 0, 0);
Vars.SpringC = SpringInitialization(236,  9, 94.13875, 0.28, 0, 0, 0);
Vars.SpringD = SpringInitialization(164,  8, 56.60981, 0.29, 0, 0, 0);

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
daqfile = [pwd '\daq config\daq.xml'];
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
Vars.LVDTnoise = 0.008; % Global LVDT noise threshold in inches
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
HybridCR(Vars);
