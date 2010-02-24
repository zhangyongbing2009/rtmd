% Downloads and Opens an XPC file and converts to ASCII data
function data = getxpcdata(tg,x)
f = tg.fs;
fhandle=f.fopen([x]);
d1 = f.fread(fhandle);
f.fclose(fhandle);
d1 = uint8(d1');
d1 = readxpcfile(d1);
data=d1;