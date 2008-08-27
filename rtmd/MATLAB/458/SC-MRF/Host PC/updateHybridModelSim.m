%Calculate interstory drifts
handles.Vars.drift(1) = handles.Vars.u(1);
handles.Vars.drift(2) = handles.Vars.u(2)-handles.Vars.u(1);
handles.Vars.drift(3) = handles.Vars.u(3)-handles.Vars.u(2);
handles.Vars.drift(4) = handles.Vars.u(4)-handles.Vars.u(3);
handles.Vars.results_drift(handles.Vars.HybridStepCount,:) = handles.Vars.drift';

%Update Spring States
handles.Vars.SpringA = SpringUpdateState(handles.Vars.SpringA, handles.Vars.drift(1));
handles.Vars.SpringB = SpringUpdateState(handles.Vars.SpringB, handles.Vars.drift(2));
handles.Vars.SpringC = SpringUpdateState(handles.Vars.SpringC, handles.Vars.drift(3));
handles.Vars.SpringD = SpringUpdateState(handles.Vars.SpringD, handles.Vars.drift(4));

%Get Spring Restoring Forces and form force vector
handles.Vars.re(1) = -(handles.Vars.SpringB.dFs - handles.Vars.SpringA.dFs);
handles.Vars.re(2) = -(handles.Vars.SpringC.dFs - handles.Vars.SpringB.dFs);
handles.Vars.re(3) = -(handles.Vars.SpringD.dFs - handles.Vars.SpringC.dFs);
handles.Vars.re(4) = handles.Vars.SpringD.dFs;    
handles.Vars.results_force_exp(handles.Vars.HybridStepCount,:) = handles.Vars.re';

%Get Test Frame force vector
handles.Vars.ra = handles.Vars.Ka * handles.Vars.u;
handles.Vars.results_force_ana(handles.Vars.HybridStepCount,:) = handles.Vars.ra';

%Form full hybrid force vector
handles.Vars.r = handles.Vars.re + handles.Vars.ra;
handles.Vars.results_force(handles.Vars.HybridStepCount,:) = handles.Vars.r';