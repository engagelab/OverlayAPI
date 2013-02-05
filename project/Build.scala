import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

  val appName         = "OverlayAPI"
  val appVersion      = "1.0-SNAPSHOT"

   val appDependencies = Seq(
      	// Add your project dependencies here,
      	"leodagdag" % "play2-morphia-plugin_2.9.1" % "0.0.6",
      
        //Apache
    	"commons-io" % "commons-io" % "2.3",
    	
    	//Thumbnailer
    	"net.coobird" % "thumbnailator" % "0.4.2",
    	
    	// Twittter Bootstrap
    	"com.github.twitter" % "bootstrap" % "2.0.2",
    	
    	//ImageScaler
    	"org.imgscalr" % "imgscalr-lib" % "4.2"
    
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA)
    .settings(
     	resolvers ++= Seq(DefaultMavenRepository, Resolvers.githubRepository, Resolvers.morphiaRepository),
      	resolvers += "webjars" at "http://webjars.github.com/m2",
     	checksums := Nil 
    )

object Resolvers {
      val githubRepository = "LeoDagDag repository" at "http://leodagdag.github.com/repository/"
      val dropboxRepository = "Dropbox repository" at "http://dl.dropbox.com/u/18533645/repository/"
     val morphiaRepository = "Morphia repository" at "http://morphia.googlecode.com/svn/mavenrepo/"
   }

}
