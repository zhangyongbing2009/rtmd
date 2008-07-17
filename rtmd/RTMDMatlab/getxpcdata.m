function data = getxpcdata(tg,x)
%f = tg.fs;
%fhandle=f.fopen([x])
fhandle = fopen(x);
%d1 = f.fread(fhandle);
d1 = fread(fhandle);
%f.fclose(fhandle);
fclose(fhandle);
d1 = uint8(d1');
d1 = readxpcfile(d1);
data=d1;