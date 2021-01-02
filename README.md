Trabalho final de Servless Architecture

Nome dos Integrantes do Grupo: 

* Aruna Fernanda Martins   	 	    – RM 338577 
* Ayrton Henrique Gomes Silva   	– RM 337089 
* Carlos Eduardo Roque da Silva     – RM 338866 
* Sara Regina Pires 	 	 		– RM 338142 
* Willian Yoshiaki Kazahaya 	 	– RM 338950 

#Fonte do Projeto

`https://github.com/AyrtonHenrique/fiap-aws-serverless`  


## Aplicativo AWS SAM para Cadastara Viagens

Este é um exemplo de aplicativo para demonstrar como construir um aplicativo no AWS Serverless Envinronment usando o
AWS SAM, Amazon API Gateway, AWS Lambda e Amazon DynamoDB.
Ele também usa a estrutura ORM do DynamoDBMapper para mapear itens de vigagem(Trip) em uma tabela do DynamoDB para uma API RESTful para gerenciar Viagens.


## Requisitos

* AWS CLI já configurado com pelo menos permissão PowerUser
* [Java SE Development Kit 8 instalado] (http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Docker instalado] (https://www.docker.com/community-edition)
* [Maven] (https://maven.apache.org/install.html)
* [SAM CLI] (https://github.com/awslabs/aws-sam-cli)
* [Python 3] (https://docs.python.org/3/)

## Processo de configuração

### Instalando dependências

Usamos `maven` para instalar nossas dependências e empacotar nosso aplicativo em um arquivo JAR:

bash 
`mvn install`	

### Desenvolvimento local

** Chamar função localmente por meio do API Gateway local **
1. Inicie o DynamoDB Local em um contêiner do Docker. `docker run -p 8000: 8000 -v $ (pwd) / local / dynamodb: / data / amazon / dynamodb-local -jar DynamoDBLocal.jar -sharedDb -dbPath / data`
2. Crie a tabela DynamoDB. `aws dynamodb create-table --table-name trip --attribute-definitions AttributeName=trip,AttributeType=S AttributeName=country,AttributeType=S AttributeName=date,AttributeType=S AttributeName=city,AttributeType=S AttributeName=reason,AttributeType=S --key-schema AttributeName=trip,KeyType=HASH AttributeName=date,KeyType=RANGE --local-secondary-indexes 'IndexName=countryIndex,KeySchema=[{AttributeName=trip,KeyType=HASH},{AttributeName=country,KeyType=RANGE}],Projection={ProjectionType=ALL}' 'IndexName=cityIndex,KeySchema=[{AttributeName=trip,KeyType=HASH},{AttributeName=city,KeyType=RANGE}],Projection={ProjectionType=ALL}' 'IndexName=reasonIndex,KeySchema=[{AttributeName=trip,KeyType=HASH},{AttributeName=reason,KeyType=RANGE}],Projection={ProjectionType=ALL}' --billing-mode PAY_PER_REQUEST  --endpoint-url http://localhost:8000`

Se a tabela já existe, você pode excluir: `aws dynamodb delete-table --table-name trip --endpoint-url http: // localhost: 8000`

3. Inicie a API local do SAM.
 - Em um Mac: `sam local start-api --env-vars src / test / resources / test_environment_mac.json`
 - No Windows: `sam local start-api --env-vars src / test / resources / test_environment_windows.json`
 - No Linux: `sam local start-api --env-vars src / test / resources / test_environment_linux.json`
 
 OBS: Se você já tem o contêiner localmente (no seu caso, o java8), você pode usar --skip-pull-image para remover o download

Se o comando anterior foi executado com sucesso, agora você deve ser capaz de atingir o seguinte endpoint local para
invocar as funções com raiz em :
* `http://localhost:3000/trip` - Criando uma Trip
* `http://localhost:3000/trip?start=<date>&end=<date>`. - Consultando Uma Trip por data
* `http://localhost:3000/trip/{Country}`. - Consultando Uma Trip por pais
* `http://localhost:3000/trip/{Country}?City=<cidade>`. - Consultando Uma Trip por pais e cidade


** SAM CLI ** é usado para emular Lambda e API Gateway localmente e usa nosso `template.yaml` para
entender como inicializar este ambiente (tempo de execução, onde está o código-fonte, etc.) - O
O trecho a seguir é o que a CLI lerá para inicializar uma API e suas rotas:


## Empacotamento e implantação

O tempo de execução do AWS Lambda Java aceita um arquivo zip ou um arquivo standalone JAR - usamos o último em
este exemplo. SAM usará a propriedade `CodeUri` para saber onde procurar tanto o aplicativo quanto
dependências:

Em primeiro lugar, precisamos de um `S3 bucket` onde podemos fazer o upload de nossas funções Lambda empacotadas como ZIP antes de
implantar qualquer coisa - Se você não tem um S3 bucket para armazenar artefatos de código, então este é um bom momento para
crie um:

`` `bash
export BUCKET_NAME=my_cool_new_bucket
aws s3 mb s3://$BUCKET_NAME
`` `

Em seguida, execute o seguinte comando para empacotar nossa função Lambda para S3:

`` `bash
pacote sam \
    --template-file template.yaml \
    --output-template-file packaged.yaml \
    --s3-bucket $ BUCKET_NAME
`` `

Em seguida, o comando a seguir criará um Cloudformation Stack e implantará seus recursos SAM.

`` `bash
sam implantar \
    --template-file packaged.yaml \
    --stack-name <YOUR_STACK> \
    --capabilities CAPABILITY_IAM
`` `

> ** Consulte [Guia de HOWTO do modelo de aplicativo sem servidor (SAM)] (https://github.com/awslabs/serverless-application-model/blob/master/HOWTO.md) para obter mais detalhes sobre como começar. **

Após a conclusão da implantação, você pode executar o seguinte comando para recuperar o URL do endpoint do gateway de API:

`` `bash
aws cloudformation describe-stacks \
    --stack-name sam-orderHandler \
    --query 'Pilhas []. Saídas'
`` `

# Apêndice

## Comandos AWS CLI

Comandos AWS CLI para empacotar, implantar e descrever saídas definidas na pilha de cloudformation:

`` `bash
pacote sam \
    --template-file template.yaml \
    --output-template-file packaged.yaml \
    --s3-bucket REPLACE_THIS_WITH_YOUR_S3_BUCKET_NAME

sam implantar \
    --template-file packaged.yaml \
    --stack-name sam-orderHandler \
    --capabilities CAPABILITY_IAM \
    --parameter-overrides MyParameterSample = MySampleValue

aws cloudformation describe-stacks \