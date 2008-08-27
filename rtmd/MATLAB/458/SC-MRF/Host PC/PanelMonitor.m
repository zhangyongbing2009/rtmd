function varargout = PanelMonitor(varargin)
% PANELMONITOR M-file for PanelMonitor.fig
%      PANELMONITOR, by itself, creates a new PANELMONITOR or raises the existing
%      singleton*.
%
%      H = PANELMONITOR returns the handle to a new PANELMONITOR or the handle to
%      the existing singleton*.
%
%      PANELMONITOR('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in PANELMONITOR.M with the given input arguments.
%
%      PANELMONITOR('Property','Value',...) creates a new PANELMONITOR or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before PanelMonitor_OpeningFunction gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to PanelMonitor_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Edit the above text to modify the response to help PanelMonitor

% Last Modified by GUIDE v2.5 29-Apr-2008 15:46:28

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @PanelMonitor_OpeningFcn, ...
                   'gui_OutputFcn',  @PanelMonitor_OutputFcn, ...
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


% --- Executes just before PanelMonitor is made visible.
function PanelMonitor_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to PanelMonitor (see VARARGIN)

% Choose default command line output for PanelMonitor
handles.output = hObject;

% Get the arguments from the setup file
handles.Mon = varargin{:};

% Update handles structure
guidata(hObject, handles);

% UIWAIT makes PanelMonitor wait for user response (see UIRESUME)
% uiwait(handles.figure1);


% --- Outputs from this function are returned to the command line.
function varargout = PanelMonitor_OutputFcn(hObject, eventdata, handles) 
% varargout  cell array for returning output args (see VARARGOUT);
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get default command line output from handles structure
varargout{1} = handles.output;



% --- PanelMonitor function 
function monitor(hObject, handles)
    % Get the monitor variable
    Mon = handles.Mon;
    
    % In the loop, get the feedback from the network
    while (true)
        % Send an 'offset' command to get the feedback
        commandstr = 'offset';
        s = java.lang.String(commandstr);
        byteBuffer = s.getBytes;
        clear s;
        Mon.out.write(byteBuffer);
        while (available(Mon.in) == 0)
            pause(0.01);
        end        
        bytesAvailable = available(Mon.in);
        repmat(java.lang.String(''),bytesAvailable,1);
        for k = 1:bytesAvailable
            result(k) = read(Mon.reader);
        end
        fromserver = char(result)';    
        clear result;  
        % Split the feedback string
        splitstring = textscan(fromserver,'%f%c%f%c%f%c%f%c%f%c%f%c%f%c%f');
        feedback(1) = splitstring{1}; % Load 1
        feedback(2) = splitstring{3}; % Load 2
        feedback(3) = splitstring{5}; % Load 3
        feedback(4) = splitstring{7}; % Load 4
        feedback(5) = splitstring{9}; % Disp 1
        feedback(6) = splitstring{11}; % Disp 2
        feedback(7) = splitstring{13}; % Disp 3
        feedback(8) = splitstring{15}; % Disp 4
        clear response;
        clear fromserver;

        % Get feedback and write to SCRAMNet for control program
        Mon.scr.writeFloat(Mon.disp1_in_scr,feedback(5));  % Disp 1 in inches
        Mon.scr.writeFloat(Mon.load1_k_scr,feedback(1));   % Load 1 in kips
        Mon.scr.writeFloat(Mon.disp2_in_scr,feedback(6));  % Disp 2 in inches
        Mon.scr.writeFloat(Mon.load2_k_scr,feedback(2));   % Load 2 in kips
        Mon.scr.writeFloat(Mon.disp3_in_scr,feedback(7));  % Disp 3 in inches
        Mon.scr.writeFloat(Mon.load3_k_scr,feedback(3));   % Load 3 in kips        
        Mon.scr.writeFloat(Mon.disp4_in_scr,feedback(8));  % Disp 4 in inches
        Mon.scr.writeFloat(Mon.load4_k_scr,feedback(4));   % Load 4 in kips                
        
        % Update GUI 
        set(handles.a1cmd,'String',num2str(Mon.scr.readFloat(Mon.disp1_cmd_in_scr)));  
        set(handles.a1acmd,'String',num2str(Mon.scr.readFloat(Mon.disp1_acmd_in_scr)));  
        set(handles.a1adisp,'String',num2str(Mon.scr.readFloat(Mon.disp1_in_scr)));  
        set(handles.a1disp,'String',num2str(Mon.scr.readFloat(Mon.disp1_in_off_scr)));  
        set(handles.a1load,'String',num2str(Mon.scr.readFloat(Mon.load1_k_scr)));  
        
        set(handles.a2cmd,'String',num2str(Mon.scr.readFloat(Mon.disp2_cmd_in_scr)));  
        set(handles.a2acmd,'String',num2str(Mon.scr.readFloat(Mon.disp2_acmd_in_scr)));  
        set(handles.a2adisp,'String',num2str(Mon.scr.readFloat(Mon.disp2_in_scr)));  
        set(handles.a2disp,'String',num2str(Mon.scr.readFloat(Mon.disp2_in_off_scr)));  
        set(handles.a2load,'String',num2str(Mon.scr.readFloat(Mon.load2_k_scr)));  
        
        set(handles.a3cmd,'String',num2str(Mon.scr.readFloat(Mon.disp3_cmd_in_scr)));  
        set(handles.a3acmd,'String',num2str(Mon.scr.readFloat(Mon.disp3_acmd_in_scr)));  
        set(handles.a3adisp,'String',num2str(Mon.scr.readFloat(Mon.disp3_in_scr)));  
        set(handles.a3disp,'String',num2str(Mon.scr.readFloat(Mon.disp3_in_off_scr)));  
        set(handles.a3load,'String',num2str(Mon.scr.readFloat(Mon.load3_k_scr)));  
        
        set(handles.a4cmd,'String',num2str(Mon.scr.readFloat(Mon.disp4_cmd_in_scr)));  
        set(handles.a4acmd,'String',num2str(Mon.scr.readFloat(Mon.disp4_acmd_in_scr)));  
        set(handles.a4adisp,'String',num2str(Mon.scr.readFloat(Mon.disp4_in_scr)));  
        set(handles.a4disp,'String',num2str(Mon.scr.readFloat(Mon.disp4_in_off_scr)));  
        set(handles.a4load,'String',num2str(Mon.scr.readFloat(Mon.load4_k_scr)));  
                     
        guidata(hObject, handles);
        
        % Run Misc calculations here
        FPTcalcs;
        
        clear feedback;
    end
    

% --- Executes on button press in startbutton.
function startbutton_Callback(hObject, eventdata, handles)
% hObject    handle to startbutton (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Network parameters
import java.net.*;
import java.io.*;
% Create TCP/IP object. Specify server machine and port number. 
handles.Mon.socket = Socket(handles.Mon.ControllerIP,handles.Mon.ControllerPort);
handles.Mon.in = handles.Mon.socket.getInputStream;
handles.Mon.reader = InputStreamReader(handles.Mon.in);
handles.Mon.out = handles.Mon.socket.getOutputStream;

% Disable the Start Button
set(handles.startbutton,'Enable','off');

% Pass data to GUI
guidata(hObject, handles);

% Run the monitor
monitor(hObject, handles);




function a3disp_Callback(hObject, eventdata, handles)
% hObject    handle to a3disp (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
function a3disp_CreateFcn(hObject, eventdata, handles)
% hObject    handle to a3disp (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
function a3acmd_Callback(hObject, eventdata, handles)
% hObject    handle to a3acmd (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
function a3acmd_CreateFcn(hObject, eventdata, handles)
% hObject    handle to a3acmd (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
function a1acmd_Callback(hObject, eventdata, handles)
% hObject    handle to a1acmd (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
function a1acmd_CreateFcn(hObject, eventdata, handles)
% hObject    handle to a1acmd (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
function a2acmd_Callback(hObject, eventdata, handles)
% hObject    handle to a2acmd (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
function a2acmd_CreateFcn(hObject, eventdata, handles)
% hObject    handle to a2acmd (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
function stopbutton_ButtonDownFcn(hObject, eventdata, handles)
% hObject    handle to stopbutton (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
function a3cmd_Callback(hObject, eventdata, handles)
% hObject    handle to a3cmd (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
function a3cmd_CreateFcn(hObject, eventdata, handles)
% hObject    handle to a3cmd (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
function a3load_Callback(hObject, eventdata, handles)
% hObject    handle to a3load (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
function a3load_CreateFcn(hObject, eventdata, handles)
% hObject    handle to a3load (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
function a1load_Callback(hObject, eventdata, handles)
% hObject    handle to a1load (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
function a1load_CreateFcn(hObject, eventdata, handles)
% hObject    handle to a1load (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
function a1adisp_Callback(hObject, eventdata, handles)
% hObject    handle to a1adisp (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
function a1adisp_CreateFcn(hObject, eventdata, handles)
% hObject    handle to a1adisp (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
function a1cmd_Callback(hObject, eventdata, handles)
% hObject    handle to a1cmd (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
function a1cmd_CreateFcn(hObject, eventdata, handles)
% hObject    handle to a1cmd (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
function a2cmd_Callback(hObject, eventdata, handles)
% hObject    handle to a2cmd (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
function a2cmd_CreateFcn(hObject, eventdata, handles)
% hObject    handle to a2cmd (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
function a2disp_Callback(hObject, eventdata, handles)
% hObject    handle to a2disp (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
function a2disp_CreateFcn(hObject, eventdata, handles)
% hObject    handle to a2disp (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
function a2load_Callback(hObject, eventdata, handles)
% hObject    handle to a2load (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
function a2load_CreateFcn(hObject, eventdata, handles)
% hObject    handle to a2load (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
function stopbutton_Callback(hObject, eventdata, handles)
% hObject    handle to stopbutton (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
close PanelMonitor;





function a4cmd_Callback(hObject, eventdata, handles)
% hObject    handle to a4cmd (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of a4cmd as text
%        str2double(get(hObject,'String')) returns contents of a4cmd as a double


% --- Executes during object creation, after setting all properties.
function a4cmd_CreateFcn(hObject, eventdata, handles)
% hObject    handle to a4cmd (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function a4disp_Callback(hObject, eventdata, handles)
% hObject    handle to a4disp (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of a4disp as text
%        str2double(get(hObject,'String')) returns contents of a4disp as a double


% --- Executes during object creation, after setting all properties.
function a4disp_CreateFcn(hObject, eventdata, handles)
% hObject    handle to a4disp (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function a4load_Callback(hObject, eventdata, handles)
% hObject    handle to a4load (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of a4load as text
%        str2double(get(hObject,'String')) returns contents of a4load as a double


% --- Executes during object creation, after setting all properties.
function a4load_CreateFcn(hObject, eventdata, handles)
% hObject    handle to a4load (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function a4acmd_Callback(hObject, eventdata, handles)
% hObject    handle to a4acmd (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of a4acmd as text
%        str2double(get(hObject,'String')) returns contents of a4acmd as a double


% --- Executes during object creation, after setting all properties.
function a4acmd_CreateFcn(hObject, eventdata, handles)
% hObject    handle to a4acmd (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end





function trigger_Callback(hObject, eventdata, handles)
% hObject    handle to trigger (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of trigger as text
%        str2double(get(hObject,'String')) returns contents of trigger as a double


% --- Executes during object creation, after setting all properties.
function trigger_CreateFcn(hObject, eventdata, handles)
% hObject    handle to trigger (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function step_Callback(hObject, eventdata, handles)
% hObject    handle to step (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of step as text
%        str2double(get(hObject,'String')) returns contents of step as a double


% --- Executes during object creation, after setting all properties.
function step_CreateFcn(hObject, eventdata, handles)
% hObject    handle to step (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function a1disp_Callback(hObject, eventdata, handles)
% hObject    handle to a1disp (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of a1disp as text
%        str2double(get(hObject,'String')) returns contents of a1disp as a double


% --- Executes during object creation, after setting all properties.
function a1disp_CreateFcn(hObject, eventdata, handles)
% hObject    handle to a1disp (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end





function a2adisp_Callback(hObject, eventdata, handles)
% hObject    handle to a2adisp (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of a2adisp as text
%        str2double(get(hObject,'String')) returns contents of a2adisp as a double


% --- Executes during object creation, after setting all properties.
function a2adisp_CreateFcn(hObject, eventdata, handles)
% hObject    handle to a2adisp (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function a3adisp_Callback(hObject, eventdata, handles)
% hObject    handle to a3adisp (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of a3adisp as text
%        str2double(get(hObject,'String')) returns contents of a3adisp as a double


% --- Executes during object creation, after setting all properties.
function a3adisp_CreateFcn(hObject, eventdata, handles)
% hObject    handle to a3adisp (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function a4adisp_Callback(hObject, eventdata, handles)
% hObject    handle to a4adisp (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of a4adisp as text
%        str2double(get(hObject,'String')) returns contents of a4adisp as a double


% --- Executes during object creation, after setting all properties.
function a4adisp_CreateFcn(hObject, eventdata, handles)
% hObject    handle to a4adisp (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end


