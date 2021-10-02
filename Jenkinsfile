pipeline {
    agent any
    environment {
       GIT_SSL_NO_VERIFY=true
       LOCAL_SERVER= '192.168.100.116'
//       MODE = 'dev'
    }
    tools {
        maven 'M3_8_2'
        nodejs 'NodeJs12'
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
        stage('Frontend') {
            steps {
                echo 'Building Frontend'
                dir('frontend/'){
                    sh 'npm install'
                    sh 'npm run build'
                    sh 'docker stop frontend-one || true'
                    sh "docker build -t frontend-web ."
                    sh 'docker run -d --rm --name frontend-one -p 8010:80 frontend-web'
                }
            }
        }

        stage('Database') {
            steps {
                dir('liquibase/'){
                    sh '/opt/liquibase/liquibase --version'
                    sh '/opt/liquibase/liquibase --changeLogFile="changesets/db.changelog-master.xml" update'
                    echo 'Applying Db changes'
                }
            }
        }

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
                sh 'docker run -d --rm --name microservicio-one -e SPRING_PROFILES_ACTIVE=qa -p 8090:8090 ${LOCAL_SERVER}:8083/repository/docker-private/microservicio_nexus:dev'
                //sh 'docker run -d --rm --name microservicio-one -e SPRING_PROFILES_ACTIVE=dev -p 8090:8090 ${LOCAL_SERVER}:8083/repository/docker-private/microservicio_nexus:dev'

            }
        }

        stage('Testing') {
            steps {
                dir('Cypress/') {
                    //sh 'docker build -t cypressfront .'
                    //sh 'docker run cypressfront'
                    // Lo de arriba funciona en caso de l error
                    sh 'docker run --rm --name Cypress -v "//C/Users/fmacedoniom/git/cursomicroservicios2021/ProyectoServicios/jenkins_home/workspace/MicroservicioTruper/Cypress:/e2e" -w /e2e -e Cypress cypress/included:3.4.0'
                }
            }
        }

        stage('tar videos') 
        {
            steps 
            {
                dir('cypress/cypress/videos/') {
                    sh 'tar -cvf videos.tar .'
                    archiveArtifacts artifacts: 'videos.tar',
                    allowEmptyArchive: true
                }
            }
        }

        stage('Estress') {
            steps {
                dir('Gatling/') {
                    sh 'mvn gatling:test'
                }
            }
            post {
                always {
                    gatlingArchive()
                }
            }
        }


    }

    //despues de todos los stages la mejor practica es eliminar todo 
    //POST
    post {
        always {
            echo 'I succeeeded POST!'
            //deleteDir()
        }
        success {
            echo 'I succeeeded!'
        }
        unstable {
            echo 'I am unstable :/'
        }
        failure {
            echo 'I failed :('
        }
        changed {
            echo 'Things were different before...'
        }
    }

}