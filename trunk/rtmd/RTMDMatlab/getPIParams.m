clear;
clc;
close all;

%% Create a SCRAMNet Object
scr = edu.lehigh.nees.scramnet.ScramNetIO;
scr.initScramnet; 
scr.setTransMode(2);  % Must be in Byte read mode

%% Set base address based on PI660 parameters
baseaddr = 65536; % 0x10000 in byte mode (1 byte per address)
scrAddrBase = 192; % Decimal base for SCRAMNet mapping per each channel

%% Error Flag
isError = 0;

%% See if SCRAMNet Parameters are set
dec = scr.readByte(baseaddr);
if (dec ~= 80)     
    fprintf('SCRAMNet Parameters not enabled on PI660\n');
    isError = 1;    
end

%% Continue only if no error
if (~isError)
    %% Open an output DAQ XML file
    [fname,pname] = uiputfile('*.xml','Create DAQ XML file');
    if (fname ~= 0)
        filename = sprintf('%s%s',pname,fname);
    else
        fprintf('No output file set\n');
        break;
    end
    [fiddaq message] = fopen(filename,'w');
    %% Open an output xPC XML file
    [fname,pname] = uiputfile('*.xml','Create xPC XML file');
    if (fname ~= 0)
        filename = sprintf('%s%s',pname,fname);
    else
        fprintf('No output file set\n');
        break;
    end
    [fidxpc message] = fopen(filename,'w');
    %% Create XML header for DAQ XML
    fprintf(fiddaq,'<?xml version="1.0" encoding="UTF-8"?>\n');
    fprintf(fiddaq,'<channelSet>\n');
    %% Create XML header for xPC XML
    fprintf(fidxpc,'<?xml version="1.0" encoding="UTF-8"?>\n');
    fprintf(fidxpc,'<xPC>\n');        
    %% Read header information
    for byteIndex = baseaddr:baseaddr+87
        dec = scr.readByte(byteIndex);            
        ascii = char(dec);    
        %fprintf('Decimal: %d, Ascii: %s\n',dec,ascii);         
    end           
    %% Find available channels
    channelCount = 1;
    maxChannels = 768; % 3 racks * 16 cards per rack * 16 CVT    
    %% Go through every channels
    while (channelCount <= maxChannels)   
        %% Get next channel offset in SCRAMNet
        byteOffset = 88*channelCount;        
        fprintf('Rack: %d, Slot: %d, Channel: %d, Overall Channel: %d, Addr: 0x%s ', floor((channelCount-1)/256), mod(floor((channelCount-1)/16),16), mod((channelCount-1),16), channelCount, dec2hex(baseaddr + byteOffset));
        %% Read the first byte to see if it is empty or not
        dec = scr.readByte(baseaddr + byteOffset);                 
        if (dec == 0)
            fprintf('\n');
            inTest = 0;
        else
            fprintf('in test\n');
            inTest = 1;
        end        
        %% If the channel is in the test, scan the channel block
        if (inTest)
            %% Get the Name
            fprintf(' Name: ');
            name = '';
            for byteIndex = (baseaddr+byteOffset) : (baseaddr+byteOffset+31)
                dec = scr.readByte(byteIndex);               
                name = [name char(dec)];                
            end
            fprintf('%s\n',name);            
            %% Get the Units
            fprintf(' Units: ');
            units = '';
            for byteIndex = (baseaddr+byteOffset+32) : (baseaddr+byteOffset+39)
                dec = scr.readByte(byteIndex);
                units = [units char(dec)];                
            end
            fprintf('%s\n',units);            
            %% Get the Gain            
            fprintf(' Gain: ');
            byteIndex = (baseaddr+byteOffset+40);            
            % Swap the 4 bytes words to put in the proper double format
            dec1 = scr.readFloat(byteIndex/4);
            dec2 = scr.readFloat((byteIndex+4)/4);
            scr.writeFloat(200000,dec2);    % write to a temp memory location
            scr.writeFloat(200001,dec1);
            scr.setTransMode(0);  % Long word mode
            gain = scr.readDouble(100000);
            scr.setTransMode(2);  % Byte mode            
            fprintf('%f\n',gain);                               
            %% Get the VSlope            
            fprintf(' VSlope: ');
            byteIndex = (baseaddr+byteOffset+48);            
            % Swap the 4 bytes words to put in the proper double format
            dec1 = scr.readFloat(byteIndex/4);
            dec2 = scr.readFloat((byteIndex+4)/4);
            scr.writeFloat(200000,dec2);    % write to a temp memory location
            scr.writeFloat(200001,dec1);
            scr.setTransMode(0);  % Long word mode
            vslope = scr.readDouble(100000);
            scr.setTransMode(2);  % Byte mode
            fprintf('%f\n',vslope);                             
            %% Get the VOffset            
            fprintf(' VOffset: ');
            byteIndex = (baseaddr+byteOffset+56);            
            % Swap the 4 bytes words to put in the proper double format
            dec1 = scr.readFloat(byteIndex/4);
            dec2 = scr.readFloat((byteIndex+4)/4);
            scr.writeFloat(200000,dec2);    % write to a temp memory location
            scr.writeFloat(200001,dec1);
            scr.setTransMode(0);  % Long word mode
            voffset = scr.readDouble(100000);
            scr.setTransMode(2);  % Byte mode
            fprintf('%f\n',voffset);                              
            %% Get the EUSlope            
            fprintf(' EUSlope: ');
            byteIndex = (baseaddr+byteOffset+64);            
            % Swap the 4 bytes words to put in the proper double format
            dec1 = scr.readFloat(byteIndex/4);
            dec2 = scr.readFloat((byteIndex+4)/4);
            scr.writeFloat(200000,dec2);    % write to a temp memory location
            scr.writeFloat(200001,dec1);
            scr.setTransMode(0);  % Long word mode
            euslope = scr.readDouble(100000);
            scr.setTransMode(2);  % Byte mode
            fprintf('%f\n',euslope);                        
            %% Get the EUOffset            
            fprintf(' EUOffset: ');
            byteIndex = (baseaddr+byteOffset+72);            
            % Swap the 4 bytes words to put in the proper double format
            dec1 = scr.readFloat(byteIndex/4);
            dec2 = scr.readFloat((byteIndex+4)/4);
            scr.writeFloat(200000,dec2);    % write to a temp memory location
            scr.writeFloat(200001,dec1);
            scr.setTransMode(0);  % Long word mode
            euoffset = scr.readDouble(100000);
            scr.setTransMode(2);  % Byte mode
            fprintf('%f\n',euoffset);                        
            %% Get the Gage Type            
            fprintf(' Gage Type: ');
            byteIndex = (baseaddr+byteOffset+80);     
            scr.setTransMode(0);  % Long word mode
            dec = scr.readInt(byteIndex/4);   
            scr.setTransMode(2);  % Byte mode
            gagetype = getPIGageType(dec);
            fprintf('%s\n',gagetype);                           
            %% Get the SCRAMNet Address
            scraddr = scrAddrBase + channelCount-1;
            fprintf(' SCRAMNet Address: %d\n', scraddr);
            %% Write to XML DAQ file            
            fprintf(fiddaq,'<channel type="int" name="%s" unit="%s" Gain="%f" VoltageSlope="%f" VoltageOffset="%f" EUSlope="%f" EUOffset="%f" location="%d" GageType="%s"/>\n',name,units,gain,vslope,voffset,euslope,euoffset,scraddr,gagetype);            
            %% Write to XML xPC file            
            fprintf(fidxpc,'<xPCReadBlock name="%s" unit="%s" Gain="%f" VoltageSlope="%f" VoltageOffset="%f" EUSlope="%f" EUOffset="%f" isDAQ="true" location="%d"/>\n',name,units,gain,vslope,voffset,euslope,euoffset,scraddr);                        
        end
        %% Go to next channel
        channelCount = channelCount + 1;        
    end
end

%% Complete XML DAQ file and close
fprintf(fiddaq,'</channelSet>\n');
fclose(fiddaq);
%% Complete XML xPC file and close
fprintf(fidxpc,'</xPC>\n');
fclose(fidxpc);
%% Unmap SCRAMNet
scr.setTransMode(0);  % Back in word mode
scr.unmapScramnet;