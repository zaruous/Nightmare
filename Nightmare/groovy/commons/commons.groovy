import groovy.grape.Grape;
import groovy.lang.GroovyClassLoader;

GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
Thread.currentThread().setContextClassLoader(groovyClassLoader);

println "commons.groovy loaded.";
def hello(){
	return "hello";
}

def alert(def msg){
	com.kyj.fx.nightmare.comm.DialogUtil.showMessageDialog(msg);
}
/*
def browse(def url){
  if(url instanceof String)
  {
	return new WebView(url);
  }
}
*/
