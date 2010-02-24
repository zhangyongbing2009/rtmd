% Opens an XPC file and converts to ASCII data
function data = convertxpcdata(x)

fhandle=fopen([x]);
d1 = fread(fhandle);
fclose(fhandle);
d1 = uint8(d1');
d1 = readxpcfile(d1);
data=d1;