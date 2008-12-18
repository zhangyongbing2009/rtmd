%% Load Input File which has descriptions of the nodes, elements, materials
%  and integrator
function [Structure, Elements, Integrator] = LoadConfiguration(INP_FILE)

FileToOpen = INP_FILE;              % input file path
InputFile  = fopen(FileToOpen,'r');
fgets(InputFile);                   % skip 3 lines
fgets(InputFile);  
fgets(InputFile);  
data = str2num(fgets(InputFile));   % get a configuration line
% parse the structure information
Structure = CreateStructure(data(1),data(2),data(3),data(4),data(5),data(6),data(7),data(8));
fgets(InputFile);                   % skip 2 lines
fgets(InputFile);  
% parse the rows of nodes information
for i = 1:Structure.NumNodes
    data = str2num(fgets(InputFile));
    % create a new node with X,Y,Z coordinates
    Nodes(i) = CreateNode(data(1), data(2), data(3), 0, data(4),data(5),data(6));
end
fgets(InputFile);                   % skip 2 lines
fgets(InputFile);
% parse the rows of material information
for i = 1:Structure.NumMaterials
   data = str2num(fgets(InputFile));
   % create a new material object
   Materials(i) = CreateMaterial(data(1), data(2));
end
fgets(InputFile);                   % skip 2 lines
fgets(InputFile);
% parse the rows of element information
for i = 1:Structure.NumElements
    data = str2num(fgets(InputFile));
    % select the proper element type to configure
    switch data(2) 
        case 1  % type 1: elastic beam-column element
            Elements(i) = {CreateElement_Type1(data(1), Nodes(data(3)), Nodes(data(4)), Materials(data(5)), data(6), data(7), data(8))};
        case 2  % type 2: experimental element with initial stiffness for simulation mode
            Elements(i) = {CreateElement_Type2(data(1), Nodes(data(3)), Nodes(data(4)), data(5))};
        case 3  % type 3: inelastic beam-column element with point-hinges DRAIN2DX type 2            
            Elements(i) = {CreateElement_Type3(data(1), Nodes(data(3)), Nodes(data(4)), Materials(data(5)), data(6), data(7), data(8), data(9), data(10), data(11), data(12), data(13))};     
        case 4   %type 4: 2 story gravity column 
            Elements(i) = {CreateElement_Type4(data(1), Nodes(data(3)), Nodes(data(4)), data(5), data(6), data(7), data(8), data(9),data(10))};
        otherwise % unknown case
            disp(sprintf('Unknown Element Type: %i', data(2)));
            return;
    end
end
fgets(InputFile);                   % skip 2 lines
fgets(InputFile);
% parse the parameters for the integration algorithm
data = str2num(fgets(InputFile));
% add the periods and damping ratio to the structure
Structure.T1 = data(1);
Structure.T2 = data(2);
Structure.DampingRatio = data(3);
% create a new integrator with the EQ scale and timestep
Integrator = CreateIntegrator(data(4), data(5));
fclose(InputFile);