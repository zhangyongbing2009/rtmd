% Create Element Type 2
function vars = CreateElement_Type2(ID, Node1, Node2, Stiffness)

% Set element nodes
vars.ID = ID;
vars.Nodes = [Node1 Node2];

% Set element properties
vars.Stiffness = Stiffness;

% Set handles to element matrices forming function and restoring force function
vars.FormElementMatrices = @FormElementMatrices_Type2;
vars.GetRestoringForce = @GetRestoringForce_Type2;