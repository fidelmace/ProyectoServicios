pipeline {
    agent any
    environment {
       GIT_SSL_NO_VERIFY=true
       LOCAL_SERVER= '192.168.100.116'
//       MODE = 'dev'
    }
    tools {
        maven 'M3_8_2'
    }
    stages {
        stage('Build and Analize') {
            steps {
                dir('microservicio-service/'){
                    echo 'Execute Maven and Analizing with SonarServer'
                    withSonarQubeEnv('SonarServer') {
                         sh "mvn clean package \
                            -Dsonar.projectKey=21_MyCompany_Microservice \
                            -Dsonar.projectName=21_MyCompany_Microservice \
                            -Dsonar.sources=src/main \
                            -Dsonar.coverage.exclusions=**/*TO.java,**/*DO.java,**/curso/web/**/*,**/curso/persistence/**/*,**/curso/commons/**/*,**/curso/model/**/* \
                            -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
                            -Djacoco.output=tcpclient \
                            -Djacoco.address=127.0.0.1 \
                            -Djacoco.port=10001" 
                    }
                }
            }
        }
// Se crea conexión para que SONAR nos avise del resultado en caso de que no pase la compilación, se tiene que indicar pasos a seguir 
//http://192.168.100.116:9000/ local sonaque

/*
        stage ('Quality Gate') {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: false //false si pasaria pero con errores
                }
            }
        }
*/

        stage('Container Build') {
            steps {
                dir('microservicio-service/'){
                    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'dockerhub_id', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
                        sh 'docker login -u $USERNAME -p $PASSWORD'
                        sh 'docker build -t microservicio-service .'
                    }
                }
            }
        }
        stage('Container Push Nexus') {
            steps {
                    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'dockernexus_id', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
                        sh 'docker login ${LOCAL_SERVER}:8083 -u $USERNAME -p $PASSWORD'
                        sh 'docker tag microservicio-service:latest ${LOCAL_SERVER}:8083/repository/docker-private/microservicio_nexus:dev'
                        sh 'docker push ${LOCAL_SERVER}:8083/repository/docker-private/microservicio_nexus:dev'
                }
            }
        }

        stage('Container Run') {
            steps {
                sh 'docker stop microservicio-one || true'  // valida que el microservicio-one exista  y No truene cuando no exista
                //sh 'docker run -d --rm --name microservicio-one -p 8090:8090 microservicio-service'
                sh 'docker run -d --rm --name microservicio-one -p 8090:8090 ${LOCAL_SERVER}:8083/repository/docker-private/microservicio_nexus:dev'

            }
        }
    }
}