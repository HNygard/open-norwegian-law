See also https://github.com/HNygard/norsk-lovtidend. That repo contains the data that is basis for Norwegian Law.

Deploying this app/API to https://open-norwegian-law.herokuapp.com/.

# Development


- Example application URL:
  http://localhost:8090/law-reference?lawRef=Offentleglova%20%C2%A72%20f%C3%B8rste%20ledd%20bokstav%20c

Running as JAR locally (same as heroku):

    mvn install
    java -jar target/norway-law-java-1.0-SNAPSHOT.jar

# TODO

- [x] Lese lovreferanse
- [x] Lese paragraf
- [x] Lese ledd
- [x] Lese punktum fra ledd
- [ ] Lese bokstav fra ledd
- [ ] Lese lover fra Norsk Lovtidend
- [ ] Konsolidere lover fra Norsk Lovtidend
- [ ] Lov-sammenligner. Hente konsolidert fra Lovdata (privat formål) og sjekke mot konsolidert fra kode.
