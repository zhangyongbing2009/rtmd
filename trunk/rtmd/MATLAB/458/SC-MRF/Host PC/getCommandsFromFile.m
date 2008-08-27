%Get commands from file and update the LVDT target boxes

% If the use history checkbox is checked, get the next command
if (get(handles.usehistorycheckbox,'Value') == 1)        
    % Write the target values to the application
    set(handles.target1,'String',num2str(handles.Vars.historyfile{1}(handles.Vars.historyfilecounter)));
    set(handles.target2,'String',num2str(handles.Vars.historyfile{2}(handles.Vars.historyfilecounter)));
    set(handles.target3,'String',num2str(handles.Vars.historyfile{3}(handles.Vars.historyfilecounter)));
    set(handles.target4,'String',num2str(handles.Vars.historyfile{4}(handles.Vars.historyfilecounter)));
    % Increment the counter for next time
    handles.Vars.historyfilecounter = handles.Vars.historyfilecounter + 1;        
    set(handles.commandsleft,'String',num2str(str2double(get(handles.commandsleft,'String'))-1));
    % If this is the last set of commands, disable the use checkbox
    if (strcmp(get(handles.commandsleft,'String'),'0'))
        set(handles.usehistorycheckbox,'Value',0);
        set(handles.usehistorycheckbox,'Enable','off');
        set(handles.nonstopcheckbox,'Value',0);
        set(handles.nonstopcheckbox,'Enable','off');
    end

    % Update handles structure
    guidata(hObject, handles);
end