# Primeniti operacije (lekcija 3):
A: Ulaz datoteke -> `IFileService.java`
- [x] Prihvata putanju izvorne datoteke
- [x] Verifikacija da datoteka na datoj putanji postoji
- [x] Verifikacija da je izvorna datoteka datoteka
- [x] Citanje datoteke uz `FileReader` ili `InputStreamReader`
- [x] Obrada praznih datoteka
- [x] Obradjivanje `IOException` izuzetaka

B: Izlaz datoteke -> `IFileService.java`
- [x] Prihvatanja putanju destinacije
- [x] Verifikacija da destinacija nije direktorijum
- [x] Izvorna datoteka se ne sme menjati
- [x] Pisanje teksta uz `FileWriter` ili `OutputStreamWriter`
- [x] Podrzana operacija nalepljivanja
- [x] Obradjivanje `IOException` izuzetaka

C: Obrada teksta -> `TextProcessor.java`
- [ ] Prebrojavanje konacnog broja karaktera
- [ ] Prebrojavanje konacnog broja reci
- [ ] Prebrojavanje konacnog broja linija
- [ ] Prebrojavanje ponavljanja odredjenih reci
- [ ] Konverzija teksta u velika slova
- [ ] Konverzija teksta u mala slova
- [ ] Zamena odredjene reci drugom recju

D: Testovi
- [ ] Bar 15 testova

# Primeniti operacije (lekcija 4):
- [x] Sve isto samo nadograditi baferovanim klasama.
- [x] Uporediti performanse zadatka 3 i 4. (Bez veceg testiranja, bez bafera pisanje traje 342.333 ns dok baferovano pisanje traje 198.959 ns => 72.06% brze)