/* Generated By:JJTree: Do not edit this line. NotNode.java */

package org.codehaus.aspectwerkz.definition.expression.ast;

public class NotNode extends SimpleNode {
    public NotNode(int id) {
        super(id);
    }

    public NotNode(ExpressionParser p, int id) {
        super(p, id);
    }

    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(ExpressionParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
