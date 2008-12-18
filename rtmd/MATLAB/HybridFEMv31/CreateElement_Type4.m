% Create Element Type 4
function vars = CreateElement_Type4(ID, Node1, Node2, K11, K12, K21, K22, m1, m2)

% Set element nodes
vars.ID = ID;
vars.Nodes = [Node1 Node2];

% Set element properties
vars.K11 = K11;
vars.K12 = K12;
vars.K21 = K21;
vars.K22 = K22;
vars.m1 = m1;
vars.m2 = m2;

% Set handles to element matrices forming function and restoring force function
vars.FormElementMatrices = @FormElementMatrices_Type4;
vars.GetRestoringForce = @GetRestoringForce_Type4;