% Modify the damping matrix
%if (handles.Vars.HybridStepCount > handles.Vars.steps - 100 && handles.Vars.HybridStepCount <= handles.Vars.steps)
if (handles.Vars.HybridStepCount > handles.Vars.steps && handles.Vars.HybridStepCount <= handles.Vars.steps + 100)
    handles.Vars.C = handles.Vars.C + 29/87 * handles.Vars.Co;
end

% If there are no steps left in the history file, use 0 for the acceleration
if (handles.Vars.HybridStepCount < handles.Vars.steps)
    % Update the hybrid count step
    handles.Vars.HybridStepCount = handles.Vars.HybridStepCount + 1;        
    handles.Vars.udd = handles.Vars.Minv*(handles.Vars.F(:,handles.Vars.HybridStepCount)-handles.Vars.C*handles.Vars.ud-handles.Vars.r);
    % Update counter on screen
    set(handles.commandsleft,'String',num2str(handles.Vars.steps - handles.Vars.HybridStepCount));
else
    % Update the hybrid count step
    handles.Vars.HybridStepCount = handles.Vars.HybridStepCount + 1;
    handles.Vars.udd = handles.Vars.Minv*(zeros(handles.Vars.nDOF,1)-handles.Vars.C*handles.Vars.ud-handles.Vars.r);
end  