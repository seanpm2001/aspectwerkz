/**************************************************************************************
 * Copyright (c) Jonas Bon�r, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.reflect.impl.asm;

import gnu.trove.TIntObjectHashMap;

import org.codehaus.aspectwerkz.annotation.Annotation;
import org.codehaus.aspectwerkz.annotation.instrumentation.asm.CustomAttribute;
import org.codehaus.aspectwerkz.exception.WrappedRuntimeException;
import org.codehaus.aspectwerkz.reflect.ClassInfo;
import org.codehaus.aspectwerkz.reflect.ConstructorInfo;
import org.codehaus.aspectwerkz.reflect.FieldInfo;
import org.codehaus.aspectwerkz.reflect.MethodInfo;
import org.codehaus.aspectwerkz.transform.inlining.AsmHelper;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.CodeVisitor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the ClassInfo interface utilizing the ASM bytecode library for the info retriaval.
 * 
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bon�r </a>
 */
public class AsmClassInfo implements ClassInfo {

    /**
     * The bytecode for the class.
     */
    private final byte[] m_bytecode;

    /**
     * The class loader.
     */
    private final ClassLoader m_loader;

    /**
     * The name of the class.
     */
    private String m_name;

    /**
     * The modifiers.
     */
    private int m_modifiers;

    /**
     * Is the class an interface.
     */
    private boolean m_isInterface = false;

    /**
     * Is the class a primitive type.
     */
    private boolean m_isPrimitive = false;

    /**
     * Is the class of type array.
     */
    private boolean m_isArray = false;

    /**
     * A list with the <code>ConstructorInfo</code> instances.
     */
    private final TIntObjectHashMap m_constructors = new TIntObjectHashMap();

    /**
     * A list with the <code>MethodInfo</code> instances.
     */
    private final TIntObjectHashMap m_methods = new TIntObjectHashMap();

    /**
     * A list with the <code>FieldInfo</code> instances.
     */
    private final TIntObjectHashMap m_fields = new TIntObjectHashMap();

    /**
     * A list with the interfaces class names.
     */
    private String[] m_interfaceClassNames = null;

    /**
     * A list with the interfaces.
     */
    private ClassInfo[] m_interfaces = null;

    /**
     * The super class name.
     */
    private String m_superClassName = null;

    /**
     * The super class.
     */
    private ClassInfo m_superClass = null;

    /**
     * The annotations.
     */
    private List m_annotations = new ArrayList();

    /**
     * The component type if array type.
     */
    private ClassInfo m_componentType = null;

    /**
     * The class info repository.
     */
    private final AsmClassInfoRepository m_classInfoRepository;

    /**
     * Creates a new ClassInfo instance.
     * 
     * TODO switch access back to private 
     * 
     * @param className
     * @param loader
     */
    public AsmClassInfo(final byte[] bytecode, final ClassLoader loader) {
        if (bytecode == null) {
            throw new IllegalArgumentException("bytecode can not be null");
        }
        m_bytecode = bytecode;
        m_loader = loader;
        m_classInfoRepository = AsmClassInfoRepository.getRepository(loader);
        try {
            ClassReader cr = new ClassReader(bytecode);
            ClassWriter cw = new ClassWriter(true);
            ClassInfoClassAdapter visitor = new ClassInfoClassAdapter(cw);
            cr.accept(visitor, false);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        m_classInfoRepository.addClassInfo(this);
    }

    /**
     * Returns the class info for a specific class.
     * 
     * @return the class info
     */
    public static ClassInfo getClassInfo(final byte[] bytecode, final ClassLoader loader) {
        String className = AsmClassInfo.retrieveClassNameFromBytecode(bytecode);
        AsmClassInfoRepository repository = AsmClassInfoRepository.getRepository(loader);
        ClassInfo classInfo = repository.getClassInfo(className);
        if (classInfo == null) {
            classInfo = new AsmClassInfo(bytecode, loader);
        }
        return classInfo;
    }

    /**
     * Returns the class info for a specific class.
     * 
     * @return the class info
     */
    public static ClassInfo getClassInfo(final InputStream stream, final ClassLoader loader) {
        try {
            ClassReader cr = new ClassReader(stream);
            ClassWriter cw = new ClassWriter(true);
            ClassNameRetrievalClassAdapter visitor = new ClassNameRetrievalClassAdapter(cw);
            cr.accept(visitor, false);
            String className = visitor.getClassName();
            AsmClassInfoRepository repository = AsmClassInfoRepository.getRepository(loader);
            ClassInfo classInfo = repository.getClassInfo(className);
            if (classInfo == null) {
                classInfo = new AsmClassInfo(cw.toByteArray(), loader);
            }
            return classInfo;
        } catch (IOException e) {
            throw new WrappedRuntimeException(e);
        }
    }

    /**
     * Retrieves the class name from the bytecode of a class.
     * 
     * @param bytecode
     * @return the class name
     */
    public static String retrieveClassNameFromBytecode(final byte[] bytecode) {
        ClassReader cr = new ClassReader(bytecode);
        ClassWriter cw = new ClassWriter(true);
        ClassNameRetrievalClassAdapter visitor = new ClassNameRetrievalClassAdapter(cw);
        cr.accept(visitor, false);
        return visitor.getClassName();
    }

    /**
     * Creates a ClassInfo based on the stream retrieved from the class loader through <code>getResourceAsStream</code>.
     * 
     * @param className
     * @param loader
     */
    public static ClassInfo createClassInfoFromStream(String className, final ClassLoader loader) {

        // FIXME handle primitives
        if (className.equals("void")) {
            return null;
        }
        
        className = className.replace('.', '/');
        InputStream classAsStream = loader.getResourceAsStream(className + ".class");
        if (classAsStream == null) {
            throw new RuntimeException("could not load class [" + className + "] as a resource in loader ["
                    + loader + "]");
        }
        return AsmClassInfo.getClassInfo(classAsStream, loader);
    }

   /**
     * Returns the bytecode for the class.
     * 
     * @return Returns the bytecode.
     */
    public byte[] getBytecode() {
        return m_bytecode;
    }

    /**
     * Returns the annotations infos.
     * 
     * @return the annotations infos
     */
    public List getAnnotations() {
        return m_annotations;
    }

    /**
     * Returns the name of the class.
     * 
     * @return the name of the class
     */
    public String getName() {
        return m_name;
    }

    /**
     * Returns the class modifiers.
     * 
     * @return the class modifiers
     */
    public int getModifiers() {
        return m_modifiers;
    }

    /**
     * Returns a constructor info by its hash.
     * 
     * @param hash
     * @return
     */
    public ConstructorInfo getConstructor(final int hash) {
        return (ConstructorInfo) m_constructors.get(hash);
    }

    /**
     * Returns a list with all the constructors info.
     * 
     * @return the constructors info
     */
    public ConstructorInfo[] getConstructors() {
        Object[] values = m_constructors.getValues();
        ConstructorInfo[] methodInfos = new ConstructorInfo[values.length];
        for (int i = 0; i < values.length; i++) {
            methodInfos[i] = (ConstructorInfo) values[i];
        }
        return methodInfos;
    }

    /**
     * Returns a method info by its hash.
     * 
     * @param hash
     * @return
     */
    public MethodInfo getMethod(final int hash) {
        return (MethodInfo) m_methods.get(hash);
    }

    /**
     * Returns a list with all the methods info.
     * 
     * @return the methods info
     */
    public MethodInfo[] getMethods() {
        Object[] values = m_methods.getValues();
        MethodInfo[] methodInfos = new MethodInfo[values.length];
        for (int i = 0; i < values.length; i++) {
            methodInfos[i] = (MethodInfo) values[i];
        }
        return methodInfos;
    }

    /**
     * Returns a field info by its hash.
     * 
     * @param hash
     * @return
     */
    public FieldInfo getField(final int hash) {
        return (FieldInfo) m_fields.get(hash);
    }

    /**
     * Returns a list with all the field info.
     * 
     * @return the field info
     */
    public FieldInfo[] getFields() {
        Object[] values = m_fields.getValues();
        FieldInfo[] fieldInfos = new FieldInfo[values.length];
        for (int i = 0; i < values.length; i++) {
            fieldInfos[i] = (FieldInfo) values[i];
        }
        return fieldInfos;
    }

    /**
     * Returns the interfaces.
     * 
     * @return the interfaces
     */
    public ClassInfo[] getInterfaces() {
        if (m_interfaces == null) {
            m_interfaces = new ClassInfo[m_interfaceClassNames.length];
            for (int i = 0; i < m_interfaceClassNames.length; i++) {
                m_interfaces[i] = AsmClassInfo.createClassInfoFromStream(m_interfaceClassNames[i], m_loader);
            }
        }
        return m_interfaces;
    }

    /**
     * Returns the super class.
     * 
     * @return the super class
     */
    public ClassInfo getSuperClass() {
        if (m_superClass == null) {
            m_superClass = AsmClassInfo.createClassInfoFromStream(m_superClassName, m_loader);
        }
        return m_superClass;
    }

     /**
     * Returns the component type if array type else null.
     * 
     * @return the component type
     */
    public ClassInfo getComponentType() {
        if (isArray() && (m_componentType == null)) {
            // TODO: get it
        }
        return m_componentType;
    }

    /**
     * Is the class an interface.
     * 
     * @return
     */
    public boolean isInterface() {
        return m_isInterface;
    }

    /**
     * Is the class a primitive type.
     * 
     * @return
     */
    public boolean isPrimitive() {
        return m_isPrimitive;
    }

    /**
     * Is the class an array type.
     * 
     * @return
     */
    public boolean isArray() {
        return m_isArray;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassInfo)) {
            return false;
        }
        ClassInfo classInfo = (ClassInfo) o;
        return m_name.equals(classInfo.getName().toString());
    }

    public int hashCode() {
        return m_name.hashCode();
    }

    /**
     * ASM bytecode visitor that retrieves the class name from the bytecode.
     * 
     * @author <a href="mailto:jboner@codehaus.org">Jonas Bon�r </a>
     */
    public static class ClassNameRetrievalClassAdapter extends ClassAdapter {

        private String m_className;

        public ClassNameRetrievalClassAdapter(final ClassVisitor visitor) {
            super(visitor);
        }

        public void visit(final int access, final String name, final String superName, final String[] interfaces,
                final String sourceFile) {
            m_className = name.replace('/', '.');
            super.visit(access, name, superName, interfaces, sourceFile);
        }

        /**
         * @return Returns the className.
         */
        public String getClassName() {
            return m_className;
        }
    }

    /**
     * ASM bytecode visitor that gathers info about the class.
     * 
     * @author <a href="mailto:jboner@codehaus.org">Jonas Bon�r </a>
     */
    class ClassInfoClassAdapter extends ClassAdapter {

        public ClassInfoClassAdapter(final ClassVisitor visitor) {
            super(visitor);
        }

        public void visit(final int access, final String name, final String superName, final String[] interfaces,
                final String sourceFile) {
            m_name = name.replace('/', '.');
            m_modifiers = access;
            m_superClassName = superName;
            m_interfaceClassNames = interfaces;
            if (m_name.endsWith("[]")) {
                m_isArray = true;
            } else if (m_name.equals("long") || m_name.equals("int") || m_name.equals("short")
                    || m_name.equals("double") || m_name.equals("float") || m_name.equals("byte")
                    || m_name.equals("boolean") || m_name.equals("char")) {
                m_isPrimitive = true;
            }
            // TODO: handle component type
            super.visit(access, name, superName, interfaces, sourceFile);
        }

        public void visitField(final int access, final String name, final String desc, final String value,
                final Attribute attrs) {
            final FieldStruct struct = new FieldStruct();
            struct.modifiers = access;
            struct.name = name;
            struct.desc = desc;
            struct.value = value;
            struct.attrs = attrs;
//            AsmFieldInfo fieldInfo = new AsmFieldInfo(struct, m_name, m_loader);
//            m_methods.put(AsmHelper.calculateHash(struct), fieldInfo);
            super.visitField(access, name, desc, value, attrs);
        }

        public CodeVisitor visitMethod(final int access, final String name, final String desc,
                final String[] exceptions, final Attribute attrs) {
            final MethodStruct struct = new MethodStruct();
            struct.modifiers = access;
            struct.name = name;
            struct.desc = desc;
            struct.exceptions = exceptions;
            struct.attrs = attrs;
            if (name.equals("<clinit>")) {
                // skip <clinit>
            } else if (name.equals("<init>")) {
//                AsmConstructorInfo methodInfo = new AsmConstructorInfo(struct, m_name, m_loader);
//                m_constructors.put(AsmHelper.calculateHash(struct), methodInfo);
            } else {
                AsmMethodInfo methodInfo = new AsmMethodInfo(struct, m_name, m_loader);
                m_methods.put(AsmHelper.calculateHash(struct), methodInfo);
            }
            return cv.visitMethod(access, name, desc, exceptions, attrs);
        }

        public void visitAttribute(final Attribute attrs) {
            if (attrs instanceof CustomAttribute) {
                CustomAttribute customAttribute = (CustomAttribute) attrs;
                byte[] bytes = customAttribute.getBytes();
                try {
                    m_annotations.add((Annotation) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject());
                } catch (Exception e) {
                    System.err.println("WARNING: could not deserialize annotation");
                }
            }

            // bring on the next attribute
            if (attrs.next != null) {
                visitAttribute(attrs.next);
            }
        }
    }
}