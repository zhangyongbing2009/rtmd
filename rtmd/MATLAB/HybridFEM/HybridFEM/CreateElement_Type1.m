% Element object
function vars = CreateElement_Type1(ID, Node1, Node2, Material, Area, Inertia, ElemDistLoad)

%% Set element nodes
vars.ID = ID;
vars.Nodes = [Node1 Node2];

%% Set element properties
vars.Material = Material;
vars.Area = Area;
vars.Inertia = Inertia;
vars.ElemDistLoad = ElemDistLoad;
vars.Stiffness = 0;
vars.hard = 0;
vars.Av = 0;
vars.v = 0;
vars.Mpl = 0;
vars.Npl = 0;
vars.ksurf = 0;
vars.cosa = 0;
vars.eal = 0;
vars.ek11 = 0;
vars.ek11h = 0;
vars.ek12 = 0;
vars.ek22 = 0;
vars.ek22h = 0;
vars.fl = 0;
vars.pr12 = 0;
vars.pr21 = 0;
vars.psh = 0;
vars.sina = 0;
vars.a1 = 0;
vars.a2 = 0;
vars.pmx = 0;
vars.bmy = 0;
%% Set handles to element matrices forming function and restoring force
%  function
vars.FormElementMatrices = @FormElementMatrices_Type1;