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
	private final int COMMENTS2 = 5;
	private final int YYINITIAL = 0;
	private final int TERMINAR = 4;
	private final int COMMENTS = 3;
	private final int STR_NULL = 2;
	private final int STR_LEX1 = 1;
	private final int yy_state_dtrans[] = {
		0,
		237,
		238,
		239,
		240,
		241
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
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NOT_ACCEPT,
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
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NOT_ACCEPT,
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
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NOT_ACCEPT,
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
		/* 138 */ YY_NO_ANCHOR,
		/* 139 */ YY_NOT_ACCEPT,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NO_ANCHOR,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NO_ANCHOR,
		/* 144 */ YY_NO_ANCHOR,
		/* 145 */ YY_NO_ANCHOR,
		/* 146 */ YY_NO_ANCHOR,
		/* 147 */ YY_NOT_ACCEPT,
		/* 148 */ YY_NO_ANCHOR,
		/* 149 */ YY_NO_ANCHOR,
		/* 150 */ YY_NOT_ACCEPT,
		/* 151 */ YY_NO_ANCHOR,
		/* 152 */ YY_NO_ANCHOR,
		/* 153 */ YY_NOT_ACCEPT,
		/* 154 */ YY_NO_ANCHOR,
		/* 155 */ YY_NO_ANCHOR,
		/* 156 */ YY_NOT_ACCEPT,
		/* 157 */ YY_NO_ANCHOR,
		/* 158 */ YY_NO_ANCHOR,
		/* 159 */ YY_NOT_ACCEPT,
		/* 160 */ YY_NO_ANCHOR,
		/* 161 */ YY_NO_ANCHOR,
		/* 162 */ YY_NOT_ACCEPT,
		/* 163 */ YY_NO_ANCHOR,
		/* 164 */ YY_NO_ANCHOR,
		/* 165 */ YY_NOT_ACCEPT,
		/* 166 */ YY_NO_ANCHOR,
		/* 167 */ YY_NO_ANCHOR,
		/* 168 */ YY_NOT_ACCEPT,
		/* 169 */ YY_NO_ANCHOR,
		/* 170 */ YY_NO_ANCHOR,
		/* 171 */ YY_NOT_ACCEPT,
		/* 172 */ YY_NO_ANCHOR,
		/* 173 */ YY_NO_ANCHOR,
		/* 174 */ YY_NOT_ACCEPT,
		/* 175 */ YY_NO_ANCHOR,
		/* 176 */ YY_NO_ANCHOR,
		/* 177 */ YY_NOT_ACCEPT,
		/* 178 */ YY_NO_ANCHOR,
		/* 179 */ YY_NO_ANCHOR,
		/* 180 */ YY_NOT_ACCEPT,
		/* 181 */ YY_NO_ANCHOR,
		/* 182 */ YY_NO_ANCHOR,
		/* 183 */ YY_NOT_ACCEPT,
		/* 184 */ YY_NO_ANCHOR,
		/* 185 */ YY_NO_ANCHOR,
		/* 186 */ YY_NOT_ACCEPT,
		/* 187 */ YY_NO_ANCHOR,
		/* 188 */ YY_NO_ANCHOR,
		/* 189 */ YY_NOT_ACCEPT,
		/* 190 */ YY_NO_ANCHOR,
		/* 191 */ YY_NO_ANCHOR,
		/* 192 */ YY_NOT_ACCEPT,
		/* 193 */ YY_NO_ANCHOR,
		/* 194 */ YY_NO_ANCHOR,
		/* 195 */ YY_NOT_ACCEPT,
		/* 196 */ YY_NO_ANCHOR,
		/* 197 */ YY_NO_ANCHOR,
		/* 198 */ YY_NOT_ACCEPT,
		/* 199 */ YY_NO_ANCHOR,
		/* 200 */ YY_NO_ANCHOR,
		/* 201 */ YY_NOT_ACCEPT,
		/* 202 */ YY_NO_ANCHOR,
		/* 203 */ YY_NO_ANCHOR,
		/* 204 */ YY_NOT_ACCEPT,
		/* 205 */ YY_NO_ANCHOR,
		/* 206 */ YY_NO_ANCHOR,
		/* 207 */ YY_NOT_ACCEPT,
		/* 208 */ YY_NO_ANCHOR,
		/* 209 */ YY_NO_ANCHOR,
		/* 210 */ YY_NOT_ACCEPT,
		/* 211 */ YY_NO_ANCHOR,
		/* 212 */ YY_NO_ANCHOR,
		/* 213 */ YY_NOT_ACCEPT,
		/* 214 */ YY_NO_ANCHOR,
		/* 215 */ YY_NO_ANCHOR,
		/* 216 */ YY_NOT_ACCEPT,
		/* 217 */ YY_NO_ANCHOR,
		/* 218 */ YY_NO_ANCHOR,
		/* 219 */ YY_NOT_ACCEPT,
		/* 220 */ YY_NO_ANCHOR,
		/* 221 */ YY_NO_ANCHOR,
		/* 222 */ YY_NOT_ACCEPT,
		/* 223 */ YY_NO_ANCHOR,
		/* 224 */ YY_NOT_ACCEPT,
		/* 225 */ YY_NO_ANCHOR,
		/* 226 */ YY_NOT_ACCEPT,
		/* 227 */ YY_NO_ANCHOR,
		/* 228 */ YY_NOT_ACCEPT,
		/* 229 */ YY_NO_ANCHOR,
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
		/* 240 */ YY_NOT_ACCEPT,
		/* 241 */ YY_NOT_ACCEPT,
		/* 242 */ YY_NO_ANCHOR,
		/* 243 */ YY_NO_ANCHOR,
		/* 244 */ YY_NOT_ACCEPT,
		/* 245 */ YY_NOT_ACCEPT,
		/* 246 */ YY_NO_ANCHOR,
		/* 247 */ YY_NOT_ACCEPT,
		/* 248 */ YY_NO_ANCHOR,
		/* 249 */ YY_NO_ANCHOR,
		/* 250 */ YY_NO_ANCHOR,
		/* 251 */ YY_NO_ANCHOR,
		/* 252 */ YY_NO_ANCHOR,
		/* 253 */ YY_NO_ANCHOR,
		/* 254 */ YY_NOT_ACCEPT,
		/* 255 */ YY_NO_ANCHOR,
		/* 256 */ YY_NO_ANCHOR,
		/* 257 */ YY_NOT_ACCEPT,
		/* 258 */ YY_NOT_ACCEPT,
		/* 259 */ YY_NO_ANCHOR,
		/* 260 */ YY_NO_ANCHOR,
		/* 261 */ YY_NO_ANCHOR,
		/* 262 */ YY_NO_ANCHOR,
		/* 263 */ YY_NO_ANCHOR,
		/* 264 */ YY_NO_ANCHOR,
		/* 265 */ YY_NO_ANCHOR,
		/* 266 */ YY_NO_ANCHOR,
		/* 267 */ YY_NO_ANCHOR,
		/* 268 */ YY_NO_ANCHOR,
		/* 269 */ YY_NO_ANCHOR,
		/* 270 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"6,1:8,68,2,15:2,8,16:7,17:2,1,17:3,9,17,1,17,1,68,1,5,1:4,17,14,4,3,37,34,1" +
"8,38,36,40:10,44,32,33,39,47,1,67,31,65,30,43,23,35,65,22,19,65:2,29,65,20," +
"28,27,65,24,26,25,49,42,41,65:3,1,7,1:2,66,1,50,13,51,52,53,12,54,55,56,54:" +
"2,57,54,11,58,59,54,60,61,10,62,63,64,54:3,46,21,48,45,1,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,271,
"0,1:3,2,1:4,3,4,5,6,1,7,1:4,8,9,1:6,10,1:2,11,12:2,1:8,13,14,1:2,15,1:2,16," +
"1:29,17,18,19,20,21,22,1:2,10:2,23,10:2,24,1,12,15,1,10,12,1,25,12,26,27,28" +
",29,10,12,1,30,10:2,12:3,24,1,15,10,31,12,10,12:2,10,32,33,34,35,12,36,37,1" +
"0,12:4,10:3,38,39,40,41,42,10:3,43,44,45,46,47,48,49,50,51,52,53,54,55,56,5" +
"7,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,8" +
"2,83,84,85,86,87,42,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104" +
",105,106,107,108,109,110,111,112,113,23,114,115,116,117,118,119,14,120,121," +
"122,31,24,15,123,16,25,124,125,126,127,128,129,130,131,132,133,134,135,136," +
"137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155" +
",156,157,158")[0];

	private int yy_nxt[][] = unpackFromString(159,69,
"1,2,3,4,5,6,7,2,8,2,9,242,80,105,10,8,2:2,11,12,81,79,106,243,106,256,106,2" +
"63,127,266,268,106,13,14,15,141,16,17,18,19,20,270,106:2,21,22,23,2,24,106," +
"105,255,105,262,105:2,126,265,140,267,105:4,269,106,2,25,8,-1:73,26,-1:74,1" +
"05:4,-1:5,105:2,78,148,105,151,105:7,-1:3,105,-1:4,105:4,-1:5,105:6,148,105" +
":4,151,105:6,-1:5,28,-1:83,29,-1:60,106,30,31,106,-1:5,106,30,83,106:4,149," +
"106:5,-1:3,31,-1:4,106:4,-1:5,106:12,149,106:5,-1:20,33,-1:20,34,-1:76,35,-" +
"1:61,20,-1:38,105:4,-1:5,105:2,-1,105:10,-1:3,105,-1:4,105:4,-1:5,105:18,-1" +
":12,106:4,-1:5,106:2,180,248,106:9,-1:3,106,-1:4,106:4,-1:5,106:6,248,106:1" +
"1,-1:12,106:4,-1:5,106:2,-1,106:10,-1:3,106,-1:4,106:4,-1:5,106:18,-1:21,23" +
"3,-1,48,-1,47,254,-1,98,-1:26,47,-1:2,233,-1:3,254,98,-1:28,254,-1:2,254,-1" +
":35,254,-1:29,98,-1:4,98,-1:34,98,-1:26,235,-1,99,-1:21,49,-1:8,49,-1:3,235" +
",-1:33,171,-1,174,-1:25,177,-1:3,174,-1:8,177,-1:17,108,84,-1:6,107,108,129" +
",125,139,-1:2,147,-1,150,153,-1,156,-1:3,84,-1:14,156,-1:2,139,-1,125,107,1" +
"53,150,-1:2,147,-1:17,105:4,-1:5,27,105,82,105:9,160,-1:3,105,-1:4,105:4,-1" +
":5,105,160,105:5,27,105:10,-1:12,106:4,-1:5,106:2,104,106,152,106:4,155,106" +
":3,-1:3,106,-1:4,106:4,-1:5,106:4,152,106:4,155,106:8,-1:23,245,-1:7,245,-1" +
":27,245,-1:32,183,180,-1:19,247,-1:12,180,-1:7,247,-1:26,43,-1:5,43,-1:31,4" +
"3,-1:30,47,-1,47,-1:29,47,-1:25,236,-1:10,236,-1:3,236,-1:44,57,58,57:2,59," +
"54,60,-1,57,61,62,63,64,57:55,-1:2,68,-1:70,73,-1:74,36,-1:10,36,-1:3,36,-1" +
":15,37,-1:22,37,-1:25,180:2,-1:32,180,-1:32,233,-1,233,-1:34,233,-1:15,74,-" +
"1:84,186,-1,198,-1,174,-1:29,174,-1:2,186,-1:22,105,142,109,105,-1:5,105,14" +
"2,83,105:4,172,105:5,-1:3,109,-1:4,105:4,-1:5,105:12,172,105:5,-1:12,106:2," +
"32,106,-1:5,106:2,85,106:10,-1:3,32,-1:4,106:4,-1:5,106:18,-1:12,36,-1:8,18" +
"6,-1,130,180,174,-1,36,189,-1,192,-1:2,195,-1:9,37,247,-1:7,195,-1:2,174,-1" +
",180,186,-1,192,-1:2,189,-1,247,37,-1:15,39,-1:8,39,41,-1,42,-1:2,232,43,23" +
"0,91,44,-1:20,44,-1,42,-1:3,91,230,43,-1,232,-1:17,38,-1:10,37,-1:3,38,-1:1" +
"5,37,-1:22,37,-1:14,105:2,110,105,-1:5,105:2,85,105:10,-1:3,110,-1:4,105:4," +
"-1:5,105:18,-1:12,106:4,-1:5,128,106,107,106:10,-1:3,106,-1:4,106:4,-1:5,10" +
"6:7,128,106:10,-1:12,105:4,-1:5,105:2,180,199,105:9,-1:3,105,-1:4,105:4,-1:" +
"5,105:6,199,105:11,-1:23,115,-1:5,43,-1,115,-1:27,115,-1,43,-1:30,201,-1:9," +
"204,-1:10,247,-1:7,204,-1:12,247,-1:15,105:4,-1:5,105:2,174,105,184,105:8,-" +
"1:3,105,-1:4,105:4,-1:5,105:4,184,105:13,-1:12,106:4,-1:5,106:2,247,106:10," +
"-1:3,106,-1:4,106:2,260,106,-1:5,106:14,260,106:3,-1:12,36,-1:10,143,-1:3,3" +
"6,-1:2,192,-1:29,192,-1:20,105:4,-1:5,105:2,177,105:10,-1:3,105,-1:4,105:4," +
"-1:5,187,105:12,187,105:4,-1:12,106:4,-1:5,106:2,37,106:10,-1:3,106,-1:4,10" +
"6,112,106:2,-1:5,106:15,112,106:2,-1:23,207,-1:4,210,-1:4,258,-1:18,258,-1:" +
"10,210,-1:17,105:4,-1:5,105:2,37,105:10,-1:3,105,-1:4,105,87,105:2,-1:5,105" +
":15,87,105:2,-1:12,111,106:3,-1:5,106:2,36,106:3,111,106:6,-1:3,106,-1:4,10" +
"6:4,-1:5,106:18,-1:23,189,-1:4,189,-1:34,189,-1:17,86,105:3,-1:5,105:2,36,1" +
"05:3,86,105:6,-1:3,105,-1:4,105:4,-1:5,105:18,-1:12,106:4,-1:5,106:2,204,10" +
"6:9,179,-1:3,106,-1:4,106:4,-1:5,106,179,106:16,-1:23,174,-1,174,-1:29,174," +
"-1:25,105:4,-1:5,105:2,245,105:7,259,105:2,-1:3,105,-1:4,105:4,-1:5,105:8,2" +
"59,105:9,-1:12,106:4,-1:5,106:2,210,106:4,182,106:5,-1:3,106,-1:4,106:4,-1:" +
"5,106:12,182,106:5,-1:23,216,-1:6,216,-1:29,216,-1:20,105:4,-1:5,105:2,258," +
"105:9,264,-1:3,105,-1:4,105:4,-1:5,105,264,105:16,-1:12,106:4,-1:5,106:2,17" +
"4,106,185,106:8,-1:3,106,-1:4,106:4,-1:5,106:4,185,106:13,-1:12,38,-1:10,88" +
",-1:3,38,-1:2,219,-1:29,219,-1:20,105:4,-1:5,105:2,189,105:4,190,105:5,-1:3" +
",105,-1:4,105:4,-1:5,105:12,190,105:5,-1:12,106:4,-1:5,106:2,216,106:6,188," +
"106:3,-1:3,106,-1:4,106:4,-1:5,106:9,188,106:8,-1:21,186,-1,186,-1:34,186,-" +
"1:22,105:4,-1:5,105:2,204,105:9,193,-1:3,105,-1:4,105:4,-1:5,105,193,105:16" +
",-1:12,113,106:3,-1:5,106:2,38,106:3,113,106:6,-1:3,106,-1:4,106:4,-1:5,106" +
":18,-1:13,39,-1:8,39:2,-1,40,-1:29,40,-1:25,105:4,-1:5,105:2,247,105:10,-1:" +
"3,105,-1:4,105:2,261,105,-1:5,105:14,261,105:3,-1:12,106:4,-1:5,106:2,258,1" +
"06:9,194,-1:3,106,-1:4,106:4,-1:5,106,194,106:16,-1:13,39,-1:8,39:2,-1:57,1" +
"31,105:3,-1:5,105:2,38,105:3,131,105:6,-1:3,105,-1:4,105:4,-1:5,105:18,-1:1" +
"2,106:4,-1:5,200,106,186,106:10,-1:3,106,-1:4,106:4,-1:5,106:7,200,106:10,-" +
"1:23,40,-1,40,-1:29,40,-1:25,105:4,-1:5,105:2,219,105:6,202,105:3,-1:3,105," +
"-1:4,105:4,-1:5,105:9,202,105:8,-1:12,106:4,-1:5,106:2,44,106:8,135,106,-1:" +
"3,106,-1:4,106:4,-1:5,106:2,135,106:15,-1:23,226,-1,226,-1:29,226,-1:25,105" +
":4,-1:5,208,105,186,105:10,-1:3,105,-1:4,105:4,-1:5,105:7,208,105:10,-1:12," +
"106:4,-1:5,106:2,95,106,119,106:8,-1:3,106,-1:4,106:4,-1:5,106:4,119,106:13" +
",-1:23,228,-1,226,-1:4,230,-1:24,226,-1:4,230,-1:20,105,89,105:2,-1:5,105,8" +
"9,39,105:10,-1:3,105,-1:4,105:4,-1:5,105:18,-1:12,106,132,106:2,-1:5,106,13" +
"2,39,106:10,-1:3,106,-1:4,106:4,-1:5,106:18,-1:23,231,-1:7,231,-1:27,231,-1" +
":21,105:4,-1:5,105:2,40,105,90,105:8,-1:3,105,-1:4,105:4,-1:5,105:4,90,105:" +
"13,-1:12,106:4,-1:5,106:2,115,106:7,133,106:2,-1:3,106,-1:4,106:4,-1:5,106:" +
"8,133,106:9,-1:23,92,-1,92,-1:29,92,-1:25,105:4,-1:5,105:2,92,105,145,105:8" +
",-1:3,105,-1:4,105:4,-1:5,105:4,145,105:13,-1:12,106:4,-1:5,106:2,43,106:5," +
"93,106:4,-1:3,106,-1:4,106:4,-1:5,106:10,93,106:7,-1:12,105:4,-1:5,105:2,44" +
",105:8,146,105,-1:3,105,-1:4,105:4,-1:5,105:2,146,105:15,-1:12,106:4,-1:5,1" +
"06:2,232,106:4,209,106:5,-1:3,106,-1:4,106:4,-1:5,106:12,209,106:5,-1:23,94" +
",-1:4,232,-1:3,44,-1:20,44,-1:9,232,-1:17,105:4,-1:5,105:2,95,105,136,105:8" +
",-1:3,105,-1:4,105:4,-1:5,105:4,136,105:13,-1:12,106:4,-1:5,106:2,92,106,13" +
"4,106:8,-1:3,106,-1:4,106:4,-1:5,106:4,134,106:13,-1:13,39,-1:8,39,114,-1:7" +
",231,-1:27,231,-1:21,105:4,-1:5,105:2,226,105,217,105:8,-1:3,105,-1:4,105:4" +
",-1:5,105:4,217,105:13,-1:12,106:4,-1:5,106:2,231,106:7,212,106:2,-1:3,106," +
"-1:4,106:4,-1:5,106:8,212,106:9,-1:23,118,-1:6,230,-1,44,-1:20,44,-1:6,230," +
"-1:20,105:4,-1:5,105:2,43,105:5,117,105:4,-1:3,105,-1:4,105:4,-1:5,105:10,1" +
"17,105:7,-1:12,106:4,-1:5,106:2,254,106:2,252,106:7,-1:3,106,-1:4,106:4,-1:" +
"5,106:11,252,106:6,-1:23,44,-1:8,44,-1:20,44,-1:27,105:4,-1:5,105:2,115,105" +
":7,144,105:2,-1:3,105,-1:4,105:4,-1:5,105:8,144,105:9,-1:12,106:4,-1:5,215," +
"106,233,106:10,-1:3,106,-1:4,106:4,-1:5,106:7,215,106:10,-1:23,45,-1,95,-1:" +
"2,232,-1:26,95,-1:7,232,-1:17,105:4,-1:5,105:2,231,105:7,223,105:2,-1:3,105" +
",-1:4,105:4,-1:5,105:8,223,105:9,-1:12,106:4,-1:5,106:2,98,106:4,121,106:5," +
"-1:3,106,-1:4,106:4,-1:5,106:12,121,106:5,-1:23,95,-1,95,-1:29,95,-1:25,105" +
":4,-1:5,105:2,46,105,96,105:8,-1:3,105,-1:4,105:4,-1:5,105:4,96,105:13,-1:1" +
"2,106:4,-1:5,106:2,47,106,97,106:8,-1:3,106,-1:4,106:4,-1:5,106:4,97,106:13" +
",-1:23,95,-1,95,-1:6,44,-1:20,44,-1,95,-1:25,105:4,-1:5,105:2,98,105:4,137," +
"105:5,-1:3,105,-1:4,105:4,-1:5,105:12,137,105:5,-1:12,106:4,-1:5,106:2,49,1" +
"06:10,-1:3,106,-1:4,106:3,122,-1:5,106:3,122,106:14,-1:23,115,-1:7,115,-1:2" +
"7,115,-1:21,105:4,-1:5,105:2,254,105:2,253,105:7,-1:3,105,-1:4,105:4,-1:5,1" +
"05:11,253,105:6,-1:12,221,106:3,-1:5,106:2,236,106:3,221,106:6,-1:3,106,-1:" +
"4,106:4,-1:5,106:18,-1:12,105:4,-1:5,225,105,233,105:10,-1:3,105,-1:4,105:4" +
",-1:5,105:7,225,105:10,-1:12,106:4,-1:5,106:2,50,106:4,100,106:5,-1:3,106,-" +
"1:4,106:4,-1:5,106:12,100,106:5,-1:23,116,-1,92,-1:2,232,-1:26,92,-1:7,232," +
"-1:17,105:4,-1:5,105:2,47,105,120,105:8,-1:3,105,-1:4,105:4,-1:5,105:4,120," +
"105:13,-1:23,46,-1,46,-1:29,46,-1:25,105:4,-1:5,105:2,49,105:10,-1:3,105,-1" +
":4,105:3,138,-1:5,105:3,138,105:14,-1:12,229,105:3,-1:5,105:2,236,105:3,229" +
",105:6,-1:3,105,-1:4,105:4,-1:5,105:18,-1:21,233,-1,234,-1:2,254,-1:31,233," +
"-1:3,254,-1:18,105:4,-1:5,105:2,50,105:4,123,105:5,-1:3,105,-1:4,105:4,-1:5" +
",105:12,123,105:5,-1:23,49,-1:21,49,-1:8,49,-1:37,50,-1:4,50,-1:34,50,-1:7," +
"1,51,52,51:2,53,54,101,55,56,51:59,1,65,66,65:2,67,65,102,-1,65:60,1,69,70," +
"103,69:4,71,72,69:4,124,71:2,72,69:51,1,75:7,-1,75:60,1,76,77,76:5,-1,76:60" +
",-1:10,105:4,-1:5,105:2,104,105,154,105:4,157,105:3,-1:3,105,-1:4,105:4,-1:" +
"5,105:4,154,105:4,157,105:8,-1:12,106:4,-1:5,106:2,244,106:4,158,106:2,161," +
"106:2,-1:3,106,-1:4,106:4,-1:5,106:8,161,106:3,158,106:5,-1:23,213,-1:4,210" +
",-1:4,204,-1:18,204,-1:10,210,-1:28,224,-1:4,224,-1:34,224,-1:17,106:4,-1:5" +
",106:2,189,106:4,197,106:5,-1:3,106,-1:4,106:4,-1:5,106:12,197,106:5,-1:23," +
"230,-1:6,230,-1:29,230,-1:20,106:4,-1:5,106:2,226,106,203,106:8,-1:3,106,-1" +
":4,106:4,-1:5,106:4,203,106:13,-1:12,105:4,-1:5,105:2,210,105:4,196,105:5,-" +
"1:3,105,-1:4,105:4,-1:5,105:12,196,105:5,-1:12,106:4,-1:5,106:2,219,106:6,1" +
"91,106:3,-1:3,106,-1:4,106:4,-1:5,106:9,191,106:8,-1:12,105:4,-1:5,105:2,21" +
"6,105:6,205,105:3,-1:3,105,-1:4,105:4,-1:5,105:9,205,105:8,-1:12,106:4,-1:5" +
",218,106,235,106:10,-1:3,106,-1:4,106:4,-1:5,106:7,218,106:10,-1:12,105:4,-" +
"1:5,227,105,235,105:10,-1:3,105,-1:4,105:4,-1:5,105:7,227,105:10,-1:21,235," +
"-1,235,-1:34,235,-1:22,105:4,-1:5,105:2,257,105:7,163,105,166,-1:3,105,-1:4" +
",105:4,-1:5,105,166,105:6,163,105:9,-1:12,106:4,-1:5,106:2,159,164,106:9,-1" +
":3,106,-1:4,106:4,-1:5,106:6,164,106:11,-1:23,222,-1:4,189,-1:4,258,-1:18,2" +
"58,-1:10,189,-1:28,232,-1:4,232,-1:34,232,-1:17,105:4,-1:5,105:2,224,105:4," +
"211,105:5,-1:3,105,-1:4,105:4,-1:5,105:12,211,105:5,-1:12,106:4,-1:5,106:2," +
"230,106:6,206,106:3,-1:3,106,-1:4,106:4,-1:5,106:9,206,106:8,-1:12,105:4,-1" +
":5,105:2,230,105:6,220,105:3,-1:3,105,-1:4,105:4,-1:5,105:9,220,105:8,-1:12" +
",105:4,-1:5,105:2,244,105:4,169,105:2,249,105:2,-1:3,105,-1:4,105:4,-1:5,10" +
"5:8,249,105:3,169,105:5,-1:12,106:4,-1:5,106:2,162,106:6,167,106:3,-1:3,106" +
",-1:4,106:4,-1:5,106:9,167,106:8,-1:12,105:4,-1:5,105:2,232,105:4,214,105:5" +
",-1:3,105,-1:4,105:4,-1:5,105:12,214,105:5,-1:12,105:4,-1:5,105:2,165,105,1" +
"75,105:4,178,105:3,-1:3,105,-1:4,105:4,-1:5,105:4,175,105:4,178,105:8,-1:12" +
",106:4,-1:5,106:2,165,106,170,106:4,250,106:3,-1:3,106,-1:4,106:4,-1:5,106:" +
"4,170,106:4,250,106:8,-1:12,105:4,-1:5,105:2,162,105:6,251,105:3,-1:3,105,-" +
"1:4,105:4,-1:5,105:9,251,105:8,-1:12,106:4,-1:5,106:2,257,106:7,173,106,246" +
",-1:3,106,-1:4,106:4,-1:5,106,246,106:6,173,106:9,-1:12,105:4,-1:5,105:2,16" +
"8,181,105:9,-1:3,105,-1:4,105:4,-1:5,105:6,181,105:11,-1:12,106:4,-1:5,106:" +
"2,168,176,106:9,-1:3,106,-1:4,106:4,-1:5,106:6,176,106:11,-1:2");

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
						{ return new Symbol(TokenConstants.ERROR, yytext()); }
					case -3:
						break;
					case 3:
						{ curr_lineno++; }
					case -4:
						break;
					case 4:
						{ return new Symbol(TokenConstants.MULT); }
					case -5:
						break;
					case 5:
						{ return new Symbol(TokenConstants.RPAREN); }
					case -6:
						break;
					case 6:
						{	string_buf.append(yytext());
														yybegin(STR_LEX1); }
					case -7:
						break;
					case 7:
						{ return new Symbol(TokenConstants.ERROR, "\\000"); }
					case -8:
						break;
					case 8:
						{ }
					case -9:
						break;
					case 9:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
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
						{	yybegin(YYINITIAL);
														curr_lineno++;
														string_buf.setLength(0);
														return new Symbol(TokenConstants.ERROR, "Unterminated string constant");}
					case -53:
						break;
					case 53:
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
					case -54:
						break;
					case 54:
						{	yybegin(STR_NULL);
														return new Symbol(TokenConstants.ERROR, "String contains null character."); }
					case -55:
						break;
					case 55:
						{ string_buf.append("\015"); }
					case -56:
						break;
					case 56:
						{ string_buf.append("\033"); }
					case -57:
						break;
					case 57:
						{	string_buf.append(yytext().charAt(1)); }
					case -58:
						break;
					case 58:
						{ string_buf.append("\n");
													curr_lineno++; }
					case -59:
						break;
					case 59:
						{	string_buf.append("\""); }
					case -60:
						break;
					case 60:
						{	string_buf.append("\\"); }
					case -61:
						break;
					case 61:
						{	string_buf.append("\t"); }
					case -62:
						break;
					case 62:
						{	string_buf.append("\n"); }
					case -63:
						break;
					case 63:
						{	string_buf.append("\f"); }
					case -64:
						break;
					case 64:
						{	string_buf.append("\b"); }
					case -65:
						break;
					case 65:
						{ }
					case -66:
						break;
					case 66:
						{ yybegin(YYINITIAL);
														curr_lineno++; }
					case -67:
						break;
					case 67:
						{ yybegin(YYINITIAL); }
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
						{ curr_lineno++;}
					case -71:
						break;
					case 71:
						{ }
					case -72:
						break;
					case 72:
						{ }
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
						{ }
					case -77:
						break;
					case 77:
						{	curr_lineno++;
														yybegin(YYINITIAL); }
					case -78:
						break;
					case 79:
						{ return new Symbol(TokenConstants.ERROR, yytext()); }
					case -79:
						break;
					case 80:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -80:
						break;
					case 81:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -81:
						break;
					case 82:
						{ return new Symbol(TokenConstants.FI); }
					case -82:
						break;
					case 83:
						{ return new Symbol(TokenConstants.IN); }
					case -83:
						break;
					case 84:
						{ return new Symbol(TokenConstants.IF); }
					case -84:
						break;
					case 85:
						{ return new Symbol(TokenConstants.OF); }
					case -85:
						break;
					case 86:
						{ return new Symbol(TokenConstants.NOT); }
					case -86:
						break;
					case 87:
						{ return new Symbol(TokenConstants.NEW); }
					case -87:
						break;
					case 88:
						{ return new Symbol(TokenConstants.LET); }
					case -88:
						break;
					case 89:
						{ return new Symbol(TokenConstants.THEN); }
					case -89:
						break;
					case 90:
						{	return new Symbol(TokenConstants.BOOL_CONST, true); }
					case -90:
						break;
					case 91:
						{ return new Symbol(TokenConstants.POOL); }
					case -91:
						break;
					case 92:
						{ return new Symbol(TokenConstants.CASE); }
					case -92:
						break;
					case 93:
						{ return new Symbol(TokenConstants.LOOP); }
					case -93:
						break;
					case 94:
						{ return new Symbol(TokenConstants.ESAC); }
					case -94:
						break;
					case 95:
						{ return new Symbol(TokenConstants.ELSE); }
					case -95:
						break;
					case 96:
						{	return new Symbol(TokenConstants.BOOL_CONST, false); }
					case -96:
						break;
					case 97:
						{ return new Symbol(TokenConstants.WHILE); }
					case -97:
						break;
					case 98:
						{ return new Symbol(TokenConstants.CLASS); }
					case -98:
						break;
					case 99:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -99:
						break;
					case 100:
						{	return new Symbol(TokenConstants.INHERITS); }
					case -100:
						break;
					case 101:
						{ string_buf.append(yytext()); }
					case -101:
						break;
					case 102:
						{ }
					case -102:
						break;
					case 103:
						{	 }
					case -103:
						break;
					case 105:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -104:
						break;
					case 106:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -105:
						break;
					case 107:
						{ return new Symbol(TokenConstants.FI); }
					case -106:
						break;
					case 108:
						{ return new Symbol(TokenConstants.IN); }
					case -107:
						break;
					case 109:
						{ return new Symbol(TokenConstants.IF); }
					case -108:
						break;
					case 110:
						{ return new Symbol(TokenConstants.OF); }
					case -109:
						break;
					case 111:
						{ return new Symbol(TokenConstants.NOT); }
					case -110:
						break;
					case 112:
						{ return new Symbol(TokenConstants.NEW); }
					case -111:
						break;
					case 113:
						{ return new Symbol(TokenConstants.LET); }
					case -112:
						break;
					case 114:
						{ return new Symbol(TokenConstants.THEN); }
					case -113:
						break;
					case 115:
						{ return new Symbol(TokenConstants.POOL); }
					case -114:
						break;
					case 116:
						{ return new Symbol(TokenConstants.CASE); }
					case -115:
						break;
					case 117:
						{ return new Symbol(TokenConstants.LOOP); }
					case -116:
						break;
					case 118:
						{ return new Symbol(TokenConstants.ESAC); }
					case -117:
						break;
					case 119:
						{ return new Symbol(TokenConstants.ELSE); }
					case -118:
						break;
					case 120:
						{ return new Symbol(TokenConstants.WHILE); }
					case -119:
						break;
					case 121:
						{ return new Symbol(TokenConstants.CLASS); }
					case -120:
						break;
					case 122:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -121:
						break;
					case 123:
						{	return new Symbol(TokenConstants.INHERITS); }
					case -122:
						break;
					case 124:
						{	 }
					case -123:
						break;
					case 126:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -124:
						break;
					case 127:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -125:
						break;
					case 128:
						{ return new Symbol(TokenConstants.FI); }
					case -126:
						break;
					case 129:
						{ return new Symbol(TokenConstants.IN); }
					case -127:
						break;
					case 130:
						{ return new Symbol(TokenConstants.NOT); }
					case -128:
						break;
					case 131:
						{ return new Symbol(TokenConstants.LET); }
					case -129:
						break;
					case 132:
						{ return new Symbol(TokenConstants.THEN); }
					case -130:
						break;
					case 133:
						{ return new Symbol(TokenConstants.POOL); }
					case -131:
						break;
					case 134:
						{ return new Symbol(TokenConstants.CASE); }
					case -132:
						break;
					case 135:
						{ return new Symbol(TokenConstants.ESAC); }
					case -133:
						break;
					case 136:
						{ return new Symbol(TokenConstants.ELSE); }
					case -134:
						break;
					case 137:
						{ return new Symbol(TokenConstants.CLASS); }
					case -135:
						break;
					case 138:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -136:
						break;
					case 140:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -137:
						break;
					case 141:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -138:
						break;
					case 142:
						{ return new Symbol(TokenConstants.IN); }
					case -139:
						break;
					case 143:
						{ return new Symbol(TokenConstants.NOT); }
					case -140:
						break;
					case 144:
						{ return new Symbol(TokenConstants.POOL); }
					case -141:
						break;
					case 145:
						{ return new Symbol(TokenConstants.CASE); }
					case -142:
						break;
					case 146:
						{ return new Symbol(TokenConstants.ESAC); }
					case -143:
						break;
					case 148:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -144:
						break;
					case 149:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -145:
						break;
					case 151:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -146:
						break;
					case 152:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -147:
						break;
					case 154:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -148:
						break;
					case 155:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -149:
						break;
					case 157:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -150:
						break;
					case 158:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -151:
						break;
					case 160:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -152:
						break;
					case 161:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -153:
						break;
					case 163:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -154:
						break;
					case 164:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -155:
						break;
					case 166:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -156:
						break;
					case 167:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -157:
						break;
					case 169:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -158:
						break;
					case 170:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -159:
						break;
					case 172:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -160:
						break;
					case 173:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -161:
						break;
					case 175:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -162:
						break;
					case 176:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -163:
						break;
					case 178:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -164:
						break;
					case 179:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -165:
						break;
					case 181:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -166:
						break;
					case 182:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -167:
						break;
					case 184:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -168:
						break;
					case 185:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -169:
						break;
					case 187:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -170:
						break;
					case 188:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -171:
						break;
					case 190:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -172:
						break;
					case 191:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -173:
						break;
					case 193:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -174:
						break;
					case 194:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -175:
						break;
					case 196:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -176:
						break;
					case 197:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -177:
						break;
					case 199:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -178:
						break;
					case 200:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -179:
						break;
					case 202:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -180:
						break;
					case 203:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -181:
						break;
					case 205:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -182:
						break;
					case 206:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -183:
						break;
					case 208:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -184:
						break;
					case 209:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -185:
						break;
					case 211:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -186:
						break;
					case 212:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -187:
						break;
					case 214:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -188:
						break;
					case 215:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -189:
						break;
					case 217:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -190:
						break;
					case 218:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -191:
						break;
					case 220:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -192:
						break;
					case 221:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -193:
						break;
					case 223:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -194:
						break;
					case 225:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -195:
						break;
					case 227:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -196:
						break;
					case 229:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -197:
						break;
					case 242:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -198:
						break;
					case 243:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -199:
						break;
					case 246:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -200:
						break;
					case 248:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -201:
						break;
					case 249:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -202:
						break;
					case 250:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -203:
						break;
					case 251:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -204:
						break;
					case 252:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -205:
						break;
					case 253:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -206:
						break;
					case 255:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -207:
						break;
					case 256:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -208:
						break;
					case 259:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -209:
						break;
					case 260:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -210:
						break;
					case 261:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -211:
						break;
					case 262:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -212:
						break;
					case 263:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -213:
						break;
					case 264:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -214:
						break;
					case 265:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -215:
						break;
					case 266:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -216:
						break;
					case 267:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -217:
						break;
					case 268:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -218:
						break;
					case 269:
						{	return new Symbol(TokenConstants.OBJECTID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -219:
						break;
					case 270:
						{	return new Symbol(TokenConstants.TYPEID,
												new IdSymbol(yytext(),yytext().length(),count_id++)); }
					case -220:
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
