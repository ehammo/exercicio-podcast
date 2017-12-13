# Bateria

Analisando a aplicação podcast no quesito de Bateria.
Seguindo a [documentação](https://developer.android.com/training/monitoring-device-state/index.html) percebi que já fazia uma das boas práticas de aplicativos eficientes neste quesito. 

**Exemplo 1:**

Umas das lições da documentação é que se deve checar se o dispositivo tem internet antes  de fazer o download e essa boa prática eu já fazia.

![alt_text](https://github.com/ehammo/exercicio-podcast/blob/master/Podcast/markdown/images/bateria1.PNG)

**Exemplo 2:**

Outra lição relevante também diz respeito ao uso da rede. O uso extensivo da rede causa drenagem de bateria. Como informa a [documentação](https://developer.android.com/topic/performance/power/network/analyze-data.html)

![alt_text](https://github.com/ehammo/exercicio-podcast/blob/master/Podcast/markdown/images/bateria2.PNG)

Como se pode perceber, um download prolongado é ineficiente neste quesito. A solução foi diminuir o tempo de download, tendo um buffer maior para baixar os episodios.
