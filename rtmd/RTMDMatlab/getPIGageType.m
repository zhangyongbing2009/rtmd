function name = getGageType(type)
    switch (type)
        case 0
            name = 'Bridge';
        case 1
            name = 'ICP';
        case 2
            name = 'Voltage';
        case 3
            name = 'DCLVDT';
        case 4
            name = 'RTD';
        case 5
            name = 'Frequency';
        case 6
            name = 'Period';
        case 7
            name = 'Counter';
        case 8
            name = 'Digital';
        case 9
            name = 'Irig';
        case 10
            name = 'EDAC';
        case 11
            name = '5500';
        case 12
            name = 'Digital Out';
        case 13
            name = 'TCB';
        case 14
            name = 'TCC';
        case 15
            name = 'TCE';
        case 16
            name = 'TCJ';
        case 17
            name = 'TCK';
        case 18
            name = 'TCN';
        case 19
            name = 'TCR';
        case 20
            name = 'TCS';
        case 21
            name = 'TCT';
        case 22
            name = 'TCREF';
        case 23
            name = '1/4 Bridge 120';
        case 24
            name = '1/4 Bridge 350';
        case 25
            name = '1/2 Bridge';
        case 26
            name = 'Full Bridge';
        case 27
            name = 'Frequency 32';
        case 28
            name = 'Period 32';
        case 29
            name = 'Counter 32';
        case 30
            name = 'FTOV';
        case 31
            name = 'DSP';
        case 32
            name = 'Digital In';
        case 33
            name = '8400';
        otherwise
            name = 'Unknown';        
    end
end