% Decide if the control method is based on the actuator displacement or
% LVDT displacement

% LVDT 1
if ((get(handles.lvdtcontrol1,'Value')) == 1.0)
    LVDT1 = handles.Vars.scr.readDAQ(handles.Vars.daqchannelinfo.offset(handles.Vars.LVDT1loc),handles.Vars.daqchannelinfo.gain(handles.Vars.LVDT1loc),handles.Vars.daqchannelinfo.Voffset(handles.Vars.LVDT1loc),handles.Vars.daqchannelinfo.Vslope(handles.Vars.LVDT1loc),handles.Vars.daqchannelinfo.EUoffset(handles.Vars.LVDT1loc),handles.Vars.daqchannelinfo.EUslope(handles.Vars.LVDT1loc))-handles.Vars.ch1lvdtoffset;
else
    LVDT1 = handles.Vars.scr.readFloat(handles.Vars.disp1_in_scr)-handles.Vars.ch1offset;
end

% LVDT 2
if ((get(handles.lvdtcontrol2,'Value')) == 1.0)        
    LVDT2 = handles.Vars.scr.readDAQ(handles.Vars.daqchannelinfo.offset(handles.Vars.LVDT2loc),handles.Vars.daqchannelinfo.gain(handles.Vars.LVDT2loc),handles.Vars.daqchannelinfo.Voffset(handles.Vars.LVDT2loc),handles.Vars.daqchannelinfo.Vslope(handles.Vars.LVDT2loc),handles.Vars.daqchannelinfo.EUoffset(handles.Vars.LVDT2loc),handles.Vars.daqchannelinfo.EUslope(handles.Vars.LVDT2loc))-handles.Vars.ch2lvdtoffset;
else
    LVDT2 = handles.Vars.scr.readFloat(handles.Vars.disp2_in_scr)-handles.Vars.ch2offset;
end

% LVDT 3
if ((get(handles.lvdtcontrol3,'Value')) == 1.0)
    LVDT3 = handles.Vars.scr.readDAQ(handles.Vars.daqchannelinfo.offset(handles.Vars.LVDT3loc),handles.Vars.daqchannelinfo.gain(handles.Vars.LVDT3loc),handles.Vars.daqchannelinfo.Voffset(handles.Vars.LVDT3loc),handles.Vars.daqchannelinfo.Vslope(handles.Vars.LVDT3loc),handles.Vars.daqchannelinfo.EUoffset(handles.Vars.LVDT3loc),handles.Vars.daqchannelinfo.EUslope(handles.Vars.LVDT3loc))-handles.Vars.ch3lvdtoffset;
else
    LVDT3 = handles.Vars.scr.readFloat(handles.Vars.disp3_in_scr)-handles.Vars.ch3offset;
end

% LVDT 4
if ((get(handles.lvdtcontrol4,'Value')) == 1.0)
    LVDT4 = handles.Vars.scr.readDAQ(handles.Vars.daqchannelinfo.offset(handles.Vars.LVDT4loc),handles.Vars.daqchannelinfo.gain(handles.Vars.LVDT4loc),handles.Vars.daqchannelinfo.Voffset(handles.Vars.LVDT4loc),handles.Vars.daqchannelinfo.Vslope(handles.Vars.LVDT4loc),handles.Vars.daqchannelinfo.EUoffset(handles.Vars.LVDT4loc),handles.Vars.daqchannelinfo.EUslope(handles.Vars.LVDT4loc))-handles.Vars.ch4lvdtoffset;
else
    LVDT4 = handles.Vars.scr.readFloat(handles.Vars.disp4_in_scr)-handles.Vars.ch4offset;
end

% Show LVDT data on screen
set(handles.lvdt1,'String',num2str(LVDT1));
set(handles.lvdt2,'String',num2str(LVDT2));
set(handles.lvdt3,'String',num2str(LVDT3));
set(handles.lvdt4,'String',num2str(LVDT4));