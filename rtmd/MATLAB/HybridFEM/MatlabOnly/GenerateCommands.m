% generate next integration displacement commands and velocity vector
function Integrator = GenerateCommands(Integrator)

% displacement vector
Integrator.Displacement = Integrator.Displacement + ...
                          Integrator.Timestep * Integrator.Velocity + ...
                          Integrator.Beta2 * Integrator.Timestep^2 * Integrator.Acceleration;
% velocity vector
Integrator.Velocity = Integrator.Velocity + ...
                      Integrator.Beta1 * Integrator.Timestep * Integrator.Acceleration;