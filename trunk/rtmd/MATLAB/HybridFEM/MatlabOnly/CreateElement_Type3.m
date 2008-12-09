%% Element object
function vars = CreateElement_Type3(ID, Node1, Node2, Material, Area, Inertia, ElemDistLoad, Stiffness, hard, Av, v, Mpl, Npl)
%% Set element nodes
vars.ID = ID;
vars.Nodes = [Node1 Node2];
%% Set element properties
vars.Material = Material;
vars.Area = Area;
vars.Inertia = Inertia;
vars.ElemDistLoad = ElemDistLoad;
vars.Stiffness = Stiffness;
vars.hard = hard;
vars.Av = Av;
vars.v = v;
vars.Mpl = Mpl;
vars.Npl = Npl; 
vars.ksurf = 0;
vars.cosa = 0;
vars.eal = 0;
vars.ek11 = 0;
vars.ek11h = 0;
vars.ek12 = 0;
vars.ek22 = 0;
vars.ek22h = 0;
vars.fl = 0;
vars.pr12 = 0;
vars.pr21 = 0;
vars.psh = 0;
vars.sina = 0;
vars.a1 = 0;
vars.a2 = 0;
vars.pmx = 0;
vars.bmy = 0;
%% Set handles to element matrices forming function and restoring force
vars.FormElementMatrices = @FormElementMatrices_Type3;
vars.GetRestoringForce = @GetRestoringForce_Type3;
%% keep track of element displacement vector in global coordinates
vars.Uprev=zeros(6,1);
%% Variables which are initialized from the input
vars.xyj(1,1)= Node2.Xcoord;    
vars.xyj(2,1)= Node2.Ycoord;
vars.xyi(1,1)= Node1.Xcoord;
vars.xyi(2,1)= Node1.Ycoord;
vars.ftyp(1,1)=Material.E;
vars.ftyp(1,2)=vars.hard;
vars.ftyp(1,3)=vars.Area;
vars.ftyp(1,4)=vars.Inertia;
vars.ftyp(1,5)=4;
vars.ftyp(1,6)=4;
vars.ftyp(1,7)=2;
vars.ftyp(1,8)=vars.Av;
vars.ftyp(1,9)=vars.v;
vars.ftyp(1,10)=0.0001;
vars.kysur(1,1)=2;
vars.sff(1,1)= vars.Mpl;
vars.sff(2,1)= vars.Mpl;
vars.sff(3,1)= vars.Npl;
vars.sff(4,1)= vars.Npl;
vars.sff(5,1)= 1.0;
vars.sff(6,1)= 0.15;
vars.sff(7,1)= 1.0;
vars.sff(8,1)= 0.15;
vars.inodi=1;
vars.inodj=1;
vars.iinc=1;
vars.iimbt=1;
vars.iiecc=0;
vars.iksfi=1;
vars.iksfj=1;
%% Other variables which are zeroed for initialization
vars.imem=0.0; 
vars.istn=zeros(8,1); 
vars.istp=zeros(8,1); 
vars.kody=zeros(2,1); 
vars.kodyx=zeros(2,1); 
vars.ksurf=0.0; 
vars.nodi=0.0; 
vars.nodj=0.0; 
vars.a=zeros(2,6); 
vars.a1=zeros(4,2,2); 
vars.a2=zeros(4,2,2); 
vars.afdp=0.0; 
vars.aint=zeros(2,1);
vars.beint=zeros(2,1); 
vars.bmint=zeros(2,1); 
vars.bmdp=zeros(2,1); 
vars.bmep=zeros(2,1); 
vars.bmtot=zeros(2,1); 
vars.bmy=zeros(2,2); 
vars.cosa=0.0; 
vars.coskg=0.0; 
vars.eal=0.0; 
vars.ec=zeros(1,1); 
vars.ek11=0.0;
vars.ek11h=0.0;  
vars.ek12=0.0; 
vars.ek22=0.0; 
vars.ek22h=0.0; 
vars.endamp=0.0; 
vars.enelas=0.0; 
vars.fl=0.0; 
vars.flkg=0.0; 
vars.ftot=zeros(2,1);  
vars.ovtol=0.0; 
vars.pkg=0.0; 
vars.pkgp=0.0;
vars.pmx=zeros(3,2,2); 
vars.pracn=zeros(2,1); 
vars.pracp=zeros(2,1); 
vars.pr12=0.0; 
vars.pr21=0.0; 
vars.prtot=zeros(2,1); 
vars.psh=0.0; 
vars.senn=zeros(8,1); 
vars.senp=zeros(8,1); 
vars.sfint=zeros(2,1); 
vars.sftot=zeros(2,1); 
vars.sina=0.0; 
vars.sinkg=0.0;
vars.pmax=zeros(5,2,1); 
vars.aa1 =zeros(4,2,1);
vars.aa2 =zeros(4,2,1);
vars.xm=zeros(5,2);
vars.finit=0.0; 
vars.ecc=0.0; 
vars.ksf=zeros(2,1); 
vars.w=0.0;
vars.relas = 0;
%%  some other variables (not members of object vars)
small=1.e-10;  
nmbt=1;
necc=0.0;
nsurf=1.;
%% STIFFNESS TYPES
        if (vars.ftyp(1,2)==0) 
            vars.ftyp(1,2)=1.e-6;
        end
        if (vars.ftyp(1,2)>0.99999) 
            vars.ftyp(1,2)=0.99999;
        end
        if (vars.ftyp(1,10)==0.0) 
            vars.ftyp(1,10)=1.e-5;
        end
%% CROSS SECTION YIELD SURFACES
          if(vars.kysur(1,1)==0) 
            vars.kysur(1,1)=1;
          end
          if(vars.kysur(1,1)==1)
          nn=2;
          else
          nn=8;
          end 
          vars.sff(2,1) = -abs(vars.sff(2,1));
          vars.sff(3,1) = -abs(vars.sff(3,1));
          vars.sff(4,1) =  abs(vars.sff(4,1));
          if (vars.sff(6,1)==0.0) 
            vars.sff(6,1)=1.0e-6;
          end
          if (vars.sff(8,1)==0.0) 
            vars.sff(8,1)=1.0e-6;
          end
        if (vars.kysur(1,1)==1)
% c---beam type
          for j=1:2
            for i=1:5
              vars.pmax(i,j,1)=0.0;
            end
            for i=1:4
              vars.aa1(i,j,1)=0.0;
              vars.aa2(i,j,1)=1.0;
            end
          end
          vars.pmax(2,1,1)=1.e20;
          vars.pmax(2,2,1)=1.e20;
          vars.aa2(1,1,1)=1.0/vars.sff(1,1);
          vars.aa2(1,2,1)=1.0/vars.sff(2,1);
        else
% c---steel and r.c. types
          vars.pmax(1,1,1)=vars.sff(3);
          vars.pmax(1,2,1)=vars.sff(3);
          vars.pmax(2,1,1)=vars.sff(3)*vars.sff(6);
          vars.pmax(2,2,1)=vars.sff(3)*vars.sff(8);
          vars.pmax(3,1,1)=0.0;
          vars.pmax(3,2,1)=0.0;

          if (vars.kysur(1,1)==3)then
% c---dummy point for r.c. surface
            vars.sff(6,1)=0.99;
            vars.sff(8,1)=0.99;
          end
          vars.pmax(4,1,1)=vars.sff(4,1)*vars.sff(6,1);
          vars.pmax(4,2,1)=vars.sff(4,1)*vars.sff(8,1);
          vars.pmax(5,1,1)=vars.sff(4,1);
          vars.pmax(5,2,1)=vars.sff(4,1);
          vars.xm(1,1)=0.0;
          vars.xm(1,2)=0.0;
          vars.xm(2,1)=vars.sff(1,1)*vars.sff(5,1);
          vars.xm(2,2)=vars.sff(2,1)*vars.sff(7,1);
          vars.xm(3,1)=vars.sff(1,1);
          vars.xm(3,2)=vars.sff(2,1);
          vars.xm(4,1)=vars.xm(2,1);
          vars.xm(4,2)=vars.xm(2,2);
          if(vars.kysur(1,1)==3)
% c---dummy point for r.c. surface
            vars.xm(4,1)=vars.sff(1,1)*0.01;
            vars.xm(4,2)=vars.sff(2,1)*0.01;
          end 

          vars.xm(5,1)=0.0;
          vars.xm(5,2)=0.0;
          for j=1:2
            p2=vars.pmax(1,j,1);
            xm2=vars.xm(1,j);
            for i=1:4
              p1=p2;
              xm1=xm2;
              p2=vars.pmax(i+1,j,1);
              xm2=vars.xm(i+1,j);
              denom=xm1*p2-xm2*p1;
              vars.aa2(i,j,1)=(p2-p1)/denom;
              vars.aa1(i,j,1)=(xm1-xm2)/denom;
            end
          end
        end 
%% READ ELEMENT GENERATION
          vars.nodi=vars.inodi;
          vars.nodj=vars.inodj;
          inc=vars.iinc;
          if (inc==0) 
              inc=1;
          end
          imbt=vars.iimbt;
          iecc=vars.iiecc;
          vars.ksf(1,1)=vars.iksfi;
          vars.ksf(2,1)=vars.iksfj;
          if((vars.kysur(vars.iksfi,1)==1) && (vars.kysur(vars.iksfj,1)==1))
            vars.ksurf=0;
          else
            vars.ksurf=1;
          end
%% ELEMENT LENGTH
          xl=vars.xyj(1,1)-vars.xyi(1,1);
          yl=vars.xyj(2,1)-vars.xyi(2,1);
          vars.flkg=sqrt(xl^2+yl^2);
          vars.coskg=xl/vars.flkg;
          vars.sinkg=yl/vars.flkg;
          vars.fl=sqrt(xl^2+yl^2);
          vars.cosa=xl/vars.fl;
          vars.sina=yl/vars.fl;
%% ELEMENT PROPERTIES
        ymod=vars.ftyp(imbt,1);
        vars.psh=vars.ftyp(imbt,2);
        ppsh=1.0-vars.psh;
        area=vars.ftyp(imbt,3);
        vars.eal=ymod*area/vars.fl;
        eil=ymod*vars.ftyp(imbt,4)*ppsh/vars.fl;
        facl=vars.ftyp(imbt,5);
        facr=vars.ftyp(imbt,6);
        faclr=vars.ftyp(imbt,7);
        if (facl==0.0) 
            facl=1.e-6;
        end
        if (facr==0.0) 
            facr=1.e-6;
        end
%% SHEAR
        if (vars.ftyp(imbt,8)==0.) 
          shfac=eil/(vars.ftyp(imbt,1)/(2.*(1.+vars.ftyp(imbt,9)))*vars.ftyp(imbt,8)*vars.fl*ppsh);
          det=facl*facr-faclr^2;
          fii=facr/det+shfac;
          fjj=facl/det+shfac;
          fij=-faclr/det+shfac;
          det=fii*fjj-fij^2;
          facr=fii/det;
          facl=fjj/det;
          faclr=-fij/det;
        end 
        vars.ek11=eil*facl;
        vars.ek22=eil*facr;
        vars.ek12=eil*faclr;
        vars.ek11h=vars.ek11-vars.ek12^2/vars.ek22;
        vars.ek22h=vars.ek22-vars.ek12^2/vars.ek11;
        vars.pr12=vars.ek12/vars.ek22;
        vars.pr21=vars.ek12/vars.ek11;
%% YIELD SURFACE DATA
        for  k=1:2
          kk=vars.ksf(k,1);
          for j=1:2
            for i=1:3
              vars.pmx(i,j,k)=vars.pmax(i+1,j,kk);
            end
            for i=1:4
              vars.a2(i,j,k)=ppsh/vars.aa2(i,j,kk);
              vars.a1(i,j,k)=vars.aa1(i,j,kk)*vars.a2(i,j,k);
            end
           end
        end
%% DISPLACEMENT TRANSFORMATION
        vars.a(1,1)=-vars.sina/vars.fl;
        vars.a(1,2)=vars.cosa/vars.fl;
        vars.a(1,3)=1.0;
        vars.a(1,4)=-vars.a(1,1);
        vars.a(1,5)=-vars.a(1,2);
        vars.a(1,6)=0.0;
        vars.a(2,1)=vars.a(1,1);
        vars.a(2,2)=vars.a(1,2);
        vars.a(2,3)=0.0;
        vars.a(2,4)=vars.a(1,4);
        vars.a(2,5)=vars.a(1,5);
        vars.a(2,6)=1.0;
        if (iecc==0)
          vars.ec(1,1)=1.23456e10;
        else
          vars.a(2,3)=(vars.sina*vars.ec(3,1)+vars.cosa*vars.ec(1,1))/vars.fl;
          vars.a(1,3)=1.+ vars.a(2,3);
          vars.a(1,6)=(-vars.sina*vars.ec(4,1)-vars.cosa*vars.ec(2,1))/vars.fl;
          vars.a(2,6)=1.+ vars.a(1,6);
        end 
%% YIELD MOMENTS FOR UNSTRESSED STATE
 vars = YMOM02(vars);
%% OVERSHOOT TOLERANCE
        vars.ovtol=vars.ftyp(imbt,10);
%% end of 


