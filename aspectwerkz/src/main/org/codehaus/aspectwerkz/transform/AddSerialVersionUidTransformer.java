/**************************************************************************************
 * Copyright (c) The AspectWerkz Team. All rights reserved.                           *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD style license *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.transform;

import java.util.List;
import java.util.Iterator;

import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.FieldGen;
import org.apache.bcel.generic.Type;
import org.apache.bcel.Constants;

import org.codehaus.aspectwerkz.metadata.WeaveModel;

/**
 * Adds a new serialVersionUID to the class (if the class is serializable and does not
 * have a UID already defined).
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bon�r</a>
 */
public class AddSerialVersionUidTransformer extends AspectWerkzAbstractInterfaceTransformer {

    /**
     * Holds the weave model.
     */
    private final WeaveModel m_weaveModel;

    /**
     * Retrieves the weave model.
     */
    public AddSerialVersionUidTransformer() {
        super();
        List weaveModels = WeaveModel.loadModels();
        if (weaveModels.isEmpty()) {
            throw new RuntimeException("no weave model (online) or no classes to transform (offline) is specified");
        }
        if (weaveModels.size() > 1) {
            throw new RuntimeException("more than one weave model is specified, if you need more that one weave model you currently have to use the -offline mode and put each weave model on the classpath");
        }
        else {
            m_weaveModel = (WeaveModel)weaveModels.get(0);
        }
    }

    /**
     * Adds a UUID to all the transformed classes.
     *
     * @param es the extension set
     * @param cs the unextendable class set
     */
    public void transformInterface(final AspectWerkzExtensionSet es, final AspectWerkzUnextendableClassSet cs) {
        Iterator it = cs.getIteratorForTransformableClasses();
        while (it.hasNext()) {

            final ClassGen cg = (ClassGen)it.next();
            if (classFilter(cg)) {
                continue;
            }
            if (!TransformationUtil.isSerializable(cg)) {
                return;
            }
            if (TransformationUtil.hasSerialVersionUid(cg)) {
                return;
            }

            addSerialVersionUidField(cg, es);
        }
    }

    /**
     * Adds a new serialVersionUID to the class (if the class is serializable and does not
     * have a UID already defined).
     *
     * @param cg the class gen
     * @param es the extension set
     */
    private void addSerialVersionUidField(final ClassGen cg, final AspectWerkzExtensionSet es) {
        FieldGen field = new FieldGen(
                Constants.ACC_FINAL | Constants.ACC_STATIC,
                Type.LONG,
                TransformationUtil.SERIAL_VERSION_UID_FIELD,
                cg.getConstantPool());
        final long uid = TransformationUtil.calculateSerialVersionUid(cg);
        field.setInitValue(uid);
        es.addField(cg, field.getField());
    }

    /**
     * Filters the classes to be transformed.
     *
     * @param cg the class to filter
     * @return boolean true if the method should be filtered away
     */
    private boolean classFilter(final ClassGen cg) {
        if (cg.isInterface()) {
            return true;
        }
        if (m_weaveModel.inTransformationScope(cg.getClassName())) {
            return false;
        }
        return true;
    }

    /**
     * JMangler callback method. Is being called before each transformation.
     */
    public void sessionStart() {
    }

    /**
     * JMangler callback method. Is being called after each transformation.
     */
    public void sessionEnd() {
    }

    /**
     * Logs a message.
     *
     * @return the log message
     */
    public String verboseMessage() {
        return getClass().getName();
    }
}
