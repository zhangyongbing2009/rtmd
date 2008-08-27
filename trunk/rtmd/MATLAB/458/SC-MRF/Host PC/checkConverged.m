%Checks to see if the LVDTs are converged to the target +/- tolerance

% Check LVDT1 convergence
if (LVDT1 < target1-(str2double(get(handles.tolerance1,'String'))))
    set(handles.lvdt1,'BackgroundColor',[1 0 0]);
elseif (LVDT1 > target1+(str2double(get(handles.tolerance1,'String'))))     
    set(handles.lvdt1,'BackgroundColor',[1 0 0]);
else
% Converged
    set(handles.lvdt1,'BackgroundColor',[1 1 1]);
    LVDT1conv = 1;  
end        

% Check LVDT2 convergence
if (LVDT2 < target2-(str2double(get(handles.tolerance2,'String'))))
    set(handles.lvdt2,'BackgroundColor',[1 0 0]);
elseif (LVDT2 > target2+(str2double(get(handles.tolerance2,'String'))))     
    set(handles.lvdt2,'BackgroundColor',[1 0 0]);
else
% Converged
    set(handles.lvdt2,'BackgroundColor',[1 1 1]);
    LVDT2conv = 1;             
end

% Check LVDT3 convergence
if (LVDT3 < target3-(str2double(get(handles.tolerance3,'String'))))
    set(handles.lvdt3,'BackgroundColor',[1 0 0]);
elseif (LVDT3 > target3+(str2double(get(handles.tolerance3,'String'))))     
    set(handles.lvdt3,'BackgroundColor',[1 0 0]);
else
% Converged
    set(handles.lvdt3,'BackgroundColor',[1 1 1]);
    LVDT3conv = 1;             
end

% Check LVDT4 convergence
if (LVDT4 < target4-(str2double(get(handles.tolerance4,'String'))))
    set(handles.lvdt4,'BackgroundColor',[1 0 0]);
elseif (LVDT4 > target4+(str2double(get(handles.tolerance4,'String'))))     
    set(handles.lvdt4,'BackgroundColor',[1 0 0]);
else
% Converged
    set(handles.lvdt4,'BackgroundColor',[1 1 1]);
    LVDT4conv = 1;             
end