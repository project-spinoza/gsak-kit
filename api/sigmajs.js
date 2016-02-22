var SigmaJs = function (options){

	var data,
	
	parentDivId,
	sigmaInstance,
	cameraInstance,
	currentValue,
	textFilePath,
	self = this,
	filters,
	slider,
	graphOptions;

	self.initialize = function(options){	

		
		
		parentDivId = options.parentDivId;
		currentValue = options.slider.value;
		textFilePath = options.textFilePath;
		slider =  options.slider;
		graphOptions = options.graphOptions;
		
		self.createGraphDivs();
		self.drawSlider();
		self.startProcess();

	}

	self.createGraphDivs = function(){
		
		$("#"+parentDivId).append("<div id='graphkit'></div>")
		
		$("#graphkit").css("width",$("#"+parentDivId).css("width"));
		$("#graphkit").css("height",$("#"+parentDivId).css("height"));
		if($(".gephikitSlider").length == 0) {
			$("body").append('<div class="gephikitSlider"><p><button  id="zoomIn">Zoom In</button><div id="slider-vertical" style="height:200px;"></div><button   id="zoomOut">Zoom out</button></div>');
		}
		if($(".graphOptions").length == 0) {
			$("body").append('<div class="graphOptions"> <form ><label for="Layouts">Layouts</label> <select class="layouts" name = "layouts" id="layouts"> <option value="YIfanHuLayout">YIfanHuLayout</option> <option value="ForceAtlasLayout">ForceAtlasLayout</option> <option value="FruchtermanReingold">FruchtermanReingold</option> </select> <label for="nodeSizeBy">nodeSize</label> <select class="nodeSizeBy" name = "nodeSizeBy" id="nodeSizeBy"> <option value="nc">Node Centraility</option> <option value="pr">PageRank</option> </select> <label for="nodeCentrailityThreashhold">nodeCentrailityThreashhold</label> <input type="text" name="nodeCentrailityThreashhold" id="nodeCentrailityThreashhold"> <label for="pageRankThreashhold">pageRankThreashhold</label> <input type="text" name = "pageRankThreashhold" id="pageRankThreashhold"> <label for="neighborRange">neighbor Count</label> <input type="text" name="neighborRange" id="neighborRange" > <label for="backgroundColor">backgroundColor</label> <input type="text" name="backgroundColor" id="backgroundColor"> <label for="edgeTypeBy">edgeTypeBy</label> <input type="text" name="edgeTypeBy" id="edgeTypeBy"> <label for="edgeColorBy">edgeColorBy</label> <select name = "edgeColorBy" id="edgeColorBy"> <option value="mix">mix</option> </select></form> </div> ');	
		}
		$('.gephikitSlider').css({ 
			position: "absolute",
			top: ($("#graphkit").offset().top + slider.top),
			left: ($("#graphkit").offset().left +slider.left)
		});
		if(graphOptions.display){
			$(".graphOptions").show();
		}else{
			
			$(".graphOptions").hide();
		}	

		if(slider.display){
			$(".gephikitSlider").css("display","block");
			 $("#slider-vertical").css("width",slider.width);
			 $("#slider-vertical").css("height",slider.height);
		}else{
			$(".gephikitSlider").css("display","none");
		}
		$('#graphkit,.gephikitSlider').bind('mousewheel', function(e){
			if(e.originalEvent.wheelDelta/120 > 0) {
				
				self.plusIconClick();
			}
			else{
				
				self.minusIconClick();
			}
		});
		
	}
	self.startProcess = function(){
		
		self.btnClick();
		self.ajaxRequest();
	}
	self.drawSlider = function(){
		$(function() {
			$( "#slider-vertical" ).slider({
				orientation: "vertical",
				range: "min",
				min: slider.minValue,
				max: slider.maxValue,
				value: slider.value,
				step: 1,
				slide: function( event, ui ) {
					self.sliderChange(ui.value);
					

				}
			});
			
		});
	}
	self.ajaxRequest = function(){
		
		$.ajax({
			url: "curl_request.php",
			data:{filters :  options.filters, tweets : options.tweets},

			beforeSend: function(xhr) { 
				$( "#graphkit" ).empty(); 
				$( "#slider-vertical" ).slider( "value",slider.value ) 
			},
			type: 'POST',
			success: function (data) {
                console.log(data);
				self.data = data;
				
				self.drawGraph();
			},
			error: function(){
				console.log('Error:');
			}
		});
	}
	self.btnClick = function(){
		$("#zoomIn").click(function(){
			
			self.plusIconClick();
		})
		$("#zoomOut").click(function(){
			
			self.minusIconClick();
		})
	}
	self.drawGraph = function(){
		
		sigmaInstance = new sigma({
			graph: JSON.parse(self.data),
			renderer: {
				container: 'graphkit',
				type: 'canvas'
			},
			settings: {
				enableEdgeHovering:true,
				edgeHoverColor: "#FF0000",
				defaultEdgeHoverColor: "#FF0000",
				enableCamera:true,
				zoomingRatio:2.0,
				mouseEnabled:true,
				mouseWheelEnabled:false,
			}
		});

	}

	
	self.increaseSize = function(){

		cameraInstance = sigmaInstance.camera;
		cameraRatio = (cameraInstance.ratio / (cameraInstance.settings('zoomingRatio')));
		cameraInstance.goTo({
			ratio: cameraRatio
		});
		//sigmaInstance.refresh();
	}

	
	self.decreaseSize = function(){
		cameraInstance = sigmaInstance.camera;
		cameraRatio = (cameraInstance.ratio * (cameraInstance.settings('zoomingRatio')));
		cameraInstance.goTo({
			ratio: cameraRatio
		});
     	//sigmaInstance.refresh();
     }

     self.plusIconClick = function(){
     	if(currentValue < slider.maxValue){
     		self.increaseSize();
     		currentValue++;
     		$( "#slider-vertical" ).slider( "value",currentValue )
     		
     	}
     }

     self.minusIconClick = function(){
     	if(currentValue > slider.minValue){
     		self.decreaseSize();
     		currentValue --;
     		$( "#slider-vertical" ).slider( "value",currentValue )
     		
     	}
     }

     self.zoomOut = function(sliderValue){
         // Zoom in - single frame :
         while(currentValue > sliderValue){


         	self.decreaseSize();
         	currentValue--;
         }


     }


     self.zoomIn = function(sliderValue){
		
		while(currentValue < sliderValue){
			self.increaseSize();
			currentValue++;
		}
		

	};
     self.sliderChange = function(sliderValue){

     	if(currentValue <sliderValue){

     		
     		self.zoomIn( sliderValue);
     	}else{
     		
     		self.zoomOut(sliderValue);
     	}
        
     	sigmaInstance.refresh();
     	
     }

     self.initialize(options);		
 }