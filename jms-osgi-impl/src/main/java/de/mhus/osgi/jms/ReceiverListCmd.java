/**
 * Copyright (C) 2020 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.osgi.jms;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import de.mhus.osgi.api.jms.JmsReceiver;
import de.mhus.osgi.api.jms.JmsReceiverAdmin;

@Command(scope = "jms", name = "direct-list", description = "listen")
@Service
public class ReceiverListCmd implements Action {

    @Override
    public Object execute() throws Exception {

        JmsReceiverAdmin admin = JmsReceiverAdminImpl.findAdmin();

        for (JmsReceiver r : admin.list()) System.out.println(r.getName());

        return null;
    }
}
