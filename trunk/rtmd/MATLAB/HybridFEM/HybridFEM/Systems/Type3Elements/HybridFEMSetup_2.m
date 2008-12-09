clear;clc;
%% Hybrid FEM Control Main Program
%  This program loads a configuration file that contains information about
%  the structure, elements, nodes, materials and integration algorithm.

%% Set the files used for this program
INP_File = 'FRAME_2.INP';
EQ_File = 'EQ_2.txt';
addpath('../../');  % Required to get the package M-files.  Make sure this path is valid

%% Load Configuration
%  This reads the configuration and sets up the MATLAB workspace objects
%  Structure contains information about the overall structure
%  Elements contains the element information and its nodes and materials
%  Integrator contains properties of the integration method
[Structure, Elements, Integrator] = LoadConfiguration(INP_File);

%% Calculate Bandwidth of Structure
Structure = CalculateBandwidth(Structure, Elements);

%% Calculate Matrices with respect to free degrees of freedom 
Structure = CalculateMatrices(Structure, Elements);
%% Optional (comment the following command for hybrid simulations) Eigen analysis
d=eig(Structure.StiffnessMatrixFree,Structure.MassMatrixFree);
%disp(d);

%% Load the EQ Record
Integrator = GetEQHistory(EQ_File, Integrator, Structure);

%% Load Integration Parameters and set Initial Conditions
Integrator = InitializeIntegrator(Integrator, Structure);

%% Set sample rate and execution time
SampleRate = Integrator.Timestep;
RunningTime = SampleRate * (Integrator.Steps-1);

disp(['[FEM] Configuration : ' INP_File]);
disp('[FEM] Structure');
disp(sprintf('[FEM]  Nodes        : %i',Structure.NumNodes));
disp(sprintf('[FEM]  Elements     : %i',Structure.NumElements));
disp(['[FEM] EQ History    : ',EQ_File]);
disp(sprintf('[FEM] Steps         : %i',Integrator.Steps));
disp(sprintf('[FEM] Timestep      : %f seconds',Integrator.Timestep));
disp(sprintf('[FEM] Running Time  : %f seconds',RunningTime));
disp('[FEM] Setup complete');
disp('[FEM] Run Simulink model or compile for xPC now');
disp('[FEM] Running Simulink model... please wait');
sim('HybridFEMModel_2')

plot(Results);
