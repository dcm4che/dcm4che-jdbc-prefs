/* ***** BEGIN LICENSE BLOCK *****
 * Version: Apache License Version 2.0
 *
 * The contents of this file are subject to the Apache License, Version 2.0;
 * you may not use this file except in compliance with the License. You may 
 * obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * Contributor(s):
 * See @authors listed below
 *
 * ***** END LICENSE BLOCK ***** */
package org.dcm4che.jdbc.prefs;

import java.util.prefs.Preferences;
import java.util.prefs.PreferencesFactory;

/**
 * @author Juergen Schmied <juergenschmied70@gmail.com>
 */
public class PreferencesFactoryImpl implements PreferencesFactory {

    private Preferences rootPreferences;

    @Override
    public Preferences systemRoot() {
        if (rootPreferences == null) {
            rootPreferences = new PreferencesImpl();
        }
        return rootPreferences;
    }

    @Override
    public Preferences userRoot() {
        return systemRoot();
    }

}
