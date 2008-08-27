% This initialization function sets constants for the spring structure and
% returns a structure of variables
function vars = SpringInitialization(initStiff, pyStiffRatio, yieldForce, fracEnergy, deformation, velocity, acceleration)

%% Set spring constants
vars.dKIni = initStiff;            %initial stiffness
vars.dAlpha = pyStiffRatio;        %post-yield stiffness ratio
vars.dFy = yieldForce;             %yield force
vars.dBetaE = fracEnergy;          %fraction of energy

%% Initial states of the spring
vars.dUs = deformation;              %deformation
vars.dVs = velocity;                 %velocity
vars.dAs = acceleration;             %acceleration

%bilinear elastic  
vars.dK1 = (1.0-vars.dAlpha/100.)*vars.dKIni;
%linear spring 
vars.dK2 = vars.dAlpha/100.*vars.dKIni;
%initial tangent stiffness of the spring
vars.dKTan = vars.dKIni; 

%Assume initial state of spring is elastic.
vars.nState = 1;
vars.dUy = vars.dFy/vars.dKIni;          %gap opening displacement
vars.dFf = vars.dBetaE*vars.dFy;         %friction force required for gap opening
vars.dUc = (vars.dFy-2*vars.dFf)/vars.dKTan;    %gap closing displacement
vars.dFgc = vars.dKTan*vars.dUc;
vars.dFgc1= vars.dK1*vars.dUc;
vars.dFy1 = vars.dK1*vars.dUy;
vars.dFs  = vars.dKTan*vars.dUs;
vars.dFs1 = vars.dK1*vars.dUs;
    
%check whether the initial spring force is beyond its yielding force.
%if so, return error message and exit program
if (abs(vars.dFs)>abs(vars.dFy))
	disp('Error : Initial spring force is beyond yield force');
end