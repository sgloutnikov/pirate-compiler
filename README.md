Pirate Compiler
================

The Pirate programming language has a JavaCC grammar consisting of pirate terms that correspond to common language constructs, such as �if�, �while�, and so on.  JavaCC is used to generate a scanner and parser for the language, which results in a parse tree consisting of the nonterminals of the grammar.  JJTree is used to preprocess the JavaCC grammar to construct an Abstract Syntax Tree (AST) when the parser is generated and executed with a .arr program file.  The grammar for the Pirate source language is defined in Pirate.jjt using methods to represent each EBNF statement of the grammar.   

The corresponding Pirate Compiler is a four-pass compiler: the first pass builds an AST, the second pass decorates the AST with type information, the third pass generates Jasmin assembly code, and the fourth pass assembles the object (.j) file to get a Java .class file that can be run with the Java Virtual Machine (JVM).  The type setting of variables in the AST and the Jasmin code generation is made possible through use of the Visitor pattern, which does a post-order traversal of the tree.  JJTree generates AST nodes that represent each nonterminal of the grammar, and the Visitor pattern is used to visit each node.  The Pirate Compiler generates corresponding Jasmin instructions based on the type of a node. 

The compiler utilizes the symbol table and intermediate code node design as described in Ronald Mak�s �Writing Compilers and Interpreters: A Software Engineering Approach�.  The symbol table stack keeps track of all of the identifiers and their corresponding type and attributes (based on the definition of the identifier).  The intermediate code node is integrated with the AST by extending the Node interface generated by JJTree to the ICodeNode.  This allows the compiler to reuse classes such as the Cross Reference and Parse Tree printer that was already created

The compiler outputs a Jasmin .j object file when it parses a source file written in the Pirate language.  This .j file can be assembled using the Jasmin assembler (http://jasmin.sourceforge.net/), which generates a .class file.  Jasmin is an assembler for the JVM, the target machine.  Any computer that supports Java can run the .class file.

For more information and details refer to Tokenizers-FinalProjectReport.doc