/************************************************************************
 * A grammar to specify term rewrite systems
* Author : Arnaud Bailly
* Version : $Id: jaskell-core.y,v 1.12 2003/04/25 15:22:06 bailly Exp $
**************************************************************************/
%{
package trs;

 import java.util.*;
  import java.io.*;
 
%}


%token ID INTEGER ERROR SYM LSYM RSYM
%token  LPAREN RPAREN LBRACKET RBRACKET REWRITE COMMA SEMICOLON QUESTION_MARK BANG EQ
%token QUIT
/* keywords*/  
%token SIGNATURE RULES VARIABLES EQUATIONS
/* commands */
%token CMD_QUIT CMD_LOAD CMD_SAVE CMD_CLEAR CMD_DEBUG

%left LSYM 
%right RSYM
%nonassoc SYM

%%

rewrite:  rewrite signature 
| rewrite rewrite_rules 
| rewrite questions
| command 
| ;

signature: SIGNATURE symbols { $$ = null;} ;

symbols: symbols SEMICOLON symbol assoc {
  /* sets associativity of the symbol */
  if($4 != null) 
    ((Symbol)$3).setAssociativity(((Integer)$4).intValue());
  $$ = $3; 
} 
| symbol  assoc 
{ 
  /* sets associativity of the symbol */
  if($2 != null)  {
    ((Symbol)$1).setAssociativity(((Integer)$2).intValue());
    }
    $$ = $1; 
} ;

symbol: ID LPAREN INTEGER RPAREN
{
  Symbol sym = sig.makeSymbol((String)$1,((Integer)$3).intValue());
  $$ = sym;
}
| ID /* default arity = 0 */
{
  Symbol sym = sig.makeSymbol((String)$1,0);
  $$ = sym;
}
| SYM  LPAREN INTEGER RPAREN
{
  Symbol sym = sig.makeSymbol((String)$1,((Integer)$3).intValue());
  $$ = sym;
}
| SYM /* default arity = 2 */
{
  Symbol sym = sig.makeSymbol((String)$1,2);
  sym.setAssociativity(Signature.LEFT_ASSOC);
  $$ = sym;
}
;

/* defines associativity of a symbol */
assoc: BANG { $$ = new Integer(Signature.RIGHT_ASSOC); } 
| QUESTION_MARK { $$ = new Integer(Signature.LEFT_ASSOC); } 
| { $$ = null; } 

rewrite_rules: RULES rules { $$ = $2; } 
| EQUATIONS equations { $$ = $2; } ;

equations: equations SEMICOLON equation 
{
  $$ = $1;
}
| equation 
{
  $$ = $1;
}
; 

equation: variables term EQ term
{
  $$ = eqt.makeEquation((Term)$2,(Term)$4);
  System.err.println("Adding equation "+$$);
};


rules: rules SEMICOLON rule 
{
  $$ = $1;
}
| rule 
{
  $$ = $1;
};

rule: variables term REWRITE term
{
  $$ = rs.makeRule((Term)$2,(Term)$4);
  System.err.println("Adding rule "+$$);
};

variables: LBRACKET variable_list RBRACKET { $$ = $1; } 
| { $$ = null; } ;

variable_list: variable_list COMMA ID 
{
 try{
    $$ = sig.makeVariable((String)$3);
}catch(RewriteException ex) {
      yyerror("Variable cannot be the same as a symbol "+ $1 +" ("+lex.line()+","+lex.col()+")");
}
}  
| ID 
{
 try{
    $$ = sig.makeVariable((String)$1);
}catch(RewriteException ex) {
      yyerror("Variable cannot be the same as a symbol "+ $1 +" ("+lex.line()+","+lex.col()+")");
}
};
    

term: function { $$ = $1; }
| ID /* either a symbol or a variable */
{
  Symbol sym = sig.getSymbol((String)$1);
  if(sym == null)  /* guess this is a variable */
    try {
    $$ = sig.makeVariable((String)$1);
  }catch(RewriteException ex){
    yyerror("Unknown identifier "+ $1 +" ("+lex.line()+","+lex.col()+")");
  }
  else if(sym.arity() != 0) {
    yyerror("Identifier "+ $1 +" ("+lex.line()+","+lex.col()+") is used with wrong arity");
  } else {
    $$ = sym;
  }
}
| LPAREN term RPAREN { $$ = $2; };
	   
	
	
function: ID LPAREN term_list RPAREN 
{
  $$ = sig.makeFunction(sig.getSymbol((String)$1),(List)$3);
}
|
SYM LPAREN term_list RPAREN 
{
  $$ = sig.makeFunction(sig.getSymbol((String)$1),(List)$3);
  System.err.println("making function from symbol "+(String)$1);
}
/*
| 
SYM term 
{
  List l = new ArrayList();
  l.add($2);
  $$ = sig.makeFunction(sig.getSymbol((String)$1),l);
  System.err.println("making function from symbol "+(String)$1);
}*/
|
term LSYM term 
{
  Symbol sym = sig.getSymbol((String)$2);
  sym.setInfix(true);
  $$ = sig.makeFunction(sym,new Term[]{(Term)$1,(Term)$3});
  System.err.println("making function from symbol "+(String)$2);
}
|
term RSYM term 
{
  Symbol sym = sig.getSymbol((String)$2);
  sym.setInfix(true);
  $$ = sig.makeFunction(sym,new Term[]{(Term)$1,(Term)$3});
  System.err.println("making function from symbol "+(String)$2);
}
/*|
term SYM term 
{
  Symbol sym = sig.getSymbol((String)$2);
  sym.setInfix(true);
  $$ = sig.makeFunction(sym,new Term[]{(Term)$1,(Term)$3});
}*/
; 

term_list: term_list COMMA term { ((List)$1).add($3); $$ = $1; } 
| term { List l = new ArrayList(); l.add($1); $$ = l; } ;

questions: QUESTION_MARK term { this.command.rewrite((Term)$2); }  /* simple rewrite */
| QUESTION_MARK term EQ term { this.command.equalize((Term)$2,(Term)$4); } /* equality proof */;

command: CMD_QUIT { this.command.quit();  }
| CMD_LOAD {this.command.load($1.toString());  }
| CMD_SAVE {this.command.save($1.toString());  }
| CMD_CLEAR {this.command.clear(); }
| CMD_DEBUG {this.command.debug();};


%%


/** the lexer we use */
private Yylex lex;

/** the generated system */
private RewriteSystem rs;

/** an equational theory */
private EquationalTheory eqt;

/** current list of variables */
private List varlist;

/** last result of rewriting */
private Term result;

/** current engine */
private Engine engine;

/** current command shell */
 private Command command;

public Term getResult() {
  return result;
}

public Symbol getSymbol(String name) {
  return sig.getSymbol(name);
}

private Signature sig;

public Signature getSignature() {
return sig;
}

public void setSignature(Signature sig) {
this.sig = sig;
}

/**
 * Returns the gerneate rewrite system
 */
public RewriteSystem getRewriteSystem() {
  return rs;
}

public void setRewriteSystem(RewriteSystem rs) {
  this.rs = rs;
}

public EquationalTheory getEquationalTheory() {
  return eqt;
}

public void setEquationalTheory(EquationalTheory eqt) {
  this.eqt = eqt;
}

public void setEngine(Engine engine ) {
  this.engine = engine;
}

public void setCommand(Command command) {
  this.command= command;
}

/**
 * Method parse.
 * @param is
 */
public void parse(InputStream is) {
  lex = new Yylex(is);
  lex.parser = this;
  /* start parsing */
  this.yyparse();
}

/**
 * Method parse.
 * @param rd
 */
public void parse(Reader rd) {
  lex = new Yylex(rd);
  lex.parser = this;
  /* start parsing */
  this.yyparse();
}

public int yylex() {
  try {
    return lex.yylex();
  } catch (IOException ioex) {
    ioex.printStackTrace();
    return -1;
  }
}

 
public void yyerror(String msg) {
  System.err.println(msg);
}


