%%
function [dvax,dvr] = ELDFO2(ddise,f1,cosa, sina) 
%%
dvr=zeros(2,1);
%DEFORMATION INCREMENTS OR DEFORMATION RATES
      dvax=cosa*(ddise(4,1)-ddise(1,1))+sina*(ddise(5,1)-ddise(2,1));
      rot=(sina*(ddise(4,1)-ddise(1,1))+cosa*(ddise(2,1)-ddise(5,1)))/f1;
      dvr(1,1)=ddise(3,1)+rot;
      dvr(2,1)=ddise(6,1)+rot;
%%