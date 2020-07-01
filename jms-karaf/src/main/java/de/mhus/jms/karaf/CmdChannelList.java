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

import java.util.Date;

import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import de.mhus.lib.core.MDate;
import de.mhus.lib.core.console.ConsoleTable;
import de.mhus.lib.jms.JmsChannel;
import de.mhus.lib.jms.JmsConnection;
import de.mhus.lib.jms.ServerJms;
import de.mhus.osgi.api.jms.JmsDataChannel;
import de.mhus.osgi.api.jms.JmsManagerService;
import de.mhus.osgi.api.jms.JmsUtil;
import de.mhus.osgi.api.karaf.AbstractCmd;

@Command(scope = "jms", name = "channel-list", description = "List Channels")
@Service
public class CmdChannelList extends AbstractCmd {

    @Override
    public Object execute2() throws Exception {

        JmsManagerService service = JmsUtil.getService();
        if (service == null) {
            System.out.println("Service not found");
            return null;
        }

        ConsoleTable table = new ConsoleTable(tblOpt);
        table.setHeaderValues(
                "Name",
                "Connection",
                "Destination",
                "Type",
                "Information",
                "Connected",
                "Closed",
                "Last Activity");
        for (JmsDataChannel chd : service.getChannels()) {
            //			JmsDataChannel chd = service.getChannel(name);
            JmsChannel ch = chd.getChannel();
            JmsConnection con = null;
            if (ch != null && ch.getJmsDestination() != null)
                con = ch.getJmsDestination().getConnection();
            String i = chd.toString();
            table.addRowValues(
                    chd.getName(),
                    (con == null ? "(" : "") + chd.getConnectionName() + (con == null ? ")" : ""),
                    ch == null ? "" : ch.getJmsDestination(),
                    ch == null ? "" : ch.getClass().getCanonicalName(),
                    i,
                    ch == null ? "" : ch.isConnected(),
                    ch == null ? "" : ch.isClosed(),
                    ch == null || !(ch instanceof ServerJms)
                            ? ""
                            : MDate.toDateTimeSecondsString(
                                    new Date(((ServerJms) ch).getLastActivity())));
        }
        table.print(System.out);

        return null;
    }
}
