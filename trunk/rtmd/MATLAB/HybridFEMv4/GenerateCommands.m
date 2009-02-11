% generate next integration displacement commands and velocity vector
function Integrator = GenerateCommands(Integrator)

% displacement vector
Integrator.Displacement = Integrator.Displacement + ...
                          Integrator.Timestep * Integrator.Velocity + ...
                          Integrator.Alpha2 * Integrator.Timestep^2 * Integrator.Acceleration;
% velocity vector
Integrator.Velocity = Integrator.Velocity + ...
                      Integrator.Alpha1 * Integrator.Timestep * Integrator.Acceleration;