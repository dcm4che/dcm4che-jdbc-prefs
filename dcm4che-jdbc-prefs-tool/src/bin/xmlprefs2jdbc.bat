@echo off
rem -------------------------------------------------------------------------
rem xmlPrefs2jdbc  Launcher
rem -------------------------------------------------------------------------

if not "%ECHO%" == ""  echo %ECHO%
if "%OS%" == "Windows_NT"  setlocal

IF NOT DEFINED MAIN_CLASS set MAIN_CLASS=org.dcm4che.jdbc.prefs.xmlprefs2jdbc.XmlPrefs2Jdbc
set MAIN_JAR=dcm4che-jdbc-prefs-tool-${project.version}.jar

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
set CP=%CP%;%DCM4CHE_HOME%\etc\xmlprefs2jdbc
set CP=%CP%;%DCM4CHE_HOME%\lib\slf4j-api-1.6.4.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\slf4j-log4j12-1.6.4.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\log4j-1.2.16.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\dcm4che-jdbc-prefs-entity-1.0.0.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\hibernate-jpa-2.0-api-1.0.1.Final.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\hibernate-entitymanager-4.1.3.Final.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\hibernate-core-4.1.3.Final.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\jboss-logging-3.1.0.GA.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\jta-1.1.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\dom4j-1.6.1.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\hibernate-commons-annotations-4.0.1.Final.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\javassist-3.15.0-GA.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\commons-collections-3.2.1.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\antlr-2.7.7.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\dcm4che-tool-prefs2xml-${dcm4che.version}.jar

REM jdbc driver
set CP=%CP%;%DCM4CHE_HOME%\lib\ojdbc6.jar

REM Setup jdbc connection properties
set JDBC=-Djdbc.prefs.datasource=jdbc:oracle:thin:@localhost:1521:xe
set JDBC=%JDBC% -Djdbc.prefs.connection.username=prefs
set JDBC=%JDBC% -Djdbc.prefs.connection.password=prefs

"%JAVA%" %JAVA_OPTS% -cp "%CP%" %JDBC% %MAIN_CLASS% %ARGS%