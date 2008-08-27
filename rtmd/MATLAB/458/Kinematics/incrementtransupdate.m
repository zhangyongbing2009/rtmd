function [d]=incrementtransupdate(N,AI,AO)
sampletime=0.02;
t=0:sampletime:N*sampletime;
dx=0.5*(0.8*(3*sin((2*pi)*t)))';
dy=0.5*(0.8*(3*sin((2*pi)*t)))';
d0=0.5*(0.8*(0.1*sin((2*pi)*t)))';

% initializations
%dxold=0;
%dyold=0;
%d0old=0;


% define the geometry
%information about the initial geometry of the set up
%note actuators and measurement devices have the same geometry
A1SNx=5.8750; A2SNx=78.7500; A3SNx=96.5000;  M1SNx=5.8750; M2SNx=78.7500; M3SNx=96.5000;  SPNx=42.3125;
A1SNy=40.75;  A2SNy=40.75;   A3SNy=54.5625;  M1SNy=40.75;  M2SNy=40.75;   M3SNy=54.5625;  SPNy=51.9063;
A1FNx=5.8750; A2FNx=78.7500; A3FNx=175.5; M1FNx=5.8750; M2FNx=78.7500; M3FNx=175.5; SPNx0=42.3125;
A1FNy=0;      A2FNy=0;       A3FNy=54.5625;  M1FNy=0;      M2FNy=0;       M3FNy=54.5625;  SPNy0=51.9063;

%LA1 LA2 LA3 won't be used as STEP 4 is omitted
LM1= 40.75; LM2= 40.75; LM3=79.0;
LA1initial=40.75; LA2initial=40.75; LA3initial=79.0;
LM1initial=40.75; LM2initial=40.75; LM3initial=79.0;
%%%%%
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

a1=((da12xbar)^2+(da12ybar)^2)^0.5;
a2=((da12xbar)^2+(da12ybar)^2)^0.5;
a3=((da3xbar)^2+(da3ybar)^2)^0.5;
m1=((da12xbar)^2+(da12ybar)^2)^0.5;
m2=((da12xbar)^2+(da12ybar)^2)^0.5;
m3=((da3xbar)^2+(da3ybar)^2)^0.5;

%%%%%%%%%%%%%%%%%%%%%%%
deltadlM1totold=0;
deltadlM2totold=0;
deltadlM3totold=0;
%to get the total measured displacement @ SPN
dmesSPN=[0;0;0];

%%for the quantities that we want to output:
%%local total command displacements at the actutors (ignore the first
%%0(initiation)
ddcA1=0; ddcA2=0; ddcA3=0;
%%%total measured displacement @SPN
ddmesSPN=[0;0;0];

% read initial offsets from NI
scalingfactors;
offset = getsample(AI);
commands = offset;
results = [0 0 0 0];

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
    
    %%% NI Command and Feedback routines
    NIbatchForKinematics
    
    %% to build arrays (column(1)) for the actuator command displacements
    %% (to keep track of the whole history)
    ddcA1=cat(1,ddcA1,deltadlA1);
    ddcA2=cat(1,ddcA2,deltadlA2);
    ddcA3=cat(1,ddcA3,deltadlA3);
    %% STEP2 compute measured displacements of the SPN
    %calculate the local measurement device readings using trigonometry:
    %deltadlM1tot=extension(SPNx0,SPNy0,dxnew,dynew,d0new,angleM10,m1,M1FNx,M1FNy,LM1initial);
    %deltadlM2tot=extension(SPNx0,SPNy0,dxnew,dynew,d0new,angleM20,m2,M2FNx,M2FNy,LM2initial);
    %deltadlM3tot=extension(SPNx0,SPNy0,dxnew,dynew,d0new,angleM30,m3,M3FNx,M3FNy,LM3initial);
    
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
    %LA1=((A1SNx-A1FNx)^2+(A1SNy-A1FNy)^2)^0.5;%not used as STEP4 omitted
    %LA2=((A2SNx-A2FNx)^2+(A2SNy-A2FNy)^2)^0.5;%not used as STEP4 omitted
    %LA3=((A3SNx-A3FNx)^2+(A3SNy-A3FNy)^2)^0.5;%not used as STEP4 omitted
    LM1=((M1SNx-M1FNx)^2+(M1SNy-M1FNy)^2)^0.5;
    LM2=((M2SNx-M2FNx)^2+(M2SNy-M2FNy)^2)^0.5;
    LM3=((M3SNx-M3FNx)^2+(M3SNy-M3FNy)^2)^0.5;
    %%%STEP 4 omitted
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
csvwrite('alldispupdt.csv',alldisplacements)
% Write to file
csvwrite('actuators.csv',results);

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

save all;

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

