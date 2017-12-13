# Bandwidth

Analisando a aplicação podcast no quesito de Rede.
A aplicação só usa rede em dois momentos. Quando a activity principal é iniciada, e quando um episódio de podcast é baixado. Com as figuras abaixo representamos o uso da rede nesses dois casos
Ao iniciar a aplicação, caso o download da lista de episódios ainda não tenha sido realizado, há um pequeno pico de uso de banda. A Figura abaixo mostra essa variação. Após o download da lista de episódios, nenhuma alteração ocorre.

![alt_text](https://github.com/ehammo/exercicio-podcast/blob/master/Podcast/markdown/images/network1.PNG)

Ao minimizar a aplicação e retornar para a mesma, o mesmo pico ocorre, já que no onStart da MainActivity o download da lista é sempre realizado, com o intuito de recuperar a lista mais atualizada. 
A Figura abaixo mostra o fluxo de que ocorre durante o download de um episódio de podcast.


![alt_text](https://github.com/ehammo/exercicio-podcast/blob/master/Podcast/markdown/images/network2.PNG)

Se a aplicação é retirada do primeiro plano, o tráfego de dados continua o mesmo, visto que este depende apenas do serviço que está sendo rodado em background. Na imagem abaixo o aplicativo permaneceu em segundo plano até o término do download. Outra informação relevante é que o uso da rede para quando o download termina. 

![alt_text](https://github.com/ehammo/exercicio-podcast/blob/master/Podcast/markdown/images/network3.PNG)
