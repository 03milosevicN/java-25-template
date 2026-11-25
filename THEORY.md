# SADRZAJ
- [1 - Java I/O](#1---java-io)
- [2 - Tokovi bajtova](#2---tokovi-bajtova)
- [3 - Tokovi karaktera](#3---tokovi-karaktera)
- [4 - Baferovani tokovi karaktera ](#4---baferovani-tokovi-karaktera)
- [5 - Tokovi podataka ](#5---tokovi-podataka)
- [6 - Tokovi objekata ](#6---tokovi-objekata)
- [7 - U/I integracija ](#7---ui-integracija)
- [8 - NIO u Javi](#8---nio-u-javi)
- [9 - `Path` i `Files`](#9---path-i-files)
- [10 - Atributi datoteka i putovanje kroz direktorijume](#10---atributi-datoteka-i-putovanje-kroz-direktorijume)

# POGLAVLJE 1
---

# 1 - Java I/O

- Input: citanje informacija sa izvora na aplikaciju
- Output: pisanje informacija ka izvoru sa aplikacije

Npr:
Input:
- Citanje tekstualne datoteke ili korisnickog unosa
- Citanje podataka sa mreze ili binarnih podataka
Output:
- Pisanje teksta na tekstualnu datoteku
- Pisanje aplikacionih logova
- Slanje podataka preko mreze ili slanje binranih podataka

**Input**:
Imamo tekstualnu datoteku `example.txt`:
```txt
Java is an Object-oriented programming language.
```
`example.txt` -> `InputStream`/`Reader` -> Aplikacija

**Output**:
Pisemo tekst na `example.txt`:
```txt
Java is an Object-oriented programming language.
```
Aplikcija -> `OutputStream`/`Writer` -> `example.txt`

Za oba pravca koriste se *tokovi*. Tokovi predstavljaju kretanje podataka izmedju izvora i destinacije.



## 1.1 - Hijerarhija klasa
### 1.1.1 - Tokovi bajtova
Tokovi bajtova obradjuju sirove binarne podatke.

Glavne apstraktne klase su `InputStream` za citanje na apl-u. i `OutputStream` za pisanje sa apl-je.

Hijerarhija je sledeca:
```txt
InputStream
    |
    |-- FileInputStream
    |-- BufferedInputStream
    |-- DataInputStream
    |-- ObjectInputStream

OutputStream
    |
    |-- FileOutputStream
    |-- BufferedOutputStream
    |-- DataOutputStream
    |-- ObjectOutputStream
```

Tokovi bajtova se koriste za podatke kao sto su slike, audio, PDF-ovi, arhive, prenos binarnih datoteka, itd. - Kada *moramo ocuvati prvobitnu strukturu bajtova pri slanju*.

Npr:
```java
try (FileInputStream input = new FileInputStream("example.txt")) {
    int data;
    while ( (data = input.read()) != -1 ) {
        System.out.println(data);
    }
}
```
- `read()` -> cita bajt po bajt
- `-1` kraj toka
- uvek se koristi `try-with-resources` sablon zbog automatskog zatvaranja toka 
- za kompleksnije primere se bajtovi citaju u konstruisanom nizu.

### 1.1.2 - Tokovi karaktera
Tokovi karaktera obradjuju tekst.

Glavne apstraktne klase su `Reader` za citanje karaktera na apl-u. i `Writer` za pisanje karaktera sa apl-je.

Hijerarhija je sledeca:
```txt
Reader
    |
    |-- FileReader
    |-- BufferedReader
    |-- InputStreamReader

Writer
    |
    |-- FileWriter
    |-- BufferedWriter
    |-- OutputStreamWriter
```

Npr:
```java
try (BufferedReader reader = new FileReader("example.txt")) {
    String line;
    while ( (line = reader.readLine()) != null ) {
        System.out.println(line);
    }
}
```
- `readLine()` vraca celu liniju, ne samo jedan karakter

## 1.2 - Tokovi bajtova naspram tokova karaktera
| Karakteristika                  | Tokovi bajtova  | Tokovi karaktera   |
| --------------                  | --------------  | ----------------   |
| Jedinica                        | Bajt            | Karakter           |
| Glavna apstrakcija pri pisanju  | `InputStream`   | `Reader`           | 
| Glavna apstrakcija pri citanju  | `OutputStream`  | `Writer`           | 
| Koristi se za                   | Binarne podatke | Karakterne podatke |
| Sifrovanje                      | Nema konkretno  | U glavnom UTF-8    |

## 1.3 - Baferovanje
Recimo da citamo podatke sa datoteke obima 10MB. Mozemo citati svaki bajt individualno ili mozemo citati bajtove u vecim blokovima koje zatim obradjujemo, odnosno *baferujemo*:

Bez baferovanja: `input.txt` -> Mnogo manjih operacija citanja -> Apl.
Sa baferovanjem: `input.txt` -> Bafer (Privremena medjumemorija) -> Apl.

Bitne baferovane apstrakcije klasa:
- `BufferedInputStream`
- `BufferedOutputStream`
- `BufferedReader`
- `BufferedWriter`

Vazan sablon je primena projektnog obrasca Decorator za dodavanje funkcionalnosti prvobitnom toku. Npr:
```java
InputStream input = new BufferedInputStream(new FileInputStream("example.txt"));
```


# 2 - Tokovi bajtova
Npr: `Hello` u bajtovima
```bin
01001000
01100101
01101100
01101100
01101111
```

Tokovi bajtova strogo rade samo sa bajtovima. API-ji napisani da obradjuju bajtove su pogodni za:
- Tekstualne datoteke
- Slike
- Videe
- Audio
- Izvrsne datoteke
- Arhive
- Mrezne podatke

Npr: rad tokova bajtova
`image.png` -> `FileInputStream` -> Apl. -> `FileOutputStream` -> `image-copy png`

## 2.1 - InputStream
Predstavlja apstraktnu klasu za rad sa bajtovima.

Najcesce metode:
| Metoda   | Opis |
|----------|------|
| `read()`                 |  Cita jedan bajt                                 |
| `read(byte[])`           |  Cita bajtove u niz                              |
| `read(byte[], off, len)` |  Cita odredjeni region niza bajtova              |
| `available()`            |  Procenjuje citljivost bajtova bez blokiranja    |
| `skip(n)`                |  Preskace bajtove                                |
| `close`                  |  Zatvara tok                                     |

Npr: citanje bajta po bajt
```java
try (FileInputStream input = new FileInputStream("example.txt")) {
    int data;
    while ( (data = input.read()) != -1 ) {
        System.out.println(data);
    }
} //...
```
- Ako je sadrzaj datoteke npr. "ABC", izlaz ce biti
```bin
65
66
67
```
jer se te vrednosti poklapaju sa ASCII reprezentacijom.

Npr: citanje bajtova u niz
```java
try (FileInputStream input = new FileInputStream("example.txt")) {
    byte[] buffer = new byte[1024];
    int bytesRead;
    while ( (bytesRead = input.read(buffer)) != -1 ) {
        System.out.println("Bytes read: " + bytesRead);
    }
}
```
- niz `buffer` je niz bajtova velicine 1024b. Metoda `read(buffer)` puni taj bafer.


## 2.2 - OutputStream
Predstavlja apstraktnu klasu za pisanje bajtova.

Najcesce metode:
| Metoda   | Opis |
|----------|------|
| `write()`                 |  Pise jedan bajt                                 |
| `write(byte[])`           |  Pise niz bajtova                                |
| `write(byte[], off, len)` |  Pise odredjeni region niza bajtova              |
| `flush()`                 |  Salje baferovan ispis kroz tok                  |
| `close`                   |  Zatvara tok                                     |

Npr: pisanje bajtova
```java
try (FileOutputStream output = new FileOutputStream("example.txt")) {
    byte[] data = {65,66,67};
    output.write(data);
}
```

## 2.3 - Spajanje klasa InputStream i OutputStream
Npr: algoritam za kopiranje datoteka
```java
try (
    FileInputStream input = new FileInputStream(source);
    FileOutputStream output = new FileOutputStream(destination);
) {
    byte[] buffer = new byte[1024];
    
    int bytesRead;
    (while (bytesRead = input.read(buffer)) != -1 ) {
        // sprecavanje delimicnog popunjavanja bafera
        output.write(buffer, 0, bytesRead);
    }
}
```
- Otvaranje izvorne dat.
- Otvaranje destinacione dat.
- Alociranje bafera
- Citanje bajtova u bafer
- Pisanje procitanih bajtova iz bafera
- Ponavljanje dok ne dodje do -1
- Zatvaranje try-with-resources bloka i tokova

## 2.4 - Baferovanje u detalje
Postoji razlika izmedju upotrebe niza bajtova i baferovanog toka

Primer A: direktan tok 
```java
try (FileInputStream input = new FileInputStream("source.dat")) {
    byte[] buffer = new byte[1024];
    int bytesRead;
    while ( (bytesRead = input.read(buffer)) != -1 ) {
        // neka obrada bajtova
    }
}
```

Primer B: baferovan tok
```java
try (InputStream input = new BufferedInputStream(new FileInputStream("source.dat"))) {
    byte[] buffer = new byte[1024];
    int bytesRead;
    while ( (bytesRead = input.read(buffer)) != -1 ) {
        // neka obrada bajtova
    }
}
```
- primer A ima bafer na nivou aplikacije ali ga ne obmotavamo ulaznom toku
- primer B ima primenjeni bafer i smanjuje operacije citanja

- Baferovanje je dobro za performanse jer smanjuje skupe U/I operacije ali ne garantuje uvek brze izvrsvanje.

# 3 - Tokovi karaktera
Do sada se radilo sa tokovima bajtova koji sluze za obradjivanje sirovih bajtova.
Tokovi karaktera sluze za obradjivanje karaktera.

## 3.1 - Bajtovi naspram karaktera
Npr:
```
Hello
```
U UTF-8 formatu, niska se sastoji iz sledecih 5 bajtova:
```
H → 01001000
e → 01100101
l → 01101100
l → 01101100
o → 01101111
```

Npr:
```
Čaša
```
Karakteri č i š se ne mogu pretstaviti putem jednog bajta u UTF-8 formatu. Zahtevaju 2 bajta.

- *Tok karaktera cita i pise karaktere i po potrebi ih sifruje i desifruje.* Tok bajtova samo cita i pise bajtove.

**Tok bajtova**: Tekstualna datoteka <-> `InputStream/OutputStream`
**Tok karaketra**: Tesktualna datoteka <-> Sifrovanje/Desifrovanje <-> `Reader/Writer`

## 3.2 - Hijerarhija tokova karaktera
```
Reader
   |
   |-- InputStreamReader
   |      |
   |      |-- FileReader
   |-- BufferedReader
   |-- StringReader

Writer
   |
   |-- OutputStreamWriter
   |      |
   |      |-- FileWriter
   |-- BufferedWriter
   |-- StringWriter
```

Klasa `Reader` sluzi za unos karaktera.
Vazne metode:
| Metoda | Opis |
| ------ | ---- |
| `read()`                 | Cita karakter, vraca celobr. vrednost     |
| `read(char[])`           | Cita karaktere u niz                      |
| `read(char[], off, len)` | Cita odredjeni region niza karaktera        |
| `skip(n)`                | Preskace karaktere                        |
| `ready()`                | Proverava da li je tok spreman za citanje |
| `close()`                | Zatvara tok                               |

Npr:
```java
try (FileReader reader = new FileReader("example.txt")) {
    int character;
    while ( (character = reader.read()) != -1 ) {
        System.out.print((char) character);
    }
}
```
- `read()` vraca ceo br a ne karakter zbog terminacionog karaktera -1.

Klasa `Writer` sluzi za ispis karaktera.
Vazne metode
| Metoda | Opis |
| ------ | ---- |
| `write()`                 | Pise karakter                             |
| `write(char[])`           | Pise niz karaktera                        |
| `write(char[], off, len)` | Pise odredjeni region niza karaktera      |
| `write(String)`           | Pise nisku                                |
| `append()`                | Nalepljuje karaktere                      |
| `flush()`                 | Silni ispis sa bafera                     |
| `close()`                 | Zatvara tok                               |

Npr:
```java
try (FileWriter writer = new FileWriter("example.txt")) {
    writer.write("Hello, Java!");
}
```
- Ukoliko datoteka ne postoji, odmah biva kreirana.
- Postoji konstruktor `FileWriter` za iskljucivo nalepljivanje karaktera:
    ```java
    try (FileWriter writer = new FileWriter("example.txt", true)) {
        writer.write("\nAnother line.");
    }
    ```

## 3.3 - Sifrovanje karaktera
Sifrovanje karaktera podrazumeva pretvaranje skladistenih bajtova u karaktere. Desifrovanje je suprotan proces.

"Čaša" -> UTF-8 sifrovanje: bajtovi u datoteku
UTF-8 desifrovanje: bajtovi sa datoteke -> "Čaša"

Postoje vise formata za sifrovanje ali je UTF-8 najopsirniji. 

## 3.4 - InputStreamReader i InputStreamWriter
Klase se koriste da premoste tokove bajtova i karaktera.

`FileInputStream` -> `InputStreamReader` -> Karakteri
Npr:
```java
try (
    InputStreamReader reader = new InputStreamReader(new FileInputStream("example.txt")) {
        int c;
        while ( (c = reader.read()) != -1 ) {
            System.out.print((char) c);
        }
    }
)
```

Karakteri -> `OutputStreamWriter` -> `FileOutputStream`
Npr:
```java
try (
    OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("example.txt"))
) {
    writer.write("Hello, Java!");
}
```
Ove klase omogucavaju upotrebu bilo kog izvorista ili destinacije koje je orijentisano bajtova. Na primer izvor sa mreze moze pruziti `InputStream` koji moze biti obmotan `InputStreamReader`-om za citanje teksta.

# 4 - Baferovani tokovi karaktera

Recimo da ucitavamo 10MB podataka. Za velike podatke uvek koristimo bafere za privremeno ucitavanje vecih komada podataka u memoriju.

**Bafer**: Datoteka -> Bafer -> Apl.

## 4.1 - `BufferedReader`
Klasa toka karaktera koja daje mogucnost baferovanja klasi `Reader`.

Najvaznija metoda je `String readLine()`, koja za razliku od `int read()` cita celu liniju.

Npr:
```java
try ( BufferedReader reader = new BufferedReader(new FileReader("example.txt")) ) {
    String line;
    while ( (line = reader.readLine()) != null ) {
        System.out.println(line);
    }
}
```
- Ovakav program obradjuje linije, a ne karaktere. Metoda vraca niske.

Vazne metode:
| Metoda | Opis |
| ------ | ---- |
| `read()` | Cita karakter   |
| `read(char[])` |  Cita karaktere u niz  |
| `readLine()` | Cita celu liniju   |
| `skip(n)` |  Preskace karaktere  |
| `ready()` |  Proverava da li je tok spreman  |
| `mark(n)` |  Oznacava poziciju  |
| `reset()` |  Vraca se na oznacenu poziciju  |
| `close()` |  Zatvara tok  |

## 4.2 - `BufferedWriter`
Klasa prikuplja manje operacije pisanja u bafer i daje tu mogucnost `Writer`-u.

Npr:
```java
try ( BufferedWriter writer = new BufferedWriter(new FileWriter("example.txt")) ) {
    writer.write("Java");
    writer.newLine();
    writer.write("Python");
    writer.newLine();
}
```
- Rezultat:
    ```
    Java
    Python
    ```

Vazne metode:
| Metoda | Opis |
| ------ | ---- |
| `write(String)` | Pise nisku   |
| `write(char[])` |  Pise niz karaktera  |
| `newLine()` | Pise izlazni karakter za odvajanje linija, ne zavisi od platformi   |
| `flush()` |  Nasilan ispis karaktera iz bafera  |
| `close()` |  Zatvara tok  |

## 4.3 - `flush()` metoda 
Baferovani karakteri mogu privremeno ostati u memoriji umesto da se odmah dodju do destinacije. Metoda `flush()` forsira ispis baferovanih karaktera.

Npr:
```java
try ( BufferedWriter writer = new BufferedWriter(new FileWriter("example.txt")) ) {
    writer.write("First message");
    writer.flush();
    writer.write("Second message");
}
```

## 4.4 - Decorator projektni obrazac
Decorator obrazac se koristi za obmotavanje tokova cime se prosiruju funkcionalnosti.

Npr:
```java
BufferedReader reader = new BufferedReader(
    new InputStreamReader(
        new FileInputStream("example.txt"),
        StandardCharsets.UTF_8
    )
);
```
- Lanac obrade je: `File` - `FileInputStream` - `InputStreamReader` - `BufferedReader` - Apl.

# 5 - Tokovi podataka
Do sada su obradjeni iskljucivo tokovi tekstualnih karaktera. Dalje se radi sa binarnim podacima.

Glavne klase su `DataInputStream` i `DataOutputStream`.

## 5.1 - Zasto tokovi podataka
Npr: zelimo da sacuvamo zapis nekog studenta
```java
int id = 101;
String name = "Mark";
double gpa = 9.2;
boolean active = true;
```
- Resenje sa dosadanjim znanjem bi bila konverzija ovih sadrzaja u tekst pri citanju i pisanju.

Tokovi podataka dozvoljavaju pisanje primitivnih tipova podataka u Javi u binarnom formatu:
```java
dataOutputStream.writeInt(101);
dataOutputStream.writeString("Mark");
dataOutputStream.writeDouble(9.2);
dataOutputStream.writeBoolean(true);
```
Kao i citanje:
```java
int id = dataInputStream.readInt();
String name = dataInputStream.readString();
//...
```
- Redosled operacija je vazan. Ako je zapisan ceo br, ceo br. mora biti i procitan na datoj poziciji.

## 5.2 - `DataOutputStream`
Decorator koji obmotava `OutputStream` i dozvoljava pisanje primitivnim tipovima

Vazne metode:
| Metoda | Velicina zapisanih podataka |
| ------ | --------------------------- |
| `writeInt(int)` | 4b |
| `writeLong(double)` | 8b | 
| `writeDouble(double)` | 8b | 
| itd.. |  | 

Npr:
```java
try (DataOutputStream out = new DataOutputStream(
        new BufferedOutputStream(
            new FileOutputStream("student.dat")))) {

    out.writeInt(101);
    out.writeUTF("Mark");
    out.writeDouble(9.2);
    out.writeBoolean(true);
}
```
- `FileOutputStream` za pisanje bajtova u datoteku
- `BufferedOutputStream` za baferovan ispis bajtova
- `DataOutputStream` za konverziju primitivnih tipova iz Jave u bajtove

## 5.3 - `DataInputStream`
Citanje primitivnih vrednosti sa `InputStream`-a

Npr:
```java
try (DataInputStream in = new DataInputStream(
        new BufferedInputStream(
            new FileInputStream("student.dat")))) {

    int id = in.readInt();
    String name = in.readUTF();
    double gpa = in.readDouble();
    boolean active = in.readBoolean();

    System.out.println(id);
    System.out.println(name);
    System.out.println(gpa);
    System.out.println(active);
}
```

## 5.4 - Vazna ogranicenja
- *Tokovi podataka ne skladise promenljive, samo informacije*

- *Vazan redosled citanja informacija*
Pravilno:
```java
int id = in.readInt();
String name = in.readUTF();
double gpa = in.readDouble();
```
Nepravilno:
```java
String name = in.readUTF();
int id = in.readInt();
double gpa = in.readDouble();
```
Citac obradjuje bajtove na osnovu redosleda poziva metoda.

- Kada tok cita vrednost ali nema dovoljno bajtova, izbacuje se `EOFException` izuzetak
Npr:
```java
try {
    while (true) {
        int id = in.readInt();
        System.out.println(id);
    }
} catch (EOFException e) {
    System.out.println("Finished reading records.");
}
```

- *Tokovi objekata nisu mehanizam serijalizacije*: za to sluze `ObjectInputStream`, `ObjectOutputStream` i `Serializable`.

!! Zavrsi projekte 4 i 5 !!

# 6 - Tokovi objekata
Kod tokova podataka, klase su se zapisivale i citale u vidu binarnih podataka.

Polja iz objekata moraju manuelno putem metoda da se citaju i pisu. Npr:
```java
out.writeInt(student.getId());
out.writeUTF(student.getName());
out.writeDouble(student.getAverage());
out.writeBoolean(student.isActive());
```

Kako bi se ovaj proces pojednostavnio, koristi se *serijalizacija*, proces konverzije stanja jednog objekta u tok bajtova. Suprotan proces je *deserijalizacija*.

Java klasa -> [ser.] -> tok bajtova -> [deser.] -> Java klasa

Kako bi klasa podrzala serijalizaciju, mora primeniti `Serializable` interfejs:
```java
public class Student implements Serializable {
    private int id;
    private String name;
    private double average;
    private boolean active;
    public Student(int id, String name, double average, boolean active) {
        this.id = id;
        this.name = name;
        this.average = average;
        this.active = active;
    }
    // Getters and toString()
}
```
- Intefejs sam po sebi nema metode, sluzi cisto kao oznaka.

## 6.1 - `ObjectOutputStream`
Klasa zapisuje objekte u tok:
```java
try (ObjectOutputStream out = new ObjectOutputStream(
        new BufferedOutputStream(
            new FileOutputStream("students.dat")))) {

    Student student = new Student(101, "Mark", 9.2, true);

    out.writeObject(student);
}
```

Radi i sa kolekcijama:
```java
List<Student> students = new ArrayList<>();

students.add(new Student(101, "Mark", 9.2, true));
students.add(new Student(102, "Anna", 8.7, true));

out.writeObject(students);
```

## 6.1 - `ObjectInputStream`
Sluzi za deserijalizaciju objekta:
```java
try (ObjectInputStream in = new ObjectInputStream(
        new BufferedInputStream(
            new FileInputStream("students.dat")))) {

    Student student = (Student) in.readObject();
    System.out.println(student);
}
```
- `readObject()` je metoda tipa Object pa se zato radi type casting. Metoda moze izbaciti `ClassNotFoundException` izuzt.

## 6.2 Koncepti serijalizacije
- Tranzijentna polja: polja koje ne zelimo da serijalizujemo
```java
public class User implements Serializable {
    private transient String password;
}
```

- **Nativna serijalizacija se generalno ne koristi u produkciji. Koriste se formati pogodni za ser/de kao sto su JSON i Protobuf**.

# 7 - U/I integracija

Odnosi se na najbolje primene.
1) Upotreba odgovarajuceg toga u zavisnosti od podataka:
| Situacija | API |
| --------- | --- |
| Slike, PDF-ovi i bajtovi | `InputStream`/`OutputStream` |
| Citanje i pisanje teksta | `Reader`/`Writer` |
| Performativno citanje teksta (liniju po liniju) | `BufferedReader` |
| Perform. pisanje teksta | `BufferedWriter` |
| Skladistenje primitivnih tipova u binarnom formatu | `DataInputStream`/`DataOutputStream` |
| Skladitstenje Java objekata | `ObjectInputStream`/`ObjectOutputStream` + `Serializable` |

2) Uvek koristiti *try-with-resources* sablon za upravljanje resursima.

3) Najcesci izuzetak je `IOException` ali U/I operacije mogu biti kvarne zbog vise razloga.

4) Baferovanje pomaze za performanse

5) Koristiti sifrovanje karaktera u konstruktoru kao sto je `StandardCharsets.UTF_8`

6) Odvajati operacije u posebnim klasama.
Na primer, sablon kao sto je:
`Main`: ulazna tacka apl.
`FileService`: operacije nad tekstom viseg nivoa.
`FileReader`: citanje
`FileWriter`: pisanje
`TextProcessor`: obrada i analiza teksta


# POGLAVLJE 2 
---

# 8 - NIO u Javi

NIO predstalja noviji API za rad sa U/I operacijama.

Dok je klasicni U/I API oreijentisan tokovima, NIO koristi pristup orijentisan baferima i kanalima.

Primer: standardni I/O API
```java
try (InputStream in = new FileInputStream("input.txt")) {
    byte[] buffer = new byte[1024];
    int bytesRead;
    while ( (bytesRead = in.read()) != -1 ) {
        // Obrada bajtova...
    }
}
```

NIO API uz kanale i bafere:
```java
try (FileChannel channel = FileChannel.open(Path.of("input.txt"))) {
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    while (channel.read(buffer) != -1) {
        buffer.flip();
        while (buffer.hasRemaining()) {
            byte value = buffer.get();
            // Obrada bajtova...
        }
        buffer.clear();
    }
}
```
- Tok cita ili pise podatke direktno preko metoda tokova.
- **Kanal** prenosi podatke kroz bafer.

## 8.1 - Blokirajuci i neblokirajuci U/I
*Blokirajuce U/I operacije* znaci da nit blokira dok se operacija ne izvrsi.
Primer:
```java
int value = input.read();
```
Ukoliko trenutno nema podataka, nit ce ostati blokirana dok podaci ne stignu, tok ne dodje to EOF-a ili dodje do neke greske. Ovim se gubi proc. vreme.

Kod *Neblokirajucih U/I operacija*, odredjene operacije nad kanalima mogu odmah vratiti vrednost ukoliko trenutno ne mogu napredovati. Neblokirajuci soket kanal ce probati da procita podatke i vratice 0 ukoliko nema podataka. Ovim se ne zauzima proc. vreme.

**Blokirajuci U/I**: Nit -> [Ceka] -> [Nastavlja]
**Neblokirajuci U/I**: Nit -> [Vraca vrednost] -> [Druge operacije]

NIO API-ji nisu sami po sebi svi neblok. `FileChannel` operacije generalno pripadaju blok. U/I-u. 


## 8.2 - Kanali
*Kanal* predstavlja vezu sa entitetom koji moze vrsiti U/I operacije.

Primeri kanala: `FileChannel`, `SocketChannel`, `ServerSocketChannel`, `DatagramChannel`.

Kanal je veza kroz koju proticu podaci. Za razliku od tokova, kanali podrzavaju i citanje i pisanje u zavisnosti od nacina na koji su otvoreni.

Primer: `FileChannel` kanal za citanje i pisanje
```java
FileChannel channel = FileChannel.open(
    Path.of("data.txt"),
    StandardOpenOption.READ,
    StandardOpenOption.WRITE
);
```

## 8.3 - Baferi
*Bafer* skladisti podatke.

Postoje baferi koji odgovaraju svim primitivnim tipovima u Javi. Najcesce se koristi `ByteBuffer`.

Primer: `ByteBuffer` koji alocira 1024b
```java
ByteBuffer buffer = ByteBuffer.allocate(1024);
```
- NIO baferi imaju tri vazna svojstva:
    - *kapacitet*: konacna velicina bafera
    - *pozicija*: trenutna pozicija citanja/pisanja
    - *limit*: granica izmedju koje citanje ili pisanje nije dozovljeno

*Stanje bafera*:
[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
 ^        ^  ^     ^  ^
 |        |  |     |  |
 x        x  y     y  z

x: trenutna pozicija bafera
y: dostupan prostor pre limita
z: preko limita bafera

Primer: pisemo 4b
```java
ByteBuffer buffer = ByteBuffer.allocate(10);
buffer.put((byte)10);
buffer.put((byte)20);
buffer.put((byte)30);
buffer.put((byte)40);
```
- Stanje bafera je
    - `capacity = 10`
    - `position = 4`
    - `limit = 10`

### 8.3.1 - Kljucne metode bafera
- `flip()` -> menja bafer sa rezima pisanja na rezim citanja.
Konceptualno, `limit` dobija vrednost prethodne `position` vrednosti bafera, dok `position` postaje 0:
```
Pre .flip():
position = 4
limit = 10

Posle .flip():
position = 0
limit = 4
```
Ovim su prethodna 4 upisana bajta spremna za citanje.

- `get()` -> cita vrednost i zatim ide unapred za jednu vr. `position`.
```java
byte first = buffer.get();
```
Stanje nakon operacije:
```
position = 1
limit = 4
```

- `clear()` -> resetuje bafer kako bi ponovo bio spreman za pisanje.
```java
buffer.clear()
// position = 0
// limit = 10
```
- Operacija ne brise podatke, samo resetuje `position` i `limit` bafera.

### 8.3.2 - Zivotni ciklus bafera
1) Alokacija -> `ByteBuffer.allocate(1024)`
2) Pisanje -> `put()`/`channel.read()`
3) `flip()` -> spreman za citanje
4) Citanje -> `get()` / `channel.write()`
5) Spremanje za sledeci ciklus -> `clear()` / `compact()`

Metoda `compact()` cuva neprocitane podatke tako sto ih pomera na pocetak bafera. Korisna je kada nismo zavrisli za obradom baferovanih podat.

## 8.4 - Selektori
*Selector* dozvoljava jednoj niti upravljanje nad vise zeljenih neblok. kanala.

Primer: server koji opsluzuje 1000 klijenata. Bez neblok. U/I-a, za $n$ klijenata morali bi da dodelimo $n$ niti. 

- Selektori dozvoljavaju da jedna nit vrsi monitoring nad vise veza radi obradjivanja onih veza koje su spremne za izvrsavanje operacija.

- Vazne klase su `Selector`, `SelectionKey`, `SocketChannel`, `ServerSocketChannel`

# 9 - `Path` i `Files`
Za rad sa datotekama i direktorijumima putem NIO API-a koriste se klase `Path` i `Files`.

## 9.1 - `Path`
Primer:
```java
Path path = Path.of("data/example.txt");
```
- Objekat `Path` predstavlja neku lokaciju u sistemu datoteka. Ne mora nuzno da znaci da ta datoteka postoji.

Kreiranje objekta `Path` se moze izvrsiti na vise nacina:
```java
// Najbolji nacin
Path path1 = Path.of("data", "example.txt");
Path path2 = Paths.get("data/example.txt");
Path path3 = FileSystems.getDefault().getPath("data/example.txt");
```
- Relativne putanje u metodi `of()` su u formatu `foo/bar.txt` dok su apsolutne `/Users/student/data/foo.txt`.

Korisne metode:
| Metoda             | Svrha |
|--------------------| ----- |
| `toAbsolutePath()` | Pretvara putanju u apsolutnu      |
| `getFileName()`    | Vraca konacnu komponentu putanje      |
| `getParent()`      | Vraca roditeljsku putanju      |
| `getRoot()`        | Vraca korenu putaju sist. dat.      |
| `normalize()`      | Uklanja redudatne . i ..      |
| `resolve()`        | Spaja putanje      |
| `relativize()`     | Racuna relativnu putanju dveju putanja      |
| `startsWith()`     | Proverava da li putanja pocinje drugom putanjom      |
| `endsWith()`       | Isto kao i prosla ali proverava zavrsetak      |

Primer:
```java
Path dir = Path.of("data");
Path file = dir.resolve("example.txt");
```
- Rezultat: `data/example.txt`

Primer: `normalize()`
```java
Path path = Path.of("data/temp/../example.txt");
System.out.println(path.normalize());
```
- Rezultat: `data/example.txt`
- Uklanja redudantne komponente jedne putanje.

## 9.2 - `Files` i operacije nad sistemom datoteka
Klasa `Files` sadrzi korisne staticke metode za rad sa datotekama i dir-ovima.

Primer: provera da li neka datoteka postoji
```java
Path path = Path.of("data/example.txt");
if (Files.exists(path)) {
    System.out.println("File exists.");
}
```

## 9.3 - Kreiranje datoteka i direktorijuma
Primer: kreiranje datoteke
```java
Path path = Path.of("data/example.txt");
Files.createFile(path);
```

Primer: kreiranje direktorijuma
```java
Files.createDirectory(Path.of("data"));
```

Primer: kreiranje ugnjezdenih dir.
```java
Files.createDirectories(
    Path.of("data/users/admin/documents")
);
```

Primer: kopiranje datoteka
```java
Path src = Path.of("src.txt");
Path dest = Path.of("backup.txt");
Files.copy(src, dest);

// Zamena originalne datoteke
Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
```

Primer: premestanje i preimenovanje datoteka
```java
Path source = Path.of("old-name.txt");
Path destination = Path.of("new-name.txt");
Files.move(source, destination);

// Zamena postojece destinacije
Files.move(
    source,
    destination,
    StandardCopyOption.REPLACE_EXISTING
);

// Atomicka zamena
Files.move(
    source,
    destination,
    StandardCopyOption.ATOMIC_MOVE
);
```

Primer: brisanje datoteka
```java
Files.delete(path);
// ili
boolean deleted = Files.deleteIfExists(path);
```

Primer: citanje svih linija
```java
List<String> lines = Files.readAllLines(
    Path.of("data.txt"),
    StandardCharsets.UTF_8
);
```

Primer: citanje svih bajtova
```java
byte[] data = Files.readAllBytes(
    Path.of("image.png")
);
```

Pisanje teksta:
```java
Files.writeString(
    Path.of("output.txt"),
    "Hello from NIO!",
    StandardCharsets.UTF_8
);
```

Citanje teksta:
```java
String content = Files.readString(
    Path.of("output.txt"),
    StandardCharsets.UTF_8
);
```

Dodavanje teksta:
```java
Files.writeString(
    Path.of("output.txt"),
    "Another line\n",
    StandardCharsets.UTF_8,
    StandardOpenOption.CREATE,
    StandardOpenOption.APPEND
);
```

Listanje datoteka:
```java
Path directory = Path.of("data");

try (Stream<Path> paths = Files.list(directory)) {
    paths.forEach(System.out::println);
}
```

Filtriranje pri listanju datoteka:
```java
try (Stream<Path> paths = Files.list(directory)) {
    paths.filter(Files::isRegularFile)
         .forEach(System.out::println);
}
```

Filtriranje po ekstenziji pri listanju datoteka:
```java
try (Stream<Path> paths = Files.list(directory)) {
    paths.filter(path ->
        path.toString().endsWith(".txt")
    ).forEach(System.out::println);
}
```

# 10 - Atributi datoteka i putovanje kroz direktorijume
