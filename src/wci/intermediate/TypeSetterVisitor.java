package wci.intermediate;

import wci.frontend.*;
import wci.intermediate.*;
import wci.intermediate.symtabimpl.*;

public class TypeSetterVisitor extends PirateTreeNodeVisitorAdapter
{
    private void setType(SimpleNode node)
    {
        int count = node.jjtGetNumChildren();
        TypeSpec type = Predefined.integerType;
        
        for (int i = 0; (i < count) && (type == Predefined.integerType); ++i) {
            SimpleNode child = (SimpleNode) node.jjtGetChild(i);
            TypeSpec childType = child.getTypeSpec();
            
            if (childType == Predefined.realType) {
                type = Predefined.realType;
            }
        }
        
        node.setTypeSpec(type);
    }
    
    public Object visit(ASTAssign node, Object data)
    {
        Object obj = super.visit(node, data);
        setType(node);
        return obj;
    }

    public Object visit(ASTPlus node, Object data)
    {
        Object obj = super.visit(node, data);
        setType(node);
        return obj;
    }
    
    public Object visit(ASTMinus node, Object data)
    {
        Object obj = super.visit(node, data);
        setType(node);
        return obj;
    }
    
    public Object visit(ASTMultiply node, Object data)
    {
       Object obj = super.visit(node, data);
       setType(node);
       return obj;
    }
    
    public Object visit(ASTDivide node, Object data)
    {
       Object obj = super.visit(node, data);
       setType(node);
       return obj;
    }
    
    public Object visit(ASTNegate node, Object data)
    {
       Object obj = super.visit(node, data);
       setType(node);
       return obj;
    }
    
    public Object visit(ASTModulo node, Object data)
    {
       Object obj = super.visit(node, data);
       setType(node);
       return obj;
    }
    
    public Object visit(ASTLoop node, Object data)
    {
        return data;
    }
    
    public Object visit(ASTIf node, Object data)
    {
        return data;
    }
    
    public Object visit(ASTTest node, Object data)
    {
        return data;
    }
    
    public Object visit(ASTLessThan node, Object data)
    {
        return data;
    }
    
    public Object visit(ASTLessEquals node, Object data)
    {
        return data;
    }
    
    public Object visit(ASTGreaterThan node, Object data)
    {
        return data;
    }
    
    public Object visit(ASTGreaterEquals node, Object data)
    {
        return data;
    }
    
    public Object visit(ASTEqualEquals node, Object data)
    {
        return data;
    }
 
    public Object visit(ASTVariable node, Object data)
    {
        return data;
    }
    
    public Object visit(ASTIntegerConstant node, Object data)
    {
        return data;
    }
    
    public Object visit(ASTFor node, Object data)
    {
        return data;
    }
    
    public Object visit(ASTAnd node, Object data)
    {
        return data;
    }
    
    public Object visit(ASTOr node, Object data)
    {
        return data;
    }
    
    public Object visit(ASTRealConstant node, Object data)
    {
        return data;
    }
}
