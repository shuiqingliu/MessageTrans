call s.bat

set CLASSPATH=%TENANT2_HOME%\src
set CLASSPATH=%CLASSPATH%;E:\tenant2\system4\build\WEB-INF\classes

java com.jyt.util.MyCommand cmd_list.txt env_list.txt