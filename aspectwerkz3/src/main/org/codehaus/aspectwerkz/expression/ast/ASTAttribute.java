/* Generated By:JJTree: Do not edit this line. ASTAttribute.java */
package org.codehaus.aspectwerkz.expression.ast;

public class ASTAttribute extends SimpleNode
{
    private String m_name;

    public ASTAttribute(int id)
    {
        super(id);
    }

    public ASTAttribute(ExpressionParser p, int id)
    {
        super(p, id);
    }

    public Object jjtAccept(ExpressionParserVisitor visitor, Object data)
    {
        return visitor.visit(this, data);
    }

    public void setName(String name)
    {
        m_name = name;
    }

    public String getName()
    {
        return m_name;
    }
}
