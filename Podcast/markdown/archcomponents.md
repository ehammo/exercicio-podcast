# Architeture Components

# Referências

Utilizamos diversar referências para fazer a refatoração do código:
* https://developer.android.com/topic/libraries/architecture/room.html
* https://developer.android.com/topic/libraries/architecture/livedata.html
* https://developer.android.com/topic/libraries/architecture/viewmodel.html
* https://android.jlelse.eu/room-store-your-data-c6d49b4d53a3
* https://github.com/googlesamples/android-architecture-components/tree/master/BasicSample

Sendo a principal das referências o BasicSample do Google, tanto para a implementação dos architetural 
components em si, como para estruturação do projeto e outras abstrações como DataRepository.

# Implementação

Removemos as classes anteriores ligadas ao banco de dados:
* GetFromDatabase
* PodcastDBHelper
* PodcastProvider
* PodcastProviderContract

E substituímos com as novas implementações dos componentes arquiteturais
* AppDatabase
* ItemFeeDAO
* ItemFeedEntity
* ItemFeedListViewModel
* ItemFeedViewModel

E remodelamos ItemFeed de domain para Model, sendo apenas uma interface agora

Todas as outras classes que tinham dependência no ItemFeed anterior agora fazem uso de ItemFeed ou 
ItemFeedEntity

Além disso, criamos DataRepository para ser a interface de comunicação com o banco de dados na 
aplicação como um todo, abstraindo a necessidade de se comunicar com DAO ou o RoomDatabase.

Para utilizarmos LiveData, partimos direto para ViewModel, pois nas referências utilizadas vimos que
toda vez que a Activity é recriada, o LiveData conectado direto ao DAO faz as queries ao banco de dados
novamente, enquanto o ViewModel faz a query uma vez e salva os valores internamente para reuso independente
do ciclo de vida da Activity.

Fizemos uso de ViewModel também na tela de Details, fazendo uma transição de passar todos os dados
por Intent para passar somente a PrimaryKey do ItemFeed e utilizando o DataRepository com ViewModel 
para pegar os dados necessários.

# Complicações

Na implementação inicial, o DownloadXMLService inseria os dados no DB sem utilizar o DataRepository 
como intermediador, e isso gerou um bug na aplicação, pois aparentemente o RoomDatabase retorna DAO's 
diferentes na hora de pegar, e tendo um DAO no DataRepository para leitura e um DAO para escrita no Service
gerava uma inconsistência que o ViewModel não era notificado das mudanças e não atualizava a tela, até
a aplicação ser completamente reiniciada, e o banco de dados já populado ser lido novamente.
A solução foi adicionar um método intermediário de adição no DataRepository e utilizá-lo no Service

