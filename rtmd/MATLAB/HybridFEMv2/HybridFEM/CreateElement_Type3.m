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
%% Variables use internally
xyj(1,1)= Node2.Xcoord;    
xyj(2,1)= Node2.Ycoord;
xyi(1,1)= Node1.Xcoord;
xyi(2,1)= Node1.Ycoord;
ftyp(1,1)=Material.E;
ftyp(1,2)=vars.hard;
ftyp(1,3)=vars.Area;
ftyp(1,4)=vars.Inertia;
ftyp(1,5)=4;
ftyp(1,6)=4;
ftyp(1,7)=2;
ftyp(1,8)=vars.Av;
ftyp(1,9)=vars.v;
ftyp(1,10)=0.0001;
kysur(1,1)=2;
sff(1,1)= vars.Mpl;
sff(2,1)= vars.Mpl;
sff(3,1)= vars.Npl;
sff(4,1)= vars.Npl;
sff(5,1)= 1.0;
sff(6,1)= 0.15;
sff(7,1)= 1.0;
sff(8,1)= 0.15;
imbt=1;
ksf=ones(2,1);
pmax = zeros(5,2,1); 
aa1 = zeros(4,2,1);
aa2 = zeros(4,2,1);
xm = zeros(5,2);

%% STIFFNESS TYPES
        if (ftyp(1,2)==0) 
            ftyp(1,2)=1.e-6;
        end
        if (ftyp(1,2)>0.99999) 
            ftyp(1,2)=0.99999;
        end
        if (ftyp(1,10)==0.0) 
            ftyp(1,10)=1.e-5;
        end
%% CROSS SECTION YIELD SURFACES
          if(kysur(1,1)==0) 
            kysur(1,1)=1;
          end
          sff(2,1) = -abs(sff(2,1));
          sff(3,1) = -abs(sff(3,1));
          sff(4,1) =  abs(sff(4,1));
          if (sff(6,1)==0.0) 
            sff(6,1)=1.0e-6;
          end
          if (sff(8,1)==0.0) 
            sff(8,1)=1.0e-6;
          end
        if (kysur(1,1)==1)
% c---beam type
          for j=1:2
            for i=1:5
              pmax(i,j,1)=0.0;
            end
            for i=1:4
              aa1(i,j,1)=0.0;
              aa2(i,j,1)=1.0;
            end
          end
          pmax(2,1,1)=1.e20;
          pmax(2,2,1)=1.e20;
          aa2(1,1,1)=1.0/sff(1,1);
          aa2(1,2,1)=1.0/sff(2,1);
        else
% c---steel and r.c. types
          pmax(1,1,1)=sff(3);
          pmax(1,2,1)=sff(3);
          pmax(2,1,1)=sff(3)*sff(6);
          pmax(2,2,1)=sff(3)*sff(8);
          pmax(3,1,1)=0.0;
          pmax(3,2,1)=0.0;

          if (kysur(1,1)==3)
% c---dummy point for r.c. surface
            sff(6,1)=0.99;
            sff(8,1)=0.99;
          end
          pmax(4,1,1)=sff(4,1)*sff(6,1);
          pmax(4,2,1)=sff(4,1)*sff(8,1);
          pmax(5,1,1)=sff(4,1);
          pmax(5,2,1)=sff(4,1);
          xm(1,1)=0.0;
          xm(1,2)=0.0;
          xm(2,1)=sff(1,1)*sff(5,1);
          xm(2,2)=sff(2,1)*sff(7,1);
          xm(3,1)=sff(1,1);
          xm(3,2)=sff(2,1);
          xm(4,1)=xm(2,1);
          xm(4,2)=xm(2,2);
          if(kysur(1,1)==3)
% c---dummy point for r.c. surface
            xm(4,1)=sff(1,1)*0.01;
            xm(4,2)=sff(2,1)*0.01;
          end 

          xm(5,1)=0.0;
          xm(5,2)=0.0;
          for j=1:2
            p2=pmax(1,j,1);
            xm2=xm(1,j);
            for i=1:4
              p1=p2;
              xm1=xm2;
              p2=pmax(i+1,j,1);
              xm2=xm(i+1,j);
              denom=xm1*p2-xm2*p1;
              aa2(i,j,1)=(p2-p1)/denom;
              aa1(i,j,1)=(xm1-xm2)/denom;
            end
          end
        end 
%% READ ELEMENT GENERATION                    
          if((kysur(ksf(1,1),1)==1) && (kysur(ksf(2,1),1)==1))
            vars.ksurf=0;
          else
            vars.ksurf=1;
          end
%% ELEMENT LENGTH
          xl=xyj(1,1)-xyi(1,1);
          yl=xyj(2,1)-xyi(2,1);
          vars.fl=sqrt(xl^2+yl^2);
          vars.cosa=xl/vars.fl;
          vars.sina=yl/vars.fl;
%% ELEMENT PROPERTIES
        ymod=ftyp(imbt,1);
        vars.psh=ftyp(imbt,2);
        ppsh=1.0-vars.psh;
        area=ftyp(imbt,3);
        vars.eal=ymod*area/vars.fl;
        eil=ymod*ftyp(imbt,4)*ppsh/vars.fl;
        facl=ftyp(imbt,5);
        facr=ftyp(imbt,6);
        faclr=ftyp(imbt,7);
        if (facl==0.0) 
            facl=1.e-6;
        end
        if (facr==0.0) 
            facr=1.e-6;
        end
%% SHEAR
        if (ftyp(imbt,8)==0.) 
          shfac=eil/(ftyp(imbt,1)/(2.*(1.+ftyp(imbt,9)))*ftyp(imbt,8)*vars.fl*ppsh);
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
          kk=ksf(k,1);
          for j=1:2
            for i=1:3
              vars.pmx(i,j,k)=pmax(i+1,j,kk);
            end
            for i=1:4
              vars.a2(i,j,k)=ppsh/aa2(i,j,kk);
              vars.a1(i,j,k)=aa1(i,j,kk)*vars.a2(i,j,k);
            end
           end
        end
%% YIELD MOMENTS FOR UNSTRESSED STATE
[vars.bmy] = YMOM02(zeros(2,1),vars.pmx,vars.a1,vars.a2,zeros(2,2));
%% end of 


function [bmy] = YMOM02(ftot,pmx,a1,a2,bmy)
%%
facc=-1.0;
for iend = 1 : 2
    % Change sign of axial force and end 1
    fft = ftot(iend,1) * facc;
    facc = 1;
    fac = 1;
    for j = 1 : 2
        fac = -fac;
        yieldcalc = 0;  % flag if the yield moment is calculated inside 
                        % this next for loop
        for i = 1 : 3
            if (fft < pmx(i,j,iend))
                yieldcalc = 1;  % set flag since the condition was met
                % Calculate yield moment (M = a2 + a1P)
                bbmy = a2(i,j,iend) - a1(i,j,iend) * fft;
                if (fac * bbmy > 0) 
                    % Axial force is beyond Pc and Pt
                    bbmy = 0;
                end
                bmy(iend,j) = bbmy;
            end
        end
        % Only calculate yield moment if it wasn't calculated in the above
        % for loop
        if (yieldcalc == 0)
            i = 4;
            % Calculate yield moment (M = a2 + a1P)
            bbmy = a2(i,j,iend) - a1(i,j,iend) * fft;
            if (fac * bbmy > 0) 
                % Axial force is beyond Pc and Pt
                bbmy = 0;
            end
            bmy(iend,j) = bbmy;
        end
    end
end
%c ---------------------------------INTERCHANGE BMY(1,1) AND BMY(1,2) AND
%c ------------------------------AND ALSO CHANGE SIGN OF MOMENTS AT END 1
      fac=-bmy(1,1);
      bmy(1,1)=-bmy(1,2);
      bmy(1,2)=fac;
      
      
      
      
      
