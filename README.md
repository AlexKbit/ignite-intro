[![Build Status](https://travis-ci.org/AlexKbit/ignite-task-manager.svg?branch=master)](https://travis-ci.org/AlexKbit/ignite-intro)
[![codecov.io](https://codecov.io/github/AlexKbit/ignite-task-manager/coverage.svg?branch=master)](https://codecov.io/gh/AlexKbit/ignite-intro?branch=develop)

# Project Overview #

Simple project for solve of mathematical expressions with using Apache Ignite.
This project is designed to demonstrate the capabilities of Apache Ignite.

## Application architecture
<img alt="Application architecture" src="https://ndownloader.figshare.com/files/9567505/preview/9567505/preview.jpg">
The application uses the queue of the Ignite to accumulate the task queue.
You can run any number of nodes. Each node will register a service for processing tasks.

## User interface

<img alt="Application architecture" src="https://ndownloader.figshare.com/files/9567496/preview/9567496/preview.jpg">
Open application by root path.
1) Create new task with your expression
2) Wait a result

<img alt="Application architecture" src="https://ndownloader.figshare.com/files/9567499/preview/9567499/preview.jpg">
You can see a state of nodes in cluster on Node statistics page.

