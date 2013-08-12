IDE Templates used for the Eclipse and IDEA GWTP Plugins

##Reference
* [GWTP](https://github.com/ArcBees/GWTP)
* [GWTP Eclipse Plugin](https://github.com/ArcBees/gwtp-eclipse-plugin)
* [GWTP Archetypes](https://github.com/ArcBees/ArcBees-tools/tree/master/archetypes)

##Building
These are some rough directions to building this lib. 

###Repackage Velocity
[Repackage Velocity](repackage/README.md)

###Eclipse Export Jar
Export this project as a jar. In the future Maven can do this. 

1. Export jar
2. Either run eclipse export, or load the eclipse export jardesc.
3. The jar exports into target/build/ide-templates.jar.
4. Copy the jar into the Eclipse plugin lib directory. 
 
[![githalytics.com alpha](https://cruel-carlota.gopagoda.com/73e4b209b006452944fd72f4f58e438b "githalytics.com")](http://githalytics.com/ArcBees/IDE-Templates)
