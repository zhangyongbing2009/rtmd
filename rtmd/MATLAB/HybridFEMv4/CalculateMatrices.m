% calculate matrices and returns back the Structure object with new
% matrices
function Structure = CalculateMatrices(Structure, Elements)
      
% calculate number of free degrees of freedom
Structure.NumFreeDOF = Structure.NumDOF * Structure.NumNodes-Structure.NumRestrainedDOF-Structure.NumSlavedDOF;

% preset arrays
MassMatrixFree = zeros(Structure.NumFreeDOF, Structure.NumFreeDOF);
StiffnessMatrixFree=zeros(Structure.NumFreeDOF, Structure.NumFreeDOF);
DampingMatrixFree=zeros(Structure.NumFreeDOF, Structure.NumFreeDOF);
DampingMatrixFreeIntegratorSetup=zeros(Structure.NumFreeDOF, Structure.NumFreeDOF);

%initial damping calculations
PI = 3.141592654;
omega1 = 2.0 * PI / Structure.T1;
omega2 = 2.0 * PI / Structure.T2;
A0 = Structure.DampingRatio * 2.0 * omega1 * omega2 / (omega1 + omega2);
A1 = Structure.DampingRatio * 2.0 / (omega1 + omega2);



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
    
    if (Elements{i}.Type==2)%experimental element
       ElementDampingMatrix=zeros(6,6);
       ElementDampingMatrix(1,1)=Elements{i}.Cexp;
       ElementDampingMatrix(1,4)=-Elements{i}.Cexp;
       ElementDampingMatrix(4,4)=Elements{i}.Cexp;
       ElementDampingMatrix(4,1)=-Elements{i}.Cexp;
    end
    
% go through each dof and set the mass and stiffness matrices
      for ii=1:6
          for jj=1:6
             if (dof(ii,1)>Structure.NumFreeDOF  || dof(jj,1)>Structure.NumFreeDOF)
             else
                 StiffnessMatrixFree(dof(ii,1),dof(jj,1))=StiffnessMatrixFree(dof(ii,1),dof(jj,1))+ElementStiffnessMatrix(ii,jj);
                 MassMatrixFree(dof(ii,1),dof(jj,1))=MassMatrixFree(dof(ii,1),dof(jj,1))+ElementMassMatrix(ii,jj);
                 if (Elements{i}.Type==2) %%element type 2 does not contibute to DampingMatrixFree
                 else
                 DampingMatrixFree(dof(ii,1),dof(jj,1))=DampingMatrixFree(dof(ii,1),dof(jj,1))+...
                 Elements{i}.DampMassFac*A0*ElementMassMatrix(ii,jj)+Elements{i}.DampStiffFac*A1*ElementStiffnessMatrix(ii,jj);
                 end
                 DampingMatrixFreeIntegratorSetup(dof(ii,1),dof(jj,1))=DampingMatrixFree(dof(ii,1),dof(jj,1));
                 if(Elements{i}.Type==2)%element type 2 contributes to DampingMatrixFreeIntegratorSetup
                     DampingMatrixFreeIntegratorSetup(dof(ii,1),dof(jj,1))=DampingMatrixFreeIntegratorSetup(dof(ii,1),dof(jj,1))+ElementDampingMatrix(ii,jj);
                 end
             end
          end
      end
end

%here should be implemented the LUMPED MASS MATRIX and the LUMPED MASS PROPORTIONAL DAMPING MATRIX
%these matrices should be added to the previous mass matrix and to the previous damping matrix (both FREE and CRsetup)

% save matrices back to the structure
Structure.StiffnessMatrixFree = StiffnessMatrixFree;
Structure.MassMatrixFree = MassMatrixFree;
Structure.DampingMatrixFree = DampingMatrixFree;
Structure.DampingMatrixFreeIntegratorSetup=DampingMatrixFreeIntegratorSetup;
Structure.MassMatrixFreeInv = inv(MassMatrixFree);
