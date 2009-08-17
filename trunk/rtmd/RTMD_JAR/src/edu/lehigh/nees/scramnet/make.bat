set JAVA_HOME="C:\program files\Java\jdk1.6.0_13"
set SCRAMNET_HOME="C:\program files\scramnet"
set PATH=%PATH%;C:\Program Files\Microsoft Visual Studio .NET 2003\Common7\IDE;
set INCLUDE=%INCLUDE%;C:\Program Files\Microsoft Visual Studio .NET 2003\Vc7\include;
set LIB=%LIB%;C:\Program Files\Microsoft Visual Studio .NET 2003\Vc7\lib;

"C:\Program Files\Microsoft Visual Studio .NET 2003\Common7\Tools\vsvars32.bat"

"C:\Program Files\Microsoft Visual Studio .NET 2003\Vc7\bin\cl.exe" -I%JAVA_HOME%\include -I%JAVA_HOME%\include\win32 -I%SCRAMNET_HOME%\inc -LD ScramNetIO.c -Fescramnetio.dll /link PNPSCR.lib
@pause