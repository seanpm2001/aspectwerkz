/* Generated By:JJTree&JavaCC: Do not edit this line. ExpressionParserConstants.java */
/**************************************************************************************
 * Copyright (c) Jonas Bon�r, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.expression.ast;

public interface ExpressionParserConstants {

  int EOF = 0;
  int AND = 3;
  int OR = 4;
  int NOT = 5;
  int POINTCUT_REFERENCE = 6;
  int EXECUTION = 7;
  int CALL = 8;
  int SET = 9;
  int GET = 10;
  int HANDLER = 11;
  int WITHIN = 12;
  int WITHIN_CODE = 13;
  int STATIC_INITIALIZATION = 14;
  int CFLOW = 15;
  int CFLOW_BELOW = 16;
  int ARGS = 17;
  int TARGET = 18;
  int THIS = 19;
  int IF = 20;
  int DOT = 21;
  int WILDCARD = 22;
  int ARRAY = 23;
  int EAGER_WILDCARD = 24;
  int CLASS_PRIVATE = 27;
  int CLASS_PROTECTED = 28;
  int CLASS_PUBLIC = 29;
  int CLASS_STATIC = 30;
  int CLASS_ABSTRACT = 31;
  int CLASS_FINAL = 32;
  int CLASS_NOT = 33;
  int CLASS_ATTRIBUTE = 34;
  int CLASS_PATTERN = 35;
  int CLASS_IDENTIFIER = 36;
  int CLASS_JAVA_NAME_LETTER = 37;
  int CLASS_POINTCUT_END = 38;
  int METHOD_PUBLIC = 41;
  int METHOD_PROTECTED = 42;
  int METHOD_PRIVATE = 43;
  int METHOD_STATIC = 44;
  int METHOD_ABSTRACT = 45;
  int METHOD_FINAL = 46;
  int METHOD_NATIVE = 47;
  int METHOD_SYNCHRONIZED = 48;
  int METHOD_NOT = 49;
  int METHOD_ANNOTATION = 50;
  int METHOD_IDENTIFIER = 51;
  int METHOD_CLASS_PATTERN = 52;
  int METHOD_ARRAY_CLASS_PATTERN = 53;
  int COMMA = 54;
  int PARAMETER_START = 55;
  int PARAMETER_END = 56;
  int METHOD_JAVA_NAME_LETTER = 57;
  int FIELD_PRIVATE = 60;
  int FIELD_PROTECTED = 61;
  int FIELD_PUBLIC = 62;
  int FIELD_STATIC = 63;
  int FIELD_ABSTRACT = 64;
  int FIELD_FINAL = 65;
  int FIELD_TRANSIENT = 66;
  int FIELD_NOT = 67;
  int FIELD_ANNOTATION = 68;
  int FIELD_IDENTIFIER = 69;
  int FIELD_CLASS_PATTERN = 70;
  int FIELD_ARRAY_CLASS_PATTERN = 71;
  int FIELD_JAVA_NAME_LETTER = 72;
  int FIELD_POINTCUT_END = 73;
  int PARAMETER_IDENTIFIER = 76;
  int PARAMETER_CLASS_PATTERN = 77;
  int PARAMETER_ARRAY_CLASS_PATTERN = 78;
  int PARAMETER_ANNOTATION = 79;
  int PARAMETER_JAVA_NAME_LETTER = 80;
  int PARAMETER_NOT = 81;

  int DEFAULT = 0;
  int CLASS = 1;
  int METHOD = 2;
  int FIELD = 3;
  int PARAMETERS = 4;

  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "<AND>",
    "<OR>",
    "<NOT>",
    "<POINTCUT_REFERENCE>",
    "\"execution(\"",
    "\"call(\"",
    "\"set(\"",
    "\"get(\"",
    "\"handler(\"",
    "\"within(\"",
    "\"withincode(\"",
    "\"staticinitialization(\"",
    "\"cflow(\"",
    "\"cflowbelow(\"",
    "\"args(\"",
    "\"target(\"",
    "\"this(\"",
    "\"if(\"",
    "\".\"",
    "\"*\"",
    "\"[]\"",
    "<EAGER_WILDCARD>",
    "\" \"",
    "\"\\t\"",
    "\"private\"",
    "\"protected\"",
    "\"public\"",
    "\"static\"",
    "\"abstract\"",
    "\"final\"",
    "<CLASS_NOT>",
    "<CLASS_ATTRIBUTE>",
    "<CLASS_PATTERN>",
    "<CLASS_IDENTIFIER>",
    "<CLASS_JAVA_NAME_LETTER>",
    "\")\"",
    "\" \"",
    "\"\\t\"",
    "\"public\"",
    "\"protected\"",
    "\"private\"",
    "\"static\"",
    "\"abstract\"",
    "\"final\"",
    "\"native\"",
    "\"synchronized\"",
    "<METHOD_NOT>",
    "<METHOD_ANNOTATION>",
    "<METHOD_IDENTIFIER>",
    "<METHOD_CLASS_PATTERN>",
    "<METHOD_ARRAY_CLASS_PATTERN>",
    "\",\"",
    "\"(\"",
    "\")\"",
    "<METHOD_JAVA_NAME_LETTER>",
    "\" \"",
    "\"\\t\"",
    "\"private\"",
    "\"protected\"",
    "\"public\"",
    "\"static\"",
    "\"abstract\"",
    "\"final\"",
    "\"transient\"",
    "<FIELD_NOT>",
    "<FIELD_ANNOTATION>",
    "<FIELD_IDENTIFIER>",
    "<FIELD_CLASS_PATTERN>",
    "<FIELD_ARRAY_CLASS_PATTERN>",
    "<FIELD_JAVA_NAME_LETTER>",
    "\")\"",
    "\" \"",
    "\"\\t\"",
    "<PARAMETER_IDENTIFIER>",
    "<PARAMETER_CLASS_PATTERN>",
    "<PARAMETER_ARRAY_CLASS_PATTERN>",
    "<PARAMETER_ANNOTATION>",
    "<PARAMETER_JAVA_NAME_LETTER>",
    "<PARAMETER_NOT>",
    "\"(\"",
    "\")\"",
  };

}
