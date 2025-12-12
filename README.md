to merge your branch into main:
git checkout main
git pull origin main
git merge my-feature-branch



# to install and run prettier localy:
npm install prettier-plugin-java --save-dev 

npx prettier --write "**/*.java"
ou
npm run format:java

# install and use husky
npm install --save-dev husky
npx husky init

# qulice

mvnw.cmd qulice:check -X

https://github.com/jhipster/prettier-java