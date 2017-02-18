# SBT Scaladoc Settings Plugin

Provides means to configure scaladoc settings for sbt.

If one is used to javadoc generation then the scaladoc is a rather different mechanism.  
First off there is no _overview.html_ like with javadoc.  
One has to specify the path to any root document file using
Secondarily there seems to be no way to included static content, such as images. 
 
### Adding root/overview documentation
E.g.
```scala
scalacOptions in (Compile, doc) ++= Seq("-doc-root-content", baseDirectory.value+"/src/main/scaladoc/overview.txt") 
```

To facilitate a common way to manage the overview file one can specify it using.  
 This will yield the exact same result as the example above
```scala
scalacOptions in (Compile, doc) ++= org.dmonix.sbt.ScalaDocSettings.rootDoc
```

```scala
copyDocAssetsTask <<= copyDocAssetsTask triggeredBy (doc in Compile)
```

### Managing static resources (e.g. images)
Javadoc has a standard _doc-files_ directory which is automatically included during the javadoc generation.  
Thus allowing the documentation author to include static content such as images or even html files.  
Scaladoc seems to completely lack this functionality.  
A simple remedy is to include the _copyDocAssetsTask_ into the build.sbt.  
The example below attaches the task to the _doc_ execution.  
It will recursively copy all the doc-files to the API output target.

```scala
copyDocAssetsTask <<= copyDocAssetsTask triggeredBy (doc in Compile)
```
