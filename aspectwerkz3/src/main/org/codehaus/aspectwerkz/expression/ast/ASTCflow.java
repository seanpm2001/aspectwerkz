/* Generated By:JJTree: Do not edit this line. ASTCflow.java */
package org.codehaus.aspectwerkz.expression.ast;

public class ASTCflow extends SimpleNode
{
    public ASTCflow(int id)
    {
        super(id);
    }

    public ASTCflow(ExpressionParser p, int id)
    {
        super(p, id);
    }

    public Object jjtAccept(ExpressionParserVisitor visitor, Object data)
    {
        return visitor.visit(this, data);
    }
}
