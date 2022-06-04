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

Bytecode je zásobníkově orientovaný, nepoužívá proměnné jako vyšší programovací jazyky, ale veškeré hodnoty ukládá do zásobníku pomocí push a pop. Což je z hlediska stroje efektivní, ale lidsky těžce čitelné. Hodnoty primitivní datových typu jsou napřímo 1, 3.14, nebo 'b', kdežto referenční hodnoty jsou aktuální reference do JVM haldy. Z tohoto důvodu Java přenáší parametry "by value" a nikoliv "by reference". Jména proměnných se tedy nahradí indexy, na ně je referencováno suffixem instrukcí.

Skupiny instrukcí:
* Načítání a ukládání (load, store)
* Aritmetické a logické (add, cmpg)
* Konverze typů (i2d, f2d)
* Tvorba objektů a manipulace (new, putfield)
* Management zásobníku operandů (swap, dup)
* Řížení programu (ifeq, goto)
* Invokace metod a návrat (invokespecial, areturn)
* Ostatní – vyhazování výjimek, synchronizace atp.

Prefixy/suffixy referují o typech operandů nad kterými pracují:
* i integer
* l long
* s short
* b byte
* c character
* f float
* d double
* a reference

Další možné "suffixy":
* Načtení a uložení reference **do lokální proměnné 0, 1, 2 a 3** – aload_0, aload_1, aload_2, aload_3, astore_0, astore_1, astore_2, astore_3 (stejně může být pro další typy dload_x apod.)
* **Zapsání hodnoty 0.0 nebo 1.0** na vrchol zásobníku – dconst_0, dconst_1

### How is bytecode generated and how can be viewed

Bytecode je generován kompilátorem Javy, který překládá .java kód na bytecode, který následně může být interpreterem (JVM) spuštěn na cílovém stroji. Přeložený Java kód na bytecode v souboru typu .class (případně .jar) může být prohlížen Java Class Disassemblerem skrze příkazovou řádku javap -c com.packageX.classY. Soubor pak lze editovat na úrovni bytekódu. Případně existují knihovny a online nástroje k zobrazení a úpravě .class souborů.

### Describe operand stack and local variables array

Frame se používá k ukládání dat a částečných výsledků, stejně tak jako k provádění dynamického propojení, návratových hodnot pro metody a odesílání výjimek. Frame se vytváří při každém vyvolání metody a je zničen po dokončení vyvolání jeho metody, ať už je dokončení normální nebo náhlé (nezachycená výjimka). Jeho součástí je pole lokálních proměnných, zásobník operandů a reference na "runtime constant pool" třídy, která obsahuje vykonávanou metodu.

Pole lokálních proměnných je adresováno indexací, první index je 0. Datové typy menší než 32 bitů jsou zvětšeny na požadovanou velikost a 64 bitové typy zabírají místo 2 proměnných. Jednotlivé proměnné mohou mít typy: boolean, byte, char, short, int, float, reference, returnAddress. Long a double zabírají indexy n a n+1 (v tomto případě nelze číst proměnnou na n+1, ale lze do ni zapsat, to pak zneplatní proměnnou na indexu n).

Zásobník operandů (OS) je zásobník, s kterým pracuje JVM při vykonávání programu, drží hodnoty, které se používají k různým instrukcím. Stejně tak se do něho zapisují výsledky operací a také slouží pro uchování hodnot pro následující operace. OS je organizován jako pole wordů (32 bit) a hodnoty se jak vkládají, tak vytahují z vrcholu zásobníku (LIFO).

### Describe how does bytecode interpretation works in runtime

Bytekód se řádek po řádku překládá do instrukcí pro daný stroj za běhu a dané CPU ho pak vykonává.

### What is JIT compilation, how does it work

Just-in-time kompilace je metoda pro zlepšení výkonu interpretovaných programů, která probíhá během běhu programu. Program může být za běhu zkompilován do nativního kódu (pro daný stroj), aby se zlepšil jeho výkon (díky přístupu k dynamickým informacím běhového prostředí). Je také známá jako dynamická kompilace.

VJednou z výhod dynamické kompilace je, že při spouštění aplikací Java nebo C# může běhové prostředí profilovat aplikaci během jejího spouštění. To umožňuje generovat více optimalizovaný kód. Pokud se chování aplikace za běhu změní, běhové prostředí může kód překompilovat.

Některé z nevýhod zahrnují zpoždění spouštění a režii kompilace za běhu. Aby se omezila režie, mnoho kompilátorů JIT kompiluje pouze často používané cesty kódu.

JIT je feature runtime interpretu, který místo toho, aby interpretoval bytekód pokaždé, když je volána metoda, kód přeloží do isntrukcí pro daný stroj a ten se poté volá místo bytekódu.

## Lecture 3

### Describe difference between namespace, module and service

Namespace jsou v Javě package. Package je pojmenovaná kolekce tříd a případně podbalíčků. Slouží ke seskupení souvisejících tříd a definování namespace pro třídy,
které obsahuje. Namespace zajišťuje unikátnost názvů pro objekty, třídy apod. uvnitř daného namespace.

Module je agregace na vyšší úrovni, nad balíčky. Module je unikátně pojmenovaná, znovupoužitelná skupina souvisejících balíčků a dalších
souborů (imgs, XMLs, apod.). "Module descriptor" specifikuje: název balíčků, jeho "dependencies" (jiné balíčky, moduly, které potřebuje ke
svému fungování), a explicitně specifikuje, jaké balíčky jsou dostupné ostatním modulům (balíčky jsou implicitně privátní). Dále specifikuje
jaké "services" – služby poskytuje a spotřebovává a kterým dalším modulům poskytuje "reflection" (možnost nastavit setAccessible pro
privátní metody, třídy balíčku). Moduly jsou součástí Javy od verze 9. Keywords používané při deklarování modulů jsou mimo jiné:
 exports, module, open, opens, provides, requires, uses, with, to, transition.

Service – služba, je definována množinou rozhraní a tříd. Služba obsahuje rozhraní, nebo abstraktní třídu, které definuje funkcionalitu
poskytovanou danou službou (její rozhraní). JEdna služba může mít několik implementací, ty se nazývají "service providers". Klient s 
jednotlivými implementacemi nepřichází do kontaktu, komunikuje pouze se službou přes její rozhraní. Tento klient se nazývá "service 
consumer". Toto dovoluje měnit implementace, aniž by to afektovalo rozhraní služby, nebo consumera.

### Describe Maven POM

POM je "Project Object Model". V Maven projektu je typicky v souboru pom.xml, tímto je reprezentován daný projekt. Obsahuje konfigurace,
verze, informace o buildu, dependency management, moduly, nastavení prostředí, a další informace o projektu (kdo, co, kde), ale i 
vlastní uživatelské pole s konfiguracemi. Základní pole, která musí obsahovat každý pom.xml jsou: groupId, artifactId, version.
Tato data slouží jako adresa a časové razítko projektu.

### What is Super POM

Super POM je výchozí POM Mavenu. Všechny POM dědí POM od rodiče, nebo použijí výchozí. Základní POM je Super POM a obsahuje výchozí 
zděděné hodnoty. Maven používá "effective POM" (konfigurace se skládá z obsahu Super POM a projektové konfigurace). Všechny modely
implicitně dědí z Super POM.

### Describe Maven build livecycle

V Mavenu jsou 3 předpřipravené "lifecycles" projektu. Jejich jednotlivé fáze (build phase) jsou provéděny sekvenčně.
* default – stará se o nasazení projektu, zkráceně:
    * validate – zkontroluje, zda je projekt správně a všechny informace jsou dostupné
    * compile – zkompiluje zdrojové soubory
    * test – otestuje zkompilované soubory v testovacím rozhraní
    * package – sloučí zkompilované soubory do distribuovatelncýh balíčků (např. JAR)
    * verify – provede integrační testy
    * install –  nainstaluje balíček od lokálního repozitáře pro použití v závislostech lokálně
    * deploy – dokončení, nakopírování finálního balíčku do vzdáleného repozitáře ke sdílení
* clean – čištění projektu
* site – vytvoření webové stránky projektu

### Describe Maven goals

I když je "build phase" zodpovědná za konkrétní krok v životním cyklu sestavení, způsob, jakým tyto povinnosti provádí, 
se může lišit. A to se provádí deklarováním "plugin goals" vázaných na tyto fáze sestavení. "Plugin goal" představuje konkrétní úkol
 (elementárnější než build phase), který přispívá k budování a řízení projektu. Může být vázán na 0 nebo více fází sestavení.
Cíl, který není vázán na žádnou fázi sestavení, lze provést mimo životní cyklus sestavení přímým vyvoláním. Pořadí provádění 
závisí na pořadí, ve kterém jsou vyvolány cíle a fáze sestavení.

### How are project dependencies managed by Maven

Maven zjišťuje, jaké moduly jsou potřeba z dat obsažených v pom.xml. Podle vypsaných modulů, balíčků v pom.xml zjistí, jaké balíčky potřebujou
tyto balíčky a jaké potřebují tyto atd. Tudíž je potřeba pro každý projekt specifikovat pouze přímé závislosti, zbytek je obstarán 
automaticky. Není žádná maximální hloubka stromu závislostí, problém nastává pouze u cyklických závislostí. Maven nabízí několik
 features:
* Dependency mediation – určí, jaká verze má být použita, pokud je potřeba balíček vícekrát (dle hloubky a pořadí, kde je potřeba ve stromě
závislostí)
* Dependency management – přímo specifikuje, jaká verze artefaktů má být použita, když jsou nalezeny tranzitivní závislosti
* Dependency scope – určuje v jakém stádiu buildu mají být různmé artefakty použity (compile – výchozí, provided, runtime, test,
system, import)
* Excluded dependencies
* Optional dependencies

## Lecture 4

### Describe dependency injection and inversion of control
### Describe Spring application container
### Describe annotations
#### @autowired
#### @component
#### @configuration
#### @qualifier
#### @ComponentScan

### What does bean represent in Spring
### Describe difference between Singleton and Prototype bean scope
### Describe bean lifecycle events

## Lecture 5

### Describe annotations
#### @springbootapplication
#### @EnableAutoConfiguration
#### @PropertySource
#### @Profile
#### @value
### Describe basic components of Logback logging system
### Describe 3 logging levels with examples when to use them
### Difference between SL4F and Logback
### Describe one of open source logging stack
#### ELK
#### Grafana + Loki
### Describe following application containerization terms
#### Image
#### Repository
#### Container

## Lecture 6

### Describe how does Spring JDBC interact with relational database
### Describe Spring transactions and their properties
