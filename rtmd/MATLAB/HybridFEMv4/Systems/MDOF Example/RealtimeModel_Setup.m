%% Hybrid FEM Control Main Program
%  This program loads a configuration file that contains information about
%  the structure, elements, nodes, materials and integration algorithm.

%% Set the files used for this program
INP_File = 'Frame.txt';
EQ_File = 'canogaT10.txt';

%% Load Configuration
%  This reads the configuration and sets up the MATLAB workspace objects
%  Structure contains information about the overall structure
%  Elements contains the element information and its nodes and materials
%  Integrator contains properties of the integration method
[Structure, Elements, Integrator] = LoadConfiguration(INP_File);

%% Calculate Matrices with respect to free degrees of freedom 
Structure = CalculateMatrices(Structure, Elements);
NumFreeDOF = Structure.NumFreeDOF;  % Necessary for forming total RF

%% Eigen analysis to obtain structure frequencies
d=eig(Structure.StiffnessMatrixFree,Structure.MassMatrixFree);
system_frequencies = sqrt(d)/2/pi;

%% Load the EQ Record
Integrator = GetEQHistory(EQ_File, Integrator, Structure, Elements);

%% Load Integration Parameters and set Initial Conditions
Integrator = InitializeIntegrator(Integrator, Structure);

%% Set sample rate and execution time
SampleRate = Integrator.Timestep;
sample = 1/1024;
Interpolations = SampleRate/sample;
RunningTime = SampleRate * Integrator.Steps;

disp(['[FEM] Configuration  : ' INP_File]);
disp('[FEM] Structure');
disp(sprintf('[FEM]  Nodes         : %i',Structure.NumNodes));
disp(sprintf('[FEM]  Elements      : %i',Structure.NumElements));
disp(sprintf('[FEM]  NumFreeDOF    : %i',Structure.NumFreeDOF));
disp(sprintf('[FEM]  Frequencies   : %f Hz\n',system_frequencies));
disp(['[FEM] EQ History     : ',EQ_File]);
disp(sprintf('[FEM] Steps          : %i',Integrator.Steps));
disp(sprintf('[FEM] Timestep       : %f seconds',Integrator.Timestep));
disp(sprintf('[FEM] Sample Rate    : %f seconds',sample));
disp(sprintf('[FEM] Running Time   : %f seconds',RunningTime));
disp('[FEM] Setup complete');
disp('[FEM] Run Simulink model or compile for xPC now');