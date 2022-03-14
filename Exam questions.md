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

### Describe JVM heap and stack. Which variables are stored on heap and which on stack

JVM Heap je prostor využívaný k alokování paměti pro objekty a JRE třídy. Objekty jsou vytvářeny vždy v haldě a mají globální přístup – mohou být referencovány odkudkoliv z aplikace. Nad haldou pracují garbage collectory.

JVM Stack je paměť pro vykonávání vlákna. Zásobník je vždy v pořadí LIFO. Pokaždé, když je invokována metoda, je vytvořen blok v paměti, který drží primitivní hodnoty a reference na objekty v haldě. Po ukončení vykonávání metody se blok uvolní a stane se dostupným pro další metodu.

Paměť stacku je mnohem menší než haldy. Stack pouze obsahuje primitivní proměnné a reference na objekty v haldě. Paměť stacku je pouze pro danou metodu a nelze k ní přistupovat zvenčí. Heap má globální přístup. Nedostatek paměti haldy označuje výjimka: OutOfMemoryError a paměti zásobníku: StackOverFlowError. Přístup k paměti stacku je rychlejší než k paměti haldy.

### How does GC work

Pokud se zaplní halda, vykonávání programu se pozastaví (STW viz níže) a spustí se algoritmus garbage collectoru (některé GC pracují i souběžně s programem). Ten začne prohledávat haldu a hledá objekty, na které není referencováno. Začne u tzv. roots (viz níže). Každý objekt na který je referencováno, nebo je kořen je označován jako "live". Dále prochází všechny reference live objektů a objekty na které z nich je referencováno označí také jako live, rekurzivně opakuje, dokud nejsou další objekty k prohledávání. Live objekty ponechá v paměti a zbytek dealokuje. POté může data kompaktně skládat k sobě a další praktiky v závislosti na typu GC.

#### Stop the world

Znamená, že vykonávání programu je pozastaveno, dokud garbage collector nezpracuje všechny objekty haldy. Pro velké haldy může mít i stovky milisekund.

#### GC roots

Objekty přístupné mimo haldu.

Kořeny jsou:
* Třídy načtené systémovým class loaderem a třídy, které obsahují reference na statické proměnné
* Lokální zásobník a jeho proměnné, parametry, reference
* Všechna aktivní vlákna
* JNI (Java Native Interface) reference – objekty pro volání JNI, proměnné a parametry JNI metod, globální JNI reference
* Objekty k monitorování synchronizace
* Objekty označené tak, aby je GC nesbíral

### Describe G1 collector

Halda je rozdělena do několika regionů v závisloti na velikosti haldy (napčíklad pro 4 GB haldu je 2048 2MB regionů). Nové objekty jsou inicializovány do regionů "Eden", po počtu alokovaných eden regionů se provádí "young collection" a přeživší objekty se z eden regionů kompaktně kopírují do "Survivor" regionů. Toto probíhá stále dokola. Pokud nějaké objekty přežijí několik young collections jsou kompaktně nakopírovány do "Old" regionů. Po postupném plnění haldy regiony eden, survivors a old začne G1 souběžně označovat objekty v old regionech (program není přerušen). Eden, survivor a old regiony jsou poté shromažďovány v "mixed" kolekcích a "live" objekty jsou kompaktně kopírovány do survivor a old regionů. Když už nezbývají další old regiony vhodné k tomuto shromažďování, G1 se vrátí k vytváření young kolekcí.

G1 tedy přechází mezi těmito stavy: Young collections -> Young collection + concurrent mark -> Mixed collections -> zpět na YC

### Describe ZGC collector

Funguje jak na malé velikosti paměti tak velké. Z algoritmického hlediska je ZGC concurrent (běží současně s vláknem programu), je tracing (prochází objekty v hladě a zjišťuje, jaké jsou live) a compacting (přesouvá objekty po haldě), dále je single-generation, NUMA-aware, region-based, používá load barriers a colored pointers. Pauzy GC se nezvětšují s velikostí haldy, ale zvětšují se s velikostí rootu, který se zhruba zvětšuje s počtem vláken, které aplikace používá.

ZGC cyklus se skládá z 3 částí. Pause Mark Start (Cuncurrent mark/remap) – skenuje zásobníky vláken, z kterých dostaneme vstupní objekty pro začátek značkování, tento graf pak prochází. Pause Mark End (Cuncurrent prepare for relocation) – konec značkování (bod synchronizace), poté se začnou zpracovávat "soft" a "phantom" reference, odnačítat třídy (class loaders a classes) a nakonec se na základě značkování z 1. částí rozhodne, jaké části haldy se zhutní. Pause Relocate Start (Concurrent Relocate) – skenování zásobníků vláken, tentokrát se hledají odkazy ukazující na množinu k přesunutí (zhutnění) a poté už následuje samotné přesouvání objektů v haldě. Na začátku každě části tedy probíhají pauzi v maximální délce 10 milisekund.

Colored pointers – metadata od objektech jsou uložena v 64bitových pointerech s přiřazenou "barvou" dle pozice v paměti. Tato metadata říkají o objektech, zda byly označeny, případně přesunuty. S těmito pointery úzce souvisí Load barrier – kus kódu vkládaný JITem do stragických míst. Účelem je při načítání reference objektu z haldy zkontrolovat, jakou má barvu (pokud má špatnou, tak referenci "ošetřit" – označit, přesunout..., a tak vylepšit barvu a při příštím použití se tento krok může přeskočit).

### Compare G1 vs ZGC

G1 má cca o 5 % lepší propustnost, ale ZGC má průměrně více než 100x kratší pauzy a až 400x kratší maximální pozastavení. Zatímco ZGC je optimalizován pro nízkou latenci, G1 se dá ladit pro rovnováhu propustnosti a latence. G1 provádí pro malé i velké sběry odpadu STW eventy, kdežto ZGC dělá většinu operací paralelně.

### Describe bytecode (groups, prefix/suffix, operand types)

### How is bytecode generated and how can be viewed

### Describe operand stack and local variables array

### Describe how does bytecode interpretation works in runtime

### What is JIT compilation, how does it work
