to merge your branch into main:
git checkout main
git pull origin main
git merge my-feature-branch



# to install and run prettier localy:
npm install prettier-plugin-java --save-dev 

npx prettier --write "**/*.java"
ou
npm run format:java

npx prettier --check .
npx prettier --write .

# install and use husky
npm install --save-dev husky
npx husky init

# qulice

mvnw.cmd qulice:check -X // shows the most detailed output including each violation.
mvnw.cmd qulice:check -e // shows the full error stack with violation details at the end

https://github.com/jhipster/prettier-java



TO DO:
add more tests

errors in bdd ?







task : 
choco install go-task