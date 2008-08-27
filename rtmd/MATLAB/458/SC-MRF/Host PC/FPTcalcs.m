%% LVDT configuration for control
% Load cell locations
LCT1loc = 123;
LCT2loc = 124;
LCT3loc = 125;
LCT4loc = 126;
LCT5loc = 1;
LCT6loc = 2;
LCT7loc = 3;
LCT8loc = 4;
LCT9loc = 5;
LCT10loc = 6;
LCT11loc = 7;
LCT12loc = 8;
LCT13loc = 63;
LCT14loc = 64;
LCT15loc = 65;
LCT16loc = 66;
LCT17loc = 67;
LCT18loc = 68;
LCT19loc = 69;
LCT20loc = 70;
LCT21loc = 55;
LCT22loc = 56;
LCT23loc = 57;
LCT24loc = 58;
LCT25loc = 59;
LCT26loc = 60;
LCT27loc = 61;
LCT28loc = 62;
DTRN1loc = 27;
DTRN2loc = 28;
DTRN3loc = 29;
DTRNRloc = 30;

F1PT_scr = 2000;                % 1st Floor Post Tensioning
F2PT_scr = 2001;                % 2nd Floor Post Tensioning
F3PT_scr = 2002;                % 3rd Floor Post Tensioning
F4PT_scr = 2003;                % 4th Floor Post Tensioning
F1Drift_scr = 2004;             % 1st Floor Drift
F2Drift_scr = 2005;             % 2nd Floor Drift
F3Drift_scr = 2006;             % 3rd Floor Drift
F4Drift_scr = 2007;             % 4th Floor Drift
F1StoryShear_scr = 2008;        % 1st Floor Story Shear
F2StoryShear_scr = 2009;        % 2nd Floor Story Shear
F3StoryShear_scr = 2010;        % 3rd Floor Story Shear
F4StoryShear_scr = 2011;        % 4th Floor Story Shear

% Calculate PTs
F1PT =  Mon.scr.readDAQ(Mon.daqchannelinfo.offset(LCT21loc),Mon.daqchannelinfo.gain(LCT21loc),Mon.daqchannelinfo.Voffset(LCT21loc),Mon.daqchannelinfo.Vslope(LCT21loc),Mon.daqchannelinfo.EUoffset(LCT21loc),Mon.daqchannelinfo.EUslope(LCT21loc)) + ...
        Mon.scr.readDAQ(Mon.daqchannelinfo.offset(LCT22loc),Mon.daqchannelinfo.gain(LCT22loc),Mon.daqchannelinfo.Voffset(LCT22loc),Mon.daqchannelinfo.Vslope(LCT22loc),Mon.daqchannelinfo.EUoffset(LCT22loc),Mon.daqchannelinfo.EUslope(LCT22loc)) + ...
        Mon.scr.readDAQ(Mon.daqchannelinfo.offset(LCT23loc),Mon.daqchannelinfo.gain(LCT23loc),Mon.daqchannelinfo.Voffset(LCT23loc),Mon.daqchannelinfo.Vslope(LCT23loc),Mon.daqchannelinfo.EUoffset(LCT23loc),Mon.daqchannelinfo.EUslope(LCT23loc)) + ...
        Mon.scr.readDAQ(Mon.daqchannelinfo.offset(LCT24loc),Mon.daqchannelinfo.gain(LCT24loc),Mon.daqchannelinfo.Voffset(LCT24loc),Mon.daqchannelinfo.Vslope(LCT24loc),Mon.daqchannelinfo.EUoffset(LCT24loc),Mon.daqchannelinfo.EUslope(LCT24loc)) + ...
        Mon.scr.readDAQ(Mon.daqchannelinfo.offset(LCT25loc),Mon.daqchannelinfo.gain(LCT25loc),Mon.daqchannelinfo.Voffset(LCT25loc),Mon.daqchannelinfo.Vslope(LCT25loc),Mon.daqchannelinfo.EUoffset(LCT25loc),Mon.daqchannelinfo.EUslope(LCT25loc)) + ...
        Mon.scr.readDAQ(Mon.daqchannelinfo.offset(LCT26loc),Mon.daqchannelinfo.gain(LCT26loc),Mon.daqchannelinfo.Voffset(LCT26loc),Mon.daqchannelinfo.Vslope(LCT26loc),Mon.daqchannelinfo.EUoffset(LCT26loc),Mon.daqchannelinfo.EUslope(LCT26loc)) + ...
        Mon.scr.readDAQ(Mon.daqchannelinfo.offset(LCT27loc),Mon.daqchannelinfo.gain(LCT27loc),Mon.daqchannelinfo.Voffset(LCT27loc),Mon.daqchannelinfo.Vslope(LCT27loc),Mon.daqchannelinfo.EUoffset(LCT27loc),Mon.daqchannelinfo.EUslope(LCT27loc)) + ...
        Mon.scr.readDAQ(Mon.daqchannelinfo.offset(LCT28loc),Mon.daqchannelinfo.gain(LCT28loc),Mon.daqchannelinfo.Voffset(LCT28loc),Mon.daqchannelinfo.Vslope(LCT28loc),Mon.daqchannelinfo.EUoffset(LCT28loc),Mon.daqchannelinfo.EUslope(LCT28loc));
F2PT =  Mon.scr.readDAQ(Mon.daqchannelinfo.offset(LCT13loc),Mon.daqchannelinfo.gain(LCT13loc),Mon.daqchannelinfo.Voffset(LCT13loc),Mon.daqchannelinfo.Vslope(LCT13loc),Mon.daqchannelinfo.EUoffset(LCT13loc),Mon.daqchannelinfo.EUslope(LCT13loc)) + ...
        Mon.scr.readDAQ(Mon.daqchannelinfo.offset(LCT14loc),Mon.daqchannelinfo.gain(LCT14loc),Mon.daqchannelinfo.Voffset(LCT14loc),Mon.daqchannelinfo.Vslope(LCT14loc),Mon.daqchannelinfo.EUoffset(LCT14loc),Mon.daqchannelinfo.EUslope(LCT14loc)) + ...
        Mon.scr.readDAQ(Mon.daqchannelinfo.offset(LCT15loc),Mon.daqchannelinfo.gain(LCT15loc),Mon.daqchannelinfo.Voffset(LCT15loc),Mon.daqchannelinfo.Vslope(LCT15loc),Mon.daqchannelinfo.EUoffset(LCT15loc),Mon.daqchannelinfo.EUslope(LCT15loc)) + ...
        Mon.scr.readDAQ(Mon.daqchannelinfo.offset(LCT16loc),Mon.daqchannelinfo.gain(LCT16loc),Mon.daqchannelinfo.Voffset(LCT16loc),Mon.daqchannelinfo.Vslope(LCT16loc),Mon.daqchannelinfo.EUoffset(LCT16loc),Mon.daqchannelinfo.EUslope(LCT16loc)) + ...
        Mon.scr.readDAQ(Mon.daqchannelinfo.offset(LCT17loc),Mon.daqchannelinfo.gain(LCT17loc),Mon.daqchannelinfo.Voffset(LCT17loc),Mon.daqchannelinfo.Vslope(LCT17loc),Mon.daqchannelinfo.EUoffset(LCT17loc),Mon.daqchannelinfo.EUslope(LCT17loc)) + ...
        Mon.scr.readDAQ(Mon.daqchannelinfo.offset(LCT18loc),Mon.daqchannelinfo.gain(LCT18loc),Mon.daqchannelinfo.Voffset(LCT18loc),Mon.daqchannelinfo.Vslope(LCT18loc),Mon.daqchannelinfo.EUoffset(LCT18loc),Mon.daqchannelinfo.EUslope(LCT18loc)) + ...
        Mon.scr.readDAQ(Mon.daqchannelinfo.offset(LCT19loc),Mon.daqchannelinfo.gain(LCT19loc),Mon.daqchannelinfo.Voffset(LCT19loc),Mon.daqchannelinfo.Vslope(LCT19loc),Mon.daqchannelinfo.EUoffset(LCT19loc),Mon.daqchannelinfo.EUslope(LCT19loc)) + ...
        Mon.scr.readDAQ(Mon.daqchannelinfo.offset(LCT20loc),Mon.daqchannelinfo.gain(LCT20loc),Mon.daqchannelinfo.Voffset(LCT20loc),Mon.daqchannelinfo.Vslope(LCT20loc),Mon.daqchannelinfo.EUoffset(LCT20loc),Mon.daqchannelinfo.EUslope(LCT20loc));
F3PT =  Mon.scr.readDAQ(Mon.daqchannelinfo.offset(LCT5loc),Mon.daqchannelinfo.gain(LCT5loc),Mon.daqchannelinfo.Voffset(LCT5loc),Mon.daqchannelinfo.Vslope(LCT5loc),Mon.daqchannelinfo.EUoffset(LCT5loc),Mon.daqchannelinfo.EUslope(LCT5loc)) + ...
        Mon.scr.readDAQ(Mon.daqchannelinfo.offset(LCT6loc),Mon.daqchannelinfo.gain(LCT6loc),Mon.daqchannelinfo.Voffset(LCT6loc),Mon.daqchannelinfo.Vslope(LCT6loc),Mon.daqchannelinfo.EUoffset(LCT6loc),Mon.daqchannelinfo.EUslope(LCT6loc)) + ...
        Mon.scr.readDAQ(Mon.daqchannelinfo.offset(LCT7loc),Mon.daqchannelinfo.gain(LCT7loc),Mon.daqchannelinfo.Voffset(LCT7loc),Mon.daqchannelinfo.Vslope(LCT7loc),Mon.daqchannelinfo.EUoffset(LCT7loc),Mon.daqchannelinfo.EUslope(LCT7loc)) + ...
        Mon.scr.readDAQ(Mon.daqchannelinfo.offset(LCT8loc),Mon.daqchannelinfo.gain(LCT8loc),Mon.daqchannelinfo.Voffset(LCT8loc),Mon.daqchannelinfo.Vslope(LCT8loc),Mon.daqchannelinfo.EUoffset(LCT8loc),Mon.daqchannelinfo.EUslope(LCT8loc)) + ...
        Mon.scr.readDAQ(Mon.daqchannelinfo.offset(LCT9loc),Mon.daqchannelinfo.gain(LCT9loc),Mon.daqchannelinfo.Voffset(LCT9loc),Mon.daqchannelinfo.Vslope(LCT9loc),Mon.daqchannelinfo.EUoffset(LCT9loc),Mon.daqchannelinfo.EUslope(LCT9loc)) + ...
        Mon.scr.readDAQ(Mon.daqchannelinfo.offset(LCT10loc),Mon.daqchannelinfo.gain(LCT10loc),Mon.daqchannelinfo.Voffset(LCT10loc),Mon.daqchannelinfo.Vslope(LCT10loc),Mon.daqchannelinfo.EUoffset(LCT10loc),Mon.daqchannelinfo.EUslope(LCT10loc)) + ...
        Mon.scr.readDAQ(Mon.daqchannelinfo.offset(LCT11loc),Mon.daqchannelinfo.gain(LCT11loc),Mon.daqchannelinfo.Voffset(LCT11loc),Mon.daqchannelinfo.Vslope(LCT11loc),Mon.daqchannelinfo.EUoffset(LCT11loc),Mon.daqchannelinfo.EUslope(LCT11loc)) + ...
        Mon.scr.readDAQ(Mon.daqchannelinfo.offset(LCT12loc),Mon.daqchannelinfo.gain(LCT12loc),Mon.daqchannelinfo.Voffset(LCT12loc),Mon.daqchannelinfo.Vslope(LCT12loc),Mon.daqchannelinfo.EUoffset(LCT12loc),Mon.daqchannelinfo.EUslope(LCT12loc));    
F4PT =  Mon.scr.readDAQ(Mon.daqchannelinfo.offset(LCT1loc),Mon.daqchannelinfo.gain(LCT1loc),Mon.daqchannelinfo.Voffset(LCT1loc),Mon.daqchannelinfo.Vslope(LCT1loc),Mon.daqchannelinfo.EUoffset(LCT1loc),Mon.daqchannelinfo.EUslope(LCT1loc)) + ...
        Mon.scr.readDAQ(Mon.daqchannelinfo.offset(LCT2loc),Mon.daqchannelinfo.gain(LCT2loc),Mon.daqchannelinfo.Voffset(LCT2loc),Mon.daqchannelinfo.Vslope(LCT2loc),Mon.daqchannelinfo.EUoffset(LCT2loc),Mon.daqchannelinfo.EUslope(LCT2loc)) + ...
        Mon.scr.readDAQ(Mon.daqchannelinfo.offset(LCT3loc),Mon.daqchannelinfo.gain(LCT3loc),Mon.daqchannelinfo.Voffset(LCT3loc),Mon.daqchannelinfo.Vslope(LCT3loc),Mon.daqchannelinfo.EUoffset(LCT3loc),Mon.daqchannelinfo.EUslope(LCT3loc)) + ...
        Mon.scr.readDAQ(Mon.daqchannelinfo.offset(LCT4loc),Mon.daqchannelinfo.gain(LCT4loc),Mon.daqchannelinfo.Voffset(LCT4loc),Mon.daqchannelinfo.Vslope(LCT4loc),Mon.daqchannelinfo.EUoffset(LCT4loc),Mon.daqchannelinfo.EUslope(LCT4loc));

% Calculate Drifts
F1Drift = Mon.scr.readDAQ(Mon.daqchannelinfo.offset(DTRN1loc),Mon.daqchannelinfo.gain(DTRN1loc),Mon.daqchannelinfo.Voffset(DTRN1loc),Mon.daqchannelinfo.Vslope(DTRN1loc),Mon.daqchannelinfo.EUoffset(DTRN1loc),Mon.daqchannelinfo.EUslope(DTRN1loc)) / 117.55;
F2Drift = (Mon.scr.readDAQ(Mon.daqchannelinfo.offset(DTRN2loc),Mon.daqchannelinfo.gain(DTRN2loc),Mon.daqchannelinfo.Voffset(DTRN2loc),Mon.daqchannelinfo.Vslope(DTRN2loc),Mon.daqchannelinfo.EUoffset(DTRN2loc),Mon.daqchannelinfo.EUslope(DTRN2loc)) - ...
           Mon.scr.readDAQ(Mon.daqchannelinfo.offset(DTRN1loc),Mon.daqchannelinfo.gain(DTRN1loc),Mon.daqchannelinfo.Voffset(DTRN1loc),Mon.daqchannelinfo.Vslope(DTRN1loc),Mon.daqchannelinfo.EUoffset(DTRN1loc),Mon.daqchannelinfo.EUslope(DTRN1loc))) / 90.0;
F3Drift = (Mon.scr.readDAQ(Mon.daqchannelinfo.offset(DTRN3loc),Mon.daqchannelinfo.gain(DTRN3loc),Mon.daqchannelinfo.Voffset(DTRN3loc),Mon.daqchannelinfo.Vslope(DTRN3loc),Mon.daqchannelinfo.EUoffset(DTRN3loc),Mon.daqchannelinfo.EUslope(DTRN3loc)) - ...
           Mon.scr.readDAQ(Mon.daqchannelinfo.offset(DTRN2loc),Mon.daqchannelinfo.gain(DTRN2loc),Mon.daqchannelinfo.Voffset(DTRN2loc),Mon.daqchannelinfo.Vslope(DTRN2loc),Mon.daqchannelinfo.EUoffset(DTRN2loc),Mon.daqchannelinfo.EUslope(DTRN2loc))) / 90.0125;       
F4Drift = (Mon.scr.readDAQ(Mon.daqchannelinfo.offset(DTRNRloc),Mon.daqchannelinfo.gain(DTRNRloc),Mon.daqchannelinfo.Voffset(DTRNRloc),Mon.daqchannelinfo.Vslope(DTRNRloc),Mon.daqchannelinfo.EUoffset(DTRNRloc),Mon.daqchannelinfo.EUslope(DTRNRloc)) - ...
           Mon.scr.readDAQ(Mon.daqchannelinfo.offset(DTRN3loc),Mon.daqchannelinfo.gain(DTRN3loc),Mon.daqchannelinfo.Voffset(DTRN3loc),Mon.daqchannelinfo.Vslope(DTRN3loc),Mon.daqchannelinfo.EUoffset(DTRN3loc),Mon.daqchannelinfo.EUslope(DTRN3loc))) / 90.0;              
       
% Calculate Story Shears
F1StoryShear = feedback(4) + feedback(3) + feedback(2) + feedback(1);
F2StoryShear = feedback(4) + feedback(3) + feedback(2);
F3StoryShear = feedback(4) + feedback(3);
F4StoryShear = feedback(4);
    
%Write to scramnet 
Mon.scr.writeFloat(F1PT_scr,F1PT);
Mon.scr.writeFloat(F2PT_scr,F2PT);
Mon.scr.writeFloat(F3PT_scr,F3PT);
Mon.scr.writeFloat(F4PT_scr,F4PT);
Mon.scr.writeFloat(F1Drift_scr,F1Drift);
Mon.scr.writeFloat(F2Drift_scr,F2Drift);
Mon.scr.writeFloat(F3Drift_scr,F3Drift);
Mon.scr.writeFloat(F4Drift_scr,F4Drift);
Mon.scr.writeFloat(F1StoryShear_scr,F1StoryShear);
Mon.scr.writeFloat(F2StoryShear_scr,F2StoryShear);
Mon.scr.writeFloat(F3StoryShear_scr,F3StoryShear);
Mon.scr.writeFloat(F4StoryShear_scr,F4StoryShear);
