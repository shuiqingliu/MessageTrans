echo off
set SYS_HOME=E:\product\msg


set JAVA_HOME=D:\MyEclipse-8.6\binary\com.sun.java.jdk.win32.x86_1.6.0.013
set path=C:\Windows\system32;
set path=%path%;%JAVA_HOME%\bin;
set path=%path%;E:\Program Files\Notepad++;
set path=%path%;C:\Program Files\Microsoft SQL Server\80\Tools\Binn

set LIB=%SYS_HOME%\v1\lib

set CLASSPATH=%SYS_HOME%\v1\bin
set CLASSPATH=%CLASSPATH%;%SYS_LIB%\junit.jar
set CLASSPATH=%CLASSPATH%;%SYS_LIB%\commons-beanutils.jar
echo on

