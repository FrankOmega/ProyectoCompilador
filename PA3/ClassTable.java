import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Vector;

/** This class may be used to contain the semantic information such as
 * the inheritance graph.  You may use it or not as you like: it is only
 * here to provide a container for the supplied methods.  */
class ClassTable {
    private int semantErrors;
    private PrintStream errorStream;

    /** Creates data structures representing basic Cool classes (Object,
     * IO, Int, Bool, String).  Please note: as is this method does not
     * do anything useful; you will need to edit it to make if do what
     * you want.
     * */
    //private void installBasicClasses() {
	AbstractSymbol filename
	    = AbstractTable.stringtable.addString("<basic class>");

	// The following demonstrates how to create dummy parse trees to
	// refer to basic Cool classes.  There's no need for method
	// bodies -- these are already built into the runtime system.

	// IMPORTANT: The results of the following expressions are
	// stored in local variables.  You will want to do something
	// with those variables at the end of this method to make this
	// code meaningful.

	// The Object class has no parent class. Its methods are
	//        cool_abort() : Object    aborts the program
	//        type_name() : Str        returns a string representation
	//                                 of class name
	//        copy() : SELF_TYPE       returns a copy of the object

	class_c Object_class =
	    new class_c(0,
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

	// The IO class inherits from Object. Its methods are
	//        out_string(Str) : SELF_TYPE  writes a string to the output
	//        out_int(Int) : SELF_TYPE      "    an int    "  "     "
	//        in_string() : Str            reads a string from the input
	//        in_int() : Int                "   an int     "  "     "

	class_c IO_class =
	    new class_c(0,
		       TreeConstants.IO,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new method(0,
					      TreeConstants.out_string,
					      new Formals(0)
						  .appendElement(new formalc(0,
								     TreeConstants.arg,
								     TreeConstants.Str)),
					      TreeConstants.SELF_TYPE,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.out_int,
					      new Formals(0)
						  .appendElement(new formalc(0,
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

	// The Int class has no methods and only a single attribute, the
	// "val" for the integer.

	class_c Int_class =
	    new class_c(0,
		       TreeConstants.Int,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.prim_slot,
					    new no_expr(0))),
		       filename);

	// Bool also has only the "val" slot.
	class_c Bool_class =
	    new class_c(0,
		       TreeConstants.Bool,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.prim_slot,
					    new no_expr(0))),
		       filename);

	// The class Str has a number of slots and operations:
	//       val                              the length of the string
	//       str_field                        the string itself
	//       length() : Int                   returns length of the string
	//       concat(arg: Str) : Str           performs string concatenation
	//       substr(arg: Int, arg2: Int): Str substring selection

	class_c Str_class =
	    new class_c(0,
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
						  .appendElement(new formalc(0,
								     TreeConstants.arg,
								     TreeConstants.Str)),
					      TreeConstants.Str,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.substr,
					      new Formals(0)
						  .appendElement(new formalc(0,
								     TreeConstants.arg,
								     TreeConstants.Int))
						  .appendElement(new formalc(0,
								     TreeConstants.arg2,
								     TreeConstants.Int)),
					      TreeConstants.Str,
					      new no_expr(0))),
		       filename);

	/* Do somethind with Object_class, IO_class, Int_class,
           Bool_class, and Str_class here */

    //}

    Vector<class_c> bases = new Vector<class_c>();
    Vector<String> v = new Vector<String>();
    Vector<AbstractSymbol> vAS = new Vector<AbstractSymbol>();
    public ClassTable(Classes cls) {
	    semantErrors = 0;
	    errorStream = System.err;
      bases.add(Object_class);
      bases.add(IO_class);
      bases.add(Int_class);
      bases.add(Str_class);
      bases.add(Bool_class);
	/* fill this in */
    }

    String papa;
    AbstractSymbol padre;
    public boolean searchCycleClasses(AbstractSymbol name,
                                Hashtable<AbstractSymbol,AbstractSymbol> hsh)
      {
      padre = hsh.get(name);
      vAS.add(name);

      if(name.equals(padre))
        return true;
      else if(TreeConstants.Object_.equals(padre))
        return false;
      else{
        if(v.contains(padre))
          return true;
        else
          return searchCycleClasses(padre, hsh);
      }
    }

    /** Prints line number and file name of the given class.
     *
     * Also increments semantic error count.
     *
     * @param c the class
     * @return a print stream to which the rest of the error message is
     * to be printed.
     *
     * */

     public boolean pertenece(AbstractSymbol var_type, AbstractSymbol type,
                            Hashtable<AbstractSymbol,AbstractSymbol> hsh){
     	//System.out.println(hsh + " " + var_type + " " + type);
        if(var_type.equals(type))
          return true;
        else if ( type.equals(TreeConstants.No_type))
          return false;
        else
          return pertenece(var_type, hsh.get(type), hsh);
     }

    public AbstractSymbol union(AbstractSymbol a1, AbstractSymbol a2,
        AbstractSymbol c, Hashtable<AbstractSymbol,AbstractSymbol>hsh)
    {
      /*Vector<AbstractSymbol> v = new Vector<AbstractSymbol>();
      v = vectorizarHerencia(a1, v, hsh);
      Vector<AbstractSymbol> v2 = new Vector<AbstractSymbol>();
      v2 = vectorizarHerencia(a2, v2, hsh);
      //System.out.println(v + "\n2" + v2);
      for(int i = 0; i < v.size(); i++){
        for(int j = 0; j < v2.size(); j++){
          if(v2.get(j).equals(v.get(i))){
            return v2.get(j);
          }
        }
      }*/
      if(TreeConstants.No_type.equals(a1) || TreeConstants.Object_.equals(a1)){
        return TreeConstants.Object_;
      }
      else if(pertenece(a1,a2,hsh))
        return a1;
      else{
        if(TreeConstants.Object_.equals(a2)){
          return union(hsh.get(a1),c,c,hsh);
        }
        else
          return union(a1,hsh.get(a2),c,hsh);
      }
    }


    Vector<AbstractSymbol> caset = new Vector<AbstractSymbol>();
    public AbstractSymbol caseUnion(Vector<AbstractSymbol> vt,
                      Hashtable<AbstractSymbol,AbstractSymbol> hsh)
      {
      if(vt.size() == 1)
        return vt.get(0);
      else if(vt.size() == 2)
        return union(vt.get(0),vt.get(1),vt.get(1),hsh);
      else{
        AbstractSymbol s1 = vt.get(0);
        AbstractSymbol s2 = vt.get(1);
        vt.remove(0);
        vt.remove(0);
        return union(union(s1,s2,s2,hsh), caseUnion(vt,hsh),caseUnion(vt,hsh), hsh);
      }
    }

     public boolean attr_tieneherncia(AbstractSymbol clase, Hashtable<AbstractSymbol,AbstractSymbol> hsh){
      if(hsh.get(clase).equals(TreeConstants.Object_) |
                hsh.get(clase).equals(TreeConstants.IO))
        return false;
      else
        return true;
     }

    public void ordenarClases(AbstractSymbol name, Hashtable<AbstractSymbol,AbstractSymbol> hsh){
      padre = hsh.get(name);

      if(!TreeConstants.Object_.equals(padre))
        ordenarClases(padre,hsh);
      if(!vAS.contains(name))
        if(!TreeConstants.IO.equals(name))
          vAS.add(name);
    }

    /*public AbstractSymbol union(){

    }

    public boolean newLessOrEqual(){

    }*/

    public PrintStream semantError(class_c c) {
	return semantError(c.getFilename(), c);
    }

    /** Prints the file name and the line number of the given tree node.
     *
     * Also increments semantic error count.
     *
     * @param filename the file name
     * @param t the tree node
     * @return a print stream to which the rest of the error message is
     * to be printed.
     *
     * */
    public PrintStream semantError(AbstractSymbol filename, TreeNode t) {
	errorStream.print(filename + ":" + t.getLineNumber() + ": ");
	return semantError();
    }

    /** Increments semantic error count and returns the print stream for
     * error messages.
     *
     * @return a print stream to which the error message is
     * to be printed.
     *
     * */

    public PrintStream semantError() {
	semantErrors++;
	return errorStream;
    }

    /** Returns true if there are any static semantic errors. */
    public boolean errors() {
	return semantErrors != 0;
    }
}
