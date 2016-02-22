<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>Sigma Graph</title>

  <!-- CSS Files -->
  
 
  <link rel="stylesheet" href="assets/css/jquery-ui.css">
  <style>
  #sigmagraph {

   border:1px solid red;
   height: 768px;
   width:1366px;
 }
 </style>
</head>
<body>
 <div class="container">

  <button  id="clickbtn">Search</button>




  <div id="sigmagraph">

  </div>



</div>

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="assets/js//jquery-1.12.0.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<!-- <script src="assets/js//bootstrap.min.js"></script> -->

<script src="assets/js//jquery-ui.js"></script>
<script src="assets/js/sigma.core.js"></script>
<script src="assets/js/conrad.js"></script>
<script src="assets/js/utils/sigma.utils.js"></script>
<script src="assets/js/utils/sigma.polyfills.js"></script>
<script src="assets/js/sigma.settings.js"></script>
<script src="assets/js/classes/sigma.classes.dispatcher.js"></script>
<script src="assets/js/classes/sigma.classes.configurable.js"></script>
<script src="assets/js/classes/sigma.classes.graph.js"></script>
<script src="assets/js/classes/sigma.classes.camera.js"></script>
<script src="assets/js/classes/sigma.classes.quad.js"></script>
<script src="assets/js/classes/sigma.classes.edgequad.js"></script>
<script src="assets/js/captors/sigma.captors.mouse.js"></script>
<script src="assets/js/captors/sigma.captors.touch.js"></script>
<script src="assets/js/renderers/sigma.renderers.canvas.js"></script>
<script src="assets/js/renderers/sigma.renderers.webgl.js"></script>
<script src="assets/js/renderers/sigma.renderers.svg.js"></script>
<script src="assets/js/renderers/sigma.renderers.def.js"></script>
<script src="assets/js/renderers/webgl/sigma.webgl.nodes.def.js"></script>
<script src="assets/js/renderers/webgl/sigma.webgl.nodes.fast.js"></script>
<script src="assets/js/renderers/webgl/sigma.webgl.edges.def.js"></script>
<script src="assets/js/renderers/webgl/sigma.webgl.edges.fast.js"></script>
<script src="assets/js/renderers/webgl/sigma.webgl.edges.arrow.js"></script>
<script src="assets/js/renderers/canvas/sigma.canvas.labels.def.js"></script>
<script src="assets/js/renderers/canvas/sigma.canvas.hovers.def.js"></script>
<script src="assets/js/renderers/canvas/sigma.canvas.nodes.def.js"></script>
<script src="assets/js/renderers/canvas/sigma.canvas.edges.def.js"></script>
<script src="assets/js/renderers/canvas/sigma.canvas.edges.curve.js"></script>
<script src="assets/js/renderers/canvas/sigma.canvas.edges.arrow.js"></script>
<script src="assets/js/renderers/canvas/sigma.canvas.edges.curvedArrow.js"></script>
<script src="assets/js/renderers/canvas/sigma.canvas.edgehovers.def.js"></script>
<script src="assets/js/renderers/canvas/sigma.canvas.edgehovers.curve.js"></script>
<script src="assets/js/renderers/canvas/sigma.canvas.edgehovers.arrow.js"></script>
<script src="assets/js/renderers/canvas/sigma.canvas.edgehovers.curvedArrow.js"></script>
<script src="assets/js/renderers/canvas/sigma.canvas.extremities.def.js"></script>
<script src="assets/js/renderers/svg/sigma.svg.utils.js"></script>
<script src="assets/js/renderers/svg/sigma.svg.nodes.def.js"></script>
<script src="assets/js/renderers/svg/sigma.svg.edges.def.js"></script>
<script src="assets/js/renderers/svg/sigma.svg.edges.curve.js"></script>
<script src="assets/js/renderers/svg/sigma.svg.labels.def.js"></script>
<script src="assets/js/renderers/svg/sigma.svg.hovers.def.js"></script>
<script src="assets/js/middlewares/sigma.middlewares.rescale.js"></script>
<script src="assets/js/middlewares/sigma.middlewares.copy.js"></script>
<script src="assets/js/misc/sigma.misc.animation.js"></script>
<script src="assets/js/misc/sigma.misc.bindEvents.js"></script>
<script src="assets/js/misc/sigma.misc.bindDOMEvents.js"></script>
<script src="assets/js/misc/sigma.misc.drawHovers.js"></script>


<script src="sigmajs.js"></script>



<script>
var sigmajs;
var   options;


$("#clickbtn").click(function(e) {

 event.preventDefault();
 var nodeSizeBy = $("#nodeSizeBy").val();
 var nodeCentrailityThreashhold = (!$("#nodeCentrailityThreashhold").val())? 30 : $("#nodeCentrailityThreashhold").val();
 var pageRankThreashhold = (!$("#pageRankThreashhold").val())? 30 : $("#pageRankThreashhold").val();
 var neighborRange = (!$("#neighborRange").val() )? 30 : $("#neighborRange").val();
 var backgroundColor = $("#backgroundColor").val();
 var edgeTypeBy = $("#edgeTypeBy").val();
 var edgeColorBy = $("#edgeColorBy").val();
 var selectedLayout = $("#layouts").val();
  
 filters = {
   nodeSizeBy:nodeSizeBy,
   nodeCentrailityThreashhold:nodeCentrailityThreashhold,
   pageRankThreashhold:pageRankThreashhold,
   neighborRange:neighborRange,
   backgroundColor:backgroundColor,
   edgeTypeBy:edgeTypeBy,
   edgeColorBy:edgeColorBy,
   selectedLayout:selectedLayout,



 };

 
 options = {

  parentDivId:'sigmagraph',
  filters:filters,
  slider:{
    display:true,
    width:10,
    height:200,
    top:10,
    left:100,
    minValue: 0,
    maxValue : 10,
    value : 5
  },
  graphOptions:{
    display:true
  }


};


sigmajs = new SigmaJs(options);




});


</script>
</body>
</html>