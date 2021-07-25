See also https://github.com/HNygard/norsk-lovtidend. That repo contains the data that is basis for Norwegian Law.

Deploying this app/API to https://open-norwegian-law.herokuapp.com/.

# Development


- Example application URL:
  http://localhost:8090/law-reference?lawRef=Offentleglova%20%C2%A72%20f%C3%B8rste%20ledd%20bokstav%20c

Running as JAR locally (same as heroku):

    mvn install
    java -jar target/norway-law-java-1.0-SNAPSHOT.jar

# Heroku

Setup:

    heroku login
    heroku git:remote -a open-norwegian-law

Deploying:

    git push heroku master

# TODO

- [x] Lese lovreferanse
- [x] Lese paragraf
- [x] Lese ledd
- [x] Lese punktum fra ledd
- [ ] Lese bokstav fra ledd
- [ ] Lese lover fra Norsk Lovtidend
- [ ] Konsolidere lover fra Norsk Lovtidend
- [ ] Lov-sammenligner. Hente konsolidert fra Lovdata (privat formål) og sjekke mot konsolidert fra kode.

# TODO - Ajourføring

Kritierie:
- [X] Lov etter 2000
- [X] Har endringslover

Aktuelle lover for ajourføring først:
- LOV-2002-06-28-57 - Lov om valg til fylkesting og kommunestyrer (valgloven)
- LOV-2001-06-15-53 - Lov om erstatning ved pasientskader mv. (pasientskadeloven)
- LOV-2001-06-15-59 - Lov om stiftelser (stiftelsesloven)
- LOV-2001-06-15-70 - Lov om fastsetjing og endring av kommune- og fylkesgrenser (inndelingslova)
- LOV-2001-06-15-75 - Lov om veterinærer og annet dyrehelsepersonell
- LOV-2001-06-15-81 - Lov om elektronisk signatur
- LOV-2002-06-21-34 - Lov om forbrukerkjøp (forbrukerkjøpsloven)
- LOV-2007-12-14-116 Lov om studentsamskipnader

