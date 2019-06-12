#!/bin/bash
sudo apt-get update

sudo debconf-set-selections <<< 'mysql-server mysql-server/root_password password ${db_password}'
sudo debconf-set-selections <<< 'mysql-server mysql-server/root_password_again password ${db_password}'
sudo apt-get -y install mysql-server
mysql -u root '-p${db_password}' -e 'CREATE DATABASE ${db_name}'
