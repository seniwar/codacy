
# Parse and store project commits

This repository contains working code that parses and stores the list of commits of a given GitHub repository.


## Pre-requisite
You need to have java installed on your system.


## Running the code from console (linux)
* Clone this repository:
```
   $ git clone https://github.com/seniwar/codacy.git
```


* Enter cloned repo and compile code: 
```
   $ cd codacy
   $ javac -d bin -cp "./bin;./libs/*" src/*/*.java
```
* Run code: 
```
   $ java -cp "./bin;./libs/*" main/CodacyExercise
```

**The console will ask for you to give an url as input.**
* Enter url from which you want to retrieve the commit list:
e.g.
```
   https://github.com/seniwar/justForTestRepo.git
```


**The Commit List is shown and serialized into referred path.**

To see the serialized data, run the program again and provide the same URL as before.