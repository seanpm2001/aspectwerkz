/* Generated By:JJTree&JavaCC: Do not edit this line. AnnotationParser.java */
/**************************************************************************************
 * Copyright (c) Jonas Bon�r, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.annotation.expression.ast;

import java.lang.reflect.Modifier;
import java.io.Reader;
import java.io.StringReader;

/**
 * The annotation parser.
 *
 * @author <a href="mailto:jboner@codehaus.org">Jonas Bon�r</a>
 */
public class AnnotationParser/*@bgen(jjtree)*/implements AnnotationParserTreeConstants, AnnotationParserConstants {/*@bgen(jjtree)*/
  protected static JJTAnnotationParserState jjtree = new JJTAnnotationParserState();
    public ASTRoot parse(String annotation) throws ParseException {
        return parse(new StringReader(annotation));
    }

    public ASTRoot parse(Reader reader) throws ParseException {
        ReInit(reader);
        return Root();
    }

/**
 * Entry point.
 */
  static final public ASTRoot Root() throws ParseException {
                        /*@bgen(jjtree) Root */
  ASTRoot jjtn000 = new ASTRoot(JJTROOT);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      Annotation();
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 0:
        jj_consume_token(0);
        break;
      case 26:
        jj_consume_token(26);
        break;
      case NEWLINE:
        jj_consume_token(NEWLINE);
        break;
      default:
        jj_la1[0] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      jjtree.closeNodeScope(jjtn000, true);
      jjtc000 = false;
      {if (true) return jjtn000;}
    } catch (Throwable jjte000) {
      if (jjtc000) {
        jjtree.clearNodeScope(jjtn000);
        jjtc000 = false;
      } else {
        jjtree.popNode();
      }
      if (jjte000 instanceof RuntimeException) {
        {if (true) throw (RuntimeException)jjte000;}
      }
      if (jjte000 instanceof ParseException) {
        {if (true) throw (ParseException)jjte000;}
      }
      {if (true) throw (Error)jjte000;}
    } finally {
      if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
    throw new Error("Missing return statement in function");
  }

/**
 * Annotation.
 */
  static final public void Annotation() throws ParseException {
                                 /*@bgen(jjtree) Annotation */
  ASTAnnotation jjtn000 = new ASTAnnotation(JJTANNOTATION);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      if (jj_2_2(3)) {
        jj_consume_token(ANNOTATION);
        jj_consume_token(LEFT_PARENTHEZIS);
        if (jj_2_1(4)) {
          KeyValuePairList();
        } else {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case INTEGER:
          case HEXNUMBER:
          case OCTNUMBER:
          case FLOAT:
          case BOOLEAN:
          case STRING:
          case CHAR:
          case LEFT_BRACKET:
          case JAVA_TYPE:
            Value();
            break;
          default:
            jj_la1[1] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
          }
        }
        jj_consume_token(RIGHT_PARENTHEZIS);
      } else if (jj_2_3(2)) {
        jj_consume_token(ANNOTATION);
        jj_consume_token(LEFT_PARENTHEZIS);
        jj_consume_token(RIGHT_PARENTHEZIS);
      } else {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case ANNOTATION:
          jj_consume_token(ANNOTATION);
          break;
        default:
          jj_la1[2] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
    } catch (Throwable jjte000) {
      if (jjtc000) {
        jjtree.clearNodeScope(jjtn000);
        jjtc000 = false;
      } else {
        jjtree.popNode();
      }
      if (jjte000 instanceof RuntimeException) {
        {if (true) throw (RuntimeException)jjte000;}
      }
      if (jjte000 instanceof ParseException) {
        {if (true) throw (ParseException)jjte000;}
      }
      {if (true) throw (Error)jjte000;}
    } finally {
      if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

/**
 * KeyValuePairList.
 */
  static final public void KeyValuePairList() throws ParseException {
    KeyValuePair();
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
      case JAVA_TYPE:
        ;
        break;
      default:
        jj_la1[3] = jj_gen;
        break label_1;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        jj_consume_token(COMMA);
        break;
      default:
        jj_la1[4] = jj_gen;
        ;
      }
      KeyValuePair();
    }
  }

/**
 * KeyValuePair.
 */
  static final public void KeyValuePair() throws ParseException {
 /*@bgen(jjtree) KeyValuePair */
    ASTKeyValuePair jjtn000 = new ASTKeyValuePair(JJTKEYVALUEPAIR);
    boolean jjtc000 = true;
    jjtree.openNodeScope(jjtn000);Token key, value;
    try {
      key = jj_consume_token(JAVA_TYPE);
        jjtn000.setKey(key.image);
      jj_consume_token(EQUALS);
      Value();
    } catch (Throwable jjte000) {
      if (jjtc000) {
        jjtree.clearNodeScope(jjtn000);
        jjtc000 = false;
      } else {
        jjtree.popNode();
      }
      if (jjte000 instanceof RuntimeException) {
        {if (true) throw (RuntimeException)jjte000;}
      }
      if (jjte000 instanceof ParseException) {
        {if (true) throw (ParseException)jjte000;}
      }
      {if (true) throw (Error)jjte000;}
    } finally {
      if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

/**
 * Value.
 *
 * @TODO: nested annotations
 */
  static final public void Value() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case CHAR:
      Char();
      break;
    case STRING:
      String();
      break;
    case LEFT_BRACKET:
      Array();
      break;
    case JAVA_TYPE:
      Identifier();
      break;
    case BOOLEAN:
      Boolean();
      break;
    case INTEGER:
      Integer();
      break;
    case FLOAT:
      Float();
      break;
    case HEXNUMBER:
      Hex();
      break;
    case OCTNUMBER:
      Oct();
      break;
    default:
      jj_la1[5] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

/**
 * Identifier.
 */
  static final public void Identifier() throws ParseException {
 /*@bgen(jjtree) Identifier */
    ASTIdentifier jjtn000 = new ASTIdentifier(JJTIDENTIFIER);
    boolean jjtc000 = true;
    jjtree.openNodeScope(jjtn000);Token value;
    try {
      value = jj_consume_token(JAVA_TYPE);
      jjtree.closeNodeScope(jjtn000, true);
      jjtc000 = false;
        jjtn000.setValue(value.image);
    } finally {
      if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

/**
 * Boolean.
 */
  static final public void Boolean() throws ParseException {
 /*@bgen(jjtree) Boolean */
    ASTBoolean jjtn000 = new ASTBoolean(JJTBOOLEAN);
    boolean jjtc000 = true;
    jjtree.openNodeScope(jjtn000);Token value;
    try {
      value = jj_consume_token(BOOLEAN);
      jjtree.closeNodeScope(jjtn000, true);
      jjtc000 = false;
        jjtn000.setValue(value.image);
    } finally {
      if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

/**
 * Char.
 */
  static final public void Char() throws ParseException {
 /*@bgen(jjtree) Char */
    ASTChar jjtn000 = new ASTChar(JJTCHAR);
    boolean jjtc000 = true;
    jjtree.openNodeScope(jjtn000);Token value;
    try {
      value = jj_consume_token(CHAR);
      jjtree.closeNodeScope(jjtn000, true);
      jjtc000 = false;
        jjtn000.setValue(value.image);
    } finally {
      if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

/**
 * String.
 */
  static final public void String() throws ParseException {
 /*@bgen(jjtree) String */
    ASTString jjtn000 = new ASTString(JJTSTRING);
    boolean jjtc000 = true;
    jjtree.openNodeScope(jjtn000);Token value;
    try {
      value = jj_consume_token(STRING);
      jjtree.closeNodeScope(jjtn000, true);
      jjtc000 = false;
        jjtn000.setValue(value.image);
    } finally {
      if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

/**
 * Array.
 */
  static final public void Array() throws ParseException {
                       /*@bgen(jjtree) Array */
  ASTArray jjtn000 = new ASTArray(JJTARRAY);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(LEFT_BRACKET);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case INTEGER:
      case HEXNUMBER:
      case OCTNUMBER:
      case FLOAT:
      case BOOLEAN:
      case STRING:
      case CHAR:
      case LEFT_BRACKET:
      case JAVA_TYPE:
        Value();
        label_2:
        while (true) {
          if (jj_2_4(2)) {
            ;
          } else {
            break label_2;
          }
          jj_consume_token(COMMA);
          Value();
        }
        break;
      default:
        jj_la1[6] = jj_gen;
        ;
      }
      jj_consume_token(RIGHT_BRACKET);
    } catch (Throwable jjte000) {
      if (jjtc000) {
        jjtree.clearNodeScope(jjtn000);
        jjtc000 = false;
      } else {
        jjtree.popNode();
      }
      if (jjte000 instanceof RuntimeException) {
        {if (true) throw (RuntimeException)jjte000;}
      }
      if (jjte000 instanceof ParseException) {
        {if (true) throw (ParseException)jjte000;}
      }
      {if (true) throw (Error)jjte000;}
    } finally {
      if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

/**
 * Integer.
 */
  static final public void Integer() throws ParseException {
 /*@bgen(jjtree) Integer */
    ASTInteger jjtn000 = new ASTInteger(JJTINTEGER);
    boolean jjtc000 = true;
    jjtree.openNodeScope(jjtn000);Token value;
    try {
      value = jj_consume_token(INTEGER);
      jjtree.closeNodeScope(jjtn000, true);
      jjtc000 = false;
        jjtn000.setValue(value.image);
    } finally {
      if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

/**
 * Float.
 */
  static final public void Float() throws ParseException {
 /*@bgen(jjtree) Float */
    ASTFloat jjtn000 = new ASTFloat(JJTFLOAT);
    boolean jjtc000 = true;
    jjtree.openNodeScope(jjtn000);Token value;
    try {
      value = jj_consume_token(FLOAT);
      jjtree.closeNodeScope(jjtn000, true);
      jjtc000 = false;
        jjtn000.setValue(value.image);
    } finally {
      if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

/**
 * Hex.
 */
  static final public void Hex() throws ParseException {
 /*@bgen(jjtree) Hex */
    ASTHex jjtn000 = new ASTHex(JJTHEX);
    boolean jjtc000 = true;
    jjtree.openNodeScope(jjtn000);Token value;
    try {
      value = jj_consume_token(HEXNUMBER);
      jjtree.closeNodeScope(jjtn000, true);
      jjtc000 = false;
        jjtn000.setValue(value.image);
    } finally {
      if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

/**
 * Oct.
 */
  static final public void Oct() throws ParseException {
 /*@bgen(jjtree) Oct */
    ASTOct jjtn000 = new ASTOct(JJTOCT);
    boolean jjtc000 = true;
    jjtree.openNodeScope(jjtn000);Token value;
    try {
      value = jj_consume_token(OCTNUMBER);
      jjtree.closeNodeScope(jjtn000, true);
      jjtc000 = false;
        jjtn000.setValue(value.image);
    } finally {
      if (jjtc000) {
        jjtree.closeNodeScope(jjtn000, true);
      }
    }
  }

  static final private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  static final private boolean jj_2_2(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_2(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(1, xla); }
  }

  static final private boolean jj_2_3(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_3(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(2, xla); }
  }

  static final private boolean jj_2_4(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_4(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(3, xla); }
  }

  static final private boolean jj_3R_14() {
    if (jj_3R_23()) return true;
    return false;
  }

  static final private boolean jj_3_3() {
    if (jj_scan_token(ANNOTATION)) return true;
    if (jj_scan_token(LEFT_PARENTHEZIS)) return true;
    return false;
  }

  static final private boolean jj_3R_13() {
    if (jj_3R_22()) return true;
    return false;
  }

  static final private boolean jj_3R_12() {
    if (jj_3R_21()) return true;
    return false;
  }

  static final private boolean jj_3R_11() {
    if (jj_3R_20()) return true;
    return false;
  }

  static final private boolean jj_3_2() {
    if (jj_scan_token(ANNOTATION)) return true;
    if (jj_scan_token(LEFT_PARENTHEZIS)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_1()) {
    jj_scanpos = xsp;
    if (jj_3R_4()) return true;
    }
    return false;
  }

  static final private boolean jj_3R_10() {
    if (jj_3R_19()) return true;
    return false;
  }

  static final private boolean jj_3R_23() {
    if (jj_scan_token(FLOAT)) return true;
    return false;
  }

  static final private boolean jj_3R_9() {
    if (jj_3R_18()) return true;
    return false;
  }

  static final private boolean jj_3_4() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_5()) return true;
    return false;
  }

  static final private boolean jj_3R_5() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_8()) {
    jj_scanpos = xsp;
    if (jj_3R_9()) {
    jj_scanpos = xsp;
    if (jj_3R_10()) {
    jj_scanpos = xsp;
    if (jj_3R_11()) {
    jj_scanpos = xsp;
    if (jj_3R_12()) {
    jj_scanpos = xsp;
    if (jj_3R_13()) {
    jj_scanpos = xsp;
    if (jj_3R_14()) {
    jj_scanpos = xsp;
    if (jj_3R_15()) {
    jj_scanpos = xsp;
    if (jj_3R_16()) return true;
    }
    }
    }
    }
    }
    }
    }
    }
    return false;
  }

  static final private boolean jj_3R_8() {
    if (jj_3R_17()) return true;
    return false;
  }

  static final private boolean jj_3R_17() {
    if (jj_scan_token(CHAR)) return true;
    return false;
  }

  static final private boolean jj_3R_26() {
    if (jj_3R_5()) return true;
    return false;
  }

  static final private boolean jj_3_1() {
    if (jj_3R_3()) return true;
    return false;
  }

  static final private boolean jj_3R_22() {
    if (jj_scan_token(INTEGER)) return true;
    return false;
  }

  static final private boolean jj_3R_21() {
    if (jj_scan_token(BOOLEAN)) return true;
    return false;
  }

  static final private boolean jj_3R_25() {
    if (jj_scan_token(OCTNUMBER)) return true;
    return false;
  }

  static final private boolean jj_3R_6() {
    if (jj_scan_token(JAVA_TYPE)) return true;
    if (jj_scan_token(EQUALS)) return true;
    if (jj_3R_5()) return true;
    return false;
  }

  static final private boolean jj_3R_19() {
    if (jj_scan_token(LEFT_BRACKET)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_26()) jj_scanpos = xsp;
    if (jj_scan_token(RIGHT_BRACKET)) return true;
    return false;
  }

  static final private boolean jj_3R_4() {
    if (jj_3R_5()) return true;
    return false;
  }

  static final private boolean jj_3R_7() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(18)) jj_scanpos = xsp;
    if (jj_3R_6()) return true;
    return false;
  }

  static final private boolean jj_3R_20() {
    if (jj_scan_token(JAVA_TYPE)) return true;
    return false;
  }

  static final private boolean jj_3R_3() {
    if (jj_3R_6()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_7()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  static final private boolean jj_3R_24() {
    if (jj_scan_token(HEXNUMBER)) return true;
    return false;
  }

  static final private boolean jj_3R_18() {
    if (jj_scan_token(STRING)) return true;
    return false;
  }

  static final private boolean jj_3R_16() {
    if (jj_3R_25()) return true;
    return false;
  }

  static final private boolean jj_3R_15() {
    if (jj_3R_24()) return true;
    return false;
  }

  static private boolean jj_initialized_once = false;
  static public AnnotationParserTokenManager token_source;
  static SimpleCharStream jj_input_stream;
  static public Token token, jj_nt;
  static private int jj_ntk;
  static private Token jj_scanpos, jj_lastpos;
  static private int jj_la;
  static public boolean lookingAhead = false;
  static private boolean jj_semLA;
  static private int jj_gen;
  static final private int[] jj_la1 = new int[7];
  static private int[] jj_la1_0;
  static {
      jj_la1_0();
   }
   private static void jj_la1_0() {
      jj_la1_0 = new int[] {0x4002001,0x411cf0,0x200000,0x440000,0x40000,0x411cf0,0x411cf0,};
   }
  static final private JJCalls[] jj_2_rtns = new JJCalls[4];
  static private boolean jj_rescan = false;
  static private int jj_gc = 0;

  public AnnotationParser(java.io.InputStream stream) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  You must");
      System.out.println("       either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new AnnotationParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 7; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  static public void ReInit(java.io.InputStream stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 7; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public AnnotationParser(java.io.Reader stream) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  You must");
      System.out.println("       either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new AnnotationParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 7; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  static public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 7; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public AnnotationParser(AnnotationParserTokenManager tm) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  You must");
      System.out.println("       either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 7; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public void ReInit(AnnotationParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 7; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  static final private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  static private final class LookaheadSuccess extends java.lang.Error { }
  static final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  static final private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }

  static final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

  static final public Token getToken(int index) {
    Token t = lookingAhead ? jj_scanpos : token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  static final private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  static private java.util.Vector jj_expentries = new java.util.Vector();
  static private int[] jj_expentry;
  static private int jj_kind = -1;
  static private int[] jj_lasttokens = new int[100];
  static private int jj_endpos;

  static private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      boolean exists = false;
      for (java.util.Enumeration e = jj_expentries.elements(); e.hasMoreElements();) {
        int[] oldentry = (int[])(e.nextElement());
        if (oldentry.length == jj_expentry.length) {
          exists = true;
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              exists = false;
              break;
            }
          }
          if (exists) break;
        }
      }
      if (!exists) jj_expentries.addElement(jj_expentry);
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  static public ParseException generateParseException() {
    jj_expentries.removeAllElements();
    boolean[] la1tokens = new boolean[27];
    for (int i = 0; i < 27; i++) {
      la1tokens[i] = false;
    }
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 7; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 27; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.addElement(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = (int[])jj_expentries.elementAt(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  static final public void enable_tracing() {
  }

  static final public void disable_tracing() {
  }

  static final private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 4; i++) {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
            case 1: jj_3_2(); break;
            case 2: jj_3_3(); break;
            case 3: jj_3_4(); break;
          }
        }
        p = p.next;
      } while (p != null);
    }
    jj_rescan = false;
  }

  static final private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

}
