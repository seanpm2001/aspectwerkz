/* Generated By:JJTree: Do not edit this line. ExpressionParserVisitor.java */

package org.codehaus.aspectwerkz.definition.expression.ast;

public interface ExpressionParserVisitor {
    public Object visit(SimpleNode node, Object data);

    public Object visit(ExpressionScript node, Object data);

    public Object visit(OrNode node, Object data);

    public Object visit(InNode node, Object data);

    public Object visit(NotInNode node, Object data);

    public Object visit(AndNode node, Object data);

    public Object visit(NotNode node, Object data);

    public Object visit(Identifier node, Object data);

    public Object visit(BooleanLiteral node, Object data);

    public Object visit(TrueNode node, Object data);

    public Object visit(FalseNode node, Object data);
}
