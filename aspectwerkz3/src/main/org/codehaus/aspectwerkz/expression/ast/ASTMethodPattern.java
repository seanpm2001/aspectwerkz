/* Generated By:JJTree: Do not edit this line. ASTMethodPattern.java */
package org.codehaus.aspectwerkz.expression.ast;

import org.codehaus.aspectwerkz.expression.SubtypePatternType;
import org.codehaus.aspectwerkz.expression.regexp.NamePattern;
import org.codehaus.aspectwerkz.expression.regexp.Pattern;
import org.codehaus.aspectwerkz.expression.regexp.TypePattern;
import java.util.ArrayList;
import java.util.List;

public class ASTMethodPattern extends SimpleNode {
    private TypePattern m_returnTypePattern;
    private TypePattern m_declaringTypePattern;
    private NamePattern m_methodNamePattern;
    private List m_modifiers = new ArrayList();

    public ASTMethodPattern(int id) {
        super(id);
    }

    public ASTMethodPattern(ExpressionParser p, int id) {
        super(p, id);
    }

    public Object jjtAccept(ExpressionParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    public void addModifier(String modifier) {
        m_modifiers.add(modifier);
    }

    public void setReturnTypePattern(String pattern) {
        if (pattern.endsWith("+")) {
            pattern = pattern.substring(0, pattern.length() - 1);
            m_returnTypePattern = Pattern.compileTypePattern(pattern, SubtypePatternType.MATCH_ON_ALL_METHODS);
        } else if (pattern.endsWith("#")) {
            pattern = pattern.substring(0, pattern.length() - 1);
            m_returnTypePattern = Pattern.compileTypePattern(pattern, SubtypePatternType.MATCH_ON_BASE_TYPE_METHODS);
        } else {
            m_returnTypePattern = Pattern.compileTypePattern(pattern, SubtypePatternType.NOT_HIERARCHICAL);
        }
    }

    public void setFullNamePattern(final String pattern) {
        int index = pattern.lastIndexOf('.');
        String classPattern = pattern.substring(0, index);
        if (classPattern.endsWith("+")) {
            classPattern = classPattern.substring(0, classPattern.length() - 1);
            m_declaringTypePattern = Pattern.compileTypePattern(classPattern, SubtypePatternType.MATCH_ON_ALL_METHODS);
        } else if (classPattern.endsWith("#")) {
            classPattern = classPattern.substring(0, classPattern.length() - 1);
            m_declaringTypePattern = Pattern.compileTypePattern(classPattern,
                                                                SubtypePatternType.MATCH_ON_BASE_TYPE_METHODS);
        } else {
            m_declaringTypePattern = Pattern.compileTypePattern(classPattern, SubtypePatternType.NOT_HIERARCHICAL);
        }
        String methodNamePattern = pattern.substring(index + 1, pattern.length());
        m_methodNamePattern = Pattern.compileNamePattern(methodNamePattern);
    }

    public TypePattern getReturnTypePattern() {
        return m_returnTypePattern;
    }

    public TypePattern getDeclaringTypePattern() {
        return m_declaringTypePattern;
    }

    public NamePattern getMethodNamePattern() {
        return m_methodNamePattern;
    }

    public List getModifiers() {
        return m_modifiers;
    }
}
