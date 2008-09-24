%Updates the step
stepcount = str2double(get(handles.stepcount,'String'));
handles.Vars.scr.writeFloat(handles.Vars.step_scr,stepcount);

%Trigger camera grabber
handles.Vars.scr.writeInt(handles.Vars.CAMERA_TRIGGER,int32(stepcount));

%Increment step
set(handles.stepcount,'String',num2str(stepcount+1));

%Get the Nikon Image
nikonTransfer;