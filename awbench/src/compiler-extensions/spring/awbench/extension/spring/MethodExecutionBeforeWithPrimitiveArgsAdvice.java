/**************************************************************************************
 * Copyright (c) Jonas Bon�r, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package awbench.extension.spring;

import java.lang.reflect.Method;
import org.springframework.aop.MethodBeforeAdvice;
import awbench.Run;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bon�r </a>
 */
public class MethodExecutionBeforeWithPrimitiveArgsAdvice implements MethodBeforeAdvice {
    public void before(Method method, Object[] objects, Object o) throws Throwable {
        Run.ADVICE_HIT++;
        int i = ((Integer)objects[0]).intValue();
    }
}

