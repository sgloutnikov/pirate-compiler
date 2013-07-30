package wci.backend.compiler;

import java.util.ArrayList;

import com.sun.org.apache.bcel.internal.classfile.Code;

import wci.frontend.*;
import wci.intermediate.*;
import wci.intermediate.symtabimpl.Predefined;

import static wci.intermediate.icodeimpl.ICodeKeyImpl.*;
import static wci.intermediate.typeimpl.TypeFormImpl.ARRAY;
import static wci.intermediate.typeimpl.TypeFormImpl.ENUMERATION;
import static wci.intermediate.typeimpl.TypeKeyImpl.ARRAY_ELEMENT_TYPE;

public class CodeGeneratorVisitor
    extends PirateTreeNodeVisitorAdapter
    implements PirateParserTreeConstants
{
   
    String prevCallType = "";
    // Loop initialization. Good to increase for larger programs.
    int labelInCount = 0;
    int labelOutCount = 100;
    int labelOut2Count = 200;
    
	/**
	 * Generates Jasmin code to print an integer constant, real constant,
	 * or variable (of type integer, real, or boolean).  
	 * Note: prints boolean values in numeric form (0 = false; 1 = true).
	 */
	public Object visit(ASTPrint node, Object data) 
	{     
      SimpleNode dataNode = (SimpleNode) node.jjtGetChild(0);
      Object dataValue = dataNode.getAttribute(VALUE);
      CodeGenerator.objectFile.println("    getstatic	java/lang/System/out Ljava/io/PrintStream;");
      CodeGenerator.objectFile.println("    new       java/lang/StringBuilder");
      CodeGenerator.objectFile.println("    dup");
      
      
      dataNode.jjtAccept(this, data);
      if (dataValue == null) {
      	// find out type of variable
         SymTabEntry variableId = (SymTabEntry) dataNode.getAttribute(ID);
      	// check type to see if need to convert the value to a string
         if (variableId.getTypeSpec() == Predefined.integerType
      			|| variableId.getTypeSpec() == Predefined.booleanType
      			|| variableId.getTypeSpec() == Predefined.charType) {
      		CodeGenerator.objectFile.println("    invokestatic	java/lang/Integer.toString(I)Ljava/lang/String;");
      	} else if (variableId.getTypeSpec() == Predefined.realType) {
      		CodeGenerator.objectFile.println("    invokestatic	java/lang/Float.toString(F)Ljava/lang/String;");
      	}
      } else if (dataValue instanceof Integer) {
      	CodeGenerator.objectFile.println("    invokestatic	java/lang/Integer.toString(I)Ljava/lang/String;");
      } else if (dataValue instanceof Float) {
      	CodeGenerator.objectFile.println("    invokestatic	java/lang/Float.toString(F)Ljava/lang/String;");
      } // else the value is a string and doesn't need to be converted
      
      CodeGenerator.objectFile.println("    invokenonvirtual java/lang/StringBuilder/<init>(Ljava/lang/String;)V");
      //  CodeGenerator.objectFile.println("    invokevirtual java/lang/StringBuilder/append(I)Ljava/lang/StringBuilder;");
      CodeGenerator.objectFile.println("    invokevirtual java/lang/StringBuilder/toString()Ljava/lang/String;");
      CodeGenerator.objectFile.println("    invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V");

      CodeGenerator.objectFile.flush();
		return data;
	}
	
	/**
	 * Generate Jasmin code to load a string constant.
	 */
	public Object visit(ASTString node, Object data) 
	{
      String value = (String) node.getAttribute(VALUE);

      // Generate code to load a constant instruction.
      CodeGenerator.objectFile.println("    ldc " + value);
      CodeGenerator.objectFile.flush();

      return data;
	}
	
	/**
	 * Generate Jasmin code to load an integer constant.
	 */
   public Object visit(ASTIntegerConstant node, Object data)
   {
       int value = (Integer) node.getAttribute(VALUE);

       // Generate code to load a constant instruction.
       CodeGenerator.objectFile.println("    ldc " + value);
       CodeGenerator.objectFile.flush();

       return data;
   }

	/**
	 * Generate Jasmin code to load a real constant.
	 */
   public Object visit(ASTRealConstant node, Object data)
   {
       float value = (Float) node.getAttribute(VALUE);

       // Generate code to load a constant instruction.
       CodeGenerator.objectFile.println("    ldc " + value);
       CodeGenerator.objectFile.flush();

       return data;
   }
   
   // TODO -- Wycee ; I think this is it for this method, but I can't test loading of local variables 
   // (.var) from their slot numbers (index) until we get assignment working.
   public Object visit(ASTVariable node, Object data)
   {
       TypeSpec type = node.getTypeSpec();
       String typePrefix = typeDescriptor(type);
       SymTabEntry variableId = (SymTabEntry) node.getAttribute(ID);
       int index = variableId.getIndex();

       // check nesting level; if 1, it's a global variable
       int nestingLevel = variableId.getSymTab().getNestingLevel();
       if (nestingLevel == 1 && index == -1) {
     	  StringBuffer sb = new StringBuffer();
     	  sb.append("    getstatic ");
     	  sb.append(CodeGenerator.programName);
     	  sb.append("/");
     	  sb.append(variableId.getName());
       	  sb.append(" ");
     	  sb.append(CodeGenerator.globalVariableMap.get(variableId.getName()));
     	  CodeGenerator.objectFile.println(sb.toString());
       } else if (nestingLevel == 0) {
     	  if (variableId.getName().equals("true")) {
     		  CodeGenerator.objectFile.println("ldc 1");
     	  } else {
     		  CodeGenerator.objectFile.println("ldc 0");
     	  }
       } else {
     	  // must be nesting level 2 (the max) since declared routines cannot have nested routines
	        // Generate the appropriate load instruction.
	        CodeGenerator.objectFile.println("    " + typePrefix +
	                                         "load " + index);
	        CodeGenerator.objectFile.flush();
       }
       return data;
   }
	
	/**
	 * Generate Jasmin code to call a (static) routine with given arguments (if any).
	 */
   public Object visit(ASTCall node, Object data)
   {
		SimpleNode routineNode   = (SimpleNode) node.jjtGetChild(0);
		SimpleNode parametersNode = (SimpleNode) node.jjtGetChild(1);
		
		// push arguments on the stack before invoking the routine
		parametersNode.jjtAccept(this, data);
		routineNode.jjtAccept(this, data);
		
		return data;
   }
   
	/**
	 * Generate Jasmin code to call a (static) routine.
	 */
   public Object visit(ASTRoutine node, Object data)
   {
      SymTabEntry routineId = (SymTabEntry) node.getAttribute(ID);
      
		// Generate code to load a constant instruction.
   	StringBuffer sb = new StringBuffer("    invokestatic ");
   	sb.append(CodeGenerator.programName);
   	sb.append("/");
   	sb.append(CodeGenerator.routineSignatureMap.get(routineId.getName()));

		CodeGenerator.objectFile.println(sb.toString());
		CodeGenerator.objectFile.flush();

		return data;
   }
   
	/**
	 * Generate Jasmin code to load parameters (if any).
	 */
   public Object visit(ASTParameters node, Object data)
   {
   	int numArgs = node.jjtGetNumChildren();
   	for (int i = 0; i < numArgs; i++) {
   		SimpleNode argNode = (SimpleNode) node.jjtGetChild(i);
   		argNode.jjtAccept(this, data);
   	}
   	return data;
   }
	
	
	// TODO -- Stefan
    public Object visit(ASTAssign node, Object data)
    {
        SimpleNode variableNode   = (SimpleNode) node.jjtGetChild(0);
        SimpleNode expressionNode = (SimpleNode) node.jjtGetChild(1);
        SymTabEntry variableId = (SymTabEntry) variableNode.getAttribute(ID);
        if (variableId.getName().equals("RUM")) {
      	  variableNode.setTypeSpec(Predefined.integerType);
        }
        
        prevCallType = typeDescriptor(variableNode.getTypeSpec());
        
        TypeSpec type = variableNode.getTypeSpec();
        String typePrefix = typeDescriptor(type); 
        int index = variableId.getIndex();
        int nestingLevel = variableId.getSymTab().getNestingLevel();

        // Generate code for the expression that needs to be calculated.
        expressionNode.jjtAccept(this, data);
        
        // Store
        // Index is -1; Global Variable
        if (index == -1)
        {
           CodeGenerator.objectFile.print("    putstatic ");
           CodeGenerator.objectFile.print(CodeGenerator.programName + "/");
           CodeGenerator.objectFile.print(variableId.getName() + " ");
           CodeGenerator.objectFile.print(typePrefix.toUpperCase());
           CodeGenerator.objectFile.println();
           
           CodeGenerator.objectFile.flush();
        }
        // Local Variable
        else
        {
           CodeGenerator.objectFile.print("    " + typePrefix);
           CodeGenerator.objectFile.print("store " + index);
           CodeGenerator.objectFile.println();
           
           CodeGenerator.objectFile.flush();
        }

        CodeGenerator.objectFile.flush();

        return data;
    }

    // TODO
    public Object visit(ASTPlus node, Object data)
    {
       SimpleNode leftSideNode = (SimpleNode) node.jjtGetChild(0);
       SimpleNode rightSideNode = (SimpleNode) node.jjtGetChild(1);
       TypeSpec leftSideType = leftSideNode.getTypeSpec();
       TypeSpec rightSideType = rightSideNode.getTypeSpec();
       String leftType = typeDescriptor(leftSideType);
       String rightType = typeDescriptor(rightSideType);
       
       // Two integers, result type integer.
       if(leftType.equals("i") && rightType.equals("i"))
       {
          leftSideNode.jjtAccept(this, data);
          rightSideNode.jjtAccept(this, data);
          CodeGenerator.objectFile.println("    iadd");
       }
       // Left is float, result type float.
       else if(leftType.equals("f") && rightType.equals("i"))
       {
          leftSideNode.jjtAccept(this, data);
          rightSideNode.jjtAccept(this, data);
          CodeGenerator.objectFile.println("    i2f");
          CodeGenerator.objectFile.println("    fadd");
       }
       // Right is float, result type float.
       else if(rightType.equals("f") && leftType.equals("i"))
       {
          leftSideNode.jjtAccept(this, data);
          CodeGenerator.objectFile.println("    i2f");
          rightSideNode.jjtAccept(this, data);
          CodeGenerator.objectFile.println("    fadd");
       }
       else
       {
          leftSideNode.jjtAccept(this, data);
          rightSideNode.jjtAccept(this, data);
          CodeGenerator.objectFile.println("    fadd");
       }
       
       CodeGenerator.objectFile.flush();
       return data;
    }
    
    public Object visit(ASTMinus node, Object data)
    {
       SimpleNode leftSideNode = (SimpleNode) node.jjtGetChild(0);
       SimpleNode rightSideNode = (SimpleNode) node.jjtGetChild(1);
       TypeSpec leftSideType = leftSideNode.getTypeSpec();
       TypeSpec rightSideType = rightSideNode.getTypeSpec();
       String leftType = typeDescriptor(leftSideType);
       String rightType = typeDescriptor(rightSideType);
       
       // Two integers, result type integer.
       if(leftType.equals("i") && rightType.equals("i"))
       {
          leftSideNode.jjtAccept(this, data);
          rightSideNode.jjtAccept(this, data);
          CodeGenerator.objectFile.println("    isub");
       }
       // Left is float, result type float.
       else if(leftType.equals("f") && rightType.equals("i"))
       {
          leftSideNode.jjtAccept(this, data);
          rightSideNode.jjtAccept(this, data);
          CodeGenerator.objectFile.println("    i2f");
          CodeGenerator.objectFile.println("    fsub");
       }
       // Right is float, result type float.
       else if(rightType.equals("f") && leftType.equals("i"))
       {
          leftSideNode.jjtAccept(this, data);
          CodeGenerator.objectFile.println("    i2f");
          rightSideNode.jjtAccept(this, data);
          CodeGenerator.objectFile.println("    fsub");
       }
       else
       {
          leftSideNode.jjtAccept(this, data);
          rightSideNode.jjtAccept(this, data);
          CodeGenerator.objectFile.println("    fsub");
       }
       
       CodeGenerator.objectFile.flush();
       return data;
    }
    
    public Object visit (ASTMultiply node, Object data)
    {
       SimpleNode leftSideNode = (SimpleNode) node.jjtGetChild(0);
       SimpleNode rightSideNode = (SimpleNode) node.jjtGetChild(1);
       TypeSpec leftSideType = leftSideNode.getTypeSpec();
       TypeSpec rightSideType = rightSideNode.getTypeSpec();
       String leftType = typeDescriptor(leftSideType);
       String rightType = typeDescriptor(rightSideType);
       
       // Two integers, result type integer.
       if(leftType.equals("i") && rightType.equals("i"))
       {
          leftSideNode.jjtAccept(this, data);
          rightSideNode.jjtAccept(this, data);
          CodeGenerator.objectFile.println("    imul");
       }
       // Left is float, result type float.
       else if(leftType.equals("f") && rightType.equals("i"))
       {
          leftSideNode.jjtAccept(this, data);
          rightSideNode.jjtAccept(this, data);
          CodeGenerator.objectFile.println("    i2f");
          CodeGenerator.objectFile.println("    fmul");
       }
       // Right is float, result type float.
       else if(rightType.equals("f") && leftType.equals("i"))
       {
          leftSideNode.jjtAccept(this, data);
          CodeGenerator.objectFile.println("    i2f");
          rightSideNode.jjtAccept(this, data);
          CodeGenerator.objectFile.println("    fmul");
       }
       else
       {
          leftSideNode.jjtAccept(this, data);
          rightSideNode.jjtAccept(this, data);
          CodeGenerator.objectFile.println("    fmul");
       }
       
       CodeGenerator.objectFile.flush();
       return data;
    }
    
    public Object visit(ASTDivide node, Object data)
    {
       SimpleNode leftSideNode = (SimpleNode) node.jjtGetChild(0);
       SimpleNode rightSideNode = (SimpleNode) node.jjtGetChild(1);
       TypeSpec leftSideType = leftSideNode.getTypeSpec();
       TypeSpec rightSideType = rightSideNode.getTypeSpec();
       String leftType = typeDescriptor(leftSideType);
       String rightType = typeDescriptor(rightSideType);
       
       // Two integers, result type integer.
       if(leftType.equals("i") && rightType.equals("i"))
       {
          leftSideNode.jjtAccept(this, data);
          rightSideNode.jjtAccept(this, data);
          CodeGenerator.objectFile.println("    idiv");
       }
       // Left is float, result type float.
       else if(leftType.equals("f") && rightType.equals("i"))
       {
          leftSideNode.jjtAccept(this, data);
          rightSideNode.jjtAccept(this, data);
          CodeGenerator.objectFile.println("    i2f");
          CodeGenerator.objectFile.println("    fdiv");
       }
       // Right is float, result type float.
       else if(rightType.equals("f") && leftType.equals("i"))
       {
          leftSideNode.jjtAccept(this, data);
          CodeGenerator.objectFile.println("    i2f");
          rightSideNode.jjtAccept(this, data);
          CodeGenerator.objectFile.println("    fdiv");
       }
       else
       {
          leftSideNode.jjtAccept(this, data);
          rightSideNode.jjtAccept(this, data);
          CodeGenerator.objectFile.println("    fdiv");
       }
       
       CodeGenerator.objectFile.flush();
       return data;
    }

    public Object visit(ASTNegate node, Object data)
    {
       SimpleNode negateNode = (SimpleNode) node.jjtGetChild(0);
       TypeSpec negateNodeType = negateNode.getTypeSpec();
       String negateType = typeDescriptor(negateNodeType);
       
       // Integer negation
       if(negateType.equals("i"))
       {
          negateNode.jjtAccept(this, data);
          CodeGenerator.objectFile.println("    ineg");
       }
       // Else Float Negate
       else
       {
          negateNode.jjtAccept(this, data);
          CodeGenerator.objectFile.println("    fneg");
       }
       
       CodeGenerator.objectFile.flush();
       return data;
    }
    

    public Object visit(ASTModulo node, Object data)
    {
       TypeSpec type = node.getTypeSpec();
       SimpleNode moduloNodeLeft = (SimpleNode) node.jjtGetChild(0);
       SimpleNode moduloNodeRight = (SimpleNode) node.jjtGetChild(1);
       String moduloType = typeDescriptor(type);
       
       // Integer type modulo
       if(moduloType.equals("i"))
       {
          moduloNodeLeft.jjtAccept(this, data);
          moduloNodeRight.jjtAccept(this, data);
          CodeGenerator.objectFile.println("    irem");
       }
       // Float type modulo
       else
       {
          moduloNodeLeft.jjtAccept(this, data);
          moduloNodeRight.jjtAccept(this, data);
          CodeGenerator.objectFile.println("    frem");     
       }
       
       CodeGenerator.objectFile.flush();
       return data;
    }
    
    public Object visit(ASTLoop node, Object data)
    {
       SimpleNode testCondition = (SimpleNode) node.jjtGetChild(0);
       SimpleNode expression = (SimpleNode) node.jjtGetChild(1);
       
       // Enter Condition Check And Print Label
       CodeGenerator.objectFile.println("L" + labelInCount + ":");
       testCondition.jjtAccept(this, data);
       expression.jjtAccept(this, data);
       CodeGenerator.objectFile.println("    goto L" + labelInCount);
       
       // Exit
       CodeGenerator.objectFile.println("L" + labelOutCount + ":");
       // Increment Label Counts
       labelInCount++;
       labelOutCount++;
       labelOut2Count++;
       CodeGenerator.objectFile.flush();
       return data;
    }
    
    public Object visit(ASTIf node, Object data)
    {
       SimpleNode testCondition = (SimpleNode) node.jjtGetChild(0);
       SimpleNode expressionIf = (SimpleNode) node.jjtGetChild(1);
       SimpleNode expressionElse = null;
       int elseExists = 0;
       // Else Exists
       try 
       {
          expressionElse = (SimpleNode) node.jjtGetChild(2);
          elseExists = 1;
       }
       catch (ArrayIndexOutOfBoundsException ex)
       {
          elseExists = 0;
       }

       
       // In the If
       if (elseExists == 0)
       {
          testCondition.jjtAccept(this, data);
          expressionIf.jjtAccept(this, data);
          CodeGenerator.objectFile.println("L" + labelOutCount + ":");
       }
       // In the If followed by the Else
       else
       {
          testCondition.jjtAccept(this, data);
          expressionIf.jjtAccept(this, data);
          CodeGenerator.objectFile.println("    goto L" + labelOut2Count);
          // Else Label
          CodeGenerator.objectFile.println("L" + labelOutCount + ":");
          // Process Else
          expressionElse.jjtAccept(this, data);
          CodeGenerator.objectFile.println("L" + labelOut2Count + ":");
       }
       
       // Increment Labels and Flush
       //labelInCount++;
       labelOutCount++;
       labelOut2Count++;
       CodeGenerator.objectFile.flush();
       return data;
    }
    
    public Object visit(ASTTest node, Object data)
    {
       SimpleNode condition = (SimpleNode) node.jjtGetChild(0);
       SimpleNode condition2 = null;
       condition.jjtAccept(this, data);
       try
       {
          condition2 = (SimpleNode) node.jjtGetChild(1);
          condition2.jjtAccept(this, data);
       }
       catch (ArrayIndexOutOfBoundsException ex)
       {
          
       }
        
       CodeGenerator.objectFile.flush();
       return data;
    }
    
    /**
     * Generate code for less than comparison.  
     */
    public Object visit(ASTLessThan node, Object data)
    {
       // Generate operators.
       SimpleNode leftSideNode = (SimpleNode) node.jjtGetChild(0);
       SimpleNode rightSideNode = (SimpleNode) node.jjtGetChild(1);
       
       // check type
       TypeSpec leftSideType = leftSideNode.getTypeSpec();
       TypeSpec rightSideType = rightSideNode.getTypeSpec();
       
       // initially assume both children are integers
       prevCallType = "i";
       
       if (leftSideType == Predefined.realType || rightSideType == Predefined.realType) {
      	 prevCallType = "f";
       }
       leftSideNode.jjtAccept(this, data);
       if (leftSideType == Predefined.integerType && prevCallType.equals("f")) {
      	 // convert to float
          CodeGenerator.objectFile.println("    i2f");
       }
       rightSideNode.jjtAccept(this, data);
       if (rightSideType == Predefined.integerType && prevCallType.equals("f")) {
      	 // convert to float
          CodeGenerator.objectFile.println("    i2f");
       }
       if (prevCallType.equals("f")) {
      	 CodeGenerator.objectFile.println("    fcmpg");
      	 CodeGenerator.objectFile.println("    ifge L" + labelOutCount);
       } else {
      	 CodeGenerator.objectFile.println("    if_icmpge L" + labelOutCount);
       }
       
       return data;
    }
    
    /**
     * Generate code for less equals comparison.
     */
    public Object visit(ASTLessEquals node, Object data)
    {
       // Generate operators.
       SimpleNode leftSideNode = (SimpleNode) node.jjtGetChild(0);
       SimpleNode rightSideNode = (SimpleNode) node.jjtGetChild(1);
       
       // check type
       TypeSpec leftSideType = leftSideNode.getTypeSpec();
       TypeSpec rightSideType = rightSideNode.getTypeSpec();
       
       // initially assume both children are integers
       prevCallType = "i";
       
       if (leftSideType == Predefined.realType || rightSideType == Predefined.realType) {
      	 prevCallType = "f";
       }
       leftSideNode.jjtAccept(this, data);
       if (leftSideType == Predefined.integerType && prevCallType.equals("f")) {
      	 // convert to float
          CodeGenerator.objectFile.println("    i2f");
       }
       rightSideNode.jjtAccept(this, data);
       if (rightSideType == Predefined.integerType && prevCallType.equals("f")) {
      	 // convert to float
          CodeGenerator.objectFile.println("    i2f");
       }
       if (prevCallType.equals("f")) {
      	 CodeGenerator.objectFile.println("    fcmpg");
      	 CodeGenerator.objectFile.println("    ifgt L" + labelOutCount);
       } else {
          CodeGenerator.objectFile.println("    if_icmpgt L" + labelOutCount);
       }
       
       return data;
    }
    
	/**
	 * Generate code for greater than comparison.  
	 */
    public Object visit(ASTGreaterThan node, Object data)
    {
       // Generate operators.
       SimpleNode leftSideNode = (SimpleNode) node.jjtGetChild(0);
       SimpleNode rightSideNode = (SimpleNode) node.jjtGetChild(1);
       
       // check type
       TypeSpec leftSideType = leftSideNode.getTypeSpec();
       TypeSpec rightSideType = rightSideNode.getTypeSpec();
       
       // initially assume both children are integers
       prevCallType = "i";
       
       if (leftSideType == Predefined.realType || rightSideType == Predefined.realType) {
      	 prevCallType = "f";
       }
       leftSideNode.jjtAccept(this, data);
       if (leftSideType == Predefined.integerType && prevCallType.equals("f")) {
      	 // convert to float
          CodeGenerator.objectFile.println("    i2f");
       }
       rightSideNode.jjtAccept(this, data);
       if (rightSideType == Predefined.integerType && prevCallType.equals("f")) {
      	 // convert to float
          CodeGenerator.objectFile.println("    i2f");
       }
       if (prevCallType.equals("f")) {
      	 CodeGenerator.objectFile.println("    fcmpg");
      	 CodeGenerator.objectFile.println("    ifle L" + labelOutCount);
       } else {
          CodeGenerator.objectFile.println("    if_icmple L" + labelOutCount);
       }
       
       return data;
    }
    
    /**
     * Generate code for greater equals comparison.
     */
    public Object visit(ASTGreaterEquals node, Object data)
    {
       // Generate operators.
       SimpleNode leftSideNode = (SimpleNode) node.jjtGetChild(0);
       SimpleNode rightSideNode = (SimpleNode) node.jjtGetChild(1);
       
       // check type
       TypeSpec leftSideType = leftSideNode.getTypeSpec();
       TypeSpec rightSideType = rightSideNode.getTypeSpec();
       
       // initially assume both children are integers
       prevCallType = "i";
       
       if (leftSideType == Predefined.realType || rightSideType == Predefined.realType) {
      	 prevCallType = "f";
       }
       leftSideNode.jjtAccept(this, data);
       if (leftSideType == Predefined.integerType && prevCallType.equals("f")) {
      	 // convert to float
          CodeGenerator.objectFile.println("    i2f");
       }
       rightSideNode.jjtAccept(this, data);
       if (rightSideType == Predefined.integerType && prevCallType.equals("f")) {
      	 // convert to float
          CodeGenerator.objectFile.println("    i2f");
       }
       if (prevCallType.equals("f")) {
      	 CodeGenerator.objectFile.println("    fcmpg");
      	 CodeGenerator.objectFile.println("    iflt L" + labelOutCount);
       } else {
          CodeGenerator.objectFile.println("    if_icmplt L" + labelOutCount);
       }
       
       return data;
    }
    
    /**
     * Generate code for equal equals comparison.
     */
    public Object visit(ASTEqualEquals node, Object data)
    {
       // Generate operators.
       SimpleNode leftSideNode = (SimpleNode) node.jjtGetChild(0);
       SimpleNode rightSideNode = (SimpleNode) node.jjtGetChild(1);
       
       // check type
       TypeSpec leftSideType = leftSideNode.getTypeSpec();
       TypeSpec rightSideType = rightSideNode.getTypeSpec();
       
       // initially assume both children are integers
       prevCallType = "i";
       
       if (leftSideType == Predefined.realType || rightSideType == Predefined.realType) {
      	 prevCallType = "f";
       }
       leftSideNode.jjtAccept(this, data);
       if (leftSideType == Predefined.integerType && prevCallType.equals("f")) {
      	 // convert to float
          CodeGenerator.objectFile.println("    i2f");
       }
       rightSideNode.jjtAccept(this, data);
       if (rightSideType == Predefined.integerType && prevCallType.equals("f")) {
      	 // convert to float
          CodeGenerator.objectFile.println("    i2f");
       }
       if (prevCallType.equals("f")) {
      	 CodeGenerator.objectFile.println("    fcmpg");
      	 CodeGenerator.objectFile.println("    ifne L" + labelOutCount);
       } else {
          CodeGenerator.objectFile.println("    if_icmpne L" + labelOutCount);
       }
       
       return data;
    }
    
    /**
     * Generate code for not equals comparison.
     */
    public Object visit(ASTNotEquals node, Object data)
    {
       // Generate operators.
       SimpleNode leftSideNode = (SimpleNode) node.jjtGetChild(0);
       SimpleNode rightSideNode = (SimpleNode) node.jjtGetChild(1);
       
       // check type
       TypeSpec leftSideType = leftSideNode.getTypeSpec();
       TypeSpec rightSideType = rightSideNode.getTypeSpec();
       
       // initially assume both children are integers
       prevCallType = "i";
       
       if (leftSideType == Predefined.realType || rightSideType == Predefined.realType) {
      	 prevCallType = "f";
       }
       leftSideNode.jjtAccept(this, data);
       if (leftSideType == Predefined.integerType && prevCallType.equals("f")) {
      	 // convert to float
          CodeGenerator.objectFile.println("    i2f");
       }
       rightSideNode.jjtAccept(this, data);
       if (rightSideType == Predefined.integerType && prevCallType.equals("f")) {
      	 // convert to float
          CodeGenerator.objectFile.println("    i2f");
       }
       if (prevCallType.equals("f")) {
      	 CodeGenerator.objectFile.println("    fcmpg");
      	 CodeGenerator.objectFile.println("    ifeq L" + labelOutCount);
       } else {
          CodeGenerator.objectFile.println("    if_icmpeq L" + labelOutCount);
       }
       
       return data;
    }
    
    public Object visit(ASTAnd node, Object data)
    {
       // Generate operators.
       SimpleNode op1 = (SimpleNode) node.jjtGetChild(0);
       SimpleNode op2 = (SimpleNode) node.jjtGetChild(1);
       op1.jjtAccept(this, data);
       op2.jjtAccept(this, data);
       
       return data;
    }
    
    public Object visit(ASTOr node, Object data)
    {
       // Generate operators.
       SimpleNode op1 = (SimpleNode) node.jjtGetChild(0);
       SimpleNode op2 = (SimpleNode) node.jjtGetChild(1);
       op1.jjtAccept(this, data);
       op2.jjtAccept(this, data);
       
       return data;
    }
    
    public Object visit(ASTFor node, Object data)
    {
       SimpleNode setter = (SimpleNode) node.jjtGetChild(0);
       SimpleNode expression = null;
       int numChildren = node.jjtGetNumChildren();
       setter.jjtAccept(this, data);
       
       CodeGenerator.objectFile.println("L" + labelInCount + ":");
       CodeGenerator.objectFile.print("    getstatic ");
       CodeGenerator.objectFile.println(CodeGenerator.programName + "/RUM I");
       CodeGenerator.objectFile.println("    ifle L" + labelOutCount);
       // Do actions in for
       for(int i = 1; i < numChildren; i++)
       {
          expression = (SimpleNode) node.jjtGetChild(i);
          expression.jjtAccept(this, data);
       }
       // Decrement the amount of rum the pirate has.
       CodeGenerator.objectFile.print("    getstatic ");
       CodeGenerator.objectFile.println(CodeGenerator.programName + "/RUM I");
       CodeGenerator.objectFile.println("    iconst_1");
       CodeGenerator.objectFile.println("    isub");
       // Store the ammount of RUM and drink again.
       CodeGenerator.objectFile.print("    putstatic ");
       CodeGenerator.objectFile.println(CodeGenerator.programName + "/RUM I");
       CodeGenerator.objectFile.println("    goto L" + labelInCount);
       
       CodeGenerator.objectFile.println("L" + labelOutCount + ":");
       CodeGenerator.objectFile.flush();
       labelInCount++;
       labelOutCount++;
       labelOut2Count++;
       return data;
    }
    
    public Object visit(ASTReturn node, Object data)
    {
    	SimpleNode dataNode = (SimpleNode) node.jjtGetChild(0);
        Object dataValue = dataNode.getAttribute(VALUE);
        
        dataNode.jjtAccept(this, data);
        if (dataValue == null) 
        {
        	// find out type of variable
           SymTabEntry variableId = (SymTabEntry) dataNode.getAttribute(ID);
        	// check type to see if need to convert the value to a string
           
           if (variableId.getTypeSpec() == Predefined.integerType)
           {
        	   CodeGenerator.objectFile.println("    ireturn");
           }
           else if (variableId.getTypeSpec() == Predefined.realType) 
           {
        	   //CodeGenerator.objectFile.println("    bipush " + variableId.getIndex());
        	   CodeGenerator.objectFile.println("    freturn");
           }
           else if (variableId.getTypeSpec() == Predefined.stringType)
           {
        	   CodeGenerator.objectFile.println("    areturn");
           }
        } 
        else if (dataValue instanceof Integer) 
        {
        	//CodeGenerator.objectFile.println("    ldc " + dataValue);
        	CodeGenerator.objectFile.println("    ireturn ");
        } 
        else if (dataValue instanceof Float) 
        {
        	//CodeGenerator.objectFile.println("    ldc " + dataValue);
        	CodeGenerator.objectFile.println("    freturn ");
        }
        else if (dataValue instanceof String) 
        {
        	//CodeGenerator.objectFile.println("    bipush " + dataValue);
        	CodeGenerator.objectFile.println("    areturn ");
        }
        CodeGenerator.objectFile.flush();
  		return data;
    }
    
    /**
     * Generate a type descriptor for a data type.
     * @param type the data type.
     * @return the type descriptor.
     */
    protected String typeDescriptor(TypeSpec type)
    {
        TypeForm form = type.getForm();
        StringBuffer buffer = new StringBuffer();

      /*  while ((form == ARRAY) && !type.isPascalString()) {
            buffer.append("[");
            type = (TypeSpec) type.getAttribute(ARRAY_ELEMENT_TYPE);
            form = type.getForm();
        } */
//TODO -- have to remove the ones we don't use
        type = type.baseType();

        if (type == Predefined.integerType) {
            buffer.append("i");
        }
        else if (type == Predefined.realType) {
            buffer.append("f");
        }
        else if (type == Predefined.booleanType) {
            buffer.append("i");
        }
        else if (type == Predefined.charType) {
            buffer.append("i");
        }
        else {
            buffer.append("a");
        }

        return buffer.toString();
    }
}
