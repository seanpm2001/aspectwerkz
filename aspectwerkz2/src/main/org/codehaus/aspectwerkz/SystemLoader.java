/**************************************************************************************
 * Copyright (c) Jonas Bon�r, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.aspectwerkz.definition.DefinitionLoader;
import org.codehaus.aspectwerkz.definition.SystemDefinition;
import org.codehaus.aspectwerkz.exception.WrappedRuntimeException;

/**
 * Loads the different types of system. Caches the system, mapped to its id.
 * <p/>
 * TODO: put this class in the same package as the System impl. and set the constructor to package private
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bon�r</a>
 */
public class SystemLoader {

    /**
     * Holds references to all the systems defined. Maps the UUID to a matching system instance.
     */
    private static final Map s_systems = new HashMap();

    /**
     * Returns the system with a specific UUID.
     * <p/>
     *
     * @param uuid the UUID for the system
     * @return the system for the UUID specified
     * @TODO: is this caching a bottleneck, since it req. the method to be synchronized? Is there a better impl.?
     */
    public synchronized static RuntimeSystem getSystem(final String uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("uuid can not be null");
        }
        try {
            RuntimeSystem system = (RuntimeSystem)s_systems.get(uuid);
            if (system == null) {
                final SystemDefinition definition = DefinitionLoader.getDefinition(
                        ContextClassLoader.getLoader(), uuid
                );
                system = new RuntimeSystem(uuid, definition);
                s_systems.put(uuid, system);
            }
            return system;
        }
        catch (Exception e) {
            throw new WrappedRuntimeException(e);
        }
    }
}
