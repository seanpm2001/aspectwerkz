package test;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

import org.codehaus.aspectwerkz.AspectWerkz;
import org.codehaus.aspectwerkz.Aspect;
import org.codehaus.aspectwerkz.DeploymentModel;
import org.codehaus.aspectwerkz.introduction.Introduction;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.advice.Advice;
import org.codehaus.aspectwerkz.advice.PreAdvice;

/**
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bon�r</a>
 * @version $Id: AspectWerkzTest.java,v 1.4 2003-06-19 17:45:23 jboner Exp $
 */
public class AspectWerkzTest extends TestCase {

    public void testSetDeploymentModelForAdvice() {
        assertEquals(DeploymentModel.PER_JVM, AspectWerkz.getSystem("tests").getAdvice("methodAdvice1").getDeploymentModel());
        AspectWerkz.getSystem("tests").getAdvice("methodAdvice1").setDeploymentModel(DeploymentModel.PER_CLASS);
        assertEquals(DeploymentModel.PER_CLASS, AspectWerkz.getSystem("tests").getAdvice("methodAdvice1").getDeploymentModel());
    }

    public void testRegisterAspect() {
        AspectWerkz.getSystem("tests").register(new Aspect(getClass().getName()));
        Collection aspects = AspectWerkz.getSystem("tests").getAspects();
        for (Iterator it = aspects.iterator(); it.hasNext();) {
            Aspect aspect = (Aspect)it.next();
            if (aspect.getName().equals(getClass().getName())) {
                return;
            }
        }
        fail();
    }

    public void testRegisterAdvice() {
        Advice advice = new PreAdvice() {
            public void execute(final JoinPoint joinPoint) {
            }
        };
        AspectWerkz.getSystem("tests").register("testRegisterAdvice", advice);
        assertNotNull(AspectWerkz.getSystem("tests").getAdvice("testRegisterAdvice"));
    }

    public void testFindAdviceByIndex() {
        Advice advice = new PreAdvice() {
            public void execute(final JoinPoint joinPoint) {
            }
        };
        AspectWerkz.getSystem("tests").register("testFindAdviceByIndex", advice);
        int index = AspectWerkz.getSystem("tests").getAdviceIndexFor("testFindAdviceByIndex");
        assertEquals(AspectWerkz.getSystem("tests").getAdvice("testFindAdviceByIndex"), AspectWerkz.getSystem("tests").getAdvice(index));
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(AspectWerkzTest.class);
    }

    public AspectWerkzTest(String name) {
        super(name);
        AspectWerkz.getSystem("tests").initialize();
    }
}
