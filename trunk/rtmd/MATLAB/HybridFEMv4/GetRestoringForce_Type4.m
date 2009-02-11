% get restoring force for the element
% returns (6x1) restoring force vector of element in global coordinates
function [Element, RestoringForce] = GetRestoringForce_Type4(Element, Structure, Integrator)

% Get distance between nodes
I1 = Element.Nodes(1).ID;
I2 = Element.Nodes(2).ID;

K11=Element.K11;
K12=Element.K12;
K21=Element.K21;
K22=Element.K22;

% preset arrays
SE = zeros(6,6);
    
SE(1, 1) = K11;
SE(1, 4) = K12;
SE(4, 1) = K21;
SE(4, 4) = K22;
%create the displacement Ue (6x1) vector of the element in global coordinates
Ue = zeros(6,1);
dof=zeros(6,1);
dof(1,1)=Element.Nodes(1).UX;
dof(2,1)=Element.Nodes(1).UY;
dof(3,1)=Element.Nodes(1).THETA;
dof(4,1)=Element.Nodes(2).UX;
dof(5,1)=Element.Nodes(2).UY;
dof(6,1)=Element.Nodes(2).THETA;
for i=1:6
    if dof(i,1)<=Structure.NumFreeDOF
        Ue(i,1)=Integrator.Displacement(dof(i,1),1);
    end
end
RestoringForce = SE*Ue;  %resisting (6x1) resisting force vector of element in global coordinates
Element.relas = 0; % necessary