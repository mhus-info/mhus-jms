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
package de.mhus.lib.jms;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.jms.JMSException;
import javax.jms.Message;

import de.mhus.lib.basics.RC;
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
            final Enumeration<String> enu = message.getPropertyNames();
            return new Iterator<Entry<String, String>>() {
                @Override
                public boolean hasNext() {
                    return enu.hasMoreElements();
                }

                @Override
                public Entry<String, String> next() {
                    final String key = enu.nextElement();
                    return new Entry<String, String>() {

                        @Override
                        public String getKey() {
                            return key;
                        }

                        @Override
                        public String getValue() {
                            try {
                                return message.getStringProperty(key);
                            } catch (JMSException e) {
                                throw new MRuntimeException(RC.STATUS.ERROR, key, e);
                            }
                        }

                        @Override
                        public String setValue(String value) {
                            return null;
                        }
                    };
                }
            };
        } catch (JMSException e) {
            throw new MRuntimeException(RC.STATUS.ERROR, e);
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
