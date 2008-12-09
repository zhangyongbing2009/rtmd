% calculate matrices and returns back the Structure object with new
% matrices
function Structure = CalculateMatrices(Structure, Elements)
      
% preset arrays
Structure.TotalNumDOF = Structure.NumDOF * Structure.NumNodes;
Structure.NumFreeDOF = Structure.TotalNumDOF - Structure.NumRestrainedDOF;
StiffnessMatrixBanded = zeros(Structure.TotalNumDOF, Structure.Bandwidth);
StiffnessMatrix = zeros(Structure.TotalNumDOF, Structure.TotalNumDOF);
StiffnessMatrixFree = zeros(Structure.NumFreeDOF, Structure.NumFreeDOF);
MassMatrixBanded = zeros(Structure.TotalNumDOF, Structure.Bandwidth);
MassMatrix = zeros(Structure.TotalNumDOF, Structure.TotalNumDOF);
MassMatrixFree = zeros(Structure.NumFreeDOF, Structure.NumFreeDOF);

% calculate global banded stiffness and mass matrices based on elements
for i = 1:Structure.NumElements
    [ElementStiffnessMatrix,...
     ElementMassMatrix] = Elements(i).FormElementMatrices(Elements(i), 0);
    for j = 1:Structure.NodesPerElement
        NRT = Structure.NumDOF * (Elements(i).Nodes(j).ID - 1);
        for k = 1:Structure.NumDOF
            NR = NRT + k;
            I = Structure.NumDOF * (j - 1) + k;
            for l = 1:Structure.NodesPerElement
                NCT = Structure.NumDOF * (Elements(i).Nodes(l).ID - 1);
                for m = 1:Structure.NumDOF
                    J = Structure.NumDOF * (l - 1) + m;
                    NC = NCT + m - NR + 1;
                    if (NC > 0)
                        StiffnessMatrixBanded(NR, NC) = StiffnessMatrixBanded(NR, NC) + ElementStiffnessMatrix(I,J);
                        MassMatrixBanded(NR, NC) = MassMatrixBanded(NR, NC) + ElementMassMatrix(I,J);                        
                    end
                end
            end
        end
    end
end

for i = 1:Structure.TotalNumDOF
    for j = i:Structure.TotalNumDOF
          if (j-i+1<=Structure.Bandwidth)
            StiffnessMatrix(i, j) = StiffnessMatrixBanded(i, j-i+1);
            MassMatrix(i, j) = MassMatrixBanded(i, j-i+1);
          end
    end
end



for i = 1:Structure.TotalNumDOF
    for j = 1:Structure.TotalNumDOF
        if (j < i)
            StiffnessMatrix(i, j) = StiffnessMatrix(j, i);
            MassMatrix(i, j) = MassMatrix(j, i);
        end
    end
end
for i = 1:Structure.NumFreeDOF
    for j = 1:Structure.NumFreeDOF
        StiffnessMatrixFree(i, j) = StiffnessMatrix(i, j);
        MassMatrixFree(i, j) = MassMatrix(i, j);
    end
end

% write back to the structure
Structure.StiffnessMatrixFree = StiffnessMatrixFree;
Structure.MassMatrixFree = MassMatrixFree;

% inverted mass matrix for speed
Structure.MassMatrixFreeInv = inv(MassMatrixFree);

% calculate damping matrix
PI = 3.141592654;
omega1 = 2.0 * PI / Structure.T1;
omega2 = 2.0 * PI / Structure.T2;
A0 = Structure.DampingRatio * 2.0 * omega1 * omega2 / (omega1 + omega2);
A1 = Structure.DampingRatio * 2.0 / (omega1 + omega2);
DampingMatrixFree = A0 * MassMatrixFree + A1 * StiffnessMatrixFree;

% write back to the structure
Structure.DampingMatrixFree = DampingMatrixFree;