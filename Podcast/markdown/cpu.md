# CPU e Performance

Analisando a aplicação podcast no quesito de CPU e performance.
Quanto ao Uso da CPU:
Antes de adicionar os componentes arquiteturais a CPU ficava baixíssima.
A figura abaixo representa o uso da CPU sem componentes arquiteturais, com uma média de 2% de uso.
O app estava em uso, e isso ocorreu durante um download, e enquanto tocava um podcast.

![alt text](https://github.com/ehammo/exercicio-podcast/blob/master/Podcast/markdown/images/antes.PNG)

Após a adição dos componentes arquiteturais a CPU aumentou em 9 vezes a média do uso da CPU.

![alt text](https://github.com/ehammo/exercicio-podcast/blob/master/Podcast/markdown/images/depois.PNG)

Quanto as boas práticas, seguindo a [Documentação](https://developer.android.com/training/best-performance.html) percebi que já fazia uma das boas práticas de aplicativos eficientes neste quesito. 

**Exemplo 1:**

O uso de ViewHolder. Usando ViewHolder podemos acessar todas as views de um layout sem ter que procurá-las usando findViewById. Poupando ciclos de processamento

![alt text](https://github.com/ehammo/exercicio-podcast/blob/master/Podcast/markdown/images/viewholder.PNG)
![alt text](https://github.com/ehammo/exercicio-podcast/blob/master/Podcast/markdown/images/viewholder2.PNG)


**Exemplo 2:**

Uso de AsyncTasks e Services. Usando threads separadas da thread principal para downloads e acesso a banco de dados, permitimos que o layout tenha uma boa performance;

