%% Analytical Substructure properties
M = [503400];      %kg
C = [9.7324e4];    %N-sec/m
K = [11760000];    %N/m

Minv = inv(M);     %Mass matrix inverse

%% External excitation force
load canoga_deltaT10.txt;            %ground acceleration record
steps=length(canoga_deltaT10);       %number of steps in ground acceleration
scalefactor = 1.15/1.5*0.0375;      %earthquake scaling factor
F=-9.81*scalefactor*M*canoga_deltaT10';   %external excitation force calculation

%% Timestep
sample = 1/1024;            %DSP tick
dt_multiplier = 10;         %integration algorithm timestep multipler
dt = dt_multiplier * sample;  %timestep for integration algorithm

%% Damper properties equivalents
Keq = 9000000;   %N/m
Ceq = 557000;    %N-sec/m

%% Integration parameters
beta1 = (4*M)/(4*M + 2*(C + Ceq) * dt + (K + Keq) * dt^2);
beta2 = (4*M)/(4*M + 2*(C + Ceq) * dt + (K + Keq) * dt^2);

