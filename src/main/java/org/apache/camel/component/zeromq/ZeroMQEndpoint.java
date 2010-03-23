/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.zeromq;

import org.apache.camel.*;
import org.apache.camel.component.zeromq.ZeroMQSupport;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.ManagementAware;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

import java.util.Map;

@ManagedResource(description = "Managed Native Endpoint")
public class ZeroMQEndpoint extends DefaultEndpoint implements ManagementAware<ZeroMQEndpoint> {

    private static final transient Log LOG = LogFactory.getLog(ZeroMQEndpoint.class);

    private final String zeroMQURI;
    private final Map zeroMQProperties;

    private ZeroMQProducer producer;
    private ZeroMQConsumer consumer;

    public String getZeroMQURI() {
        return zeroMQURI;
    }

    public Map getZeroMQProperties() {
        return zeroMQProperties;
    }

    public ZeroMQEndpoint(String endpointUri, String remaining, Map parameters, CamelContext context) {
        super(endpointUri, context);
        this.zeroMQURI = remaining;
        this.zeroMQProperties = parameters;
        LOG.trace("Begin ZeroMQEndpoint.ZeroMQEndpoint");
        try {
            try {
                NativeLibraryLoader.loadLibrary("zeromq_support");
            } catch (java.io.IOException e) {
                System.loadLibrary("zeromq_support");
            }
        } catch (Exception ex) {
            LOG.fatal(ex, ex);
        } finally {
            LOG.trace("End HdfsEndpoint.HdfsEndpoint");
        }
    }

    @Override
    public final Consumer createConsumer(Processor processor) {
        try {
            LOG.trace("Begin ZeroMQEndpoint.createConsumer");
            consumer = new ZeroMQConsumer(this, processor);
            return consumer;
        }
        catch (Exception ex) {
            LOG.fatal(ex, ex);
            throw new RuntimeCamelException(ex);
        }
        finally {
            LOG.trace("End ZeroMQEndpoint.createConsumer");
        }
    }

    @Override
    public final Producer createProducer() {
        try {
            LOG.trace("Begin ZeroMQEndpoint.createProducer");
            producer = new ZeroMQProducer(this);
            return producer;
        }
        catch (Exception ex) {
            LOG.fatal(ex, ex);
            throw new RuntimeCamelException(ex);
        }
        finally {
            LOG.trace("End ZeroMQEndpoint.createProducer");
        }
    }

    @Override
    public boolean isLenientProperties() {
        return true;
    }

    @Override
    @ManagedAttribute
    public final boolean isSingleton() {
        return true;
    }


    public ZeroMQConsumer getConsumer() {
        return consumer;
    }
    
    public final ZeroMQProducer getProducer() {
        return producer;
    }

    @Override
    public final Object getManagedObject(ZeroMQEndpoint endpoint) {
        return this;
    }

    @ManagedAttribute(description = "Camel id")
    public final String getCamelId() {
        return getCamelContext().getName();
    }

}
