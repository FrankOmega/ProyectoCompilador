/*
 *  The scanner definition for COOL.
 */
import java_cup.runtime.Symbol;


class CoolLexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

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
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private boolean yy_at_bol;
	private int yy_lexical_state;

	CoolLexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	CoolLexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private CoolLexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */
    // empty for now
	}

	private boolean yy_eof_done = false;
	private final int COMMENTS2 = 4;
	private final int YYINITIAL = 0;
	private final int COMMENTS = 3;
	private final int STR_NULL = 2;
	private final int STR_LEX1 = 1;
	private final int yy_state_dtrans[] = {
		0,
		236,
		237,
		238,
		239
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NOT_ACCEPT,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NOT_ACCEPT,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NOT_ACCEPT,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR,
		/* 133 */ YY_NO_ANCHOR,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NO_ANCHOR,
		/* 136 */ YY_NO_ANCHOR,
		/* 137 */ YY_NO_ANCHOR,
		/* 138 */ YY_NOT_ACCEPT,
		/* 139 */ YY_NO_ANCHOR,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NO_ANCHOR,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NO_ANCHOR,
		/* 144 */ YY_NO_ANCHOR,
		/* 145 */ YY_NO_ANCHOR,
		/* 146 */ YY_NOT_ACCEPT,
		/* 147 */ YY_NO_ANCHOR,
		/* 148 */ YY_NO_ANCHOR,
		/* 149 */ YY_NOT_ACCEPT,
		/* 150 */ YY_NO_ANCHOR,
		/* 151 */ YY_NO_ANCHOR,
		/* 152 */ YY_NOT_ACCEPT,
		/* 153 */ YY_NO_ANCHOR,
		/* 154 */ YY_NO_ANCHOR,
		/* 155 */ YY_NOT_ACCEPT,
		/* 156 */ YY_NO_ANCHOR,
		/* 157 */ YY_NO_ANCHOR,
		/* 158 */ YY_NOT_ACCEPT,
		/* 159 */ YY_NO_ANCHOR,
		/* 160 */ YY_NO_ANCHOR,
		/* 161 */ YY_NOT_ACCEPT,
		/* 162 */ YY_NO_ANCHOR,
		/* 163 */ YY_NO_ANCHOR,
		/* 164 */ YY_NOT_ACCEPT,
		/* 165 */ YY_NO_ANCHOR,
		/* 166 */ YY_NO_ANCHOR,
		/* 167 */ YY_NOT_ACCEPT,
		/* 168 */ YY_NO_ANCHOR,
		/* 169 */ YY_NO_ANCHOR,
		/* 170 */ YY_NOT_ACCEPT,
		/* 171 */ YY_NO_ANCHOR,
		/* 172 */ YY_NO_ANCHOR,
		/* 173 */ YY_NOT_ACCEPT,
		/* 174 */ YY_NO_ANCHOR,
		/* 175 */ YY_NO_ANCHOR,
		/* 176 */ YY_NOT_ACCEPT,
		/* 177 */ YY_NO_ANCHOR,
		/* 178 */ YY_NO_ANCHOR,
		/* 179 */ YY_NOT_ACCEPT,
		/* 180 */ YY_NO_ANCHOR,
		/* 181 */ YY_NO_ANCHOR,
		/* 182 */ YY_NOT_ACCEPT,
		/* 183 */ YY_NO_ANCHOR,
		/* 184 */ YY_NO_ANCHOR,
		/* 185 */ YY_NOT_ACCEPT,
		/* 186 */ YY_NO_ANCHOR,
		/* 187 */ YY_NO_ANCHOR,
		/* 188 */ YY_NOT_ACCEPT,
		/* 189 */ YY_NO_ANCHOR,
		/* 190 */ YY_NO_ANCHOR,
		/* 191 */ YY_NOT_ACCEPT,
		/* 192 */ YY_NO_ANCHOR,
		/* 193 */ YY_NO_ANCHOR,
		/* 194 */ YY_NOT_ACCEPT,
		/* 195 */ YY_NO_ANCHOR,
		/* 196 */ YY_NO_ANCHOR,
		/* 197 */ YY_NOT_ACCEPT,
		/* 198 */ YY_NO_ANCHOR,
		/* 199 */ YY_NO_ANCHOR,
		/* 200 */ YY_NOT_ACCEPT,
		/* 201 */ YY_NO_ANCHOR,
		/* 202 */ YY_NO_ANCHOR,
		/* 203 */ YY_NOT_ACCEPT,
		/* 204 */ YY_NO_ANCHOR,
		/* 205 */ YY_NO_ANCHOR,
		/* 206 */ YY_NOT_ACCEPT,
		/* 207 */ YY_NO_ANCHOR,
		/* 208 */ YY_NO_ANCHOR,
		/* 209 */ YY_NOT_ACCEPT,
		/* 210 */ YY_NO_ANCHOR,
		/* 211 */ YY_NO_ANCHOR,
		/* 212 */ YY_NOT_ACCEPT,
		/* 213 */ YY_NO_ANCHOR,
		/* 214 */ YY_NO_ANCHOR,
		/* 215 */ YY_NOT_ACCEPT,
		/* 216 */ YY_NO_ANCHOR,
		/* 217 */ YY_NO_ANCHOR,
		/* 218 */ YY_NOT_ACCEPT,
		/* 219 */ YY_NO_ANCHOR,
		/* 220 */ YY_NO_ANCHOR,
		/* 221 */ YY_NOT_ACCEPT,
		/* 222 */ YY_NO_ANCHOR,
		/* 223 */ YY_NOT_ACCEPT,
		/* 224 */ YY_NO_ANCHOR,
		/* 225 */ YY_NOT_ACCEPT,
		/* 226 */ YY_NO_ANCHOR,
		/* 227 */ YY_NOT_ACCEPT,
		/* 228 */ YY_NO_ANCHOR,
		/* 229 */ YY_NOT_ACCEPT,
		/* 230 */ YY_NOT_ACCEPT,
		/* 231 */ YY_NOT_ACCEPT,
		/* 232 */ YY_NOT_ACCEPT,
		/* 233 */ YY_NOT_ACCEPT,
		/* 234 */ YY_NOT_ACCEPT,
		/* 235 */ YY_NOT_ACCEPT,
		/* 236 */ YY_NOT_ACCEPT,
		/* 237 */ YY_NOT_ACCEPT,
		/* 238 */ YY_NOT_ACCEPT,
		/* 239 */ YY_NOT_ACCEPT,
		/* 240 */ YY_NO_ANCHOR,
		/* 241 */ YY_NO_ANCHOR,
		/* 242 */ YY_NOT_ACCEPT,
		/* 243 */ YY_NOT_ACCEPT,
		/* 244 */ YY_NO_ANCHOR,
		/* 245 */ YY_NOT_ACCEPT,
		/* 246 */ YY_NO_ANCHOR,
		/* 247 */ YY_NO_ANCHOR,
		/* 248 */ YY_NO_ANCHOR,
		/* 249 */ YY_NO_ANCHOR,
		/* 250 */ YY_NO_ANCHOR,
		/* 251 */ YY_NO_ANCHOR,
		/* 252 */ YY_NOT_ACCEPT,
		/* 253 */ YY_NO_ANCHOR,
		/* 254 */ YY_NO_ANCHOR,
		/* 255 */ YY_NOT_ACCEPT,
		/* 256 */ YY_NOT_ACCEPT,
		/* 257 */ YY_NO_ANCHOR,
		/* 258 */ YY_NO_ANCHOR,
		/* 259 */ YY_NO_ANCHOR,
		/* 260 */ YY_NO_ANCHOR,
		/* 261 */ YY_NO_ANCHOR,
		/* 262 */ YY_NO_ANCHOR,
		/* 263 */ YY_NO_ANCHOR,
		/* 264 */ YY_NO_ANCHOR,
		/* 265 */ YY_NO_ANCHOR,
		/* 266 */ YY_NO_ANCHOR,
		/* 267 */ YY_NO_ANCHOR,
		/* 268 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"4,13:8,19,12,15:2,6,16:7,17:2,13,17:3,7,17,13,17,13,19,13,3,13:4,17,14,2,1," +
"38,35,18,39,37,41:10,45,33,34,40,48,13,68,32,66,31,44,24,36,66,23,20,66:2,3" +
"0,66,21,29,28,66,25,27,26,50,43,42,66:3,13,5,13:2,67,13,51,11,52,53,54,10,5" +
"5,56,57,55:2,58,55,9,59,60,55,61,62,8,63,64,65,55:3,47,22,49,46,13,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,269,
"0,1,2,1:5,3,1,4,5,6,1,7,1:4,8,9,1:6,10,1:2,11,12:2,1:8,13,14,1:2,15,1:2,16," +
"1:20,17,1:7,18,19,20,21,22,23,1:2,10:2,24,10:2,25,1,12,15,1,10,12,1,26,12,2" +
"7,28,1,29,10,12,1,30,10:2,12:3,25,1,15,10,31,12,10,12:2,10,32,33,34,35,12,3" +
"6,37,10,12:4,10:3,38,39,40,41,42,10:3,43,44,45,46,47,48,49,50,51,52,53,54,5" +
"5,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,8" +
"0,81,82,83,84,85,86,87,42,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,1" +
"03,104,105,106,107,108,109,110,111,112,113,24,114,115,116,117,118,119,14,12" +
"0,121,122,31,25,15,123,16,26,124,125,126,127,128,129,130,131,132,133,134,13" +
"5,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,1" +
"54,155,156,157")[0];

	private int yy_nxt[][] = unpackFromString(158,69,
"1,2,3,4,5,6,7,6,8,240,79,104,9,6,10,7,6:2,11,7,12,80,78,105,241,105,254,105" +
",261,126,264,266,105,13,14,15,140,16,17,18,19,20,268,105:2,21,22,23,6,24,10" +
"5,104,253,104,260,104:2,125,263,139,265,104:4,267,105,6,25,-1:71,26,-1:74,1" +
"04:4,-1:8,104:2,77,147,104,150,104:7,-1:3,104,-1:4,104:4,-1:5,104:6,147,104" +
":4,150,104:6,-1:2,28,-1:85,29,-1:58,105,30,31,105,-1:8,105,30,82,105:4,148," +
"105:5,-1:3,31,-1:4,105:4,-1:5,105:12,148,105:5,-1:19,33,-1:21,34,-1:76,35,-" +
"1:61,20,-1:35,104:4,-1:8,104:2,-1,104:10,-1:3,104,-1:4,104:4,-1:5,104:18,-1" +
":9,105:4,-1:8,105:2,179,246,105:9,-1:3,105,-1:4,105:4,-1:5,105:6,246,105:11" +
",-1:9,105:4,-1:8,105:2,-1,105:10,-1:3,105,-1:4,105:4,-1:5,105:18,-1:21,232," +
"-1,48,-1,47,252,-1,97,-1:26,47,-1:2,232,-1:3,252,97,-1:28,252,-1:2,252,-1:3" +
"5,252,-1:29,97,-1:4,97,-1:34,97,-1:26,234,-1,98,-1:21,49,-1:8,49,-1:3,234,-" +
"1:13,73,-1:88,170,-1,173,-1:25,176,-1:3,173,-1:8,176,-1:14,107,83,-1:9,106," +
"107,128,124,138,-1:2,146,-1,149,152,-1,155,-1:3,83,-1:14,155,-1:2,138,-1,12" +
"4,106,152,149,-1:2,146,-1:14,104:4,-1:8,27,104,81,104:9,159,-1:3,104,-1:4,1" +
"04:4,-1:5,104,159,104:5,27,104:10,-1:9,105:4,-1:8,105:2,103,105,151,105:4,1" +
"54,105:3,-1:3,105,-1:4,105:4,-1:5,105:4,151,105:4,154,105:8,-1:23,243,-1:7," +
"243,-1:27,243,-1:32,182,179,-1:19,245,-1:12,179,-1:7,245,-1:26,43,-1:5,43,-" +
"1:31,43,-1:30,47,-1,47,-1:29,47,-1:22,235,-1:13,235,-1:3,235,-1:43,57:2,58," +
"53,59,-1,57,60,61,62,63,64,57:56,-1:12,68,-1:64,36,-1:13,36,-1:3,36,-1:15,3" +
"7,-1:22,37,-1:25,179:2,-1:32,179,-1:32,232,-1,232,-1:34,232,-1:12,74,-1:87," +
"185,-1,197,-1,173,-1:29,173,-1:2,185,-1:19,104,141,108,104,-1:8,104,141,82," +
"104:4,171,104:5,-1:3,108,-1:4,104:4,-1:5,104:12,171,104:5,-1:9,105:2,32,105" +
",-1:8,105:2,84,105:10,-1:3,32,-1:4,105:4,-1:5,105:18,-1:9,36,-1:11,185,-1,1" +
"29,179,173,-1,36,188,-1,191,-1:2,194,-1:9,37,245,-1:7,194,-1:2,173,-1,179,1" +
"85,-1,191,-1:2,188,-1,245,37,-1:12,39,-1:11,39,41,-1,42,-1:2,231,43,229,90," +
"44,-1:20,44,-1,42,-1:3,90,229,43,-1,231,-1:14,38,-1:13,37,-1:3,38,-1:15,37," +
"-1:22,37,-1:11,104:2,109,104,-1:8,104:2,84,104:10,-1:3,109,-1:4,104:4,-1:5," +
"104:18,-1:9,105:4,-1:8,127,105,106,105:10,-1:3,105,-1:4,105:4,-1:5,105:7,12" +
"7,105:10,-1:9,104:4,-1:8,104:2,179,198,104:9,-1:3,104,-1:4,104:4,-1:5,104:6" +
",198,104:11,-1:23,114,-1:5,43,-1,114,-1:27,114,-1,43,-1:30,200,-1:9,203,-1:" +
"10,245,-1:7,203,-1:12,245,-1:12,104:4,-1:8,104:2,173,104,183,104:8,-1:3,104" +
",-1:4,104:4,-1:5,104:4,183,104:13,-1:9,105:4,-1:8,105:2,245,105:10,-1:3,105" +
",-1:4,105:2,258,105,-1:5,105:14,258,105:3,-1:9,36,-1:13,142,-1:3,36,-1:2,19" +
"1,-1:29,191,-1:17,104:4,-1:8,104:2,176,104:10,-1:3,104,-1:4,104:4,-1:5,186," +
"104:12,186,104:4,-1:9,105:4,-1:8,105:2,37,105:10,-1:3,105,-1:4,105,111,105:" +
"2,-1:5,105:15,111,105:2,-1:23,206,-1:4,209,-1:4,256,-1:18,256,-1:10,209,-1:" +
"14,104:4,-1:8,104:2,37,104:10,-1:3,104,-1:4,104,86,104:2,-1:5,104:15,86,104" +
":2,-1:9,110,105:3,-1:8,105:2,36,105:3,110,105:6,-1:3,105,-1:4,105:4,-1:5,10" +
"5:18,-1:23,188,-1:4,188,-1:34,188,-1:14,85,104:3,-1:8,104:2,36,104:3,85,104" +
":6,-1:3,104,-1:4,104:4,-1:5,104:18,-1:9,105:4,-1:8,105:2,203,105:9,178,-1:3" +
",105,-1:4,105:4,-1:5,105,178,105:16,-1:23,173,-1,173,-1:29,173,-1:22,104:4," +
"-1:8,104:2,243,104:7,257,104:2,-1:3,104,-1:4,104:4,-1:5,104:8,257,104:9,-1:" +
"9,105:4,-1:8,105:2,209,105:4,181,105:5,-1:3,105,-1:4,105:4,-1:5,105:12,181," +
"105:5,-1:23,215,-1:6,215,-1:29,215,-1:17,104:4,-1:8,104:2,256,104:9,262,-1:" +
"3,104,-1:4,104:4,-1:5,104,262,104:16,-1:9,105:4,-1:8,105:2,173,105,184,105:" +
"8,-1:3,105,-1:4,105:4,-1:5,105:4,184,105:13,-1:9,38,-1:13,87,-1:3,38,-1:2,2" +
"18,-1:29,218,-1:17,104:4,-1:8,104:2,188,104:4,189,104:5,-1:3,104,-1:4,104:4" +
",-1:5,104:12,189,104:5,-1:9,105:4,-1:8,105:2,215,105:6,187,105:3,-1:3,105,-" +
"1:4,105:4,-1:5,105:9,187,105:8,-1:21,185,-1,185,-1:34,185,-1:19,104:4,-1:8," +
"104:2,203,104:9,192,-1:3,104,-1:4,104:4,-1:5,104,192,104:16,-1:9,112,105:3," +
"-1:8,105:2,38,105:3,112,105:6,-1:3,105,-1:4,105:4,-1:5,105:18,-1:10,39,-1:1" +
"1,39:2,-1,40,-1:29,40,-1:22,104:4,-1:8,104:2,245,104:10,-1:3,104,-1:4,104:2" +
",259,104,-1:5,104:14,259,104:3,-1:9,105:4,-1:8,105:2,256,105:9,193,-1:3,105" +
",-1:4,105:4,-1:5,105,193,105:16,-1:10,39,-1:11,39:2,-1:54,130,104:3,-1:8,10" +
"4:2,38,104:3,130,104:6,-1:3,104,-1:4,104:4,-1:5,104:18,-1:9,105:4,-1:8,199," +
"105,185,105:10,-1:3,105,-1:4,105:4,-1:5,105:7,199,105:10,-1:23,40,-1,40,-1:" +
"29,40,-1:22,104:4,-1:8,104:2,218,104:6,201,104:3,-1:3,104,-1:4,104:4,-1:5,1" +
"04:9,201,104:8,-1:9,105:4,-1:8,105:2,44,105:8,134,105,-1:3,105,-1:4,105:4,-" +
"1:5,105:2,134,105:15,-1:23,225,-1,225,-1:29,225,-1:22,104:4,-1:8,207,104,18" +
"5,104:10,-1:3,104,-1:4,104:4,-1:5,104:7,207,104:10,-1:9,105:4,-1:8,105:2,94" +
",105,118,105:8,-1:3,105,-1:4,105:4,-1:5,105:4,118,105:13,-1:23,227,-1,225,-" +
"1:4,229,-1:24,225,-1:4,229,-1:17,104,88,104:2,-1:8,104,88,39,104:10,-1:3,10" +
"4,-1:4,104:4,-1:5,104:18,-1:9,105,131,105:2,-1:8,105,131,39,105:10,-1:3,105" +
",-1:4,105:4,-1:5,105:18,-1:23,230,-1:7,230,-1:27,230,-1:18,104:4,-1:8,104:2" +
",40,104,89,104:8,-1:3,104,-1:4,104:4,-1:5,104:4,89,104:13,-1:9,105:4,-1:8,1" +
"05:2,114,105:7,132,105:2,-1:3,105,-1:4,105:4,-1:5,105:8,132,105:9,-1:23,91," +
"-1,91,-1:29,91,-1:22,104:4,-1:8,104:2,91,104,144,104:8,-1:3,104,-1:4,104:4," +
"-1:5,104:4,144,104:13,-1:9,105:4,-1:8,105:2,43,105:5,92,105:4,-1:3,105,-1:4" +
",105:4,-1:5,105:10,92,105:7,-1:9,104:4,-1:8,104:2,44,104:8,145,104,-1:3,104" +
",-1:4,104:4,-1:5,104:2,145,104:15,-1:9,105:4,-1:8,105:2,231,105:4,208,105:5" +
",-1:3,105,-1:4,105:4,-1:5,105:12,208,105:5,-1:23,93,-1:4,231,-1:3,44,-1:20," +
"44,-1:9,231,-1:14,104:4,-1:8,104:2,94,104,135,104:8,-1:3,104,-1:4,104:4,-1:" +
"5,104:4,135,104:13,-1:9,105:4,-1:8,105:2,91,105,133,105:8,-1:3,105,-1:4,105" +
":4,-1:5,105:4,133,105:13,-1:10,39,-1:11,39,113,-1:7,230,-1:27,230,-1:18,104" +
":4,-1:8,104:2,225,104,216,104:8,-1:3,104,-1:4,104:4,-1:5,104:4,216,104:13,-" +
"1:9,105:4,-1:8,105:2,230,105:7,211,105:2,-1:3,105,-1:4,105:4,-1:5,105:8,211" +
",105:9,-1:23,117,-1:6,229,-1,44,-1:20,44,-1:6,229,-1:17,104:4,-1:8,104:2,43" +
",104:5,116,104:4,-1:3,104,-1:4,104:4,-1:5,104:10,116,104:7,-1:9,105:4,-1:8," +
"105:2,252,105:2,250,105:7,-1:3,105,-1:4,105:4,-1:5,105:11,250,105:6,-1:23,4" +
"4,-1:8,44,-1:20,44,-1:24,104:4,-1:8,104:2,114,104:7,143,104:2,-1:3,104,-1:4" +
",104:4,-1:5,104:8,143,104:9,-1:9,105:4,-1:8,214,105,232,105:10,-1:3,105,-1:" +
"4,105:4,-1:5,105:7,214,105:10,-1:23,45,-1,94,-1:2,231,-1:26,94,-1:7,231,-1:" +
"14,104:4,-1:8,104:2,230,104:7,222,104:2,-1:3,104,-1:4,104:4,-1:5,104:8,222," +
"104:9,-1:9,105:4,-1:8,105:2,97,105:4,120,105:5,-1:3,105,-1:4,105:4,-1:5,105" +
":12,120,105:5,-1:23,94,-1,94,-1:29,94,-1:22,104:4,-1:8,104:2,46,104,95,104:" +
"8,-1:3,104,-1:4,104:4,-1:5,104:4,95,104:13,-1:9,105:4,-1:8,105:2,47,105,96," +
"105:8,-1:3,105,-1:4,105:4,-1:5,105:4,96,105:13,-1:23,94,-1,94,-1:6,44,-1:20" +
",44,-1,94,-1:22,104:4,-1:8,104:2,97,104:4,136,104:5,-1:3,104,-1:4,104:4,-1:" +
"5,104:12,136,104:5,-1:9,105:4,-1:8,105:2,49,105:10,-1:3,105,-1:4,105:3,121," +
"-1:5,105:3,121,105:14,-1:23,114,-1:7,114,-1:27,114,-1:18,104:4,-1:8,104:2,2" +
"52,104:2,251,104:7,-1:3,104,-1:4,104:4,-1:5,104:11,251,104:6,-1:9,220,105:3" +
",-1:8,105:2,235,105:3,220,105:6,-1:3,105,-1:4,105:4,-1:5,105:18,-1:9,104:4," +
"-1:8,224,104,232,104:10,-1:3,104,-1:4,104:4,-1:5,104:7,224,104:10,-1:9,105:" +
"4,-1:8,105:2,50,105:4,99,105:5,-1:3,105,-1:4,105:4,-1:5,105:12,99,105:5,-1:" +
"23,115,-1,91,-1:2,231,-1:26,91,-1:7,231,-1:14,104:4,-1:8,104:2,47,104,119,1" +
"04:8,-1:3,104,-1:4,104:4,-1:5,104:4,119,104:13,-1:23,46,-1,46,-1:29,46,-1:2" +
"2,104:4,-1:8,104:2,49,104:10,-1:3,104,-1:4,104:3,137,-1:5,104:3,137,104:14," +
"-1:9,228,104:3,-1:8,104:2,235,104:3,228,104:6,-1:3,104,-1:4,104:4,-1:5,104:" +
"18,-1:21,232,-1,233,-1:2,252,-1:31,232,-1:3,252,-1:15,104:4,-1:8,104:2,50,1" +
"04:4,122,104:5,-1:3,104,-1:4,104:4,-1:5,104:12,122,104:5,-1:23,49,-1:21,49," +
"-1:8,49,-1:37,50,-1:4,50,-1:34,50,-1:6,1,51:2,52,53,100,54,55,51:4,56,51:56" +
",1,65:2,66,65,101,-1,65:5,67,65:56,1,69,102:4,70,71,102:4,72,102,123,70:2,7" +
"1,102:51,1,75:11,76,75:56,-1:8,104:4,-1:8,104:2,103,104,153,104:4,156,104:3" +
",-1:3,104,-1:4,104:4,-1:5,104:4,153,104:4,156,104:8,-1:9,105:4,-1:8,105:2,2" +
"42,105:4,157,105:2,160,105:2,-1:3,105,-1:4,105:4,-1:5,105:8,160,105:3,157,1" +
"05:5,-1:23,212,-1:4,209,-1:4,203,-1:18,203,-1:10,209,-1:28,223,-1:4,223,-1:" +
"34,223,-1:14,105:4,-1:8,105:2,188,105:4,196,105:5,-1:3,105,-1:4,105:4,-1:5," +
"105:12,196,105:5,-1:23,229,-1:6,229,-1:29,229,-1:17,105:4,-1:8,105:2,225,10" +
"5,202,105:8,-1:3,105,-1:4,105:4,-1:5,105:4,202,105:13,-1:9,104:4,-1:8,104:2" +
",209,104:4,195,104:5,-1:3,104,-1:4,104:4,-1:5,104:12,195,104:5,-1:9,105:4,-" +
"1:8,105:2,218,105:6,190,105:3,-1:3,105,-1:4,105:4,-1:5,105:9,190,105:8,-1:9" +
",104:4,-1:8,104:2,215,104:6,204,104:3,-1:3,104,-1:4,104:4,-1:5,104:9,204,10" +
"4:8,-1:9,105:4,-1:8,217,105,234,105:10,-1:3,105,-1:4,105:4,-1:5,105:7,217,1" +
"05:10,-1:9,104:4,-1:8,226,104,234,104:10,-1:3,104,-1:4,104:4,-1:5,104:7,226" +
",104:10,-1:21,234,-1,234,-1:34,234,-1:19,104:4,-1:8,104:2,255,104:7,162,104" +
",165,-1:3,104,-1:4,104:4,-1:5,104,165,104:6,162,104:9,-1:9,105:4,-1:8,105:2" +
",158,163,105:9,-1:3,105,-1:4,105:4,-1:5,105:6,163,105:11,-1:23,221,-1:4,188" +
",-1:4,256,-1:18,256,-1:10,188,-1:28,231,-1:4,231,-1:34,231,-1:14,104:4,-1:8" +
",104:2,223,104:4,210,104:5,-1:3,104,-1:4,104:4,-1:5,104:12,210,104:5,-1:9,1" +
"05:4,-1:8,105:2,229,105:6,205,105:3,-1:3,105,-1:4,105:4,-1:5,105:9,205,105:" +
"8,-1:9,104:4,-1:8,104:2,229,104:6,219,104:3,-1:3,104,-1:4,104:4,-1:5,104:9," +
"219,104:8,-1:9,104:4,-1:8,104:2,242,104:4,168,104:2,247,104:2,-1:3,104,-1:4" +
",104:4,-1:5,104:8,247,104:3,168,104:5,-1:9,105:4,-1:8,105:2,161,105:6,166,1" +
"05:3,-1:3,105,-1:4,105:4,-1:5,105:9,166,105:8,-1:9,104:4,-1:8,104:2,231,104" +
":4,213,104:5,-1:3,104,-1:4,104:4,-1:5,104:12,213,104:5,-1:9,104:4,-1:8,104:" +
"2,164,104,174,104:4,177,104:3,-1:3,104,-1:4,104:4,-1:5,104:4,174,104:4,177," +
"104:8,-1:9,105:4,-1:8,105:2,164,105,169,105:4,248,105:3,-1:3,105,-1:4,105:4" +
",-1:5,105:4,169,105:4,248,105:8,-1:9,104:4,-1:8,104:2,161,104:6,249,104:3,-" +
"1:3,104,-1:4,104:4,-1:5,104:9,249,104:8,-1:9,105:4,-1:8,105:2,255,105:7,172" +
",105,244,-1:3,105,-1:4,105:4,-1:5,105,244,105:6,172,105:9,-1:9,104:4,-1:8,1" +
"04:2,167,180,104:9,-1:3,104,-1:4,104:4,-1:5,104:6,180,104:11,-1:9,105:4,-1:" +
"8,105:2,167,175,105:9,-1:3,105,-1:4,105:4,-1:5,105:6,175,105:11,-1");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

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
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{ return new Symbol(TokenConstants.MULT); }
					case -3:
						break;
					case 3:
						{ return new Symbol(TokenConstants.RPAREN); }
					case -4:
						break;
					case 4:
						{	string_buf.append(yytext());
														yybegin(STR_LEX1); }
					case -5:
						break;
					case 5:
						{ return new Symbol(TokenConstants.ERROR, "\\000"); }
					case -6:
						break;
					case 6:
						{ return new Symbol(TokenConstants.ERROR, yytext()); }
					case -7:
						break;
					case 7:
						{ }
					case -8:
						break;
					case 8:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -9:
						break;
					case 9:
						{ curr_lineno++; }
					case -10:
						break;
					case 10:
						{ return new Symbol(TokenConstants.LPAREN); }
					case -11:
						break;
					case 11:
						{ return new Symbol(TokenConstants.MINUS); }
					case -12:
						break;
					case 12:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -13:
						break;
					case 13:
						{ return new Symbol(TokenConstants.SEMI); }
					case -14:
						break;
					case 14:
						{ return new Symbol(TokenConstants.LT); }
					case -15:
						break;
					case 15:
						{ return new Symbol(TokenConstants.COMMA); }
					case -16:
						break;
					case 16:
						{ return new Symbol(TokenConstants.DIV); }
					case -17:
						break;
					case 17:
						{ return new Symbol(TokenConstants.PLUS); }
					case -18:
						break;
					case 18:
						{ return new Symbol(TokenConstants.DOT); }
					case -19:
						break;
					case 19:
						{ return new Symbol(TokenConstants.EQ); }
					case -20:
						break;
					case 20:
						{ return new Symbol(TokenConstants.INT_CONST,
									new IntSymbol(yytext(), yytext().length(), count_int++)); }
					case -21:
						break;
					case 21:
						{ return new Symbol(TokenConstants.COLON); }
					case -22:
						break;
					case 22:
						{ return new Symbol(TokenConstants.NEG); }
					case -23:
						break;
					case 23:
						{ return new Symbol(TokenConstants.LBRACE); }
					case -24:
						break;
					case 24:
						{ return new Symbol(TokenConstants.RBRACE); }
					case -25:
						break;
					case 25:
						{ return new Symbol(TokenConstants.AT); }
					case -26:
						break;
					case 26:
						{ return new Symbol(TokenConstants.ERROR, "Unmatched *)");}
					case -27:
						break;
					case 27:
						{ return new Symbol(TokenConstants.FI); }
					case -28:
						break;
					case 28:
						{	yybegin(COMMENTS); }
					case -29:
						break;
					case 29:
						{ yybegin(COMMENTS2); }
					case -30:
						break;
					case 30:
						{ return new Symbol(TokenConstants.IN); }
					case -31:
						break;
					case 31:
						{ return new Symbol(TokenConstants.IF); }
					case -32:
						break;
					case 32:
						{ return new Symbol(TokenConstants.OF); }
					case -33:
						break;
					case 33:
						{ return new Symbol(TokenConstants.ASSIGN); }
					case -34:
						break;
					case 34:
						{ return new Symbol(TokenConstants.LE); }
					case -35:
						break;
					case 35:
						{ return new Symbol(TokenConstants.DARROW); }
					case -36:
						break;
					case 36:
						{ return new Symbol(TokenConstants.NOT); }
					case -37:
						break;
					case 37:
						{ return new Symbol(TokenConstants.NEW); }
					case -38:
						break;
					case 38:
						{ return new Symbol(TokenConstants.LET); }
					case -39:
						break;
					case 39:
						{ return new Symbol(TokenConstants.THEN); }
					case -40:
						break;
					case 40:
						{	return new Symbol(TokenConstants.BOOL_CONST, true); }
					case -41:
						break;
					case 41:
						{ return new Symbol(TokenConstants.POOL); }
					case -42:
						break;
					case 42:
						{ return new Symbol(TokenConstants.CASE); }
					case -43:
						break;
					case 43:
						{ return new Symbol(TokenConstants.LOOP); }
					case -44:
						break;
					case 44:
						{ return new Symbol(TokenConstants.ESAC); }
					case -45:
						break;
					case 45:
						{ return new Symbol(TokenConstants.ELSE); }
					case -46:
						break;
					case 46:
						{	return new Symbol(TokenConstants.BOOL_CONST, false); }
					case -47:
						break;
					case 47:
						{ return new Symbol(TokenConstants.WHILE); }
					case -48:
						break;
					case 48:
						{ return new Symbol(TokenConstants.CLASS); }
					case -49:
						break;
					case 49:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -50:
						break;
					case 50:
						{	return new Symbol(TokenConstants.INHERITS); }
					case -51:
						break;
					case 51:
						{ string_buf.append(yytext()); }
					case -52:
						break;
					case 52:
						{ string_buf.append(yytext());
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
					case -53:
						break;
					case 53:
						{	yybegin(STR_NULL);
														return new Symbol(TokenConstants.ERROR, "String contains null character."); }
					case -54:
						break;
					case 54:
						{ string_buf.append("\015"); }
					case -55:
						break;
					case 55:
						{ string_buf.append("\033"); }
					case -56:
						break;
					case 56:
						{	yybegin(YYINITIAL);
														curr_lineno++;
														string_buf.setLength(0);
														return new Symbol(TokenConstants.ERROR, "Unterminated string constant");}
					case -57:
						break;
					case 57:
						{	string_buf.append(yytext().charAt(1)); }
					case -58:
						break;
					case 58:
						{	string_buf.append("\""); }
					case -59:
						break;
					case 59:
						{	string_buf.append("\\"); }
					case -60:
						break;
					case 60:
						{	string_buf.append("\t"); }
					case -61:
						break;
					case 61:
						{	string_buf.append("\n"); }
					case -62:
						break;
					case 62:
						{	string_buf.append("\f"); }
					case -63:
						break;
					case 63:
						{	string_buf.append("\b"); }
					case -64:
						break;
					case 64:
						{ string_buf.append("\n");
													curr_lineno++; }
					case -65:
						break;
					case 65:
						{ }
					case -66:
						break;
					case 66:
						{ yybegin(YYINITIAL); }
					case -67:
						break;
					case 67:
						{ yybegin(YYINITIAL);
														curr_lineno++; }
					case -68:
						break;
					case 68:
						{	curr_lineno++; }
					case -69:
						break;
					case 69:
						{	 }
					case -70:
						break;
					case 70:
						{ }
					case -71:
						break;
					case 71:
						{ }
					case -72:
						break;
					case 72:
						{ curr_lineno++;}
					case -73:
						break;
					case 73:
						{ if (count_comm == 0)	yybegin(YYINITIAL);
												 		else count_comm--;	}
					case -74:
						break;
					case 74:
						{ count_comm++; }
					case -75:
						break;
					case 75:
						{ }
					case -76:
						break;
					case 76:
						{	yybegin(YYINITIAL); curr_lineno++; }
					case -77:
						break;
					case 78:
						{ return new Symbol(TokenConstants.ERROR, yytext()); }
					case -78:
						break;
					case 79:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -79:
						break;
					case 80:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -80:
						break;
					case 81:
						{ return new Symbol(TokenConstants.FI); }
					case -81:
						break;
					case 82:
						{ return new Symbol(TokenConstants.IN); }
					case -82:
						break;
					case 83:
						{ return new Symbol(TokenConstants.IF); }
					case -83:
						break;
					case 84:
						{ return new Symbol(TokenConstants.OF); }
					case -84:
						break;
					case 85:
						{ return new Symbol(TokenConstants.NOT); }
					case -85:
						break;
					case 86:
						{ return new Symbol(TokenConstants.NEW); }
					case -86:
						break;
					case 87:
						{ return new Symbol(TokenConstants.LET); }
					case -87:
						break;
					case 88:
						{ return new Symbol(TokenConstants.THEN); }
					case -88:
						break;
					case 89:
						{	return new Symbol(TokenConstants.BOOL_CONST, true); }
					case -89:
						break;
					case 90:
						{ return new Symbol(TokenConstants.POOL); }
					case -90:
						break;
					case 91:
						{ return new Symbol(TokenConstants.CASE); }
					case -91:
						break;
					case 92:
						{ return new Symbol(TokenConstants.LOOP); }
					case -92:
						break;
					case 93:
						{ return new Symbol(TokenConstants.ESAC); }
					case -93:
						break;
					case 94:
						{ return new Symbol(TokenConstants.ELSE); }
					case -94:
						break;
					case 95:
						{	return new Symbol(TokenConstants.BOOL_CONST, false); }
					case -95:
						break;
					case 96:
						{ return new Symbol(TokenConstants.WHILE); }
					case -96:
						break;
					case 97:
						{ return new Symbol(TokenConstants.CLASS); }
					case -97:
						break;
					case 98:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -98:
						break;
					case 99:
						{	return new Symbol(TokenConstants.INHERITS); }
					case -99:
						break;
					case 100:
						{ string_buf.append(yytext()); }
					case -100:
						break;
					case 101:
						{ }
					case -101:
						break;
					case 102:
						{	 }
					case -102:
						break;
					case 104:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -103:
						break;
					case 105:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -104:
						break;
					case 106:
						{ return new Symbol(TokenConstants.FI); }
					case -105:
						break;
					case 107:
						{ return new Symbol(TokenConstants.IN); }
					case -106:
						break;
					case 108:
						{ return new Symbol(TokenConstants.IF); }
					case -107:
						break;
					case 109:
						{ return new Symbol(TokenConstants.OF); }
					case -108:
						break;
					case 110:
						{ return new Symbol(TokenConstants.NOT); }
					case -109:
						break;
					case 111:
						{ return new Symbol(TokenConstants.NEW); }
					case -110:
						break;
					case 112:
						{ return new Symbol(TokenConstants.LET); }
					case -111:
						break;
					case 113:
						{ return new Symbol(TokenConstants.THEN); }
					case -112:
						break;
					case 114:
						{ return new Symbol(TokenConstants.POOL); }
					case -113:
						break;
					case 115:
						{ return new Symbol(TokenConstants.CASE); }
					case -114:
						break;
					case 116:
						{ return new Symbol(TokenConstants.LOOP); }
					case -115:
						break;
					case 117:
						{ return new Symbol(TokenConstants.ESAC); }
					case -116:
						break;
					case 118:
						{ return new Symbol(TokenConstants.ELSE); }
					case -117:
						break;
					case 119:
						{ return new Symbol(TokenConstants.WHILE); }
					case -118:
						break;
					case 120:
						{ return new Symbol(TokenConstants.CLASS); }
					case -119:
						break;
					case 121:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -120:
						break;
					case 122:
						{	return new Symbol(TokenConstants.INHERITS); }
					case -121:
						break;
					case 123:
						{	 }
					case -122:
						break;
					case 125:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -123:
						break;
					case 126:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -124:
						break;
					case 127:
						{ return new Symbol(TokenConstants.FI); }
					case -125:
						break;
					case 128:
						{ return new Symbol(TokenConstants.IN); }
					case -126:
						break;
					case 129:
						{ return new Symbol(TokenConstants.NOT); }
					case -127:
						break;
					case 130:
						{ return new Symbol(TokenConstants.LET); }
					case -128:
						break;
					case 131:
						{ return new Symbol(TokenConstants.THEN); }
					case -129:
						break;
					case 132:
						{ return new Symbol(TokenConstants.POOL); }
					case -130:
						break;
					case 133:
						{ return new Symbol(TokenConstants.CASE); }
					case -131:
						break;
					case 134:
						{ return new Symbol(TokenConstants.ESAC); }
					case -132:
						break;
					case 135:
						{ return new Symbol(TokenConstants.ELSE); }
					case -133:
						break;
					case 136:
						{ return new Symbol(TokenConstants.CLASS); }
					case -134:
						break;
					case 137:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -135:
						break;
					case 139:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -136:
						break;
					case 140:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -137:
						break;
					case 141:
						{ return new Symbol(TokenConstants.IN); }
					case -138:
						break;
					case 142:
						{ return new Symbol(TokenConstants.NOT); }
					case -139:
						break;
					case 143:
						{ return new Symbol(TokenConstants.POOL); }
					case -140:
						break;
					case 144:
						{ return new Symbol(TokenConstants.CASE); }
					case -141:
						break;
					case 145:
						{ return new Symbol(TokenConstants.ESAC); }
					case -142:
						break;
					case 147:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -143:
						break;
					case 148:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -144:
						break;
					case 150:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -145:
						break;
					case 151:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -146:
						break;
					case 153:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -147:
						break;
					case 154:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -148:
						break;
					case 156:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -149:
						break;
					case 157:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -150:
						break;
					case 159:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -151:
						break;
					case 160:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -152:
						break;
					case 162:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -153:
						break;
					case 163:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -154:
						break;
					case 165:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -155:
						break;
					case 166:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -156:
						break;
					case 168:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -157:
						break;
					case 169:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -158:
						break;
					case 171:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -159:
						break;
					case 172:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -160:
						break;
					case 174:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -161:
						break;
					case 175:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -162:
						break;
					case 177:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -163:
						break;
					case 178:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -164:
						break;
					case 180:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -165:
						break;
					case 181:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -166:
						break;
					case 183:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -167:
						break;
					case 184:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -168:
						break;
					case 186:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -169:
						break;
					case 187:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -170:
						break;
					case 189:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -171:
						break;
					case 190:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -172:
						break;
					case 192:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -173:
						break;
					case 193:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -174:
						break;
					case 195:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -175:
						break;
					case 196:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -176:
						break;
					case 198:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -177:
						break;
					case 199:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -178:
						break;
					case 201:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -179:
						break;
					case 202:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -180:
						break;
					case 204:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -181:
						break;
					case 205:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -182:
						break;
					case 207:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -183:
						break;
					case 208:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -184:
						break;
					case 210:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -185:
						break;
					case 211:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -186:
						break;
					case 213:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -187:
						break;
					case 214:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -188:
						break;
					case 216:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -189:
						break;
					case 217:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -190:
						break;
					case 219:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -191:
						break;
					case 220:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -192:
						break;
					case 222:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -193:
						break;
					case 224:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -194:
						break;
					case 226:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -195:
						break;
					case 228:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -196:
						break;
					case 240:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -197:
						break;
					case 241:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -198:
						break;
					case 244:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -199:
						break;
					case 246:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -200:
						break;
					case 247:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -201:
						break;
					case 248:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -202:
						break;
					case 249:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -203:
						break;
					case 250:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -204:
						break;
					case 251:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -205:
						break;
					case 253:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -206:
						break;
					case 254:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -207:
						break;
					case 257:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -208:
						break;
					case 258:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -209:
						break;
					case 259:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -210:
						break;
					case 260:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -211:
						break;
					case 261:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -212:
						break;
					case 262:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -213:
						break;
					case 263:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -214:
						break;
					case 264:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -215:
						break;
					case 265:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -216:
						break;
					case 266:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -217:
						break;
					case 267:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -218:
						break;
					case 268:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -219:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
