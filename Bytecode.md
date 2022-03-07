# Bytecode

Bytecode je zásobníkově orientovaný, nepoužívá proměnné jako vyšší programovací jazyky, ale veškeré hodnoty ukládá do zásobníku
pomocí push a pop. Což je z hlediska stroje efektivní, ale lidsky těžce čitelné. Hodnoty primitivní datových typu jsou
napřímo 1, 3.14, nebo 'b', kdežto referenční hodnoty jsou aktuální reference do JVM haldy. Z tohoto důvodu Java přenáší parametry
"by value" a nikoliv "by reference". Jména proměnných se tedy nahradí indexy, na ně je referencováno suffixem instrukcí.

[Instrukce](https://en.wikipedia.org/wiki/List_of_Java_bytecode_instructions)

---

Základní paměť a pseudokód

| Memory address | Value |
|----------------|-------|
| AA10           | 0     |
| AA11           | 0     |
| AA12           | 0     |

<code>
main():
  x = 10 //AA10
  y = power2(x) //AA12

power2(x):
  x = x * x //AA11
</code>

## Pass by Value

Jako parametr metody je předávána kopie hodnoty v nové proměnné.

| Memory address | Value |
|----------------|-------|
| AA10           | 10    |
| AA11           | 100   |
| AA12           | 100   |

## Pass by Reference

Jako pramater metody je předáván alias, nebo reference.

| Memory address | Value |
|----------------|-------|
| AA10           | 100   |
| AA11           | AA10  |
| AA12           | 100   |
