/**************************************************************************************
 * Copyright (c) Jonas Bon�r, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.intercept;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Field;

import gnu.trove.TIntObjectHashMap;
import org.codehaus.aspectwerkz.reflect.ClassInfo;
import org.codehaus.aspectwerkz.reflect.ReflectionInfo;
import org.codehaus.aspectwerkz.reflect.impl.asm.AsmClassInfo;
import org.codehaus.aspectwerkz.reflect.impl.java.JavaClassInfo;
import org.codehaus.aspectwerkz.expression.PointcutType;
import org.codehaus.aspectwerkz.expression.ExpressionContext;
import org.codehaus.aspectwerkz.expression.ExpressionInfo;
import org.codehaus.aspectwerkz.transform.inlining.AsmHelper;
import org.codehaus.aspectwerkz.transform.inlining.EmittedJoinPoint;
import org.codehaus.aspectwerkz.transform.TransformationConstants;
import org.codehaus.aspectwerkz.joinpoint.management.JoinPointType;

/**
 * Implementation of the <code>Advisable</code> mixin.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bon�r </a>
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class AdvisableImpl implements Advisable {

    private static final String EXPRESSION_NAMESPACE = "___AW_ADVISABLE_AW___";

    public static final ClassInfo CLASS_INFO;
    public static final AroundAdvice[] EMPTY_AROUND_ADVICE_ARRAY = new AroundAdvice[0];
    public static final BeforeAdvice[] EMPTY_BEFORE_ADVICE_ARRAY = new BeforeAdvice[0];
    public static final AfterAdvice[] EMPTY_AFTER_ADVICE_ARRAY = new AfterAdvice[0];
    public static final AfterReturningAdvice[] EMPTY_AFTER_RETURNING_ADVICE_ARRAY = new AfterReturningAdvice[0];
    public static final AfterThrowingAdvice[] EMPTY_AFTER_THROWING_ADVICE_ARRAY = new AfterThrowingAdvice[0];

    static {
        final Class clazz = AdvisableImpl.class;
        try {
            CLASS_INFO = AsmClassInfo.getClassInfo(clazz.getName(), clazz.getClassLoader());
        } catch (Exception e) {
            throw new Error("could not create class info for [" + clazz.getName() + ']');
        }
    }

    private final Advisable m_targetInstance;
    private final TIntObjectHashMap m_emittedJoinPoints;

    private final TIntObjectHashMap m_aroundAdvice = new TIntObjectHashMap();
    private final TIntObjectHashMap m_beforeAdvice = new TIntObjectHashMap();
    private final TIntObjectHashMap m_afterAdvice = new TIntObjectHashMap();
    private final TIntObjectHashMap m_afterReturningAdvice = new TIntObjectHashMap();
    private final TIntObjectHashMap m_afterThrowingAdvice = new TIntObjectHashMap();

    /**
     * Creates a new mixin impl.
     *
     * @param targetInstance the target for this mixin instance (perInstance deployed)
     */
    public AdvisableImpl(final Object targetInstance) {
        if (!(targetInstance instanceof Advisable)) {
            throw new RuntimeException(
                    "advisable mixin applied to target class that does not implement the Advisable interface"
            );
        }
        m_targetInstance = (Advisable) targetInstance;

        try {
            Field f = targetInstance.getClass().getDeclaredField("aw$emittedJoinPoints");
            f.setAccessible(true);
            m_emittedJoinPoints = (TIntObjectHashMap) f.get(null);
        } catch (Exception e) {
            throw new RuntimeException(
                    "advisable mixin applied to target class cannot access reflective information: " + e.toString()
            );
        }
    }

    /**
     * @param pointcut
     * @param advice
     */
    public void aw_addAdvice(final String pointcut, final Advice advice) {
        addAdvice(pointcut, advice);
    }

    /**
     * @param pointcut
     * @param adviceClass
     */
    public void aw_removeAdvice(final String pointcut, final Class adviceClass) {
        removeAdvice(pointcut, adviceClass);
    }

    /**
     * @param joinPointIndex
     * @return
     */
    public AroundAdvice[] aw$getAroundAdvice(final int joinPointIndex) {
        Object advice = m_aroundAdvice.get(joinPointIndex);
        if (advice == null) {
            return EMPTY_AROUND_ADVICE_ARRAY;
        } else {
            return (AroundAdvice[]) advice;
        }
    }

    /**
     * @param joinPointIndex
     * @return
     */
    public BeforeAdvice[] aw$getBeforeAdvice(final int joinPointIndex) {
        Object advice = m_beforeAdvice.get(joinPointIndex);
        if (advice == null) {
            return EMPTY_BEFORE_ADVICE_ARRAY;
        } else {
            return (BeforeAdvice[]) advice;
        }
    }

    /**
     * @param joinPointIndex
     * @return
     */
    public AfterAdvice[] aw$getAfterAdvice(final int joinPointIndex) {
        Object advice = m_afterAdvice.get(joinPointIndex);
        if (advice == null) {
            return EMPTY_AFTER_ADVICE_ARRAY;
        } else {
            return (AfterAdvice[]) advice;
        }
    }

    /**
     * @param joinPointIndex
     * @return
     */
    public AfterReturningAdvice[] aw$getAfterReturningAdvice(final int joinPointIndex) {
        Object advice = m_afterReturningAdvice.get(joinPointIndex);
        if (advice == null) {
            return EMPTY_AFTER_RETURNING_ADVICE_ARRAY;
        } else {
            return (AfterReturningAdvice[]) advice;
        }
    }

    /**
     * @param joinPointIndex
     * @return
     */
    public AfterThrowingAdvice[] aw$getAfterThrowingAdvice(final int joinPointIndex) {
        Object advice = m_afterThrowingAdvice.get(joinPointIndex);
        if (advice == null) {
            return EMPTY_AFTER_THROWING_ADVICE_ARRAY;
        } else {
            return (AfterThrowingAdvice[]) advice;
        }
    }

    /**
     * @param pointcut
     * @param advice
     */
    private void addAdvice(final String pointcut,
                           final Advice advice) {
        ExpressionInfo expressionInfo = new ExpressionInfo(pointcut, EXPRESSION_NAMESPACE);
        Object[] emittedJoinPoints = m_emittedJoinPoints.getValues();
        for (int i = 0; i < emittedJoinPoints.length; i++) {
            EmittedJoinPoint emittedJoinPoint = (EmittedJoinPoint) emittedJoinPoints[i];
            if (match(expressionInfo, PointcutType.EXECUTION, emittedJoinPoint)
                || match(expressionInfo, PointcutType.CALL, emittedJoinPoint)
                || match(expressionInfo, PointcutType.HANDLER, emittedJoinPoint)
                || match(expressionInfo, PointcutType.GET, emittedJoinPoint)
                || match(expressionInfo, PointcutType.SET, emittedJoinPoint)
                //note: STATIC INIT is useless since the class is already loaded to manipulate the instance
            ) {
                int hash = emittedJoinPoint.getJoinPointClassName().hashCode();
                addAroundAdvice(advice, hash);
                addBeforeAdvice(advice, hash);
                addAfterAdvice(advice, hash);
                addAfterReturningAdvice(advice, hash);
                addAfterThrowingAdvice(advice, hash);
            }
        }
    }

    /**
     * @param pointcut
     * @param adviceClass
     */
    private void removeAdvice(final String pointcut,
                              final Class adviceClass) {
        ExpressionInfo expressionInfo = new ExpressionInfo(pointcut, EXPRESSION_NAMESPACE);
        Object[] emittedJoinPoints = m_emittedJoinPoints.getValues();
        for (int i = 0; i < emittedJoinPoints.length; i++) {
            EmittedJoinPoint emittedJoinPoint = (EmittedJoinPoint) emittedJoinPoints[i];
            if (match(expressionInfo, PointcutType.EXECUTION, emittedJoinPoint)
                || match(expressionInfo, PointcutType.CALL, emittedJoinPoint)
                || match(expressionInfo, PointcutType.HANDLER, emittedJoinPoint)
                || match(expressionInfo, PointcutType.GET, emittedJoinPoint)
                || match(expressionInfo, PointcutType.SET, emittedJoinPoint)
                //note: STATIC INIT is useless since the class is already loaded to manipulate the instance
            ) {
                int hash = emittedJoinPoint.getJoinPointClassName().hashCode();
                removeAroundAdvice(adviceClass, hash);
                removeBeforeAdvice(adviceClass, hash);
                removeAfterAdvice(adviceClass, hash);
                removeAfterReturningAdvice(adviceClass, hash);
                removeAfterThrowingAdvice(adviceClass, hash);
            }
        }
    }

    /**
     * @param advice
     * @param joinPointHash
     */
    private void addAroundAdvice(final Advice advice, int joinPointHash) {
        if (advice instanceof AroundAdvice) {
            AroundAdvice aroundAdvice = (AroundAdvice) advice;
            AroundAdvice[] advices;
            AroundAdvice[] olds = aw$getAroundAdvice(joinPointHash);
            if (olds != null) {
                advices = new AroundAdvice[olds.length + 1];
                System.arraycopy(olds, 0, advices, 0, olds.length);
                advices[advices.length - 1] = aroundAdvice;
            } else {
                advices = new AroundAdvice[]{aroundAdvice};
            }
            m_aroundAdvice.put(joinPointHash, advices);
        }
    }

    /**
     * @param advice
     * @param joinPointHash
     */
    private void addBeforeAdvice(final Advice advice, int joinPointHash) {
        if (advice instanceof BeforeAdvice) {
            BeforeAdvice beforeAdvice = (BeforeAdvice) advice;
            BeforeAdvice[] advices;
            BeforeAdvice[] olds = aw$getBeforeAdvice(joinPointHash);
            if (olds != null) {
                advices = new BeforeAdvice[olds.length + 1];
                System.arraycopy(olds, 0, advices, 0, olds.length);
                advices[advices.length - 1] = beforeAdvice;
            } else {
                advices = new BeforeAdvice[]{beforeAdvice};
            }
            m_beforeAdvice.put(joinPointHash, advices);
        }
    }

    /**
     * @param advice
     * @param joinPointHash
     */
    private void addAfterAdvice(final Advice advice, int joinPointHash) {
        if (advice instanceof AfterAdvice) {
            AfterAdvice afterFinallyAdvice = (AfterAdvice) advice;
            AfterAdvice[] advices;
            AfterAdvice[] olds = aw$getAfterAdvice(joinPointHash);
            if (olds != null) {
                advices = new AfterAdvice[olds.length + 1];
                System.arraycopy(olds, 0, advices, 0, olds.length);
                advices[advices.length - 1] = afterFinallyAdvice;
            } else {
                advices = new AfterAdvice[]{afterFinallyAdvice};
            }
            m_afterAdvice.put(joinPointHash, advices);
        }
    }

    /**
     * @param advice
     * @param joinPointHash
     */
    private void addAfterReturningAdvice(final Advice advice, int joinPointHash) {
        if (advice instanceof AfterReturningAdvice) {
            AfterReturningAdvice afterReturningAdvice = (AfterReturningAdvice) advice;
            AfterReturningAdvice[] advices;
            AfterReturningAdvice[] olds = aw$getAfterReturningAdvice(joinPointHash);
            if (olds != null) {
                advices = new AfterReturningAdvice[olds.length + 1];
                System.arraycopy(olds, 0, advices, 0, olds.length);
                advices[advices.length - 1] = afterReturningAdvice;
            } else {
                advices = new AfterReturningAdvice[]{afterReturningAdvice};
            }
            m_afterReturningAdvice.put(joinPointHash, advices);
        }
    }

    /**
     * @param advice
     * @param joinPointHash
     */
    private void addAfterThrowingAdvice(final Advice advice, int joinPointHash) {
        if (advice instanceof AfterThrowingAdvice) {
            AfterThrowingAdvice afterThrowingAdvice = (AfterThrowingAdvice) advice;
            AfterThrowingAdvice[] advices;
            AfterThrowingAdvice[] olds = aw$getAfterThrowingAdvice(joinPointHash);
            if (olds != null) {
                advices = new AfterThrowingAdvice[olds.length + 1];
                System.arraycopy(olds, 0, advices, 0, olds.length);
                advices[advices.length - 1] = afterThrowingAdvice;
            } else {
                advices = new AfterThrowingAdvice[]{afterThrowingAdvice};
            }
            m_afterThrowingAdvice.put(joinPointHash, advices);
        }
    }

    /**
     * @param adviceClass
     * @param joinPointHash
     */
    private void removeAroundAdvice(final Class adviceClass, int joinPointHash) {
        if (isAroundAdvice(adviceClass)) {
            AroundAdvice[] oldArray = aw$getAroundAdvice(joinPointHash);
            if (oldArray.length == 0) {
            } else if (oldArray.length == 1) {
                m_aroundAdvice.put(joinPointHash, EMPTY_AROUND_ADVICE_ARRAY);
            } else {
                List newArrayList = new ArrayList();
                for (int i = 0; i < oldArray.length; i++) {
                    AroundAdvice aroundAdvice = oldArray[i];
                    if (!aroundAdvice.getClass().equals(adviceClass)) {
                        newArrayList.add(aroundAdvice);
                    }
                }
                m_aroundAdvice.put(
                        joinPointHash,
                        (AroundAdvice[]) newArrayList.toArray(new AroundAdvice[newArrayList.size()])
                );
            }
        }
    }

    /**
     * @param adviceClass
     * @param joinPointHash
     */
    private void removeBeforeAdvice(final Class adviceClass, int joinPointHash) {
        if (isBeforeAdvice(adviceClass)) {
            BeforeAdvice[] oldArray = aw$getBeforeAdvice(joinPointHash);
            if (oldArray.length == 0) {
            } else if (oldArray.length == 1) {
                m_beforeAdvice.put(joinPointHash, EMPTY_BEFORE_ADVICE_ARRAY);
            } else {
                List newArrayList = new ArrayList();
                for (int i = 0; i < oldArray.length; i++) {
                    BeforeAdvice beforeAdvice = oldArray[i];
                    if (!beforeAdvice.getClass().equals(adviceClass)) {
                        newArrayList.add(beforeAdvice);
                    }
                }
                m_beforeAdvice.put(
                        joinPointHash,
                        (BeforeAdvice[]) newArrayList.toArray(new BeforeAdvice[newArrayList.size()])
                );
            }
        }
    }

    /**
     * @param adviceClass
     * @param joinPointHash
     */
    private void removeAfterAdvice(final Class adviceClass, int joinPointHash) {
        if (isAfterAdvice(adviceClass)) {
            AfterAdvice[] oldArray = aw$getAfterAdvice(joinPointHash);
            if (oldArray.length == 0) {
            } else if (oldArray.length == 1) {
                m_afterAdvice.put(joinPointHash, EMPTY_AFTER_ADVICE_ARRAY);
            } else {
                List newArrayList = new ArrayList();
                for (int i = 0; i < oldArray.length; i++) {
                    AfterAdvice afterAdvice = oldArray[i];
                    if (!afterAdvice.getClass().equals(adviceClass)) {
                        newArrayList.add(afterAdvice);
                    }
                }
                m_afterAdvice.put(
                        joinPointHash,
                        (AfterAdvice[]) newArrayList.toArray(new AfterAdvice[newArrayList.size()])
                );
            }
        }
    }

    /**
     * @param adviceClass
     * @param joinPointHash
     */
    private void removeAfterReturningAdvice(final Class adviceClass, int joinPointHash) {
        if (isAfterReturningAdvice(adviceClass)) {
            AfterReturningAdvice[] oldArray = aw$getAfterReturningAdvice(joinPointHash);
            if (oldArray.length == 0) {
            } else if (oldArray.length == 1) {
                m_afterReturningAdvice.put(joinPointHash, EMPTY_AFTER_RETURNING_ADVICE_ARRAY);
            } else {
                List newArrayList = new ArrayList();
                for (int i = 0; i < oldArray.length; i++) {
                    AfterReturningAdvice afterReturningAdvice = oldArray[i];
                    if (!afterReturningAdvice.getClass().equals(adviceClass)) {
                        newArrayList.add(afterReturningAdvice);
                    }
                }
                m_afterReturningAdvice.put(
                        joinPointHash,
                        (AfterReturningAdvice[]) newArrayList.toArray(new AfterReturningAdvice[newArrayList.size()])
                );
            }
        }
    }

    /**
     * @param adviceClass
     * @param joinPointHash
     */
    private void removeAfterThrowingAdvice(final Class adviceClass, int joinPointHash) {
        if (isAfterThrowingAdvice(adviceClass)) {
            AfterThrowingAdvice[] oldArray = aw$getAfterThrowingAdvice(joinPointHash);
            if (oldArray.length == 0) {
            } else if (oldArray.length == 1) {
                m_afterThrowingAdvice.put(joinPointHash, EMPTY_AFTER_THROWING_ADVICE_ARRAY);
            } else {
                List newArrayList = new ArrayList();
                for (int i = 0; i < oldArray.length; i++) {
                    AfterThrowingAdvice advice = oldArray[i];
                    if (!advice.getClass().equals(adviceClass)) {
                        newArrayList.add(advice);
                    }
                }
                m_afterThrowingAdvice.put(
                        joinPointHash,
                        (AfterThrowingAdvice[]) newArrayList.toArray(new AfterThrowingAdvice[newArrayList.size()])
                );
            }
        }
    }

    private boolean isAroundAdvice(final Class adviceClass) {
        if (adviceClass == AroundAdvice.class) {
            return true;
        }
        Class[] interfaces = adviceClass.getInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            Class anInterface = interfaces[i];
            if (anInterface == AroundAdvice.class) {
                return true;
            }
        }
        return false;
    }

    private boolean isBeforeAdvice(final Class adviceClass) {
        if (adviceClass == BeforeAdvice.class) {
            return true;
        }
        Class[] interfaces = adviceClass.getInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            Class anInterface = interfaces[i];
            if (anInterface == BeforeAdvice.class) {
                return true;
            }
        }
        return false;
    }

    private boolean isAfterAdvice(final Class adviceClass) {
        if (adviceClass == AfterAdvice.class) {
            return true;
        }
        Class[] interfaces = adviceClass.getInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            Class anInterface = interfaces[i];
            if (anInterface == AfterAdvice.class) {
                return true;
            }
        }
        return false;
    }

    private boolean isAfterReturningAdvice(final Class adviceClass) {
        if (adviceClass == AfterReturningAdvice.class) {
            return true;
        }
        Class[] interfaces = adviceClass.getInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            Class anInterface = interfaces[i];
            if (anInterface == AfterReturningAdvice.class) {
                return true;
            }
        }
        return false;
    }

    private boolean isAfterThrowingAdvice(final Class adviceClass) {
        if (adviceClass == AfterThrowingAdvice.class) {
            return true;
        }
        Class[] interfaces = adviceClass.getInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            Class anInterface = interfaces[i];
            if (anInterface == AfterThrowingAdvice.class) {
                return true;
            }
        }
        return false;
    }

    /**
     * Match the given expression for the given pointcut type against the given emittedJoinPoint
     *
     * @param expression
     * @param pointcutType
     * @param emittedJoinPoint
     * @return
     */
    private boolean match(ExpressionInfo expression, PointcutType pointcutType, EmittedJoinPoint emittedJoinPoint) {
        ClassInfo callerClassInfo = JavaClassInfo.getClassInfo(m_targetInstance.getClass());
        ClassInfo calleeClassInfo = AsmClassInfo.getClassInfo(emittedJoinPoint.getCalleeClassName(), m_targetInstance.getClass().getClassLoader());

        // early match
        if (!expression.getAdvisedClassFilterExpression().match(new ExpressionContext(pointcutType, calleeClassInfo, callerClassInfo))) {
            return false;
        }

        // create the callee info
        final ReflectionInfo reflectionInfo;
        final PointcutType joinPointType;
        switch (emittedJoinPoint.getJoinPointType()) {
            case JoinPointType.STATIC_INITIALIZATION_INT:
                reflectionInfo = calleeClassInfo.staticInitializer();
                joinPointType = PointcutType.STATIC_INITIALIZATION;
                break;
            case JoinPointType.METHOD_EXECUTION_INT:
                reflectionInfo = calleeClassInfo.getMethod(emittedJoinPoint.getJoinPointHash());
                joinPointType = PointcutType.EXECUTION;
                break;
            case JoinPointType.METHOD_CALL_INT:
                reflectionInfo = calleeClassInfo.getMethod(emittedJoinPoint.getJoinPointHash());
                joinPointType = PointcutType.CALL;
                break;
            case JoinPointType.FIELD_GET_INT:
                reflectionInfo = calleeClassInfo.getField(emittedJoinPoint.getJoinPointHash());
                joinPointType = PointcutType.GET;
                break;
            case JoinPointType.FIELD_SET_INT:
                reflectionInfo = calleeClassInfo.getField(emittedJoinPoint.getJoinPointHash());
                joinPointType = PointcutType.SET;
                break;
            case JoinPointType.CONSTRUCTOR_EXECUTION_INT:
                reflectionInfo = calleeClassInfo.getConstructor(emittedJoinPoint.getJoinPointHash());
                joinPointType = PointcutType.EXECUTION;
                break;
            case JoinPointType.CONSTRUCTOR_CALL_INT:
                reflectionInfo = calleeClassInfo.getConstructor(emittedJoinPoint.getJoinPointHash());
                joinPointType = PointcutType.CALL;
                break;
            case JoinPointType.HANDLER_INT:
                reflectionInfo = calleeClassInfo;
                joinPointType = PointcutType.HANDLER;
                break;
            default:
                throw new RuntimeException("Joinpoint type not supported: " + emittedJoinPoint.getJoinPointType());
        }

        // create the caller info
        final ReflectionInfo withinInfo;
        if (TransformationConstants.CLINIT_METHOD_NAME.equals(emittedJoinPoint.getCallerMethodName())) {
            withinInfo = callerClassInfo.staticInitializer();
        } else if (TransformationConstants.INIT_METHOD_NAME.equals(emittedJoinPoint.getCallerMethodName())) {
            withinInfo = callerClassInfo.getConstructor(AsmHelper.calculateConstructorHash(
                    emittedJoinPoint.getCallerMethodDesc()
            ));
        } else {
            withinInfo =
                    callerClassInfo.getMethod(AsmHelper.calculateMethodHash(emittedJoinPoint.getCallerMethodName(),
                                                                            emittedJoinPoint.getCallerMethodDesc())
                    );
        }

        // check pointcutType vs joinPointType
        if (pointcutType != PointcutType.WITHIN && pointcutType != joinPointType) {
            return false;
        }

        return expression.getExpression().match(new ExpressionContext(pointcutType, reflectionInfo, withinInfo));
    }
}
