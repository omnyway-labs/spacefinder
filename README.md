# spacefinder

A [re-frame](https://github.com/Day8/re-frame) /
[re-com](https://re-com.day8.com.au) application designed to recreate some or
all of the spacefinder client for demoing AWS Cognito auth / access control.

The goal is to have a clojurescript webapp (just the frontend )for showcasing authentication and
authorization patterns using Amazon Cognito, Amazon API Gateway, AWS Lambda, and
AWS IAM.

The AWS services definition and setup that this app should interop with as well
as the original working apps are at
https://github.com/awslabs/aws-serverless-auth-reference-app

## Development Mode

### Install shadow-cljs if it isn't not yet installed and other npm modules

```
npm install -g shadow-cljs
npm install
```

### Start Cider from Emacs:

Refer to the [shadow-cljs Emacs / CIDER documentation](https://shadow-cljs.github.io/docs/UsersGuide.html#cider).


### Compile css:

Compile css file once.

```
lein garden once
```

Automatically recompile css file on change.

```
lein garden auto
```

### Run application:

```
lein clean
shadow-cljs watch app
```

shadow-cljs will automatically push cljs changes to the browser.

Wait a bit, then browse to [http://localhost:8280](http://localhost:8280).

### Run tests:

Install karma and headless chrome

(But there aren't any tests, so don't bother!)

```
npm install -g karma-cli
```

And then run your tests

```
lein clean
lein run -m shadow.cljs.devtools.cli compile karma-test
karma start --single-run --reporters junit,dots
```
