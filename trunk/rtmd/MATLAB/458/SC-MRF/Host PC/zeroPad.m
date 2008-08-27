%% Zero pad i so it has a total of len numbers and return it as a string
function snum = zeroPad(i,len)
    s = num2str(i);
    zs = '0000000000';
    snum = [zs(1:len-length(s)) s];    