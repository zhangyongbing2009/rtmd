%Get Test Frame force vector
handles.Vars.ra = handles.Vars.Ka * handles.Vars.u;
handles.Vars.results_force_ana(handles.Vars.HybridStepCount,:) = handles.Vars.ra';

%Get Experimental restoring force
handles.Vars.re(1) = handles.Vars.scr.readFloat(handles.Vars.load1_k_scr);
handles.Vars.re(2) = handles.Vars.scr.readFloat(handles.Vars.load2_k_scr);
handles.Vars.re(3) = handles.Vars.scr.readFloat(handles.Vars.load3_k_scr);
handles.Vars.re(4) = handles.Vars.scr.readFloat(handles.Vars.load4_k_scr);
handles.Vars.results_force_exp(handles.Vars.HybridStepCount,:) = handles.Vars.re';

%Form full hybrid force vector
handles.Vars.r = handles.Vars.re + handles.Vars.ra;
handles.Vars.results_force(handles.Vars.HybridStepCount,:) = handles.Vars.r';


