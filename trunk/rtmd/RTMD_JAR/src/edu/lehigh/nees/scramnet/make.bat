set JAVA_HOME="C:\program files\Java\jdk1.6.0"
set SCRAMNET_HOME="C:\program files\scramnet"

cl -I%JAVA_HOME%\include -I%JAVA_HOME%\include\win32 -I%SCRAMNET_HOME%\inc -LD ScramNetIO.c -Fescramnetio.dll /link PNPSCR.lib
@pause