/**************************************************************************************
 * Copyright (c) Jonas Bon�r, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.transform;

import org.codehaus.aspectwerkz.definition.InterfaceIntroductionDefinition;
import org.codehaus.aspectwerkz.definition.SystemDefinition;
import org.codehaus.aspectwerkz.definition.SystemDefinitionContainer;
import org.codehaus.aspectwerkz.exception.WrappedRuntimeException;
import org.codehaus.aspectwerkz.expression.ExpressionContext;
import org.codehaus.aspectwerkz.expression.PointcutType;
import org.codehaus.aspectwerkz.reflect.ClassInfo;
import org.codehaus.aspectwerkz.reflect.impl.javassist.JavassistClassInfo;
import java.util.Iterator;
import java.util.List;
import javassist.CtClass;
import javassist.NotFoundException;

/**
 * Adds an interfaces to classes.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bon�r</a>
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur</a>
 */
public final class AddInterfaceTransformer implements Transformer {
    /**
     * Adds an interfaces to the classes specified.
     *
     * @param context the transformation context
     * @param klass   the class
     */
    public void transform(final Context context, final Klass klass) {
        List definitions = SystemDefinitionContainer.getDefinitionsContext();

        // loop over all the definitions
        for (Iterator it = definitions.iterator(); it.hasNext();) {
            SystemDefinition definition = (SystemDefinition)it.next();
            final CtClass ctClass = klass.getCtClass();
            ClassInfo classInfo = new JavassistClassInfo(ctClass, context.getLoader());
            ExpressionContext ctx = new ExpressionContext(PointcutType.ANY, classInfo, classInfo);
            if (classFilter(ctClass, ctx, definition)) {
                continue;
            }
            addInterfaceIntroductions(definition, ctClass, context, ctx);
        }
    }

    /**
     * Adds the interface introductions to the class.
     *
     * @param definition the definition
     * @param cg         the class gen
     * @param context    the TF context
     * @param ctx        the context
     */
    private void addInterfaceIntroductions(final SystemDefinition definition, final CtClass cg, final Context context,
                                           final ExpressionContext ctx) {
        boolean isClassAdvised = false;
        List introDefs = definition.getInterfaceIntroductions(ctx);
        for (Iterator it = introDefs.iterator(); it.hasNext();) {
            InterfaceIntroductionDefinition introductionDef = (InterfaceIntroductionDefinition)it.next();
            List interfaceClassNames = introductionDef.getInterfaceClassNames();
            for (Iterator iit = interfaceClassNames.iterator(); iit.hasNext();) {
                String className = (String)iit.next();
                if (implementsInterface(cg, className)) {
                    continue;
                }
                if (className != null) {
                    try {
                        cg.addInterface(cg.getClassPool().get(className));
                    } catch (NotFoundException e) {
                        throw new WrappedRuntimeException(e);
                    }
                    isClassAdvised = true;
                }
            }
        }
        if (isClassAdvised) {
            context.markAsAdvised();
        }
    }

    /**
     * Checks if a class implements an interface.
     *
     * @param ctClass ConstantUtf8 constant
     * @return true if the class implements the interface
     */
    private boolean implementsInterface(final CtClass ctClass, final String interfaceName) {
        try {
            CtClass[] interfaces = ctClass.getInterfaces();
            for (int i = 0; i < interfaces.length; i++) {
                if (interfaces[i].getName().replace('/', '.').equals(interfaceName)) {
                    return true;
                }
            }
            return false;
        } catch (NotFoundException e) {
            throw new WrappedRuntimeException(e);
        }
    }

    /**
     * Filters the classes to be transformed.
     *
     * @param cg         the class to filter
     * @param ctx        the context
     * @param definition the definition
     * @return boolean true if the method should be filtered away
     */
    private boolean classFilter(final CtClass cg, final ExpressionContext ctx, final SystemDefinition definition) {
        if (cg.isInterface()) {
            return true;
        }
        String className = cg.getName().replace('/', '.');
        if (definition.inExcludePackage(className)) {
            return true;
        }
        if (definition.inIncludePackage(className) && definition.isIntroduced(ctx)) {
            return false;
        }
        return true;
    }

    /**
     * Callback method. Is being called before each transformation.
     */
    public void sessionStart() {
    }

    /**
     * Callback method. Is being called after each transformation.
     */
    public void sessionEnd() {
    }

    /**
     * Callback method. Prints a log/status message at each transformation.
     *
     * @return a log string
     */
    public String verboseMessage() {
        return this.getClass().getName();
    }
}
