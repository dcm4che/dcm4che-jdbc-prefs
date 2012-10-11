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
package org.dcm4che.jdbc.prefs.test;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.junit.Assume;
import org.junit.Test;


/**
 * @author Juergen Schmied <juergenschmied70@gmail.com>
 * @author Michael Backhaus <michael.backhaus@agfa.com>
 */
public class TestPreferences {

    @Test
    public void testPreferences() throws BackingStoreException {
        System.setProperty("java.util.prefs.PreferencesFactory", "org.dcm4che.jdbc.prefs.PreferencesFactoryJDBCImpl");
        Preferences root = Preferences.systemRoot();
        Preferences node2 = root.node("test1/test2");
        node2.put("key99", "value");
        node2.putLong("Pi", 3532545L);
        Assume.assumeTrue(node2.keys().length == 2);
        node2.remove("Pi");
        Assume.assumeTrue(node2.keys().length == 1);
        node2.removeNode();
        Preferences node3 = root.node("test1");
        Assume.assumeTrue(!node3.nodeExists("test2"));
        Assume.assumeTrue(node3.childrenNames().length == 0);
        node3.put("key88", "value88");
        Assume.assumeTrue(node3.keys().length == 1);
        node3.removeNode();
        root.sync();
        root.flush();
    }

}
