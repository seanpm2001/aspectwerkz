/**************************************************************************************
 * Copyright (c) Jonas Bon�r, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.transform.inlining.weaver;

import java.util.Iterator;
import java.util.Set;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.objectweb.asm.*;
import org.codehaus.aspectwerkz.transform.Context;
import org.codehaus.aspectwerkz.transform.TransformationConstants;
import org.codehaus.aspectwerkz.transform.inlining.ContextImpl;
import org.codehaus.aspectwerkz.transform.inlining.AsmHelper;
import org.codehaus.aspectwerkz.definition.SystemDefinition;
import org.codehaus.aspectwerkz.definition.MixinDefinition;
import org.codehaus.aspectwerkz.expression.ExpressionContext;
import org.codehaus.aspectwerkz.expression.PointcutType;
import org.codehaus.aspectwerkz.reflect.ClassInfo;
import org.codehaus.aspectwerkz.reflect.MethodInfo;
import org.codehaus.aspectwerkz.DeploymentModel;

/**
 * Adds mixin methods and fields to hold mixin instances to the target class.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bon�r </a>
 */
public class AddMixinMethodsVisitor extends ClassAdapter implements TransformationConstants {

    private final ContextImpl m_ctx;
    private String m_declaringTypeName;
    private final ClassInfo m_classInfo;
    private final Set m_addedMethods;
    private ExpressionContext m_expressionContext;
    private boolean m_hasClinit = false;
    private final Map m_mixinFields = new HashMap();

    /**
     * Creates a new class adapter.
     *
     * @param cv
     * @param classInfo
     * @param ctx
     * @param addedMethods
     */
    public AddMixinMethodsVisitor(final ClassVisitor cv,
                                  final ClassInfo classInfo,
                                  final Context ctx,
                                  final Set addedMethods) {
        super(cv);
        m_classInfo = classInfo;
        m_ctx = (ContextImpl) ctx;
        m_addedMethods = addedMethods;
        m_expressionContext = new ExpressionContext(PointcutType.WITHIN, m_classInfo, m_classInfo);
    }

    /**
     * Visits the class.
     *
     * @param access
     * @param name
     * @param superName
     * @param interfaces
     * @param sourceFile
     */
    public void visit(final int version,
                      final int access,
                      final String name,
                      final String superName,
                      final String[] interfaces,
                      final String sourceFile) {
        m_declaringTypeName = name;
        addMixinFields();
        addMixinMethods();
        super.visit(version, access, name, superName, interfaces, sourceFile);
    }

    /**
     * Adds mixin fields to the target class.
     */
    private void addMixinFields() {
        int index = 0;
        for (Iterator it = m_ctx.getDefinitions().iterator(); it.hasNext();) {
            List mixinDefs = ((SystemDefinition) it.next()).getMixinDefinitions(m_expressionContext);
            for (Iterator it2 = mixinDefs.iterator(); it2.hasNext();) {
                final MixinDefinition mixinDef = (MixinDefinition) it2.next();
                final String mixinClassName = mixinDef.getClassName().replace('.', '/');
                final int deploymentModel = DeploymentModel.getDeploymentModelAsInt(mixinDef.getDeploymentModel());

                for (Iterator it3 = mixinDef.getInterfaces().iterator(); it3.hasNext();) {
                    ClassInfo mixinInterfaceClassInfo = (ClassInfo) it3.next();
                    if (m_mixinFields.containsKey(mixinInterfaceClassInfo)) {
                        continue;
                    }
                    final String fieldName = MIXIN_FIELD_NAME + index;
                    MixinFieldInfo fieldInfo = new MixinFieldInfo();
                    fieldInfo.fieldName = fieldName;
                    fieldInfo.mixinImplClassName = mixinClassName;
                    fieldInfo.mixinInterfaceClassInfo = mixinInterfaceClassInfo;
                    final String signature = mixinInterfaceClassInfo.getSignature();

                    if (deploymentModel == DeploymentModel.PER_CLASS) {
                        fieldInfo.isStatic = true;
                        cv.visitField(
                                ACC_PRIVATE + ACC_FINAL + ACC_STATIC,
                                fieldName,
                                signature,
                                null, null
                        );
                    } else if (deploymentModel == DeploymentModel.PER_INSTANCE) {
                        fieldInfo.isStatic = false;
                        cv.visitField(
                                ACC_PRIVATE + ACC_FINAL,
                                fieldName,
                                signature,
                                null, null
                        );
                    }
                    m_mixinFields.put(mixinInterfaceClassInfo, fieldInfo);
                    index++;
                }
            }
        }
    }

    /**
     * Adds mixin methods to the target class.
     */
    private void addMixinMethods() {
        for (Iterator it = m_ctx.getDefinitions().iterator(); it.hasNext();) {
            List mixinDefs = ((SystemDefinition) it.next()).getMixinDefinitions(m_expressionContext);
            for (Iterator it2 = mixinDefs.iterator(); it2.hasNext();) {
                final MixinDefinition mixinDef = (MixinDefinition) it2.next();
                final String mixinClassName = mixinDef.getClassName().replace('.', '/');
                final String mixinClassSignature = L + mixinClassName + SEMICOLON;

                for (Iterator it3 = mixinDef.getMethodsToIntroduce().iterator(); it3.hasNext();) {
                    MethodInfo methodInfo = (MethodInfo) it3.next();
                    final String methodName = methodInfo.getName();
                    final String methodSignature = methodInfo.getSignature();

                    MixinFieldInfo mixinFieldInfo = null;
                    for (Iterator i4 = m_mixinFields.values().iterator(); i4.hasNext();) {
                        if (mixinFieldInfo != null) {
                            break;
                        }
                        MixinFieldInfo fieldInfo = (MixinFieldInfo) i4.next();
                        MethodInfo[] methods = fieldInfo.mixinInterfaceClassInfo.getMethods();
                        for (int i = 0; i < methods.length; i++) {
                            MethodInfo method = methods[i];
                            if (method.getName().equals(methodName) &&
                                method.getSignature().equals(methodSignature)) {
                                mixinFieldInfo = fieldInfo;
                                break;
                            }
                        }
                    }
                    if (mixinFieldInfo == null) {
                        continue;
                    }
                    if (m_addedMethods.contains(AlreadyAddedMethodVisitor.getMethodKey(methodName, methodSignature))) {
                        continue;
                    }
                    CodeVisitor mv = cv.visitMethod(
                            ACC_PUBLIC | ACC_FINAL,
                            methodName,
                            methodSignature,
                            null,
                            null
                    );
                    if (mixinFieldInfo.isStatic) {
                        mv.visitFieldInsn(
                                GETSTATIC,
                                m_declaringTypeName,
                                mixinFieldInfo.fieldName,
                                mixinFieldInfo.mixinInterfaceClassInfo.getSignature()
                        );
                    } else {
                        mv.visitVarInsn(ALOAD, 0);
                        mv.visitFieldInsn(
                                GETFIELD,
                                m_declaringTypeName,
                                mixinFieldInfo.fieldName,
                                mixinFieldInfo.mixinInterfaceClassInfo.getSignature()
                        );
                    }
                    AsmHelper.loadArgumentTypes(mv, Type.getArgumentTypes(methodSignature), false);
                    mv.visitMethodInsn(
                            INVOKEINTERFACE,
                            mixinFieldInfo.mixinInterfaceClassInfo.getName().replace('.', '/'),
                            methodName,
                            methodSignature
                    );
                    AsmHelper.addReturnStatement(mv, Type.getReturnType(methodSignature));
                    mv.visitMaxs(0, 0);
                }
            }
        }
    }

    /**
     * Appends mixin instantiation to the clinit method and/or init method.
     *
     * @param access
     * @param name
     * @param desc
     * @param exceptions
     * @param attrs
     * @return
     */
    public CodeVisitor visitMethod(final int access,
                                   final String name,
                                   final String desc,
                                   final String[] exceptions,
                                   final Attribute attrs) {
        if (name.equals(CLINIT_METHOD_NAME)) {
            m_hasClinit = true;
            CodeVisitor mv = new PrependToClinitMethodCodeAdapter(
                    cv.visitMethod(access, name, desc, exceptions, attrs)
            );
            mv.visitMaxs(0, 0);
            return mv;
        } else if (name.equals(INIT_METHOD_NAME)) {
            CodeVisitor mv = new AppendToInitMethodCodeAdapter(
                    cv.visitMethod(access, name, desc, exceptions, attrs)
            );
            mv.visitMaxs(0, 0);
            return mv;
        }
        return super.visitMethod(access, name, desc, exceptions, attrs);
    }

    /**
     * Creates a new clinit method and adds mixin instantiation if it does not exist.
     */
    public void visitEnd() {
        if (!m_hasClinit) {
            // add the <clinit> method
            CodeVisitor mv = cv.visitMethod(
                    ACC_STATIC, CLINIT_METHOD_NAME, NO_PARAM_RETURN_VOID_SIGNATURE, null, null
            );
            for (Iterator i4 = m_mixinFields.values().iterator(); i4.hasNext();) {
                MixinFieldInfo fieldInfo = (MixinFieldInfo) i4.next();
                if (fieldInfo.isStatic) {
                    initializeStaticMixinField(mv, fieldInfo);
                }
            }

            mv.visitInsn(RETURN);
            mv.visitMaxs(0, 0);
        }
        super.visitEnd();
    }

    /**
     * Initializes a static mixin field.
     *
     * @param mv
     * @param fieldInfo
     */
    private void initializeStaticMixinField(final CodeVisitor mv, final MixinFieldInfo fieldInfo) {
        mv.visitLdcInsn(fieldInfo.mixinImplClassName.replace('/', '.'));
        mv.visitFieldInsn(
                GETSTATIC,
                m_declaringTypeName,
                TARGET_CLASS_FIELD_NAME,
                CLASS_CLASS_SIGNATURE
        );
        mv.visitMethodInsn(
                INVOKESTATIC,
                MIXINS_CLASS_NAME,
                MIXIN_OF_METHOD_NAME,
                MIXIN_OF_METHOD_PER_CLASS_SIGNATURE
        );
        mv.visitTypeInsn(CHECKCAST, fieldInfo.mixinInterfaceClassInfo.getName().replace('.', '/'));
        mv.visitFieldInsn(
                PUTSTATIC,
                m_declaringTypeName,
                fieldInfo.fieldName,
                fieldInfo.mixinInterfaceClassInfo.getSignature()
        );
    }

    /**
     * Initializes a member mixin field.
     *
     * @param mv
     * @param fieldInfo
     */
    private void initializeMemberMixinField(final CodeVisitor mv, final MixinFieldInfo fieldInfo) {
        mv.visitVarInsn(ALOAD, 0);
        mv.visitLdcInsn(fieldInfo.mixinImplClassName.replace('/', '.'));
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(
                INVOKESTATIC,
                MIXINS_CLASS_NAME,
                MIXIN_OF_METHOD_NAME,
                MIXIN_OF_METHOD_PER_INSTANCE_SIGNATURE
        );
        mv.visitTypeInsn(CHECKCAST, fieldInfo.mixinInterfaceClassInfo.getName().replace('.', '/'));
        mv.visitFieldInsn(
                PUTFIELD,
                m_declaringTypeName,
                fieldInfo.fieldName,
                fieldInfo.mixinInterfaceClassInfo.getSignature()
        );
    }

    /**
     * Filters the classes to be transformed.
     *
     * @param classInfo  the class to filter
     * @param ctx        the context
     * @param definition the definition
     * @return boolean true if the method should be filtered away
     */
    public static boolean classFilter(final ClassInfo classInfo,
                                      final ExpressionContext ctx,
                                      final SystemDefinition definition) {
        if (classInfo.isInterface()) {
            return true;
        }
        String className = classInfo.getName().replace('/', '.');
        if (definition.inExcludePackage(className)) {
            return true;
        }
        if (!definition.inIncludePackage(className)) {
            return true;
        }
        if (definition.hasMixin(ctx)) {
            return false;
        }
        return true;
    }

    /**
     * Adds initialization of static mixin fields to the beginning of the clinit method.
     *
     * @author <a href="mailto:jboner@codehaus.org">Jonas Bon�r </a>
     */
    public class PrependToClinitMethodCodeAdapter extends CodeAdapter {

        public PrependToClinitMethodCodeAdapter(final CodeVisitor ca) {
            super(ca);
            for (Iterator i4 = m_mixinFields.values().iterator(); i4.hasNext();) {
                MixinFieldInfo fieldInfo = (MixinFieldInfo) i4.next();
                if (fieldInfo.isStatic) {
                    initializeStaticMixinField(ca, fieldInfo);
                }
            }
        }
    }

    /**
     * Adds initialization of member mixin fields to end of the init method.
     *
     * @author <a href="mailto:jboner@codehaus.org">Jonas Bon�r </a>
     */
    public class AppendToInitMethodCodeAdapter extends CodeAdapter {

        public AppendToInitMethodCodeAdapter(final CodeVisitor ca) {
            super(ca);
        }

        public void visitInsn(final int opcode) {
            if (opcode == RETURN) {
                for (Iterator i4 = m_mixinFields.values().iterator(); i4.hasNext();) {
                    MixinFieldInfo fieldInfo = (MixinFieldInfo) i4.next();
                    if (!fieldInfo.isStatic) {
                        initializeMemberMixinField(cv, fieldInfo);
                    }
                }
            }
            super.visitInsn(opcode);
        }
    }

    private static class MixinFieldInfo {
        private String fieldName;
        private String mixinImplClassName;
        private ClassInfo mixinInterfaceClassInfo;
        private boolean isStatic;
    }
}