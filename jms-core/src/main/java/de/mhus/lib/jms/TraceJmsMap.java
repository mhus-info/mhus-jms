package de.mhus.lib.jms;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.jms.JMSException;
import javax.jms.Message;

import de.mhus.lib.core.MLog;
import de.mhus.lib.errors.MRuntimeException;
import io.opentracing.propagation.TextMap;

public class TraceJmsMap extends MLog implements TextMap {

    private Message message;

    public TraceJmsMap(Message message) {
        this.message = message;
    }
    
    @Override
    public Iterator<Entry<String, String>> iterator() {
        try {
            @SuppressWarnings("unchecked")
            final Enumeration<String> enu =
                    message.getPropertyNames();
            return new Iterator<
                    Entry<String, String>>() {
                @Override
                public boolean hasNext() {
                    return enu.hasMoreElements();
                }

                @Override
                public Entry<String, String> next() {
                    final String key =
                            enu.nextElement();
                    return new Entry<String, String>() {

                        @Override
                        public String getKey() {
                            return key;
                        }

                        @Override
                        public String getValue() {
                            try {
                                return message
                                        .getStringProperty(
                                                key);
                            } catch (JMSException e) {
                                throw new MRuntimeException(
                                        key, e);
                            }
                        }

                        @Override
                        public String setValue(
                                String value) {
                            return null;
                        }
                    };
                }
            };
        } catch (JMSException e) {
            throw new MRuntimeException(e);
        }
    }

    @Override
    public void put(String key, String value) {
        try {
            message.setStringProperty(key, value);
        } catch (JMSException e) {
            log().e(key, value, e);
        }
    }
    
}
