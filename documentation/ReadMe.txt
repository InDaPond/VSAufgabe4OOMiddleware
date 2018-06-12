Die Binaries sind unter out/production/OOMiddleware/<packageName> zu finden.
Entweder kann in deren Pfad gewechselt werde, oder der gesamte Pfad wird beim Aufruf mit angeben.
Die Binaries werden mittels java <Pfad> <Args> aufgerufen.

IDL-Compiler:
java <Pfad zu idl_compiler/Parser> <Pfad der zu übersetzenden .idl Datei> <Pfad, wo das package erzeugt werden soll>
Beispielaufruf:
java idl_compiler/Parser D:\\Uni\\VS\\OOMiddleware\\idlFiles\\bank.idl D:\\Uni\\VS\\OOMiddleware\\src
Verzeichnis: /d/Uni/VS/OOMiddleware/out/production/OOMiddleware

NameService:
java <Pfad zu nameservice/NameService> <Port>
Beispielaufruf:
java nameservice/NameService 4000
Verzeichnis: /d/Uni/VS/OOMiddleware/out/production/OOMiddleware
