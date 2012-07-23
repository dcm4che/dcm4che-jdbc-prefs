@echo off
rem -------------------------------------------------------------------------
rem xmlPrefs2jdbc  Launcher
rem -------------------------------------------------------------------------

if not "%ECHO%" == ""  echo %ECHO%
if "%OS%" == "Windows_NT"  setlocal

set MAIN_CLASS=org.dcm4che.tool.xml2prefs.Xml2Prefs
set MAIN_JAR=dcm4che-tool-xml2prefs-3.0.0-SNAPSHOT.jar

set DIRNAME=.\
if "%OS%" == "Windows_NT" set DIRNAME=%~dp0%

rem Read all command line arguments

set ARGS=
:loop
if [%1] == [] goto end
        set ARGS=%ARGS% %1
        shift
        goto loop
:end

if not "%DCM4CHE_HOME%" == "" goto HAVE_DCM4CHE_HOME

set DCM4CHE_HOME=%DIRNAME%..

:HAVE_DCM4CHE_HOME

if not "%JAVA_HOME%" == "" goto HAVE_JAVA_HOME

set JAVA=java

goto SKIP_SET_JAVA_HOME

:HAVE_JAVA_HOME

set JAVA=%JAVA_HOME%\bin\java

:SKIP_SET_JAVA_HOME

set CP=%DCM4CHE_HOME%\lib\%MAIN_JAR%
set CP=%CP%;%DCM4CHE_HOME%\lib\slf4j-api-1.6.4.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\slf4j-log4j12-1.6.4.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\log4j-1.2.16.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\dcm4che-jdbc-prefs-1.0.0-SNAPSHOT.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\ojdbc6.jar

"%JAVA%" %JAVA_OPTS% -cp "%CP%" -Djdbc.backend.url=jdbc:oracle:thin:@localhost:1521:xe -Djdbc.user.name=prefs -Djdbc.user.pwd=prefs -Djava.util.prefs.PreferencesFactory=org.dcm4che.jdbc.prefs.PreferencesFactoryImpl %MAIN_CLASS% %ARGS%
