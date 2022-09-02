$(document).ready(function(){
	
//Функционал кнопки Cancel
	$("#buttonCancel").on("click", function(){ 
	    window.location = moduleURL;
	});
            
//Функционал кнопки Photo 
    $("#fileImage").change(function(){
		fileSize = this.files[0].size;	
		//Проверка: файл должен быть менее  мегабайта
		if (fileSize > 1048576){
			this.setCustomValidity("You must choose n image less thn 1MB!");
			this.reportValidity();
		} else {
			this.setCustomValidity("");
			showImageThumbnail(this);	
		}	
		});
    });
        
function showImageThumbnail(fileInput){
	var file = fileInput.files[0];
	var reader = new FileReader();
	reader.onload = function(e) {
		$("#thumbnail").attr("src", e.target.result);
	};
	
	reader.readAsDataURL(file);
}