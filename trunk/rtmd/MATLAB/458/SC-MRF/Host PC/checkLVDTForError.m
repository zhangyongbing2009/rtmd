%% Check LVDTs for ERRORs

% LVDT 1
if (abs(LVDT1-LVDT1prev) > (str2double(get(handles.error1,'String'))))
    disp(sprintf('WARNING: LVDT1 jumped by %f\nHit any key to continue...',LVDT1-LVDT1prev));
    pause;
end

% LVDT 2
if (abs(LVDT2-LVDT2prev) > (str2double(get(handles.error2,'String'))))
    disp(sprintf('WARNING: LVDT2 jumped by %f\nHit any key to continue...',LVDT2-LVDT2prev));
    pause;
end 

% LVDT 3
if (abs(LVDT3-LVDT3prev) > (str2double(get(handles.error3,'String'))))
    disp(sprintf('WARNING: LVDT3 jumped by %f\nHit any key to continue...',LVDT3-LVDT3prev));
    pause;
end

% LVDT 4
if (abs(LVDT4-LVDT4prev) > (str2double(get(handles.error4,'String'))))
    disp(sprintf('WARNING: LVDT4 jumped by %f\nHit any key to continue...',LVDT4-LVDT4prev));
    pause;
end