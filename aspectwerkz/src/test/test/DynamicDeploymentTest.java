/**************************************************************************************
 * Copyright (c) The AspectWerkz Team. All rights reserved.                           *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD style license *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test;

import java.util.List;

import junit.framework.TestCase;
import org.codehaus.aspectwerkz.AspectWerkz;
import org.codehaus.aspectwerkz.pointcut.MethodPointcut;
import org.codehaus.aspectwerkz.metadata.MethodMetaData;
import org.codehaus.aspectwerkz.metadata.ClassMetaData;
import org.codehaus.aspectwerkz.metadata.ReflectionMetaDataMaker;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bon�r</a>
 */
public class DynamicDeploymentTest extends TestCase implements Loggable {

    private String m_logString = "";
    private ClassMetaData m_classMetaData = ReflectionMetaDataMaker.createClassMetaData(DynamicDeploymentTest.class);

//    public void testReorderAdvicesAtRuntime() {
//        m_logString = "";
//        reorderAdvicesTestMethod();
//        assertEquals("before1 before2 before2 invocation after2 after2 after1 ", m_logString);
//
//        MethodMetaData methodMetaData = new MethodMetaData();
//        methodMetaData.setName("reorderAdvicesTestMethod");
//        methodMetaData.setParameterTypes(new String[]{});
//        methodMetaData.setReturnType("void");
//        methodMetaData.setExceptionTypes(new String[]{});
//
//        List advices = ((MethodPointcut)AspectWerkz.getSystem("tests").getAspect("DynamicDeploymentTest").
//                getMethodPointcuts(m_classMetaData, methodMetaData).get(0)).
//                getAdviceIndexTuples();
//        NameIndexTuple tuple1 = (NameIndexTuple)advices.get(0);
//        NameIndexTuple tuple2 = (NameIndexTuple)advices.get(1);
//        advices.set(0, tuple2);
//        advices.set(1, tuple1);
//        ((MethodPointcut)AspectWerkz.getSystem("tests").getAspect("DynamicDeploymentTest").
//                getMethodPointcuts(m_classMetaData, methodMetaData).get(0)).
//                setAdviceIndexTuples(advices);
//
//        m_logString = "";
//        reorderAdvicesTestMethod();
//        assertEquals("before2 before1 before2 invocation after2 after1 after2 ", m_logString);
//    }

//    public void testCreateAdviceAtRuntime() {
//        try {
//            // create the new advice
//            AspectWerkz.getSystem("tests").createAdvice("createTransientAdviceTest",
//                    "test.DynamicallyCreatedTransientAdvice", "perInstance", null);
//
//            // test the easy stuff
//            assertNotNull(AspectWerkz.getSystem("tests").getAdvice("createTransientAdviceTest"));
//            assertEquals(DeploymentModel.getDeploymentModelAsInt("perInstance"),
//                    AspectWerkz.getSystem("tests").getAdvice("createTransientAdviceTest").
//                    getDeploymentModel());
//            assertEquals("createTransientAdviceTest", AspectWerkz.getSystem("tests").
//                    getAdvice("createTransientAdviceTest").getName());
//
//            // test it in action
//            MethodMetaData methodMetaData = new MethodMetaData();
//            methodMetaData.setName("createTransientAdviceTestMethod");
//            methodMetaData.setParameterTypes(new String[]{});
//            methodMetaData.setReturnType("void");
//            methodMetaData.setExceptionTypes(new String[]{});
//
//            ((MethodPointcut)AspectWerkz.getSystem("tests").getAspect("DynamicDeploymentTest").
//                    getMethodPointcuts(m_classMetaData, methodMetaData).
//                    get(0)).addAdvice("createTransientAdviceTest");
//
//            m_logString = "";
//            createTransientAdviceTestMethod();
//            assertEquals("before invocation after ", m_logString);
//        }
//        catch (Exception e) {
//            fail();
//        }
//    }

    public void testAddAdviceAtRuntime() {
        m_logString = "";
        addAdviceTestMethod();
        assertEquals("before1 invocation after1 ", m_logString);

        MethodMetaData methodMetaData = new MethodMetaData();
        methodMetaData.setName("addAdviceTestMethod");
        methodMetaData.setParameterTypes(new String[]{});
        methodMetaData.setReturnType("void");
        methodMetaData.setExceptionTypes(new String[]{});

        ((MethodPointcut)AspectWerkz.getSystem("tests").getAspectMetaData("DynamicDeploymentTest").
                getMethodPointcuts(m_classMetaData, methodMetaData).get(0)).addAdvice("methodAdvice3");

        m_logString = "";
        addAdviceTestMethod();
        assertEquals("before1 before2 invocation after2 after1 ", m_logString);
    }

    public void testRemoveAdviceAtRuntime() {
        m_logString = "";
        removeAdviceTestMethod();
        assertEquals("before2 before1 before2 invocation after2 after1 after2 ", m_logString);

        MethodMetaData methodMetaData = new MethodMetaData();
        methodMetaData.setName("removeAdviceTestMethod");
        methodMetaData.setParameterTypes(new String[]{});
        methodMetaData.setReturnType("void");
        methodMetaData.setExceptionTypes(new String[]{});
        ((MethodPointcut)AspectWerkz.getSystem("tests").getAspectMetaData("DynamicDeploymentTest").
                getMethodPointcuts(m_classMetaData, methodMetaData).get(0)).
                removeAdvice("methodAdvice2");

        m_logString = "";
        removeAdviceTestMethod();
        assertEquals("before2 before2 invocation after2 after2 ", m_logString);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(DynamicDeploymentTest.class);
    }

    public DynamicDeploymentTest(String name) {
        super(name);
        AspectWerkz.getSystem("tests").initialize();
    }

    public void log(final String wasHere) {
        m_logString += wasHere;
    }

    public void reorderAdvicesTestMethod() {
        log("invocation ");
    }

    public void removeAdviceTestMethod() {
        log("invocation ");
    }

    public void addAdviceTestMethod() {
        log("invocation ");
    }

    public void createTransientAdviceTestMethod() {
        log("invocation ");
    }

    public void createPersistentAdviceTestMethod() {
    }
}
