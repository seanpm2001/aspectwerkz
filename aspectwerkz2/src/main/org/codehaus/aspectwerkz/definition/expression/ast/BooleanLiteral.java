/* Generated By:JJTree: Do not edit this line. BooleanLiteral.java */

package org.codehaus.aspectwerkz.definition.expression.ast;

public class BooleanLiteral extends SimpleNode {
    public BooleanLiteral(int id) {
        super(id);
    }

    public BooleanLiteral(ExpressionParser p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(ExpressionParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
