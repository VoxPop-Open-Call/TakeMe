# TakeMe by Famility project
Mobile first companion webapp for itinerary monitors

(example for test environment, project famility-takeme-test)

## Release build to deploy

1. make sure you are in the lastest develop commit
2. make sure you have no local changes to the files
3. use Node 14.21.3
4. use Cordova CLI 11.0.0
5. delete the following folders, if there: node_modules, platforms, plugins, www
6. npm i
7. ionic cordova platform add browser --confirm --no-interactive
8. ionic cordova build browser --release --confirm
9. ionic cordova build browser --release --configuration=test --confirm

## Deploying to firebase hosting

1. check if firebase CLI is installed in your machine; if not: npm install -g firebase-tools
2. in order to use firebase CLI, Node might need a higher version than 14.21.3
3. check if you are logged in: firebase login
4. check the firebase projects list and the one selected as 'current': firebase projects:list
5. select the correct project if it's not selected: firebase use famility-takeme-test
6. firebase deploy
7. use the link in the log to go to the firebase project console, check if the deployment is there
8. use the link in the log to go to the hosting URL, check if the requests are pointing to the correct backend environment
