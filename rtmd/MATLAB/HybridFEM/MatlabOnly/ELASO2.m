%%
function [relas] = ELASO2(ndof,vars)
%%
relas=zeros(ndof,1);
%BASIC RESISTING FORCES
      relas(1,1)=(vars.ftot(1,1)-vars.aint(1,1))*vars.cosa-(vars.sftot(1,1)-vars.sfint(1,1))*vars.sina;
      relas(2,1)=(vars.ftot(1,1)-vars.aint(1,1))*vars.sina+(vars.sftot(1,1)-vars.sfint(1,1))*vars.cosa;
      relas(3,1)=vars.bmtot(1,1)-vars.bmint(1,1)-vars.beint(1,1);
      relas(4,1)=(vars.ftot(2,1)-vars.aint(2,1))*vars.cosa-(vars.sftot(2,1)-vars.sfint(2,1))*vars.sina;
      relas(5,1)=(vars.ftot(2,1)-vars.aint(2,1))*vars.sina+(vars.sftot(2,1)-vars.sfint(2,1))*vars.cosa;
      relas(6,1)=vars.bmtot(2,1)-vars.bmint(2,1)-vars.beint(2,1);
%%
