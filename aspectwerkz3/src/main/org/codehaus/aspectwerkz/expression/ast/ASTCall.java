/* Generated By:JJTree: Do not edit this line. ASTCall.java */
package org.codehaus.aspectwerkz.expression.ast;

public class ASTCall extends SimpleNode {
    public ASTCall(int id) {
        super(id);
    }

    public ASTCall(ExpressionParser p, int id) {
        super(p, id);
    }

    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(ExpressionParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
