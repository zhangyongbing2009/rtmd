% Form global element stiffness matrix and mass matrix of lean on column
%  ISTF = 1: Zero Matrices
%  ISTF = 0: Formed Matrices
function [ElementStiffnessMatrix,...
          ElementMassMatrix] = FormElementMatrices_Type4(Element, ISTF)
      
ElementStiffnessMatrix = zeros(6);
ElementMassMatrix = zeros(6);

K11=Element.K11;
K12=Element.K12;
K21=Element.K21;
K22=Element.K22;
m1=Element.m1;
m2=Element.m2;

ElementStiffnessMatrix(1,1)=K11;
ElementStiffnessMatrix(1,4)=K12;
ElementStiffnessMatrix(4,1)=K21;
ElementStiffnessMatrix(4,4)=K22;

ElementMassMatrix(1,1)=m1;
ElementMassMatrix(4,4)=m2;