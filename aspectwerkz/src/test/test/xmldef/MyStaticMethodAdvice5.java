/**************************************************************************************
 * Copyright (c) The AspectWerkz Team. All rights reserved.                           *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD style license *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.xmldef.xmldef;

import org.codehaus.aspectwerkz.xmldef.advice.AroundAdvice;
import org.codehaus.aspectwerkz.xmldef.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.xmldef.joinpoint.MethodJoinPoint;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bon�r</a>
 */
public class MyStaticMethodAdvice5 extends AroundAdvice {
    public MyStaticMethodAdvice5() {
        super();
    }

    public Object execute(final JoinPoint joinPoint) throws Throwable {
        MethodJoinPoint jp = (MethodJoinPoint)joinPoint;
        ((StaticMethodAdviceTest)jp.getTargetObject()).log("before ");
        final Object result = joinPoint.proceedInNewThread();
        ((StaticMethodAdviceTest)jp.getTargetObject()).log("after ");
        return result;
    }
}
