# Contributing to Hipster4j

First of all, thank you so much for being interested in contributing to Hipster4j!. This document will guide you through this process. You can contribute in different ways:

- Reporting issues
- Fixing bugs or developing new features
- Creating new examples
- Sending a motivating email telling us how do you like the library (or dislike) :)

## Issues
Feel free to open new issues or participating in the discussion of the existing ones on 
[this repository](https://github.com/citiususc/hipster/issues), but before doing so, please make sure that the issue is not duplicated and/or the discussion is related to the topic of the issue.

## Pull requests
Code contributions are welcome following a process which guarantees the long-term maintainability of the project. 
You can contribute either with bugfixes or new features. Before submitting a new feature, we highly encourage you to first open a new issue describing its motivation and details and discuss it with one of the project mantainers. This will ensure that the feature fits well in the project.

### Step 1: Open a new issue (if not opened yet)
Before starting to code, it is desirable to first open an issue describing the bug or the new feature. Please be sure the issue is not duplicated.

### Step 2: Fork the repository
Fork the project https://github.com/citiususc/hipster into your account. Then, check out your copy of the project locally.
```
git clone git@github.com:username/hipster.git
cd hipster
git remote add upstream https://github.com/citiususc/hipster.git
```

### Step 3: Create a new feature branch `contrib/issue-number`
Put your code in a new feature branch. The name of the new branch should start with `contrib/`. This convention will help us to keep track of future changes from pull requests.
```
git checkout -b contrib/issue-number origin/branch
```
Note that origin/‘branch’ would correspond with any of the current development branches (for example 1.0.X) but never the origin/master branch. For example, suppose that the latest version of the project is v1.0.0 and you want to fix a new bug that you discovered in this version. If the new reported issue has an id, say, #186, then you would create your feature branch in this way:
```
git checkout -b contrib/issue-186 origin/1.0.X
```

### Step 4: Committing your changes
First of all, make sure that git is configured with your complete name and email address. It is desirable to use the same email of your Github account, this will help to identify the contributions:
```
git config --global user.name "Your Name"
git config --global user.email "your@email.com"
```
Write a good commit message. It should describe the changes you made and its motivation. Be sure to reference the issue you are working in the commit that finishes your contribution using one the [keywords to close issues in Github](https://help.github.com/articles/closing-issues-via-commit-messages/).
If your commit message is too long, try to summarize the changes in the header of the message, like this:
```
fix #xx : summarize your commit in one line

If needed, explain more in detail the changes introduced in your 
commit and the motivation. You could introduce some background 
about the issue you worked in. 

This message can contain several paragraphs and be as long as 
you need, but try to do a good indentation: the columns should 
be shorter than 72 characters and with a proper word-wrap. 
The command `git log` will print this complete text in a nice 
way if you format it properly.
```
The header and the body of the commit message must be separated by a line in blank. The header is the message shown when running the command `git shortlog`.

#### Keep your branch in sync
Remember to keep in sync your version. Use git rebase instead of git merge to bring all the changes from the upstream branch to your feature branch:

```
git fetch upstream
git rebase upstream/branch #where branch would be 1.0.X, 1.1.X etc
```

#### Test your code
Verify that your changes are actually working by adding the required unit tests. It is desirable to include unit test covering all new features you implement. Also, if you find a bug which is not currently detected by the unit tests you might consider to implement a new one or modify the current implementation. After this, you can verify that everything works fine after your changes with:

```
mvn clean test
```

### Step 5: Push your changes

Push your changes to your forked project with:
```
git push origin my-feature-branch
```

### Step 6: Create and submit a pull request
Go to your forked project on GitHub, select your feature branch and click the “Compare, review, create a pull request button”. After that, we will review your pull request in a few days (hopefully!), but if we delay please be patient :). We do our best in our spare time to keep the project updated, but unfortunately there may be some periods of time in which we simply can’t work in the project.



### License Agreement
By contributing your code, you agree to license your contribution under the terms of the [Apache 2.0 license](https://raw.githubusercontent.com/citiususc/hipster/4ca93e681ad7335acbd0bea9e49fe678d56f3519/LICENSE).

Also, remember to add this header to each new file that you’ve created:

```
/*
* Copyright 2015 Centro de Investigación en Tecnoloxías da Información (CITIUS), 
* University of Santiago de Compostela (USC).
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
```

That’s all!
