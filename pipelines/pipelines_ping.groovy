pipeline {

  agent any

options {
ansiColor("xterm")
}

stages {

    stage ('Clonage repo GIT') {
      steps {
      checkout([
                $class: 'GitSCM',
                branches: [[name: '*/Develop']],
                doGenerateSubmoduleConfigurations: false,
                extensions: [],
                submoduleCfg: [],
                userRemoteConfigs: [[credentialsId: 'git', url: 'https://github.com/PercheyRenaud/SoutenanceDevOPs-v01']]
              ])
      }
    }

    stage ('MODELE TEST') {
      steps {
          sh 'echo "test"'
      }
    }




    stage ('Installation de mediawiki') {
        environment {
          ANSIBLE_FORCE_COLOR = true
        }
        steps {
          ansiblePlaybook (
            vaultCredentialsId: '1cb0cef4-ed37-48da-a9e7-5dc68ac27f95',
            colorized: true,
            playbook: 'installationroles.yml',
            tags: 'deversement',
            tags: 'prerequis',
            inventory: 'inventories/hosts',
            extras: '${VERBOSE}'
          )
        }
      }

}
}
