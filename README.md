# SBT Scaladoc Settings Plugin

Provides means to configure scaladoc settings for sbt.

If one is used to javadoc generation then the scaladoc is a rather different mechanism.  
First off there is no _overview.html_ like with javadoc.  
One has to specify the path to any root document file using manual settings.    
Secondarily there seems to be no way to included static content, such as images.  
This plugin will provide the same mechanism as the Javadoc.  
Added to that the plugin can also parse [PlantUML](http://plantuml.com/) files and render images out of these.  
This can be extremely useful for creating a richer documentation with e.g. class/sequence diagrams.

###How does it work?
First off the plugin does expect some standardized layout of the project.  
E.g. given below:

```
[project-root]/
  main/
   scala/
   scaladoc/
       doc-files/
         flowchart.puml
         flowchart.txt
       overview.txt
```
The _scaladoc_ directory maps to the _javadoc_ directory one might be used to from generating Javadoc.  
The _overview.txt_ maps to the _overview.html_ used in Javadoc.  
This is where you place project root documentation that is not specific to any package.  
   
The _doc-files_ directory is meant for static content such as images and possible static html pages you want to link to.  
The files in this directory will automatically be copied to the same output directory of the generated API docs.  
With **one** exception, any files called _.puml_ will be treated as PlantUML files and will automatically be rendered into _png_ images.  
This is explained in a later section of the documentation.
### Adding root/overview documentation
This is a fully working example how to add a root file to the doc generation.  
E.g.
```scala
scalacOptions in (Compile, doc) ++= Seq("-doc-root-content", baseDirectory.value+"/src/main/scaladoc/overview.txt") 
```

However to facilitate a common way to manage the overview file one can specify it using _ScalaDocSettings.rootDoc_.  
 This will yield the exact same result as the example above, i.e it will  expect the file _src-dir/scaladoc/overview.txt_
```scala
scalacOptions in (Compile, doc) ++= org.dmonix.sbt.ScalaDocSettings.rootDoc
```
Should you want to use some other file then simply specify it using:
```scala
scalacOptions in (Compile, doc) ++= org.dmonix.sbt.ScalaDocSettings.rootDoc("yourfilepath")
```
### Managing static resources (e.g. images)
Javadoc has a standard _doc-files_ directory which is automatically included during the javadoc generation.  
Thus allowing the documentation author to include static content such as images or even html files.  
Scaladoc seems to completely lack this functionality.     
This setting overrides the default sequence of files to add in _packageDoc_.  
It's needed in order for the copyDocAssetsTask task to execute.  
In practice it will execute the _copyDocAssetsTask_ and add the output to the already generated output by the _doc_ command.
```scala
mappings in (Compile, packageDoc) <<= copyDocAssetsTask
```

### Managing PlantUML diagrams
As an addon the plugin manages to automatically render [PlantUML](http://plantuml.com/) diagrams into _png_ files.
This feature is enabled by default and it will automatically convert any _.puml_ files it finds under any _doc-files_ directory.  
There's a few configuration options one can set in _build.sbt_ as illustrated below.  
If the default values (as below) are ok for you then you don't need to set anything.
```scala
processPlantUML := true //default value
plantUMLExtension := ".puml" //default value 
```

The output is automatically the name of the _puml_ file where the extension has been changed to _.png_.
So you can easily refer in to your PlantUML diagrams in your _overview.txt_ file.  
There's examples below.

### Generating the documentation
Simply run the _packageDoc_ task to render the documentation.  
```scala
sbt packageDoc
```
The output should be a jar file containing the documentation.  
This means the documentation is also rendered properly when e.g. publishing artifacts using the _publish_ and/or _release_ commands.
###Live Example
Check out the [build.sbt](../master/build.sbt) for this project to see the usage of the plugin.  
Also the [overview.txt](../master/src/main/scaladoc/overview.txt) for an example on how to refer to static files as well as PlantUML images rendered from the plugin.

###Installing the plugin
Simply add this to the _plugins.sbt_ file:
```scala
addSbtPlugin("org.dmonix.sbt" % "sbt-scaladoc-settings-plugin" % "0.6")
```
