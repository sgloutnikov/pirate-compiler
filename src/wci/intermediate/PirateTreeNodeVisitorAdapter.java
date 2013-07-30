package wci.intermediate;

// import every type of node we have
import wci.frontend.ASTAnd;
import wci.frontend.ASTAssign;
//import wci.frontend.ASTBreak;
import wci.frontend.ASTCall;
import wci.frontend.ASTCharacter;
import wci.frontend.ASTCompound;
//import wci.frontend.ASTCond;
//import wci.frontend.ASTCondBranch;
//import wci.frontend.ASTDefaultBranch;
import wci.frontend.ASTDivide;
import wci.frontend.ASTEqualEquals;
import wci.frontend.ASTFor;
import wci.frontend.ASTGreaterEquals;
import wci.frontend.ASTGreaterThan;
import wci.frontend.ASTIf;
import wci.frontend.ASTIntegerConstant;
import wci.frontend.ASTLessEquals;
import wci.frontend.ASTLessThan;
import wci.frontend.ASTLoop;
import wci.frontend.ASTMinus;
import wci.frontend.ASTModulo;
import wci.frontend.ASTMultiply;
import wci.frontend.ASTNegate;
import wci.frontend.ASTNot;
import wci.frontend.ASTNotEquals;
import wci.frontend.ASTOr;
import wci.frontend.ASTParameters;
import wci.frontend.ASTPlus;
import wci.frontend.ASTPrint;
import wci.frontend.ASTProcedureBody;
import wci.frontend.ASTRealConstant;
import wci.frontend.ASTReturn;
//import wci.frontend.ASTSelect;
//import wci.frontend.ASTSelectBranch;
//import wci.frontend.ASTSelectConstant;
import wci.frontend.ASTString;
import wci.frontend.ASTTest;
import wci.frontend.ASTVariable;
import wci.frontend.ASTRoutine;
import wci.frontend.PirateParserVisitor;
import wci.frontend.SimpleNode;

public class PirateTreeNodeVisitorAdapter implements PirateParserVisitor
{
    public Object visit(SimpleNode node, Object data)
    {
        return node.childrenAccept(this, data);
    }
    
    public Object visit(ASTAnd node, Object data)
    {
        return node.childrenAccept(this, data);
    }
    
    public Object visit(ASTAssign node, Object data)
    {
        return node.childrenAccept(this, data);
    }
    
   /* public Object visit(ASTBreak node, Object data)
    {
        return node.childrenAccept(this, data);
    } */
    
    public Object visit(ASTCharacter node, Object data)
    {
        return node.childrenAccept(this, data);
    }
    
    public Object visit(ASTCompound node, Object data)
    {
        return node.childrenAccept(this, data);
    }
    
  /*  public Object visit(ASTCond node, Object data)
    {
        return node.childrenAccept(this, data);
    } */

  /*  public Object visit(ASTCondBranch node, Object data)
    {
        return node.childrenAccept(this, data);
    } */
    
  /*  public Object visit(ASTDefaultBranch node, Object data)
    {
        return node.childrenAccept(this, data);
    } */
    
    public Object visit(ASTDivide node, Object data)
    {
        return node.childrenAccept(this, data);
    }
    
    public Object visit(ASTEqualEquals node, Object data)
    {
        return node.childrenAccept(this, data);
    }
    
    public Object visit(ASTGreaterEquals node, Object data)
    {
        return node.childrenAccept(this, data);
    }
    
    public Object visit(ASTGreaterThan node, Object data)
    {
        return node.childrenAccept(this, data);
    }
    
    public Object visit(ASTIf node, Object data)
    {
        return node.childrenAccept(this, data);
    }
    
    public Object visit(ASTIntegerConstant node, Object data)
    {
        return node.childrenAccept(this, data);
    }
    
    public Object visit(ASTLessEquals node, Object data)
    {
        return node.childrenAccept(this, data);
    }
    
    public Object visit(ASTLessThan node, Object data)
    {
        return node.childrenAccept(this, data);
    }
    
    public Object visit(ASTLoop node, Object data)
    {
         return node.childrenAccept(this, data);
    }

    public Object visit(ASTMinus node, Object data)
    {
        return node.childrenAccept(this, data);
    }

    public Object visit(ASTModulo node, Object data)
    {
        return node.childrenAccept(this, data);
    }
    
    public Object visit(ASTMultiply node, Object data)
    {
        return node.childrenAccept(this, data);
    }
    
    public Object visit(ASTNegate node, Object data)
    {
        return node.childrenAccept(this, data);
    }
    
    public Object visit(ASTNot node, Object data)
    {
        return node.childrenAccept(this, data);
    }
    
    public Object visit(ASTNotEquals node, Object data)
    {
        return node.childrenAccept(this, data);
    }
    
    public Object visit(ASTOr node, Object data)
    {
        return node.childrenAccept(this, data);
    }
    
    public Object visit(ASTPlus node, Object data)
    {
        return node.childrenAccept(this, data);
    }
    
    public Object visit(ASTPrint node, Object data)
    {
        return node.childrenAccept(this, data);
    }
    
    public Object visit(ASTProcedureBody node, Object data)
    {
        return node.childrenAccept(this, data);
    }
    
    public Object visit(ASTRealConstant node, Object data)
    {
        return node.childrenAccept(this, data);
    }
    
    public Object visit(ASTReturn node, Object data)
    {
        return node.childrenAccept(this, data);
    }
    
  /*  public Object visit(ASTSelect node, Object data)
    {
        return node.childrenAccept(this, data);
    }
    
    public Object visit(ASTSelectBranch node, Object data)
    {
        return node.childrenAccept(this, data);
    }
    
    public Object visit(ASTSelectConstant node, Object data)
    {
        return node.childrenAccept(this, data);
    } */
    
    public Object visit(ASTString node, Object data)
    {
        return node.childrenAccept(this, data);
    }
    
    public Object visit(ASTTest node, Object data)
    {
        return node.childrenAccept(this, data);
    }
    
    public Object visit(ASTVariable node, Object data)
    {
        return node.childrenAccept(this, data);
    }

   public Object visit(ASTRoutine node, Object data)
   {
      return node.childrenAccept(this, data);
   }

   public Object visit(ASTCall node, Object data) 
   {
	  return node.childrenAccept(this, data);
   }

   public Object visit(ASTParameters node, Object data) 
   {
	  return node.childrenAccept(this, data);
   }
   
   public Object visit(ASTFor node, Object data) 
   {
	  return node.childrenAccept(this, data);
   }
}
