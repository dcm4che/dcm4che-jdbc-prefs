@echo off
rem -------------------------------------------------------------------------
rem jdbcprefs2xml Launcher
rem -------------------------------------------------------------------------
set MAIN_CLASS=org.dcm4che.jdbc.prefs.xmlprefs2jdbc.JdbcPrefs2Xml
			
%~dp0/xmlprefs2jdbc.bat %* 
