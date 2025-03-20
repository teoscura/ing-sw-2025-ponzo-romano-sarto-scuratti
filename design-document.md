# Progetto di ing del software.

2025 - Ponzo, Romano, Sarto, Scuratti - Prof. Cugola

## Idee di design e come applicarle

Gestione dei diversi tipi di voli: Volo di Prova o Volo Normale:

- Implementazione tramite un pattern strategy.

Scelta della configurazione della partita:

- Il primo utente che entra ha a disposizione un pannello dic controllo con dropdown menu, checkbox e text fields, invia poi al server controllore della partita la configurazione segnata in formato JSON, ed essa viene applicata alla successiva implementazione strategica e costruzione dei componenti coinvolti. Fallback su un file di default pre-selezionato a presentazione del pannello di controllo.

Gestione multipartita.

- Il controller apre una prima stanza di attesa e attende che il primo utente si unisca, nella fase di attesa della prima partita tutti entrano, a partita avviata viene considerata chiusa la stanza di attesa, viene avviato il thread della partita avviata e aggiunto a un observer che ne monitora il corretto funzionamento e l'eventuale chiusura, salvataggio dati etc.\
\
A primo contatto con l'utente il controller manda una lista delle partite in attesa al momento della connessione, con numero identificativo e username del primo utente connesso.\
Al controller con una flag in cli, o con un pulsante apposito in JavaFX sarà possibile inoltre richiedere una lista delle partite non concluse che si possono recuperare in cui l'username del giocatore è presente, e sarà possibile scegliere quale recuperare tramite identificativo.\
L'utente puo' selezionare un numero tra gli identificativi forniti, o inviare `-1` per creare una nuova sala d'attesa.\
\
Ogni sala d'attesa ha un timeout, dopo 5 minuto e mezzo dalla creazione di essa, si chiude, disconnette ogni utente e li invita a riconnettersi se successo per sbaglio.\
\
Per ovviare al problema delle disconnessioni, ogni disconnessione in fase di gioco avviato viene gestita con la chiusura e conclusione della partita, con ammessa riconnessione tramite persistenza.\
\
A partita avviata conclusa si disconnette ogni utente, singolarmente poi capace di riconnettersi nel caso esso voglia rigiocare.

Gestione persistenza.

- Ogni partita e ogni turno contribuisce alla creazione e modifica di un file json che elenca lo stato di ogni giocatore, permettendo una ricostruzione ricorsiva delle classi che lo compongono e del giocatore stesso, oltre a qualsiasi stato della partita, della sua tipologia. \
\
La sala d'attesa della partita perpetua rifiuterà qualsiasi richiesta di join da giocatori non coinvolti nella sua precedente realizzazione.
\
A inizializzazione server si controllano i numeri delle partite non finite, e li si evitano.

>== TODO RESTO ==
>==================


piuttosto che usare instanceof: visitor, metodo "destroy", metodo retrieve, tenere conto di dove stanno le celle che tengono dentro un tipo specifico di cella e oggetto