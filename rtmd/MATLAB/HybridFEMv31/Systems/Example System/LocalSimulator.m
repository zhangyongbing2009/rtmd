%% Hybrid Simulation Main Program
%  This program loads a configuration file that contains information about
%  the structure, elements, nodes, materials and integration algorithm.
%% Set the files used for this program
INP_File = 'FRAME.INP';
EQ_File = 'EQ.txt';

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
% store displacement command and restoring force results
DisplacementResults = zeros(Integrator.Steps, Structure.NumFreeDOF);
RestoringForceResults = zeros(Integrator.Steps, Structure.NumFreeDOF);

disp(['[FEM] Configuration  : ' INP_File]);
disp('[FEM] Structure');
disp(sprintf('[FEM]  Nodes         : %i',Structure.NumNodes));
disp(sprintf('[FEM]  Elements      : %i',Structure.NumElements));
disp(sprintf('[FEM]  NumFreeDOF    : %i',Structure.NumFreeDOF));
disp(['[FEM] EQ History     : ',EQ_File]);
disp(sprintf('[FEM] Steps          : %i',Integrator.Steps));
disp(sprintf('[FEM] Timestep       : %f seconds',Integrator.Timestep));
disp('[FEM] Setup complete');
disp('[FEM] Running...');

%% Dynamic steps of the integration algorithm
for step = 1:Integrator.Steps
    % Generate Commands and store them in the Results variable
    Integrator = GenerateCommands(Integrator);
    DisplacementResults(step,:) = Integrator.Displacement';
    % Calculate Restoring Forces per Element and form full structure
    % restoring force vector
    Integrator.RestoringForce = zeros(Structure.NumFreeDOF, 1);
    for element = 1:Structure.NumElements;
        %get the resisting force of each element
        [Elements{element}, ElementRF] = Elements{element}.GetRestoringForce(Elements{element}, Structure, Integrator); 
        % Get element nodes to assemble in the structure restoring force
        % vector
        dof=zeros(6,1);
        dof(1,1)=Elements{element}.Nodes(1).UX;
        dof(2,1)=Elements{element}.Nodes(1).UY;
        dof(3,1)=Elements{element}.Nodes(1).THETA;
        dof(4,1)=Elements{element}.Nodes(2).UX;
        dof(5,1)=Elements{element}.Nodes(2).UY;
        dof(6,1)=Elements{element}.Nodes(2).THETA;
        for i=1:6
            if dof(i,1)<=Structure.NumFreeDOF
              Integrator.RestoringForce(dof(i,1), 1) = ElementRF(i, 1) + Integrator.RestoringForce(dof(i,1), 1);
            end
        end          
    end
    
    % Store restoring force results
    RestoringForceResults(step,:) = Integrator.RestoringForce';
    
    % Update Acceleration Vector
    Integrator = UpdateAcceleration(Integrator, Structure, step);
    
    Commands = Integrator.Displacement;
end

% plot results
subplot(2,1,1);
sp1 = plot(DisplacementResults);
title('Displacement');
subplot(2,1,2);
sp2 = plot(RestoringForceResults);
title('Restoring Force');