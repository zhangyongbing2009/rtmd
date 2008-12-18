%% Hybrid Simulation Main Program
%  This program loads a configuration file that contains information about
%  the structure, elements, nodes, materials and integration algorithm.

%% Load Configuration
%  This reads the configuration and sets up the MATLAB workspace objects
%  Structure contains information about the overall structure
%  Elements contains the element information and its nodes and materials
%  Integrator contains properties of the integration method
[Structure, Elements, Integrator] = LoadConfiguration('2Story_Frame.txt');

%% Calculate Bandwidth of Structure
Structure = CalculateBandwidth(Structure, Elements);

%% Calculate Matrices with respect to free degrees of freedom 
Structure = CalculateMatrices(Structure, Elements);
%% Optional (comment the following command for hybrid simulations) Eigen analysis
d=eig(Structure.StiffnessMatrixFree,Structure.MassMatrixFree)
%% Load the EQ Record
Integrator = GetEQHistory('EQ.txt', Integrator, Structure);

%% Load Integration Parameters and set Initial Conditions
Integrator = InitializeIntegrator(Integrator, Structure);
% store displacement command results in this variable
Results = zeros(Integrator.Steps, Structure.NumFreeDOF);

%% Dynamic steps of the integration algorithm
for step = 1:Integrator.Steps
    % Generate Commands and store them in the Results variable
    Integrator = GenerateCommands(Integrator);
    Results(step,:) = Integrator.Displacement';
 
    % Calculate Restoring Forces per Element and form full structure
    % restoring force vector
    Integrator.RestoringForce = zeros(Structure.NumFreeDOF, 1);
    for element = 1:Structure.NumElements;
        %get the resisting force of each element
        [Elements(element), ElementRF] = Elements(element).GetRestoringForce(Elements(element), Structure, Integrator); 
        % Get element nodes to assemble in the structure restoring force vector
        Node1ID = Elements(element).Nodes(1).ID;
        Node2ID = Elements(element).Nodes(2).ID;
        if (3 * Node1ID) <= Structure.NumFreeDOF
            Integrator.RestoringForce(Node1ID * 3 - 2, 1) = ElementRF(1, 1) + Integrator.RestoringForce(Node1ID * 3 - 2, 1);
            Integrator.RestoringForce(Node1ID * 3 - 1, 1) = ElementRF(2, 1) + Integrator.RestoringForce(Node1ID * 3 - 1, 1);
            Integrator.RestoringForce(Node1ID * 3, 1) = ElementRF(3, 1) + Integrator.RestoringForce(Node1ID * 3, 1);
        end
        if (3 * Node2ID) <= Structure.NumFreeDOF
            Integrator.RestoringForce(Node2ID * 3 - 2, 1) = ElementRF(4, 1) + Integrator.RestoringForce(Node2ID * 3 - 2, 1);
            Integrator.RestoringForce(Node2ID * 3 - 1, 1) = ElementRF(5, 1) + Integrator.RestoringForce(Node2ID * 3 - 1, 1);
            Integrator.RestoringForce(Node2ID * 3, 1) = ElementRF(6, 1) + Integrator.RestoringForce(Node2ID * 3, 1);
        end
    end
    
    % Update Acceleration Vector
    Integrator = UpdateAcceleration(Integrator, Structure, step);
    
    Commands = Integrator.Displacement;
end

plot(Results);