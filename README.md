# Exam questions

## Lecture 1

### Describe 5 new features in Java since version 7

Tři nové funkce viz níže.

Novinkou od Javy 8 jsou Lambda výrazy, což jsou krátké kousky kódu, které berou parametry a vrací hodnotu. Jsou podobné metodám, ale
nepotřebují název a mohou být naimplementovány přímo v těle metody. Jsou limitovány tím, že nemohou obsahovat proměnné a musí okamžitě
vracet hodnotu. Pokud je třeba, aby lambda výraz vracel hodnotu je možné použít blok kódu za výrazem ve složených závorkách,
který v sobě bude mít "return".

Příklady:
```
parameter -> expression
(parameter1, parameter2) -> expression
(parameter1, parameter2) -> { code block }
numbers.forEach( (n) -> { System.out.println(n); } );
```

Novinka Javy 18 je JEP 400: UTF-8 by default. Plno API (včetně standardních Java API) používalo výchozí znakovou sadu, ale ta
se lišila dle systému i napčíklad dle přihlášeného uživatele, to mohlo zapříčinit poškození ukládaných souborů. Nyní je tedy pro
všechny platformy výchozí kódová sada UTF-8. Přičemž stále je možné si manuálně změnit výchozí znakovou sadu.

### What is local type inference (Java 10)

Jedná se o přidání nového klíčového slova (spíše "rezervovaný název typu"?) "var", které se dá použít při inicializaci proměnné.
Namísto specifikace typu proměnné, Java rozhodne a určí datový typ proměnné dle hodnoty/objektu,
který má být do proměnné uložen. Lze použít i s klíčovým slovem "final". Funguje pro většinu případů
a šetří programátorovi čas. Samozřejmě nelze var použít pro pouhou deklaraci proměnné, ani pro
inicializaci "null", jednoduše musí být jednoznačné, jakého typ má proměnná nabývat. Také nelze použít
pro specifikaci návratového typu funkce, pro lambda výrazy – nejednoznačná volání a přiřazení.
Také nefunguje pro specifikování proměnné na úrovni třídy.

Příklady:
```
var x = 10;
var info = new ArrayList<String>();
```

### Describe Java records in (Java 15)

Jedná se o třídy, které fungují jako transparentní nosiče pro neměnné data. "Records" lze považovat za nominální tuply.
V normálním případě pro vytvoření objektu a jeho používání bychom musel vytvořit třídu a v ní konstruktor, gettery a settery.
Nyní místo toho lze vytvořit record a atributy vložit přímo do závorek za jméno recordu – parametrů. Tyto parametry lze pak
využívat jako klasické gettery a settery. Record má také automaticky naimplementovanou metodu toString(). Při vytváření recordu
můžeme vytvořit "konstruktor" bez závorek, kde například může být předdefinována nějaká výchozí hodnota pro vlastnost atp.
Na record lze například použít getClass().isRecord(), či getClass().getRecordComponents(), přes které lze iterovat.

Příklad:
```
public record Timestamp(string id, DateTime dt, string from)
```

### Describe Java sealed classes introduced (Java 17)

"Sealed classes" omezují, jaké jiné třídy (včetně abstraktních) a rozhraní je můžou rozšířit nebo implementovat ("extend"/"implement").
Dříve se muselo převážně řešit tříděním do balíčků a používáním modifikátoru viditelnosti pro konstruktory tříd. Třídy, které
rozšiřují/implementují sealed třídu nebo rozhraní, mohou být buď také "sealed", nebo "non-sealed", nebo "final" (případně final 
efektivně skrze použití recordu nebo enumerátoru). Non-sealed znamená, že může být volně rozšiřována/implementována.

Příklady:
```
public sealed class Shape permits Square, Circle, OtherShape {}
non-sealed class OtherShape extends Shape {}
```

### Difference between Java and JVM

JVM je prostředí pro spouštění bytekódu – virtuální stroj. Konvertuje bytekód na strojový kód. Je součástí JRE – JVM nemůže
být stažen a nainstalován samostatně. JVM je nezávislý na platformě. Obsahuje JIT kompiler pro překlad bytekódu na strojový kód,
výhodné pro zrychlení aplikace – často spouštěného kódu.

Kdežto Java je název pro OOP jazyk. Java soubory se pak kompilují do souborů .class (případně zabalené v archivu .jar), které slouží
jako vstup pro JVM, který tyto soubory spouští.

## Lecture 2

