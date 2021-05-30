Cada usuário pode escrever perguntas sobre os mais diversos assuntos. Essas perguntas serão mantidas em um segundo arquivo que também precisará do seu CRUD. Nesse arquivo, é necessário manter o vínculo da pergunta ao usuário que a fez. Esse relacionamento é do tipo 1:N (1 usuário para N perguntas).

### A entidade Pergunta

A Pergunta é a principal entidade do nosso sistema. Tudo girará em torno dela. Vamos começar, portanto, definindo a estrutura da nossa entidade Pergunta. Usaremos os seguintes atributos para ela:

* `int idPergunta` - Este atributo identificará cada pergunta de forma exclusiva. Da mesma forma que no caso dos usuários, ele será gerado automaticamente e será na forma de um número inteiro sequencial. Os valores sequenciais, no entanto, não serão numerados por usuário, mas para todo o sistema. Assim, a pergunta de ID 1 pode pertencer a um usuário, a de ID 2 pode pertencer a outro usuário e a pergunta de ID 3 pode ser de um terceiro usuário.
* `int idUsuario` - O ID do usuário será usado para associar esta pergunta ao seu criador. É por meio desse atributo que criaremos o nosso relacionamento 1:N, isto é, 1 usuário poderá criar N perguntas, mas 1 pergunta só pode pertencer a 1 único usuário. Para mantermos a integridade dos nossos dados, será sempre importante só permitir a inclusão de IDs de usuários se eles de fato existirem no arquivo de usuários. Também é importante assegurar que, se um usuário for excluído, todas as suas perguntas também sejam excluídas.
* `long criacao` - Este atributo, do tipo long , conterá a data e a hora da criação da pergunta na forma de milissegundos.
* `short nota` - Esse valor indicará a nota da pergunta dada pelos demais usuários com base na sua relevância e na qualidade da sua construção. A nota é baseada na soma dos votos positivos e negativos. Assim, a nota atual pode também ser positiva ou negativa.
* `String pergunta` - Este atributo será uma string para conter todo o texto da pergunta, mesmo que tenha vários parágrafos ou trechos de código. 
* `booalen ativa` - Este atributo indica se a pergunta está ativa. As perguntas não poderão ser excluídas, mas arquivadas. Uma pergunta arquivada não aparecerá nas listagens. Nesse caso, o valor deste atributo será false. 

Você deve criar uma classe Pergunta com esses atributos e com os métodos getID(), setID(), chaveSecundaria(), toByteArray() e fromByteArray() da mesma forma que na classe Usuário. 


### Menu principal

Antes de implementarmos as operações com perguntas, precisamos começar a criar o nosso menu principal. Você deverá criar, para a sua aplicação, um laço de mais alto nível para conter esse menu. Ele oferecerá acesso a três operações: a primeira é a criação (e gerenciamento) das suas próprias perguntas; a segunda é a consulta a perguntas de outros usuários (onde você também poderá responder, votar ou adicionar comentários a elas); e a terceira é só uma lista de notificações, caso alguém responda a uma pergunta sua. O seu menu principal, por enquanto, deverá ter uma tela como esta:

```
PERGUNTAS 1.0
=============

INÍCIO

1) Criação de perguntas
2) Consultar/responder perguntas 
3) Notificações: 0

0) Sair

Opção: _
```

Por enquanto, você só precisa implementar o acesso à primeira opção, isto é, à criação de perguntas, e a última, de saída do sistema. 

### Operações com perguntas

Precisaremos implementar quatro operações relacionadas às nossas perguntas: inclusão, alteração, exclusão e listagem. Neste ponto, você já deverá ter a classe CRUD lidando com tipos genéricos, porque precisaremos dos seus métodos.

Cada usuário só poderá realizar essas operações para si mesmo. A consulta às perguntas de outros usuários será tratada mais tarde, na segunda opção do menu principal. 

> DICA: Crie uma variável global para armazenar o ID do usuário que estiver usando o sistema. Assim, você agilizará as operações que dependem dessa informação. 

Todas as operações deverão ser realizadas a partir do menu de perguntas, que pode ter essa aparência:

```
PERGUNTAS 1.0
=============

INÍCIO > CRIAÇÃO DE PERGUNTAS

1) Listar
2) Incluir
3) Alterar
4) Arquivar

0) Retornar ao menu anterior

Opção: _
Cada opção levará a uma operação diferente que será descrita abaixo.
```

### Listagem

Na maioria dos sistemas que oferecem relatórios baseados em algum tipo de filtro, é comum encontrarmos arquivos auxiliares para a geração desses relatórios. Caso contrário, seria necessário percorrer todo o arquivo principal e analisar cada um dos registros para ver qual atende aos critérios do filtro.

No nosso caso, o filtro é o atributo `idUsuário`, isto é, só listaremos as perguntas que tiverem o valor do atributo `idUsuário` igual ao ID do usuário que estiver usando o sistema.

Como estrutura de dados para esse arquivo auxiliar, usaremos uma Árvore B+. As árvores B+ são estruturas ordenadas que aceleram consideravelmente processos como as listagens.

A sua Árvore B+ deverá armazenar apenas dois valores para cada pergunta cadastrada no arquivo: o par `idUsuário` e `idPergunta`. Para que tudo funcione adequadamente, é importante que a árvore permita repetições de `idUsuário`, pois um usuário pode ter mais de uma pergunta cadastrada. No entanto, não deve ser permitida a repetição do par de chaves [`idUsuário`, `idPergunta`].

Sua árvore já deve oferecer um método para retornar todos os valores de `idPergunta` cuja primeira chave seja a de um `idUsuário` especificado.

A partir dessa lista, você deve consultar cada `idPergunta` no arquivo de perguntas, usando o índice direto desse arquivo (método `read(ID)`) e apresentá-la na tela.

Os passos dessa operação, portanto, são:

1. Obter a lista de IDs de perguntas na Árvore B+ usando o ID do usuário;
2. Para cada ID nessa lista,
    1. Obter os dados da pergunta usando o método read(ID) do CRUD;
    2. Apresentar os dados da pergunta na tela.

**Atenção**: as perguntas apresentadas na tela devem ser numeradas sequencialmente. O ID da pergunta e o ID do usuário não devem ser apresentados, pois esses IDs são dados para uso interno pelo sistema. O estado da pergunta indicado pelo atributo `ativa` só será apresentado se ela estiver arquivada (inativa) e como sugerido por meio da pergunta 3 abaixo Sua tela deve ficar mais ou menos assim:

### Inclusão
Para incluir uma pergunta no arquivo, o usuário precisará informar apenas a pergunta propriamente dita. Os demais atributos serão já existirão ou serão definidos automaticamente, isto é, o ID do usuário será o ID da pessoa que está usando o sistema, a data/hora de criação será obtida do computador e a nota será, inicialmente, zero. Depois, basta incluir todos esses valores no arquivo usando o método create() CRUD.

Mas aqui há algo bem importante a ser feito. O par de chaves [`idUsuário`, `idPergunta`] deve ser incluído na árvore B+ que mantém o relacionamento entre esses valores. Caso contrário, a listagem acima não poderá ser realizada.

Assim, a sequência de passos da inclusão é:

1. Solicitar a descrição da pergunta;
    * Se a pergunta estiver em branco, retornar ao menu de criação de perguntas;
2. Solicitar a confirmação da inclusão da nova pergunta;
    * Se o usuário não confirmar a inclusão, voltar ao menu de criação de perguntas;
3. Incluir a pergunta no arquivo, por meio do método create(), usando o texto da pergunta, a data/hora da criação, a nota e o ID do usuário;
    * O método retornará o ID da nova pergunta;
5. Incluir o par ID do usuário e ID da pergunta na árvore B+ do relacionamento.

### Alteração
Para alterarmos os dados de uma pergunta, precisamos do ID dessa pergunta. Em um sistema com interface gráfica, apresentaríamos uma lista das perguntas do usuário na tela e ele apenas clicaria na pergunta desejada. Aqui, como estamos usando uma interface textual, teremos que simular algo semelhante. 

A primeira ação é listar todas as perguntas da mesma forma que foi mostrada na tela acima para a listagem. Só que, em seguida, será importante saber qual dessas perguntas o usuário deseja alterar. Assim, você deve manter uma associação (um simples vetor é suficiente) entre o número sequencial apresentado na tela e o real ID da pergunta. O usuário informará o número sequencial apresentado na tela, mas nos passos abaixo você precisará usar o ID da pergunta.

Mas, atenção, uma pergunta só pode ser alterada se ela ainda não tiver sido respondida. Se já houver alguma resposta, o usuário não poderá alterá-la. Se necessário, ele poderá apenas enviar comentários para esclarecer ou corrigir a sua pergunta. Como ainda não criamos as respostas, teremos que deixar esse teste para ser implementado em uma próxima etapa.

Então vamos aos passos desta operação:

1. Obter a lista de IDs de perguntas na Árvore B+ usando o ID do usuário;
2. Para cada ID nessa lista,
    1. Obter os dados da pergunta usando o método read(ID) do CRUD;
    2. Se a pergunta estiver ativa, apresentar os seus dados na tela.
3. Solicitar do usuário o número da pergunta que deseja alterar;
    * Se o usuário digitar 0, retornar ao menu de perguntas;
4. Usando o ID da pergunta escolhida, recuperar os dados da pergunta usando o método read(ID) do CRUD;
5. Apresentar os dados da pergunta na tela;
6. Solicitar a nova redação da pergunta ;
    * Se o usuário deixar esse campo em branco, retornar ao menu de perguntas;
7. Solicitar a confirmação de alteração ao usuário;
    * Se o usuário não confirmar a alteração, voltar ao menu de perguntas;
8. Alterar os dados da pergunta por meio do método update() do CRUD;
9. Apresentar mensagem de confirmação da alteração;
10. Voltar ao menu de perguntas.

Como você deve ter observado, não demos a opção de alteração do ID de pergunta, do ID de usuário ou do estado. IDs nunca são alterados. Assim, podemos alterar todos os outros dados, mas não o seu ID. Quanto ao ID do usuário, não demos a opção de sua alteração, porque cada usuário só pode gerenciar as suas próprias perguntas. Finalmente, o estado da pergunta permanece como ativo e não há por que alterá-lo manualmente.

### Arquivamento
Como existem muitas relações das perguntas com outras entidades (respostas, votos e comentários) que foram criadas por outros usuários, não faremos a exclusão de perguntas. Me parece meio agressivo apagar dados criados por outros usuários, mesmo que em resposta às minhas próprias perguntas. Assim, permitiremos apenas o arquivamento das perguntas. O processo parece com o de exclusão, mas, ao invés de apagar os dados, apenas muda o estado do atributo `ativa`.

Assim, a sequência de passos será:

1. Obter a lista de IDs de perguntas na Árvore B+ usando o ID do usuário;
2. Para cada ID nessa lista,
    1. Obter os dados da pergunta usando o método read(ID) do CRUD;
    2. Se a pergunta estiver ativa, apresentar os seus dados na tela.
3. Solicitar do usuário o número da pergunta que deseja arquivar;
    * Se o usuário digitar 0, retornar ao menu de perguntas;
4. Usando o ID da pergunta escolhida, recuperar os dados da pergunta usando o método read(ID) do CRUD;
5. Apresentar os dados da pergunta na tela;
6. Solicitar a confirmação de arquivamento ao usuário;
    * Se o usuário não confirmar o arquivamento, voltar ao menu de perguntas;
7. Arquivar a pergunta por meio do método update() do CRUD, mudando apenas o valor do atributo `ativa` para `false`.
8. Apresentar mensagem de confirmação do arquivamento;
9. Voltar ao menu de perguntas.

O arquivamento será definitivo, isto é, não será possível desarquivar uma pergunta.

# O QUE DEVE SER NESTA ETAPA

O seu grupo deve implementar:

* A classe Pergunta;
* O CRUD de perguntas;
* A implementação da opção 1 do menu principal do programa, com as operações de listagem, inclusão, alteração e arquivamento de perguntas (esses códigos precisarão de uma Árvore B+).
