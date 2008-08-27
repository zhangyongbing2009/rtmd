%Updates the step
stepcount = str2double(get(handles.stepcount,'String'));
handles.Vars.scr.writeFloat(handles.Vars.step_scr,stepcount);
set(handles.stepcount,'String',num2str(stepcount+1));

%Get the Nikon Image
nikonTransfer;