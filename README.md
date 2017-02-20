# SBT Scaladoc Settings Plugin

Provides means to configure scaladoc settings for sbt.

If one is used to javadoc generation then the scaladoc is a rather different mechanism.  
First off there is no _overview.html_ like with javadoc.  
One has to specify the path to any root document file using manual settings.    
Secondarily there seems to be no way to included static content, such as images. 
 
### Adding root/overview documentation
This is a fully working example how to add a root file to the doc generation. 
E.g.
```scala
scalacOptions in (Compile, doc) ++= Seq("-doc-root-content", baseDirectory.value+"/src/main/scaladoc/overview.txt") 
```

However to facilitate a common way to manage the overview file one can specify it using.  
 This will yield the exact same result as the example above.
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
A simple remedy is to include the _copyDocAssetsTask_ into the build.sbt.  
The example below attaches the task to the _doc_ execution.  
It will recursively copy all the _doc-file_ directories it finds under the _scaladoc_ directory of your source directory to the API output target.

```scala
copyDocAssetsTask <<= copyDocAssetsTask triggeredBy (doc in Compile)
```

###Live Example
Check out the [build.sbt](../master/build.sbt) for this project to see the usage of the plugin.

###Installing the plugin
Simply add this to the _plugins.sbt_ file:
```scala
addSbtPlugin("org.dmonix.sbt" % "sbt-scaladoc-settings-plugin" % "0.5")
```
