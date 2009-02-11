% Create Element Type 2
function vars = CreateElement_Type2(ID,Type, Node1, Node2, Stiffness,DampStiffFac,DampMassFac,Cexp)

% Set element nodes
vars.ID = ID;
vars.Type=Type;
vars.Nodes = [Node1 Node2];

% Set element properties
vars.Stiffness = Stiffness;
vars.DampStiffFac=DampStiffFac;
vars.DampMassFac=DampMassFac;
vars.Cexp=Cexp;
% Set handles to element matrices forming function and restoring force function
vars.FormElementMatrices = @FormElementMatrices_Type2;
vars.GetRestoringForce = @GetRestoringForce_Type2;