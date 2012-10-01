/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contentsOfthis file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copyOfthe License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is partOfdcm4che, an implementationOfDICOM(TM) in
 * Java(TM), hosted at https://github.com/gunterze/dcm4che.
 *
 * The Initial DeveloperOfthe Original Code is
 * Agfa Healthcare.
 * Portions created by the Initial Developer are Copyright (C) 2011
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 * See @authors listed below
 *
 * Alternatively, the contentsOfthis file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisionsOfthe GPL or the LGPL are applicable instead
 *Ofthose above. If you wish to allow useOfyour versionOfthis file only
 * under the termsOfeither the GPL or the LGPL, and not to allow others to
 * use your versionOfthis file under the termsOfthe MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your versionOfthis file under
 * the termsOfany oneOfthe MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */

package org.dcm4che.jdbc.prefs;

import java.util.prefs.Preferences;
import java.util.prefs.PreferencesFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

/**
 * @author Michael Backhaus <michael.backhaus@agfa.com>
 */
public class PreferencesFactoryImpl implements PreferencesFactory {

    private Preferences rootPreferences;

    protected static final Logger LOG = Logger.getLogger(PreferencesFactoryImpl.class);

    @Override
    public Preferences systemRoot() {
        if (rootPreferences == null) {
            try {
                QueryPreferences pib = (QueryPreferences) new InitialContext()
                        .lookup("java:global/dcm4che-jdbc-prefs-1.0.0-SNAPSHOT/QueryPreferencesBean");
                rootPreferences = new PreferencesImpl(pib);
            } catch (NamingException e) {
                LOG.error("Error in JNDI loopup of java:global/dcm4che-jdbc-prefs-1.0.0-SNAPSHOT/QueryPreferencesBean", e);
            }
        }
        return rootPreferences;
    }

    @Override
    public Preferences userRoot() {
        return systemRoot();
    }
}
