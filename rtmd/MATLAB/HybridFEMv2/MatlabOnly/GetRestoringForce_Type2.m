% get restoring force for the element
% returns (6x1) restoring force vector of element in global coordinates
function [Element, RestoringForce] = GetRestoringForce_Type2(Element, Structure, Integrator)

% Get distance between nodes
I1 = Element.Nodes(1).ID;
I2 = Element.Nodes(2).ID;

% preset arrays
SE = zeros(6,6);
    
SE(1, 1) = Element.Stiffness;
SE(1, 4) = -Element.Stiffness;
SE(4, 4) = Element.Stiffness;
SE(4, 1) = SE(1,4);
%create the displacement Ue (6x1) vector of the element in global coordinates
Ue = zeros(6, 1);
if (I1 * 3) <= Structure.NumFreeDOF
    Ue(1, 1) = Integrator.Displacement(I1 * 3 - 2, 1);
    Ue(2, 1) = Integrator.Displacement(I1 * 3 - 1, 1);
    Ue(3, 1) = Integrator.Displacement(I1 * 3, 1);
end
if (I2 * 3) <= Structure.NumFreeDOF
    Ue(4, 1) = Integrator.Displacement(I2 * 3 - 2, 1);
    Ue(5, 1) = Integrator.Displacement(I2 * 3 - 1, 1);
    Ue(6, 1) = Integrator.Displacement(I2 * 3, 1);
end
RestoringForce = SE*Ue;  %resisting (6x1) resisting force vector of element in global coordinates
Element.relas = 0; % necessary