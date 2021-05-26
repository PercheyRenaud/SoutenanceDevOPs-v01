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
            vaultCredentialsId: '07e2bbad-b754-45f3-b2dd-4effc623ee3f',
            colorized: true,
            playbook: 'installationroles.yml',
            inventory: 'inventories/hosts',
            extras: '${VERBOSE}'
          )
        }
      }

}
}
