@Library ('folio_jenkins_shared_libs') _

pipeline {

  environment {
    ORG_GRADLE_PROJECT_appName = 'mod-licenses'
    GRADLEW_OPTS = '--console plain --no-daemon'
    BUILD_DIR = "${env.WORKSPACE}/service"
    MD = "${env.WORKSPACE}/service/build/resources/main/okapi/ModuleDescriptor.json"
    doKubeDeploy = true
  }

  options {
    timeout(30)
    buildDiscarder(logRotator(numToKeepStr: '30'))
  }

  agent {
    node {
      label 'jenkins-slave-all'
    }
  }

  stages {
    stage ('Setup') {
      steps {
        dir(env.BUILD_DIR) {
          script {
            def foliociLib = new org.folio.foliociCommands()
            def gradleVersion = foliociLib.gradleProperty('appVersion')

            env.name = env.ORG_GRADLE_PROJECT_appName

            // if release
            if ( foliociLib.isRelease() ) {
              // make sure git tag and version match
              if ( foliociLib.tagMatch(gradleVersion) ) {
                env.isRelease = true
                env.dockerRepo = 'folioorg'
                env.version = gradleVersion
              }
              else {
                error('Git release tag and Maven version mismatch')
              }
            }
            else {
              env.dockerRepo = 'folioci'
              env.version = "${gradleVersion}-SNAPSHOT.${env.BUILD_NUMBER}"
            }
          }
        }
        sendNotifications 'STARTED'
      }
    }

    stage('Lint raml-cop') {
      steps {
        runLintRamlCop()
      }
    }

    stage('Gradle Build') {
      steps {
        dir(env.BUILD_DIR) {
          sh "./gradlew $env.GRADLEW_OPTS -PappVersion=${env.version} assemble"
        }
      }
    }

    stage('Build Docker') {
      steps {
        dir(env.BUILD_DIR) {
          sh "./gradlew $env.GRADLEW_OPTS -PappVersion=${env.version} -PdockerRepo=${env.dockerRepo} buildImage"
        }
        // debug
        sh "cat $env.MD"
      }
    }

    stage('Publish Docker Image') {
      when {
        anyOf {
          branch 'master'
          expression { return env.isRelease }
        }
      }
      steps {
        script {
          docker.withRegistry('https://index.docker.io/v1/', 'DockerHubIDJenkins') {
            sh "docker tag ${env.dockerRepo}/${env.name}:${env.version} ${env.dockerRepo}/${env.name}:latest"
            sh "docker push ${env.dockerRepo}/${env.name}:${env.version}"
            sh "docker push ${env.dockerRepo}/${env.name}:latest"
          }
        }
      }
    }

    stage('Publish Module Descriptor') {
      when {
        anyOf {
          branch 'master'
          expression { return env.isRelease }
        }
      }
      steps {
        script {
          def foliociLib = new org.folio.foliociCommands()
          foliociLib.updateModDescriptor(env.MD)
        }
        postModuleDescriptor(env.MD)
      }
    }

    stage('Lint raml schema') {
      steps {
        runLintRamlSchema()
      }
    }

    stage('Publish API Docs') {
      when {
        branch 'master'
      }
      steps {
        sh "python3 /usr/local/bin/generate_api_docs.py -r ${env.name} -l info -o folio-api-docs"
        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding',
                          accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                          credentialsId: 'jenkins-aws',
                          secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
          sh 'aws s3 sync folio-api-docs s3://foliodocs/api'
        }
      }
    }

    stage('Kubernetes Deploy'){
      when {
        branch 'master'
        expression { return env.doKubeDeploy }  
      }
      steps {
        echo "Deploying to kubernetes cluster"
        kubeDeploy('folio-default',
                  "[{" +
                    "\"name\" : \"${env.name}\"," +
                    "\"version\" : \"${env.version}\"," +
                    "\"deploy\":true" +
                  "}]")
      }
    }

  } // end stages

  post {
    always {
      dockerCleanup()
      sendNotifications currentBuild.result
    }
  }
}


