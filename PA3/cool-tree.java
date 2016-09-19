// -*- mode: java -*-
//
// file: cool-tree.m4
//
// This file defines the AST
//
//////////////////////////////////////////////////////////

import java.util.Enumeration;
import java.io.PrintStream;
import java.util.Vector;
import java.util.Hashtable;

//Variables globales
class tbl{
  public static ClassTable cT;
  public static SymbolTable mapa;

  public static Hashtable<String,String> cyh;
  public static Hashtable<String,Hashtable<String,Vector<String>>> cym;
  public static Hashtable<String,Vector<String>> myf;
  public static Hashtable<String,Hashtable<String,AbstractSymbol>> cyo;
  public static Hashtable<String,AbstractSymbol>oyt;
}

/** Defines simple phylum Program */
abstract class Program extends TreeNode {
    protected Program(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);
    public abstract void semant();

}


/** Defines simple phylum Class_ */
abstract class Class_ extends TreeNode {
    protected Class_(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);
    public abstract void semant(class_c clase);

}


/** Defines list phylum Classes*/
class Classes extends ListNode {
    public final static Class elementClass = Class_.class;
    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }
    protected Classes(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }
    /** Creates an empty "Classes" list */
    public Classes(int lineNumber) {
        super(lineNumber);
    }
    /** Appends "Class_" element to this list */
    public Classes appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }
    public TreeNode copy() {
        return new Classes(lineNumber, copyElements());
    }
}


/** Defines simple phylum Feature */
abstract class Feature extends TreeNode {
    protected Feature(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);
    public abstract void semant(class_c clase);

}


/** Defines list phylum Features*/
class Features extends ListNode {
    public final static Class elementClass = Feature.class;
    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }
    protected Features(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }
    /** Creates an empty "Features" list */
    public Features(int lineNumber) {
        super(lineNumber);
    }
    /** Appends "Feature" element to this list */
    public Features appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }
    public TreeNode copy() {
        return new Features(lineNumber, copyElements());
    }
}


/** Defines simple phylum Formal */
abstract class Formal extends TreeNode {
    protected Formal(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);

}


/** Defines list phylum Formals */
class Formals extends ListNode {
    public final static Class elementClass = Formal.class;
    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }
    protected Formals(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }
    /** Creates an empty "Formals" list */
    public Formals(int lineNumber) {
        super(lineNumber);
    }
    /** Appends "Formal" element to this list */
    public Formals appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }
    public TreeNode copy() {
        return new Formals(lineNumber, copyElements());
    }
}


/** Defines simple phylum Expression */
abstract class Expression extends TreeNode {
    protected Expression(int lineNumber) {
        super(lineNumber);
    }
    private AbstractSymbol type = null;
    public AbstractSymbol get_type() { return type; }
    public Expression set_type(AbstractSymbol s) { type = s; return this; }
    public abstract void dump_with_types(PrintStream out, int n);
    public void dump_type(PrintStream out, int n) {
        if (type != null)
            { out.println(Utilities.pad(n) + ": " + type.getString()); }
        else
            { out.println(Utilities.pad(n) + ": _no_type"); }
    }
    public abstract void semant(class_c clase);

}


/** Defines list phylum Expressions */
class Expressions extends ListNode {
    public final static Class elementClass = Expression.class;
    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }
    protected Expressions(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }
    /** Creates an empty "Expressions" list */
    public Expressions(int lineNumber) {
        super(lineNumber);
    }
    /** Appends "Expression" element to this list */
    public Expressions appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }
    public TreeNode copy() {
        return new Expressions(lineNumber, copyElements());
    }
}


/** Defines simple phylum Case */
abstract class Case extends TreeNode {
    protected Case(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);
    public abstract void semant(class_c clase);

}


/** Defines list phylum Cases */
class Cases extends ListNode {
    public final static Class elementClass = Case.class;
    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }
    protected Cases(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }
    /** Creates an empty "Cases" list */
    public Cases(int lineNumber) {
        super(lineNumber);
    }
    /** Appends "Case" element to this list */
    public Cases appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }
    public TreeNode copy() {
        return new Cases(lineNumber, copyElements());
    }
}


/** Defines AST constructor 'programc'.*/
class programc extends Program {
    protected Classes classes;
    /** Creates "programc" AST node.
      *dfsadfsd
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for classes
      */
    public programc(int lineNumber, Classes a1) {
        super(lineNumber);
        classes = a1;
    }
    public TreeNode copy() {
        return new programc(lineNumber, (Classes)classes.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "programc\n");
        classes.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_program");
        for (Enumeration e = classes.getElements(); e.hasMoreElements(); ) {
            // sm: changed 'n + 1' to 'n + 2' to match changes elsewhere
	    ((Class_)e.nextElement()).dump_with_types(out, n + 2);
        }
    }
    /** Check that the program is semantically correct
	<li>Decorate the abstract syntax tree with type information
        by setting the type field in each Expression node.
        (see tree.h)
	</ol>
	<p>
	You are free to first do (1) and make sure you catch all semantic
    	errors. Part (2) can be done in a second stage when you want
	to test the complete compiler.
    */
    public void semant() {
      //Inicializar las variables globales
      tbl.cyh = new Hashtable<String,String>();
      tbl.cym = new Hashtable<String,Hashtable<String,Vector<String>>>();
      tbl.myf = new Hashtable<String,Vector<String>>();
      tbl.cyo = new Hashtable<String,Hashtable<String,AbstractSymbol>>();
      tbl.oyt = new Hashtable<String,AbstractSymbol>();
      tbl.cT = new ClassTable(classes);
      tbl.mapa = new SymbolTable();

      //----------PRIMER RECORRIDO CLASES-----------
      for(int i = classes.getLength()-1; i >= 0; i--){
        class_c clase = (class_c) classes.getNth(i);
        String clasestr = clase.name.toString();
        String padrestr = clase.parent.toString();

        //No se puede redefinir clases bases
        if(clasestr.equals("Object") | clasestr.equals("Bool")
          | clasestr.equals("Int") | clasestr.equals("String")
          | clasestr.equals("SELF_TYPE") | clasestr.equals("IO")){
          SemantErrors.basicClassRedefined(clase.name,
            tbl.cT.semantError(clase));
        }

        if (tbl.cT.errors()) {
      	    System.err.println("Compilation halted due to static semantic errors.");
      	    System.exit(1);
      	}

        //No se puede heredar de estos 3 casos
        if(padrestr.equals("Bool")| padrestr.equals("Int")
                | padrestr.equals("String")){
          SemantErrors.cannotInheritClass(clase.name,
                clase.parent, tbl.cT.semantError(clase));
        }

        else{
          //Se duplica?
          if(tbl.cyh.containsKey(clasestr)){
            SemantErrors.classPreviouslyDefined(clase.name,
                    tbl.cT.semantError(clase));
          }
          //Llenar el hashtable
          else{
            tbl.cyh.put(clasestr,padrestr);
          }
        }
      }
      //------------------------------------------------

      if (tbl.cT.errors()) {
          System.err.println("Compilation halted due to static semantic errors.");
          System.exit(1);
      }

      //Añadir el padre de IO que es Object
      tbl.cyh.put(TreeConstants.IO.toString(),TreeConstants.Object_.toString());

      //------------- SEGUNDO RECORRIDO CLASES---------------

      for(int i = classes.getLength()-1; i >= 0; i--){
        class_c clase = (class_c) classes.getNth(i);
        //Undefined class
        if(!clase.parent.toString().equals("Object")){
          if(!tbl.cyh.containsKey(clase.parent.toString())){
            SemantErrors.inheritsFromAnUndefinedClass(clase.name,
            clase.parent,  tbl.cT.semantError(clase));
          }
        }
        //Has cycle?
        else if(tbl.cT.searchCycleClasses(clase.name.toString(), tbl.cyh)){
          SemantErrors.inheritanceCycle(clase.name,
              tbl.cT.semantError(clase));
        }
        tbl.cT.v.clear();
      }

      //---------------------------------------------------------

      if (tbl.cT.errors()) {
    	    System.err.println("Compilation halted due to static semantic errors.");
    	    System.exit(1);
    	}

      //Main no esta definido
      if(!tbl.cyh.containsKey("Main")){
        SemantErrors.noClassMain(tbl.cT.semantError());
      }

	    if (tbl.cT.errors()) {
	    System.err.println("Compilation halted due to static semantic errors.");
	    System.exit(1);
	    }


      //----------------RECORRER EL ARBOL---------------------------
      class_c mainclase = null;
      tbl.cT.v.add("Object");
      Hashtable<Integer,class_c> clord = new Hashtable<Integer,class_c>();

      for(int i = 0; i < classes.getLength(); i++){
        class_c clase = (class_c) classes.getNth(i);
        String namestr = clase.name.toString();
        tbl.cT.ordenarClases(namestr, tbl.cyh);
        clord.put(tbl.cT.v.indexOf(namestr), clase);
        if(namestr.equals("Main"))
          mainclase = (class_c) clase;
      }

      tbl.mapa.enterScope();
      class_c a_io = tbl.cT.IO_class;
      class_c a_int = tbl.cT.Int_class;
      class_c a_bool = tbl.cT.Bool_class;
      class_c a_str = tbl.cT.Str_class;

      tbl.mapa.addId(a_io.name, TreeConstants.Object_);
      tbl.mapa.addId(a_int.name, TreeConstants.Object_);
      tbl.mapa.addId(a_bool.name, TreeConstants.Object_);
      tbl.mapa.addId(a_str.name, TreeConstants.Object_);

      for(int i = 1; i < tbl.cT.v.size(); i++){
        class_c clase = clord.get(i);
        clase.semant(clase);
      }

      tbl.mapa.exitScope();

      //Si la clase Main no contiene el metodo main
      if(!tbl.cym.get("Main").containsKey("main"))
        SemantErrors.noMainMethodInMainClass(tbl.cT.semantError());

      //Si el metodo main en la clase main tiene parametros
      else
        if(!tbl.cym.get("Main").get("main").isEmpty())
          SemantErrors.mainMethodNoArgs(tbl.cT.semantError(mainclase));

      if (tbl.cT.errors()) {
      System.err.println("Compilation halted due to static semantic errors.");
      System.exit(1);
      }

    }
}


/** Defines AST constructor 'class_c'. */
class class_c extends Class_ {
    protected AbstractSymbol name;
    protected AbstractSymbol parent;
    protected Features features;
    protected AbstractSymbol filename;
    /** Creates "class_c" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for parent
      * @param a2 initial value for features
      * @param a3 initial value for filename
      */
    public class_c(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Features a3, AbstractSymbol a4) {
        super(lineNumber);
        name = a1;
        parent = a2;
        features = a3;
        filename = a4;
    }
    public TreeNode copy() {
        return new class_c(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(parent), (Features)features.copy(), copy_AbstractSymbol(filename));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "class_c\n");
        dump_AbstractSymbol(out, n+2, name);
        dump_AbstractSymbol(out, n+2, parent);
        features.dump(out, n+2);
        dump_AbstractSymbol(out, n+2, filename);
    }

    public void semant(class_c clase){
      Vector<attr> vattr = new Vector<attr>();
      Vector<method> vmethod = new Vector<method>();

      for(int i = 0; i  < features.getLength(); i++){
        Feature cosa = (Feature)features.getNth(i);

        if(cosa instanceof attr){
          attr atributo = (attr) cosa;
          if(vattr.contains(atributo.name.toString()))
            SemantErrors.attrMultiplyDefined(atributo.name,tbl.cT.semantError(clase));
          vattr.add(atributo);
          if(atributo.name.toString().equals("self")){
            SemantErrors.selfCannotBeTheNameOfAttr(tbl.cT.semantError(clase));
          }
        }

        else{
          method metodo = (method) cosa;
          vmethod.add(metodo);
        }
      }

      for(int i = 0; i < vattr.size(); i++){
        vattr.get(i).semant(clase);
      }

      for(int i = 0; i < vmethod.size(); i++){
        vmethod.get(i).semant(clase);
      }
    }


    public AbstractSymbol getFilename() { return filename; }
    public AbstractSymbol getName()     { return name; }
    public AbstractSymbol getParent()   { return parent; }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_class");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, parent);
        out.print(Utilities.pad(n + 2) + "\"");
        Utilities.printEscapedString(out, filename.getString());
        out.println("\"\n" + Utilities.pad(n + 2) + "(");
        for (Enumeration e = features.getElements(); e.hasMoreElements();) {
	    ((Feature)e.nextElement()).dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
    }

}


/** Defines AST constructor 'method'.*/
class method extends Feature {
    protected AbstractSymbol name;
    protected Formals formals;
    protected AbstractSymbol return_type;
    protected Expression expr;
    /** Creates "method" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for formals
      * @param a2 initial value for return_type
      * @param a3 initial value for expr
      */
    public method(int lineNumber, AbstractSymbol a1, Formals a2, AbstractSymbol a3, Expression a4) {
        super(lineNumber);
        name = a1;
        formals = a2;
        return_type = a3;
        expr = a4;
    }
    public TreeNode copy() {
        return new method(lineNumber, copy_AbstractSymbol(name), (Formals)formals.copy(), copy_AbstractSymbol(return_type), (Expression)expr.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "method\n");
        dump_AbstractSymbol(out, n+2, name);
        formals.dump(out, n+2);
        dump_AbstractSymbol(out, n+2, return_type);
        expr.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_method");
        dump_AbstractSymbol(out, n + 2, name);
        for (Enumeration e = formals.getElements(); e.hasMoreElements();) {
	    ((Formal)e.nextElement()).dump_with_types(out, n + 2);
        }
        dump_AbstractSymbol(out, n + 2, return_type);
	expr.dump_with_types(out, n + 2);
    }

    public void semant(class_c clase){
      Vector<String> vfirmas = new Vector<String>();

      tbl.mapa.enterScope();
      for(int i = 0;i < formals.getLength(); i++){
        formalc firma = (formalc) formals.getNth(i);

        if(TreeConstants.self.equals(firma.name))
          SemantErrors.formalCannotBeSelf(tbl.cT.semantError(clase));

        if(TreeConstants.SELF_TYPE.equals(firma.type_decl))
          SemantErrors.formalParamCannotHaveTypeSELF_TYPE(
            firma.name,tbl.cT.semantError());

        if(vfirmas.contains(firma.name.toString()))
          SemantErrors.formalMultiplyDefined(firma.name, tbl.cT.semantError(clase));

        else{
          vfirmas.add(firma.name.toString());
          tbl.mapa.addId(firma.name,firma.type_decl);
        }
      }

      tbl.myf.put(name.toString(), vfirmas);
      tbl.cym.put(clase.name.toString(), tbl.myf);
      tbl.mapa.enterScope();
      expr.semant(clase);
      AbstractSymbol t = expr.get_type();
      if(!return_type.equals(t) && !TreeConstants.Object_.equals(return_type))
        SemantErrors.diffReturnType(t,name,return_type,tbl.cT.semantError(clase));
      tbl.mapa.exitScope();
      tbl.mapa.exitScope();

    }
}


/** Defines AST constructor 'attr'.*/
class attr extends Feature {
    protected AbstractSymbol name;
    protected AbstractSymbol type_decl;
    protected Expression init;
    /** Creates "attr" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for type_decl
      * @param a2 initial value for init
      */
    public attr(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Expression a3) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
        init = a3;
    }
    public TreeNode copy() {
        return new attr(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(type_decl), (Expression)init.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "attr\n");
        dump_AbstractSymbol(out, n+2, name);
        dump_AbstractSymbol(out, n+2, type_decl);
        init.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_attr");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
	init.dump_with_types(out, n + 2);
    }

    public void semant(class_c clase){
      tbl.mapa.addId(name, type_decl);
      tbl.mapa.enterScope();
      init.semant(clase);
      tbl.mapa.exitScope();

      AbstractSymbol t = init.get_type();;
      if((!TreeConstants.No_type.equals(t)) &&
      (!TreeConstants.Object_.equals(type_decl)))
        if(!type_decl.equals(t))
          SemantErrors.diffInitType(t,name,type_decl,tbl.cT.semantError(clase));
    }

}


/** Defines AST constructor 'formalc'.*/
class formalc extends Formal {
    protected AbstractSymbol name;
    protected AbstractSymbol type_decl;
    /** Creates "formalc" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for type_decl
      */
    public formalc(int lineNumber, AbstractSymbol a1, AbstractSymbol a2) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
    }
    public TreeNode copy() {
        return new formalc(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(type_decl));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "formalc\n");
        dump_AbstractSymbol(out, n+2, name);
        dump_AbstractSymbol(out, n+2, type_decl);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_formal");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
    }

}


/** Defines AST constructor 'branch'. */
class branch extends Case {
    protected AbstractSymbol name;
    protected AbstractSymbol type_decl;
    protected Expression expr;
    /** Creates "branch" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for type_decl
      * @param a2 initial value for expr
      */
    public branch(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Expression a3) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
        expr = a3;
    }
    public TreeNode copy() {
        return new branch(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(type_decl), (Expression)expr.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "branch\n");
        dump_AbstractSymbol(out, n+2, name);
        dump_AbstractSymbol(out, n+2, type_decl);
        expr.dump(out, n+2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_branch");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
	expr.dump_with_types(out, n + 2);
    }

    public void semant(class_c clase){
      tbl.mapa.addId(name,type_decl);
      expr.semant(clase);
    }

}


/** Defines AST constructor 'assign'. */
class assign extends Expression {
    protected AbstractSymbol name;
    protected Expression expr;
    /** Creates "assign" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for expr
      */
    public assign(int lineNumber, AbstractSymbol a1, Expression a2) {
        super(lineNumber);
        name = a1;
        expr = a2;
    }
    public TreeNode copy() {
        return new assign(lineNumber, copy_AbstractSymbol(name), (Expression)expr.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "assign\n");
        dump_AbstractSymbol(out, n+2, name);
        expr.dump(out, n+2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_assign");
        dump_AbstractSymbol(out, n + 2, name);
	expr.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    public void semant(class_c clase){
      expr.semant(clase);
      set_type(expr.get_type());
    }

}


/** Defines AST constructor 'static_dispatch'. */
class static_dispatch extends Expression {
    protected Expression expr;
    protected AbstractSymbol type_name;
    protected AbstractSymbol name;
    protected Expressions actual;
    /** Creates "static_dispatch" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for expr
      * @param a1 initial value for type_name
      * @param a2 initial value for name
      * @param a3 initial value for actual
      */
    public static_dispatch(int lineNumber, Expression a1, AbstractSymbol a2, AbstractSymbol a3, Expressions a4) {
        super(lineNumber);
        expr = a1;
        type_name = a2;
        name = a3;
        actual = a4;
    }
    public TreeNode copy() {
        return new static_dispatch(lineNumber, (Expression)expr.copy(), copy_AbstractSymbol(type_name), copy_AbstractSymbol(name), (Expressions)actual.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "static_dispatch\n");
        expr.dump(out, n+2);
        dump_AbstractSymbol(out, n+2, type_name);
        dump_AbstractSymbol(out, n+2, name);
        actual.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_static_dispatch");
	expr.dump_with_types(out, n + 2);
        dump_AbstractSymbol(out, n + 2, type_name);
        dump_AbstractSymbol(out, n + 2, name);
        out.println(Utilities.pad(n + 2) + "(");
        for (Enumeration e = actual.getElements(); e.hasMoreElements();) {
	    ((Expression)e.nextElement()).dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
	dump_type(out, n);
    }

    public void semant(class_c clase){
      expr.semant(clase);
      for(int i = 0; i < actual.getLength(); i++){
        Expression expresion = (Expression) actual.getNth(i);
        expresion.semant(clase);
      }
    }

}


/** Defines AST constructor 'dispatch'.*/
class dispatch extends Expression {
    protected Expression expr;
    protected AbstractSymbol name;
    protected Expressions actual;
    /** Creates "dispatch" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for expr
      * @param a1 initial value for name
      * @param a2 initial value for actual
      */
    public dispatch(int lineNumber, Expression a1, AbstractSymbol a2, Expressions a3) {
        super(lineNumber);
        expr = a1;
        name = a2;
        actual = a3;
    }
    public TreeNode copy() {
        return new dispatch(lineNumber, (Expression)expr.copy(), copy_AbstractSymbol(name), (Expressions)actual.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "dispatch\n");
        expr.dump(out, n+2);
        dump_AbstractSymbol(out, n+2, name);
        actual.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_dispatch");
	expr.dump_with_types(out, n + 2);
        dump_AbstractSymbol(out, n + 2, name);
        out.println(Utilities.pad(n + 2) + "(");
        for (Enumeration e = actual.getElements(); e.hasMoreElements();) {
	    ((Expression)e.nextElement()).dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
	dump_type(out, n);
    }

    public void semant(class_c clase){
      expr.semant(clase);
      for(int i = 0; i < actual.getLength();i++){
        Expression expresion = (Expression) actual.getNth(i);
        expresion.semant(clase);
      }
    }

}


/** Defines AST constructor 'cond'. */
class cond extends Expression {
    protected Expression pred;
    protected Expression then_exp;
    protected Expression else_exp;
    /** Creates "cond" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for pred
      * @param a1 initial value for then_exp
      * @param a2 initial value for else_exp
      */
    public cond(int lineNumber, Expression a1, Expression a2, Expression a3) {
        super(lineNumber);
        pred = a1;
        then_exp = a2;
        else_exp = a3;
    }
    public TreeNode copy() {
        return new cond(lineNumber, (Expression)pred.copy(), (Expression)then_exp.copy(), (Expression)else_exp.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "cond\n");
        pred.dump(out, n+2);
        then_exp.dump(out, n+2);
        else_exp.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_cond");
	pred.dump_with_types(out, n + 2);
	then_exp.dump_with_types(out, n + 2);
	else_exp.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    public void semant(class_c clase){
      pred.semant(clase);
      if(TreeConstants.Bool.equals(pred.get_type()))
        SemantErrors.ifNoBoolPredicate(tbl.cT.semantError(clase));

      then_exp.semant(clase);
      else_exp.semant(clase);
      AbstractSymbol t1 = then_exp.get_type();
      AbstractSymbol t2 = else_exp.get_type();


    }
}


/** Defines AST constructor 'loop'.*/
class loop extends Expression {
    protected Expression pred;
    protected Expression body;
    /** Creates "loop" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for pred
      * @param a1 initial value for body
      */
    public loop(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        pred = a1;
        body = a2;
    }
    public TreeNode copy() {
        return new loop(lineNumber, (Expression)pred.copy(), (Expression)body.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "loop\n");
        pred.dump(out, n+2);
        body.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_loop");
	pred.dump_with_types(out, n + 2);
	body.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    public void semant(class_c clase){
      pred.semant(clase);
      if(TreeConstants.Bool.equals(pred.get_type()))
        pred.set_type(TreeConstants.Bool);
      else
        SemantErrors.whileNoBoolCondition(tbl.cT.semantError(clase));
      body.semant(clase);
      set_type(TreeConstants.Object_);
    }

}


/** Defines AST constructor 'typcase'. */
class typcase extends Expression {
    protected Expression expr;
    protected Cases cases;
    /** Creates "typcase" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for expr
      * @param a1 initial value for cases
      */
    public typcase(int lineNumber, Expression a1, Cases a2) {
        super(lineNumber);
        expr = a1;
        cases = a2;
    }
    public TreeNode copy() {
        return new typcase(lineNumber, (Expression)expr.copy(), (Cases)cases.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "typcase\n");
        expr.dump(out, n+2);
        cases.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_typcase");
	expr.dump_with_types(out, n + 2);
        for (Enumeration e = cases.getElements(); e.hasMoreElements();) {
	    ((Case)e.nextElement()).dump_with_types(out, n + 2);
        }
	dump_type(out, n);
    }

    public void semant(class_c clase){
      Vector<String> vbranch = new Vector<String>();
      expr.semant(clase);
      tbl.mapa.enterScope();
      for(int i = 0; i < cases.getLength(); i++){
        branch caso = (branch) cases.getNth(i);
        String branchstr = caso.type_decl.toString();
        if(vbranch.contains(branchstr))
          SemantErrors.duplicateBranch(caso.type_decl,tbl.cT.semantError(clase));
        vbranch.add(branchstr) ;
        caso.semant(clase);
      }
      set_type(TreeConstants.Object_);
      tbl.mapa.exitScope();
    }

}


/** Defines AST constructor 'block'. */
class block extends Expression {
    protected Expressions body;
    /** Creates "block" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for body
      */
    public block(int lineNumber, Expressions a1) {
        super(lineNumber);
        body = a1;
    }
    public TreeNode copy() {
        return new block(lineNumber, (Expressions)body.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "block\n");
        body.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_block");
        for (Enumeration e = body.getElements(); e.hasMoreElements();) {
	    ((Expression)e.nextElement()).dump_with_types(out, n + 2);
        }
	dump_type(out, n);
    }

    public void semant(class_c clase){
      Expression expr = (Expression) body.getNth(0);
      for(int i = 0; i < body.getLength();i++){
        expr = (Expression) body.getNth(i);
        expr.semant(clase);
      }
      set_type(expr.get_type());
    }

}


/** Defines AST constructor 'let'.*/
class let extends Expression {
    protected AbstractSymbol identifier;
    protected AbstractSymbol type_decl;
    protected Expression init;
    protected Expression body;
    /** Creates "let" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for identifier
      * @param a1 initial value for type_decl
      * @param a2 initial value for init
      * @param a3 initial value for body
      */
    public let(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Expression a3, Expression a4) {
        super(lineNumber);
        identifier = a1;
        type_decl = a2;
        init = a3;
        body = a4;
    }
    public TreeNode copy() {
        return new let(lineNumber, copy_AbstractSymbol(identifier), copy_AbstractSymbol(type_decl), (Expression)init.copy(), (Expression)body.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "let\n");
        dump_AbstractSymbol(out, n+2, identifier);
        dump_AbstractSymbol(out, n+2, type_decl);
        init.dump(out, n+2);
        body.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_let");
	dump_AbstractSymbol(out, n + 2, identifier);
	dump_AbstractSymbol(out, n + 2, type_decl);
	init.dump_with_types(out, n + 2);
	body.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    public void semant(class_c clase){
      init.semant(clase);
      body.semant(clase);
      //set_type(body.get_type());
    }

}


/** Defines AST constructor 'plus'. */
class plus extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "plus" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public plus(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new plus(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "plus\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_plus");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    public void semant(class_c clase){
      e1.semant(clase);
      e2.semant(clase);
      AbstractSymbol t1 = e1.get_type();
      AbstractSymbol t2 = e2.get_type();

      if(TreeConstants.Int.equals(t1) && TreeConstants.Int.equals(t2))
        set_type(TreeConstants.Int);

      else
        SemantErrors.noIntArguments(t1,t2,"+",tbl.cT.semantError(clase));
    }

}


/** Defines AST constructor 'sub'.*/
class sub extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "sub" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public sub(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new sub(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "sub\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_sub");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    public void semant(class_c clase){
      e1.semant(clase);
      e2.semant(clase);
      AbstractSymbol t1 = e1.get_type();
      AbstractSymbol t2 = e2.get_type();

      if(TreeConstants.Int.equals(t1) && TreeConstants.Int.equals(t2))
        set_type(TreeConstants.Int);

      else
        SemantErrors.noIntArguments(t1,t2,"-",tbl.cT.semantError(clase));
    }

}


/** Defines AST constructor 'mul'.*/
class mul extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "mul" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public mul(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new mul(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "mul\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_mul");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    public void semant(class_c clase){
      e1.semant(clase);
      e2.semant(clase);
      AbstractSymbol t1 = e1.get_type();
      AbstractSymbol t2 = e2.get_type();

      if(TreeConstants.Int.equals(t1) && TreeConstants.Int.equals(t2))
        set_type(TreeConstants.Int);

      else
        SemantErrors.noIntArguments(t1,t2,"*",tbl.cT.semantError(clase));
    }

}


/** Defines AST constructor 'divide'.*/
class divide extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "divide" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public divide(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new divide(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "divide\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_divide");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    public void semant(class_c clase){
      e1.semant(clase);
      e2.semant(clase);
      AbstractSymbol t1 = e1.get_type();
      AbstractSymbol t2 = e2.get_type();

      if(TreeConstants.Int.equals(t1) && TreeConstants.Int.equals(t2))
        set_type(TreeConstants.Int);

      else
        SemantErrors.noIntArguments(t1,t2,"/",tbl.cT.semantError(clase));
    }

}


/** Defines AST constructor 'neg'.*/
class neg extends Expression {
    protected Expression e1;
    /** Creates "neg" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      */
    public neg(int lineNumber, Expression a1) {
        super(lineNumber);
        e1 = a1;
    }
    public TreeNode copy() {
        return new neg(lineNumber, (Expression)e1.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "neg\n");
        e1.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_neg");
	e1.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    public void semant(class_c clase){
      e1.semant(clase);
      AbstractSymbol t = e1.get_type();

      if(TreeConstants.Int.equals(t))
        set_type(TreeConstants.Int);
      else
        SemantErrors.argumentOfNegNoIntType(t,tbl.cT.semantError(clase));
    }

}


/** Defines AST constructor 'lt'.*/
class lt extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "lt" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public lt(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new lt(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "lt\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_lt");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    public void semant(class_c clase){
      e1.semant(clase);
      e2.semant(clase);
      if(e1.get_type().equals(e2.get_type()))
        set_type(TreeConstants.Bool);
      else
        SemantErrors.illegalCompWithABasicType(tbl.cT.semantError(clase));
    }

}


/** Defines AST constructor 'eq'.*/
class eq extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "eq" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public eq(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new eq(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "eq\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_eq");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    public void semant(class_c clase){
      e1.semant(clase);
      e2.semant(clase);
      if(e1.get_type().equals(e2.get_type()))
        set_type(TreeConstants.Bool);
      else
        SemantErrors.illegalCompWithABasicType(tbl.cT.semantError(clase));
    }

}


/** Defines AST constructor 'leq'.*/
class leq extends Expression {
    protected Expression e1;
    protected Expression e2;
    /** Creates "leq" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public leq(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new leq(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "leq\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_leq");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    public void semant(class_c clase){
      e1.semant(clase);
      e2.semant(clase);
      if(e1.get_type().equals(e2.get_type()))
        set_type(TreeConstants.Bool);
      else
        SemantErrors.illegalCompWithABasicType(tbl.cT.semantError(clase));
    }

}


/** Defines AST constructor 'comp */
class comp extends Expression {
    protected Expression e1;
    /** Creates "comp" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      */
    public comp(int lineNumber, Expression a1) {
        super(lineNumber);
        e1 = a1;
    }
    public TreeNode copy() {
        return new comp(lineNumber, (Expression)e1.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "comp\n");
        e1.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_comp");
	e1.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    public void semant(class_c clase){
      e1.semant(clase);
      AbstractSymbol t = e1.get_type();
      if(TreeConstants.Bool.equals(t))
        set_type(TreeConstants.Bool);
      else
        SemantErrors.notNotBoolType(t, tbl.cT.semantError(clase));
    }

}


/** Defines AST constructor 'int_const'. */
class int_const extends Expression {
    protected AbstractSymbol token;
    /** Creates "int_const" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for token
      */
    public int_const(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        token = a1;
    }
    public TreeNode copy() {
        return new int_const(lineNumber, copy_AbstractSymbol(token));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "int_const\n");
        dump_AbstractSymbol(out, n+2, token);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_int");
	dump_AbstractSymbol(out, n + 2, token);
	dump_type(out, n);
    }

    public void semant(class_c clase){
      set_type(TreeConstants.Int);
    }

}


/** Defines AST constructor 'bool_const'.*/
class bool_const extends Expression {
    protected Boolean val;
    /** Creates "bool_const" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for val
      */
    public bool_const(int lineNumber, Boolean a1) {
        super(lineNumber);
        val = a1;
    }
    public TreeNode copy() {
        return new bool_const(lineNumber, copy_Boolean(val));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "bool_const\n");
        dump_Boolean(out, n+2, val);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_bool");
	dump_Boolean(out, n + 2, val);
	dump_type(out, n);
    }

    public void semant(class_c clase){
      set_type(TreeConstants.Bool);
    }

}


/** Defines AST constructor 'string_const'.*/
class string_const extends Expression {
    protected AbstractSymbol token;
    /** Creates "string_const" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for token
      */
    public string_const(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        token = a1;
    }
    public TreeNode copy() {
        return new string_const(lineNumber, copy_AbstractSymbol(token));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "string_const\n");
        dump_AbstractSymbol(out, n+2, token);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_string");
	out.print(Utilities.pad(n + 2) + "\"");
	Utilities.printEscapedString(out, token.getString());
	out.println("\"");
	dump_type(out, n);
    }

    public void semant(class_c clase){
      set_type(TreeConstants.Str);
    }

}


/** Defines AST constructor 'new_'.*/
class new_ extends Expression {
    protected AbstractSymbol type_name;
    /** Creates "new_" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for type_name
      */
    public new_(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        type_name = a1;
    }
    public TreeNode copy() {
        return new new_(lineNumber, copy_AbstractSymbol(type_name));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "new_\n");
        dump_AbstractSymbol(out, n+2, type_name);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_new");
	dump_AbstractSymbol(out, n + 2, type_name);
	dump_type(out, n);
    }

    public void semant(class_c clase){
      set_type(type_name);
    }

}


/** Defines AST constructor 'isvoid'.*/
class isvoid extends Expression {
    protected Expression e1;
    /** Creates "isvoid" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      */
    public isvoid(int lineNumber, Expression a1) {
        super(lineNumber);
        e1 = a1;
    }
    public TreeNode copy() {
        return new isvoid(lineNumber, (Expression)e1.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "isvoid\n");
        e1.dump(out, n+2);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_isvoid");
	e1.dump_with_types(out, n + 2);
	dump_type(out, n);
    }

    public void semant(class_c clase){
      e1.semant(clase);
      set_type(TreeConstants.Bool);
    }

}


/** Defines AST constructor 'no_expr'. */
class no_expr extends Expression {
    /** Creates "no_expr" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      */
    public no_expr(int lineNumber) {
        super(lineNumber);
    }
    public TreeNode copy() {
        return new no_expr(lineNumber);
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "no_expr\n");
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_no_expr");
	dump_type(out, n);
    }

    public void semant(class_c clase){
      set_type(TreeConstants.No_type);
    }

}


/** Defines AST constructor 'object'.*/
class object extends Expression {
    protected AbstractSymbol name;
    /** Creates "object" AST node.
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      */
    public object(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        name = a1;
    }
    public TreeNode copy() {
        return new object(lineNumber, copy_AbstractSymbol(name));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "object\n");
        dump_AbstractSymbol(out, n+2, name);
    }


    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_object");
	dump_AbstractSymbol(out, n + 2, name);
	dump_type(out, n);
    }

    public void semant(class_c clase){
      Object var = tbl.mapa.lookup(name);

      if(var == null)
        SemantErrors.undeclaredIdentifiers(name, tbl.cT.semantError(clase));
      else
        set_type((AbstractSymbol)tbl.mapa.lookup(name));

      if(name.toString().equals("self")){
        set_type(TreeConstants.SELF_TYPE);
      }
    }

}
