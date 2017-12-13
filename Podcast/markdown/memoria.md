# Memoria

Neste quesito foi analisado Leaks na memoria.

A seguir é mostrado em que activity foi encontrado o leak, por que o leak aconteceu e como foi resolvido.

Activity: SettingsActivity

Problema: No onCreate fragmento se registra um listener mas não se cancela o registro.

![alt_text](https://github.com/ehammo/exercicio-podcast/blob/master/Podcast/markdown/images/memoria1.PNG)

Solução: Dar unregister no onDestroy da activity

![alt_text](https://github.com/ehammo/exercicio-podcast/blob/master/Podcast/markdown/images/memoria2.PNG)

Activity: MainActivity

Problema: Passa uma referência de um ListView e uma de contexto para o PodcastReceiver. Que por sua vez passa essas referências para uma tarefa assíncrona. E já que a AsyncTask é uma classe não estática, a tarefa mantém a referência do ListView, e a referência do contexto da MainAcitivty. Isso faz com que a activity não esteja elegível para garbage collection, gerando o leak de memória.

![alt_text](https://github.com/ehammo/exercicio-podcast/blob/master/Podcast/markdown/images/memoria3.PNG)
![alt_text](https://github.com/ehammo/exercicio-podcast/blob/master/Podcast/markdown/images/memoria4.PNG)
![alt_text](https://github.com/ehammo/exercicio-podcast/blob/master/Podcast/markdown/images/memoria5.PNG)


Solução: Cancelar a AsyncTask caso a activity pare.

![alt_text](https://github.com/ehammo/exercicio-podcast/blob/master/Podcast/markdown/images/memoria6.PNG)
![alt_text](https://github.com/ehammo/exercicio-podcast/blob/master/Podcast/markdown/images/memoria7.PNG)

