# tmpio
A simple groovy library providing an easy way for creating filesystem structures. It should be very usfull when writing unit tests for filesystem dependend functions (e.g. directory scanner, file manager, build system).

This project arised out of boredom and will not come with any kind of support and regular updates.

### creating a file tree
When creating a file tree you are using a very simple definition language called `FsLang
```
Content 	<- Files? Directory (';' Directory)*
Directory 	<- Name ('(' Content ')')?
Files 		<- '[' Name (',' Name) ']'
Name 		<- [a-zA-Z0-9-_]*
``` 
Here a quick example:
```groovy
def tmp = new TmpDir(root: '/tmp/tmpio/')
tmp.create "[app.conf,db.json]bin([run.sh,uninstall.exe]);src(main(groovy([main.groovy]));test(groovy))"
```

### deleting the tree
ATM the only option for deleting the file tree on the HD is via manually calling the cleanup method on a TmpDir instance:
```groovy
tmp.cleanup()
```
It uses the java.nio `FileVisitor interface to execute this task as effecient as possible.

### possible upcomming features
If I or someone else finds the time here are some possible improvements to tmpio:
* additional cleanup strategies (on exit, by Timer, by GC?)
* integrating popular persisting frameworks (like Gson) to provide a quick way of writing content to the files (e.g. `tmp["db.json"] << Db.instance()`)
* implementing FsLang as embedded DSL for Groovy

### first prototype of file content api
I've started working on the file content API (issue #1) in the file-content-api branch. It already provides all the writing functionalities for strings and a first json persistence function.
Here a little excerpt:
```groovy
def dir = new TmpDir()
dir.create "[foo.txt,bar.txt,c.csv]"
dir.find /.*\.txt/ << {
    // generate and return content as String
}
```
