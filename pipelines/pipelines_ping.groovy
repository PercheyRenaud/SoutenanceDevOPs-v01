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


    stage ('Installation de mediawiki_creation_FS') {
        environment {
          ANSIBLE_FORCE_COLOR = true
        }
        steps {
          ansiblePlaybook (
            vaultCredentialsId: '1cb0cef4-ed37-48da-a9e7-5dc68ac27f95',
            colorized: true,
            playbook: 'installationroles.yml',
            tags: 'creation_FS',
            inventory: 'inventories/hosts',
            extras: '${VERBOSE}'
          )
        }
      }

      stage ('Installation de mediawiki_creation_Maria_DB') {
          environment {
            ANSIBLE_FORCE_COLOR = true
          }
          steps {
            ansiblePlaybook (
              vaultCredentialsId: '1cb0cef4-ed37-48da-a9e7-5dc68ac27f95',
              colorized: true,
              playbook: 'installationroles.yml',
              tags: 'firewalld',
              inventory: 'inventories/hosts',
              extras: '${VERBOSE}'
            )
          }
        }

    stage ('Installation de mediawiki_prerequis') {
        environment {
          ANSIBLE_FORCE_COLOR = true
        }
        steps {
          ansiblePlaybook (
            vaultCredentialsId: '1cb0cef4-ed37-48da-a9e7-5dc68ac27f95',
            colorized: true,
            playbook: 'installationroles.yml',
            tags: 'dependencies',
            inventory: 'inventories/hosts',
            extras: '${VERBOSE}'
          )
        }
      }

    stage ('Installation de mediawiki_deversement') {
        environment {
          ANSIBLE_FORCE_COLOR = true
        }
        steps {
          ansiblePlaybook (
            vaultCredentialsId: '1cb0cef4-ed37-48da-a9e7-5dc68ac27f95',
            colorized: true,
            playbook: 'installationroles.yml',
            tags: 'deversement',
            inventory: 'inventories/hosts',
            extras: '${VERBOSE}'
          )
        }
      }

      stage ('Installation de mediawiki_newdb') {
          environment {
            ANSIBLE_FORCE_COLOR = true
          }
          steps {
            ansiblePlaybook (
              vaultCredentialsId: '1cb0cef4-ed37-48da-a9e7-5dc68ac27f95',
              colorized: true,
              playbook: 'installationroles.yml',
              tags: 'config_wikiDB',
              inventory: 'inventories/hosts',
              extras: '${VERBOSE}'
            )
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
                tags: 'install_mediawiki',
                inventory: 'inventories/hosts',
                extras: '${VERBOSE}'
              )
            }
          }

          stage ('Installation de mediawiki_config_apache2') {
              environment {
                ANSIBLE_FORCE_COLOR = true
              }
              steps {
                ansiblePlaybook (
                  vaultCredentialsId: '1cb0cef4-ed37-48da-a9e7-5dc68ac27f95',
                  colorized: true,
                  playbook: 'installationroles.yml',
                  tags: 'config_apache2',
                  inventory: 'inventories/hosts',
                  extras: '${VERBOSE}'
                )
              }
            }


    }
}
