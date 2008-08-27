function [d]=totaltransform(N)
sampletime=0.01;
t=0:sampletime:N*sampletime;
dx=(3*sin((4*pi)*t))';
dy=(3*sin((4*pi)*t))';
d0=(0.1*sin((4*pi)*t))';

% initializations
%dxold=0;
%dyold=0;
%d0old=0;


% define the geometry
%information about the initial geometry of the set up
%note actuators and measurement devices have the same geometry
A1SNx=5.5; A2SNx=79; A3SNx=98.5;   M1SNx=5.5; M2SNx=79; M3SNx=98.5;   SPNx=42.25;   M4SNx=79;
A1SNy=42;  A2SNy=42; A3SNy=49.125; M1SNy=42;  M2SNy=42; M3SNy=49.125; SPNy=49.125;  M4SNy=42;
A1FNx=5.5; A2FNx=79; A3FNx=174.5;  M1FNx=5.5; M2FNx=79; M3FNx=174.5;  SPNx0=42.25;  M4FNx=100;
A1FNy=0;   A2FNy=0;  A3FNy=49.125; M1FNy=0;   M2FNy=0;  M3FNy=49.125; SPNy0=49.125; M4FNy=0;

%LA1=192; LA2=192; LA3=192; won't be used as STEP 4 is omitted
LM1=42; LM2=42; LM3=76; LM4=(21^2+42^2)^0.5;
LA1initial=42; LA2initial=42; LA3initial=76;
LM1initial=42; LM2initial=42; LM3initial=76; LM4initial=(21^2+42^2)^0.5;
%%%%%
%block dimensions
da12xbar=73.5/2; da3xbar=56.25;
da12ybar=14.25/2; da3ybar=0;

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
% fixed distance between the MFN's for M2 and M4
Lf=M4FNx-M2FNx;
Ledge=M2SNx-M1SNx;


%%for the quantities that we want to output:
%%local total command displacements at the actutors (ignore the first
%%0(initiation)
ddcA1=0; ddcA2=0; ddcA3=0;
%%%total measured displacement @SPN
ddmesSPN=[0;0;0];

for i=1:N;
    %% receive total command displacement @SPN
    dxnew=dx(i);
    dynew=dy(i);
    d0new=d0(i);
    %%% STEP 1 Compute the total command displacement for each actuator
    %%%%%to compute the command displacements for actuators
    deltadlA1=extension(SPNx0,SPNy0,dxnew,dynew,d0new,angleA10,a1,A1FNx,A1FNy,LA1initial);
    deltadlA2=extension(SPNx0,SPNy0,dxnew,dynew,d0new,angleA20,a2,A2FNx,A2FNy,LA2initial);
    deltadlA3=extension(SPNx0,SPNy0,dxnew,dynew,d0new,angleA30,a3,A3FNx,A3FNy,LA3initial);
    
    %% to build arrays (column(1)) for the actuator command displacements
    %% (to keep track of the whole history)
    ddcA1=cat(1,ddcA1,deltadlA1);
    ddcA2=cat(1,ddcA2,deltadlA2);
    ddcA3=cat(1,ddcA3,deltadlA3);
    
    %calculate the local measurement device extensions using trigonometry:
    deltadlM1tot=extension(SPNx0,SPNy0,dxnew,dynew,d0new,angleM10,m1,M1FNx,M1FNy,LM1initial);
    deltadlM2tot=extension(SPNx0,SPNy0,dxnew,dynew,d0new,angleM20,m2,M2FNx,M2FNy,LM2initial);
    deltadlM3tot=extension(SPNx0,SPNy0,dxnew,dynew,d0new,angleM30,m3,M3FNx,M3FNy,LM3initial);
    deltadlM4tot=extension(SPNx0,SPNy0,dxnew,dynew,d0new,angleM40,m4,M4FNx,M4FNy,LM4initial);
    
    %% compute the total lengths of measurement devices
    LM1=LM1initial+deltadlM1tot;
    LM2=LM2initial+deltadlM2tot;
    LM3=LM3initial+deltadlM3tot;
    LM4=LM4initial+deltadlM4tot;
    
    
    %%% First update the coordinate for the second MSN (where M2 and M4 are
    %%% connected)
    theta2=acos((LM2^2+Lf^2-LM4^2)/(2*LM2*Lf));
    theta3qq=asin(LM2*sin(theta2)/LM4);
    theta3=acos((Lf^2+LM4^2-LM2^2)/(2*Lf*LM4));
    error=theta3-theta3qq;
    MSN2xl=-LM4*cos(theta3);
    MSN2yl=LM4*sin(theta3);
    %% w.r.t. global coord sys:
    MSN2X=M4FNx+MSN2xl;
    MSN2Y=M4FNy+MSN2yl;
    %%% Update the coordinates of the first MSN;
    Lgh=((MSN2Y-M1FNy)^2+(MSN2X-M1FNx)^2)^0.5;
    beta3=acos((Ledge^2+Lgh^2-LM1^2)/(2*Ledge*Lgh));
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
    dmesSPN=[dmesSPNX;dmesSPNY;dmesSPN0];
    ddmesSPN=cat(2,ddmesSPN,dmesSPN);
    %%% restoring force computations omitted

end


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

leng=length(dmesSPNxs);
dxs=dx(1:leng);
dys=dy(1:leng);
d0s=d0(1:leng);
ddcA1s=ddcA1(2:end-1);
ddcA2s=ddcA2(2:end-1);
ddcA3s=ddcA3(2:end-1);
%allcom=[dxs dys d0s];
alldisplacements=[t(1:end-2)',dxs, dmesSPNxs, dys, dmesSPNys, d0s, dmesSPN0s,ddcA1s,ddcA2s,ddcA3s];
%wk1write('alldisp.wk1', alldisplacements)
csvwrite('alldisptot.csv',alldisplacements)



%%%%% FUNCTIONS%%%%%%%%%%%%%%%%%%%%%%%%%

function [ext]=extension(Spnx,Spny,dxnew,dynew,d0new,angle0,diagdist,fixedx,fixedy,initiallength)
Spnxnew=Spnx+dxnew;
Spnynew=Spny+dynew;
anglenew=angle0+d0new;
newXcoord=Spnxnew+diagdist*cos(anglenew);
newYcoord=Spnynew+diagdist*sin(anglenew);
length=((newXcoord-fixedx)^2+(newYcoord-fixedy)^2)^0.5;
ext=length-initiallength;

