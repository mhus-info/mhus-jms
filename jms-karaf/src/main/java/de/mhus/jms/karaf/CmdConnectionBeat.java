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

import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import de.mhus.osgi.api.jms.JmsManagerService;
import de.mhus.osgi.api.jms.JmsUtil;
import de.mhus.osgi.api.karaf.AbstractCmd;

@Command(
        scope = "jms",
        name = "connection-beat",
        description = "Beat the connection, load connections and channels")
@Service
public class CmdConnectionBeat extends AbstractCmd {

    @Option(
            name = "-c",
            aliases = "--channels",
            description = "Beat also all channels",
            required = false)
    boolean channels = false;

    @Override
    public Object execute2() throws Exception {

        JmsManagerService service = JmsUtil.getService();
        if (service == null) {
            System.out.println("Service not found");
            return null;
        }

        service.doBeat();
        if (channels) service.doChannelBeat();
        System.out.println("OK");

        return null;
    }
}
