deploy:
	npm run build
	rsync -avz --delete ./build/ 78.47.154.190:/var/www/vhosts/bornholdt.io/drasyl.network/
