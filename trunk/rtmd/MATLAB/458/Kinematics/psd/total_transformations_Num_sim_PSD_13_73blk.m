% function [d]=total_transformations(N,sample,AI,AO)
function [d]=total_transformations(N,sample)
%Program to consider kinematics in PDF numerical simulation with a planar
%structure - use measured displacements to determine command displacements for the
%next time step)
%Kinematics is based on the Total Transformation Method
sampletime=sample
N=24.9/sampletime
% t=0:sampletime:N*sampletime;
% dx=0.5*0.8*(3*sin((2*pi)*t))';
% dy=0.5*0.8*(3*sin((2*pi)*t))';
% d0=0.5*0.8*(0.1*sin((2*pi)*t))';
%damping
si=0.02
% scale factor for EQ
EQ_scalex=0.5*(1/1.5);
EQ_scaley=0.08*(1/1.5);
%block dimensions for kinematics
hL=(54.5625-40.75); bL=(78.5-5.875); 
bL2=bL/2; hL2=2.656;
%Specify structural stiffness at actuators.
k1=60; k2=60; k3=60;
% Specify Mass, Stiffness,Damping Matrices
ndof=3
emx=0.20*(bL2);
emy=-0.00*(hL);
m1=1.34; m2=1.34; m3=167.5;
K=[k3,0,-k3*hL2;0,k1+k2,0;-k3*hL2,0,k3*hL2^2 + 0.25*(k1+k2)*bL^2]
% M=[m1,0,0;0,m2,0;0,0,m3]
M=[m1,0,-m1*emy;-m1*emy,m2,m2*emx;0,m2*emx,m3+m2*(emx)^2 + m1*(emy)^2]
%Determine modes shapes, natural frequencies, and natural periods
[V,D] = eig(K,M);
for i=1:ndof;
T(i)=2*pi/sqrt(D(i,i));
end
T=T'
wn1=sqrt(D(1,1))
wn2=sqrt(D(2,2))
wn3=sqrt(D(3,3))
%Determine damping matric using Rayleigh Proportional damping
dratio=si*[1.0;1.0];
Dcoeff=0.5*[1/wn1,wn1;1/wn3,wn3];
Dmat=inv(Dcoeff)*dratio
C=Dmat(1)*M + Dmat(2)*K
%%
%%Read EQ records
fid=fopen('CNP106.at2','r');
for j=1:4;   fgetl(fid);   end;
Tempx=fscanf(fid,'%e',[1 inf]); fclose (fid);
fid=fopen('CNP196.at2','r');
for j=1:4;   fgetl(fid);   end;
Tempy=fscanf(fid,'%e',[1 inf]); fclose (fid);
rec_dt=0.01
tt = (0:length(Tempx)-1)*rec_dt;
dt = sampletime;
Time=N*dt
t = 0:dt:Time;
%N = length(t);
g=32.2*12
Xg = EQ_scalex*g*interp1(tt,Tempx,t);
Yg = EQ_scaley*g*interp1(tt,Tempy,t);
%%
%Compute effective load vector
r=[1;1;1];
nn=length(Xg)
Mr=M*r
for j=1:nn
peff1(j)=-Mr(1)*Xg(j);
peff2(j)=-Mr(2)*Yg(j);
peff3(j)=-Mr(3)*0.0;
peff(j,:)=[peff1(j);peff2(j);peff3(j)];
end
%%
% read initial offsets from NI
% scalingfactors;
% offset = getsample(AI);
% commands = offset;
% results = [0 0 0 0];

% initializations
%dxold=0;
%dyold=0;
%d0old=0;


% define the geometry
%information about the initial geometry of the set up
%note actuators and measurement devices have the same geometry
 A1SNx=5.8750; A2SNx=78.7500; A3SNx=96.5000;  M1SNx=5.8750; M2SNx=78.7500; M3SNx=96.5000;  SPNx=42.3125;
%Change starts
 % A1SNy=36.75;  A2SNy=36.75;   A3SNy=50.5625;  M1SNy=36.75;  M2SNy=36.75;   M3SNy=50.5625;  SPNy=47.9063;
A1SNy=40.75;  A2SNy=40.75;   A3SNy=54.5625;  M1SNy=40.75;  M2SNy=40.75;   M3SNy=54.5625;  SPNy=51.9063;
%A1FNx=5.8750; A2FNx=78.7500; A3FNx=173.3125; M1FNx=5.8750; M2FNx=78.7500; M3FNx=173.3125; SPNx0=42.3125;
A1FNx=5.8750; A2FNx=78.7500; A3FNx=175.5; M1FNx=5.8750; M2FNx=78.7500; M3FNx=175.5; SPNx0=42.3125;
%A1FNy=0;      A2FNy=0;       A3FNy=50.5625;  M1FNy=0;      M2FNy=0;       M3FNy=50.5625;  SPNy0=47.9063;
A1FNy=0;      A2FNy=0;       A3FNy=54.5625;  M1FNy=0;      M2FNy=0;       M3FNy=54.5625;  SPNy0=51.9063;

M4SNx=78.7500;
M4SNy=40.75; 
M4FNx=53.75;
M4FNy=-4;

%LA1 LA2 LA3 won't be used as STEP 4 is omitted
% LM1= 36.75; LM2= 36.75; LM3=76.8125;
LM1= 40.75; LM2= 40.75; LM3=79; LM4=(25^2+44.75^2)^0.5;
% LA1initial=36.75; LA2initial=36.75; LA3initial=76.8125;
LA1initial=40.75; LA2initial=40.75; LA3initial=79;
% LM1initial=36.75; LM2initial=36.75; LM3initial=76.8125;
LM1initial=40.75; LM2initial=40.75; LM3initial=79; LM4initial=(25^2+44.75^2)^0.5;
%%%%%Change ends  

%block dimensions
da12xbar=(72+7/8)/2; da3xbar=(72+7/8)/2+17+3/4;
da12ybar=((14+5/16)/2)+4; da3ybar=(7+2.5/16-4.5);


% to be able to compute the measurement device readings
angleA10=(pi)+atan(da12ybar/da12xbar);  
angleA20=(3*pi/2)+atan(da12xbar/da12ybar); 
angleA30=(0)+atan(da3ybar/da3xbar);
angleM10=(pi)+atan(da12ybar/da12xbar);
angleM20=(3*pi/2)+atan(da12xbar/da12ybar);     
angleM30=(0)+atan(da3ybar/da3xbar);
angleM40=angleM20;
a1=((da12xbar)^2+(da12ybar)^2)^0.5;
a2=((da12xbar)^2+(da12ybar)^2)^0.5;
a3=((da3xbar)^2+(da3ybar)^2)^0.5;
m1=((da12xbar)^2+(da12ybar)^2)^0.5;
m2=((da12xbar)^2+(da12ybar)^2)^0.5;
m3=((da3xbar)^2+(da3ybar)^2)^0.5;
m4=m2;
%%%%%%%%%%%%%%%%%%%%%%%
deltadlM1totold=0;
deltadlM2totold=0;
deltadlM3totold=0;
%to get the total measured displacement @ SPN
dmesSPN=[0;0;0];

%%for the quantities that we want to output:
%%local total command displacements at the actuators (ignore the first
%%0(initiation)
ddcA1=0; ddcA2=0; ddcA3=0;
m4calc=0;
%%%total measured displacement @SPN for incremental
ddmesSPN=[0;0;0];
%total measured displacement @SPN for total
ddmesSPNtot=[0;0;0];
%%%%total measured restoring force @SPN
rrmSPN=[0;0;0];
% fixed distance between the MFN's for M2 and M4
Lf=abs(M4FNx-M2FNx);Lfedge=(Lf^2+4^2)^0.5;
Ledge=abs(M2SNx-M1SNx); dtotM4=0;
%%
%Perform time integration analysis, determine displacement command for PSD
%with Newmark Explicit
%
% initializations
ui = [0;0;0];
udi = [0;0;0];
Mhat=inv(M+0.5*dt*C);
rmSPN=[0;0;0];
uddi = inv(M)*(peff(1,:)'-C*udi-rmSPN);
dxold=0;
dyold=0;
d0old=0;
dx(1)=ui(1);
dy(1)=ui(2);
d0(1)=ui(3);
%%
for i=1:N;
        %% receive total command displacement @SPN
%     dxnew=dx(i);
%     dynew=dy(i);
%     d0new=d0(i);
%    Use measured SPN displacements instead of calculated to determine
    %command displacement for next time step
%     ui=dmesSPN;
    uii=ui+dt*udi + 0.5*(dt^2)*uddi;
    dx(i+1)=uii(1);
    dy(i+1)=uii(2);
    d0(i+1)=uii(3);
    dxnew=dx(i+1);
    dynew=dy(i+1);
    d0new=d0(i+1);
    %%% STEP 1 Compute the total command displacement for each actuator
    %%%%%to compute the command displacements for actuators
    deltadlA1=extension(SPNx0,SPNy0,dxnew,dynew,d0new,angleA10,a1,A1FNx,A1FNy,LA1initial);
    deltadlA2=extension(SPNx0,SPNy0,dxnew,dynew,d0new,angleA20,a2,A2FNx,A2FNy,LA2initial);
    deltadlA3=extension(SPNx0,SPNy0,dxnew,dynew,d0new,angleA30,a3,A3FNx,A3FNy,LA3initial);
    
    %%% NI Command and Feedback routines
%     NIbatchForKinematics
    
    %% to build arrays (column(1)) for the actuator command displacements
    %% (to keep track of the whole history)
    ddcA1=cat(1,ddcA1,deltadlA1);
    ddcA2=cat(1,ddcA2,deltadlA2);
    ddcA3=cat(1,ddcA3,deltadlA3);
    %% STEP2 compute measured displacements of the SPN
    %calculate the local measurement device readings using trigonometry:
    deltadlM1tot=extension(SPNx0,SPNy0,dxnew,dynew,d0new,angleM10,m1,M1FNx,M1FNy,LM1initial);
    deltadlM2tot=extension(SPNx0,SPNy0,dxnew,dynew,d0new,angleM20,m2,M2FNx,M2FNy,LM2initial);
    deltadlM3tot=extension(SPNx0,SPNy0,dxnew,dynew,d0new,angleM30,m3,M3FNx,M3FNy,LM3initial);
    
    %establish the composite transformation matrix from slave to master:
    [xbarM1,ybarM1]=xybar(SPNx,SPNy,M1SNx,M1SNy);
    [xbarM2,ybarM2]=xybar(SPNx,SPNy,M2SNx,M2SNy);
    [xbarM3,ybarM3]=xybar(SPNx,SPNy,M3SNx,M3SNy);
    CTms=compmtos(xbarM1,ybarM1,xbarM2,ybarM2,xbarM3,ybarM3,M1SNx,M1FNx,M1SNy,M1FNy,LM1,M2SNx,M2FNx,M2SNy,M2FNy,LM2,M3SNx,M3FNx,M3SNy,M3FNy,LM3);
    CTsm=inv(CTms);
    deltadlM1=deltadlM1tot-deltadlM1totold;
    deltadlM2=deltadlM2tot-deltadlM2totold;
    deltadlM3=deltadlM3tot-deltadlM3totold;
    deltadlM=[deltadlM1;deltadlM2;deltadlM3];
    deltadlM1totold=deltadlM1tot;
    deltadlM2totold=deltadlM2tot;
    deltadlM3totold=deltadlM3tot;
    %calculate the delta_d_measured @SPN:
    deltadmSPN=CTsm*deltadlM;
    %calculate the total displacement @SPN;
    dmesSPN=dmesSPN+deltadmSPN;
    %% to build arrays (row(2)) for the total measuredSPN displacements
    %% (to keep track of the whole history)
    ddmesSPN=cat(2,ddmesSPN,dmesSPN);
    %%%STEP3 Update the coordinates, lengths and distances  based on the measured
    % displacement @SPN 
    %calculate the global displacement increments at the actuator
    [xbarA1,ybarA1]=xybar(SPNx,SPNy,A1SNx,A1SNy);
    TmsA1=mtos(xbarA1,ybarA1);
    [xbarA2,ybarA2]=xybar(SPNx,SPNy,A2SNx,A2SNy);
    TmsA2=mtos(xbarA2,ybarA2);
    [xbarA3,ybarA3]=xybar(SPNx,SPNy,A3SNx,A3SNy);
    TmsA3=mtos(xbarA3,ybarA3);
    deltadmA1SN=TmsA1*deltadmSPN;
    deltadmA2SN=TmsA2*deltadmSPN;
    deltadmA3SN=TmsA3*deltadmSPN;
    %for measurement devices only xbars and ybars are ready, therefore
    %calculating Tms's
    TmsM1=mtos(xbarM1,ybarM1);
    TmsM2=mtos(xbarM2,ybarM2);
    TmsM3=mtos(xbarM3,ybarM3);
    deltadmM1SN=TmsM1*deltadmSPN;
    deltadmM2SN=TmsM2*deltadmSPN;
    deltadmM3SN=TmsM3*deltadmSPN;
    % Update the coordinates;
    A1SNx=A1SNx+deltadmA1SN(1);A2SNx=A2SNx+deltadmA2SN(1);A3SNx=A3SNx+deltadmA3SN(1);
    A1SNy=A1SNy+deltadmA1SN(2);A2SNy=A2SNy+deltadmA2SN(2);A3SNy=A3SNy+deltadmA3SN(2);
    M1SNx=M1SNx+deltadmM1SN(1);M2SNx=M2SNx+deltadmM2SN(1);M3SNx=M3SNx+deltadmM3SN(1);
    M1SNy=M1SNy+deltadmM1SN(2);M2SNy=M2SNy+deltadmM2SN(2);M3SNy=M3SNy+deltadmM3SN(2);
    SPNx=SPNx+deltadmSPN(1); SPNy=SPNy+deltadmSPN(2);
    %Update the lengths of the devices;
%     LA1=((A1SNx-A1FNx)^2+(A1SNy-A1FNy)^2)^0.5;%not used as STEP4 omitted
%     LA2=((A2SNx-A2FNx)^2+(A2SNy-A2FNy)^2)^0.5;%not used as STEP4 omitted
%     LA3=((A3SNx-A3FNx)^2+(A3SNy-A3FNy)^2)^0.5;%not used as STEP4 omitted
    LM1=((M1SNx-M1FNx)^2+(M1SNy-M1FNy)^2)^0.5;
    LM2=((M2SNx-M2FNx)^2+(M2SNy-M2FNy)^2)^0.5;
    LM3=((M3SNx-M3FNx)^2+(M3SNy-M3FNy)^2)^0.5;
    %%%STEP 4   
%     % calculate the local total measured restoring forces:
%     r1local=[k1*deltadlA1;0];
%     r2local=[k2*deltadlA2;0];
%     r3local=[k3*deltadlA3;0];
%     %updating the transformation matrices:
%     TglA1=gtol(A1SNx,A1SNy,A1FNx,A1FNy,LA1);
%     TglA2=gtol(A2SNx,A2SNy,A2FNx,A2FNy,LA2);
%     TglA3=gtol(A3SNx,A3SNy,A3FNx,A3FNy,LA3);
%     [xbarA1,ybarA1]=xybar(SPNx,SPNy,A1SNx,A1SNy);
%     TmsA1=mtos(xbarA1,ybarA1);
%     [xbarA2,ybarA2]=xybar(SPNx,SPNy,A2SNx,A2SNy);
%     TmsA2=mtos(xbarA2,ybarA2);
%     [xbarA3,ybarA3]=xybar(SPNx,SPNy,A3SNx,A3SNy);
%     TmsA3=mtos(xbarA3,ybarA3);
%     %%%global restoring forces @ actuator nodes
%     rmA1SN=TglA1'*r1local;
%     rmA2SN=TglA2'*r2local;
%     rmA3SN=TglA3'*r3local;
%     %%%%%%global restoring forces @ SPN from each actuator
%     rmSPNA1=TmsA1'*cat(1,rmA1SN,0);
%     rmSPNA2=TmsA2'*cat(1,rmA2SN,0);
%     rmSPNA3=TmsA3'*cat(1,rmA3SN,0);
%     %%%%considering the contribution from all the actuators
%     rmSPN=rmSPNA1+rmSPNA2+rmSPNA3;
%     %% to build arrays (row(2)) for the total measuredSPN restoring forces
%     %% (to keep track of the whole history)
%     rrmSPN=cat(2,rrmSPN,rmSPN);
    %%%% end of incremental kinematics
    %%beginning of total kinematics
    %deltadlM4tot=extension(SPNx0,SPNy0,dxnew,dynew,d0new,angleM40,m4,M4FNx,M4FNy,LM4initial);
    deltadlM4tot_cal=extension(SPNx0,SPNy0,dxnew,dynew,d0new,angleM40,m4,M4FNx,M4FNy,LM4initial);
    m4calc = cat(1,m4calc,deltadlM4tot_cal);
    
    deltadlM4tot=extension(SPNx0,SPNy0,dxnew,dynew,d0new,angleM40,m4,M4FNx,M4FNy,LM4initial);
    dtotM4=cat(1,dtotM4,deltadlM4tot);
    %% compute the total lengths of measurement devices
    LM1tot=LM1initial+deltadlM1tot;
    LM2tot=LM2initial+deltadlM2tot;
    LM3tot=LM3initial+deltadlM3tot;
    LM4tot=LM4initial+deltadlM4tot;
    
    
    %%% First update the coordinate for the second MSN (where M2 and M4 are
    %%% connected)
    theta3=(acos((Lfedge^2+LM4tot^2-LM2tot^2)/(2*Lfedge*LM4tot))+atan(4/Lf));
    MSN2xl=LM4tot*cos(theta3);
    MSN2yl=LM4tot*sin(theta3);
    %% w.r.t. global coord sys:
    MSN2X=M4FNx+MSN2xl;
    MSN2Y=M4FNy+MSN2yl;
    %%% Update the coordinates of the first MSN;
    Lgh=((MSN2Y-M1FNy)^2+(MSN2X-M1FNx)^2)^0.5;
    beta3=acos((Ledge^2+Lgh^2-LM1tot^2)/(2*Ledge*Lgh));
    MSN1xl=-Ledge*cos(beta3);
    MSN1yl=Ledge*sin(beta3);
    phiSN1=atan((MSN2Y-M1FNy)/(MSN2X-M1FNx));
    MSN1glob=[cos(phiSN1) -sin(phiSN1); sin(phiSN1) cos(phiSN1)]*[MSN1xl;MSN1yl]+[MSN2X; MSN2Y];
    MSN1X=MSN1glob(1);MSN1Y=MSN1glob(2);
    
    %%% compute the measured disp @ SPN;
    dmesSPN0=atan((MSN2Y-MSN1Y)/(MSN2X-MSN1X));
    SPNxnew=MSN2X-m2*cos(angleM20+dmesSPN0);
    SPNynew=MSN2Y-m2*sin(angleM20+dmesSPN0);
    dmesSPNX=(SPNxnew-SPNx0);
    dmesSPNY=(SPNynew-SPNy0);
    dmesSPNtot=[dmesSPNX;dmesSPNY;dmesSPN0];
    ddmesSPNtot=cat(2,ddmesSPNtot,dmesSPNtot);
    
    % calculate the local total measured restoring forces:
    r1local=[k1*deltadlA1;0];
    r2local=[k2*deltadlA2;0];
    r3local=[k3*deltadlA3;0];
    %updating actuator coordinates at SN Nodes:
    [A1SNx_new,A1SNy_new]=newcoord(SPNx0,SPNy0,dxnew,dynew,d0new,angleA10,a1);
    [A2SNx_new,A2SNy_new]=newcoord(SPNx0,SPNy0,dxnew,dynew,d0new,angleA20,a2);
    [A3SNx_new,A3SNy_new]=newcoord(SPNx0,SPNy0,dxnew,dynew,d0new,angleA30,a3);
    
    %updating the transformation matrices:
    LA1=((A1SNx_new-A1FNx)^2+(A1SNy_new-A1FNy)^2)^0.5;
    LA2=((A2SNx_new-A2FNx)^2+(A2SNy_new-A2FNy)^2)^0.5;
    LA3=((A3SNx_new-A3FNx)^2+(A3SNy_new-A3FNy)^2)^0.5;

    TglA1=gtol(A1SNx_new,A1SNy_new,A1FNx,A1FNy,LA1);
    TglA2=gtol(A2SNx_new,A2SNy_new,A2FNx,A2FNy,LA2);
    TglA3=gtol(A3SNx_new,A3SNy_new,A3FNx,A3FNy,LA3);
    [xbarA1,ybarA1]=xybar(SPNx,SPNy,A1SNx_new,A1SNy_new);
    TmsA1=mtos(xbarA1,ybarA1);
    [xbarA2,ybarA2]=xybar(SPNx,SPNy,A2SNx_new,A2SNy_new);
    TmsA2=mtos(xbarA2,ybarA2);
    [xbarA3,ybarA3]=xybar(SPNx,SPNy,A3SNx_new,A3SNy_new);
    TmsA3=mtos(xbarA3,ybarA3);
    %%%global restoring forces @ actuator nodes
    rmA1SN=TglA1'*r1local;
    rmA2SN=TglA2'*r2local;
    rmA3SN=TglA3'*r3local;
    %%%%%%global restoring forces @ SPN from each actuator
    rmSPNA1=TmsA1'*cat(1,rmA1SN,0);
    rmSPNA2=TmsA2'*cat(1,rmA2SN,0);
    rmSPNA3=TmsA3'*cat(1,rmA3SN,0);
    %%%%considering the contribution from all the actuators
    rmSPN=rmSPNA1+rmSPNA2+rmSPNA3;
    %% to build arrays (row(2)) for the total measuredSPN restoring forces
    %% (to keep track of the whole history)
    rrmSPN=cat(2,rrmSPN,rmSPN);

% determine velocity and acceleration for next time step from numerical integration
    uddii=Mhat*(peff(i,:)'-rmSPN-C*udi-0.5*dt*C*uddi);
    udii=udi+0.5*dt*(uddi+uddii);
    ui=uii;
    udi=udii;
    uddi=uddii;
end
%save ddcA1 ddcA1 -ascii
%save ddcA2 ddcA2 -ascii
%save ddcA3 ddcA3 -ascii
    

dmesSPNx=0;
dmesSPNy=0;
dmesSPN0=0;

for i=1:N;    
dmesSPNxc=ddmesSPN(1,i);
dmesSPNyc=ddmesSPN(2,i);
dmesSPN0c=ddmesSPN(3,i);

dmesSPNx=cat(1,dmesSPNx,dmesSPNxc);
dmesSPNy=cat(1,dmesSPNy,dmesSPNyc);
dmesSPN0=cat(1,dmesSPN0,dmesSPN0c);

end



dmesSPNxs=dmesSPNx(3:end);
dmesSPNys=dmesSPNy(3:end);
dmesSPN0s=dmesSPN0(3:end);


dmesSPNxtot=0;
dmesSPNytot=0;
dmesSPN0tot=0;

for i=1:N;    
dmesSPNxctot=ddmesSPNtot(1,i);
dmesSPNyctot=ddmesSPNtot(2,i);
dmesSPN0ctot=ddmesSPNtot(3,i);

dmesSPNxtot=cat(1,dmesSPNxtot,dmesSPNxctot);
dmesSPNytot=cat(1,dmesSPNytot,dmesSPNyctot);
dmesSPN0tot=cat(1,dmesSPN0tot,dmesSPN0ctot);

end



dmesSPNxstot=dmesSPNxtot(3:end);
dmesSPNystot=dmesSPNytot(3:end);
dmesSPN0stot=dmesSPN0tot(3:end);

leng=length(dmesSPNxs);
% dxs=dx(1:leng);
% dys=dy(1:leng);
% d0s=d0(1:leng);
dxs=dx(2:end-1);
dys=dy(2:end-1);
d0s=d0(2:end-1);
ddcA1s=ddcA1(2:end-1);
ddcA2s=ddcA2(2:end-1);
ddcA3s=ddcA3(2:end-1);
%allcom=[dxs dys d0s];
%temp remove next line
%alldisplacements=[t(1:end-2)',dxs, dmesSPNxs, dys, dmesSPNys, d0s, dmesSPN0s,ddcA1s,ddcA2s,ddcA3s]; 
%wk1write('alldisp.wk1', alldisplacements)
%temp remove next line
%csvwrite('alldispupdt.csv',alldisplacements);
% Write to file
% csvwrite('actuators.csv',results);
% csvwrite('m4.csv',m4calc);

maxa1=max(ddcA1s);
mina1=min(ddcA1s);
maxa2=max(ddcA2s);
mina2=min(ddcA2s);
maxa3=max(ddcA3s);
mina3=min(ddcA3s);

for i=1:leng-1;
inca1(i)=ddcA1s(i+1)-ddcA1s(i);
inca2(i)=ddcA2s(i+1)-ddcA2s(i);
inca3(i)=ddcA3s(i+1)-ddcA3s(i);
end
maxinca1=max(abs(inca1));
maxinca2=max(abs(inca2));
maxinca3=max(abs(inca3));
mininca1=min(abs(inca1));
mininca2=min(abs(inca2));
mininca3=min(abs(inca3));
[maxa1 maxa2 maxa3;mina1 mina2 mina3;maxinca1 maxinca2 maxinca3;mininca1 mininca2 mininca3]

save all;

figure(1)
plot(t(1:end-2)',dxs,t(1:end-2)', dmesSPNxs)
legend('comSPNx','measSPNx')
xlabel('time')
ylabel('disp (in)')

figure(2)
plot(t(1:end-2)',dys, t(1:end-2)',dmesSPNys)
legend('comSPNy','measSPNy')
xlabel('time')
ylabel('disp (in)')

figure(3)
plot(t(1:end-2)',d0s,t(1:end-2)', dmesSPN0s)
legend('comSPN0','measSPN0')
xlabel('time')
ylabel('rot (rad)')

figure(4)
plot(t(1:end-2)',dxs,t(1:end-2)', dmesSPNxstot,'-.r')
legend('comSPNx','measSPNxtot')
xlabel('time')
ylabel('disp (in)')

figure(5)
plot(t(1:end-2)',dys, t(1:end-2)',dmesSPNystot,'-.r')
legend('comSPNy','measSPNytot')
xlabel('time')
ylabel('disp (in)')

figure(6)
plot(t(1:end-2)',d0s,t(1:end-2)', dmesSPN0stot,'-.r')
legend('comSPN0','measSPN0tot')
xlabel('time')
ylabel('rot (rad)')

%%%%% FUNCTIONS%%%%%%%%%%%%%%%%%%%%%%%%%
function [Tms]=mtos(xbar,ybar)
Tms(1,1)=1;Tms(1,2)=0;Tms(1,3)=ybar;
Tms(2,1)=0;Tms(2,2)=1;Tms(2,3)=-xbar;
Tms(3,1)=0;Tms(3,2)=0;Tms(3,3)=1;

function [Tgl]=gtol(Sx,Sy,Fx,Fy,length)
Tgl(1,1)=(Sx-Fx)/length; Tgl(1,2)=(Sy-Fy)/length;
Tgl(2,1)=-(Sy-Fy)/length; Tgl(2,2)=(Sx-Fx)/length;

function [xbar, ybar] = xybar(Spnx,Spny,Sx,Sy)
xbar=Spnx-Sx;
ybar=Spny-Sy;

function [CTms]=compmtos(xbarm1,ybarm1,xbarm2,ybarm2,xbarm3,ybarm3,M1snx,M1fnx,M1sny,M1fny,m1length,M2snx,M2fnx,M2sny,M2fny,m2length,M3snx,M3fnx,M3sny,M3fny,m3length)
tbys(1,1)=(M1snx-M1fnx)/m1length; tbys(1,2)=(M1sny-M1fny)/m1length;tbys(1,3)=0;tbys(1,4)=0;tbys(1,5)=0;tbys(1,6)=0;
tbys(2,1)=0;tbys(2,2)=0;tbys(2,3)=(M2snx-M2fnx)/m2length;tbys(2,4)=(M2sny-M2fny)/m2length;tbys(2,5)=0;tbys(2,6)=0;
tbys(3,1)=0;tbys(3,2)=0;tbys(3,3)=0;tbys(3,4)=0;tbys(3,5)=(M3snx-M3fnx)/m3length;tbys(3,6)=(M3sny-M3fny)/m3length;
sbyt(1,1)=1;sbyt(1,2)=0;sbyt(1,3)=ybarm1;
sbyt(2,1)=0;sbyt(2,2)=1;sbyt(2,3)=-xbarm1;
sbyt(3,1)=1;sbyt(3,2)=0;sbyt(3,3)=ybarm2;
sbyt(4,1)=0;sbyt(4,2)=1;sbyt(4,3)=-xbarm2;
sbyt(5,1)=1;sbyt(5,2)=0;sbyt(5,3)=ybarm3;
sbyt(6,1)=0;sbyt(6,2)=1;sbyt(6,3)=-xbarm3;
tbys;
sbyt;
CTms=tbys*sbyt;
function [ext]=extension(Spnx,Spny,dxnew,dynew,d0new,angle0,diagdist,fixedx,fixedy,initiallength)
Spnxnew=Spnx+dxnew;
Spnynew=Spny+dynew;
anglenew=angle0+d0new;
newXcoord=Spnxnew+diagdist*cos(anglenew);
newYcoord=Spnynew+diagdist*sin(anglenew);
length=((newXcoord-fixedx)^2+(newYcoord-fixedy)^2)^0.5;
ext=length-initiallength;
function [newXcoord,newYcoord]=newcoord(Spnx,Spny,dxnew,dynew,d0new,angle0,diagdist)
Spnxnew=Spnx+dxnew;
Spnynew=Spny+dynew;
anglenew=angle0+d0new;
newXcoord=Spnxnew+diagdist*cos(anglenew);
newYcoord=Spnynew+diagdist*sin(anglenew);



