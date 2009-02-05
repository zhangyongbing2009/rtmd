% calculate matrices and returns back the Structure object with new
% matrices
function Structure = CalculateMatrices(Structure, Elements)
      
% calculate number of free degrees of freedom
Structure.NumFreeDOF = Structure.NumDOF * Structure.NumNodes-Structure.NumRestrainedDOF-Structure.NumSlavedDOF;

% preset arrays
MassMatrixFree = zeros(Structure.NumFreeDOF, Structure.NumFreeDOF);
StiffnessMatrixFree=zeros(Structure.NumFreeDOF, Structure.NumFreeDOF);

% go through elements and set the degrees of freedom
for i=1:Structure.NumElements
    [ElementStiffnessMatrix,...
    ElementMassMatrix] = Elements{i}.FormElementMatrices(Elements{i}, 0);
    dof=zeros(6,1);
    dof(1,1)=Elements{i}.Nodes(1).UX;
    dof(2,1)=Elements{i}.Nodes(1).UY;
    dof(3,1)=Elements{i}.Nodes(1).THETA;
    dof(4,1)=Elements{i}.Nodes(2).UX;
    dof(5,1)=Elements{i}.Nodes(2).UY;
    dof(6,1)=Elements{i}.Nodes(2).THETA;
        
% go through each dof and set the mass and stiffness matrices
      for ii=1:6
          for jj=1:6
             if (dof(ii,1)>Structure.NumFreeDOF  || dof(jj,1)>Structure.NumFreeDOF)
             else
                 StiffnessMatrixFree(dof(ii,1),dof(jj,1))=StiffnessMatrixFree(dof(ii,1),dof(jj,1))+ElementStiffnessMatrix(ii,jj);
                 MassMatrixFree(dof(ii,1),dof(jj,1))=MassMatrixFree(dof(ii,1),dof(jj,1))+ElementMassMatrix(ii,jj);
             end
          end
      end
end

% calculate damping matrix
PI = 3.141592654;
omega1 = 2.0 * PI / Structure.T1;
omega2 = 2.0 * PI / Structure.T2;
A0 = Structure.DampingRatio * 2.0 * omega1 * omega2 / (omega1 + omega2);
A1 = Structure.DampingRatio * 2.0 / (omega1 + omega2);
DampingMatrixFree = A0 * MassMatrixFree + A1 * StiffnessMatrixFree;

% save matrices back to the structure
Structure.StiffnessMatrixFree = StiffnessMatrixFree;
Structure.MassMatrixFree = MassMatrixFree;
Structure.DampingMatrixFree = DampingMatrixFree;
Structure.MassMatrixFreeInv = inv(MassMatrixFree);