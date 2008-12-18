%%
function [dbm] = BMCAO2(dvr,vars)
%%
dbm=zeros(2,1);
% c ------------------SET KYY TO INDICATE TYPE OF ELEMENT STIFFNESS TO USE
      kyy=vars.kody(1,1)+2*vars.kody(2,1)+1;
      if(kyy==1)
%c---no hinge at either end
        dbm(1,1)=vars.ek11*dvr(1,1)+vars.ek12*dvr(2,1);
        dbm(2,1)=vars.ek12*dvr(1,1)+vars.ek22*dvr(2,1);
      elseif(kyy==2)
%c---hinge at end 1
        dbm(1,1)=0.0;
        dbm(2,1)=vars.ek22h*dvr(2,1);
      elseif(kyy==3)
%c---hinge at end 2
        dbm(1,1)=vars.ek11h*dvr(1,1);
        dbm(2,1)=0.0;
      elseif(kyy==4)
%c---hinges at both end
        dbm(1,1)=0.0;
        dbm(2,1)=0.0;
       else
           %never executed
      end