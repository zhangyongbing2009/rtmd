% Element object with nodes, material and properties
function vars = CreateElement_Type2(ID, Node1, Node2, Material, Stiffness)

%% Set element nodes
vars.ID = ID;
vars.Nodes = [Node1 Node2];

%% Set element properties
vars.Material = Material;
vars.Area = 0;
vars.Inertia = 0;
vars.ElemDistLoad = 0;
vars.Stiffness = Stiffness;
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
vars.FormElementMatrices = @FormElementMatrices_Type2;
vars.GetRestoringForce = @GetRestoringForce_Type2;

% necessary
vars.Uprev = 0;
vars.xyj = 0;
vars.xyi = 0;
vars.ftyp = 0;
vars.kysur = 0;
vars.sff = 0;
vars.inodi = 0;
vars.inodj = 0;
vars.iinc = 0;
vars.iimbt = 0;
vars.iiecc = 0;
vars.iksfi = 0;
vars.iksfj = 0;
vars.imem = 0;
vars.istn = 0;
vars.istp = 0;
vars.kody = 0;
vars.kodyx = 0;
vars.nodi = 0;
vars.nodj = 0;
vars.a = 0;
vars.afdp = 0;
vars.aint = 0;
vars.beint = 0;
vars.bmint = 0;
vars.bmdp = 0;
vars.bmep = 0;
vars.bmtot = 0;
vars.coskg = 0;
vars.ec = 0;
vars.endamp = 0;
vars.enelas = 0;
vars.flkg = 0;
vars.ftot = 0;
vars.ovtol = 0;
vars.pkg = 0;
vars.pkgp = 0;
vars.pracn = 0;
vars.pracp = 0;
vars.prtot = 0;
vars.senn = 0;
vars.senp = 0;
vars.sfint = 0;
vars.sftot = 0;
vars.sinkg = 0;
vars.pmax = 0;
vars.aa1 = 0;
vars.aa2 = 0;
vars.xm = 0;
vars.finit = 0;
vars.ecc = 0;
vars.ksf = 0;
vars.w = 0;
vars.relas = 0;
%