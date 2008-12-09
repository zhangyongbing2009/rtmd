% get restoring force for the element
% returns (6x1) restoring force vector of element in global coordinates
function [Element, RestoringForce] = GetRestoringForce_Type1(Element, Structure, Integrator)

% Get distance between nodes
I1 = Element.Nodes(1).ID;
I2 = Element.Nodes(2).ID;
X21 = Element.Nodes(2).Xcoord - Element.Nodes(1).Xcoord;
Y21 = Element.Nodes(2).Ycoord - Element.Nodes(1).Ycoord;
EL = sqrt(X21 * X21 + Y21 * Y21);
EAL = Element.Material.E * Element.Area / EL;
EIZL = Element.Material.E * Element.Inertia / EL;
%calculation of density (the uniform load is only taken into account)
SEP = zeros(6);
SEP(1, 1) = EAL;
SEP(1, 4) = -EAL;
SEP(4, 4) = EAL;
SEP(2, 2) = 12 * EIZL / EL ^ 2;
SEP(2, 3) = 6 * EIZL / EL;
SEP(2, 5) = -SEP(2, 2);
SEP(2, 6) = SEP(2, 3);
SEP(3, 3) = 4 * EIZL;
SEP(3, 5) = -6 * EIZL / EL;
SEP(3, 6) = 2 * EIZL;
SEP(5, 5) = 12 * EIZL / EL ^ 2;
SEP(5, 6) = -6 * EIZL / EL;
SEP(6, 6) = 4 * EIZL;
for i = 1:6
   for j = i:6
      SEP(j, i) = SEP(i, j);
   end
end
DCOS(1, 1) = X21 / EL;
DCOS(1, 2) = Y21 / EL;
DCOS(1, 3) = 0;
DCOS(2, 1) = -DCOS(1, 2);
DCOS(2, 2) = DCOS(1, 1);
DCOS(2, 3) = 0;
DCOS(3, 1) = 0;
DCOS(3, 2) = 0;
DCOS(3, 3) = 1;
ALMBDA = zeros(6);       
for i = 1:2
   IK = 3 * (i - 1);
   for j = 1:3
      for k = 1:3
         ALMBDA(j + IK, k + IK) = DCOS(j, k);
      end
   end
end
SE = SEP * ALMBDA;
SEP = SE;
SE = ALMBDA' * SEP;
%create the displacement Ue (6x1) vector of the element in global coordinates
Ue = zeros(6,1);
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
RestoringForce = SE * Ue;  %resisting (6x1) resisting force vector of element in global coordinates
Element.relas = 0; % necessary