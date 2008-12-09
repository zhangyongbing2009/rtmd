%%
function vars = YMOM02(vars)
%%
facc=-1.0;
for iend = 1 : 2
    % Change sign of axial force and end 1
    fft = vars.ftot(iend,1) * facc;
    facc = 1;
    fac = 1;
    for j = 1 : 2
        fac = -fac;
        yieldcalc = 0;  % flag if the yield moment is calculated inside 
                        % this next for loop
        for i = 1 : 3
            if (fft < vars.pmx(i,j,iend))
                yieldcalc = 1;  % set flag since the condition was met
                % Calculate yield moment (M = a2 + a1P)
                bbmy = vars.a2(i,j,iend) - vars.a1(i,j,iend) * fft;
                if (fac * bbmy > 0) 
                    % Axial force is beyond Pc and Pt
                    bbmy = 0;
                end
                vars.bmy(iend,j) = bbmy;
            end
        end
        % Only calculate yield moment if it wasn't calculated in the above
        % for loop
        if (yieldcalc == 0)
            i = 4;
            % Calculate yield moment (M = a2 + a1P)
            bbmy = vars.a2(i,j,iend) - vars.a1(i,j,iend) * fft;
            if (fac * bbmy > 0) 
                % Axial force is beyond Pc and Pt
                bbmy = 0;
            end
            vars.bmy(iend,j) = bbmy;
        end
    end
end
%c ---------------------------------INTERCHANGE BMY(1,1) AND BMY(1,2) AND
%c ------------------------------AND ALSO CHANGE SIGN OF MOMENTS AT END 1
      fac=-vars.bmy(1,1);
      vars.bmy(1,1)=-vars.bmy(1,2);
      vars.bmy(1,2)=fac;
%%