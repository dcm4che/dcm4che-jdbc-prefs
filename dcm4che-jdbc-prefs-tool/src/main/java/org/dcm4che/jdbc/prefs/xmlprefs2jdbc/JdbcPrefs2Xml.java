package org.dcm4che.jdbc.prefs.xmlprefs2jdbc;

import org.dcm4che3.tool.prefs2xml.Prefs2Xml;

public class JdbcPrefs2Xml {
    public static void main(String[] args) throws Exception {
        System.setProperty("java.util.prefs.PreferencesFactory", "org.dcm4che.jdbc.prefs.PreferencesFactoryJDBCImpl");
        Prefs2Xml.main(args);
    }
}
