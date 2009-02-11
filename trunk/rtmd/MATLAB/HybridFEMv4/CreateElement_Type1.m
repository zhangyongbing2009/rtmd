% Create Element Type 1
function vars = CreateElement_Type1(ID,Type, Node1, Node2, Material, Area, Inertia, ElemDistLoad, DampStiffFac,DampMassFac)

% Set element nodes
vars.ID = ID;
vars.Type=Type;
vars.Nodes = [Node1 Node2];

% Set element properties
vars.Material = Material;
vars.Area = Area;
vars.Inertia = Inertia;
vars.ElemDistLoad = ElemDistLoad;
vars.DampStiffFac=DampStiffFac;
vars.DampMassFac=DampMassFac;


% Set handles to element matrices forming function and restoring force function
vars.FormElementMatrices = @FormElementMatrices_Type1;
vars.GetRestoringForce = @GetRestoringForce_Type1;