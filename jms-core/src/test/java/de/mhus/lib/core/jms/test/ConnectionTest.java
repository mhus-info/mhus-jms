/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.jms.test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.jms.JMSException;

import org.junit.jupiter.api.Test;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.logging.Log.LEVEL;
import de.mhus.lib.jms.JmsConnection;
import de.mhus.lib.tests.TestCase;

public class ConnectionTest extends TestCase {

    @Test
    public void testLifecycle() throws JMSException {
        //		ConnectionFactory connectionFactory = new
        // ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");

        MApi.get().getLogFactory().setDefaultLevel(LEVEL.TRACE);

        JmsConnection con =
                new JmsConnection("vm://localhost?broker.persistent=false", "admin", "password");

        con.open();

        assertTrue(con.isConnected());

        con.close();
        try {
            con.open();
            assertTrue(false);
        } catch (JMSException e) {
        }
        assertFalse(con.isConnected());

        con.reopen();

        con.open();

        assertTrue(con.isConnected());

        con.close();

        assertFalse(con.isConnected());
    }
}
