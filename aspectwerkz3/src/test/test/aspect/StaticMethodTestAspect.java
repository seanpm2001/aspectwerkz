/**************************************************************************************
 * Copyright (c) Jonas Bon�r, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.aspect;

import test.StaticMethodAdviceTest;
import org.codehaus.aspectwerkz.Pointcut;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.joinpoint.MethodRtti;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bon�r</a>
 * @Aspect perJVM
 */
public class StaticMethodTestAspect {
    // ============ Pointcuts ============

    /**
     * @Expression execution(* test.StaticMethodAdviceTest.get*(..))
     */
    Pointcut static_pc1;

    /**
     * @Expression execution(* test.StaticMethodAdviceTest.*Param*(..))
     */
    Pointcut static_pc2;

    /**
     * @Expression execution(void test.StaticMethodAdviceTest.methodAdvicedMethod(..))
     */
    Pointcut static_pc4;

    /**
     * @Expression execution(* test.StaticMethodAdviceTest.methodAdvicedMethod(..))
     */
    Pointcut static_pc5;

    /**m
     * @Expression execution(* test.StaticMethodAdviceTest.methodAdvicedMethodNewThread(..))
     */
    Pointcut static_pc6;

    /**
     * @Expression execution(* test.StaticMethodAdviceTest.multipleMethodAdvicedMethod(..))
     */
    Pointcut static_pc7;

    /**
     * @Expression execution(* test.StaticMethodAdviceTest.multipleChainedMethodAdvicedMethod(..))
     */
    Pointcut static_pc8;

    /**
     * @Expression execution(* test.StaticMethodAdviceTest.joinPointMetaData(..))
     */
    Pointcut static_pc9;

    /**
     * @Expression execution(void test.StaticMethodAdviceTest.multiplePointcutsMethod(..))
     */
    Pointcut static_pc10;

    /**
     * @Expression execution(void test.StaticMethodAdviceTest.multiplePointcutsMethod(..))
     */
    Pointcut static_pc11;

    /**
     * @Expression execution(* test.StaticMethodAdviceTest.takesArrayAsArgument(String[]))
     */
    Pointcut static_pc12;

    /**
     * @Expression execution(long test.StaticMethodAdviceTest.getPrimitiveAndNullFromAdvice())
     */
    Pointcut static_pc13;

    // ============ Advices ============

    /**
     * @Around static_pc1 || static_pc2 || static_pc5 || static_pc8 || static_pc12
     */
    public Object advice1(final JoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }

    /**
     * @Around static_pc4 || static_pc7 || static_pc8 || static_pc10
     */
    public Object advice2(final JoinPoint joinPoint) throws Throwable {
        StaticMethodAdviceTest.log("before1 ");
        final Object result = joinPoint.proceed();
        StaticMethodAdviceTest.log("after1 ");
        return result;
    }

    /**
     * @Around static_pc7 || static_pc8 || static_pc11
     */
    public Object advice3(final JoinPoint joinPoint) throws Throwable {
        StaticMethodAdviceTest.log("before2 ");
        final Object result = joinPoint.proceed();
        StaticMethodAdviceTest.log("after2 ");
        return result;
    }

    /**
     * @Around static_pc9
     */
    public Object advice4(final JoinPoint joinPoint) throws Throwable {
        final Object result = joinPoint.proceed();
        MethodRtti rtti = (MethodRtti)joinPoint.getRtti();
        String metadata = joinPoint.getTargetClass().getName() + rtti.getMethod().getName()
                          + rtti.getParameterValues()[0] + rtti.getParameterTypes()[0].getName()
                          + rtti.getReturnType().getName() + rtti.getReturnValue();
        return metadata;
    }

    /**
     * @Around static_pc6
     */
    public Object advice5(final JoinPoint joinPoint) throws Throwable {
        StaticMethodAdviceTest.log("before ");
        final Object result = joinPoint.proceed();
        StaticMethodAdviceTest.log("after ");
        return result;
    }

    /**
     * @Around static_pc13
     */
    public Object advice7(final JoinPoint joinPoint) throws Throwable {
        return null;
    }
}
