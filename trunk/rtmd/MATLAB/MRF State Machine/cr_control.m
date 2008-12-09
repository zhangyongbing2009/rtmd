clear;
clc;

%% Number of degrees of freedom for overall structure
nDOF = 4;

%% Matrices 
M = [0.977  0       0       0;
     0      0.971   0       0;
     0      0       0.971   0;
     0      0       0       1.056];
Minv = inv(M);     %Mass matrix inverse

C = [3.337      -2.442      0.634       -0.079;
     -2.429     4.101       -2.468      0.462;
     0.648     -2.519      3.333      -1.248;
     -0.081     0.502       -1.281      0.966];
Co = C;

K = [1017       -768.207    201.848     -17.555;
     -774.397   1251       -794.564     151.186;
     200.783    -787.464    1039       -414.993;
     -21.367    155.404     -415.363    274.243];
 
Kgc = [292.684 -265.467 114.561 -19.198;
        -265.467 388.292 -278.524 80.004;
        114.561 -278.524 293.983 -111.513;
        -19.198 80.004 -111.513 47.603];

Ka = Kgc;    % Analytical Substructure

%% External excitation force loading
DOF = ones(nDOF,1);                         %define the DOF nodes
eq = load('STM090.txt');                                %ground acceleration record
scalefactor = 1;                            %earthquake scaling factor
F=-386*scalefactor*M*DOF*eq';                   %external excitation force calculation
shift=zeros(nDOF,1);
F=cat(2,shift,F);
steps=length(F);

%% Timestep parameters
%dt = sqrt(0.6) * 0.02;                  %Timestep for integration algorithm
dt = 0.015492;

%for Newmark
MinvAccel = inv(M + dt/2*C);

%% Integration parameters
beta1=(4*M+2*C*dt+K*dt^2)^(-1)*4*M;
beta2=beta1;

%% Initial Conditions are set to 0
dpred=zeros(nDOF,1);
u=zeros(nDOF,1);
ud=zeros(nDOF,1);
uold=zeros(nDOF,1);
drift=zeros(nDOF,1);
r=zeros(nDOF,1);
re=zeros(nDOF,1);
ra=zeros(nDOF,1);
um=zeros(nDOF,1);
udd=zeros(nDOF,1);
uddold=zeros(nDOF,1);

%% Arrays for holding results
results_commands=zeros(steps,nDOF);
results_drift=zeros(steps,nDOF);
results_force_exp=zeros(steps,nDOF);
results_force_ana=zeros(steps,nDOF);
results_force=zeros(steps,nDOF);

%% Create Spring Analytical Substructures
SpringA = SpringInitialization(242, 23, 126.7755, 0.24, 0, 0, 0);
SpringB = SpringInitialization(255, 13, 116.8522, 0.26, 0, 0, 0);
SpringC = SpringInitialization(236,  9, 94.13875, 0.28, 0, 0, 0);
SpringD = SpringInitialization(164,  8, 56.60981, 0.29, 0, 0, 0);

%% Integrator
i = 0;          % step counter
while (i <= steps+200)            
    %Calculate Predictor Displacement on first iteration for each step
    i = i + 1;          % Next step
    if (i > steps+200)     % No more steps?
        break;
    end    
    uold=u;             % Store previous predictor displacement
    %% CR
    %u = u + dt*ud + beta2*dt^2*udd;     % Generate command vector
    %ud = ud + beta1*dt*udd;             % Generate velocity vector
    %% Newmark
    u = u + dt*ud + (dt^2)/2*udd;     % Generate command vector
    
    %Show status
    disp(sprintf('%i / %i',i,steps));
            
    %Store command vector
    results_commands(i,:) = u';
    
    %Calculate interstory drifts
    drift(1) = u(1);
    drift(2) = u(2)-u(1);
    drift(3) = u(3)-u(2);
    drift(4) = u(4)-u(3);
    results_drift(i,:) = drift';
    
    %Update Spring States
    SpringA = SpringUpdateState(SpringA, drift(1));
    SpringB = SpringUpdateState(SpringB, drift(2));
    SpringC = SpringUpdateState(SpringC, drift(3));
    SpringD = SpringUpdateState(SpringD, drift(4));
    
    %Get Spring Restoring Forces and form force vector
    re(1) = -(SpringB.dFs - SpringA.dFs);
    re(2) = -(SpringC.dFs - SpringB.dFs);
    re(3) = -(SpringD.dFs - SpringC.dFs);
    re(4) = SpringD.dFs;    
    results_force_exp(i,:) = re';
    
    %Get Test Frame force vector
    ra = Ka * u;
    results_force_ana(i,:) = ra';
    
    %Form full hybrid force vector
    r = re + ra;
    results_force(i,:) = r';

    %Calculate acceleration vector
    %if (i > steps - 100 && i <= steps)
    if (i > steps && i <= steps + 200)
        % Go from 2% to 60% damping over 100 steps
        %C = C + 29/87 * Co;
        C = C + 0.1 * Co;
        
        %% CR
        %udd = Minv*(0-C*ud-r);               
        %% Newmark    
        uddold = udd;
        udd = MinvAccel * (0 - C*ud - dt/2*C*udd - r);    
        ud = ud + dt/2*(udd + uddold);
    else        
        %% CR
        %udd = Minv*(F(:,i)-C*ud-r);               
        %% Newmark    
        uddold = udd;
        udd = MinvAccel * (F(:,i) - C*ud - dt/2*C*udd - r);    
        ud = ud + dt/2*(udd + uddold);
    end
end

disp('Done!');
