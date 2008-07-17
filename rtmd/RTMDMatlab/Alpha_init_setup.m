%%%%structural properties
Cve=0.5618e6; %Ns/m
Kve=3.92e6; %N/m
man=135500; %kg
kelm=[3;3;2.5]*Kve;
mass=[1;1;0.5]*man;

%%% for the servo control loop (others are  hardcoded)
Mass=1335+1025;%the mass of the damper+piston
%Ceq=0.5618e6*0.6;
%Keq=3.92e6*0.3;
%Keq=3.92e6*0.8;
%Ceqin=0.5618e6*0.4;
%Ceqout=0.5618e6*0.25;
%%%
%inoutlimit=13/1000;
%damperhardening=20/100;

%FSYdamperposin=Keq*15/1000;
%FSYdampernegin=Keq*15/1000;
%FSYdamperposout=Keq*5/1000;
%FSYdampernegout=Keq*10/1000;


%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Keq=3.92e6*1;
Ceqin=0.5618e6*0.2;
Ceqout=0.5618e6*0.2;
%%%
inoutlimit=16/1000;
damperhardeningin=50/100;
damperhardeningout=20/100;

FSYdamperposin=Keq*5/1000;
FSYdampernegin=Keq*5/1000;
FSYdamperposout=Keq*5/1000;
FSYdampernegout=Keq*5/1000;


%%Controller parameters

feedforward = 2;
Ti=5;
Kp=40;




%%% define yield displacements for each story
deltay1=10000000000000000000000000000;%mm
deltay2=1000000000000000000000000000000;%mm
deltay3=10000000000000000000000000000000;%mm

%deltay1=10;%mm
%deltay2=10;%mm
%deltay3=10;%mm

%
scalef=1/5;
%scalef=1.125/5;
hardening=[0;0;0];
%hardening=[0.015; 0.015; 0.015];





%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

FSY=[kelm(1)*deltay1/1000;kelm(2)*deltay2/1000;kelm(3)*deltay3/1000];

kalpha=[kelm(1)+kelm(2)+Kve, -kelm(2), 0  ;
    -kelm(2),  kelm(2)+kelm(3), -kelm(3) ;
    0, -kelm(3), kelm(3)];



kalphaframe=[kelm(1)+kelm(2) -kelm(2) 0  ;
             -kelm(2)  kelm(2)+kelm(3) -kelm(3) ;
                 0 -kelm(3) kelm(3)];
%kalpha=kalphaframe;
%%%% the stiffness matrix to compute the restoring force from analytical
%%%% structure
%kalphahybrid=kalpha;
%kalphahybrid=[kelm(1)+kelm(2) -kelm(2) 0  ;
%             -kelm(2)  kelm(2)+kelm(3) -kelm(3) ;
%                  0 -kelm(3) kelm(3)];
malpha=[mass(1), 0 ,0 ;
    0 ,mass(2), 0 ;
    0, 0, mass(3)];

dalpha=inv(malpha)*kalphaframe;
lambda=eig(dalpha);
slambda=sort(lambda);
w1=(slambda(1))^0.5;
w2=(slambda(2))^0.5;
w3=(slambda(3))^0.5;

%%%%% assuming 2% damping @1st and 3rd modes;
rho=2/100;
a0=rho*(2*w1*w3)/(w1+w3);
a1=rho*2/(w1+w3);
%%%% damping matrix
calpha=a0*malpha+a1*kalphaframe;

load canoga.txt;
%load singroundacc.txt;
%canoga=singroundacc;
%Timing
%steps=1000;
steps=length(canoga);
iterations=20;

deltat=20/1024;

%%%load the EQ data
t=0:20/1024:(steps-1)*20/1024;
tfinal=(steps-1)*20/1024;
rf=[1;1;1];

p=-(9.81*1.15/1.5*scalef)*malpha*rf*canoga';
%p=10*sin(2*pi*t);
shift=[0;0;0];
p=cat(2,shift,p);
p1=p(1,:);p2=p(2,:);p3=p(3,:);
steps=length(p1);
%p=7*sin(2*pi*t);
   
%%%%%initial conditions;
%need to initialize every data cell as matrix or vector
dpred=[0;0;0];u=[0;0;0];ud=[0;0;0];uc=[0;0;0];ucold=[0;0;0];f=[0;0;0];drift=[0;0;0];
uold=[0;0;0];udold=[0;0;0];uddold=[0;0;0];rold=[0;0;0];umold=[0;0;0];
udd=inv(malpha)*(p(:,2)-calpha*ud-kalpha*u);

%%% more setup
alpha=-1/12;
%alpha=0;
beta=(1-alpha)^2/4;
gamma=0.5-alpha;
Mbar=malpha+(1+alpha)*gamma*deltat*calpha;
Mbarinv = inv(Mbar);
Kstar=Mbar+deltat^2*beta*(1+alpha)*kalpha;
Kstarinv = inv(Kstar);
