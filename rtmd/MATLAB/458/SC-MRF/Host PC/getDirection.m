%Show directi'on's of actuators

% Positive is North
if (cmd1 > prevcmd1)
    set(handles.a1N,'Visible','on');
    set(handles.a1S,'Visible','off');
elseif (cmd1 < prevcmd1)
    set(handles.a1N,'Visible','off');
    set(handles.a1S,'Visible','on');
else 
    set(handles.a1N,'Visible','off');
    set(handles.a1S,'Visible','off');
end
    
% Positive is North
if (cmd2 > prevcmd2)
    set(handles.a2N,'Visible','on');
    set(handles.a2S,'Visible','off');
elseif (cmd2 < prevcmd2)
    set(handles.a2N,'Visible','off');
    set(handles.a2S,'Visible','on');
else 
    set(handles.a2N,'Visible','off');
    set(handles.a2S,'Visible','off');
end
    
% Positive is North
if (cmd3 > prevcmd3)
    set(handles.a3N,'Visible','on');
    set(handles.a3S,'Visible','off');
elseif (cmd3 < prevcmd3)
    set(handles.a3N,'Visible','off');
    set(handles.a3S,'Visible','on');
else 
    set(handles.a3N,'Visible','off');
    set(handles.a3S,'Visible','off');
end
    

% Positive is North
if (cmd4 > prevcmd4)
    set(handles.a4N,'Visible','on');
    set(handles.a4S,'Visible','off');
elseif (cmd4 < prevcmd4)
    set(handles.a4N,'Visible','off');
    set(handles.a4S,'Visible','on');
else 
    set(handles.a4N,'Visible','off');
    set(handles.a4S,'Visible','off');
end
    