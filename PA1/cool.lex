/*
 *  The scanner definition for COOL.
 */

import java_cup.runtime.Symbol;

%%

%{

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */


		int count_int = 1;
		int count_str = 1;
		int count_id = 1;
		int count_class = 1;

		int count_comm = 0;
		int strlong = 0;
		int instr = 0;

		String auxstr = "";

    // Max size of string constants
    static int MAX_STR_CONST = 1025;

    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();

		String arreglar_string(String arreglado){
				String cosa = arreglado.substring(1,arreglado.length()-1);
				cosa.concat("\"");
				return cosa;
		}

		int cant_newlines(String comentario){
			int lines = 0;
    	int pos = 0;
    	while ((pos = comentario.indexOf("\n", pos) + 1) != 0) {
        lines++;
    	}
    	return lines;
		}

    private int curr_lineno = 1;
    int get_curr_lineno() {
	return curr_lineno;
    }

    private AbstractSymbol filename;

    void set_filename(String fname) {
	filename = AbstractTable.stringtable.addString(fname);
    }

    AbstractSymbol curr_filename() {
	return filename;
    }
%}

%init{

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */

    // empty for now
%init}

%eofval{

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */

    switch(yy_lexical_state) {
    case YYINITIAL:
	/* nothing special to do in the initial state */
	break;

		case STR_LEX1:
		yybegin(YYINITIAL);
		return new Symbol(TokenConstants.ERROR, "EOF in string constant");


		case COMMENTS:
		yybegin(YYINITIAL);
		return new Symbol(TokenConstants.ERROR, "EOF in comment");

	/* If necessary, add code for other states here, e.g:
	   case COMMENT:
	   ...
	   break;
	*/
    }
    return new Symbol(TokenConstants.EOF);
%eofval}

%class CoolLexer
CLASS_LEX = [c|C][l|L][a|A][s|S][s|S]
ELSE_LEX = [e|E][l|L][s|S][e|E]
FALSE_LEX = f[a|A][l|L][s|S][e|E]
FI_LEX = [f|F][i|I]
IF_LEX = [i|I][f|F]
IN_LEX = [i|I][n|N]

INH_LEX = [i|I][n|N][h|H][e|E][r|R][i|I][t|T][s|S]
ISVD_LEX = [i|I][s|S][v|V][o|O][i|I][d|D]
LET_LEX = [l|L][e|E][t|T]
LOOP_LEX = [l|L][o|O][o|O][p|P]
POOL_LEX = [p|P][o|O][o|O][l|L]
THEN_LEX = [t|T][h|H][e|E][n|N]
WHILE_LEX = [w|W][h|H][i|I][l|L][e|E]

CASE_LEX = [c|C][a|A][s|S][e|E]
ESAC_LEX = [e|E][s|S][a|A][c|C]
NEW_LEX = [n|N][e|E][w|W]
OF_LEX = [o|O][f|F]
NOT_LEX = [n|N][o|O][t|T]
TRUE_LEX = t[r|R][u|U][e|E]

INT_LEX = [0-9]+
TYPE_LEX = [A-Z]([A-Z]|[a-z]|[0-9]|_)*
OBJECT_LEX = [a-z]([A-Z]|[a-z]|[0-9]|_)*
NOTHING = " "|\t|\f|\x0D|\x0B
COMMENT2 = \-\-[^(\n)]*\n
SLASH = \\.
NULL_LEX = (\x00|(\\\x00))
RAROS1 = ((\x0B)|(\x0C)|(\x0E)|(\x0F)|(\x10)|(\x11)|(\x12)|(\x13)|(\x14)|(\x0D))
RAROS2 = ((\x15)|(\x16)|(\x27)|(\x18)|(\x19)|(\x1A)|(\x1B)|(\x1C)|(\x1E))

%state STR_LEX1
%state STR_NULL
%state COMMENTS
%state TERMINAR
%state COMMENTS2
%cup

%%

<TERMINAR>.|\n 					{ }

<YYINITIAL>"*)"						{ return new Symbol(TokenConstants.ERROR, "Unmatched *)");}

<YYINITIAL>"\""						{	string_buf.append(yytext());
														yybegin(STR_LEX1); }


<STR_LEX1>{NULL_LEX}			{	yybegin(STR_NULL);
														return new Symbol(TokenConstants.ERROR, "String contains null character."); }

<STR_LEX1>\\"\""					{	string_buf.append("\""); }

<STR_LEX1>"\""						{ string_buf.append(yytext());
														yybegin(YYINITIAL);
														auxstr = arreglar_string(string_buf.toString());
														string_buf.setLength(0);
														strlong = auxstr.replace("\\", "").length();
														if(auxstr.length() <= 1024){
															AbstractSymbol strfin = AbstractTable.stringtable.addString(auxstr);
	    												return new Symbol(TokenConstants.STR_CONST, strfin);
														}else{
															return new Symbol(TokenConstants.ERROR, "String constant too long");
														}
													}

<STR_LEX1>\x0D						{ string_buf.append("\015"); }
<STR_LEX1>\x1B						{ string_buf.append("\033"); }
<STR_LEX1>(\\t)						{	string_buf.append("\t"); }
<STR_LEX1>(\\n)						{	string_buf.append("\n"); }
<STR_LEX1>(\\f)						{	string_buf.append("\f"); }
<STR_LEX1>(\\b)						{	string_buf.append("\b"); }
<STR_LEX1>(\\)(\\)				{	string_buf.append("\\"); }
<STR_LEX1>(\\)\n					{ string_buf.append("\n");
													curr_lineno++; }

<STR_LEX1> {SLASH}				{	string_buf.append(yytext().charAt(1)); }

<STR_LEX1>\n							{	yybegin(YYINITIAL);
														curr_lineno++;
														string_buf.setLength(0);
														return new Symbol(TokenConstants.ERROR, "Unterminated string constant");}

<STR_LEX1>.|\n						{ string_buf.append(yytext()); }


<STR_NULL>"\""						{ yybegin(YYINITIAL); }
<STR_NULL>(\\)\n					{	curr_lineno++; }
<STR_NULL>\n							{ yybegin(YYINITIAL);
														curr_lineno++; }
<STR_NULL>.|\n						{ }


<YYINITIAL>"(*"						{	yybegin(COMMENTS); }
<COMMENTS>"(*"						{ count_comm++; }

<COMMENTS>{RAROS1}				{ }
<COMMENTS>{RAROS2}				{ }
<COMMENTS>\n							{ curr_lineno++;}
<COMMENTS>.|\n						{	 }
<COMMENTS>"*)"						{ if (count_comm == 0)	yybegin(YYINITIAL);
												 		else count_comm--;	}

<YYINITIAL>"--"						{ yybegin(COMMENTS2); }
<COMMENTS2>.							{ }
<COMMENTS2>\n							{	curr_lineno++;
														yybegin(YYINITIAL); }

<YYINITIAL>"*"						{ return new Symbol(TokenConstants.MULT); }
<YYINITIAL> {INH_LEX}			{	return new Symbol(TokenConstants.INHERITS); }
<YYINITIAL> {POOL_LEX}		{ return new Symbol(TokenConstants.POOL); }
<YYINITIAL> {CASE_LEX}		{ return new Symbol(TokenConstants.CASE); }
<YYINITIAL>"("						{ return new Symbol(TokenConstants.LPAREN); }
<YYINITIAL>";"						{ return new Symbol(TokenConstants.SEMI); }
<YYINITIAL>"-"						{ return new Symbol(TokenConstants.MINUS); }

<YYINITIAL>")"						{ return new Symbol(TokenConstants.RPAREN); }
<YYINITIAL> {NOT_LEX}			{ return new Symbol(TokenConstants.NOT); }
<YYINITIAL>"<"						{ return new Symbol(TokenConstants.LT); }
<YYINITIAL> {IN_LEX}			{ return new Symbol(TokenConstants.IN); }
<YYINITIAL>","						{ return new Symbol(TokenConstants.COMMA); }
<YYINITIAL> {CLASS_LEX}		{ return new Symbol(TokenConstants.CLASS); }
<YYINITIAL> {FI_LEX}			{ return new Symbol(TokenConstants.FI); }
<YYINITIAL>"/"						{ return new Symbol(TokenConstants.DIV); }
<YYINITIAL> {LOOP_LEX}		{ return new Symbol(TokenConstants.LOOP); }

<YYINITIAL>"+"						{ return new Symbol(TokenConstants.PLUS); }
<YYINITIAL>"*"						{ return new Symbol(TokenConstants.MULT); }
<YYINITIAL>"<-"						{ return new Symbol(TokenConstants.ASSIGN); }
<YYINITIAL> {IF_LEX}			{ return new Symbol(TokenConstants.IF); }
<YYINITIAL>"."						{ return new Symbol(TokenConstants.DOT); }

<YYINITIAL>"<="						{ return new Symbol(TokenConstants.LE); }
<YYINITIAL> {OF_LEX}			{ return new Symbol(TokenConstants.OF); }
<YYINITIAL>\x00						{ return new Symbol(TokenConstants.ERROR, "\\000"); }
<YYINITIAL> {INT_LEX}			{ return new Symbol(TokenConstants.INT_CONST,
									new IntSymbol(yytext(), yytext().length(), count_int++)); }

<YYINITIAL> {NEW_LEX}			{ return new Symbol(TokenConstants.NEW); }
<YYINITIAL> {ISVD_LEX}		{ return new Symbol(TokenConstants.ISVOID); }
<YYINITIAL>"="						{ return new Symbol(TokenConstants.EQ); }
<YYINITIAL>":"						{ return new Symbol(TokenConstants.COLON); }

<YYINITIAL>"~"						{ return new Symbol(TokenConstants.NEG); }
<YYINITIAL>"{"						{ return new Symbol(TokenConstants.LBRACE); }
<YYINITIAL> {ELSE_LEX}		{ return new Symbol(TokenConstants.ELSE); }
<YYINITIAL>"=>"						{ return new Symbol(TokenConstants.DARROW); }
<YYINITIAL> {WHILE_LEX}		{ return new Symbol(TokenConstants.WHILE); }

<YYINITIAL> {ESAC_LEX}		{ return new Symbol(TokenConstants.ESAC); }
<YYINITIAL> {LET_LEX}			{ return new Symbol(TokenConstants.LET); }
<YYINITIAL>"}"						{ return new Symbol(TokenConstants.RBRACE); }
<YYINITIAL> {THEN_LEX}		{ return new Symbol(TokenConstants.THEN); }

<YYINITIAL> {TRUE_LEX}		{	return new Symbol(TokenConstants.BOOL_CONST, true); }
<YYINITIAL> {FALSE_LEX}		{	return new Symbol(TokenConstants.BOOL_CONST, false); }
<YYINITIAL> {OBJECT_LEX}	{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
<YYINITIAL> {TYPE_LEX}		{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }

<YYINITIAL>"@"						{ return new Symbol(TokenConstants.AT); }
<YYINITIAL>\n							{ curr_lineno++; }
<YYINITIAL> {NOTHING}			{ }
.                					{ return new Symbol(TokenConstants.ERROR, yytext()); }
