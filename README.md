
# GSAK-KIT 

This project provides a plugin for graph analysis and visualization through which you can easily embed, visualize, and analyze your data on any website or web page.<br>

# Building the Project:

#### Prerequisits: 
  * Apache Maven 3.x <br>
  * Java 8 <br>
  * Git (Optional)<br>

#### Steps 
  * Download or clone Gsak-Kit from github into any convenient Directory (Path should not contain special Characters like spaces etc. <br>
  * Go to Project root directory (Directory containing src, pom.xml) <br>
  * Execut the following commands<br>
      <code>mvn compile</code><br>
      <code>mvn clean package</code><br>
  * If compilation completed without any errors, a <code>target</code> will be created inside project root directory. <br>
  * The <code>target</code> directory should contain a jar file with possible name <code>gsak-kit-with-dependencies.jar</code>.<br>
  * To deploy gsak-kit server, run the following command inside <code>target</code> directory.<br>
      <code>java -jar gsak-kit-with-dependencies.jar -config configuration.json</code><br>
  * Open <a href="localhost:8182/gephi">localhost:8182/gephi</a> in browser.<br>
  * A sigma graph will be generated from the mentioned file data in <code>configuration.json</code> in the browser response.<br>
  * 
  
# Routes:
  Routes define different processing that can be applied to the visulized graph. Standard processes include Layouts, Filtering or combination of both.
#### Layouts
  * <bold>Route<bold>: <code>/layout</code> <br>
  * <bold>Layout ID:<bold> <code>settings="name"=xyz</code> <br>
  
GSAK-KIT is divided into two sections.
#### The server side 
  * **input** <br>Text data or the built-in graph<br>
  * **output**<br>Processed graph<br>
  * **Description** <br>
 The server side will do all the data processing, graph generation, analysis, filteration, etc.
 When the server is provided with text data, it generates a graph based on specific keywords or relationships that exist   within the data.
 When the server is provided with the built-in graph, it will analyze and filter the graph to create another processed graph.  The server specifies formats for the input data. From this, the server generates the graph and provides a way to write your  own implementation code for the graph generator. <br>
  * **Third Party Libraries**<br> 
For graph processing we'll use the [GephiToolkit](https://gephi.org/toolkit/) <br>
For java web server we'll use the [Undertow](https://github.com/undertow-io/undertow) <br>
For logging we'll use [log4j2](https://github.com/logentries/le_java/wiki/Log4j2)

#### The front end side 
  * **input** <br> Processed graph data <br> 
  * **output** <br> Sigma graph <br>
  * **Description** <br> The front end will provide a way for visualizing the graph generated by the server. The front end will also provide a way to use the graph services in a webpage. <br>
  * **Third Party Libraries** <br> For front end visualization we'll use the [SigmaJs](http://sigmajs.org/) <br><br>
  The front-end visualization is completely independent of the backend graph generation/processing, it will enable users to design their own front-end layouts and to use graph services where they need.
  
## License
The code is licensed under the [Apache License Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)
<br>
## Road Map
https://github.com/project-spinoza/gsak-kit/wiki

## Questions or Suggestions
**Email** `project.spinoza@gmail.com`<br>
**Issues** https://github.com/project-spinoza/gsak-kit/issues
  
