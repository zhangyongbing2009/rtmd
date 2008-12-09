%% get restoring force for the element
% returns (6x1) restoring force vector of element in global coordinates
function [Element,RestoringForce] = GetRestoringForce_Type3(Element, Structure, Integrator)
%% Get distance between nodes
I1 = Element.Nodes(1).ID;
I2 = Element.Nodes(2).ID;
%% Get the displacement Ue (6x1) vector of the element in global coordinates
Ue = zeros(6,1);
if (I1 * 3) <= Structure.NumFreeDOF
    Ue(1, 1) = Integrator.Displacement(I1 * 3 - 2, 1);
    Ue(2, 1) = Integrator.Displacement(I1 * 3 - 1, 1);
    Ue(3, 1) = Integrator.Displacement(I1 * 3, 1);
end
if (I2 * 3) <= Structure.NumFreeDOF
    Ue(4, 1) = Integrator.Displacement(I2 * 3 - 2, 1);
    Ue(5, 1) = Integrator.Displacement(I2 * 3 - 1, 1);
    Ue(6, 1) = Integrator.Displacement(I2 * 3, 1);
end
%% Input of Drain State Determination Type 2 Element
ndof=6;  %the element has 6 dofs
ddise=Ue-Element.Uprev;  %the incremental displacement vector ddise of the element in global coordinates 
Element.Uprev=Ue; %update Uprev of element for use in next state determination call
%% calculate element deformation increments
      [dvax,dvr] = ELDFO2(ddise,Element.fl,Element.cosa,Element.sina);         
%% UPDATE AXIAL FORCES AND AXIAL ENERGY
      dfax=Element.eal*dvax;
      pkgx = Element.pkg;
      Element.ftot(1,1)=Element.ftot(1,1)-dfax;
      Element.ftot(2,1)=Element.ftot(2,1)+dfax;
      Element.pkg=(Element.ftot(2,1)-Element.ftot(1,1))*0.5;
%% MOMENTS IN ELASTIC COMPONENT AT BEGINING OF STEP
      bmel(1,1)=Element.bmtot(1,1)-Element.bmep(1,1);
      bmel(2,1)=Element.bmtot(2,1)-Element.bmep(2,1);
%% LINEAR MOMENT INCREMENTS FOR ELASTO-PLASTIC COMPONENT - SET IN DBM
      [dbm] = BMCAO2(dvr,Element); 
%% TRACE NONLINEAR PATH FOR ELASTO-PLASTIC COMPONENT---state determination
 facac=0.0;
 while (facac<0.999999)
   factor=1.0-facac;
% PLASTIC HINGE ROTATIONS
        kod=Element.kody(1,1)+Element.kody(2,1)-1.0;
        if(kod>0)
% hinges at both ends
          dpr(1,1)=dvr(1,1);
          dpr(2,1)=dvr(2,1);
        elseif(kod==0)
% hinge at one end only
          dpr(1,1)=dvr(1,1)+Element.pr21*dvr(2,1);
          dpr(2,1)=dvr(2,1)+Element.pr12*dvr(1,1);
         else
             %never executed
        end
         
% CALCULATE WHICH END HAS NEXT EVENT IF YIELDS, SET KFAC=IEND AND BBMY=YIELD VALUE
% SET EVENT FACTOR IN FACTOR
       kfac=3;
         for iend=1:2
          if (Element.kody(iend,1)==0.0)
            dbmm=dbm(iend,1);
% elastic, get factor for status change(no hinge at end)
            if (dbmm<0.0)
              dbmo=Element.bmy(iend,2)-Element.bmep(iend,1);
              if(dbmo>=dbmm) 
                fac=dbmo/dbmm;
              else
                fac=2.0;
              end 
              if (fac<=factor)
                factor=fac;
                bbmy=Element.bmy(iend,2);
                kfac=iend;
              end
            elseif(dbmm>0.0)
              dbmo=Element.bmy(iend,1)-Element.bmep(iend,1);
              if(dbmo<=dbmm) 
                fac=dbmo/dbmm;
              else
                fac=2.0;
              end 
              if (fac<=factor)
                factor=fac;
                bbmy=Element.bmy(iend,1);
                kfac=iend;
              end
             else
             end
          else
% hinge at this end
            if(Element.bmep(iend,1)*dpr(iend,1)<0.0)
% unloading (plastic work negative)
              factor=0.0;
              kfac=0.0;
              Element.kody(iend,1)=0.0;
            else
% continuing to yield (plastic work positive)
            end
          end
         end
% UPDATE BENDING STATE AND CALCULATE BENDING ENERGY
% dbm= elastic increment - use if elastic bbmy= yield value - use if yields
        if(kfac~=0)
         for iend=1:2
            if(iend==kfac)
% new hinge at this end
              Element.bmep(iend,1)=bbmy;
              Element.kody(iend,1)=1;
            elseif(Element.kody(iend,1)==0)
% this end remains elastic
              dmep=factor*dbm(iend,1);
              Element.bmep(iend,1)=Element.bmep(iend,1)+dmep;
            else
% this end remains plastic
              dppr=factor*dpr(iend,1);
              Element.prtot(iend,1)=Element.prtot(iend,1)+dppr;
              if (dppr<0.0)
                Element.pracn(iend,1)=Element.pracn(iend,1)+dppr;
              else
                Element.pracp(iend,1)=Element.pracp(iend,1)+dppr;
              end
            end
         end
        else
% unloads - factor=0.
        end
% CHECK COMPLETION OF CYCLE
      facac=facac+factor;
      if (facac<0.99999)
% cycle not completed, calculate new moment increments
       [dbm] = BMCAO2(dvr,Element); 
      end
 end
%% YIELD MOMENTS FOR NEXT STEP
      if(Element.ksurf==1)
          vars = YMOM02(Element);
% CHECK YIELD SURFACE OVERSHOOT
        for iend=1:2
          if (Element.kody(iend,1)~=0.0)
% inelastic on old surface
            if (Element.bmep(iend,1)>Element.bmy(iend,1)) 
% inelastic positive on new surface also
              Element.bmep(iend,1)=Element.bmy(iend,1);
            elseif (Element.bmep(iend,1)<Element.bmy(iend,2))
% inelastic negative on new surface also
              Element.bmep(iend,1)=Element.bmy(iend,2);
            elseif ((Element.bmep(iend,1)<(Element.bmy(iend,1)*0.98)) && (Element.bmep(iend,1)>(Element.bmy(iend,2)*0.98)))
% moves inside new surface by > 2% - reset yield code
              Element.kody(iend,1)=0;
            else
            end
          else
% elastic on old surface
            if (Element.bmep(iend,1)>Element.bmy(iend,1))
% inelastic positive on new surface
              Element.bmep(iend,1)=Element.bmy(iend,1);
              Element.kody(iend,1)=1;
            elseif (Element.bmep(iend,1)<=Element.bmy(iend,2))
% inelastic negative on new surface
              Element.bmep(iend,1)=Element.bmy(iend,2);
              Element.kody(iend,1)=1;
            else
            end
          end
        end
      end
%% UPDATE TOTAL FORCES
      dsf=(Element.bmtot(1,1)+ Element.bmtot(2,1))/Element.fl;
      ppsh=Element.psh/(1.0-Element.psh);
      dmeli=(Element.ek11*dvr(1,1)+Element.ek12*dvr(2,1))*ppsh+bmel(1,1);
      dmelj=(Element.ek12*dvr(1,1)+ Element.ek22*dvr(2,1))*ppsh+bmel(2,1);
      Element.bmtot(1,1)=Element.bmep(1,1)+dmeli;
      Element.bmtot(2,1)=Element.bmep(2,1)+dmelj;
      dsf=dsf-(Element.bmtot(1,1)+Element.bmtot(2,1))/Element.fl;
      Element.sftot(1,1)=Element.sftot(1,1)-dsf;
      Element.sftot(2,1)=Element.sftot(2,1)+dsf;

 Element.relas = ELASO2(ndof,Element);  
%% set RestoringForce equal to relas
RestoringForce = Element.relas; 
%% 