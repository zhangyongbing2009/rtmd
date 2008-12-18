% set parameters and initial conditions for the integrator
function Integrator = InitializeIntegrator(Integrator, Structure)

% set integration parameters
Integrator.Alpha1 = (4 * Structure.MassMatrixFree + ...
                    2 * Structure.DampingMatrixFree * Integrator.Timestep + ...
                    Structure.StiffnessMatrixFree * Integrator.Timestep^2)^(-1) * 4 * Structure.MassMatrixFree;
Integrator.Alpha2 = Integrator.Alpha1;

% initial array conditions
Integrator.Displacement = zeros(Structure.NumFreeDOF, 1);
Integrator.Velocity = zeros(Structure.NumFreeDOF, 1);
Integrator.Acceleration = ones(Structure.NumFreeDOF, 1);
