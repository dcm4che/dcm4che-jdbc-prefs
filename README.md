INFO
====

The dcm4che-jdbc-prefs project provides an extension to store java preferences in a database.

Library
=======

The library requires the system properties `java.util.prefs.PreferencesFactory` and `jdbc.backend.url` 
to be set either in the code (example):

```java
System.setProperty("java.util.prefs.PreferencesFactory", "org.dcm4che.jdbc.prefs.PreferencesFactoryImpl");
System.setProperty("jdbc.backend.url","jdbc:oracle:thin:prefs/prefs@localhost:1521:xe");
```

or as execution parameters (example):

```
-Djdbc.backend.url=jdbc:oracle:thin:prefs/prefs@localhost:1521:xe 
-Djava.util.prefs.PreferencesFactory=org.dcm4che.jdbc.prefs.PreferencesFactoryImpl
```

Tool
====

The tool `xmlPrefs2jdbc` provides a vehicle to import XML preferences into a database.

JBoss 7 Module
==============

The JBoss 7 Module can be unpacked into the JBoss 7 modules folder.

BUILD
=====

* Library: `mvn install [-P lib]`
* Tool: `mvn install -P tool`
* Module: `mvn install -P module`
