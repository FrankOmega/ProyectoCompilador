/*
Copyright (c) 2000 The Regents of the University of California.
All rights reserved.

Permission to use, copy, modify, and distribute this software for any
purpose, without fee, and without written agreement is hereby granted,
provided that the above copyright notice and the following two
paragraphs appear in all copies of this software.

IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR
DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE UNIVERSITY OF
CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
ON AN "AS IS" BASIS, AND THE UNIVERSITY OF CALIFORNIA HAS NO OBLIGATION TO
PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
*/

// This is a project skeleton file

import java.io.PrintStream;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

/** This class is used for representing the inheritance tree during code
    generation. You will need to fill in some of its methods and
    potentially extend it in other useful ways. */
class CgenClassTable extends SymbolTable {

    /** All classes in the program, represented as CgenNode */
    private Vector nds;

    /** This is the stream to which assembly instructions are output */
    private PrintStream str;

    private int stringclasstag;
    private int intclasstag;
    private int boolclasstag;


    // The following methods emit code for constants and global
    // declarations.

    /** Emits code to start the .data segment and to
     * declare the global names.
     * */
    private void codeGlobalData() {
	// The following global names must be defined first.

	str.print("\t.data\n" + CgenSupport.ALIGN);
	str.println(CgenSupport.GLOBAL + CgenSupport.CLASSNAMETAB);
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitProtObjRef(TreeConstants.Main, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitProtObjRef(TreeConstants.Int, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitProtObjRef(TreeConstants.Str, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	BoolConst.falsebool.codeRef(str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	BoolConst.truebool.codeRef(str);
	str.println("");
	str.println(CgenSupport.GLOBAL + CgenSupport.INTTAG);
	str.println(CgenSupport.GLOBAL + CgenSupport.BOOLTAG);
	str.println(CgenSupport.GLOBAL + CgenSupport.STRINGTAG);

	// We also need to know the tag of the Int, String, and Bool classes
	// during code generation.

	str.println(CgenSupport.INTTAG + CgenSupport.LABEL
		    + CgenSupport.WORD + intclasstag);
	str.println(CgenSupport.BOOLTAG + CgenSupport.LABEL
		    + CgenSupport.WORD + boolclasstag);
	str.println(CgenSupport.STRINGTAG + CgenSupport.LABEL
		    + CgenSupport.WORD + stringclasstag);

    }

    /** Emits code to start the .text segment and to
     * declare the global names.
     * */
    private void codeGlobalText() {
	str.println(CgenSupport.GLOBAL + CgenSupport.HEAP_START);
	str.print(CgenSupport.HEAP_START + CgenSupport.LABEL);
	str.println(CgenSupport.WORD + 0);
	str.println("\t.text");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitInitRef(TreeConstants.Main, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitInitRef(TreeConstants.Int, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitInitRef(TreeConstants.Str, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitInitRef(TreeConstants.Bool, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitMethodRef(TreeConstants.Main, TreeConstants.main_meth, str);
	str.println("");
    }

    /** Emits code definitions for boolean constants. */
    private void codeBools(int classtag) {
	BoolConst.falsebool.codeDef(classtag, str);
	BoolConst.truebool.codeDef(classtag, str);
    }

    /** Generates GC choice constants (pointers to GC functions) */
    private void codeSelectGc() {
	str.println(CgenSupport.GLOBAL + "_MemMgr_INITIALIZER");
	str.println("_MemMgr_INITIALIZER:");
	str.println(CgenSupport.WORD
		    + CgenSupport.gcInitNames[Flags.cgen_Memmgr]);

	str.println(CgenSupport.GLOBAL + "_MemMgr_COLLECTOR");
	str.println("_MemMgr_COLLECTOR:");
	str.println(CgenSupport.WORD
		    + CgenSupport.gcCollectNames[Flags.cgen_Memmgr]);

	str.println(CgenSupport.GLOBAL + "_MemMgr_TEST");
	str.println("_MemMgr_TEST:");
	str.println(CgenSupport.WORD
		    + ((Flags.cgen_Memmgr_Test == Flags.GC_TEST) ? "1" : "0"));
    }

    /** Emits code to reserve space for and initialize all of the
     * constants.  Class names should have been added to the string
     * table (in the supplied code, is is done during the construction
     * of the inheritance graph), and code for emitting string constants
     * as a side effect adds the string's length to the integer table.
     * The constants are emmitted by running through the stringtable and
     * inttable and producing code for each entry. */
    private void codeConstants() {
	// Add constants that are required by the code generator.
	AbstractTable.stringtable.addString("");
	AbstractTable.inttable.addString("0");

	AbstractTable.stringtable.codeStringTable(stringclasstag, str);
	AbstractTable.inttable.codeStringTable(intclasstag, str);
	codeBools(boolclasstag);

    }


    /** Creates data structures representing basic Cool classes (Object,
     * IO, Int, Bool, String).  Please note: as is this method does not
     * do anything useful; you will need to edit it to make if do what
     * you want.
     * */
    private void installBasicClasses() {
	AbstractSymbol filename
	    = AbstractTable.stringtable.addString("<basic class>");

	// A few special class names are installed in the lookup table
	// but not the class list.  Thus, these classes exist, but are
	// not part of the inheritance hierarchy.  No_class serves as
	// the parent of Object and the other special classes.
	// SELF_TYPE is the self class; it cannot be redefined or
	// inherited.  prim_slot is a class known to the code generator.

	addId(TreeConstants.No_class,
	      new CgenNode(new class_(0,
				      TreeConstants.No_class,
				      TreeConstants.No_class,
				      new Features(0),
				      filename),
			   CgenNode.Basic, this));

	addId(TreeConstants.SELF_TYPE,
	      new CgenNode(new class_(0,
				      TreeConstants.SELF_TYPE,
				      TreeConstants.No_class,
				      new Features(0),
				      filename),
			   CgenNode.Basic, this));

	addId(TreeConstants.prim_slot,
	      new CgenNode(new class_(0,
				      TreeConstants.prim_slot,
				      TreeConstants.No_class,
				      new Features(0),
				      filename),
			   CgenNode.Basic, this));

	// The Object class has no parent class. Its methods are
	//        cool_abort() : Object    aborts the program
	//        type_name() : Str        returns a string representation
	//                                 of class name
	//        copy() : SELF_TYPE       returns a copy of the object

	class_ Object_class =
	    new class_(0,
		       TreeConstants.Object_,
		       TreeConstants.No_class,
		       new Features(0)
			   .appendElement(new method(0,
					      TreeConstants.cool_abort,
					      new Formals(0),
					      TreeConstants.Object_,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.type_name,
					      new Formals(0),
					      TreeConstants.Str,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.copy,
					      new Formals(0),
					      TreeConstants.SELF_TYPE,
					      new no_expr(0))),
		       filename);

	installClass(new CgenNode(Object_class, CgenNode.Basic, this));

	// The IO class inherits from Object. Its methods are
	//        out_string(Str) : SELF_TYPE  writes a string to the output
	//        out_int(Int) : SELF_TYPE      "    an int    "  "     "
	//        in_string() : Str            reads a string from the input
	//        in_int() : Int                "   an int     "  "     "

	class_ IO_class =
	    new class_(0,
		       TreeConstants.IO,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new method(0,
					      TreeConstants.out_string,
					      new Formals(0)
						  .appendElement(new formal(0,
								     TreeConstants.arg,
								     TreeConstants.Str)),
					      TreeConstants.SELF_TYPE,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.out_int,
					      new Formals(0)
						  .appendElement(new formal(0,
								     TreeConstants.arg,
								     TreeConstants.Int)),
					      TreeConstants.SELF_TYPE,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.in_string,
					      new Formals(0),
					      TreeConstants.Str,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.in_int,
					      new Formals(0),
					      TreeConstants.Int,
					      new no_expr(0))),
		       filename);

	installClass(new CgenNode(IO_class, CgenNode.Basic, this));

	// The Int class has no methods and only a single attribute, the
	// "val" for the integer.

	class_ Int_class =
	    new class_(0,
		       TreeConstants.Int,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.prim_slot,
					    new no_expr(0))),
		       filename);

	installClass(new CgenNode(Int_class, CgenNode.Basic, this));

	// Bool also has only the "val" slot.
	class_ Bool_class =
	    new class_(0,
		       TreeConstants.Bool,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.prim_slot,
					    new no_expr(0))),
		       filename);

	installClass(new CgenNode(Bool_class, CgenNode.Basic, this));

	// The class Str has a number of slots and operations:
	//       val                              the length of the string
	//       str_field                        the string itself
	//       length() : Int                   returns length of the string
	//       concat(arg: Str) : Str           performs string concatenation
	//       substr(arg: Int, arg2: Int): Str substring selection

	class_ Str_class =
	    new class_(0,
		       TreeConstants.Str,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.Int,
					    new no_expr(0)))
			   .appendElement(new attr(0,
					    TreeConstants.str_field,
					    TreeConstants.prim_slot,
					    new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.length,
					      new Formals(0),
					      TreeConstants.Int,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.concat,
					      new Formals(0)
						  .appendElement(new formal(0,
								     TreeConstants.arg,
								     TreeConstants.Str)),
					      TreeConstants.Str,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.substr,
					      new Formals(0)
						  .appendElement(new formal(0,
								     TreeConstants.arg,
								     TreeConstants.Int))
						  .appendElement(new formal(0,
								     TreeConstants.arg2,
								     TreeConstants.Int)),
					      TreeConstants.Str,
					      new no_expr(0))),
		       filename);

	installClass(new CgenNode(Str_class, CgenNode.Basic, this));
    }

    // The following creates an inheritance graph from
    // a list of classes.  The graph is implemented as
    // a tree of `CgenNode', and class names are placed
    // in the base class symbol table.

    private void installClass(CgenNode nd) {
	AbstractSymbol name = nd.getName();
	if (probe(name) != null) return;
	nds.addElement(nd);
	addId(name, nd);
    }

    private void installClasses(Classes cs) {
        for (Enumeration e = cs.getElements(); e.hasMoreElements(); ) {
	    installClass(new CgenNode((Class_)e.nextElement(),
				       CgenNode.NotBasic, this));
        }
    }

    private void buildInheritanceTree() {
	for (Enumeration e = nds.elements(); e.hasMoreElements(); ) {
	    setRelations((CgenNode)e.nextElement());
	}
    }

    private void setRelations(CgenNode nd) {
	CgenNode parent = (CgenNode)probe(nd.getParent());
	nd.setParentNd(parent);
	parent.addChild(nd);
    }

    /** Constructs a new class table and invokes the code generator */
    public CgenClassTable(Classes cls, PrintStream str) {
	nds = new Vector();

	this.str = str;

	stringclasstag = 4 /* Change to your String class tag here */;
	intclasstag =    2 /* Change to your Int class tag here */;
	boolclasstag =   3 /* Change to your Bool class tag here */;

	enterScope();
	if (Flags.cgen_debug) System.out.println("Building CgenClassTable");

	installBasicClasses();
	installClasses(cls);
	buildInheritanceTree();

	code();

	exitScope();
    }

    /** This method is the meat of the code generator.  It is to be
        filled in programming assignment 5 */
    public void code() {
	if (Flags.cgen_debug) System.out.println("coding global data");
	codeGlobalData();

	if (Flags.cgen_debug) System.out.println("choosing gc");
	codeSelectGc();

	if (Flags.cgen_debug) System.out.println("coding constants");
	codeConstants();

  //class_nameTab
  str.print(CgenSupport.CLASSNAMETAB + CgenSupport.LABEL);
  for(int i = 0; i < nds.size(); i++){
    class_ clase = (class_)nds.get(i);
    StringSymbol coso = (StringSymbol)AbstractTable.stringtable.lookup(clase.name.toString());
    str.print(CgenSupport.WORD); coso.codeRef(str); str.println();
  }

  //class_objTab
  str.print(CgenSupport.CLASSOBJTAB + CgenSupport.LABEL);
  for(int i = 0; i < nds.size(); i++){
    class_ clase = (class_)nds.get(i);
    str.println(CgenSupport.WORD + clase.name + CgenSupport.PROTOBJ_SUFFIX);
    str.println(CgenSupport.WORD + clase.name + CgenSupport.CLASSINIT_SUFFIX);
  }

  //-------------------DATOS IMPORTANTES-----------------------
  //<clase,padre>
  Hashtable<AbstractSymbol,AbstractSymbol> inh;
  inh = new Hashtable<AbstractSymbol,AbstractSymbol>();

  //<clase,<nombre metodos actuales>>
  Hashtable<AbstractSymbol,Vector<AbstractSymbol>> hs;
  hs = new Hashtable<AbstractSymbol,Vector<AbstractSymbol>>();

  //<clase,<padres>>
  Hashtable<AbstractSymbol,Vector<AbstractSymbol>> inhtotal;
  inhtotal = new Hashtable<AbstractSymbol,Vector<AbstractSymbol>>();

  //<clase,<nombre todos metodos>>
  Hashtable<AbstractSymbol,Vector<AbstractSymbol>> hstotal;
  hstotal = new Hashtable<AbstractSymbol,Vector<AbstractSymbol>>();

  //<clase,<atributos>>
	Hashtable<AbstractSymbol,Vector<attr>> attrhs;
	attrhs = new Hashtable<AbstractSymbol,Vector<attr>>();

	Hashtable<AbstractSymbol,Vector<AbstractSymbol>> allattr;
	allattr = new Hashtable<AbstractSymbol,Vector<AbstractSymbol>>();

  //<clase, numero de atributos>
  Hashtable<AbstractSymbol,Integer> cantattr;
  cantattr = new Hashtable<AbstractSymbol,Integer>();


  Vector<AbstractSymbol> metodos;
  //Llenar hs de herencia
  for(int i = 0; i < nds.size(); i++){
    class_ clase = (class_)nds.get(i);
    inh.put(clase.name, clase.parent);
  }

  //Llenar padres de herencia
  AbstractSymbol asAux;
  for(int i = 0; i < nds.size(); i++){
    Vector<AbstractSymbol> padres = new Vector<AbstractSymbol>();
    class_ clase = (class_)nds.get(i);
    asAux = clase.name;
    while(!asAux.equals(TreeConstants.Object_)){
      AbstractSymbol padre = inh.get(asAux);
      padres.add(padre);
      asAux = padre;
    }
    inhtotal.put(clase.name, padres);
  }


  //Llenar hs de metodo actuales
  for(int i = 0; i < nds.size(); i++){
    class_ clase = (class_)nds.get(i);
    metodos = new Vector<AbstractSymbol>();
		Vector atributos = new Vector<attr>();
    for(int j = 0; j < clase.features.getLength(); j++){
      Feature f = (Feature)clase.features.getNth(j);
      if(f instanceof method){
        method mth = (method) f;
        metodos.add(mth.name);
        //System.out.println(mth.expr.contador(0) + " " + mth.name + " in " + clase.name);
      }
			else{
				attr attradd = (attr) f;
				atributos.add(attradd);
			}
    }
    hs.put(clase.name, metodos);
		attrhs.put(clase.name, atributos);
  }

  Vector<AbstractSymbol> hstv;
  //Llenar hstv de metodos heredados
  for(int i = 0; i < nds.size(); i++){
    class_ clase = (class_)nds.get(i);
    hstv = (Vector<AbstractSymbol>)hs.get(clase.name).clone();
    Vector<AbstractSymbol> hstv1 = new Vector<AbstractSymbol>();
    for(int j = hstv.size() - 1; j >= 0; j--){
      hstv1.add(hstv.get(j));
    }
    if(!clase.name.equals(TreeConstants.Object_)){
      Vector<AbstractSymbol> cosos1 = inhtotal.get(clase.name);
      for(int j = 0; j < cosos1.size(); j++){
        Vector<AbstractSymbol> cosos2 = hs.get(cosos1.get(j));
        for(int k = cosos2.size() - 1 ; k >= 0; k--){
          AbstractSymbol mn = cosos2.get(k);
          if(!hstv1.contains(mn))
            hstv1.add(cosos2.get(k));
          else{
            hstv1.remove(mn);
            hstv1.add(mn);
          }
        }
      }
    }
    hstotal.put(clase.name,hstv1);
  }
  Aux.hs_metodos = hstotal;

  //Llenar cuantos atributos tiene una clase(incluyendo a sus padres)
  for(int i = 0; i < nds.size(); i++){
    class_ clase = (class_)nds.get(i);
    int ncantattr = 0;
    asAux = clase.name;
		Vector<AbstractSymbol> attraux = new Vector<AbstractSymbol>();
    while(!asAux.equals(TreeConstants.Object_)){
      AbstractSymbol padre = inh.get(asAux);
      Vector atributos = attrhs.get(asAux);
      ncantattr = ncantattr + atributos.size();
			for(int j = atributos.size() - 1; j >= 0; j--){
				attr a  = (attr) atributos.get(j);
				attraux.add(a.name);
			}
      asAux = padre;
    }
    cantattr.put(clase.name, ncantattr);
		allattr.put(clase.name, attraux);
  }
	Aux.cant_attr = cantattr;
	Aux.all_attr = allattr;


  //-------------------------------------------------------------------

  Boolean b = false;
  //Imprimir los dipTab
  for(int i = 0; i < nds.size(); i++){
    class_ clase = (class_)nds.get(i);
    str.print(clase.name + CgenSupport.DISPTAB_SUFFIX + CgenSupport.LABEL);
    Vector<AbstractSymbol> vm = hstotal.get(clase.name);
    Vector<AbstractSymbol> vc = inhtotal.get(clase.name);
    for(int j = vm.size() - 1; j >= 0; j--){
      AbstractSymbol m = vm.get(j);
      for(int k = -1; !b && k < vc.size(); k++){
        AbstractSymbol c;
        if(k == -1)
          c = clase.name;
        else
          c = vc.get(k);
        Vector<AbstractSymbol> actuales = hs.get(c);
        if(actuales.contains(m)){
          b = true;
          str.println(CgenSupport.WORD + c + "." + m);
        }
      }
      b = false;
    }
  }

	
  //Prototypes
  for(int i = 0; i < nds.size(); i++){

    class_ clase = (class_)nds.get(i);
    str.println(CgenSupport.WORD + "-1");
    str.print(clase.name + CgenSupport.PROTOBJ_SUFFIX + CgenSupport.LABEL);
    int ncantattr = cantattr.get(clase.name);
		str.print(CgenSupport.WORD + i + "\n");
		str.print(CgenSupport.WORD + (CgenSupport.DEFAULT_OBJFIELDS + ncantattr) + "\n");
		str.print(CgenSupport.WORD + clase.name + CgenSupport.DISPTAB_SUFFIX + "\n");

    Vector<AbstractSymbol> clases;
    clases = (Vector<AbstractSymbol>)inhtotal.get(clase.name).clone();
    clases.add(0,clase.name);
		for(int j = clases.size() - 1; j >= 0; j--){
      Vector<attr> atributos = attrhs.get(clases.get(j));

      for(int k = 0; k < atributos.size(); k++){
        AbstractSymbol tipo = atributos.get(k).type_decl;

        if(tipo.equals(TreeConstants.Int)){
  				IntSymbol numero;
  				numero = (IntSymbol)AbstractTable.inttable.lookup("0");
  				str.print(CgenSupport.WORD); numero.codeRef(str);	str.println();
  			}

        else if(tipo.equals(TreeConstants.Str)){
  				StringSymbol palabra;
  				palabra = (StringSymbol)AbstractTable.stringtable.lookup("");
  				str.print(CgenSupport.WORD); palabra.codeRef(str); str.println();
  			}

        else if(tipo.equals(TreeConstants.Bool))
          str.println(CgenSupport.WORD + CgenSupport.BOOLCONST_PREFIX + "0");

        else
  				str.println(CgenSupport.WORD + "0");
      }
		}

  }

	if (Flags.cgen_debug) System.out.println("coding global text");
	codeGlobalText();

  //Objects inits
  for(int i = 0; i < nds.size(); i++){

    class_ clase = (class_)nds.get(i);
    str.print(clase.name + CgenSupport.CLASSINIT_SUFFIX + CgenSupport.LABEL);
    CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -12, str);
    CgenSupport.emitStore(CgenSupport.FP, 3, CgenSupport.SP, str);
    CgenSupport.emitStore(CgenSupport.SELF, 2, CgenSupport.SP, str);
    CgenSupport.emitStore(CgenSupport.RA, 1, CgenSupport.SP, str);
    CgenSupport.emitAddiu(CgenSupport.FP, CgenSupport.SP, 4, str);
    CgenSupport.emitMove(CgenSupport.SELF, CgenSupport.ACC, str);

    if(!clase.name.equals(TreeConstants.Object_))
      CgenSupport.emitJal(inh.get(clase.name) + CgenSupport.CLASSINIT_SUFFIX, str);

		//Lo que cambia XD -----------------------------------
		Vector<attr> atributos = attrhs.get(clase.name);
		for(int j = 0; j < atributos.size(); j++){
			attr atributo = (attr)atributos.get(j);
			AbstractSymbol tipo = atributo.init.get_type();
			if(tipo != null){
        Expression e = (Expression) atributo.init;
        e.code(str,clase);

        int ncantattr = cantattr.get(clase.name);
				int n = ncantattr + CgenSupport.DEFAULT_OBJFIELDS - atributos.size() + j;
        CgenSupport.emitStore(CgenSupport.ACC,n,CgenSupport.SELF,str);
			}
		}
		//----------------------------------------------------

    CgenSupport.emitMove(CgenSupport.ACC, CgenSupport.SELF, str);
    CgenSupport.emitLoad(CgenSupport.FP, 3, CgenSupport.SP, str);
    CgenSupport.emitLoad(CgenSupport.SELF, 2, CgenSupport.SP, str);
    CgenSupport.emitLoad(CgenSupport.RA, 1, CgenSupport.SP, str);
    CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, 12, str);
    str.println(CgenSupport.RET);
  }

  Vector<class_> c_ordenadas = new Vector<class_>();
  Aux.ids_let = new Vector<AbstractSymbol>();
  for(int i= 5; i < nds.size(); i++){
    class_ clase = (class_) nds.get(i);
    for(int j = 0; j < clase.features.getLength(); j++){
      Feature f = (Feature)clase.features.getNth(j);
      if(f instanceof method){
        method m = (method) f;
        m.expr.contador(0);
        int cant_frms = m.formals.getLength();
        Vector<AbstractSymbol> frms = new Vector<AbstractSymbol>();
        for(int k = cant_frms - 1; k >= 0; k--){
          formal frm = (formal) m.formals.getNth(k);
          frms.add(frm.name);
        }
        Aux.firmas = frms;

        str.print(clase.name + "." + m.name + CgenSupport.LABEL);
        CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -(12+(Aux.MAX * 4)), str);
        CgenSupport.emitStore(CgenSupport.FP, 3 + Aux.MAX, CgenSupport.SP, str);
        CgenSupport.emitStore(CgenSupport.SELF, 2 + Aux.MAX, CgenSupport.SP, str);
        CgenSupport.emitStore(CgenSupport.RA, 1 + Aux.MAX, CgenSupport.SP, str);
        CgenSupport.emitAddiu(CgenSupport.FP, CgenSupport.SP, 4, str);
        CgenSupport.emitMove(CgenSupport.SELF, CgenSupport.ACC, str);
        m.expr.code(str, clase);

        CgenSupport.emitLoad(CgenSupport.FP, 3 + Aux.MAX, CgenSupport.SP, str);
        CgenSupport.emitLoad(CgenSupport.SELF, 2 + Aux.MAX, CgenSupport.SP, str);
        CgenSupport.emitLoad(CgenSupport.RA, 1 + Aux.MAX, CgenSupport.SP, str);

        int coso = 12+(Aux.MAX * 4) + (cant_frms*4);
        CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, coso, str);
        str.println(CgenSupport.RET);
        Aux.firmas = new Vector<AbstractSymbol>();
        Aux.MAX = 0;
      }
    }
  }
  //Ordenar clases


	//                 Add your code to emit
	//                   - the class methods
	//                   - etc...
    }


    /** Gets the root of the inheritance tree */
    public CgenNode root() {
	return (CgenNode)probe(TreeConstants.Object_);
    }

}
