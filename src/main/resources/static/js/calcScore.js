function IsNumeric(sText) {
	var ValidChars = "0123456789";
	var IsNumber = true;
	var Char;
	for (i = 0; i < sText.length && IsNumber == true; i++) {
		Char = sText.charAt(i);
		if (ValidChars.indexOf(Char) == -1) {
			IsNumber = false;
		}
	}
	return IsNumber;
};

function calcSum(){
	var scoreSum = 0;
	$(".score-input").each(function(){
		var val = $.trim($(this).val()) || 0;
		if (IsNumeric(val) && val >= 0 && val <= 30) {
			scoreSum += parseInt(val);
		}
	});
	$("#compInteScoSum").val(scoreSum);
}

$(function(){
	$(".score-input").blur(function(){
		var $this = $(this);
		var score = $.trim($this.val());
		if (score != '' && IsNumeric(score) && score >= 0 && score <= 30) {
			$this.css("background-color", "white").val(score);
		} else {
			$this.css("background-color", "#ffdcdc").val('').parent().parent().find("span.score-error").text("请输入符合要求的分数格式");
		}
		calcSum();
	}).focus(function(){
		$("span.score-error").empty();
	});
});