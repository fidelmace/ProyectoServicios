pipeline {
    agent any
    environment {
       GIT_SSL_NO_VERIFY=true
       LOCAL_SERVER= '192.168.100.116'
        // LOCAL_SERVER= '192.168.100.116'
//       MODE = 'dev'
    }
    tools {
        maven 'M3_8_2'
        nodejs 'NodeJs12'
    }
    stages {
        stage('Build and Analize') {
            when {
                anyOf {
                    changeset "*microservicio-service/**"
                    expression { currentBuild.previousBuild.result != "SUCCESS"}
                }
            }
            steps {
                dir('microservicio-service/'){
                    echo 'Execute Maven and Analizing with SonarServer'
                    withSonarQubeEnv('SonarServer') {
                         sh "mvn clean package -DskipTests \
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

        stage('Build and Analize micro2') {
            when {
                    anyOf {
                        changeset "*microservicio-service-two/**"
                        expression { currentBuild.previousBuild.result != "SUCCESS"}
                    }
                }
                steps {
                    dir('microservicio-service-two/'){
                        echo 'Execute Maven and Analizing with SonarServer'
                        withSonarQubeEnv('SonarServer') {
                            sh "mvn clean package \
                                -Dsonar.projectKey=21_MyCompany_Microservice-two \
                                -Dsonar.projectName=21_MyCompany_Microservice-two \
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

/*
        stage('Frontend') {
            when {
                anyOf {
                    changeset "*frontend/**"
                    expression { currentBuild.previousBuild.result != "SUCCESS"}
                }
            }
            steps {
                echo 'Building Frontend'
                dir('frontend/'){
                    sh 'npm install'
                    sh 'npm run build'
                    sh 'npm config ls'
                    sh 'docker stop frontend-one || true'
                    sh "docker build -t frontend-web ."
                    sh 'docker run -d --rm --name frontend-one -p 8010:80 frontend-web'
                }
            }
        }
*/

/* esta OK 
        stage('Database') {
            when {
                anyOf {
                    changeset "*liquibase/**"
                    expression { currentBuild.previousBuild.result != "SUCCESS"}
                }
            }

            steps {
                dir('liquibase/'){
                    sh '/opt/liquibase/liquibase --version'
                    sh '/opt/liquibase/liquibase --changeLogFile="changesets/db.changelog-master.xml" update'
                    echo 'Applying Db changes'
                }
            }
        }
*/
        stage('Container Build') {
            when {
                anyOf {
                    changeset "*microservicio-service/**"
                    expression { currentBuild.previousBuild.result != "SUCCESS"}
                }
            }

            steps {
                dir('microservicio-service/'){
                    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'dockerhub_id', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
                        sh 'docker login -u $USERNAME -p $PASSWORD'
                        sh 'docker build -t microservicio-service .'
                    }
                }
            }
        }

        stage('Container Build micro2') {
            when {
                anyOf {
                    changeset "*microservicio-service-two/**"
                    expression { currentBuild.previousBuild.result != "SUCCESS"}
                }
            }

            steps {
                dir('microservicio-service-two/'){
                    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'dockerhub_id', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
                        sh 'docker login -u $USERNAME -p $PASSWORD'
                        sh 'docker build -t microservicio-service-two .'
                    }
                }
            }
        }

        stage('Zuul') {
            /*
             when {
                anyOf {
                    changeset "*ZuulBase/**"
                    expression { currentBuild.previousBuild.result != "SUCCESS"}
                }
             }
             */
            steps {
                dir('ZuulBase/'){
                    sh 'mvn clean package'
                    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'dockerhub_id', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
                        sh 'docker login -u $USERNAME -p $PASSWORD'
                        sh 'docker build -t zuul .'
                        sh 'docker stop zuul-service || true'
                        sh 'docker run -d --rm --name zuul-service -p 8000:8000 zuul'
                    }
                }
            }
        }
        stage('Eureka') {
     /*
             when {
                anyOf {
                    changeset "*EurekaBase/**"
                    expression { currentBuild.previousBuild.result != "SUCCESS"}
                }
             }
             */
            steps {
                dir('EurekaBase/'){
                    sh 'mvn clean package'
                    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'dockerhub_id', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
                        sh 'docker login -u $USERNAME -p $PASSWORD'
                        sh 'docker build -t eureka .'
                        sh 'docker stop eureka-service || true'
                        sh 'docker run -d --rm --name eureka-service -p 8761:8761 eureka'
                    }
                }
            }
        }
/*
        stage('Container Push Nexus') {
            steps {
                    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'dockernexus_id', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
                        sh 'docker login ${LOCAL_SERVER}:8083 -u $USERNAME -p $PASSWORD'
                        sh 'docker tag microservicio-service:latest ${LOCAL_SERVER}:8083/repository/docker-private/microservicio_nexus:dev'
                        sh 'docker push ${LOCAL_SERVER}:8083/repository/docker-private/microservicio_nexus:dev'
                }
            }
        }
*/

       stage('Container Run') {
           /*
            when {
                anyOf {
                    changeset "*microservicio-service/**"
                    expression { currentBuild.previousBuild.result != "SUCCESS"}
                }
            }
            */         
            steps {
                sh 'docker stop microservicio-one || true'  // valida que el microservicio-one exista  y No truene cuando no exista
                //Con la linea de abajo levantas solo una replica 
                sh 'docker run -d --rm --name microservicio-one -e SPRING_PROFILES_ACTIVE=qa  microservicio-service'
                // con la linea de abajo se va a crear 2 replicas 
                sh 'docker stop microservicio-one-two || true'
                sh 'docker run -d --rm --name microservicio-one-two -e SPRING_PROFILES_ACTIVE=qa  microservicio-service'

                //sh 'docker run -d --rm --name microservicio-one -e SPRING_PROFILES_ACTIVE=qa -p 8090:8090 ${LOCAL_SERVER}:8083/repository/docker-private/microservicio_nexus:dev'
                //sh 'docker run -d --rm --name microservicio-one -e SPRING_PROFILES_ACTIVE=dev -p 8090:8090 ${LOCAL_SERVER}:8083/repository/docker-private/microservicio_nexus:dev'
            }
        }

        stage('Container Run micro2') {
            steps {
            //Agregamos el segundo microservicio
                sh 'docker stop microservicio-two || true'  // valida que el microservicio-one exista  y No truene cuando no exista
                sh 'docker run -d --rm --name microservicio-two -e SPRING_PROFILES_ACTIVE=qa  microservicio-service-two'
                // con la linea de abajo se va a crear 2 replicas 
                sh 'docker stop microservicio-two-two || true'
                sh 'docker run -d --rm --name microservicio-two-two -e SPRING_PROFILES_ACTIVE=qa  microservicio-service-two'
            }
        }

/*
        stage('Container Run') {
            steps {
                sh 'docker stop microservicio-one || true'  // valida que el microservicio-one exista  y No truene cuando no exista
                //sh 'docker run -d --rm --name microservicio-one -p 8090:8090 microservicio-service'
                sh 'docker run -d --rm --name microservicio-one -e SPRING_PROFILES_ACTIVE=qa -p 8090:8090 ${LOCAL_SERVER}:8083/repository/docker-private/microservicio_nexus:dev'
                //sh 'docker run -d --rm --name microservicio-one -e SPRING_PROFILES_ACTIVE=dev -p 8090:8090 ${LOCAL_SERVER}:8083/repository/docker-private/microservicio_nexus:dev'

            }
        }
*/


/*
        stage('Testing') {
            steps {
                dir('Cypress/') {
                    //sh 'docker build -t cypressfront .'
                    //sh 'docker run cypressfront'
                    // Lo de arriba funciona en caso de l error
                    // la linea de abajo esta ok para windows  y esta OK 
                    //sh 'docker run --rm --name Cypress -v "/C/Users/fmacedoniom/git/cursomicroservicios2021/ProyectoServicios/jenkins_home/workspace/MicroservicioTruper/Cypress:/e2e" -w /e2e -e Cypress cypress/included:3.4.0'
                    sh 'docker run --rm --name Cypress -v /vagrant_data/jenkins_home/workspace/MicroservicioTruper/Cypress:/e2e -w /e2e -e Cypress cypress/included:3.4.0'

                }
            }
        }
*/


/*
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
*/

/*
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
*/

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