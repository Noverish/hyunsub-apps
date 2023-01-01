#!/bin/bash

ROOT_PATH=`pwd`
CERT_PATH=$ROOT_PATH/cert
APPS_CONF_PATH=$ROOT_PATH/apps-local.conf
AUTH_CONF_PATH=$ROOT_PATH/../hyunsub-auth/etc/auth-local.conf
VIDEO_CONF_PATH=$ROOT_PATH/../hyunsub-video/etc/video-local.conf
PHOTO_CONF_PATH=$ROOT_PATH/../hyunsub-photo/etc/photo-local.conf
ENCODE_CONF_PATH=$ROOT_PATH/../hyunsub-encode/etc/encode-local.conf
APPAREL_CONF_PATH=$ROOT_PATH/../hyunsub-apparel/etc/apparel-local.conf
DRIVE_CONF_PATH=$ROOT_PATH/../hyunsub-drive/etc/drive-local.conf
COMIC_CONF_PATH=$ROOT_PATH/../hyunsub-comic/etc/comic-local.conf
NGINX_IMG=nginx:1.23.1

docker stop hyunsub_nginx || true

docker run -d --rm -p 80:80 -p 443:443 \
  --name hyunsub_nginx \
  --platform linux/amd64 \
	-v $CERT_PATH:/etc/nginx/cert \
	-v $APPS_CONF_PATH:/etc/nginx/conf.d/apps-local.conf \
	-v $AUTH_CONF_PATH:/etc/nginx/conf.d/auth-local.conf \
	-v $VIDEO_CONF_PATH:/etc/nginx/conf.d/video-local.conf \
	-v $PHOTO_CONF_PATH:/etc/nginx/conf.d/photo-local.conf \
	-v $ENCODE_CONF_PATH:/etc/nginx/conf.d/encode-local.conf \
	-v $APPAREL_CONF_PATH:/etc/nginx/conf.d/apparel-local.conf \
	-v $DRIVE_CONF_PATH:/etc/nginx/conf.d/drive-local.conf \
	-v $COMIC_CONF_PATH:/etc/nginx/conf.d/comic-local.conf \
	$NGINX_IMG
