#!/bin/sh
JAVACUP=/usr/share/java/cup/java-cup-11a.jar
JFLEX=/usr/bin/jflex

if [ $# -ne 1 ]; then
  echo "Manjka argument: ImePrograma | compile | clean"
  exit 1
fi

if [ "$1" == "clean" ]; then
  rm -f src/compiler/lexanal/PascalLex.java
  rm -f src/compiler/synanal/PascalTok.java
  rm -f src/compiler/synanal/PascalSyn.java
  rm -rf bin/
  echo "Uspesno brisanje generiranih datotek"
  exit 0
fi

if [ "$1" == "compile" ]; then
  $JFLEX --nobak src/compiler/lexanal/pascal.jflex
  java -jar $JAVACUP -package synanal -parser PascalSyn -symbols PascalTok -nonterms -expect 1 src/compiler/synanal/pascal.cup
  mv PascalTok.java src/compiler/synanal/PascalTok.java
  mv PascalSyn.java src/compiler/synanal/PascalSyn.java

  if [ ! -d "bin" ]; then
    mkdir bin
  fi
  javac -d bin -cp src src/compiler/Main.java
  echo "Uspesno generiranje datotek"
  exit 0
fi

PROGNAME=`dirname $1`"/"`basename -s .pascal $1`
if [ ! -f "$PROGNAME"".pascal" ]; then
  echo "'$PROGNAME"".pascal'" "ne obstaja"
  exit 1
fi

export PASCALXSL=xsl
java -cp bin/java_cup/runtime:$JAVACUP:bin/compiler/.. compiler.Main $PROGNAME semanal > /dev/null
java -cp bin/java_cup/runtime:$JAVACUP:bin/compiler/.. compiler.Main $PROGNAME frames > /dev/null
java -cp bin/java_cup/runtime:$JAVACUP:bin/compiler/.. compiler.Main $PROGNAME imcode > /dev/null
java -cp bin/java_cup/runtime:$JAVACUP:bin/compiler/.. compiler.Main $PROGNAME compiler/Main.java 
