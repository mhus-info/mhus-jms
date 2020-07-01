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
package de.mhus.jms.karaf;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import de.mhus.lib.core.console.ConsoleTable;
import de.mhus.lib.jms.JmsConnection;
import de.mhus.osgi.api.jms.JmsDataChannel;
import de.mhus.osgi.api.jms.JmsManagerService;
import de.mhus.osgi.api.jms.JmsUtil;
import de.mhus.osgi.api.karaf.AbstractCmd;

@Command(scope = "jms", name = "connection-info", description = "Connection Information")
@Service
public class CmdConnectionInfo extends AbstractCmd {

    @Argument(
            index = 0,
            name = "name",
            required = true,
            description = "ID of the connection",
            multiValued = false)
    String name;

    @Override
    public Object execute2() throws Exception {

        JmsManagerService service = JmsUtil.getService();
        if (service == null) {
            System.out.println("Service not found");
            return null;
        }

        JmsConnection con = service.getConnection(name);
        if (con == null) {
            System.out.println("Connection not found");
            return null;
        }

        ConsoleTable table = new ConsoleTable(tblOpt);
        table.setHeaderValues("Name", "Queue", "Type");
        for (JmsDataChannel c : service.getChannels()) {
            if (c.getChannel() != null
                    && c.getChannel().getJmsDestination() != null
                    && c.getChannel().getJmsDestination().getConnection() == con)
                table.addRowValues(
                        c.getName(),
                        c.getChannel().toString(),
                        c.getChannel().getClass().getSimpleName());
        }
        table.print(System.out);

        return null;
    }
}
