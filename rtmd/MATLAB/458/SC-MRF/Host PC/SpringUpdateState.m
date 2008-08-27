% Update the state of the spring by passing in the variable structure
function vars = SpringUpdateState(vars, displacement)

dU = (displacement-vars.dUs);       %incremental displacement
accFac = 0.0;                       %accumulated factor to trace out nonlinear path
dF1 = 0.0;                          %incremental spring force

%% State determination loop
while (accFac < 0.999999999)
    factor = 1.0 - accFac;
    %current state is in elastic branch
	if (vars.nState == 1)
        %elastically loading
		if (dU > 0.0)
            fac = (vars.dFy1 - vars.dFs1 ) / (vars.dK1 * dU);
            if (fac < factor)
                factor = fac;
                vars.nState = 2;
            end
        %elastically unloading
        elseif (dU < 0.0)
            %in negative spring force
            fac = (-vars.dFs1) / (vars.dK1 * dU);
            %change in state
            if (fac < factor)    
                factor = fac;
                vars.nState = -1;
            end
        else            
        end        
        dF1 = vars.dK1 * dU;
    elseif (vars.nState == -1)
        %elastically loading
        if (dU < 0.0)
            fac = (-vars.dFy1 - vars.dFs1) / (vars.dK1 * dU);
            if (fac < factor)
                factor = fac;
                vars.nState = -2;
            end
            %elastically unloading
        elseif ( dU > 0.0)
            %in negative spring force
            fac = (-vars.dFs1) / (vars.dK1 * dU);
            %change in state
            if (fac < factor)
                factor = fac;
                vars.nState = 1;
            end
        else
        end
        dF1 = vars.dK1 * dU;
    %current state is in yielding branch 
    elseif (vars.nState == 2)        
        %elastically unloading along the line 3
        if (dU < 0.0)
            factor = 0.0;
            vars.nState = 3;
        %continue to yield 
        elseif (dU > 0.0)
            dF1 = 0.0;    
        else
        end
    elseif (vars.nState == -2)
        %elastically unloading along the line -3
        if (dU > 0.0)
            factor = 0.0;
            vars.nState = -3;
        %continue to yield 
        elseif (dU < 0.0)
            dF1 = 0.0;
        else
        end
    %current state is in elastic unloading/reloading branch
    elseif (vars.nState == 3)        
        %elastically reloading 
        if (dU > 0.0)
            fac = (vars.dFy1 - vars.dFs1) / (vars.dK1 * dU);
            if (fac < factor)
                factor = fac;
                vars.nState = 2;
            end
        elseif (dU < 0.0)
            fac = (vars.dFgc1 - vars.dFs1) / (vars.dK1 * dU);
            if(fac < factor)               
                factor = fac;
                vars.nState = 4;
            end
        else
        end
        dF1 = vars.dK1 * dU;
    elseif (vars.nState == -3)
        %elastically unloading 
        if (dU < 0.0)
            fac = (-vars.dFy1 - vars.dFs1) / (vars.dK1 * dU);
            if (fac < factor)
                factor = fac;
                vars.nState = -2;
            end
        elseif (dU > 0.0)
            fac = (-vars.dFgc1 - vars.dFs1) / (vars.dK1 * dU);
            if (fac < factor)
                factor = fac;
                vars.nState  = -4;
            end
        else
        end
        dF1 = vars.dK1 * dU;
    elseif (vars.nState == 4)
        vars.dKTan = vars.dK2;
        %elastically reloading 
        if (dU > 0.0)
            factor =0.0;
            vars.nState = 3;
        %continue to unloading 
        elseif (dU < 0.0)
            fac = (vars.dUc - vars.dUs) / dU;
            if (fac < factor)
                factor = fac;
                if (vars.dFgc <= 0.0)	
                    vars.nState = -1;
                elseif (vars.dFgc > 0.0)	
                    vars.nState = 1;
                else
                end
            end
            dF1 = 0.0;
        else
        end
    elseif (vars.nState == -4)
        %elastically reloading
        if (dU < 0.0)
            factor = 0.0;
            vars.nState = -3;
        %continue to unloading 
        elseif (dU > 0.0)
            fac = (-vars.dUc - vars.dUs) / dU;
            if (fac < factor)
                factor = fac;
                if (vars.dFgc <= 0.0)	
                    vars.nState = 1;
                elseif (vars.dFgc > 0.0)	
                    vars.nState = -1;
                else
                end
            end
            dF1 = 0.0;
        else
        end
    else
    end

    %update state
    vars.dUs = vars.dUs + factor * dU;
    vars.dFs1 = vars.dFs1 + factor * dF1;		
    accFac = accFac + factor;
end

%sum spring force for nonlinear and linear springs at current state 
vars.dFs = vars.dFs1 + vars.dK2 * vars.dUs;