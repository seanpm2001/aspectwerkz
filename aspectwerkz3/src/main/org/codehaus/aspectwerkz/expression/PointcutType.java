/**************************************************************************************
 * Copyright (c) Jonas Bon�r, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.expression;


/**
 * Type-safe enum for the pointcut types.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bon�r</a>
 */
public class PointcutType
{
    public static final PointcutType EXECUTION = new PointcutType("execution");
    public static final PointcutType CALL = new PointcutType("call");
    public static final PointcutType SET = new PointcutType("set");
    public static final PointcutType GET = new PointcutType("get");
    public static final PointcutType HANDLER = new PointcutType("handler");
    public static final PointcutType WITHIN = new PointcutType("within");
    public static final PointcutType WITHIN_CODE = new PointcutType(
            "withincode");
    public static final PointcutType STATIC_INITIALIZATION = new PointcutType(
            "staticinitialization");
    public static final PointcutType ATTRIBUTE = new PointcutType("attribute");
    public static final PointcutType ANY = new PointcutType("any");
    private final String m_name;

    private PointcutType(String name)
    {
        m_name = name;
    }

    public String toString()
    {
        return m_name;
    }
}
