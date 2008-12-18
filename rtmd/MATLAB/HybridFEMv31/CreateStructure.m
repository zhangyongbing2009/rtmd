% Create a Structure object
function vars = CreateStructure(NumNodes, NumElements, NumMaterials, Dimensions, NodesPerElement, NumDOF, NumRestrainedDOF, NumSlavedDOF)

%% Set properties
vars.NumNodes = NumNodes;
vars.NumElements = NumElements;
vars.NumMaterials = NumMaterials;
vars.Dimensions = Dimensions;
vars.NodesPerElement = NodesPerElement;
vars.NumDOF = NumDOF;
vars.NumRestrainedDOF = NumRestrainedDOF;
vars.NumSlavedDOF= NumSlavedDOF;