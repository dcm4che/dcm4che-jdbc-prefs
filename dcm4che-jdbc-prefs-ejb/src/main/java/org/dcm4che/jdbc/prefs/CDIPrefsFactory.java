package org.dcm4che.jdbc.prefs;

import java.util.prefs.Preferences;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;

import org.dcm4che3.conf.prefs.cdi.PrefsFactory;

@ApplicationScoped
public class CDIPrefsFactory implements PrefsFactory{
    
    @EJB
    private QueryPreferences queryPreferences;
    
    @Override
    public Preferences getPreferences() {
        return new PreferencesImpl(queryPreferences);
    }
    
}
