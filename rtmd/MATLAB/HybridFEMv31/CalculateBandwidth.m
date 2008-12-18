% Calculate the Bandwidth of the structure for use in calculating the
% stiffness matrix boundaries and sets it in the Structure object
function Structure = CalculateBandwidth(Structure, Elements)

% initialize the bandwidth variable to have 0 distance
bw = 0;
for i = 1:Structure.NumElements
    % take the max bandwidth between nodes for an element
    bw = max(bw, Structure.NumDOF * (abs(Elements{i}.Nodes(1).ID - Elements{i}.Nodes(2).ID) + 1));
end

% write back to the structure
Structure.Bandwidth = bw;

