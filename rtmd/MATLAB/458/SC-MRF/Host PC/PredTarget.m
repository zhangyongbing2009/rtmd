function varargout = PredTarget(varargin)
% PREDTARGET M-file for PredTarget.fig
%      PREDTARGET, by itself, creates a new PREDTARGET or raises the existing
%      singleton*.
%
%      H = PREDTARGET returns the handle to a new PREDTARGET or the handle to
%      the existing singleton*.
%
%      PREDTARGET('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in PREDTARGET.M with the given input arguments.
%
%      PREDTARGET('Property','Value',...) creates a new PREDTARGET or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before PredTarget_OpeningFunction gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to PredTarget_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Edit the above text to modify the response to help PredTarget

% Last Modified by GUIDE v2.5 02-Jul-2008 09:06:08

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @PredTarget_OpeningFcn, ...
                   'gui_OutputFcn',  @PredTarget_OutputFcn, ...
                   'gui_LayoutFcn',  [] , ...
                   'gui_Callback',   []);
if nargin && ischar(varargin{1})
    gui_State.gui_Callback = str2func(varargin{1});
end

if nargout
    [varargout{1:nargout}] = gui_mainfcn(gui_State, varargin{:});
else
    gui_mainfcn(gui_State, varargin{:});
end
% End initialization code - DO NOT EDIT


% --- Executes just before PredTarget is made visible.
function PredTarget_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to PredTarget (see VARARGIN)

% Choose default command line output for PredTarget
handles.output = hObject;

% Get the arguments from the setup file
handles.Vars = varargin{:};

% Update handles structure
guidata(hObject, handles);

% UIWAIT makes PredTarget wait for user response (see UIRESUME)
% uiwait(handles.figure1);

% Do some initialization
set(handles.tolerance1,'String',num2str(handles.Vars.LVDTnoise));
set(handles.tolerance2,'String',num2str(handles.Vars.LVDTnoise));
set(handles.tolerance3,'String',num2str(handles.Vars.LVDTnoise));
set(handles.tolerance4,'String',num2str(handles.Vars.LVDTnoise));
set(handles.error1,'String',num2str(handles.Vars.LVDTerror));
set(handles.error2,'String',num2str(handles.Vars.LVDTerror));
set(handles.error3,'String',num2str(handles.Vars.LVDTerror));
set(handles.error4,'String',num2str(handles.Vars.LVDTerror));
set(handles.increment1,'String',num2str(0.0));
set(handles.increment2,'String',num2str(0.0));
set(handles.increment3,'String',num2str(0.0));
set(handles.increment4,'String',num2str(0.0));
set(handles.triggercount,'String',num2str(1.0));
set(handles.stepcount,'String',num2str(1.0));

% Hide direction indicators
set(handles.a1N,'Visible','off');
set(handles.a1S,'Visible','off');
set(handles.a2N,'Visible','off');
set(handles.a2S,'Visible','off');
set(handles.a3N,'Visible','off');
set(handles.a3S,'Visible','off');
set(handles.a4N,'Visible','off');
set(handles.a4S,'Visible','off');

% History file handler
handles.Vars.historyfile = [];
handles.Vars.historyfilecounter = 1;

% There is a rogue panel to hide
set(handles.uipanel5,'Visible','off');

% Update handles structure
guidata(hObject, handles);

% --- Outputs from this function are returned to the command line.
function varargout = PredTarget_OutputFcn(hObject, eventdata, handles) 
% varargout  cell array for returning output args (see VARARGOUT);
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get default command line output from handles structure
varargout{1} = handles.output;



function target1_Callback(hObject, eventdata, handles)
% hObject    handle to target1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of target1 as text
%        str2double(get(hObject,'String')) returns contents of target1 as a double


% --- Executes during object creation, after setting all properties.
function target1_CreateFcn(hObject, eventdata, handles)
% hObject    handle to target1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end


% --- Executes on button press in gobutton.
function gobutton_Callback(hObject, eventdata, handles)
% hObject    handle to gobutton (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

disablecontrols(handles);

% Get selected control option
convergeradiobutton = get(handles.convergeradiobutton,'Value');
converge80radiobutton = get(handles.converge80radiobutton,'Value');
manualradiobutton = get(handles.manualradiobutton,'Value');

% Incrementing Convergence Step
if (convergeradiobutton)     
  nonstop = 1;
  while (nonstop) 
    disp('Running Slow Convergence Step');
    
    % Get commands from file which updates the LVDT target objects
    getCommandsFromFile
        
    % Get target values from input boxes
    target1 = str2double(get(handles.target1,'String'));
    target2 = str2double(get(handles.target2,'String'));
    target3 = str2double(get(handles.target3,'String'));
    target4 = str2double(get(handles.target4,'String'));
    
    % Flags to notify when converged to LVDTs
    LVDT1conv = 0;
    LVDT2conv = 0;    
    LVDT3conv = 0;
    LVDT4conv = 0;    
    
    % Read in LVDT data
    getLVDTdata
    
    %Check LVDTs for convergence
    checkConverged
    
    % While the convergence hasn't been achieve, keep imposing increments
    while(LVDT1conv == 0 || LVDT2conv == 0 || LVDT3conv == 0 || LVDT4conv == 0)                
        % Pause if Sim Bit disabled
        while (handles.Vars.scr.readFloat(handles.Vars.SIM_BIT) == 0)
            pause(0.01);
        end                
        
        % Get the current commands to the actuators
        getCurrentCommands
        
        % Assume not converged
        %LVDT1conv = 0;
        %LVDT2conv = 0;
        %LVDT3conv = 0;
        %LVDT4conv = 0; 
    
        % Get previous LVDT data
        LVDT1prev = LVDT1;
        LVDT2prev = LVDT2; 
        LVDT3prev = LVDT3;
        LVDT4prev = LVDT4;
        
        % Get current LVDT data
        getLVDTdata
                       
        % Check for LVDT errors
        checkLVDTForError
        
        % Skip this step if Pause Bit enabled
        if (handles.Vars.scr.readFloat(handles.Vars.PAUSE_BIT) == 1)
            disp('Skipping step');
            set(handles.nonstopcheckbox,'Value',0);
            break;
        end
        
        %Check LVDTs for convergence and adjust the commands
        checkConvergedAndAdjust
        
        %Show direction of actuators
        getDirection
        
        % Set the commands based on convergence checks and add to current
        % command value.  The 'cmdx' variables were set in the
        % checkConverged code.
        gencmd1 = cmd1 + handles.Vars.ch1offset;
        gencmd2 = cmd2 + handles.Vars.ch2offset;  
        gencmd3 = cmd3 + handles.Vars.ch3offset;
        gencmd4 = cmd4 + handles.Vars.ch4offset;  
        updateSCRAMNetCommands;
        
        % Show new commands on screen and create the command string to send
        % to controller
        set(handles.cmd1,'String',num2str(cmd1));
        set(handles.cmd2,'String',num2str(cmd2)); 
        set(handles.cmd3,'String',num2str(cmd3));
        set(handles.cmd4,'String',num2str(cmd4)); 
        commandstr = ['execute,' num2str(gencmd1) ',' num2str(gencmd2) ',' num2str(gencmd3) ',' num2str(gencmd4)];        
        
        % Execute motion
        execute(handles,commandstr);        

        % Write data to CSV file
        writedatatofile(handles,target1,target2,target3,target4,cmd1,cmd2,cmd3,cmd4,gencmd1,gencmd2,gencmd3,gencmd4);              
        
        %Check LVDTs for convergence again
        checkConverged
    end        
    
    % Update step    
    updateStep
    
    % See if the nonstop checkbox was checked
    if (get(handles.nonstopcheckbox,'Value') == 1)
      nonstop = 1;
    else
      nonstop = 0;
    end
  end
      
% 80% Convergence Step
elseif (converge80radiobutton)     
  nonstop = 1;
  while (nonstop) 
    disp('Running % Convergence Step');
    
    % Get commands from file which updates the LVDT target objects
    getCommandsFromFile
        
    % Get target values from input boxes
    target1 = str2double(get(handles.target1,'String'));
    target2 = str2double(get(handles.target2,'String'));
    target3 = str2double(get(handles.target3,'String'));
    target4 = str2double(get(handles.target4,'String'));
    
    % Flags to notify when converged to LVDTs
    LVDT1conv = 0;
    LVDT2conv = 0;    
    LVDT3conv = 0;
    LVDT4conv = 0;    
    
    % Read in LVDT data
    getLVDTdata
    
    %Check LVDTs for convergence
    checkConverged
    
    % While the convergence hasn't been achieve, keep imposing increments
    while(LVDT1conv == 0 || LVDT2conv == 0 || LVDT3conv == 0 || LVDT4conv == 0)                
        % Pause if Sim Bit disabled
        while (handles.Vars.scr.readFloat(handles.Vars.SIM_BIT) == 0)
            pause(0.01);
        end                            
        
        % Get the current commands to the actuators
        getCurrentCommands
        
        % Assume not converged
        %LVDT1conv = 0;
        %LVDT2conv = 0;
        %LVDT3conv = 0;
        %LVDT4conv = 0; 
    
        % Get previous LVDT data
        LVDT1prev = LVDT1;
        LVDT2prev = LVDT2; 
        LVDT3prev = LVDT3;
        LVDT4prev = LVDT4;
        
        % Get current LVDT data
        getLVDTdata
                       
        % Check for LVDT errors
        checkLVDTForError
        
        % Skip this step if Pause Bit enabled
        if (handles.Vars.scr.readFloat(handles.Vars.PAUSE_BIT) == 1)
            disp('Skipping step');
            set(handles.nonstopcheckbox,'Value',0);
            break;
        end   
        
        % Calculate increment by taking 80% of the absolute value of target-current
        percentage = str2double(get(handles.convergencepercentage,'String'))/100;
        set(handles.increment1,'String',num2str(percentage*abs(target1 - LVDT1)));
        set(handles.increment2,'String',num2str(percentage*abs(target2 - LVDT2)));
        set(handles.increment3,'String',num2str(percentage*abs(target3 - LVDT3)));
        set(handles.increment4,'String',num2str(percentage*abs(target4 - LVDT4)));
        
        %Check LVDTs for convergence and adjust the commands
        checkConvergedAndAdjust
    
        %Show direction of actuators
        getDirection
        
        % Set the commands based on convergence checks and add to current
        % command value.  The 'cmdx' variables were set in the
        % checkConverged code.
        gencmd1 = cmd1 + handles.Vars.ch1offset;
        gencmd2 = cmd2 + handles.Vars.ch2offset;  
        gencmd3 = cmd3 + handles.Vars.ch3offset;
        gencmd4 = cmd4 + handles.Vars.ch4offset;  
        updateSCRAMNetCommands;
        
        % Show new commands on screen and create the command string to send
        % to controller
        set(handles.cmd1,'String',num2str(cmd1));
        set(handles.cmd2,'String',num2str(cmd2)); 
        set(handles.cmd3,'String',num2str(cmd3));
        set(handles.cmd4,'String',num2str(cmd4)); 
        commandstr = ['execute,' num2str(gencmd1) ',' num2str(gencmd2) ',' num2str(gencmd3) ',' num2str(gencmd4)];        
        
        % Execute motion
        execute(handles,commandstr);        

        % Write data to CSV file
        writedatatofile(handles,target1,target2,target3,target4,cmd1,cmd2,cmd3,cmd4,gencmd1,gencmd2,gencmd3,gencmd4);              
        
        %Check LVDTs for convergence again
        checkConverged
    end
    
    % Update step    
    updateStep     
    
    % See if the nonstop checkbox was checked
    if (get(handles.nonstopcheckbox,'Value') == 1)
      nonstop = 1;
    else
      nonstop = 0;
    end
  end
    
% Manual step    
elseif (manualradiobutton)    
    disp('Running Single Increment Step');   
    
    % Get LVDT data          
    getLVDTdata        
    
    % Get the current commands to the actuators
    getCurrentCommands

    % Add increment value for actuators and set command values
    cmd1 = cmd1 + str2double(get(handles.increment1,'String'));
    cmd2 = cmd2 + str2double(get(handles.increment2,'String'));                
    cmd3 = cmd3 + str2double(get(handles.increment3,'String'));
    cmd4 = cmd4 + str2double(get(handles.increment4,'String'));                
    gencmd1 = cmd1 + handles.Vars.ch1offset;        
    gencmd2 = cmd2 + handles.Vars.ch2offset;
    gencmd3 = cmd3 + handles.Vars.ch3offset;        
    gencmd4 = cmd4 + handles.Vars.ch4offset;
    updateSCRAMNetCommands;
    
    %Show direction of actuators
    getDirection

    % Show new commands on screen and create the command string to send
    % to controller
    set(handles.cmd1,'String',num2str(cmd1));
    set(handles.cmd2,'String',num2str(cmd2));        
    set(handles.cmd3,'String',num2str(cmd3));
    set(handles.cmd4,'String',num2str(cmd4));        
    commandstr = ['execute,' num2str(gencmd1) ',' num2str(gencmd2) ',' num2str(gencmd3) ',' num2str(gencmd4)];        

    % Execute motion
    execute(handles,commandstr); 
    
    % Write data to CSV file
    writedatatofile(handles,0,0,0,0,cmd1,cmd2,cmd3,cmd4,gencmd1,gencmd2,gencmd3,gencmd4);
    
    % Update step    
    updateStep 
    
else    
    disp('Unknown option, skipping');
end

% Clear pause bit 
handles.Vars.scr.writeFloat(handles.Vars.PAUSE_BIT,0);

% Re enable the controls
enablecontrols(handles);

function tolerance1_Callback(hObject, eventdata, handles)
% hObject    handle to tolerance1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of tolerance1 as text
%        str2double(get(hObject,'String')) returns contents of tolerance1 as a double


% --- Executes during object creation, after setting all properties.
function tolerance1_CreateFcn(hObject, eventdata, handles)
% hObject    handle to tolerance1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function error1_Callback(hObject, eventdata, handles)
% hObject    handle to error1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of error1 as text
%        str2double(get(hObject,'String')) returns contents of error1 as a double


% --- Executes during object creation, after setting all properties.
function error1_CreateFcn(hObject, eventdata, handles)
% hObject    handle to error1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function increment1_Callback(hObject, eventdata, handles)
% hObject    handle to increment1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of increment1 as text
%        str2double(get(hObject,'String')) returns contents of increment1 as a double


% --- Executes during object creation, after setting all properties.
function increment1_CreateFcn(hObject, eventdata, handles)
% hObject    handle to increment1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function loadtolerance1_Callback(hObject, eventdata, handles)
% hObject    handle to loadtolerance1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of loadtolerance1 as text
%        str2double(get(hObject,'String')) returns contents of loadtolerance1 as a double


% --- Executes during object creation, after setting all properties.
function loadtolerance1_CreateFcn(hObject, eventdata, handles)
% hObject    handle to loadtolerance1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function target2_Callback(hObject, eventdata, handles)
% hObject    handle to target2 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of target2 as text
%        str2double(get(hObject,'String')) returns contents of target2 as a double


% --- Executes during object creation, after setting all properties.
function target2_CreateFcn(hObject, eventdata, handles)
% hObject    handle to target2 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function tolerance2_Callback(hObject, eventdata, handles)
% hObject    handle to tolerance2 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of tolerance2 as text
%        str2double(get(hObject,'String')) returns contents of tolerance2 as a double


% --- Executes during object creation, after setting all properties.
function tolerance2_CreateFcn(hObject, eventdata, handles)
% hObject    handle to tolerance2 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function error2_Callback(hObject, eventdata, handles)
% hObject    handle to error2 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of error2 as text
%        str2double(get(hObject,'String')) returns contents of error2 as a double


% --- Executes during object creation, after setting all properties.
function error2_CreateFcn(hObject, eventdata, handles)
% hObject    handle to error2 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function increment2_Callback(hObject, eventdata, handles)
% hObject    handle to increment2 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of increment2 as text
%        str2double(get(hObject,'String')) returns contents of increment2 as a double


% --- Executes during object creation, after setting all properties.
function increment2_CreateFcn(hObject, eventdata, handles)
% hObject    handle to increment2 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function loadtolerance2_Callback(hObject, eventdata, handles)
% hObject    handle to loadtolerance2 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of loadtolerance2 as text
%        str2double(get(hObject,'String')) returns contents of loadtolerance2 as a double


% --- Executes during object creation, after setting all properties.
function loadtolerance2_CreateFcn(hObject, eventdata, handles)
% hObject    handle to loadtolerance2 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function triggercount_Callback(hObject, eventdata, handles)
% hObject    handle to triggercount (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of triggercount as text
%        str2double(get(hObject,'String')) returns contents of triggercount as a double


% --- Executes during object creation, after setting all properties.
function triggercount_CreateFcn(hObject, eventdata, handles)
% hObject    handle to triggercount (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function stepcount_Callback(hObject, eventdata, handles)
% hObject    handle to stepcount (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of stepcount as text
%        str2double(get(hObject,'String')) returns contents of stepcount as a double


% --- Executes during object creation, after setting all properties.
function stepcount_CreateFcn(hObject, eventdata, handles)
% hObject    handle to stepcount (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end


% --- Executes on button press in convergeradiobutton.
function convergeradiobutton_Callback(hObject, eventdata, handles)
% hObject    handle to convergeradiobutton (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of convergeradiobutton
val = get(hObject,'Value');
if (val == 1)
    set(handles.manualradiobutton,'Value',0)
    set(handles.converge80radiobutton,'Value',0)
end


% --- Executes on button press in manualradiobutton.
function manualradiobutton_Callback(hObject, eventdata, handles)
% hObject    handle to manualradiobutton (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of manualradiobutton
val = get(hObject,'Value');
if (val == 1)
    set(handles.convergeradiobutton,'Value',0)
    set(handles.converge80radiobutton,'Value',0)
end

% --- Executes on button press in converge80radiobutton.
function converge80radiobutton_Callback(hObject, eventdata, handles)
% hObject    handle to converge80radiobutton (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of converge80radiobutton
val = get(hObject,'Value');
if (val == 1)
    set(handles.convergeradiobutton,'Value',0)
    set(handles.manualradiobutton,'Value',0)
end

% --- Executes on button press in stopbutton.
function stopbutton_Callback(hObject, eventdata, handles)
% hObject    handle to stopbutton (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Close Server
commandstr = 'close';
s = java.lang.String(commandstr);
byteBuffer = s.getBytes;
clear s;
handles.Vars.out.write(byteBuffer);
while (available(handles.Vars.in) == 0)
    pause(0.01);
end        
bytesAvailable = available(handles.Vars.in);
repmat(java.lang.String(''),bytesAvailable,1);
for k = 1:bytesAvailable
    result(k) = read(handles.Vars.reader);
end
fromserver = char(result)';    
disp(fromserver);

% End Simulation by writing 0.0 to SIM RUNNING 
handles.Vars.scr.writeFloat(handles.Vars.SIM_BIT, 0);
handles.Vars.scr.unmapScramnet;

close PredTarget;


%% Do the routine over the network
function execute(handles,commandstr)
    s = java.lang.String(commandstr);
    handles.Vars.byteBuffer = s.getBytes;
    clear s;
    handles.Vars.out.write(handles.Vars.byteBuffer);
    while (available(handles.Vars.in) == 0)
        pause(0.01);
    end        
    bytesAvailable = available(handles.Vars.in);
    repmat(java.lang.String(''),bytesAvailable,1);
    for k = 1:bytesAvailable
        read(handles.Vars.reader);
    end   
    
    % Execution pause
    pause(str2double(get(handles.executionwait,'String'))); 


% --- Executes during object creation, after setting all properties.
function cmd1_CreateFcn(hObject, eventdata, handles)
% hObject    handle to cmd1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called


% --- Executes during object creation, after setting all properties.
function cmd2_CreateFcn(hObject, eventdata, handles)
% hObject    handle to cmd2 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

function disablecontrols(handles)
    set(handles.gobutton,'Enable','off');
    set(handles.stopbutton,'Enable','off');
    set(handles.lvdtcontrol1,'Enable','off');
    set(handles.lvdtcontrol2,'Enable','off');    
    set(handles.lvdtcontrol3,'Enable','off');
    set(handles.lvdtcontrol4,'Enable','off');    
    set(handles.actuatorcontrol1,'Enable','off');
    set(handles.actuatorcontrol2,'Enable','off');
    set(handles.actuatorcontrol3,'Enable','off');
    set(handles.actuatorcontrol4,'Enable','off');
    set(handles.convergeradiobutton,'Enable','off');
    set(handles.converge80radiobutton,'Enable','off');
    set(handles.manualradiobutton,'Enable','off');
    set(handles.target1,'Enable','off');
    set(handles.target2,'Enable','off');
    set(handles.target3,'Enable','off');
    set(handles.target4,'Enable','off');
    set(handles.tolerance1,'Enable','off');
    set(handles.tolerance2,'Enable','off');
    set(handles.tolerance3,'Enable','off');
    set(handles.tolerance4,'Enable','off');
    set(handles.error1,'Enable','off');
    set(handles.error2,'Enable','off');
    set(handles.error3,'Enable','off');
    set(handles.error4,'Enable','off');
    set(handles.increment1,'Enable','off');
    set(handles.increment2,'Enable','off');
    set(handles.increment3,'Enable','off');
    set(handles.increment4,'Enable','off');
    set(handles.triggercount,'Enable','off');
    set(handles.stepcount,'Enable','off');
    set(handles.executionwait,'Enable','off');
    set(handles.convergencepercentage,'Enable','off');
    
function enablecontrols(handles)
    set(handles.gobutton,'Enable','on');
    set(handles.stopbutton,'Enable','on');
    set(handles.lvdtcontrol1,'Enable','on');
    set(handles.lvdtcontrol2,'Enable','on');    
    set(handles.lvdtcontrol3,'Enable','on');
    set(handles.lvdtcontrol4,'Enable','on');    
    set(handles.actuatorcontrol1,'Enable','on');
    set(handles.actuatorcontrol2,'Enable','on');
    set(handles.actuatorcontrol3,'Enable','on');
    set(handles.actuatorcontrol4,'Enable','on');
    set(handles.convergeradiobutton,'Enable','on');
    set(handles.converge80radiobutton,'Enable','on');
    set(handles.manualradiobutton,'Enable','on');
    set(handles.target1,'Enable','on');
    set(handles.target2,'Enable','on');
    set(handles.target3,'Enable','on');
    set(handles.target4,'Enable','on');
    set(handles.tolerance1,'Enable','on');
    set(handles.tolerance2,'Enable','on');
    set(handles.tolerance3,'Enable','on');
    set(handles.tolerance4,'Enable','on');
    set(handles.error1,'Enable','on');
    set(handles.error2,'Enable','on');
    set(handles.error3,'Enable','on');
    set(handles.error4,'Enable','on');
    set(handles.increment1,'Enable','on');
    set(handles.increment2,'Enable','on');
    set(handles.increment3,'Enable','on');
    set(handles.increment4,'Enable','on');
    set(handles.triggercount,'Enable','on');
    set(handles.stepcount,'Enable','on');
    set(handles.executionwait,'Enable','on');
    set(handles.convergencepercentage,'Enable','on');

function writedatatofile(handles,target1,target2,target3,target4,cmd1,cmd2,cmd3,cmd4,gencmd1,gencmd2,gencmd3,gencmd4)
    % Get displacements at the actuators 
    disp1 = handles.Vars.scr.readFloat(handles.Vars.disp1_in_scr) - handles.Vars.ch1offset;
    disp2 = handles.Vars.scr.readFloat(handles.Vars.disp2_in_scr) - handles.Vars.ch2offset;
    disp3 = handles.Vars.scr.readFloat(handles.Vars.disp3_in_scr) - handles.Vars.ch3offset;
    disp4 = handles.Vars.scr.readFloat(handles.Vars.disp4_in_scr) - handles.Vars.ch4offset;        
    
    % Update SCRAMNet values
    % Update commands
    handles.Vars.scr.writeFloat(handles.Vars.disp1_cmd_in_scr,cmd1);   % Command 1 in inches      
    handles.Vars.scr.writeFloat(handles.Vars.disp2_cmd_in_scr,cmd2);   % Command 2 in inches      
    handles.Vars.scr.writeFloat(handles.Vars.disp3_cmd_in_scr,cmd3);   % Command 3 in inches      
    handles.Vars.scr.writeFloat(handles.Vars.disp4_cmd_in_scr,cmd4);   % Command 4 in inches      
    handles.Vars.scr.writeFloat(handles.Vars.disp1_acmd_in_scr,gencmd1);   % Actuator Command 1 in inches      
    handles.Vars.scr.writeFloat(handles.Vars.disp2_acmd_in_scr,gencmd2);   % Actuator Command 2 in inches      
    handles.Vars.scr.writeFloat(handles.Vars.disp3_acmd_in_scr,gencmd3);   % Actuator Command 3 in inches      
    handles.Vars.scr.writeFloat(handles.Vars.disp4_acmd_in_scr,gencmd4);   % Actuator Command 4 in inches      

    % Displacement offsets
    handles.Vars.scr.writeFloat(handles.Vars.disp1_in_off_scr,disp1); % Offset Displacement 1 in inches
    handles.Vars.scr.writeFloat(handles.Vars.disp2_in_off_scr,disp2); % Offset Displacement 2 in inches
    handles.Vars.scr.writeFloat(handles.Vars.disp3_in_off_scr,disp3); % Offset Displacement 3 in inches                
    handles.Vars.scr.writeFloat(handles.Vars.disp4_in_off_scr,disp4); % Offset Displacement 4 in inches                
       
    % Update camera trigger    
    triggercount = str2double(get(handles.triggercount,'String'));
    handles.Vars.scr.writeInt(handles.Vars.CAMERA_TRIGGER,int32(triggercount));
    set(handles.triggercount,'String',num2str(triggercount+1));   

function axialload_Callback(hObject, eventdata, handles)
% hObject    handle to axialload (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of axialload as text
%        str2double(get(hObject,'String')) returns contents of axialload as a double


% --- Executes during object creation, after setting all properties.
function axialload_CreateFcn(hObject, eventdata, handles)
% hObject    handle to axialload (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end




% --- Executes on button press in lvdtcontrol1.
function lvdtcontrol1_Callback(hObject, eventdata, handles)
% hObject    handle to lvdtcontrol1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of lvdtcontrol1
val = get(hObject,'Value');
if (val == 1)
    set(handles.actuatorcontrol1,'Value',0)
end

% --- Executes on button press in actuatorcontrol1.
function actuatorcontrol1_Callback(hObject, eventdata, handles)
% hObject    handle to actuatorcontrol1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of actuatorcontrol1
val = get(hObject,'Value');
if (val == 1)
    set(handles.lvdtcontrol1,'Value',0)
end

% --- Executes on button press in lvdtcontrol2.
function lvdtcontrol2_Callback(hObject, eventdata, handles)
% hObject    handle to lvdtcontrol2 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of lvdtcontrol2
val = get(hObject,'Value');
if (val == 1)
    set(handles.actuatorcontrol2,'Value',0)
end

% --- Executes on button press in actuatorcontrol2.
function actuatorcontrol2_Callback(hObject, eventdata, handles)
% hObject    handle to actuatorcontrol2 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of actuatorcontrol2
val = get(hObject,'Value');
if (val == 1)
    set(handles.lvdtcontrol2,'Value',0)
end



% --- Executes on button press in updatebutton.
function updatebutton_Callback(hObject, eventdata, handles)
% hObject    handle to updatebutton (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get LVDT Data
getLVDTdata

% Get target values
target1 = str2double(get(handles.target1,'String'));
target2 = str2double(get(handles.target2,'String'));
target3 = str2double(get(handles.target3,'String'));
target4 = str2double(get(handles.target4,'String'));

% Check LVDT1 convergence to set display colors
if (LVDT1 < target1-(str2double(get(handles.tolerance1,'String'))))   
    set(handles.lvdt1,'BackgroundColor',[1 0 0]);
elseif (LVDT1 > target1+(str2double(get(handles.tolerance1,'String'))))     
    set(handles.lvdt1,'BackgroundColor',[1 0 0]);
else
% Converged
    set(handles.lvdt1,'BackgroundColor',[1 1 1]);
end
% Check LVDT2 convergence
if (LVDT2 < target2-(str2double(get(handles.tolerance2,'String'))))
    set(handles.lvdt2,'BackgroundColor',[1 0 0]);
elseif (LVDT2 > target2+(str2double(get(handles.tolerance2,'String'))))     
    set(handles.lvdt2,'BackgroundColor',[1 0 0]);
else
% Converged
    set(handles.lvdt2,'BackgroundColor',[1 1 1]);
end
% Check LVDT3 convergence
if (LVDT3 < target3-(str2double(get(handles.tolerance3,'String'))))
    set(handles.lvdt3,'BackgroundColor',[1 0 0]);
elseif (LVDT3 > target3+(str2double(get(handles.tolerance3,'String'))))     
    set(handles.lvdt3,'BackgroundColor',[1 0 0]);
else
% Converged
    set(handles.lvdt3,'BackgroundColor',[1 1 1]);
end
% Check LVDT4 convergence
if (LVDT4 < target4-(str2double(get(handles.tolerance4,'String'))))
    set(handles.lvdt4,'BackgroundColor',[1 0 0]);
elseif (LVDT4 > target4+(str2double(get(handles.tolerance4,'String'))))     
    set(handles.lvdt4,'BackgroundColor',[1 0 0]);
else
% Converged
    set(handles.lvdt4,'BackgroundColor',[1 1 1]);
end



function target3_Callback(hObject, eventdata, handles)
% hObject    handle to target3 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of target3 as text
%        str2double(get(hObject,'String')) returns contents of target3 as a double


% --- Executes during object creation, after setting all properties.
function target3_CreateFcn(hObject, eventdata, handles)
% hObject    handle to target3 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function tolerance3_Callback(hObject, eventdata, handles)
% hObject    handle to tolerance3 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of tolerance3 as text
%        str2double(get(hObject,'String')) returns contents of tolerance3 as a double


% --- Executes during object creation, after setting all properties.
function tolerance3_CreateFcn(hObject, eventdata, handles)
% hObject    handle to tolerance3 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function error3_Callback(hObject, eventdata, handles)
% hObject    handle to error3 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of error3 as text
%        str2double(get(hObject,'String')) returns contents of error3 as a double


% --- Executes during object creation, after setting all properties.
function error3_CreateFcn(hObject, eventdata, handles)
% hObject    handle to error3 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function increment3_Callback(hObject, eventdata, handles)
% hObject    handle to increment3 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of increment3 as text
%        str2double(get(hObject,'String')) returns contents of increment3 as a double


% --- Executes during object creation, after setting all properties.
function increment3_CreateFcn(hObject, eventdata, handles)
% hObject    handle to increment3 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function target4_Callback(hObject, eventdata, handles)
% hObject    handle to target4 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of target4 as text
%        str2double(get(hObject,'String')) returns contents of target4 as a double


% --- Executes during object creation, after setting all properties.
function target4_CreateFcn(hObject, eventdata, handles)
% hObject    handle to target4 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function tolerance4_Callback(hObject, eventdata, handles)
% hObject    handle to tolerance4 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of tolerance4 as text
%        str2double(get(hObject,'String')) returns contents of tolerance4 as a double


% --- Executes during object creation, after setting all properties.
function tolerance4_CreateFcn(hObject, eventdata, handles)
% hObject    handle to tolerance4 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function error4_Callback(hObject, eventdata, handles)
% hObject    handle to error4 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of error4 as text
%        str2double(get(hObject,'String')) returns contents of error4 as a double


% --- Executes during object creation, after setting all properties.
function error4_CreateFcn(hObject, eventdata, handles)
% hObject    handle to error4 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function increment4_Callback(hObject, eventdata, handles)
% hObject    handle to increment4 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of increment4 as text
%        str2double(get(hObject,'String')) returns contents of increment4 as a double


% --- Executes during object creation, after setting all properties.
function increment4_CreateFcn(hObject, eventdata, handles)
% hObject    handle to increment4 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end


% --- Executes on button press in lvdtcontrol3.
function lvdtcontrol3_Callback(hObject, eventdata, handles)
% hObject    handle to lvdtcontrol3 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of lvdtcontrol3
val = get(hObject,'Value');
if (val == 1)
    set(handles.actuatorcontrol3,'Value',0)
end

% --- Executes on button press in actuatorcontrol3.
function actuatorcontrol3_Callback(hObject, eventdata, handles)
% hObject    handle to actuatorcontrol3 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of actuatorcontrol3
val = get(hObject,'Value');
if (val == 1)
    set(handles.lvdtcontrol3,'Value',0)
end

% --- Executes on button press in lvdtcontrol4.
function lvdtcontrol4_Callback(hObject, eventdata, handles)
% hObject    handle to lvdtcontrol4 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of lvdtcontrol4
val = get(hObject,'Value');
if (val == 1)
    set(handles.actuatorcontrol4,'Value',0)
end

% --- Executes on button press in actuatorcontrol4.
function actuatorcontrol4_Callback(hObject, eventdata, handles)
% hObject    handle to actuatorcontrol4 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of actuatorcontrol4
val = get(hObject,'Value');
if (val == 1)
    set(handles.lvdtcontrol4,'Value',0)
end



% --- Executes on button press in loadhistorybutton.
function loadhistorybutton_Callback(hObject, eventdata, handles)
% hObject    handle to loadhistorybutton (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Open the history file and get the command data
[historyfile path] = uigetfile('*.csv');
fid = fopen([path historyfile]);
fdata = textscan(fid,'%f%f%f%f','Delimiter',',');
fclose(fid);
% Show how many commands are in the history
set(handles.commandsleft,'String',num2str(length(fdata{1})));
% Store the data
handles.Vars.historyfile = fdata;
% Allow the use history checkbox to be checked
set(handles.usehistorycheckbox,'Enable','on');
% Allow the nonstop checkbox to be checked
set(handles.nonstopcheckbox,'Enable','on');
% Reset counter
handles.Vars.historyfilecounter = 1;

% Update handles structure
guidata(hObject, handles);


function commandsleft_Callback(hObject, eventdata, handles)
% hObject    handle to commandsleft (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of commandsleft as text
%        str2double(get(hObject,'String')) returns contents of commandsleft as a double


% --- Executes during object creation, after setting all properties.
function commandsleft_CreateFcn(hObject, eventdata, handles)
% hObject    handle to commandsleft (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end




% --- Executes on button press in usehistorycheckbox.
function usehistorycheckbox_Callback(hObject, eventdata, handles)
% hObject    handle to usehistorycheckbox (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of usehistorycheckbox



function executionwait_Callback(hObject, eventdata, handles)
% hObject    handle to executionwait (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of executionwait as text
%        str2double(get(hObject,'String')) returns contents of executionwait as a double


% --- Executes during object creation, after setting all properties.
function executionwait_CreateFcn(hObject, eventdata, handles)
% hObject    handle to executionwait (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end





function convergencepercentage_Callback(hObject, eventdata, handles)
% hObject    handle to convergencepercentage (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of convergencepercentage as text
%        str2double(get(hObject,'String')) returns contents of convergencepercentage as a double
value = get(hObject,'String');
set(handles.converge80radiobutton,'String',[value '% Converge Step']);

% --- Executes during object creation, after setting all properties.
function convergencepercentage_CreateFcn(hObject, eventdata, handles)
% hObject    handle to convergencepercentage (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end




% --- Executes on button press in nonstopcheckbox.
function nonstopcheckbox_Callback(hObject, eventdata, handles)
% hObject    handle to nonstopcheckbox (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of nonstopcheckbox


