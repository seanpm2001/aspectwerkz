/* Generated By:JJTree: Do not edit this line. ASTStaticInitialization.java */
package org.codehaus.aspectwerkz.expression.ast;

public class ASTStaticInitialization extends SimpleNode {
    public ASTStaticInitialization(int id) {
        super(id);
    }

    public ASTStaticInitialization(ExpressionParser p, int id) {
        super(p, id);
    }

    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(ExpressionParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
