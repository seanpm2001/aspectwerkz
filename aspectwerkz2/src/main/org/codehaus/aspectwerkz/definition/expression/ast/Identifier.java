/* Generated By:JJTree: Do not edit this line. Identifier.java */

package org.codehaus.aspectwerkz.definition.expression.ast;

public class Identifier extends SimpleNode {
    public String name;

    public Identifier(int id) {
        super(id);
    }

    public Identifier(ExpressionParser p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(ExpressionParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
