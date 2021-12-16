package de.mhus.osgi.jms;

import org.osgi.service.component.annotations.Component;

import de.mhus.lib.core.MLog;
import de.mhus.lib.jms.JmsApi;
import de.mhus.lib.jms.JmsConnection;
import de.mhus.osgi.api.jms.JmsUtil;

@Component(name = "JmsApi", immediate = true)
public class JmsApiImpl extends MLog implements JmsApi {

    @Override
    public JmsConnection getConnection(String name) {
        return JmsUtil.getConnection(name);
    }

}
