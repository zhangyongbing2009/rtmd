%Get the current commands to the actuators
cmd1 = str2double(get(handles.cmd1,'String'));
cmd2 = str2double(get(handles.cmd2,'String'));            
cmd3 = str2double(get(handles.cmd3,'String'));
cmd4 = str2double(get(handles.cmd4,'String'));       

% Save as previous commands too
prevcmd1 = cmd1;
prevcmd2 = cmd2;
prevcmd3 = cmd3;
prevcmd4 = cmd4;